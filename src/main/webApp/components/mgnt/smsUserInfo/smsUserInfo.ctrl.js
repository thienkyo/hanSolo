'use strict';
angular.module('smsUserInfoModule')
	.controller('smsUserInfoController',['$rootScope','$location','memberService','smsUserInfoService','AmountList',
									'NgTableParams','SmsUserInfoDO','uploadService','$timeout','JobTypeList',
									'FirstTimeLoadSize','SmsQueueDO','smsQueueService','SmsJobDO','smsJobService',
									'CommonStatusArray','specificSmsUserInfoService',
	function($rootScope,$location,memberService,smsUserInfoService,AmountList,
	        NgTableParams,SmsUserInfoDO,uploadService,$timeout,JobTypeList,
	        FirstTimeLoadSize,SmsQueueDO,smsQueueService,SmsJobDO,smsJobService,
	        CommonStatusArray,specificSmsUserInfoService) {
	var self = this;
	self.theSmsUserInfo = new SmsUserInfoDO();
	self.theSmsQueue = new SmsQueueDO();
	self.theSmsJob = new SmsJobDO();
	self.theSpecificSmsUserInfo = new SmsUserInfoDO();
	self.jobTypeList = JobTypeList; // sms job
	self.statusList = CommonStatusArray;
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
		console.log(data);
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
		self.theSmsUserInfo = new SmsUserInfoDO();
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
            self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
        });
    }

    self.setTheSmsQueue = function(one){
        self.theSmsQueue = one;
        self.responseStr = false;
    }

    self.clearSmsQueue = function(){
        self.responseStr = false;
        self.theSmsQueue = new SmsQueueDO();
    }

    self.upsertSmsQueue = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        smsQueueService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
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
    smsJobService.getDataForMgnt(0).then(function (data) {
        self.smsJobList = data;
        console.log(data);
        self.smsJobTableParams = new NgTableParams({}, { dataset: self.smsJobList});
    });

    self.upsertSmsJob = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        smsJobService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            console.log(data);
            if(one.id == 0){
                self.smsJobList.unshift(data.smsJob);
                self.smsJobTableParams = new NgTableParams({}, { dataset: self.smsJobList});
            }
        });
    }

    self.setTheSmsJob = function(one){
        self.theSmsJob = one;
        self.responseStr = false;
    }

    self.clearSmsJob = function(){
        self.responseStr = false;
        self.theSmsJob = new SmsJobDO();
    }

    self.deleteSmsJob = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        smsJobService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.smsJobList.indexOf(one);
            self.smsJobList.splice(index,1);
            self.smsJobTableParams = new NgTableParams({}, { dataset: self.smsJobList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

//////// specific sms user info //////
    self.loadSpecificSmsUserInfo = function(){
        specificSmsUserInfoService.getDataForMgnt(self.amount).then(function (data) {
                self.specificSmsUserInfoList = data;
                console.log(data);
                self.specificSmsUserInfoTableParams = new NgTableParams({}, { dataset: self.specificSmsUserInfoList});
            });
    }

    self.deleteSpecificSmsUserInfo = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        specificSmsUserInfoService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.specificSmsUserInfoList.indexOf(one);
            self.specificSmsUserInfoList.splice(index,1);
            self.specificSmsUserInfoTableParams = new NgTableParams({}, { dataset: self.specificSmsUserInfoList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.upsertSpecificSmsUserInfo = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        self.responseStrFail = false;
        specificSmsUserInfoService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            console.log(data);
            if(one.id == 0){
                self.specificSmsUserInfoList.unshift(data.specificSmsUserInfo);
                self.specificSmsUserInfoTableParams = new NgTableParams({}, { dataset: self.specificSmsUserInfoList});
            }
        });
    }

    self.setTheSpecificSmsUserInfo = function(one){
        self.theSpecificSmsUserInfo = one;
        self.responseStr = false;
    }

}]);