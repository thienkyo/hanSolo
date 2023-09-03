'use strict';
angular.module('loginModule')
.factory('loginService', ['ajaxService',function(ajaxService) {
		var service = {
			login : login,
			logout : logout,
			signup : signup
			};
	return service;

	function login(loginRequest){
		var url = "member/login";
		return ajaxService.post(url,loginRequest,null,{}).then(function(response){
			return response.data;
		});
	}

	function logout(){
        var url = "mgnt/logout";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function signup(signupRequest){
		var url = "member/add";
		return ajaxService.post(url,signupRequest,null,{}).then(function(response){
			return response.data;
		});
	}
 }]);
