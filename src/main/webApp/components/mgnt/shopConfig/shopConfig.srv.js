'use strict';
angular.module('shopConfigModule')
.factory('shopConfigService', ['ajaxService',function(ajaxService) {
		var shopConfigService = {
                getDataForMgnt : getDataForMgnt,
                upsert : upsert,
                refreshShopConfig : refreshShopConfig,
                deleteOne : deleteOne,
                deleteMany : deleteMany
			};
	return shopConfigService;

	function getDataForMgnt(){
        var url = "mgnt/getShopConfig/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function upsert(one){
        var url = "mgnt/upsertShopConfig";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

    function refreshShopConfig(){
        var url = "mgnt/refreshShopConfig/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function deleteOne(one){
        var url = "mgnt/deleteLensProduct";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

    function deleteMany(many){
        var url = "mgnt/deleteManyLensProduct";
        return ajaxService.post(url,many,{}).then(function(response){
            return response.data;
        });
    }
      
 }]);