angular.module('app')
.service('APIInterceptor', function($q,$rootScope, memberService,) {
	var service = this;

    service.request = function(config) {
        var currentMember = memberService.getCurrentMember();
        var	authen_token = currentMember ? currentMember.token : null;
        if (authen_token) {
            config.headers.authorization = authen_token;
        }
        return config;
    };
    service.responseError = function(response) {
    	 if (response.status === 401) {
             $rootScope.$broadcast('unauthorized');
         }
         console.log(response);
    	 if (response.status === 500 && response.data.path.includes('mgnt')) {
    	    $rootScope.$broadcast('ExpiredJwt');
    	    console.log('interrupt');
         }
         return $q.reject(response);;
    };
    
});