'use strict';
angular.module('memberListModule')
.factory('memberListService', ['ajaxService',function(ajaxService) {
		var service = {
			getMembersForMgnt : getMembersForMgnt,
			upsert : upsert,
			insertByClient : insertByClient,
			updateMemberStatus : updateMemberStatus,
			deleteRole : deleteRole,
			upsertRole : upsertRole
			};
	return service;
	
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

	function insertByClient(client){
        var url = "Hmgnt/insertMemberByClient";
        return ajaxService.post(url,client,{}).then(function(response){
            return response.data;
        });
    }

	function updateMemberStatus(mem){
        var url = "mgnt/updateMemberStatus";
        return ajaxService.post(url,mem,{}).then(function(response){
            return response.data;
        });
    }

	function upsertRole(role){
        var url = "mgnt/upsertMemberRole";
        return ajaxService.post(url,role,{}).then(function(response){
            return response.data;
        });
    }

    function deleteRole(role){
        var url = "mgnt/deleteMemberRole";
        return ajaxService.post(url,role,{}).then(function(response){
            return response.data;
        });
    }
      
 }]);