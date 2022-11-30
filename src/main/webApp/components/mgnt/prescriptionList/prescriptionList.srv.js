'use strict';
angular.module('prescriptionListModule')
.factory('prescriptionListService', ['ajaxService',function(ajaxService) {
		var prescriptionListService = {
			getPrescriptionsForMgnt : getPrescriptionsForMgnt, // 50 order
		//	getAllOrdersForMgnt : getAllOrdersForMgnt,
			updateOrderStatus : updateOrderStatus,

			deleteOrder : deleteOrder
			};
	return prescriptionListService;

	
	function getPrescriptionsForMgnt(amount){
		var url = "mgnt/getPrescriptionsForMgnt/"+amount;
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

	function updateOrderStatus(order){
        var url = "mgnt/updateOrderStatus";
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