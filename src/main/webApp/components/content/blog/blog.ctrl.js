'use strict';
angular.module('blogModule').controller('blogController', ['$routeParams','blogService','memberService','productService','paginationService',
	function($routeParams, blogService,memberService,productService,paginationService) {
		var self = this;
		
		self.isAdmin = memberService.isAdmin();
		blogService.getBlogPage(1)
		.then(function (data) {
			self.currentPage = data;
			self.pagination = paginationService.builder(data);
	    });
		
		self.getTargetPage = function(pageNumber){
			if(pageNumber != self.pagination.currentNumber && pageNumber <= self.pagination.list.length){
				blogService.getBlogPage(pageNumber)
				.then(function (data) {
					self.currentPage = data;
					self.pagination = paginationService.builder(data);
			    });
			}
		}
		/*
		productService.getRandomProduct()
		.then(function (data) {
			self.randomProducts = data;
		});*/
}]);

