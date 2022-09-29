'use strict';
angular.module('cartModule')
	.controller('cartController', ['$rootScope','cartService','cartStoreService',
								   'memberService','OrderDO','accountService',
								   'OrderDetailDO','MemberDO','$timeout',
	function($rootScope, cartService,cartStoreService,memberService,OrderDO,accountService,OrderDetailDO,MemberDO,$timeout) {
		var self = this;
		self.isShow = false;
		self.currentMember = memberService.getCurrentMember();
		self.currentCart = cartStoreService.getCurrentCart();
		self.subTotal = 0;
		self.total = 0;
		self.couponDiscount = 0; //%
		self.couponCode = '';
		self.order = new OrderDO;
		self.guest = new MemberDO;//guest member
		//self.guest.fullName = 'GUEST';// guest id
		self.orderDetail = [];
		self.order_one_time_trigger = true;
		
//////get full user data from db		
		accountService.getMe().then(function(data){
			self.me = data.member;
			self.order.member = self.me;

		},
		function(error){
			self.me = self.guest;
            self.order.member = self.guest;
		});


		// calculate subtotal
		self.updateTotal = function(){
            self.subTotal = 0;
            for (var i = 0; i < self.currentCart.length; i++){
                self.subTotal += self.currentCart[i].prod.sellPrice*(100 - self.currentCart[i].prod.discount)/100*self.currentCart[i].quantity;
            }
            self.total = self.subTotal*(100 - self.couponDiscount)/100;
            cartStoreService.setCurrentCart(self.currentCart);;
        }

		// load cart at load page
		if(self.currentCart.length > 0){
			self.updateTotal();
		}
		
		self.removeItem = function(index){
			self.currentCart.splice(index, 1);
			cartStoreService.setCurrentCart(self.currentCart);
			self.updateTotal();
			$rootScope.$broadcast('removeItemCart');
		}
		
		self.placeOrder = function(){
			self.isErrorMsg = false;
			if(self.order_one_time_trigger && self.currentCart.length > 0){
				var OrderDetailList = [];
				
				for (var i = 0; i < self.currentCart.length; i++){
					var tempOrderDetail = new OrderDetailDO();
					tempOrderDetail.product = self.currentCart[i].prod;
					tempOrderDetail.framePriceAtThatTime = self.currentCart[i].prod.sellPrice;
					tempOrderDetail.frameDiscountAtThatTime = self.currentCart[i].prod.discount;
					tempOrderDetail.weight = self.currentCart[i].prod.weight;
					tempOrderDetail.quantity = self.currentCart[i].quantity;
					OrderDetailList.push(tempOrderDetail);
				}
				self.order.orderDetails = OrderDetailList;
				self.order.shippingAddress = self.me.address;
				self.order.shippingName = self.me.fullName;
				self.order.shippingPhone = self.me.phone;

				self.order.couponCode = self.couponCode;
				self.order.couponDiscount = self.couponDiscount;

				//save order
				if(self.me.address && self.me.fullName && self.me.phone){
				    if(memberService.isLogin()){
				        cartService.placeOrder(self.order).then(function (data) {
                            self.order_return_status = data; // return after saving order, order_return_status would be orderid
                            self.newOrderId = data.replyStr;
                        });
				    }else{
				        cartService.placeGuestOrder(self.order).then(function (data) {
                            self.order_return_status = data;
                            self.newOrderId = data.replyStr;
                        });
				    }

					self.order_one_time_trigger = false;
                    cartStoreService.clearCart();
                    self.currentCart = [];
                    $rootScope.$broadcast('clearCart');

				}else{
					self.isErrorMsg ='Cần nhập địa chỉ/tên/số điện thoại.';
				}
			}else{
			    self.isErrorMsg ='Please add an item to cart.';
			}
		}

        self.clearErrorMsg = function() {
            self.isErrorMsg = false;
        }

        self.getCoupon = function(code) {
            if(code ==''){
                self.isErrorMsg ='Cần nhập coupon code.';
                return;
            }
             cartService.getCoupon(code).then(function (data) {
             if(data.errorCode == 'SUCCESS'){
                self.couponDiscount = data.replyStr;
                self.isCouponApplied = true;
                self.updateTotal();
                self.isErrorMsg = false;
             }else{
                self.isErrorMsg = data.errorMessage;
             }
             });
        }
		
}]);