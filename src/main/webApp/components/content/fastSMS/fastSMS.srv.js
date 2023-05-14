'use strict';
angular.module('fastSMSModule')
.factory('fastSMSService', ['ajaxService',function(ajaxService) {
		var fastSMSService = {
			getSMSConfig : getSMSConfig,
			upsert : upsert
			};
	return fastSMSService;

	function getSMSConfig(){
		var url = "member/getFastSMSConfig";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
        var url = "member/upsertSmsQueue";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }
 }]);
