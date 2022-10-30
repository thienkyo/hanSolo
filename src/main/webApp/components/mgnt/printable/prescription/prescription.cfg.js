angular.module('prescriptionModule')
.config(function($routeProvider,$httpProvider){
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.interceptors.push('APIInterceptor');
});