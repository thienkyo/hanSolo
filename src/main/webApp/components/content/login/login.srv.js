'use strict';
angular.module('loginModule')
.factory('loginService', ['ajaxService',function(ajaxService) {
		var loginService = {
			login2 : login2,
			signup2 : signup2
			};
	return loginService;

	function login2(loginRequest){
		var url = "members/login";
		return ajaxService.post(url,loginRequest,null,{}).then(function(response){
			return response.data.token;
		});
	}

	function signup2(signupRequest){
		var url = "members/add";
		return ajaxService.post(url,signupRequest,null,{}).then(function(response){
			return response.data.replyStr;
		});

	}
      
 }]);
