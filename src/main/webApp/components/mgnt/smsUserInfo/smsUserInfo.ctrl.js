'use strict';
angular.module('smsUserInfoModule')
	.controller('smsUserInfoController',['$rootScope','$location','memberService','smsUserInfoService','AmountList',
									'NgTableParams','smsUserInfoDO','uploadService','$timeout',
									'FirstTimeLoadSize',
	function($rootScope,$location,memberService,smsUserInfoService,AmountList,
	        NgTableParams,smsUserInfoDO,uploadService,$timeout,
	        FirstTimeLoadSize) {
	var self = this;
	self.theSmsUserInfo = new smsUserInfoDO;
	self.statusStyle = {};
	self.isSaveButtonPressed=false;
	self.tempArray=[];
	self.tempAmount=0;
	self.OneDayExpense={};

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	self.amountList=AmountList;
    self.amount = FirstTimeLoadSize;
	
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
}]);