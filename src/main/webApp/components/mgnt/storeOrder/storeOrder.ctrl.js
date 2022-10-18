'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$rootScope','$routeParams','$location',
										 'memberService','storeOrderService','orderListService',
										 'OrderStatusArray','cartService','OrderDO','OrderDetailDO',
	function($rootScope, $routeParams,$location,memberService,storeOrderService,orderListService,OrderStatusArray,cartService,OrderDO,OrderDetailDO) {
	var self = this;
	//self.orderDetailList = new Array(3).fill(new OrderDetailDO(false));
	//self.orderDetailList.unshift(new OrderDetailDO(true));
	self.orderDetailList = [new OrderDetailDO()];

	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "120px" };

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

    self.add1Tab = function(){
        self.orderDetailList.push(new OrderDetailDO());
    }

    self.remove1Tab = function(){
        self.orderDetailList.pop();
    }

	console.log(self.orderDetailList);
	
	 // load product
    if($routeParams.orderId > 0){
        productUpsertService.getProductById($routeParams.orderId)
            .then(function (data) {
                self.theOrder = data;
        });
    }else{
        self.theOrder = new OrderDO;
    }



	

	




	
}]);