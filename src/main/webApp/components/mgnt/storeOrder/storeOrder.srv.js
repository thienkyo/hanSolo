'use strict';
angular.module('storeOrderModule')
.factory('storeOrderService', ['ajaxService',function(ajaxService) {
		var storeOrderService = {
			getOrdersForMgnt : getOrdersForMgnt,
			getAllOrdersForMgnt : getAllOrdersForMgnt,
			/*updateOrderStatus : updateOrderStatus,*/
			placeOrder : placeOrder,
			updateOrder : updateOrder,
			deleteOrder : deleteOrder
			};
	return storeOrderService;

	function placeOrder(order){
       var url = "http://matkinhnguyen.com/authenticated/saveOrder";
       return ajaxService.post2(url,order,{}).then(function(response){
            return response.data;
       });
   }
	
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
	/*
	function updateOrderStatus(orderId,status){
		var url = "mgnt/updateOrderStatus/"+orderId+"/"+status;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}*/

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