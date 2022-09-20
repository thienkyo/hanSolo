'use strict';
angular.module('blogDetailModule')
.factory('blogDetailService', ['ajaxService',function(ajaxService) {
	var blogDetailService = {
			getOneActiveBlog : getOneActiveBlog
			};
	return blogDetailService;

	function getOneActiveBlog(id){
		var url = "blog/"+id;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	      
 }]);
