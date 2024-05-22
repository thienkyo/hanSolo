'use strict';
angular.module('orderCacheListModule')
	.controller('orderCacheListController',['$rootScope','$routeParams','$location',
										 'memberService','orderListService','customerSourceService',
										 'NgTableParams','OrderStatusArray','cartService','AmountList',
										 'orderCacheListService','orderCacheService',
	function($rootScope, $routeParams,$location,
	        memberService,orderListService,customerSourceService,
	        NgTableParams,OrderStatusArray,cartService,AmountList,
	        orderCacheListService,orderCacheService,
	        ){
	var self = this;
	self.isSyncingOrder = false;
	self.orderList = [];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "80px" };
    self.isSuperAdmin = memberService.isSuperAdmin();
	if(!memberService.isMod()){
		$location.path('#/');
	}

	self.cacheCount = orderCacheService.getQuantity();

    //get order from cache
    self.orderList = orderCacheService.getCurrentOrderCache();
    console.log(self.orderList);
    self.orderList.forEach(calculateOrderTotal);
    self.tableParams = new NgTableParams({}, { dataset: self.orderList});


	self.getOrderFromOnline = function() {
        orderCacheListService.get100OrdersForCache().then(function (data) {
            self.orderList = data;
            self.orderList = data.map(enrichData);
            self.orderList.forEach(calculateOrderTotal);
            console.log(self.orderList);
            self.tableParams = new NgTableParams({}, { dataset: self.orderList});
            orderCacheService.setCurrentOrderCache(self.orderList);
            self.cacheCount = orderCacheService.getQuantity();
        });
    }

    function enrichData(item) {
        item.phase = 'LOADED';
        return item;
    }

    self.clearOrderCache = function() {
        orderCacheService.clearCache();
        self.orderList = [];
        self.tableParams = new NgTableParams({}, { dataset: self.orderList});
        self.cacheCount = orderCacheService.getQuantity();
    }

    self.copy = function(order){
        self.isSyncingOrder=true;
        var clone = Object.assign({}, order); //clone object
        clone.id = 0;

        for (var i = 0; i < clone.orderDetails.length; i++){
            clone.orderDetails[i].id = 0;
            clone.orderDetails[i].orderId = null;
        }
        orderCacheListService.syncOrder(clone).then(function (data) {
          self.isSyncingOrder = false;
          console.log(clone);
        });
    }

    self.revert = function(order){
        self.isSyncingOrder=true;
        orderCacheListService.syncOrder(order).then(function (data) {
          self.isSyncingOrder = false;
        });
    }


	self.setStyle = function(status){
		if(status==0){
			self.statusStyle.color = "limegreen";
		}else if(status==1){
			self.statusStyle.color = "blue";
		}else if(status==4){
            self.statusStyle.color = "brown";
        }
		else{
			self.statusStyle = { "width": "80px" }
		}
		return self.statusStyle;
	}

	self.showOrderDetail = function(order){
		if(self.responseStr || self.responseStrFail){
			self.responseStr = false;
		}
		if(order.location == 'STORE'){
		    var url = '#/mgnt/storeOrder/'+order.id;
            window.open(url, '_blank');
		}else{
		    self.theOrder = order;
		}
	}

    self.promptDelete = function(orderId){
        self.deletingOrderId = self.deletingOrderId ? false : orderId;
    }
    self.resetDelete = function(){
        self.deletingOrderId = false;
    }

	function calculateOrderTotal(order){
        var subTotal = 0;
        var temp = 0;
        order.frameNumber = 0;
        order.lensNumber = 0;
        for (var i = 0; i < order.orderDetails.length; i++){
            temp = order.orderDetails[i].framePriceAtThatTime;
            // apply discount coupon for frame
            if(order.orderDetails[i].frameDiscountAmount && order.orderDetails[i].frameDiscountAmount > 0){
                temp = order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAmount)/100
            }
            subTotal += temp*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity + order.orderDetails[i].lensPrice*(100 - order.orderDetails[i].lensDiscountAmount)/100 + order.orderDetails[i].otherPrice;
            if(order.orderDetails[i].framePriceAtThatTime > 1000){
                order.frameNumber +=1;
            }
            if(order.orderDetails[i].lensPrice > 1000){
                if(order.orderDetails[i].monoLens){
                     order.lensNumber +=0.5;
                }else{
                     order.lensNumber +=1;
                }
            }
            if(order.orderDetails[i].reading){
                var odSphere = Number(order.orderDetails[i].odSphere) ? Number(order.orderDetails[i].odSphere) : 0;
                var osSphere = Number(order.orderDetails[i].osSphere) ? Number(order.orderDetails[i].osSphere) : 0;
                order.orderDetails[i].odReading = odSphere + Number(order.orderDetails[i].odAdd);
                order.orderDetails[i].osReading = osSphere + Number(order.orderDetails[i].osAdd);

                if(order.orderDetails[i].odReading > 0){
                    order.orderDetails[i].odReading = '+' + order.orderDetails[i].odReading;
                }
                if(order.orderDetails[i].osReading > 0){
                    order.orderDetails[i].osReading = '+' + order.orderDetails[i].osReading;
                }
            }
        }
        order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
        order.subTotal = subTotal;
        order.currentCusSource = order.cusSource;
        order.couponAmount = subTotal*order.couponDiscount/100;
        order.total = subTotal - order.couponAmount;
        order.remain = 0;
        if(order.status == 4){
            order.remain = subTotal - order.couponAmount - order.deposit;
        }


    }

/// modal

    self.setSummaryModal = function(one) {
        self.theSummaryModal = one;
        self.theSummaryModal.plainText = JSON.stringify(one, undefined, 2);
        console.log(self.theSummaryModal);
    }

}]);
