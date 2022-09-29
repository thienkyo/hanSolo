'use strict';
angular.module('blogDetailModule')
.controller('blogDetailController', ['$scope','$routeParams','blogDetailService','$sce','memberService','productService',
	function($scope,$routeParams, blogDetailService,$sce,memberService,productService) {
		var self = this;
		self.isAdmin = memberService.isAdmin();
		
		blogDetailService.getOneActiveBlog($routeParams.blogId)
		.then(function (data) {

			self.theBlog = data;
			self.theBlog.content=$sce.trustAsHtml(self.theBlog.content);
		});
}]);

