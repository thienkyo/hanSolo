'use strict';
angular.module('smsUserInfoModule')
.factory('smsUserInfoService', ['ajaxService',function(ajaxService) {
	var smsUserInfoService = {
			getSmsUserInfoForMgnt : getSmsUserInfoForMgnt,
			upsert : upsert,
			deleteSmsUserInfo : deleteSmsUserInfo
			};
	return smsUserInfoService;

	function getSmsUserInfoForMgnt(amount){
		var url = "mgnt/getSmsUserInfoForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}   
    
	function upsert(smsUserInfo){
		var url = "mgnt/upsertSmsUserInfo";
		return ajaxService.post(url,smsUserInfo,{}).then(function(response){
			return response.data;
		});
	}

	function deleteSmsUserInfo(one){
        var url = "mgnt/deleteSmsUserInfo";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

 }])
.factory('smsQueueService', ['ajaxService',function(ajaxService) {
	var smsQueueService = {
			getDataForMgnt : getDataForMgnt,
			upsert : upsert,
			deleteOne : deleteOne
			};
	return smsQueueService;

	function getDataForMgnt(amount){
		var url = "mgnt/getSmsQueueForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertSmsQueue";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function deleteOne(one){
        var url = "mgnt/deleteSmsQueue";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

 }])
.factory('smsJobService', ['ajaxService',function(ajaxService) {
	var smsJobService = {
			getDataForMgnt : getDataForMgnt,
			upsert : upsert,
			deleteOne : deleteOne
			};
	return smsJobService;

	function getDataForMgnt(amount){
		var url = "mgnt/getSmsQueueForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertSmsQueue";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function deleteOne(one){
        var url = "mgnt/deleteSmsQueue";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

 }])
;
