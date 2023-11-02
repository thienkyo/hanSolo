'use strict';
angular.module('contractModule')
.controller('contractController', ['$scope','$location','NgTableParams','memberService','ContractDO','SalaryDO',
                                    'CommonStatusArray','contractService','salaryService','clientListCacheService',
                                    'shopListCacheService','clientInfoCacheService',
function($scope,$location,NgTableParams,memberService,ContractDO,SalaryDO,
            CommonStatusArray,contractService,salaryService,clientListCacheService,
            shopListCacheService,clientInfoCacheService) {
    var self = this;
    self.statusList = CommonStatusArray;
    self.theOne = new ContractDO();
    self.theSalary = new SalaryDO();

    if(!memberService.isSuperAdmin()){
        $location.path('#/');
    }

    self.isGodLike = memberService.isGodLike();
    self.clientList = clientListCacheService.get();
    self.shopList = shopListCacheService.get();
    self.clientList2 = clientListCacheService.get();
    self.shopList2 = shopListCacheService.get();
    self.queryRequest={};
    self.queryRequest.amount = 0;
    console.log('this is contract');

    if(self.isGodLike){
        self.queryRequest.clientCode = 'ALL';
        self.queryRequest.shopCode = 'ALL';
    }else{
        self.queryRequest.clientCode  = clientInfoCacheService.get().clientCode;
        if(self.shopList.length == 1){
            self.queryRequest.shopCode = self.shopList[0].shopCode;
        }else{
            self.queryRequest.shopCode = 'ALL';
        }
    }

    contractService.getAll().then(function (data) {
        data.forEach(getShopName);
        self.contractList = data;
        console.log(self.contractList);
        self.contractList.forEach(enrichContractList);
        self.tableParams = new NgTableParams({}, { dataset: self.contractList});
    });


////////////////////////

    self.filterShopByClientCode = function(clientCode){
        self.shopList2 = shopListCacheService.get().filter(i => i.clientCode == clientCode || i.shopCode == 'ALL' );
    }

    //filterOrderAndShopByClientCode

    function enrichContractList(contract){
        if(contract.endDay){
            contract.active = false;
        }else{
            contract.active = true;
        }
    }

    self.addContract = function(){
        self.theOne = new ContractDO();
        self.theSalary = new SalaryDO();

        if(self.isGodLike){
            self.theOne.clientCode = 'ALL';
            self.theOne.shopCode = 'ALL';
        }else{
            self.theOne.clientCode  = clientInfoCacheService.get().clientCode;
            if(self.shopList.length == 1){
                self.theOne.shopCode = self.shopList[0].shopCode;
            }else{
                self.theOne.shopCode = 'ALL';
            }
        }
    }

    self.setTheOne = function(one){
        self.theOne = one;
        self.theSalary.amount = self.theOne.salary;
        self.theSalary.month = (new Date()).getMonth() + 1 >9 ? ((new Date()).getMonth() + 1) : "0"+((new Date()).getMonth() + 1);
        self.theSalary.year = (new Date()).getFullYear();

        salaryService.getAll(self.theOne.id).then(function (data) {
            self.salaryList = data;
            self.salaryTableParams = new NgTableParams({}, { dataset: self.salaryList});
        });
    }

    self.upsert = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        contractService.upsert(one).then(function (data) {
            self.responseStr = data.errorMessage;
            console.log(data);
            console.log(data.contract);
            console.log(data.obj);
            if(one.id == 0){
                self.contractList.unshift(data.obj);
                self.tableParams = new NgTableParams({}, { dataset: self.contractList});
            }
        });
    }



    self.deleteOne = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        contractService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.contractList.indexOf(one);
            self.contractList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.contractList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    function getShopName(contract){
        if(contract.shopCode){
            contract.shopName = self.shopList.find(i => i.shopCode == contract.shopCode).shopName;
        }

    }

    self.clear = function(){
        self.responseStr = false;
        self.responseStrFail = false;
        self.theOne = new BizReportDO;
        self.isShowUploadPic = false;
    }

    self.promptDelete = function(id){
        self.deletingId = self.deletingId ? false : id;
    }

	self.resetDelete = function(){
        self.deletingId = false;
    }

    // open datePicker
    self.openDP = function() {
        self.DPisOpen = true;
    };
    self.openDP2 = function() {
        self.DPisOpen2 = true;
    };
    self.openDP3 = function() {
        self.DPisOpen3 = true;
    };

    /////// salary

    self.setTheSalary = function(one){
        self.theSalary = one;
    }
    self.clearSalary = function(one){
        self.theSalary = new SalaryDO();;
    }

    self.upsertSalary = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        one.contractId = self.theOne.id;
        salaryService.upsert(one).then(function (data) {
            self.responseStr = data.errorMessage;
            console.log(data);
            console.log(data.salary);
            console.log(data.obj);
            if(one.id == 0){
                self.salaryList.unshift(data.obj);
                self.salaryTableParams = new NgTableParams({}, { dataset: self.salaryList});
            }
        });
    }

    self.deleteSalary = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        salaryService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.salaryList.indexOf(one);
            self.salaryList.splice(index,1);
            self.salaryTableParams = new NgTableParams({}, { dataset: self.salaryList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.closeAlert = function() {
        self.responseStr = false;
    };

}]);

