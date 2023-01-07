'use strict';
angular.module('smsUserInfoModule')
	.controller('smsUserInfoController',['$rootScope','$location','memberService','smsUserInfoService','AmountList',
									'NgTableParams','smsUserInfoDO','uploadService','$timeout',
									'FirstTimeLoadSize','smsQueueDO','smsQueueService',
	function($rootScope,$location,memberService,smsUserInfoService,AmountList,
	        NgTableParams,smsUserInfoDO,uploadService,$timeout,
	        FirstTimeLoadSize,smsQueueDO,smsQueueService) {
	var self = this;
	self.theSmsUserInfo = new smsUserInfoDO();
	self.theSmsQueue = new smsQueueDO();
	self.statusStyle = {};
	self.isSaveButtonPressed=false;
	self.tempArray=[];
	self.tempAmount=0;
	self.OneDayExpense={};
	self.isAccountant = memberService.isAccountant();

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	self.amountList=AmountList;
    self.amount = FirstTimeLoadSize;
    self.smsQueueAmount = FirstTimeLoadSize;

	smsUserInfoService.getSmsUserInfoForMgnt(self.amount).then(function (data) {
		self.smsUserInfoList = data;
		//console.log(data);
		self.tableParams = new NgTableParams({}, { dataset: self.smsUserInfoList});
	});


	self.upsert = function(smsUserInfo){
	    self.isSaveButtonPressed=true;
		self.responseStr = false;
		self.responseStrFail = false;
		smsUserInfoService.upsert(smsUserInfo).then(function (data) {
			self.responseStr = data;
			self.isSaveButtonPressed=false;
			console.log(data);
			if(smsUserInfo.id == 0){
				self.smsUserInfoList.unshift(data.smsUserInfo);
				self.tableParams = new NgTableParams({}, { dataset: self.smsUserInfoList});
			}
		});
	}

	self.deleteSmsUserInfo = function(smsUserInfo){
        self.responseStr = false;
        self.responseStrFail = false;
        smsUserInfoService.deleteSmsUserInfo(smsUserInfo).then(function (data) {
            self.responseStr = data;
            var index = self.smsUserInfoList.indexOf(smsUserInfo);
            self.smsUserInfoList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.smsUserInfoList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.closeAlert = function(index) {
        self.responseStr = false;
    };

    self.getSmsUserInfoByTerm = function(){
        smsUserInfoService.getSmsUserInfoForMgnt(self.amount).then(function (data) {
            self.smsUserInfoList = data;

            self.tableParams = new NgTableParams({}, { dataset: self.smsUserInfoList});
        });
    }

    self.promptDelete = function(id){
        self.deletingId = self.deletingId ? false : id;
    }
    self.resetDelete = function(){
        self.deletingId = false;
    }

	self.setTheOne = function(one){
		self.theSmsUserInfo = one;
		self.responseStr = false;
		self.responseStrFail = false;
	}
	
	self.clear = function(){
		self.responseStr = false;
		self.responseStrFail = false;
		self.theSmsUserInfo = new smsUserInfoDO;
		self.picFile = null;
	}

	self.setStyle = function(status){
        if(status==0){
            self.statusStyle.color = "crimson";
        }else if(status==1){
            self.statusStyle.color = "blue";
        }
        return self.statusStyle;
    }

////////  sms queue//////
    smsQueueService.getDataForMgnt(self.amount).then(function (data) {
        self.smsQueueList = data;
        console.log(data);
        self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
    });

    self.getSmsQueueByTerm = function(){
        smsQueueService.getDataForMgnt(self.amount).then(function (data) {
            self.smsQueueList = data;
            console.log(data);
            self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
        });
    }

    self.setTheSmsQueue = function(one){
        self.theSmsQueue = one;
        self.responseStrSmsQueue = false;

    }

    self.clearSmsQueue = function(){
        self.responseStrSmsQueue = false;
        self.theSmsQueue = new smsQueueDO();
    }

    self.upsertSmsQueue = function(one){
        self.isSaveButtonPressed=true;
        self.responseStrSmsQueue = false;
        smsQueueService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            console.log(data);
            if(one.id == 0){
                self.smsQueueList.unshift(data.smsQueue);
                self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
            }
        });
    }

    self.deleteSmsQueue = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        smsQueueService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.smsQueueList.indexOf(one);
            self.smsQueueList.splice(index,1);
            self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

////////  sms job //////



}]);