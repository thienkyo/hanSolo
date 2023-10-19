'use strict';
angular.module('bizExpenseModule')
.factory('bizExpenseService', ['ajaxService',function(ajaxService) {
	var bizExpenseService = {
			getBizExpenseForMgnt : getBizExpenseForMgnt,
			upsert : upsert,
			deleteBizExpense : deleteBizExpense,
			updateBizExpenseStatus : updateBizExpenseStatus
			};
	return bizExpenseService;

	/*function getBizExpenseForMgnt(amount){
		var url = "mgnt/getBizExpenseForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}   */


	function getBizExpenseForMgnt(queryRequest){
        var url = "mgnt/getBizExpenseForMgnt/";
        return ajaxService.post(url,queryRequest,{}).then(function(response){
            return response.data;
        });
    }
    
	function upsert(bizExpense){
		var url = "mgnt/upsertBizExpense";
		return ajaxService.post(url,bizExpense,{}).then(function(response){
			return response.data;
		});
	}

	function deleteBizExpense(bizExpense){
        var url = "mgnt/deleteBizExpense";
        return ajaxService.post(url,bizExpense,{}).then(function(response){
            return response.data;
        });
    }

    function updateBizExpenseStatus(bizExpense){
        var url = "mgnt/updateBizExpenseStatus";
        return ajaxService.post(url,bizExpense,{}).then(function(response){
            return response.data;
        });
    }
	
 }]);
