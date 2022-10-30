'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$routeParams','$location',
										 'memberService','orderListService',
										 'OrderStatusArray','cartService','OrderDO','OrderDetailDO','ajaxService',
	function($routeParams,$location,memberService,orderListService,
	            OrderStatusArray,cartService,OrderDO,OrderDetailDO,ajaxService) {
	var self = this;
	//self.orderDetailList = new Array(3).fill(new OrderDetailDO(false));
	//self.orderDetailList.unshift(new OrderDetailDO(true));

	self.DPisOpen = false;
	self.couponDiscount = 0; //%
    self.couponCode = '';

//	self.orderDetailList = [new OrderDetailDO()];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "120px" };

//////////////// function section ////////////

    self.add1Tab = function(){
        self.theOrder.orderDetails.push(new OrderDetailDO());
        console.log(self.theOrder);
    }

    self.remove1Tab = function(){
         if(self.theOrder.orderDetails.length > 1){
            self.theOrder.orderDetails.pop();
        }
    }

    self.nameCopy = function(){
        self.theOrder.orderDetails[0].name = self.theOrder.shippingName;
    }

    self.phoneCopy = function(){
        self.theOrder.orderDetails[0].phone = self.theOrder.shippingPhone;
    }

    self.addressCopy = function(){
        self.theOrder.orderDetails[0].address = self.theOrder.shippingAddress;
    }

    // open datePicker
    self.openDP = function() {
        self.DPisOpen = true;
        self.isPickDP = true;
    };

    self.querySearch = function(searchText){
        if(searchText){
            var url = "search/product/"+searchText;
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });

        }else{
            return {id:0,name:'no result',type:1,image:''};
        }
    }

    self.searchTextChange =function(text) {
        //console.log('Text changed to ' + text);
    }

    self.selectedItemChange = function(one,orderDetail) {
        if(one){
          console.log('selectedItemChange in if');
          orderDetail.product = one;
          orderDetail.frameNote = one.name;
         // orderDetail.framePriceAtThatTime = one.name;
          orderDetail.framePriceAfterSale = orderDetail.product.sellPrice*(100 - orderDetail.product.discount)/100 * orderDetail.quantity;
        }
        self.calculateOrderTotal();
    }

    self.removeSearchResult = function(orderDetail){
        orderDetail.product = null;
        self.calculateOrderTotal();
    }

    self.calculateOrderTotal = function(){
        var subTotal = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            if(self.theOrder.orderDetails[i].product){
                self.theOrder.orderDetails[i].framePriceAtThatTime = self.theOrder.orderDetails[i].product.sellPrice;
                self.theOrder.orderDetails[i].frameDiscountAtThatTime = self.theOrder.orderDetails[i].product.discount;
            }else{
                self.theOrder.orderDetails[i].framePriceAtThatTime = self.theOrder.orderDetails[i].framePriceAfterSale;
                self.theOrder.orderDetails[i].frameDiscountAtThatTime = 0;
            }
            subTotal += self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100*self.theOrder.orderDetails[i].quantity + self.theOrder.orderDetails[i].lensPrice;
        }
    //  self.theOrder.statusName = OrderStatusArray.find(i => i.value == self.theOrder.status).name;
        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount;
    }

    self.getCoupon = function(code) {
        if(code ==''){
            self.isErrorMsg ='Cần nhập coupon code.';
            return;
        }
         cartService.getCoupon(code).then(function (data) {

         if(data.errorCode == 'SUCCESS'){
            self.theOrder.couponDiscount = data.replyStr;
            self.theOrder.couponCode = code;
          //  self.isCouponApplied = true;
            self.calculateOrderTotal(self.theOrder);
            self.isErrorMsg = false;
            console.log(self.theOrder);
         }else{
            self.isErrorMsg = data.errorMessage;
         }
         });
    }

    self.saveOrder = function(){
        console.log(self.theOrder);
        if(self.isPickDP){
            self.theOrder.gmtModify = self.theOrder.gmtCreate;
            for (var i = 0; i < self.theOrder.orderDetails.length; i++){
                self.theOrder.orderDetails[i].gmtCreate = self.theOrder.gmtCreate;
                self.theOrder.orderDetails[i].gmtModify = self.theOrder.gmtCreate;
            }
        }

        if(self.theOrder.shippingName && self.theOrder.shippingPhone ){
            if(memberService.isAdmin()){
                cartService.placeOrder(self.theOrder).then(function (data) {
                    self.order_return_status = data; // return after saving order, order_return_status would be orderid
                    self.newOrderId = data.replyStr;
                    console.log(self.theOrder);
                });
            }
        }else{
            self.isErrorMsg ='Cần nhập tên/số điện thoại.';
        }

    }

    self.calculateFramePriceAfterSale = function(orderDetail){
        orderDetail.framePriceAfterSale = orderDetail.framePriceAtThatTime*(100 - orderDetail.frameDiscountAtThatTime)/100 * orderDetail.quantity;
    }

////// run when loading page/////

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	 // load product
    if($routeParams.orderId > 0){
        orderListService.getOrderById($routeParams.orderId)
            .then(function (data) {
                self.theOrder = data;
                console.log(self.theOrder);
                if(self.theOrder.orderDetails.length > 0){
                    self.theOrder.orderDetails.forEach(self.calculateFramePriceAfterSale);
                    self.calculateOrderTotal(self.theOrder);
                }
        });
    }else{
        self.theOrder = new OrderDO;
        self.theOrder.location='STORE';
        self.theOrder.orderDetails = [new OrderDetailDO()];
    }



}]);