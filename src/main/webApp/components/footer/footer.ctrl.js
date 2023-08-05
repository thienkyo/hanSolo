angular.module('app')
.controller('footerController',['$scope','$rootScope','shopConfigService','commonCacheService','cacheName',
function ( $scope,$rootScope,shopConfigService,commonCacheService,cacheName ){

var self=this;
self.shopInfo =  commonCacheService.getCurrentCache(cacheName.shopInfoCacheName);

}]);
