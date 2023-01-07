'use strict';
angular.module('invoiceModule')
	.controller('invoiceController',['$routeParams','$location','invoiceService',
	function($routeParams,$location,invoiceService) {
	var self = this;
	self.OrderDetailList=[];
	function MiniOrderDetailDO (price,quantity,description) {
    	this.price = price;
    	this.quantity = quantity;
    	this.description = description;
    }

    var paramValue = $location.search();

    invoiceService.getOneOrder(paramValue.orderId)
        .then(function (data) {
            self.theOrder = data;
            self.calculateOrderTotal();
    });


    self.calculateOrderTotal = function(){
        var subTotal = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            self.theOrder.orderDetails[i].framePriceAfterSale = self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100 * self.theOrder.orderDetails[i].quantity;
            subTotal += self.theOrder.orderDetails[i].framePriceAfterSale + self.theOrder.orderDetails[i].lensPrice;
            if(self.theOrder.orderDetails[i].framePriceAfterSale > 0){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].framePriceAfterSale,1,self.theOrder.orderDetails[i].frameNote));
            }

            if(self.theOrder.orderDetails[i].lensPrice > 0 ){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].lensPrice,1,self.theOrder.orderDetails[i].lensNote));
            }
        }
        var temp = 12 - self.OrderDetailList.length;
        for (var i = 0; i < temp; i++){
            self.OrderDetailList.push(new MiniOrderDetailDO());
        }


        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount;
        self.theOrder.remain = subTotal - self.theOrder.couponAmount - self.theOrder.deposit;
    }
}]);