angular.module('app')
.controller('headerController', ['$rootScope','$location','ajaxService','shopConfigService',
                                 'memberService','cartStoreService','categoryService','loginService',
                                 'cacheName','commonCacheService','shopInfoCacheService',
	function($rootScope,$location,ajaxService,shopConfigService,
	         memberService,cartStoreService,categoryService,loginService,
	         cacheName,commonCacheService,shopInfoCacheService) {
	var self=this;
	self.cart=[];
	self.currentMember = memberService.getCurrentMember();
	self.currentCart = cartStoreService.getCurrentCart();
	self.orderQuantity = cartStoreService.getQuantity();
	self.isGodLike = memberService.isGodLike();
	self.isAdmin = memberService.isAdmin();
	self.isSuperAdmin = memberService.isSuperAdmin();
	self.isMod = memberService.isMod();
	self.shopInfo =  shopInfoCacheService.getCurrentCache();

/*
	categoryService.getActiveCategories().then(function(data){
		self.cateList = data;
	});
*/
	self.logout = function() {
	    console.log(self.currentMember);
	    loginService.logout().then(function (data) {
	        console.log(data);
	        memberService.logout();
        });
		self.isAdmin = false;
		self.isMod = false;
		self.isGodLike = false;
		self.isSuperAdmin = false;
		self.currentMember = null;

		$location.path('#/');
    }

	self.querySearch = function(searchText){
		if(searchText){
			var url = "search/"+searchText;
			return ajaxService.get(url,null,{}).then(function(response){
				return response.data;
			});

		}else{
			return {id:0,name:'no result',type:1,image:''};
		}
	}

	self.searchTextChange =function(text) {
	    //console.log('Text changed to ' + text);
	}

	self.selectedItemChange = function(item) {
		var url = '';
		if(item){
			if(item.type == 'Frame'){
				url = 'productDetail/'+item.id;
			}else{
				url = 'blogDetail/'+item.id;
			}
			$location.path(url);
		}
	}
///////////////////////////Receiver/////////////////////////////////////////////
	$rootScope.$on('authorized', function() {
		self.currentMember = memberService.getCurrentMember();
		self.isAdmin = memberService.isAdmin();
		self.isMod = memberService.isMod();
		self.isGodLike = memberService.isGodLike();
		self.isSuperAdmin = memberService.isSuperAdmin();
    });
	
    $rootScope.$on('unauthorized', function() {
        self.currentMember = memberService.setCurrentMember(null);
        $location.path('#/');
    });
    
    $rootScope.$on('addToCart', function() {
    	self.orderQuantity = cartStoreService.getQuantity();
		self.currentCart = cartStoreService.getCurrentCart();
    });
    
    $rootScope.$on('removeItemCart', function() {
    	self.orderQuantity = cartStoreService.getQuantity();
    });
    
    $rootScope.$on('clearCart', function() {
    	self.orderQuantity = 0;
    });
    
    $rootScope.$on('ExpiredJwt', function() {
    	self.currentMember = memberService.setCurrentMember(null);
		self.isAdmin = false;
		self.isGodLike = false;
        self.isSuperAdmin = false;
        self.isMod = false;
	//	$location.path('#/');
    });

}]);