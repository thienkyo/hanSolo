'use strict';
angular.module('blogModule')
.factory('blogService', ['ajaxService',function(ajaxService) {
	var blogService = {
			getActiveBlog : getActiveBlog,
			getOneActiveBlog : getOneActiveBlog,
			getBlogPage : getBlogPage
			};
	return blogService;

   function getActiveBlog(){
		var url = "blog";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
   
   function getOneActiveBlog(id){
		var url = "blog/"+id;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
   
   function getBlogPage(page){
		var url = "blog/getArticlePage/"+page;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	    
 }]);
