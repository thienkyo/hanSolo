'use strict';
angular.module('bizReportModule')
.controller('bizReportController', ['$scope','$location','bizReportService','NgTableParams','memberService','modifiedReportDO',
                                    'CommonStatusArray','BizReportDO','Upload','$timeout','uploadService',
function($scope,$location,bizReportService,NgTableParams,memberService,modifiedReportDO,
            CommonStatusArray,BizReportDO,Upload,$timeout,uploadService) {
    var self = this;
    self.statusList = CommonStatusArray;
    self.theOne = new BizReportDO;
    self.allIncome = 0;
    self.allOutcome = 0;
    self.allOrders = 0;
    self.allFrames = 0;
    self.allLenses = 0;
    self.modifiedReports = [
        new modifiedReportDO('2022'),
        new modifiedReportDO('2023'),
        new modifiedReportDO('2024'),
        new modifiedReportDO('2025'),
        new modifiedReportDO('2026'),
        new modifiedReportDO('2027'),
        new modifiedReportDO('2028'),
        new modifiedReportDO('2029'),
        new modifiedReportDO('2030')
    ];
    self.modifiedReports2 = [];// array of modifiedReportDO


    if(!memberService.isAdmin()){
        $location.path('#/');
    }
    if(!memberService.isSuperAccountant()){
        $location.path('#/');
    }

    bizReportService.getAll().then(function (data) {
        console.log(data);
        self.bizReportList = data;
        self.setModifiedReports(data);
        self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});
    });

    self.setModifiedReports = function(data){
        data.forEach((dataOne, index, array) => {
            self.modifiedReports.forEach((reportOne, index, array) => {
                if(dataOne.year == reportOne.year){
                    reportOne.details.push(dataOne);
                    reportOne.income += dataOne.income;
                    reportOne.outcome += dataOne.outcome;
                    reportOne.orders += dataOne.orderQuantity;
                    reportOne.frames += dataOne.frameQuantity;
                    reportOne.lenses += dataOne.lensQuantity;
                }
            });
            self.allIncome += dataOne.income;
            self.allOutcome += dataOne.outcome;
            self.allOrders += dataOne.orderQuantity;
            self.allFrames += dataOne.frameQuantity;
            self.allLenses += dataOne.lensQuantity;
        });

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

