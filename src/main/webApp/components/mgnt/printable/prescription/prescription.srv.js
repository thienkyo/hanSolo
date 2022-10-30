'use strict';
angular.module('prescriptionModule')
.factory('prescriptionService', ['ajaxService',function(ajaxService) {
    var prescriptionService = {
        getOnePrescription : getOnePrescription
    };
	return prescriptionService;

	function getOnePrescription(id){
        var url = "mgnt/getOnePrescription/"+id;
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }
}])
.factory('memberService',['store', function(store) {
	var currentMember = null;
	var memberService = {
		getCurrentMember : getCurrentMember,
		isLogin : isLogin,
		isAdmin :isAdmin,
		isMod : isMod
		};
	return memberService;

	function getCurrentMember(){
		if (!currentMember) {
            currentMember = store.get('member');
        }
        return currentMember;
	}

	function isLogin(){
		if (!currentMember) {
            currentMember = store.get('member');
        }
		if(currentMember){
			return true;
		}
		return false;
	}

	function isAdmin(){
		if(isLogin() && currentMember.roles.indexOf("ADMIN") != -1){
			return true;
		}
		return false;
	}

	function isMod(){
		if(isLogin() && currentMember.roles.indexOf("MOD") != -1){
			return true;
		}
		return false;
	}

}])

 ;