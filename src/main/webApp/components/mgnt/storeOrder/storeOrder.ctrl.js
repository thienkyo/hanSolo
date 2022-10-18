'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$rootScope','$routeParams','$location',
										 'memberService','storeOrderService',
										 'NgTableParams','OrderStatusArray','cartService','AmountList',
	function($rootScope, $routeParams,$location,memberService,storeOrderService,NgTableParams,OrderStatusArray,cartService,AmountList) {
	var self = this;
	self.orderList = [];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "120px" };
	self.statusNumber = {"ordered":0, "paid":0,"shipped":0, "done":0, "shopDelete":0, "userDelete":0};

	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = 50;



	

	

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
		self.theOrder = order;
	}
	
	self.deleteOrder = function(order){
		orderListService.deleteOrder(order).then(function (data) {
			self.responseStr = data;
		});
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
            subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity;
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