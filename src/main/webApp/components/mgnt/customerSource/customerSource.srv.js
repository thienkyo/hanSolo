'use strict';
angular.module('customerSourceModule')
.factory('customerSourceService', ['ajaxService',function(ajaxService) {
		var customerSourceService = {
				getAll : getAll,
				upsert : upsert,
				deleteOne : deleteOne,
				getReportAll : getReportAll,
				calculateReport : calculateReport,
				upsertReport : upsertReport
			};
	return customerSourceService;

	/* management from here*/
	function getAll(){
		var url = "mgnt/getAllCustomerSource";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function getReportAll(){
        var url = "mgnt/getAllCustomerSourceReport";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function upsert(one){
		var url = "mgnt/upsertCustomerSource";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function calculateReport(one){
	    console.log("calculateReport srv");
        var url = "mgnt/calCustomerSourceReport";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

	function upsertReport(one){
        var url = "mgnt/upsertCustomerSourceReport";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

	function deleteOne(one){
		var url = "mgnt/deleteCoupon";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}


 }]);