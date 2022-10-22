'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$rootScope','$routeParams','$location',
										 'memberService','storeOrderService','orderListService',
										 'OrderStatusArray','cartService','OrderDO','OrderDetailDO','ajaxService',
	function($rootScope, $routeParams,$location,memberService,storeOrderService,orderListService,
	            OrderStatusArray,cartService,OrderDO,OrderDetailDO,ajaxService) {
	var self = this;
	//self.orderDetailList = new Array(3).fill(new OrderDetailDO(false));
	//self.orderDetailList.unshift(new OrderDetailDO(true));

	self.DPisOpen = false;

	self.orderDetailList = [new OrderDetailDO()];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "120px" };

//////////////// function section ////////////

    self.add1Tab = function(){
        self.theOrder.orderDetails.push(new OrderDetailDO());
        console.log(self.theOrder);
    }

    self.remove1Tab = function(){
        self.theOrder.orderDetails.pop();
    }

    // open datePicker
    self.openDP = function() {
        self.DPisOpen = true;
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
          orderDetail.product = one;
          orderDetail.frameNote = one.name;
          orderDetail.framePriceAfterSale = orderDetail.product.sellPrice*(100 - orderDetail.product.discount)/100 * orderDetail.quantity;
        }else{
            orderDetail.product = null;
        }
    }

    self.removeSearchResult = function(orderDetail){
        orderDetail.product = null;
    }

    self.calculateOrderTotal = function(order){

        var subTotal = 0;
        for (var i = 0; i < order.orderDetails.length; i++){

            subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity;
        }
    //    order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
        order.subTotal = subTotal;
        order.couponAmount = subTotal*order.couponDiscount/100;
        order.total = subTotal - order.couponAmount;
    }

////// run when loading page/////

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	 // load product
    if($routeParams.orderId > 0){
        productUpsertService.getProductById($routeParams.orderId)
            .then(function (data) {
                self.theOrder = data;
        });
    }else{
        self.theOrder = new OrderDO;
        self.theOrder.location='STORE';
        self.theOrder.orderDetails = [new OrderDetailDO()];
    }
    console.log(self.theOrder);

    if(self.theOrder.orderDetails.length > 0){
        self.calculateOrderTotal(self.theOrder);
    }



	
}]);