'use strict';
angular.module('orderCacheListModule')
.factory('orderCacheListService', ['ajaxService',function(ajaxService) {
		var orderCacheListService = {
			get100OrdersForCache : get100OrdersForCache, // 100 order
			updateOrderStatus : updateOrderStatus,
			getOrderById : getOrderById,
			updateCusSource : updateCusSource,
			deleteOrder : deleteOrder
			};
	return orderCacheListService;

	function getOrderById(id){
        var url = "mgnt/getOrderById/"+id;
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }
	
	function get100OrdersForCache(){
		var url = "mgnt/getOrdersForMgnt/100";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function updateCusSource(order){
        var url = "mgnt/updateCusSource";
        return ajaxService.post(url,order,{}).then(function(response){
            return response.data;
        });
    }

	function updateOrderStatus(order){
        var url = "mgnt/updateOrderStatus";
        return ajaxService.post(url,order,{}).then(function(response){
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
	
	function deleteOrder(order){
		var url = "mgnt/deleteOrder";
		return ajaxService.post(url,order,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);