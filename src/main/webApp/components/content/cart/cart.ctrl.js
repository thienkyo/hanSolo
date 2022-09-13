'use strict';
angular.module('cartModule')
	.controller('cartController', ['$rootScope','cartService','cartStoreService',
								   'memberService','OrderDO',
								   'OrderDetailDO','MemberDO','$timeout',
	function($rootScope, cartService,cartStoreService,memberService,OrderDO,OrderDetailDO,MemberDO,$timeout) {
		var self = this;
		self.isShow = false;
		self.currentMember = memberService.getCurrentMember();
		self.currentCart = cartStoreService.getCurrentCart();
		self.subTotal = 0;
		self.total = 0;
		self.order = new OrderDO;
		self.order.shipCostId = 0;
		self.guest = new MemberDO;//guest member
		self.guest.memberId = 3;// guest id
		self.orderDetail = [];
		self.order_one_time_trigger = true;

/////get ship list		
		var tempShipCost = {
				distance : "",
				price : 0,
				region : "----chọn vùng----",
				shipCostId : 0
		};
		
		cartService.getActiveShipCost().then(function (response) {
	        self.shipCostList = response;
	        self.shipCostList.push(tempShipCost);
		});
		
//////get full user data from db		
		accountService.getMe().then(function(me){
			self.me = me;
			self.order.member = me;
			self.isShow = false;
			self.updateTotal();
			// set shipCostId based on member address.
			for (var i = 0; i < self.shipCostList.length; i++){
                if(self.shipCostList[i].shipCostId === self.me.shipCostId){
                    self.order.shipCostId = self.shipCostList[i].shipCostId;
                    break;
                }
            }
		},
		function(error){
			self.isShow = false;
			self.me = self.guest;
            self.order.member = self.guest;
		});

		// calculate subtotal
		if(self.currentCart.length > 0){
			for (var i = 0; i < self.currentCart.length; i++){
				self.subTotal += self.currentCart[i].prod.price*self.currentCart[i].quantity;
			}
		//	console.log(self.currentCart);
        //    console.log('before init order');
			console.log(cartStoreService.getOrderId());
			//// init order
			if(cartStoreService.getOrderId() == ''){
			    var OrderDetailList = [];
                for (var i = 0; i < self.currentCart.length; i++){
                    var tempOrderDetail = new OrderDetailDO();
                    tempOrderDetail.product = self.currentCart[i].prod;
                    tempOrderDetail.priceAtThatTime = self.currentCart[i].prod.price;
                    tempOrderDetail.discountAtThatTime = self.currentCart[i].prod.discount;
                    tempOrderDetail.weight = self.currentCart[i].prod.weight;
                    tempOrderDetail.quantity = self.currentCart[i].quantity;
                    tempOrderDetail.imageNames = self.currentCart[i].imageNames;
                    OrderDetailList.push(tempOrderDetail);
                }
                self.order.orderDetails = OrderDetailList;
                self.order.shippingAddress = 'init order';
                self.order.shipCostId = '8';
                self.order.shippingName = 'init order';
                self.order.shippingPhoneNumber = '0000000000';

                cartService.placeGuestOrder(self.order).then(function (response) {
                    cartStoreService.setOrderId(response.orderId);
                });
                console.log(self.order);
			}
		}
		
		self.removeItem = function(index){
			self.currentCart.splice(index, 1);
			cartStoreService.setCurrentCart(self.currentCart);
			self.updateTotal();
			$rootScope.$broadcast('removeItemCart');
		}
		
		self.updateTotal = function(){
			self.subTotal = 0;
			for (var i = 0; i < self.currentCart.length; i++){
				//prodIds.push(self.currentCart[i].id);
				self.subTotal += self.currentCart[i].prod.price*self.currentCart[i].quantity;
			}
			cartStoreService.setCurrentCart(self.currentCart);;
			self.updateShippingFee();
		}
		
		self.placeOrder = function(){
			self.isErrorMsg = false;
			if(self.order_one_time_trigger && self.currentCart.length > 0){
				var OrderDetailList = [];
				
				for (var i = 0; i < self.currentCart.length; i++){
				    // user not upload image for prod that require image.
				    if(self.currentCart[i].prod.needImage && !self.currentCart[i].imageNames){
				        self.isErrorMsg = 'Cần upload hình cho đơn hàng';
				        return;
				    }
					var tempOrderDetail = new OrderDetailDO();
					tempOrderDetail.product = self.currentCart[i].prod;
					tempOrderDetail.priceAtThatTime = self.currentCart[i].prod.price;
					tempOrderDetail.discountAtThatTime = self.currentCart[i].prod.discount;
					tempOrderDetail.weight = self.currentCart[i].prod.weight;
					tempOrderDetail.quantity = self.currentCart[i].quantity;
					tempOrderDetail.imageNames = self.currentCart[i].imageNames;
					OrderDetailList.push(tempOrderDetail);
				}
				self.order.orderDetails = OrderDetailList;
				self.order.shippingAddress = self.me.address;
			//	self.order.shipCostId = self.me.shipCostId;
				self.order.shippingName = self.me.fullName;
				self.order.shippingPhoneNumber = self.me.phone;
				self.order.orderId = cartStoreService.getOrderId();
				self.order.status = 20;
				self.isShow = false;
				self.isErrorMsg = false;
				//console.log(self.order);
				//save order
				if(self.me.address &&  self.order.shipCostId != 0 && self.me.fullName && self.me.phone){
				    if(memberService.isLogin()){
				        cartService.placeOrder(self.order).then(function (response) {
                            self.order_return_status = response; // return after saving order, order_return_status would be orderid
                            self.newOrderId = response.replyStr;
                        });
				    }else{
				        cartService.placeGuestOrder(self.order).then(function (response) {
                            self.order_return_status = response.replyStr;
                            self.newOrderId = response.orderId;
                        });
				    }
				    cartStoreService.clearOrderId();
					self.order_one_time_trigger = false;
                    cartStoreService.clearCart();
                    self.currentCart = [];
                    $rootScope.$broadcast('clearCart');

				}else{
					self.isErrorMsg ='Cần nhập địa chỉ/tên/số điện thoại/chọn vùng.';
				}
			}else{
				console.log('NOT logined');
			}
		}
		
		self.updateShippingFee = function(){
		    self.isErrorMsg = false;
			var shipBaseFee = 0;
			for (var i = 0; i < self.shipCostList.length; i++){
				if(self.shipCostList[i].shipCostId === self.me.shipCostId){
					shipBaseFee = self.shipCostList[i].price;
					break;
				}
			}
			var w=0;
			for (var i = 0; i < self.currentCart.length; i++){
				w += self.currentCart[i].prod.weight*self.currentCart[i].quantity;
			}
			self.order.shipCostFee = w*shipBaseFee;
		//	self.order.shipCostFee = (self.order.shipCostFee < 20000 && self.me.shipCostId != 7) ?  25000 : self.order.shipCostFee ;
			self.total = self.order.shipCostFee + self.subTotal;
		}

		self.uploadPic = function(files,oldNames,cartDetail) {
		        self.isErrorMsg = false;
		        const pattern = /^[0-9]{9,14}$/;
		        if(!pattern.test(self.me.phone)){
		            self.isErrorMsg = 'Cần nhập số điện thoại, ít nhất 9 ký số';
		            return;
		        }

		        // check if user upload more file than allows
		        if(files.length > cartDetail.prod.maxNumberOfImage){
		            self.isErrorMsg = 'Bạn chọn quá nhiều hình cho phép, tối đa là '+cartDetail.prod.maxNumberOfImage+' hình';
		            return;
		        }
		        // 1st upload, no oldNames yet
		        if(!oldNames){
                    oldNames='';
                }
                Upload.upload({
                  url: 'guest/uploadFile',
                  data: { files: files, oldNames: oldNames, phone: self.me.phone, orderId: cartStoreService.getOrderId()},
                  arrayKey: '',
                  headers:{'Content-Type':'multipart/form-data'}
                }).then(function (response) {
                  $timeout(function () {
                    cartDetail.imageNames = response.data;
                    cartDetail.quantity = files.length;
                    //cartStoreService.setCurrentCart(self.currentCart);;
                    self.updateTotal();
                  });
                }, function (response) {
                  if (response.status > 0)
                    self.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                  // Math.min is to fix IE which reports 200% sometimes
                  files.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
        }

        self.clearErrorMsg = function() {
            self.isErrorMsg = false;
        }
		
}]);