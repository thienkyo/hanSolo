'use strict';
angular.module('accountModule').controller('accountController', ['$scope','$location','accountService','cartService','OrderStatusArray','paginationService',
	function($scope,$location, accountService,cartService,OrderStatusArray,paginationService) {
		var self = this;
		self.me = {};
/*		self.me.shipCostId = 0;
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
		*/
		accountService.getMe().then(function(data){
			self.me = data.member;
			console.log(self.me);
			for(var i = 0; i < data.member.orders.length; i++){
				var total = 0;
				for(var k = 0; k < data.member.orders[i].orderDetails.length; k++){
					total += data.member.orders[i].orderDetails[k].priceAtThatTime*data.member.orders[i].orderDetails[k].quantity;
				}
				//total += data.member.orders[i].shipCostFee;
				data.member.orders[i].total = total;
				for(var k = 0; k < OrderStatusArray.length; k++){
					if(OrderStatusArray[k].value == data.member.orders[i].status){
						data.member.orders[i].status = OrderStatusArray[k].name;
						break;
					}
				}
			}
			self.orderList = data.member.orders.reverse();
			self.orderListPage = buildPageable(1);
			self.pagination = paginationService.builder(self.orderListPage);
			console.log(data.member.orders);
			console.log(data.member.orders.reverse());
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

