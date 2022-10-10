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
	self.statusNumber = {"ordered":0, "paid":0,"shipped":0, "done":0};
	/*self.statusNumber.ordered = 0;
	self.statusNumber.paid = 0;
	self.statusNumber.shipped = 0;
	self.statusNumber.done = 0;*/
	
	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = 50;

	
	orderListService.getOrdersForMgnt(self.amount).then(function (data) {
		self.orderList = data;
		self.orderList.forEach(calculateOrderTotal);

	    console.log(data);
	    console.log(self.orderList);
	//	engineerOrderList();
		self.tableParams = new NgTableParams({}, { dataset: self.orderList});
	});
/*	
	 function calculateTotal(order){
		var total = 0;
		for(var k = 0; k < order.orderDetails.length; k++){
			total += order.orderDetails[k].priceAtThatTime*order.orderDetails[k].quantity;
		}
		total += order.shipCostFee;
		return total;
	}
*/	 
	self.getOrderStatusName = function(value){
		for(var k = 0; k < OrderStatusArray.length; k++){
			if(OrderStatusArray[k].value == value){
				return OrderStatusArray[k].name;
				break;
			}
		}
	}
	
	self.updateOrderStatus = function(){
		orderListService.updateOrderStatus(self.theOrder.orderId,self.newOrderStatus).then(function(data){
			self.responseStr = data.replyStr;
			self.theOrder.status = self.newOrderStatus;
			engineerOrderList();
		});
		
	}
	
	self.getOrderbyTerm = function(){
		orderListService.getOrdersForMgnt(self.amount).then(function (data) {
			self.orderList = data;;
			engineerOrderList();
			self.tableParams = new NgTableParams({}, { dataset: self.orderList});
		});
	}
	
	self.setStyle = function(status){
		if(status==20){
			self.statusStyle.color = "limegreen";
		}else if(status==21){
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
		self.newOrderStatus = order.status;
		self.theOrder = order;
		
		self.theOrder.subTotal = 0;
		self.weight = 0;
		for (var i = 0; i < self.theOrder.orderDetails.length; i++){
			self.weight += self.theOrder.orderDetails[i].weight*self.theOrder.orderDetails[i].quantity;
			self.theOrder.subTotal += self.theOrder.orderDetails[i].priceAtThatTime*self.theOrder.orderDetails[i].quantity;
		}
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
		
		for(var i = 0; i < self.orderList.length; i++){
			if(self.orderList[i].status == 0){
				self.statusNumber.ordered += 1;
			}
			if(self.orderList[i].status == 1){
				self.statusNumber.paid += 1;
			}
			if(self.orderList[i].status == 2){
				self.statusNumber.shipped += 1;
			}
			if(self.orderList[i].status == 3){
				self.statusNumber.done += 1;
			}
		}
	}

	function calculateOrderTotal(order){
        var subTotal = 0;
        for (var i = 0; i < order.orderDetails.length; i++){
            subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity;
        }
        order.status = OrderStatusArray.find(i => i.value == order.status).name;
        order.subTotal = subTotal;
        order.couponAmount = subTotal*order.couponDiscount/100;
        order.total = subTotal - order.couponAmount;

        self.statusNumber.ordered = 0;
        self.statusNumber.paid = 0;
        self.statusNumber.shipped = 0;
        self.statusNumber.done = 0;
        self.statusNumber.shopDelete = 0;
        self.statusNumber.userDelete = 0;

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