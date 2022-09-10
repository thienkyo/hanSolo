'use strict';
angular.module('homeModule')
.factory('homeService', ['ajaxService',function(ajaxService) {
	var homeService = {
			getHomeProduct : getHomeProduct,
			getHomeArticle : getHomeArticle,
			getBanner : getBanner
			};
	return homeService;
   
   function getHomeProduct(){
		var url = "products/homeProduct";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
	}
   
   function getHomeArticle(){
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
