'use strict';
angular.module('bizReportModule')
.controller('bizReportController', ['$scope','$location','bizReportService','NgTableParams','memberService','ModifiedReportDO',
                                    'CommonStatusArray','BizReportDO','Upload','$timeout','uploadService','clientInfoCacheService',
                                    'clientListCacheService','shopListCacheService','commonCacheService',
function($scope,$location,bizReportService,NgTableParams,memberService,ModifiedReportDO,
            CommonStatusArray,BizReportDO,Upload,$timeout,uploadService,clientInfoCacheService,
            clientListCacheService,shopListCacheService,commonCacheService){
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
    self.modifiedReports = resetModifiedReports();

    self.isGodLike = memberService.isGodLike();
    self.clientList = clientListCacheService.get().filter(i => i.clientCode != 'ALL' );
    //self.shopList = shopListCacheService.get();
    //self.clientList2 = clientListCacheService.get();
    //self.shopList2 = shopListCacheService.get();
    self.queryRequest={};
    self.queryRequest.amount = 0;

    /*if(self.isGodLike){
        self.queryRequest.clientCode = 'ALL';
        self.queryRequest.shopCode = 'ALL';
    }else{*/
        self.queryRequest.clientCode  = clientInfoCacheService.get().clientCode;
        self.shopList = shopListCacheService.get().filter(i => i.clientCode == self.queryRequest.clientCode);
        self.queryRequest.shopCode = self.shopList[0].shopCode;

    //}

    // exclude some month.
    self.beginMonthNumber = clientInfoCacheService.get().bizReportBeginMonthNumber;
    self.endMonthNumber = clientInfoCacheService.get().bizReportEndMonthNumber;

    self.modifiedReports2 = [];// array of ModifiedReportDO

    if(!memberService.isSuperAdmin()){
        $location.path('#/');
    }

    commonCacheService.getLastTimeData().then(function (data) {
        self.lastBizReportCalculationTime = data.lastBizReportCalculationTime;
    });

    getDataByCondition();
    self.dynamicPopover = {
        content: 'Hello, World!',
        templateUrl: 'myPopoverTemplate.html',
        title: 'Title'
    };

    self.currentDate  = (new Date()).getDate();
    self.currentMonth = (new Date()).getMonth() + 1;
    self.currentYear  = (new Date()).getFullYear();

    ///////////////////////////////////

    function resetModifiedReports(){
        var startYear = 2010;
        var endYear = 2040;
        var modifiedReports = [];
        for(var i = startYear; i < endYear; i++){
            modifiedReports.push(new ModifiedReportDO(i+""));
        }
        return modifiedReports;
    }

    function getDataByCondition(){
        self.allIncome = 0;
        self.allOutcome = 0;
        self.allOrders = 0;
        self.allFrames = 0;
        self.allLenses = 0;
        self.allDiscountAmount = 0;
        self.allProfit = 0;
        self.tableParams = new NgTableParams({}, { dataset: []});
        self.modifiedReports2 = [];
        self.modifiedReports = resetModifiedReports();
        bizReportService.getDataByCondition(self.queryRequest).then(function (data) {
            self.bizReportList = data;
            self.setModifiedReports(data);

            var data2 = [...data];// clone
            for(var i = 0; i < self.beginMonthNumber; i++){
                data2.pop();
            }

            for(var i = 0; i < self.endMonthNumber; i++){
                data2.shift();
            }

            self.maxAllIncomeMonth = data2.find(item => item.income == Math.max(...data2.map(o => o.income)));
            self.minAllIncomeMonth = data2.find(item => item.income == Math.min(...data2.map(o => o.income)));

            self.maxAllOutcomeMonth = data2.find(item => item.outcome == Math.max(...data2.map(o => o.outcome)));
            self.minAllOutcomeMonth = data2.find(item => item.outcome == Math.min(...data2.map(o => o.outcome)));

            self.maxAllProfitMonth = data2.find(item => item.profit == Math.max(...data2.map(o => o.profit)));
            self.minAllProfitMonth = data2.find(item => item.profit == Math.min(...data2.map(o => o.profit)));

            self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});
        });
    }
    self.getDataByCondition = getDataByCondition;


    self.filterShopAndCalByClientCode = function(clientCode){
        self.shopList = shopListCacheService.get().filter(i => i.clientCode == clientCode || i.shopCode == 'ALL' );
        if(self.shopList.length > 0){
            self.queryRequest.shopCode = self.shopList[1].shopCode;
        }else{
            self.queryRequest.shopCode = 'ALL';
        }
        getDataByCondition();
    }

    self.filterBizReportByShopCode = function(){
        getDataByCondition(self.queryRequest);
    }

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
                    // add more detail in current year,
                    // for calculate the correct average
                    // ex: income2 doesn't count the latest month
                    if(reportOne.year == self.currentYear && dataOne.month != self.currentMonth){

                        reportOne.income2 += dataOne.income;
                        reportOne.outcome2 += dataOne.outcome;
                        reportOne.orders2 += dataOne.orderQuantity;
                        reportOne.frames2 += dataOne.frameQuantity;
                        reportOne.lenses2 += dataOne.lensQuantity;
                        reportOne.discountAmount2 += dataOne.discountAmount;
                    }
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
          one.income = data.obj.income;
          one.outcome = data.obj.outcome;
            //self.tableParams = new NgTableParams({}, { dataset: self.bizReportList});
        });
    }

    self.upsert = function(bizReport){
        console.log(bizReport);
        self.responseStr = false;
        self.responseStrFail = false;
        bizReport.clientCode = self.queryRequest.clientCode;
        bizReport.shopCode = self.queryRequest.shopCode;

        bizReportService.upsert(bizReport).then(function (data) {

            console.log(data);
            if(data.errorCode == 'SUCCESS'){
                self.responseStr = data.errorMessage;
            }else{
                self.responseStrFail = data.errorMessage;
            }
            if(bizReport.id == 0){
                self.bizReportList.unshift(data.obj);
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

