'use strict';
angular.module('storeOrderModule')
.factory('storeOrderService', ['ajaxService',function(ajaxService) {
		var service = {
			getOrdersForMgnt : getOrdersForMgnt,
			//getAllOrdersForMgnt : getAllOrdersForMgnt,
			splitOrder : splitOrder,
			updateOrder : updateOrder,
			deleteOrder : deleteOrder,
			getCoupon : getCoupon
		};
	return service;
	
	function getOrdersForMgnt(amount){
		var url = "mgnt/getOrdersForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
/*
	function getAllOrdersForMgnt(){
		var url = "mgnt/getAllOrdersForMgnt/";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}*/

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

	function getCoupon(code,type){
        var url = "mgnt/coupon/getByCode2/" + code +"/"+ type;
        return ajaxService.get(url,null,{}).then(function(data){
            return data.data;
        });
    }
      
 }]);