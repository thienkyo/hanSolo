'use strict';
angular.module('invoiceModule')
	.controller('invoiceController',['$routeParams','$location','invoiceService',
	function($routeParams,$location,invoiceService) {
	var self = this;
	self.OrderDetailList=[];
	function MiniOrderDetailDO (price,quantity,description,discount) {
    	this.price = price;
    	this.quantity = quantity;
    	this.discount = discount;
    	this.description = description;
    }
    self.paramValue = $location.search();

    invoiceService.getOneOrder(self.paramValue.orderId)
        .then(function (data) {
            self.theOrder = data.obj;
            console.log(self.theOrder);
            self.calculateOrderTotal();
    });


    self.calculateOrderTotal = function(){
        var subTotal = 0;
        var temp = 0;
        var count = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            self.theOrder.orderDetails[i].framePriceAfterSale = self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100;

            temp = self.theOrder.orderDetails[i].framePriceAfterSale;
            // apply discount
            if(self.theOrder.orderDetails[i].frameDiscountAmount && self.theOrder.orderDetails[i].frameDiscountAmount > 0){
                temp = self.theOrder.orderDetails[i].framePriceAfterSale*self.theOrder.orderDetails[i].quantity*
                       (100 - self.theOrder.orderDetails[i].frameDiscountAmount)/100
            }
            subTotal += temp +
                        self.theOrder.orderDetails[i].lensPrice*self.theOrder.orderDetails[i].lensQuantity*
                        (100 - self.theOrder.orderDetails[i].lensDiscountAmount)/100
                        + self.theOrder.orderDetails[i].otherPrice;

            if(self.theOrder.orderDetails[i].lensPrice > 0 ){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].lensPrice,
                                                                 self.theOrder.orderDetails[i].lensQuantity,
                                                                 self.theOrder.orderDetails[i].lensNote,
                                                                 self.theOrder.orderDetails[i].lensDiscountAmount));
            }

            if(self.theOrder.orderDetails[i].framePriceAfterSale > 0){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].framePriceAfterSale,
                                                                 self.theOrder.orderDetails[i].quantity,
                                                                 self.theOrder.orderDetails[i].frameNote,
                                                                 self.theOrder.orderDetails[i].frameDiscountAmount));
            }

            if(self.theOrder.orderDetails[i].lensPrice && self.theOrder.orderDetails[i].otherPrice > 0 ){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].otherPrice,
                                                                 1,
                                                                 self.theOrder.orderDetails[i].otherNote,
                                                                 0));
            }
            if(self.theOrder.orderDetails[i].frameNote.length >= 23){count++;}
            if(self.theOrder.orderDetails[i].lensNote.length  >= 24){count++;}
            if(self.theOrder.orderDetails[i].otherNote.length >= 24){count++;}
        }

        console.log(count);
        var totalLine = 10;
        if(count < 3){
            totalLine = 12;
        }
        if(count > 5){
            totalLine = 9;
        }

        console.log(self.OrderDetailList);

        var temp = totalLine - self.OrderDetailList.length;
        for (var i = 0; i < temp; i++){
            self.OrderDetailList.push(new MiniOrderDetailDO());
        }
        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount;
        self.theOrder.remain = subTotal - self.theOrder.couponAmount - self.theOrder.deposit;
        self.theOrder.doubleLine = count;
    }
}]);