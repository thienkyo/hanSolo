'use strict';
angular.module('orderListModule')
.factory('orderListService', ['ajaxService',function(ajaxService) {
		var orderListService = {
			getOrdersForMgnt : getOrdersForMgnt, // 50 order
			// getAllOrdersForMgnt : getAllOrdersForMgnt,
			updateOrderStatus : updateOrderStatus,
			/*updateOrder : updateOrder,*/
			getOrderById : getOrderById,
			updateCusSource : updateCusSource,
			deleteOrder : deleteOrder
			};
	return orderListService;

	function getOrderById(id){
        var url = "mgnt/getOrderById/"+id;
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }
	
	function getOrdersForMgnt(amount){
		var url = "mgnt/getOrdersForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	
	/*function getAllOrdersForMgnt(){
		var url = "mgnt/getAllOrdersForMgnt/";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}*/

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