angular.module('app').controller('appcontroller',['$scope','$rootScope','cfpLoadingBar', function($scope,$rootScope,cfpLoadingBar) {
  //  $scope.headingTitle = "app controller";
    var self = this;
    $scope.$on('productNameBC', function(event,data) {
    	console.log(data);
    	self.product = data;
    });
    
}]);