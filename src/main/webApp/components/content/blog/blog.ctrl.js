'use strict';
angular.module('blogModule').controller('blogController', ['$routeParams','blogService','memberService','productService','paginationService',
	function($routeParams, blogService,memberService,productService,paginationService) {
		var self = this;
		
		self.isAdmin = memberService.isAdmin();
		blogService.getBlogPage(1)
		.then(function (data) {
		    console.log(data);
			self.currentPage = data;
			self.pagination = paginationService.builder(data);
	    });
		
		self.getTargetPage = function(pageNumber){
			if(pageNumber != self.pagination.currentNumber && pageNumber <= self.pagination.list.length){
				blogService.getBlogPage(pageNumber)
				.then(function (response) {
					self.currentPage = response;
					self.pagination = paginationService.builder(response);
			    });
			}
		}
		/*
		productService.getRandomProduct()
		.then(function (data) {
			self.randomProducts = data;
		});*/
}]);

