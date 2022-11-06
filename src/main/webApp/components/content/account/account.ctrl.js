'use strict';
angular.module('accountModule').controller('accountController', ['$scope','$location','accountService','cartService','OrderStatusArray','paginationService',
	function($scope,$location, accountService,cartService,OrderStatusArray,paginationService) {
		var self = this;
		self.me = {};
		self.oldPass = '';
		self.newPass = '';
		function MiniOrderDetailDO (framePriceAtThatTime,quantity,name,thumbnail,frameDiscountAtThatTime,frameId,type) {
            this.framePriceAtThatTime = framePriceAtThatTime;
            this.quantity = quantity;
            this.name = name;
            this.thumbnail = thumbnail;
            this.frameDiscountAtThatTime = frameDiscountAtThatTime;
            this.frameId = frameId;
            this.type = type;
        }

		accountService.getMe().then(function(data){
			self.me = data.member;
			for(var i = 0; i < data.member.orders.length; i++){
				// var total = 0;
				self.calculateOrderTotal(data.member.orders[i]);
			}
			self.orderList = data.member.orders.reverse();
			self.orderListPage = buildPageable(1);
			self.pagination = paginationService.builder(self.orderListPage);

		},function(error){
			$location.path("#/");
		});
		
		function buildPageable(targetPage){
			var pageable ={};
			var size = 5;
			pageable.totalElements = self.orderList.length;
			pageable.totalPages = Math.ceil(pageable.totalElements/size);
			pageable.number = targetPage - 1;
			pageable.content = [];
			var start = size*(targetPage -1);
			var end   = size*targetPage;
			for(var i = start; i< end; i++){
				if(self.orderList[i]){
					pageable.content.push(self.orderList[i]);
				}
			}
			return pageable;
		}
		
		self.getTargetPage = function(targetPage){
			self.orderListPage = buildPageable(targetPage);
			self.pagination = paginationService.builder(self.orderListPage);
		}
		
		self.updateMe = function(){
		    self.me.oldPass = btoa(self.oldPass);
		    self.me.newPass = btoa(self.newPass);
			accountService.updateMe(self.me).then(function (data) {
				self.responseCode = data.errorCode;
				self.responseStr = data.errorMessage;
			});
		}
		
		self.showOrderDetail = function(order){
			self.theOrder = order;
		}

		self.calculateOrderTotal = function(order){
            var subTotal = 0;
            order.newOrderDetails=[];
            for (var i = 0; i < order.orderDetails.length; i++){
                order.orderDetails[i].framePriceAfterSale = order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100 * order.orderDetails[i].quantity;
                subTotal += order.orderDetails[i].framePriceAfterSale + order.orderDetails[i].lensPrice;

                var name = '';
                var thumbnail = '';
                var frameId = '';
                if(order.orderDetails[i].framePriceAfterSale > 0){
                    if(order.orderDetails[i].product){
                        thumbnail = order.orderDetails[i].product.thumbnail;
                        name = order.orderDetails[i].product.name
                        frameId = order.orderDetails[i].product.id
                    }else{
                        name = order.orderDetails[i].frameNote
                    }
                    order.newOrderDetails.push(new MiniOrderDetailDO (order.orderDetails[i].framePriceAtThatTime,order.orderDetails[i].quantity,name,thumbnail,order.orderDetails[i].frameDiscountAtThatTime,frameId,'FRAME'));
                }

                if(order.orderDetails[i].lensPrice > 0 ){
                    order.newOrderDetails.push(new MiniOrderDetailDO (order.orderDetails[i].lensPrice,order.orderDetails[i].quantity,order.orderDetails[i].lensNote,'thumbnail',0,frameId,'LENS'));
                }
            }
            order.statusText = OrderStatusArray.find(i => i.value == order.status).name;
            order.subTotal = subTotal;
            order.couponAmount = subTotal*order.couponDiscount/100;
            order.total = subTotal - order.couponAmount ;
            order.remain = subTotal - order.couponAmount - order.deposit;
        }
}]);

