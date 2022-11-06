'use strict';
angular.module('prescriptionModule')
	.controller('prescriptionController',['$routeParams','$location','prescriptionService',
	function($routeParams,$location,prescriptionService) {
	var self = this;

    var paramValue = $location.search();

    prescriptionService.getOnePrescription(paramValue.orderDetailId)
        .then(function (data) {
            self.thePrescription = data;
    });
}]);