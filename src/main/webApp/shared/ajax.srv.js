angular.module('networkServices',[])
	.factory('ajaxService',['$http','$cookies','$location',function($http,$cookies,$location){
		var host = $location.host();
		var urlbase = $location.protocol()+"://"+$location.host()+":"+$location.port()+"/"; // dev
		var sessionid = $cookies.get('JSESSIONID');
		//console.log(urlbase);
		var config = {
				headers:{
					'Accept': 'application/json',
					'Content-Type': 'application/json' ,
					'requestType':'angularJS',
					'Cache-Control': 'no-cach, no-store, must-revalidate',
					'Pragame':'no-catch',
					'X-Testing': sessionid,
					'Expries': 0,
					action: ''
				},
				params:{}
		};

		var config2 = {
                headers:{
                    'Accept': 'application/json',
                    'Content-Type': 'application/json' ,
                    'requestType':'angularJS',
                    'Cache-Control': 'no-cach, no-store, must-revalidate',
                    'Pragame':'no-catch',
                    'X-Testing': sessionid,
                    'Expries': 0,
                    'Access-Control-Allow-Origin':'*',
                    action: ''
                },
                params:{}
        };

		var ajaxService = {
			get : get,
			post: post,
			post2: post2
		};

		return ajaxService;

		function get(url,action,params){
			close.params = params || {};
			config.params["timeStamp"] = (new Date()).getTime();
			config.headers.action = action || '';
            url = urlbase + url;
			return $http.get(url,config);
		}

		function post(url,data,action,params) {
			close.params = params || {};
			config.params["timeStamp"] = (new Date()).getTime();
			config.headers.action = action || '';
			url = urlbase + url;
			return $http.post(url,data,config);
		}

		function post2(url,data,action,params) {
            close.params = params || {};
            config2.params["timeStamp"] = (new Date()).getTime();
            config2.headers.action = action || '';
            return $http.post(url,data,config2);
        }
}]);