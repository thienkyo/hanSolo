angular.module('app').controller('appController',['$scope','$rootScope', function($scope,$rootScope) {
    $scope.headingTitle = "app controller";
    var self = this;
    $scope.$on('productNameBC', function(event,data) {
    	self.product = data;
    });
    
}]);