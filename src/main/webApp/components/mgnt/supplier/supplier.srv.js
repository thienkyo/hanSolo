'use strict';
angular.module('supplierModule')
.factory('supplierService', ['ajaxService',function(ajaxService) {
		var categoryService = {
				getAllSuppliers : getAllSuppliers,
				upsert : upsert,
				deleteSupplier : deleteSupplier
			};
	return categoryService;

	/* management from here*/
	function getAllSuppliers(){
		var url = "mgnt/getAllSuppliers";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	
	function upsert(supplier){
		var url = "mgnt/upsertSupplier";
		return ajaxService.post(url,cate,{}).then(function(response){
			return response.data;
		});
	}
	
	function deleteSupplier(supplier){
		var url = "mgnt/deleteSupplier";
		return ajaxService.post(url,cate,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);