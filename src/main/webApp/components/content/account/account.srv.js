'use strict';
angular.module('accountModule')
.factory('accountService', ['ajaxService',function(ajaxService) {
	var accountService = {
			getMe : getMe,
			updateMe : updateMe
			};
	return accountService;

   function getMe(){
		var url = "authenticated/me";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
   }
   
   function updateMe(me){
	   var url = "authenticated/updateMe";
		return ajaxService.post(url,me,{}).then(function(response){
			return response.data;
		});
   }
	      
 }]);
