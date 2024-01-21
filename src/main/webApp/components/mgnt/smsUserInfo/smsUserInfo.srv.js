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
			prepareData : prepareData,
			delete100 : delete100,
			deleteOne : deleteOne
			};
	return smsQueueService;

	function getDataForMgnt(amount){
		var url = "mgnt/getSmsQueueForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function prepareData(){
        var url = "api/prepareSmsData/";
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

    function delete100(){
        var url = "mgnt/delete100SmsQueue";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

 }])
.factory('smsJobService', ['ajaxService',function(ajaxService) {
	var smsJobService = {
			getDataForMgnt : getDataForMgnt,
			upsert : upsert,
			getLastHeartBeatTime : getLastHeartBeatTime,
			getLastPrepareDataTime : getLastPrepareDataTime,
			toggleSmsDataPrepare : toggleSmsDataPrepare,
			getSmsDataPrepareStatus : getSmsDataPrepareStatus,
			toggleSmsSend : toggleSmsSend,
			getSmsSendStatus : getSmsSendStatus,
			deleteOne : deleteOne
			};
	return smsJobService;

	function getDataForMgnt(amount){
		var url = "mgnt/getSmsJobForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertSmsJob";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function toggleSmsDataPrepare(){
        var url = "mgnt/togglePrepareSmsData";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function getSmsDataPrepareStatus(){
        var url = "mgnt/getSmsDataPrepareStatus";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function toggleSmsSend(){
        var url = "mgnt/toggleSmsSend";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function getSmsSendStatus(){
        var url = "mgnt/getSmsSendStatus";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function getLastHeartBeatTime(){
        var url = "mgnt/getLastHeartBeatTime";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function getLastPrepareDataTime(){
        var url = "mgnt/getLastPrepareDataTime";
        return ajaxService.post(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function deleteOne(one){
        var url = "mgnt/deleteSmsJob";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }
 }])
.factory('specificSmsUserInfoService', ['ajaxService',function(ajaxService) {
 	var specificSmsUserInfoService = {
 			getDataForMgnt : getDataForMgnt,
 			upsert : upsert,
 			deleteOne : deleteOne
 			};
 	return specificSmsUserInfoService;

 	function getDataForMgnt(amount){
 		var url = "mgnt/getSpecificSmsUserInfoForMgnt/"+amount;
 		return ajaxService.get(url,null,{}).then(function(response){
 			return response.data;
 		});
 	}

 	function upsert(one){
 		var url = "mgnt/upsertSpecificSmsUserInfo";
 		return ajaxService.post(url,one,{}).then(function(response){
 			return response.data;
 		});
 	}

 	function deleteOne(one){
         var url = "mgnt/deleteSpecificSmsUserInfo";
         return ajaxService.post(url,one,{}).then(function(response){
             return response.data;
         });
    }
 }])
.factory('strategyService', ['ajaxService',function(ajaxService) {
 	var strategyService = {
 			getDataForMgnt : getDataForMgnt,
 			upsert : upsert,
 			deleteOne : deleteOne
 			};
 	return strategyService;

 	function getDataForMgnt(amount){
 		var url = "mgnt/getStrategyForMgnt/"+amount;
 		return ajaxService.get(url,null,{}).then(function(response){
 			return response.data;
 		});
 	}

 	function upsert(one){
 		var url = "mgnt/upsertStrategy";
 		return ajaxService.post(url,one,{}).then(function(response){
 			return response.data;
 		});
 	}

 	function deleteOne(one){
         var url = "mgnt/deleteStrategy";
         return ajaxService.post(url,one,{}).then(function(response){
             return response.data;
         });
    }
 }])
 .factory('programService', ['ajaxService',function(ajaxService) {
  	var service = {
  			getDataForMgnt : getDataForMgnt,
  			saveResult : saveResult,
  			prepareCouponAndSms : prepareCouponAndSms,
  			deleteOne : deleteOne
  			};
  	return service;

  	function getDataForMgnt(req){
  		var url = "mgnt/program/getByClientCode";
  		return ajaxService.post(url,req,{}).then(function(response){
  			return response.data;
  		});
  	}

  	function saveResult(req){
  		var url = "mgnt/program/saveResult";
  		return ajaxService.post(url,req,{}).then(function(response){
  			return response.data;
  		});
  	}

  	function prepareCouponAndSms(req){
        var url = "mgnt/program/createCouponAndSms";
        return ajaxService.post(url,req,{}).then(function(response){
            return response.data;
        });
    }

  	function deleteOne(one){
          var url = "mgnt/program/delete";
          return ajaxService.post(url,one,{}).then(function(response){
              return response.data;
          });
     }
 }])
;
