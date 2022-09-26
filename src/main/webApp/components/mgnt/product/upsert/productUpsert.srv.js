'use strict';
angular.module('productUpsertModule')
.factory('productUpsertService', ['ajaxService',function(ajaxService) {
		var productUpsertService = {
			upsert : upsert, 
			getProductById : getProductById
			};
	return productUpsertService;
	
	function upsert(product){
		var url = "mgnt/upsertProduct";
		return ajaxService.post(url,product,{}).then(function(response){
			return response.data;
		});
	}
	
	function getProductById(id){
		var url = "mgnt/getProductById/"+id;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);