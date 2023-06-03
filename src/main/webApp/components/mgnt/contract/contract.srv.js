'use strict';
angular.module('contractModule')
.factory('contractService', ['ajaxService',function(ajaxService) {
		var mainService = {
				getAll : getAll,
				upsert : upsert,
				deleteOne : deleteOne
			};
	return mainService;


	function getAll(){
		var url = "mgnt/getAllContract";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertContract";
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


}])
.factory('salaryService', ['ajaxService',function(ajaxService) {
		var mainService = {
				getAll : getAll,
				upsert : upsert,
				deleteOne : deleteOne
			};
	return mainService;


	function getAll(contractId){
		var url = "mgnt/getAllSalaryOnePerson/"+contractId;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertSalary";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function deleteOne(one){
		var url = "mgnt/deleteSalary";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}


}])

;