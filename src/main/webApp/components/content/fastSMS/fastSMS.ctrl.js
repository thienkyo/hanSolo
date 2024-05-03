'use strict';
angular.module('fastSMSModule')
.controller('fastSMSController',['$rootScope','fastSMSService','$location','SmsQueueDO','clientInfoCacheService','currentShopCacheService',
                                'oneClientShopListCacheService',
	function($rootScope, fastSMSService,$location,SmsQueueDO,clientInfoCacheService,currentShopCacheService,
	        oneClientShopListCacheService
	){
	var self = this;
	self.theSmsQueue = new SmsQueueDO();

	self.shopList = oneClientShopListCacheService.get();

    fastSMSService.getSMSConfig().then(function (data) {
		self.theSmsQueue.content = data.smsJob.msgContentTemplate;
		self.theSmsQueue.receiverName = 'fastSMS';
		self.theSmsQueue.jobId = data.smsJob.id;
		self.theSmsQueue.weight = data.smsJob.weight;
		self.theSmsQueue.jobType = data.smsJob.jobType;
		self.theSmsQueue.clientCode  = clientInfoCacheService.get().clientCode;
        self.theSmsQueue.shopCode = currentShopCacheService.get().shopCode;
		console.log(self.theSmsQueue);
		console.log(data);
	});

	self.addQueue = function(one){
	    one.gmtCreate = (new Date()).getTime();
        one.gmtModify = (new Date()).getTime();
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        self.responseStrFail = false;
        fastSMSService.upsert(one).then(function (data) {

            if(data.errorCode == 'SUCCESS'){
                self.responseStr = data;
                self.isErrorMsg = false;
            }else{
               self.responseStrFail = data.errorMessage;
            }
            self.isSaveButtonPressed = false;

        });
    }

    self.closeAlert = function() {
        self.responseStr = false;
    };

    self.clear = function() {
        self.theSmsQueue.receiverPhone = '';
        self.responseStrFail = null;
    };

}]);