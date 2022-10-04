'use strict';
angular.module('couponModule')
.factory('couponService', ['ajaxService',function(ajaxService) {
		var couponService = {
				getAllCoupons : getAllCoupons,
				upsert : upsert,
				deleteCoupon : deleteCoupon
			};
	return couponService;

	/* management from here*/
	function getAllCoupons(){
		var url = "mgnt/getAllCoupons";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(coupon){
		var url = "mgnt/upsertCoupon";
		return ajaxService.post(url,coupon,{}).then(function(response){
			return response.data;
		});
	}

	function deleteCoupon(coupon){
		var url = "mgnt/deleteCoupon";
		return ajaxService.post(url,coupon,{}).then(function(response){
			return response.data;
		});
	}

 }]);