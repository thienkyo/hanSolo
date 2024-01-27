'use strict';
angular.module('smsUserInfoModule')
	.controller('smsUserInfoController',['$rootScope','$location','memberService','smsUserInfoService','AmountList',
									'NgTableParams','SmsUserInfoDO','uploadService','$timeout','JobTypeList',
									'FirstTimeLoadSize','SmsQueueDO','smsQueueService','SmsJobDO','smsJobService',
									'CommonStatusArray','specificSmsUserInfoService','AreaCodeList','genderArray',
									'strategyService','StrategyDO','clientInfoCacheService','LuckyDrawRequest','programService',
									'queryRequestDO',
	function($rootScope,$location,memberService,smsUserInfoService,AmountList,
	        NgTableParams,SmsUserInfoDO,uploadService,$timeout,JobTypeList,
	        FirstTimeLoadSize,SmsQueueDO,smsQueueService,SmsJobDO,smsJobService,
	        CommonStatusArray,specificSmsUserInfoService,AreaCodeList,genderArray,
	        strategyService,StrategyDO,clientInfoCacheService,LuckyDrawRequest,programService,
	        queryRequestDO) {
	var self = this;
	self.theSmsUserInfo = new SmsUserInfoDO();
	self.theSmsQueue = new SmsQueueDO();
	self.theSmsJob = new SmsJobDO();
	self.theStrategy = new StrategyDO();
	self.luckyDrawReq = new LuckyDrawRequest();
	self.theSpecificSmsUserInfo = new SmsUserInfoDO();
	self.jobTypeList = JobTypeList; // sms job
	self.statusList = CommonStatusArray;
	self.AreaCodeList = AreaCodeList;
	self.genderArray=genderArray;
	self.statusStyle = {};
	self.isSaveButtonPressed=false;
	self.tempArray=[];
	self.tempAmount=0;
	self.OneDayExpense={};
	self.isAdmin = memberService.isAdmin();
	self.isSuperAdmin = memberService.isSuperAdmin();
	self.queryRequest = queryRequestDO;

    // temp logic for production and dev
    if($location.host().includes('opticshop')){
        self.luckyDrawReq.shopCode = 'S7LJ6W';
    }

	self.queryRequest.clientCode = self.luckyDrawReq.clientCode;
	self.queryRequest.shopCode = self.luckyDrawReq.shopCode;

	if(!memberService.isAdmin() || !clientInfoCacheService.get().isUnlockSmsFeature){
		$location.path('#/');
	}

	self.amountList=AmountList;
    self.amount = FirstTimeLoadSize;
    self.smsQueueAmount = FirstTimeLoadSize;

	smsUserInfoService.getSmsUserInfoForMgnt(self.amount).then(function (data) {
		self.smsUserInfoList = data;
		self.tableParams = new NgTableParams({}, { dataset: self.smsUserInfoList});
	});


	self.upsert = function(smsUserInfo){
	    self.isSaveButtonPressed=true;
		self.responseStr = false;
		self.responseStrFail = false;
		smsUserInfoService.upsert(smsUserInfo).then(function (data) {
			self.responseStr = data;
			self.isSaveButtonPressed=false;
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
        self.responseStrFail = false;
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
    smsQueueService.getDataForMgnt(self.smsQueueAmount).then(function (data) {
        self.smsQueueList = data;
        self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
    });

    self.prepareData = function(){
        smsQueueService.prepareData(self.smsQueueAmount).then(function (data) {
            self.responseStr = true;
        });
    }

    self.loadSmsQueue = function(){
        smsQueueService.getDataForMgnt(self.smsQueueAmount).then(function (data) {
            self.smsQueueList = data;
            self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
        });
    }

    self.getSmsQueueByTerm = function(){
        smsQueueService.getDataForMgnt(self.smsQueueAmount).then(function (data) {
            self.smsQueueList = data;
            self.smsQueueTableParams = new NgTableParams({}, { dataset: self.smsQueueList});
        });
    }

     self.delete100SmsQueue = function(){
        self.responseStr = false;
        smsQueueService.delete100().then(function (data) {
            self.responseStr = data;
            self.isDelete100 = false;
        });
    }

    self.promptDelete100 = function(){
        self.isDelete100 = true;
    }
    self.resetDelete100 = function(){
        self.isDelete100 = false;
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

    smsJobService.getLastHeartBeatTime().then(function (data) {
        self.lastHeartBeatTime = data;
    });
    smsJobService.getLastPrepareDataTime().then(function (data) {
        self.lastPrepareDataTime = data;
    });

    /// for sms send api
    smsJobService.getSmsSendStatus().then(function (data) {
        self.isRunSmsSend = data;
    });

    self.toggleSmsSend = function(){
        self.isSaveButtonPressed=true;
        smsJobService.toggleSmsSend().then(function (data) {
            self.isSaveButtonPressed=false;
            self.isRunSmsSend = data;
        });
    }

    /// for sms data prepare api
    smsJobService.getSmsDataPrepareStatus().then(function (data) {
        self.isRunSmsDataPrepare = data;
    });

    self.toggleSmsDataPrepare = function(){
        self.isSaveButtonPressed=true;
        smsJobService.toggleSmsDataPrepare().then(function (data) {
            self.isSaveButtonPressed=false;
            self.isRunSmsDataPrepare = data;
        });
    }


    smsJobService.getDataForMgnt(0).then(function (data) {
        self.smsJobList = data;
        console.log(data);
        self.smsJobTableParams = new NgTableParams({}, { dataset: self.smsJobList});
        self.luckyDrawReq.smsJobId = self.smsJobList.find(i => i.jobType == 'LUCKYDRAW').id;
    });

    self.upsertSmsJob = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        smsJobService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            if(one.id == 0){
                self.smsJobList.unshift(data.smsJob);
                self.smsJobTableParams = new NgTableParams({}, { dataset: self.smsJobList});
            }
        });
    }

    // open datePicker
    self.openDP = function() {
        self.DPisOpen = true;
        self.isPickDP = true;
    };

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

    self.clearTheSpecificSmsUserInfo = function(){
        self.responseStr = false;
        self.theSpecificSmsUserInfo = new SmsUserInfoDO();
    }

///////////  strategy //////
    self.genderArrayStrategy=[
    	{name : 'All', value:"ALL" },
    	{name : 'Male', value:"MALE" },
    	{name : 'Female', value:"FEMALE" }
    ];
    self.locationArrayStrategy=[
        {name : 'All', value:"ALL" },
        {name : 'NEARHCM', value:"NEARHCM" },
        {name : 'OTHERS', value:"OTHERS" }
    ];
    // open datePicker
    self.openDPStrategy = function() {
        self.DPisOpenStrategy = true;
        self.isPickDPStrategy = true;
    };

    self.setTheStrategy = function(one){
        self.theStrategy = one;
        self.responseStr = false;
    }
    self.getStrategyByTerm = function(){
        strategyService.getDataForMgnt(0).then(function (data) {
            self.strategyList = data;
            self.strategyTableParams = new NgTableParams({}, { dataset: self.strategyList});
        });
    }

    self.upsertStrategy = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        strategyService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            if(one.id == 0){
                self.strategyList.unshift(data.obj);
                self.strategyTableParams = new NgTableParams({}, { dataset: self.strategyList});
            }
        });
    }

    self.deleteStrategy = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        strategyService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.smsJobList.indexOf(one);
            self.smsJobList.splice(index,1);
            self.smsJobTableParams = new NgTableParams({}, { dataset: self.smsJobList});

        });
    }

//////////////  lucky draw section //////
    console.log(self.queryRequest);
    programService.getDataForMgnt(self.queryRequest).then(function (data) {
        self.programResultList = data;
        self.programResultTableParams = new NgTableParams({}, { dataset: self.programResultList});
    });

    self.getProgramResultForMgnt = function(req){
        self.programResultList = [];
        self.programResultTableParams = new NgTableParams({}, { dataset: self.programResultList});
        programService.getDataForMgnt(self.queryRequest).then(function (data) {
            self.programResultList = data;
            console.log(data);
            self.programResultTableParams = new NgTableParams({}, { dataset: self.programResultList});
        });
    }

    self.saveProgramResult = function(req){
        self.isSaveButtonPressed = true;
        self.responseStr = false;
        self.responseStrFail = false;
        console.log(req);
        if(req.orderIdList == ''){
            self.responseStrFail = 'pls input orderId';
            self.isSaveButtonPressed = false;
            return;
        }
        programService.saveResult(req).then(function (data) {
             self.isSaveButtonPressed = false;
             console.log(data);
             if(data.errorCode == 'SUCCESS' ){
                console.log(data.obj);
                self.responseStr = "Thành công";

                data.obj.forEach((dataOne, index, array) => {
                    self.programResultList.unshift(dataOne);
                });

                self.programResultTableParams = new NgTableParams({}, { dataset: self.programResultList});
             }else{
                self.responseStrFail = data.errorMessage;
             }
        });
    }

    self.prepareCouponAndSms = function(req){
        self.isSaveButtonPressed = true;
        self.responseStr = false;
        self.responseStrFail = false;
        programService.prepareCouponAndSms(req).then(function (data) {
            console.log(data);
            self.isSaveButtonPressed = false;
            if(data.errorCode == 'SUCCESS' ){
            self.responseStr = 'Thành công';
            self.programResultList2 = self.programResultList.map(obj => data.obj.find(o => o.orderId == obj.orderId) || obj);

            self.programResultTableParams = new NgTableParams({}, { dataset: self.programResultList2});
            }else{
            self.responseStrFail = data.errorMessage;
            }
        });
    }

    self.deleteProgramResult = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        programService.deleteOne(one).then(function (data) {
            self.responseStr = data.errorMessage;
            var index = self.programResultList.indexOf(one);
            self.programResultList.splice(index,1);
            self.programResultTableParams = new NgTableParams({}, { dataset: self.programResultList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.setStyle = function(status){
        if(status==0){
            self.statusStyle.color = "crimson";
        }else if(status==1){
            self.statusStyle.color = "blue";
        }
        return self.statusStyle;
    }
/////////////////////

}]);