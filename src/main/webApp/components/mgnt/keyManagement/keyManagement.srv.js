'use strict';
angular.module('keyManagementModule')
.factory('keyManagementService', ['ajaxService',function(ajaxService) {
		var mainService = {
				getAll : getAll,
				upsert : upsert,
				renewKey : renewKey,
				deleteOne : deleteOne
			};
	return mainService;

	/* management from here*/
	function getAll(){
		var url = "mgnt/getAllKeyManagement";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertKeyManagement";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function renewKey(one){
        var url = "mgnt/renewKey";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

	function deleteOne(one){
		var url = "mgnt/deleteKeyManagement";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}


 }]);