'use strict';
angular.module('opticShopOnlineModule')
.factory('opticShopOnlineService', ['ajaxService',function(ajaxService) {
		var service = {
                getDataForMgnt : getDataForMgnt,
                upsert : upsert,
                refreshShopConfig : refreshShopConfig
			};
	return service;

	function getDataForMgnt(){

        var url = "Hmgnt/getOSOConfig/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function upsert(one){
        var url = "Hmgnt/upsertOSOConfig";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

    function refreshShopConfig(){
        var url = "Hmgnt/refreshOSOConfig/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }
      
 }]);