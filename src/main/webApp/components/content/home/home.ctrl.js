'use strict';
angular.module('homeModule')
.controller('homeController', ['$scope','homeService','cartService','clientListCacheService','memberService',
                               'shopListCacheService','clientInfoCacheService','clientService','currentShopCacheService',
                               'oneClientShopListCacheService',
	function($scope,homeService,cartService,clientListCacheService,memberService,
	         shopListCacheService,clientInfoCacheService,clientService,currentShopCacheService,
	         oneClientShopListCacheService
	){
		var self = this;
		self.isGodLike = memberService.isGodLike();
		self.isLogin = memberService.isLogin();


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
/*
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
        */

		console.log('this is home');
		console.log(clientListCacheService.get());
        console.log(clientInfoCacheService.get());
        console.log(shopListCacheService.get());
        console.log(currentShopCacheService.get());
        console.log(oneClientShopListCacheService.get());
        console.log(memberService.getCurrentMember());

		if(self.isGodLike){ // only godlike get new data from db.
            clientService.getClientShopList().then(function (data) {
                console.log(data);
                clientListCacheService.set(data.obj.clientList);
                shopListCacheService.set(data.obj.shopList);
                clientInfoCacheService.set(data.obj.clientList.find(i => i.clientCode == 'GODLIKE'));
            });
        }else if(self.isLogin){
            clientService.getClientShopList2().then(function (data) {
                console.log(data);
                clientInfoCacheService.set(data.obj.clientList[0]);
                clientListCacheService.set(data.obj.clientList);
                oneClientShopListCacheService.set(data.obj.oneClientShopList);
                 if(data.obj.shopList){
                    shopListCacheService.set(data.obj.shopList);
                    if(data.obj.shopList.length == 1){
                        currentShopCacheService.set(data.obj.shopList[0]);
                    }else{
                        if(currentShopCacheService.get()) {
                            if(!data.obj.shopList.some(e => e.shopCode === currentShopCacheService.get().shopCode)){
                                currentShopCacheService.clear();
                            }
                        }
                    }
                }
            });
        }

/////////////////////////
/*

		self.addToCart = function(prod){
			if(prod.quantity > 0){
				cartService.addToCart(prod,1);
			}
			self.alertProdId = prod.id;
		}
*/


}]);

