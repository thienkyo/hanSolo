'use strict';
angular.module('loginModule')
.factory('loginService', ['ajaxService',function(ajaxService) {
		var loginService = {
			login : login,
			signup : signup
			};
	return loginService;

	function login(loginRequest){
		var url = "member/login";
		return ajaxService.post(url,loginRequest,null,{}).then(function(response){
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
