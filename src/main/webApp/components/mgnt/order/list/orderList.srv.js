'use strict';
angular.module('orderListModule')
.factory('orderListService', ['ajaxService',function(ajaxService) {
		var orderListService = {
			getOrdersForMgnt : getOrdersForMgnt, // 100 order
			getOrdersByTerms : getOrdersByTerms,
			// getAllOrdersForMgnt : getAllOrdersForMgnt,
			updateOrderStatus : updateOrderStatus,
			getOrderById : getOrderById,
			updateCusSource : updateCusSource,
			doRecovery : doRecovery,
			getOrderHistory : getOrderHistory,
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

	function getOrdersByTerms(queryRequest){
        var url = "mgnt/getOrdersByTerms";
        return ajaxService.post(url,queryRequest,{}).then(function(response){
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

	function doRecovery(orders){
        var url = "mgnt/recoverOrder";
        return ajaxService.post(url,orders,{}).then(function(response){
            return response.data;
        });
    }

    function getOrderHistory(queryRequest){
        var url = "mgnt/getOrderHistory/";
        return ajaxService.post(url,queryRequest,{}).then(function(response){
            return response.data;
        });
    }

	
	function deleteOrder(order){
		var url = "mgnt/deleteOrder";
		return ajaxService.post(url,order,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);