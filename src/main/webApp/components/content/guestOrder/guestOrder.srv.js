'use strict';
angular.module('guestOrderModule')
.factory('guestOrderService', ['ajaxService',function(ajaxService) {
	var guestOrderService = {
			getGuestOrder : getGuestOrder
			};
	return guestOrderService;

   function getGuestOrder(phone){
		var url = "guest/getGuestOrder/"+phone;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
   }
 }]);
