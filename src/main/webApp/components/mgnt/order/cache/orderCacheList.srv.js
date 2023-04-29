'use strict';
angular.module('orderCacheListModule')
.factory('orderCacheListService', ['ajaxService',function(ajaxService) {
		var orderCacheListService = {
			get100OrdersForCache : get100OrdersForCache, // 100 order
			syncOrder : syncOrder
			};
	return orderCacheListService;
	
	function get100OrdersForCache(){
		var url = "mgnt/getOrdersForMgnt/100";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

/*
	function updateOrder(order){
    		var url = "mgnt/updateOrder";
    		return ajaxService.post(url,order,{}).then(function(response){
    			return response.data;
    		});
    	}*/
	
	function syncOrder(order){
		var url = "authenticated/syncOrder";
		return ajaxService.post(url,order,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);