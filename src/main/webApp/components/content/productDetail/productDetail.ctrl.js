'use strict';
angular.module('productDetailModule')
	.controller('productDetailController',['$rootScope','$routeParams','productDetailService','memberService','$sce','cartService',
	function($rootScope, $routeParams, productDetailService,memberService,$sce,cartService) {
	
	var self = this;
	self.qty = 1;
	self.isAdmin = memberService.isAdmin();
	self.selectedPicIndex = 0;
	
	productDetailService.getProductByProdId($routeParams.prodId)
		.then(function (data) {
			self.product = data;
			self.description=$sce.trustAsHtml(self.product.description);
	        $rootScope.$broadcast('productNameBC', self.product);//self.product.prodName
	});
	
	self.addToCart = function(prod){
        var temp = prod;
		if(prod.quantity > 0){
			cartService.addToCart(temp, self.qty);
		}
	}

	self.togglePic = function(index){
    		self.selectedPicIndex = index;
    	}

}]);