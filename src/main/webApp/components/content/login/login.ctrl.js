'use strict';
angular.module('loginModule')
.controller('loginController',['$rootScope','loginService','clientInfoCacheService','shopListCacheService',
							   '$location','MemberDO','memberService','$mdDialog','currentShopCacheService',
	function($rootScope, loginService,clientInfoCacheService,shopListCacheService,
	         $location,MemberDO,memberService,$mdDialog,currentShopCacheService) {

	var self = this;
	self.newMember = new MemberDO();
    self.member = {};
    self.credential ={};
    
	self.login = function() {
		if(self.credential.phone && self.credential.pass){
			loginService.login({loginStr: memberService.makeLoginStr(self.credential.phone,btoa(self.credential.pass))}).then(function(data) {
			    //console.log(data);
			    if(data.errorCode == 'SUCCESS'){

			        self.member.token =  'sheep ' + data.obj.token;
                    var arr = data.obj.data.split('.');
                    var decodedString = decodeURIComponent(escape(atob(data.obj.data)));
                    var a = angular.fromJson(decodedString);
                    self.member.roles = a.roles;
                    self.member.name = a.name;
                    self.member.phone = a.sub;
                    self.member.shopCode = a.shopCode;
                    memberService.setCurrentMember(self.member);

                    $rootScope.$broadcast('authorized');
                    $location.path('#/');
			    }else{
			        self.loginError = data.errorMessage;
			    }
            },
            function(error){

            	self.loginError = error.data.error;
            });
		}else{
			self.loginError = 'phone và pass cannot be empty';
		}     
    }

	self.signup = function(){
		if(self.newMember.phone && self.newMember.fullName && self.newMember.pass){
			loginService.signup({signupStr: memberService.makeLoginStr(self.newMember.phone,btoa(self.newMember.pass)), fullName:self.newMember.fullName, email:self.newMember.email}).then(function(data) {
                if(data.errorCode == 'SUCCESS'){
                    self.reset();
                    self.showAlert();
                }else{
                    self.signUpError = data.errorMessage;
                }
            },
            function(error){
            	self.signUpError = error.data.error;
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