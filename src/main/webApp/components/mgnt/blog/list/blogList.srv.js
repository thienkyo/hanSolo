'use strict';
angular.module('blogListModule')
.factory('blogListService', ['ajaxService',function(ajaxService) {
	var blogListService = {
			getBlogsForMgnt : getBlogsForMgnt
			};
	return blogListService;

	function getBlogsForMgnt(amount){
		var url = "mgnt/getArticlesForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}   
    
 }]);
