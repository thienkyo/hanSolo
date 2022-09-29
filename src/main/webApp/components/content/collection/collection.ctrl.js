'use strict';
angular.module('collectionModule').controller('collectionController', ['$scope','categoryService','paginationService',
	function($scope,categoryService,paginationService) {
		var self = this;

		categoryService.getActiveCategories().then(function(data){
            self.cateList = data;
        });

        categoryService.getCollectionPage(1).then(function(data){
            self.currentPage = data;
            if(self.currentPage.totalElements != 0){
                self.pagination = paginationService.builder(data);
            }
        });

        self.getTargetPage = function(pageNumber){
            if(pageNumber != self.pagination.currentNumber && pageNumber <= self.pagination.list.length){
                categoryService.getCollectionPage(pageNumber)
                .then(function (data) {
                    self.currentPage = data;
                    self.pagination = paginationService.builder(data);
                });
            }
        }
}]);

