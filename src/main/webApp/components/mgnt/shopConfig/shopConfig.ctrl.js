'use strict';
angular.module('shopConfigModule')
	.controller('shopConfigController',['$rootScope','$routeParams','$location',
										 'memberService','LensProductDO','ShopConfigDO',
										 'NgTableParams','OrderStatusArray','AmountList',
										 'lensProductService','shopConfigService','shopInfoService',
	function($rootScope, $routeParams,$location,
	        memberService,LensProductDO,ShopConfigDO,
	        NgTableParams,OrderStatusArray,AmountList,
	        lensProductService,shopConfigService,shopInfoService
	        ){
	var self = this;
	if(!memberService.isAdmin()){
        $location.path('#/');
    }

    self.shopInfoCacheName = 'shopInfoCache';
	self.theShopConfig = new ShopConfigDO();
    self.isSuperAdmin = memberService.isSuperAdmin();

    shopConfigService.getDataForMgnt().then(function (data) {
        self.theOne = data.obj;
    });


    self.upsert = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        shopConfigService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            self.theShopConfig = data.obj;
            shopInfoService.setCurrentCache(data.obj,self.shopInfoCacheName);
        });
        shopConfigService.refreshShopConfig().then(function (data) {
            self.isSaveButtonPressed=false;
        });
    }

    self.refreshShopConfig = function(){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        shopConfigService.refreshShopConfig().then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
        });
    }

    self.closeAlert = function(index) {
        self.responseStr = false;
    };

//////////////

    self.deleteOne = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        lensProductService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.lensProductList.indexOf(one);
            self.lensProductList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.deleteMany = function() {
        var lensList = self.lensProductList.filter(item => item.picked == true);
        lensProductService.deleteMany(lensList).then(function(data){

            for (var i = 0; i < lensList.length; i++){
                 var index = self.lensProductList.indexOf(lensList[i]);
                 self.lensProductList.splice(index,1);
            }
            self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});
        });
    }

/// modal

}]);
