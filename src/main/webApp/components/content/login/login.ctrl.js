'use strict';
angular.module('loginModule')
.controller('loginController',['$rootScope','loginService',
							   '$location','MemberDO','memberService','$mdDialog',
	function($rootScope, loginService,$location,MemberDO,memberService,$mdDialog) {
	
	var self = this;
	self.newMember = new MemberDO();
    self.member = {};
    self.credential ={};
    
	self.login = function() {
		if(self.credential.phone && self.credential.pass){
			loginService.login({loginStr: memberService.makeLoginStr(self.credential.phone,btoa(self.credential.pass))}).then(function(token) {
            	self.member.token =  'sheep ' + token;
            	var arr = token.split('.');
            	var decodedString = atob(arr[1]);
                var a = angular.fromJson(decodedString);
                self.member.roles = a.roles;
                self.member.name = a.name;
                console.log(self.member);
               
                memberService.setCurrentMember(self.member);
                $rootScope.$broadcast('authorized');
                $location.path('#/');
            },
            function(error){
            	self.loginError = 'invalid login';
            });
		}else{
			self.loginError = 'phone và pass cannot be empty';
		}     
    }

	self.signup = function(){
		if(self.newMember.phone && self.newMember.fullName && self.newMember.pass){
			loginService.signup({signupStr: memberService.makeLoginStr(self.newMember.phone,btoa(self.newMember.pass)), fullName:self.newMember.fullName, email:self.newMember.email}).then(function(replyStr) {
				self.reset();
				self.showAlert();
            },
            function(error){
            	if(error.data.message == 'phoneExists'){
            		self.signUpError = 'phone existed';
            	}
            });
		}else{
			self.signUpError = 'Name, phone, password cannot be empty';
		}
	};
	
	self.showAlert = function() {
	    $mdDialog.show(
	      $mdDialog.alert()
	        .parent(angular.element(document.body))
	        .clickOutsideToClose(true)
	        .title('SignUp successfully')
	        .textContent('Pls sign in and start shopping.')
	        .ariaLabel('SignUp successfully')
	        .ok('OK')
	    );
	};
	
	self.reset = function(){
		self.loginError = null;
		self.signUpError = null;
		self.newMember.phone = '';
		self.newMember.fullName = '';  
		self.newMember.pass = '';
		self.newMember.phone ='';
	}

}]);