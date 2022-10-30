'use strict';
angular.module('prescriptionModule')
	.controller('prescriptionController',['$routeParams','$location','prescriptionService',
	function($routeParams,$location,prescriptionService) {
	var self = this;

    var paramValue = $location.search();
    console.log(paramValue.orderDetailId);

    prescriptionService.getOnePrescription(paramValue.orderDetailId)
        .then(function (data) {
            console.log(data);
            self.thePrescription = data;
    });
}]);