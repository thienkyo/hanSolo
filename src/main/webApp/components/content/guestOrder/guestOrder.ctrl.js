'use strict';
angular.module('guestOrderModule').controller('guestOrderController', ['$scope','$location','guestOrderService','cartService','OrderStatusArray','paginationService',
	function($scope,$location, guestOrderService,cartService,OrderStatusArray,paginationService) {
		var self = this;
		self.me = {};
		self.me.shipCostId = 0;
		function MiniOrderDetailDO (framePriceAtThatTime,quantity,name,thumbnail,frameDiscountAtThatTime,frameId,type) {
            this.framePriceAtThatTime = framePriceAtThatTime;
            this.quantity = quantity;
            this.name = name;
            this.thumbnail = thumbnail;
            this.frameDiscountAtThatTime = frameDiscountAtThatTime;
            this.frameId = frameId;
            this.type = type;
        }
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
				for(var i = 0; i < data.length; i++){
                   self.calculateOrderTotal(data[i]);
                }
                self.orderList = data;
                self.orderListPage = buildPageable(1);
                self.pagination = paginationService.builder(self.orderListPage);
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


                //(framePriceAtThatTime,quantity,name,thumbnail,frameDiscountAtThatTime,type)
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

