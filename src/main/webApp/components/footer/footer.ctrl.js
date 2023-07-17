angular.module('app')
.controller('footerController',['$scope','$rootScope','shopConfigService','shopInfoService',
function ( $scope,$rootScope,shopConfigService,shopInfoService ){

var self=this;
self.shopInfoCacheName = 'shopInfoCache';
self.shopInfo =  shopInfoService.getCurrentCache(self.shopInfoCacheName);

}]);
