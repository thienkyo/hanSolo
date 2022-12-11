'use strict';
angular.module('prescriptionListModule')
	.controller('prescriptionListController',['$rootScope','$routeParams','$location',
										 'memberService','prescriptionListService','FirstTimeLoadSize',
										 'NgTableParams','OrderStatusArray','cartService',
	function($rootScope, $routeParams,$location,
	        memberService,prescriptionListService,FirstTimeLoadSize,
	        NgTableParams,OrderStatusArray,cartService) {
	var self = this;
	self.prescriptionList = [];

	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=[
        {name : '100', value:100 },
        {name : 'all', value:0 }
    ];;
	self.amount = FirstTimeLoadSize;

	
	prescriptionListService.getPrescriptionsForMgnt(self.amount).then(function (data) {
		self.prescriptionList = data;
		//console.log(self.prescriptionList);
		self.tableParams = new NgTableParams({}, { dataset: self.prescriptionList});
	});

	
	self.getPrescriptionsByTerm = function(){
		prescriptionListService.getPrescriptionsForMgnt(self.amount).then(function (data) {
			self.prescriptionList = data;
			self.tableParams = new NgTableParams({}, { dataset: self.prescriptionList});
			engineerOrderList();
		});
	}
	
}]);