'use strict';
angular.module('orderListModule')
	.controller('orderListController',['$rootScope','$routeParams','$location',
										 'memberService','orderListService',
										 'NgTableParams','OrderStatusArray','cartService','AmountList',
	function($rootScope, $routeParams,$location,memberService,orderListService,NgTableParams,OrderStatusArray,cartService,AmountList) {
	var self = this;
	self.orderList = [];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "120px" };
	self.statusNumber = {"ordered":0, "paid":0,"shipped":0, "done":0, "shopDelete":0, "userDelete":0};
	self.isUpdatingOrderStatus = false; // disable/able the select for update order status
	
	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = 50;

	
	orderListService.getOrdersForMgnt(self.amount).then(function (data) {
		self.orderList = data;
		self.orderList.forEach(calculateOrderTotal);
		self.tableParams = new NgTableParams({}, { dataset: self.orderList});
	});
	
	self.updateOrderStatus = function(order){
	    order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
	    self.isUpdatingOrderStatus = true;
	    setTimeout(function(){ console.log("After 5 seconds!"); }, 5000);
		orderListService.updateOrderStatus(order).then(function(data){
			self.responseStr = data.replyStr;
			self.isUpdatingOrderStatus = false;
			//engineerOrderList();
		});
		
	}

	self.deleteOrder = function(order){
        self.responseStr = false;
        self.responseStrFail = false;
        orderListService.deleteOrder(order).then(function (data) {
            self.responseStr = data;
            console.log(data);
            var index = self.orderList.indexOf(order);
            self.orderList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.orderList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }
	
	self.getOrderByTerm = function(){
		orderListService.getOrdersForMgnt(self.amount).then(function (data) {
			self.orderList = data;
			self.orderList.forEach(calculateOrderTotal);
			self.tableParams = new NgTableParams({}, { dataset: self.orderList});
		});
	}
	
	self.setStyle = function(status){
		if(status==0){
			self.statusStyle.color = "limegreen";
		}else if(status==1){
			self.statusStyle.color = "blue";
		}
		else{
			self.statusStyle = { "width": "120px" }
		}
		return self.statusStyle;
	}

	self.showOrderDetail = function(order){
		if(self.responseStr || self.responseStrFail){
			self.responseStr = false;
		}
		if(order.location == 'STORE'){
		    var url = '#/mgnt/storeOrder/'+order.id;
            window.open(url, '_blank');
		}else{
		    self.theOrder = order;
		}
	}

    self.promptDelete = function(orderId){

        self.deletingOrderId = self.deletingOrderId ? false : orderId;
    }
    self.resetDelete = function(){
        self.deletingOrderId = false;
    }
	
	function engineerOrderList(){
		self.statusNumber.ordered = 0;
        self.statusNumber.paid = 0;
        self.statusNumber.shipped = 0;
        self.statusNumber.done = 0;
        self.statusNumber.shopDelete = 0;
        self.statusNumber.userDelete = 0;
		
		for(var i = 0; i < self.orderList.length; i++){

	        switch(self.orderList[i].status) {
              case 0:
                  self.statusNumber.ordered += 1;
                  break;
              case 1:
                  self.statusNumber.paid += 1;
                  break;
              case 2:
                  self.statusNumber.shipped += 1;
                  break;
              case 3:
                  self.statusNumber.done += 1;
                  break;
              case 4:
                  self.statusNumber.shopDelete += 1;
                  break;
              case 5:
                  self.statusNumber.userDelete += 1;
                  break;
              default:
            }
		}
	}

	function calculateOrderTotal(order){
        var subTotal = 0;
        for (var i = 0; i < order.orderDetails.length; i++){
            subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity + order.orderDetails[i].lensPrice;
        }
        order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
        order.subTotal = subTotal;
        order.couponAmount = subTotal*order.couponDiscount/100;
        order.total = subTotal - order.couponAmount;

        switch(order.status) {
          case 0:
              self.statusNumber.ordered += 1;
              break;
          case 1:
              self.statusNumber.paid += 1;
              break;
          case 2:
              self.statusNumber.shipped += 1;
              break;
          case 3:
              self.statusNumber.done += 1;
              break;
          case 4:
              self.statusNumber.shopDelete += 1;
              break;
          case 5:
              self.statusNumber.userDelete += 1;
              break;
          default:
        }

    }
	
}]);