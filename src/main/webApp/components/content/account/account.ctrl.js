'use strict';
angular.module('accountModule').controller('accountController', ['$scope','$location','accountService','cartService','OrderStatusArray','paginationService',
	function($scope,$location, accountService,cartService,OrderStatusArray,paginationService) {
		var self = this;
		self.me = {};
		self.me.shipCostId = 0;
/////get ship list		
		var tempShipCost = {
				distance: "",
				price:"",
				region:"------chọn vùng-------",
				shipCostId : 0
		};
		
		cartService.getShipCost().then(function (response) {
	        self.shipCostList = response;
	        self.shipCostList.push(tempShipCost);
		});
		
		accountService.getMe().then(function(me){
			self.me = me;
			for(var i = 0; i < me.orders.length; i++){
				var total = 0;
				for(var k = 0; k < me.orders[i].orderDetails.length; k++){
					total += me.orders[i].orderDetails[k].priceAtThatTime*me.orders[i].orderDetails[k].quantity;
				}
				total += me.orders[i].shipCostFee;
				me.orders[i].total = total;
				for(var k = 0; k < OrderStatusArray.length; k++){
					if(OrderStatusArray[k].value == me.orders[i].status){
						me.orders[i].status = OrderStatusArray[k].name;
						break;
					}
				}
			}
			self.orderList = me.orders.reverse();
			self.orderListPage = buildPageable(1);
			self.pagination = paginationService.builder(self.orderListPage);
			console.log(me.orders);
			console.log(me.orders.reverse());
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
		
		self.getTargetPage = function(targetpage){
			self.orderListPage = buildPageable(targetpage);
			self.pagination = paginationService.builder(self.orderListPage);
		} 
		
		self.updateMe = function(){
			for(var i = 0; i < self.shipCostList.length; i++){
				if(self.shipCostList[i].shipCostId == self.me.shipCostId){
					self.me.shipCost = self.shipCostList[i];
					break;
				}
			}
			accountService.updateMe(self.me).then(function (response) {
				self.responseStr = response;
			});
		}
		
		self.showOrderDetail = function(order){
			self.theOrder = order;
			self.theOrder.subTotal = 0;
			for (var i = 0; i < self.theOrder.orderDetails.length; i++){
				self.theOrder.subTotal += self.theOrder.orderDetails[i].priceAtThatTime*self.theOrder.orderDetails[i].quantity;
			}
			self.theOrder.total = self.theOrder.subTotal + self.theOrder.shipCostFee; 
		}
}]);

