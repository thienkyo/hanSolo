'use strict';
angular.module('guestOrderModule').controller('guestOrderController', ['$scope','$location','guestOrderService','cartService','OrderStatusArray','paginationService',
	function($scope,$location, guestOrderService,cartService,OrderStatusArray,paginationService) {
		var self = this;
		self.me = {};
		self.me.shipCostId = 0;
/////get ship list
		
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
		
		self.getOrders = function(){
		    self.errorMsg = false;
		    const pattern = /^[0-9]{9,14}$/;
		    if(!pattern.test(self.phone)){
		        self.errorMsg = 'Chỉ nhập ít nhất 9 ký số';
		        return;
		    }

			guestOrderService.getGuestOrder(self.phone).then(function (data) {
			    console.log(data);
				for(var i = 0; i < data.length; i++){
                   self.calculateOrderTotal(data[i]);
                }
                self.orderList = data;
                self.orderListPage = buildPageable(1);
                console.log(self.orderListPage);
                self.pagination = paginationService.builder(self.orderListPage);
                console.log(self.pagination);
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

