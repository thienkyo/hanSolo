'use strict';
angular.module('couponModule')
.controller('couponController', ['$scope','couponService','NgTableParams','memberService','CommonStatusArray','CouponDO',
            'Upload','$timeout','uploadService','CouponTypeList','CouponCreatedByList',
	function($scope,couponService,NgTableParams,memberService,CommonStatusArray,CouponDO,
	         Upload,$timeout,uploadService,CouponTypeList,CouponCreatedByList) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.CouponTypeList = CouponTypeList;
		self.CouponCreatedByList = CouponCreatedByList;
		self.theCoupon = new CouponDO;
		self.statusStyle = {};
		self.discountOrderNumber = 0;
		self.totalDiscountAmount = 0;

		if(!memberService.isAdmin()){
			$location.path('#/');
		}
		//self.currentMember = memberService.getCurrentMember();
		
		couponService.getAllCoupons().then(function (data) {
			data.forEach(calculateExpiry);
			self.couponList = data;
			self.tableParams = new NgTableParams({}, { dataset: self.couponList});
		});

		couponService.loadUsedCouponHistory().then(function (data) {
		    //console.log(data);
		    data.forEach(calculateUserCoupon);
            self.usedCouponList = data;
            self.usedCouponTableParams = new NgTableParams({}, { dataset: self.usedCouponList});
        });
		
		self.updateCoupon = function(coupon){
			self.theCoupon = coupon;
			self.responseStr = false;
			self.responseStrFail = false;
		}
		
		self.upsert = function(coupon){

			self.responseStr = false;
			self.responseStrFail = false;
			couponService.upsert(coupon).then(function (data) {
				self.responseStr = data.errorMessage;
				if(coupon.id == 0){
					self.couponList.unshift(data.coupon);
					self.couponList.forEach(calculateExpiry);
					self.tableParams = new NgTableParams({}, { dataset: self.couponList});
				}
			});
		}
		
		self.deleteCoupon = function(coupon){
			self.responseStr = false;
			self.responseStrFail = false;
			couponService.deleteCoupon(coupon).then(function (data) {
				self.responseStr = data;
				var index = self.couponList.indexOf(coupon);
				self.couponList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.couponList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}

		self.promptDelete = function(id){
            self.deletingId = self.deletingId ? false : id;
        }
        self.resetDelete = function(){
            self.deletingId = false;
        }
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theCoupon = new CouponDO;
			self.isShowUploadPic = false;
		}

		function calculateExpiry(coupon){
            var modifyDate = new Date(coupon.gmtModify);
            modifyDate.setDate(modifyDate.getDate() + coupon.lifespan);

            if(modifyDate > new Date() && coupon.quantity >0){
                coupon.status = 1;
            }else{
                coupon.status = 0;
            }
        }

        function calculateUserCoupon(usedCoupon){
            usedCoupon.discountAmount = usedCoupon.couponValue*usedCoupon.orderAmount/100;
            self.totalDiscountAmount += usedCoupon.discountAmount;
        }

        self.setStyle = function(status){
            if(status==0){
                self.statusStyle.color = "crimson";
            }else if(status==1){
                self.statusStyle.color = "blue";
            }
            return self.statusStyle;
        }
		
}]);

