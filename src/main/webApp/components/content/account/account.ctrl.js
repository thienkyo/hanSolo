'use strict';
angular.module('accountModule').controller('accountController', ['$scope','$location','accountService','cartService','OrderStatusArray','paginationService',
	function($scope,$location, accountService,cartService,OrderStatusArray,paginationService) {
		var self = this;
		self.me = {};

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
			var size = 4;
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
			accountService.updateMe(self.me).then(function (data) {
				self.responseStr = data.errorMessage;
			});
		}
		
		self.showOrderDetail = function(order){
			self.theOrder = order;
		}

		self.calculateOrderTotal = function(order){
            var subTotal = 0;
            for (var i = 0; i < order.orderDetails.length; i++){
                subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity;
            }
            order.status = OrderStatusArray.find(i => i.value == order.status).name;
            order.subTotal = subTotal;
            order.couponAmount = subTotal*order.couponDiscount/100;
            order.total = subTotal - order.couponAmount;
        }
}]);

