'use strict';
angular.module('homeModule')
.factory('homeService', ['ajaxService',function(ajaxService) {
	var homeService = {
			getFirst6Product : getFirst6Product,
			getHomeProduct : getHomeProduct,
			gethomeArticle : gethomeArticle,
			getBanner : getBanner
			};
	return homeService;

   function getFirst6Product(){
		var url = "products/first6";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
   
   function getHomeProduct(){
		var url = "products/homeProduct";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
   
   function gethomeArticle(){
		var url = "blog/homeArticle";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
   
   function getBanner(){
		var url = "banner";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
	      
 }]);
