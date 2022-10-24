'use strict';
angular.module('bannerModule')
.factory('bannerService', ['ajaxService',function(ajaxService) {
	var bannerService = {
			getBannerForMgnt : getBannerForMgnt,
			upsert : upsert,
			deleteBanner : deleteBanner
			};
	return bannerService;

	function getBannerForMgnt(){
		var url = "mgnt/getBannerForMgnt";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}   
    
	function upsert(banner){
		var url = "mgnt/upsertBanner";
		return ajaxService.post(url,banner,{}).then(function(response){
			return response.data;
		});
	}

	function deleteBanner(banner){
        var url = "mgnt/deleteBanner";
        return ajaxService.post(url,banner,{}).then(function(response){
            return response.data;
        });
    }
	
 }]);
