'use strict';
angular.module('couponModule')
.controller('couponController', ['$scope','couponService','NgTableParams','memberService','CommonStatusArray','CouponDO','Upload','$timeout','uploadService',
	function($scope,couponService,NgTableParams,memberService,CommonStatusArray,CouponDO,Upload,$timeout,uploadService) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theCoupon = new CouponDO;

		if(!memberService.isAdmin()){
			$location.path('#/');
		}
		self.currentMember = memberService.getCurrentMember();
		
		couponService.getAllCoupons().then(function (data) {
		    console.log(data);
			self.couponList = data
			self.tableParams = new NgTableParams({}, { dataset: self.couponList});
		});
		
		self.updateCoupon = function(coupon){
			self.theCoupon = coupon;
			self.responseStr = false;
			self.responseStrFail = false;
		}
		
		self.upsert = function(coupon){
		    console.log(coupon);

			self.responseStr = false;
			self.responseStrFail = false;
			couponService.upsert(coupon).then(function (data) {
			    console.log(data);
				self.responseStr = data.errorMessage;
				if(coupon.id == 0){
					self.couponList.unshift(data.coupon);
					self.tableParams = new NgTableParams({}, { dataset: self.couponList});
				}
			});
		}
		
	/*	self.deleteCoupon = function(cate){
			self.responseStr = false;
			self.responseStrFail = false;
			couponService.deleteCoupon(cate).then(function (data) {
				self.responseStr = data;
				var index = self.cateList.indexOf(cate);
				self.cateList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.cateList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}*/
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theCoupon = new CouponDO;
			self.isShowUploadPic = false;
		}
		
}]);

