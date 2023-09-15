'use strict';
angular.module('memberListModule')
.factory('memberListService', ['ajaxService',function(ajaxService) {
		var service = {
			getMembersForMgnt : getMembersForMgnt,
			getMemberByClientCode : getMemberByClientCode,
			getMemberByTerms : getMemberByTerms,
			upsert : upsert,
			upsertMemberByAdmin : upsertMemberByAdmin,
			insertByClient : insertByClient,
			updateMemberStatus : updateMemberStatus,
			deleteRole : deleteRole,
			upsertRole : upsertRole
			};
	return service;


	function getMemberByTerms(req){
        var url = "mgnt/getMemberByTerms";
        return ajaxService.post(url,req,{}).then(function(response){
            return response.data;
        });
    }

	function getMembersForMgnt(amount){
		var url = "mgnt/getMemberForMgnt/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function getMemberByClientCode(amount){
        var url = "mgnt/getMemberByClientCode/"+amount;
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

	function upsertMemberByAdmin(mem){
        var url = "mgnt/upsertMemberByAdmin";
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