'use strict';
angular.module('memberListModule')
.factory('memberListService', ['ajaxService',function(ajaxService) {
		var productListService = {
			getMembersForMgnt : getMembersForMgnt,
			upsert : upsert
			};
	return productListService;
	
	function getMembersForMgnt(amount){
		var url = "mgnt/getMemberForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	
	function upsert(mem){
		var url = "mgnt/upsertMember";
		return ajaxService.post(url,mem,{}).then(function(response){
			return response.data;
		});
	}
      
 }]);