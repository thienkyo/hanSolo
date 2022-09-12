'use strict';
angular.module('productDetailModule')
	.controller('productDetailController',['$rootScope','$routeParams','productDetailService','memberService','$sce',
	function($rootScope, $routeParams, productDetailService,memberService,$sce) {
	
	var self = this;
	self.qty = 1;
	self.isAdmin = memberService.isAdmin();
	
	productDetailService.getProductByProdId($routeParams.prodId)
		.then(function (data) {
		    console.log(data);
			self.product = data;
		//	self.product.description=$sce.trustAsHtml(self.product.description);
	     //   self.test = response;
	        $rootScope.$broadcast('productNameBC', self.product);//self.product.prodName
	});
	
	self.addToCart = function(prod){
		if(prod.quantity > 0){
			cartService.addToCart(prod, self.qty);
		}
		self.alertProdId = prod.prodId;
	}

}]);