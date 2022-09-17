'use strict';
angular.module('productCategoryModule')
.factory('productService', ['ajaxService',function(ajaxService) {
		var productService = {
				getProductByCategory : getProductByCategory,
				getRandomProduct : getRandomProduct,
				getProductPage : getProductPage
			};
	return productService;
	
	function getProductByCategory(categoryId){
		var url = "products/category/" + categoryId;
		
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
	
	function getRandomProduct(){
		var url = "products/getRandomProduct";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
	
	function getProductPage(categoryId,page){
		var url = "products/getProductPage/"+categoryId+"/"+page;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);
