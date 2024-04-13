'use strict';
angular.module('customerSourceModule')
.controller('customerSourceController', ['$scope','$location','customerSourceService','NgTableParams','memberService','CommonStatusArray',
                                        'CustomerSourceDO','Upload','$timeout','uploadService','CustomerSourceReportDO','clientInfoCacheService',
                                        'currentShopCacheService','shopListCacheService','oneClientShopListCacheService',
	function($scope,$location,customerSourceService,NgTableParams,memberService,CommonStatusArray,
	        CustomerSourceDO,Upload,$timeout,uploadService,CustomerSourceReportDO,clientInfoCacheService,
	        currentShopCacheService,shopListCacheService,oneClientShopListCacheService) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theOne = new CustomerSourceDO;
		self.theOneReport = new CustomerSourceReportDO;
		self.statusStyle = {};
		self.discountOrderNumber = 0;
		self.totalDiscountAmount = 0;
		self.totalCount = 0;

        self.currentYear  = (new Date()).getFullYear();
		self.queryRequest={};
        self.queryRequest.amount = 0;
        self.queryRequest.clientCode  = clientInfoCacheService.get().clientCode;
        self.queryRequest.shopCode = currentShopCacheService.get().shopCode;
        self.queryRequest.generalPurpose = self.currentYear;
        self.isGodLike = memberService.isGodLike();
        self.isSuperAdmin = memberService.isSuperAdmin();
        self.yearList = buildYearList();

		if(!memberService.isAdmin()){
			$location.path('#/');
		}


        function buildYearList(){
            var yearList = [];
            for(var i = 0; i < 10; i++){
                yearList.push(self.currentYear - i);
            }
            return yearList;
        }
        console.log(self.yearList);
        console.log(self.queryRequest);


        if(self.isSuperAdmin){
            self.shopList = oneClientShopListCacheService.get();
            console.log(self.shopList);
        }





        self.loadCusSourceData = function(str){
            customerSourceService.getCustomerSourceByTerms(self.queryRequest).then(function (data) {
                self.customerSourceList = data;
                self.totalCount = 0;
                self.customerSourceList.forEach((dataOne, index, array) => {
                    self.totalCount += dataOne.count;
                });
                self.customerSourceList.forEach((dataOne, index, array) => {
                    dataOne.percent = dataOne.count/self.totalCount*100;
                });

                self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});

                customerSourceService.getReportByTerms(self.queryRequest).then(function (data) {
                    self.reportListByYear = data;
                    if(self.customerSourceList){
                        self.reportListByYear.forEach(fillInSourceName);
                    }
                    console.log(self.reportListByYear);
                    self.reportByYearParams = new NgTableParams({ page: 1,count: 10,},
                        {total: self.reportListByYear.length, dataset: self.reportListByYear});
                });

            });
        }

        self.loadCusSourceData();



		



		
		self.setTheOne = function(one){
			self.theOne = one;
			self.responseStr = false;
			self.responseStrFail = false;
		}
		
		self.upsert = function(customerSource){
			self.responseStr = false;
			self.responseStrFail = false;

			customerSource.clientCode  = clientInfoCacheService.get().clientCode;
            customerSource.shopCode = self.queryRequest.shopCode;


			customerSourceService.upsert(customerSource).then(function (data) {
				self.responseStr = data.errorMessage;
				console.log(data);
				if(customerSource.id == 0){
					self.customerSourceList.unshift(data.obj);
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
            report.clientCode  = clientInfoCacheService.get().clientCode;
            report.shopCode = self.queryRequest.shopCode;
            customerSourceService.upsertReport(report).then(function (data) {
                self.responseStr = data.errorMessage;
            });
        }

		self.calculateReport = function(report){
            self.responseStr = false;
            self.responseStrFail = false;
            customerSourceService.calculateReport(report).then(function (data) {
                self.responseStr = data.errorMessage;
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

