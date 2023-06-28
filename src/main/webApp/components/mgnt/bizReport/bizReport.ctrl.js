'use strict';
angular.module('bizReportModule')
.controller('bizReportController', ['$scope','$location','bizReportService','NgTableParams','memberService','ModifiedReportDO',
                                    'CommonStatusArray','BizReportDO','Upload','$timeout','uploadService',
function($scope,$location,bizReportService,NgTableParams,memberService,ModifiedReportDO,
            CommonStatusArray,BizReportDO,Upload,$timeout,uploadService) {
    var self = this;
    self.statusList = CommonStatusArray;
    self.theOne = new BizReportDO;
    self.allIncome = 0;
    self.allOutcome = 0;
    self.allOrders = 0;
    self.allFrames = 0;
    self.allLenses = 0;
    self.allDiscountAmount = 0;
    self.allProfit = 0;
    self.modifiedReports = [
        new ModifiedReportDO('2022'),
        new ModifiedReportDO('2023'),
        new ModifiedReportDO('2024'),
        new ModifiedReportDO('2025'),
        new ModifiedReportDO('2026'),
        new ModifiedReportDO('2027'),
        new ModifiedReportDO('2028'),
        new ModifiedReportDO('2029'),
        new ModifiedReportDO('2030'),
        new ModifiedReportDO('2031')
    ];
    self.excludedMonth = ['202207','202208','202209'];
    self.modifiedReports2 = [];// array of ModifiedReportDO


    if(!memberService.isAdmin()){
        $location.path('#/');
    }
    if(!memberService.isSuperAccountant()){
        $location.path('#/');
    }

    bizReportService.getAll().then(function (data) {

        self.bizReportList = data;
        self.setModifiedReports(data);

        var data2 = [...data];

        self.excludedMonth.forEach((one, index, array) => {
            data2 = data2.filter(i => i.year+i.month != one);
        });

        self.maxAllIncomeMonth = data2.find(item => item.income == Math.max(...data2.map(o => o.income)));
        self.minAllIncomeMonth = data2.find(item => item.income == Math.min(...data2.map(o => o.income)));

        self.maxAllOutcomeMonth = data2.find(item => item.outcome == Math.max(...data2.map(o => o.outcome)));
        self.minAllOutcomeMonth = data2.find(item => item.outcome == Math.min(...data2.map(o => o.outcome)));

        self.maxAllProfitMonth = data2.find(item => item.profit == Math.max(...data2.map(o => o.profit)));
        self.minAllProfitMonth = data2.find(item => item.profit == Math.min(...data2.map(o => o.profit)));

        self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});
    });

    self.setModifiedReports = function(data){
        data.forEach((dataOne, index, array) => {
            self.modifiedReports.forEach((reportOne, index, array) => {
                if(dataOne.year == reportOne.year){
                    dataOne.profit = dataOne.income - dataOne.outcome;
                    reportOne.details.push(dataOne);// input months detail
                    reportOne.income += dataOne.income;
                    reportOne.outcome += dataOne.outcome;
                    reportOne.orders += dataOne.orderQuantity;
                    reportOne.frames += dataOne.frameQuantity;
                    reportOne.lenses += dataOne.lensQuantity;
                    reportOne.discountAmount += dataOne.discountAmount;
                }
            });
            self.allIncome  += dataOne.income;
            self.allOutcome += dataOne.outcome;
            self.allDiscountAmount += dataOne.discountAmount;
            self.allOrders += dataOne.orderQuantity;
            self.allFrames += dataOne.frameQuantity;
            self.allLenses += dataOne.lensQuantity;
        });

        self.allProfit = self.allIncome-self.allOutcome;
        self.modifiedReports.reverse();
        self.modifiedReports.forEach((reportOne, index, array) => {
            if(reportOne.details.length > 0){
                self.modifiedReports2.push(reportOne);
            }
        });

        console.log(self.modifiedReports2);
    }

    self.setTheOne = function(one){
        self.theOne = one;
        self.responseStr = false;
        self.responseStrFail = false;
    }

    self.calculateReport = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        bizReportService.calculateReport(one).then(function (data) {
          self.responseStr = data.errorMessage;
          one.income = data.bizReport.income;
          one.outcome = data.bizReport.outcome;
            //self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});
        });
    }

    self.upsert = function(bizReport){

        self.responseStr = false;
        self.responseStrFail = false;
        bizReportService.upsert(bizReport).then(function (data) {
            self.responseStr = data.errorMessage;
            if(bizReport.id == 0){
                self.bizReportList.unshift(data.bizReport);
                self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});
            }
        });
    }

    self.deleteOne = function(bizReport){
        self.responseStr = false;
        self.responseStrFail = false;
        bizReportService.deleteOne(bizReport).then(function (data) {
            self.responseStr = data;
            var index = self.bizReportList.indexOf(bizReport);
            self.bizReportList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.clear = function(){
        self.responseStr = false;
        self.responseStrFail = false;
        self.theOne = new BizReportDO;
        self.isShowUploadPic = false;
    }
		
}]);

