'use strict';
angular.module('contractModule')
.controller('contractController', ['$scope','$location','NgTableParams','memberService','ContractDO','SalaryDO',
                                    'CommonStatusArray','contractService','salaryService',
function($scope,$location,NgTableParams,memberService,ContractDO,SalaryDO,
            CommonStatusArray,contractService,salaryService) {
    var self = this;
    self.statusList = CommonStatusArray;
    self.theOne = new ContractDO();
    self.theSalary = new SalaryDO();

    if(!memberService.isSuperAdmin()){
        $location.path('#/');
    }

    contractService.getAll().then(function (data) {
        self.contractList = data;
        self.contractList.forEach(enrichContractList);
        self.tableParams = new NgTableParams({}, { dataset: self.contractList});
    });

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
        console.log(self.theOne);
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

