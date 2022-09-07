'use strict';
angular.module('homeModule').controller('homeController', ['$scope','homeService',
	function($scope,homeService) {
		var self = this;

		homeService.getBanner()
        		.then(function (response) {
        			self.banners = response.reverse();
        			console.log(self.banners);

        			$(document).ready(function() {
                              $(".hero__slider").owlCarousel({
                                      loop: true,
                                      margin: 0,
                                      items: 1,
                                      dots: false,
                                      nav: true,
                                      navText: ["<span class='arrow_left'><span/>", "<span class='arrow_right'><span/>"],
                                      animateOut: 'fadeOut',
                                      animateIn: 'fadeIn',
                                      smartSpeed: 2400,
                                      autoHeight: false,
                                      autoplay: true
                                  });

                            })
        		});






	/*
		homeService.getHomeProduct()
			.then(function (response) {
				self.homeProducts = response;
			});



		homeService.gethomeArticle()
		.then(function (response) {
			self.homeArticles = response;
		});

		self.addToCart = function(prod){
			if(prod.quantity > 0){
				cartService.addToCart(prod,1);
			}
			self.alertProdId = prod.prodId;
		}
*/
}]);

