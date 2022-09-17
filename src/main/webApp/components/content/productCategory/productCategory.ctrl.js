'use strict';
angular.module('productCategoryModule')
.controller('productCategoryController',['$rootScope','$routeParams','productService','cartService','paginationService',
	function($rootScope, $routeParams, productService,cartService,paginationService) {	
	var self = this;
	self.cateId = $routeParams.categoryId;

	productService.getProductPage(self.cateId,1)
	.then(function (response) {
	    console.log(response);
		self.currentPage = response;
		self.pagination = paginationService.builder(response);
    });
	
	self.getTargetPage = function(pageNumber){
		if(pageNumber != self.pagination.currentNumber && pageNumber <= self.pagination.list.length){
			productService.getProductPage(self.cateId,pageNumber)
			.then(function (response) {
				self.currentPage = response;
				self.pagination = paginationService.builder(response);
		    });
		}
	}
	
	self.addToCart = function(prod){
		if(prod.quantity > 0){
			cartService.addToCart(prod,1);
		}
		self.alertProdId = prod.prodId;
	}
}]);