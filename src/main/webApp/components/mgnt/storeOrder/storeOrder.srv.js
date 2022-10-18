'use strict';
angular.module('storeOrderModule')
.factory('storeOrderService', ['ajaxService',function(ajaxService) {
		var orderListService = {
			getOrdersForMgnt : getOrdersForMgnt,
			getAllOrdersForMgnt : getAllOrdersForMgnt,
			updateOrderStatus : updateOrderStatus,
			updateOrder : updateOrder,
			deleteOrder : deleteOrder
			};
	return orderListService;
	
	function getOrdersForMgnt(amount){
		var url = "mgnt/getOrdersForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	
	function getAllOrdersForMgnt(){
		var url = "mgnt/getAllOrdersForMgnt/";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	
	function updateOrderStatus(orderId,status){
		var url = "mgnt/updateOrderStatus/"+orderId+"/"+status;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function updateOrder(order){
    		var url = "mgnt/updateOrder";
    		return ajaxService.post(url,order,{}).then(function(response){
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