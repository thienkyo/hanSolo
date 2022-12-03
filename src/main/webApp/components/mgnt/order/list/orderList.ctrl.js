'use strict';
angular.module('orderListModule')
	.controller('orderListController',['$rootScope','$routeParams','$location',
										 'memberService','orderListService','customerSourceService',
										 'NgTableParams','OrderStatusArray','cartService','AmountList',
	function($rootScope, $routeParams,$location,
	        memberService,orderListService,customerSourceService,
	        NgTableParams,OrderStatusArray,cartService,AmountList) {
	var self = this;
	self.orderList = [];
	self.cusSourceList = [];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "80px" };
	self.statusNumber = {"ordered":0, "paid":0,"shipped":0, "done":0, "deposit":0, "userDelete":0};
	self.isUpdatingOrder = false; // disable/able the select for update order status
	self.showLoadingText = true; // disable/able Loading..
	
	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = 100;

	customerSourceService.getAll().then(function (data) {
        self.cusSourceList = data;
       // console.log(self.cusSourceList);
        self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});
    });

	orderListService.getOrdersForMgnt(self.amount).then(function (data) {
		self.orderList = data;
		self.orderList.forEach(calculateOrderTotal);
	    console.log(self.orderList);
		self.tableParams = new NgTableParams({}, { dataset: self.orderList});
		self.showLoadingText = false;
	});
	
	self.updateOrderStatus = function(order){
	    order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
	    self.isUpdatingOrder = true;
		orderListService.updateOrderStatus(order).then(function(data){
			self.responseStr = data.replyStr;
			self.isUpdatingOrder = false;
		});
	}

	self.updateCusSource = function(order){
        self.isUpdatingOrder = true;
        orderListService.updateCusSource(order).then(function(data){
            self.responseStr = data.replyStr;
            self.isUpdatingOrder = false;
        });
    }

	self.deleteOrder = function(order){
        self.responseStr = false;
        self.responseStrFail = false;
        orderListService.deleteOrder(order).then(function (data) {
            self.responseStr = data;
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
			engineerOrderList();
		});
	}
	
	self.setStyle = function(status){
		if(status==0){
			self.statusStyle.color = "limegreen";
		}else if(status==1){
			self.statusStyle.color = "blue";
		}else if(status==4){
            self.statusStyle.color = "brown";
        }
		else{
			self.statusStyle = { "width": "80px" }
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
        self.statusNumber.deposit = 0;
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
                  self.statusNumber.deposit += 1;
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
        order.frameNumber = 0;
        order.lensNumber = 0;
        for (var i = 0; i < order.orderDetails.length; i++){
            subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity + order.orderDetails[i].lensPrice;
            if(order.orderDetails[i].framePriceAtThatTime > 1000){
                order.frameNumber +=1;
            }
            if(order.orderDetails[i].lensPrice > 1000){
                order.lensNumber +=1;
            }
        }
        order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
        order.subTotal = subTotal;
        order.currentCusSource = order.cusSource;
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
              self.statusNumber.deposit += 1;
              break;
          case 5:
              self.statusNumber.userDelete += 1;
              break;
          default:
        }

    }
	
}]);