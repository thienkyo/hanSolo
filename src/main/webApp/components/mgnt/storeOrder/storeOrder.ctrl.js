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

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

    self.add1Tab = function(){
        self.orderDetailList.push(new OrderDetailDO());
        console.log(self.orderDetailList);
    }

    self.remove1Tab = function(){
        self.orderDetailList.pop();
    }

    self.openDP = function() {
        self.DPisOpen = true;
      };

	//console.log(self.orderDetailList);
	
	 // load product
    if($routeParams.orderId > 0){
        productUpsertService.getProductById($routeParams.orderId)
            .then(function (data) {
                self.theOrder = data;
        });
    }else{
        self.theOrder = new OrderDO;
        console.log(self.theOrder);
    }


    self.querySearch = function(searchText){
        if(searchText){
            var url = "search/product/"+searchText;
            return ajaxService.get(url,null,{}).then(function(response){
                console.log(response);
                return response.data;
            });

        }else{
            return {id:0,name:'no result',type:1,image:''};
        }
    }

    self.searchTextChange =function(text) {
        //console.log('Text changed to ' + text);
    }

    self.selectedItemChange = function(item) {
        var url = '';
        if(item){
            if(item.type == 'Frame'){
                url = 'productDetail/'+item.id;
            }else{
                url = 'blogDetail/'+item.id;
            }
            $location.path(url);
        }
    }




	

	




	
}]);