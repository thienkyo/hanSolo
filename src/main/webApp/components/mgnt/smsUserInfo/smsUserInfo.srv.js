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

	function deleteSmsUserInfo(smsUserInfo){
        var url = "mgnt/deleteSmsUserInfo";
        return ajaxService.post(url,smsUserInfo,{}).then(function(response){
            return response.data;
        });
    }

 }]);
