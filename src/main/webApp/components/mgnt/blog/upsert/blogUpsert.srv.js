'use strict';
angular.module('blogUpsertModule')
.factory('blogUpsertService', ['ajaxService',function(ajaxService) {
		var blogUpsertService = {
			upsert : upsert,
			getArticleById : getArticleById
			};
	return blogUpsertService;
	
	function upsert(article){
		var url = "mgnt/upsertArticle";
		return ajaxService.post(url,article,{}).then(function(response){
			return response.data;
		});
	}

	function getArticleById(articleId){
		var url = "mgnt/getArticleById/"+articleId;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}  
 }]);