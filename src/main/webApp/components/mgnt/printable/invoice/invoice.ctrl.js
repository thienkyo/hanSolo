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
    console.log(paramValue.orderId);

    invoiceService.getOneOrder(paramValue.orderId)
        .then(function (data) {
            self.theOrder = data;
            self.calculateOrderTotal();
            console.log(self.theOrder);
            console.log(self.OrderDetailList);
    });


    self.calculateOrderTotal = function(){
        var subTotal = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            self.theOrder.orderDetails[i].framePriceAfterSale = self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100 * self.theOrder.orderDetails[i].quantity;
            subTotal += self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100*self.theOrder.orderDetails[i].quantity + self.theOrder.orderDetails[i].lensPrice;
            if(self.theOrder.orderDetails[i].framePriceAfterSale > 0){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].framePriceAfterSale,1,self.theOrder.orderDetails[i].frameNote));
            }

            if(self.theOrder.orderDetails[i].lensPrice > 0 ){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].lensPrice,1,self.theOrder.orderDetails[i].lensNote));
            }
        }
        console.log(self.OrderDetailList.length);
        var temp = 10 - self.OrderDetailList.length;
        for (var i = 0; i < temp; i++){
            self.OrderDetailList.push(new MiniOrderDetailDO());
        }


        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount - self.theOrder.deposit;
    }
}]);