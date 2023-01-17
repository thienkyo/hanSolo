'use strict';
angular.module('keyManagementModule')
.controller('keyManagementController', ['$scope','$location','NgTableParams','memberService',
                                    'CommonStatusArray','keyManagementService',
function($scope,$location,bizReportService,NgTableParams,memberService,
            CommonStatusArray,keyManagementService) {
    var self = this;
    self.statusList = CommonStatusArray;



    if(!memberService.isAdmin()){
        $location.path('#/');
    }
    if(!memberService.isSuperAccountant()){
        $location.path('#/');
    }

    keyManagementService.getAll().then(function (data) {
        self.KeyManagementList = data;
        self.tableParams = new NgTableParams({}, { dataset: self.KeyManagementList});
    });


    self.setTheOne = function(one){
        self.theOne = one;
        self.responseStr = false;
        self.responseStrFail = false;
    }

    self.upsert = function(one){

        self.responseStr = false;
        self.responseStrFail = false;
        bizReportService.upsert(one).then(function (data) {
            self.responseStr = data.errorMessage;
            if(one.id == 0){
                self.KeyManagementList.unshift(data.keyManagement);
                self.tableParams = new NgTableParams({}, { dataset: self.KeyManagementList});
            }
        });
    }

    self.deleteOne = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        bizReportService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.KeyManagementList.indexOf(one);
            self.KeyManagementList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.KeyManagementList});

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

