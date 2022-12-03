'use strict';
angular.module('bizReportModule')
.factory('bizReportService', ['ajaxService',function(ajaxService) {
		var bizReportService = {
				getAll : getAll,
				upsert : upsert,
				calculateReport : calculateReport,
				deleteOne : deleteOne
			};
	return bizReportService;

	/* management from here*/
	function getAll(){
		var url = "mgnt/getAllBizReport";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "mgnt/upsertBizReport";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function calculateReport(one){
        var url = "mgnt/calculateReport";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

	function deleteOne(one){
		var url = "mgnt/deleteBizReport";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}


 }]);