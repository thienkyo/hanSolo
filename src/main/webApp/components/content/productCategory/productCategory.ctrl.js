'use strict';
angular.module('productCategoryModule')
.controller('productCategoryController',['$rootScope','$routeParams','productService','cartService','paginationService','categoryService',
	function($rootScope, $routeParams, productService,cartService,paginationService,categoryService) {
	var self = this;
	self.cateId = $routeParams.categoryId;
	self.categoryName = 'category';

    categoryService.getActiveCategories().then(function(data){
        self.cateList = data;
    });

	productService.getProductPage(self.cateId,1)
	.then(function (data) {
		self.currentPage = data;
		if(self.currentPage.totalElements != 0){
		    self.currentCategory = self.currentPage.content[0].categories.find(i => i.id == self.cateId);
		    self.categoryName =    self.currentCategory ? self.currentCategory.name : '';
        	self.pagination = paginationService.builder(data);
		}
    });
	
	self.getTargetPage = function(pageNumber){
		if(pageNumber != self.pagination.currentNumber && pageNumber <= self.pagination.list.length){
			productService.getProductPage(self.cateId,pageNumber)
			.then(function (response) {
			console.log(response);
				self.currentPage = response;
				self.pagination = paginationService.builder(response);
		    });
		}
	}
	
	self.addToCart = function(prod){
		if(prod.quantity > 0){
			cartService.addToCart(prod,1);
		}
		self.alertProdId = prod.id;
	}
}]);