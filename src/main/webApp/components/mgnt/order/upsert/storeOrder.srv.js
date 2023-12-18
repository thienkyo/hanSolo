'use strict';
angular.module('storeOrderModule')
.factory('storeOrderService', ['ajaxService',function(ajaxService) {
		var service = {
			//getOrdersForMgnt : getOrdersForMgnt,
			//getAllOrdersForMgnt : getAllOrdersForMgnt,
			splitOrder : splitOrder,
			updateOrder : updateOrder,
			deleteOrder : deleteOrder,
			deleteOrderDetail : deleteOrderDetail,
			getOrderById : getOrderById,
			getCoupon3 : getCoupon3
		};
	return service;
	
	/*function getOrdersForMgnt(amount){
		var url = "mgnt/getOrdersForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}*/
/*
	function getAllOrdersForMgnt(){
		var url = "mgnt/getAllOrdersForMgnt/";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}*/
	function getOrderById(queryRequest){
        var url = "mgnt/getOrderById/";
        return ajaxService.post(url,queryRequest,{}).then(function(response){
            return response.data;
        });
    }

	function splitOrder(orders){
        var url = "mgnt/saveMultipleOrders";
        return ajaxService.post(url,orders,{}).then(function(response){
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

	function deleteOrderDetail(orderDetail){
        var url = "mgnt/deleteOrderDetail";
        return ajaxService.post(url,orderDetail,{}).then(function(response){
            return response.data;
        });
    }

    function getCoupon3(queryRequest){
        var url = "mgnt/coupon/getByCode3/";
        return ajaxService.post(url,queryRequest,{}).then(function(response){
            return response.data;
        });
    }
      
 }]);