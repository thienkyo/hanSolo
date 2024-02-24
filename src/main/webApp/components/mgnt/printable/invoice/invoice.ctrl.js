'use strict';
angular.module('invoiceModule')
	.controller('invoiceController',['$routeParams','$location','invoiceService','orderCacheService',
	function($routeParams,$location,invoiceService,orderCacheService) {
	var self = this;
	self.OrderDetailList=[];
	function MiniOrderDetailDO (price,quantity,description,discount) {
    	this.price = price;
    	this.quantity = quantity;
    	this.discount = discount;
    	this.description = description;
    }
    self.paramValue = $location.search();
    if(orderCacheService.getOneOrder(self.paramValue.orderId)){
        self.addDetailToBill = orderCacheService.getOneOrder(self.paramValue.orderId).addDetailToBill;
    }
    invoiceService.getOneOrder(self.paramValue.orderId)
        .then(function (data) {
            self.theOrder = data.obj;
            self.calculateOrderTotal();
    });


    self.calculateOrderTotal = function(){
        var subTotal = 0;
        var temp = 0;
        var count = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            self.theOrder.orderDetails[i].framePriceAfterSale =
                self.theOrder.orderDetails[i].framePriceAtThatTime
                *(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100;

            temp = self.theOrder.orderDetails[i].framePriceAfterSale
                   *self.theOrder.orderDetails[i].quantity;
            // apply discount
            if(self.theOrder.orderDetails[i].frameDiscountAmount && self.theOrder.orderDetails[i].frameDiscountAmount > 0){
                temp = temp*(100 - self.theOrder.orderDetails[i].frameDiscountAmount)/100
            }
            subTotal += temp
                        +self.theOrder.orderDetails[i].lensPrice
                        *self.theOrder.orderDetails[i].lensQuantity
                        *(100 - self.theOrder.orderDetails[i].lensDiscountAmount)/100
                        + self.theOrder.orderDetails[i].otherPrice;

            if(self.theOrder.orderDetails[i].lensPrice > 0 ){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].lensPrice,
                                                                 self.theOrder.orderDetails[i].lensQuantity,
                                                                 self.buildDescription(self.addDetailToBill,self.theOrder.orderDetails[i].name,self.theOrder.orderDetails[i].lensNote),
                                                                 self.theOrder.orderDetails[i].lensDiscountAmount));
            }

            if(self.theOrder.orderDetails[i].framePriceAfterSale > 0){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].framePriceAfterSale,
                                                                 self.theOrder.orderDetails[i].quantity,
                                                                 self.buildDescription(self.addDetailToBill,self.theOrder.orderDetails[i].name,self.theOrder.orderDetails[i].frameNote),
                                                                 self.theOrder.orderDetails[i].frameDiscountAmount));
            }

            if(self.theOrder.orderDetails[i].lensPrice && self.theOrder.orderDetails[i].otherPrice > 0 ){
                self.OrderDetailList.push(new MiniOrderDetailDO (self.theOrder.orderDetails[i].otherPrice,
                                                                 1,
                                                                 self.theOrder.orderDetails[i].otherNote,
                                                                 0));
            }
            if(self.theOrder.orderDetails[i].frameNote.length > 31){count++;}
            if(self.theOrder.orderDetails[i].lensNote.length  > 31){count++;}
            if(self.theOrder.orderDetails[i].otherNote.length > 31){count++;}
        }

        var totalLine = 14;


        console.log(count);

        var filler = totalLine - self.OrderDetailList.length - count;
        for (var i = 0; i < filler; i++){
            self.OrderDetailList.push(new MiniOrderDetailDO());
        }
        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount - self.theOrder.customDiscountAmount;
        self.theOrder.remain = subTotal - self.theOrder.couponAmount - self.theOrder.customDiscountAmount - self.theOrder.deposit;
        self.theOrder.doubleLine = count;
    }


    self.buildDescription = function(isDo, name, note){
        if (!name || name == '') { return note; }
        var shortName = name;
        if(name.split(' ').length >= 2){
            shortName = name.split(' ')[name.split(' ').length-2] +' '+name.split(' ')[name.split(' ').length-1];
        }
        return isDo ? note+" "+ shortName : note;
    }
}]);