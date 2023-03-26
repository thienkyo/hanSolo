'use strict';
angular.module('customerSourceModule')
.controller('customerSourceController', ['$scope','customerSourceService','NgTableParams','memberService','CommonStatusArray',
                                        'CustomerSourceDO','Upload','$timeout','uploadService','CustomerSourceReportDO',
	function($scope,customerSourceService,NgTableParams,memberService,CommonStatusArray,
	        CustomerSourceDO,Upload,$timeout,uploadService,CustomerSourceReportDO) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theOne = new CustomerSourceDO;
		self.theOneReport = new CustomerSourceReportDO;
		self.statusStyle = {};
		self.discountOrderNumber = 0;
		self.totalDiscountAmount = 0;

		if(!memberService.isAdmin()){
			$location.path('#/');
		}
		
		customerSourceService.getAll().then(function (data) {
			self.customerSourceList = data;
			console.log(data);
			self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});

			customerSourceService.getReportAll().then(function (data) {
                self.reportList = data;
                console.log(data);
                if(self.customerSourceList){
                    self.reportList.forEach(fillInSourceName);
                }
                console.log(self.reportList);
                self.reportParams = new NgTableParams({}, { dataset: self.reportList});
            });

		});


		
		self.setTheOne = function(one){
			self.theOne = one;
			self.responseStr = false;
			self.responseStrFail = false;
		}
		
		self.upsert = function(customerSource){
			self.responseStr = false;
			self.responseStrFail = false;
			customerSourceService.upsert(customerSource).then(function (data) {
				self.responseStr = data.errorMessage;
				//console.log(data);
				if(customerSource.id == 0){
					self.customerSourceList.unshift(data.customerSource);
					self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});
				}
			});
		}
		
		self.deleteOne = function(customerSource){
			self.responseStr = false;
			self.responseStrFail = false;
			customerSourceService.deleteOne(customerSource).then(function (data) {
				self.responseStr = data;
				var index = self.customerSourceList.indexOf(customerSource);
				self.customerSourceList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}

		self.clear = function(){
            self.responseStr = false;
            self.responseStrFail = false;
            self.theOne = new CustomerSourceDO;
            self.isShowUploadPic = false;
        }

		self.upsertReport = function(report){
            self.responseStr = false;
            self.responseStrFail = false;
            customerSourceService.upsertReport(report).then(function (data) {
                self.responseStr = data.errorMessage;
            });
        }

		self.calculateReport = function(report){
		    console.log("calculateReport");
            self.responseStr = false;
            self.responseStrFail = false;
            customerSourceService.calculateReport(report).then(function (data) {
                self.responseStr = data.errorMessage;
                console.log(data);
            });
        }

        self.setTheOneReport = function(one){
            self.theOneReport = one;
            self.responseStr = false;
            self.responseStrFail = false;
        }
		
		self.clearTheOneReport = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theOneReport = new CustomerSourceDO;
			self.isShowUploadPic = false;
		}

		function fillInSourceName(report){
		    report.sourceName = self.customerSourceList.find(i => i.id == report.customerSourceId).name;
		}
		
}]);

