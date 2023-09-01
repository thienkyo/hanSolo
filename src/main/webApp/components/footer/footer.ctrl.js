angular.module('app')
.controller('footerController',['$scope','$rootScope','configOSOCacheService','cacheName',
function ( $scope,$rootScope,configOSOCacheService,cacheName ){

var self=this;
self.configOSOInfo =  configOSOCacheService.getCurrentCache();

}]);
