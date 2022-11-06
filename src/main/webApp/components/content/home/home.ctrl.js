'use strict';
angular.module('homeModule').controller('homeController', ['$scope','homeService','cartService',
	function($scope,homeService,cartService) {
		var self = this;

		homeService.getBanner()
        		.then(function (response) {
        			self.banners = response.reverse();
        			// trigger slider
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

        homeService.getHomeProduct()
                .then(function (response) {
                    self.homeProducts = response;

                    $(document).ready(function() {
                        //    Gallery filter
                        $('.filter__controls li').on('click', function () {
                            $('.filter__controls li').removeClass('active');
                            $(this).addClass('active');
                        });
                        if ($('.product__filter').length > 0) {
                            var containerEl = document.querySelector('.product__filter');
                            var mixer = mixitup(containerEl);
                        }
                    })
        });

		homeService.getHomeArticle()
		.then(function (response) {
			self.homeArticles = response;
		});

		self.addToCart = function(prod){
			if(prod.quantity > 0){
				cartService.addToCart(prod,1);
			}
			self.alertProdId = prod.id;
		}
}]);

