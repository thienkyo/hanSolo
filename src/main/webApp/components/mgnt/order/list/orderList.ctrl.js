'use strict';
angular.module('orderListModule')
	.controller('orderListController',['$rootScope','$routeParams','$location','FirstTimeLoadSize',
										 'memberService','orderListService','customerSourceService',
										 'NgTableParams','OrderStatusArray','cartService','AmountList','$modal','$log',
	function($rootScope, $routeParams,$location,FirstTimeLoadSize,
	        memberService,orderListService,customerSourceService,
	        NgTableParams,OrderStatusArray,cartService,AmountList, $modal, $log) {
	var self = this;
	self.orderList = [];
	self.cusSourceList = [];
	self.OrderStatusArray=OrderStatusArray;
	self.statusStyle = { "width": "80px" };
	self.statusNumber = {"ordered":0, "paid":0,"shipped":0, "done":0, "deposit":0, "userDelete":0};
	self.isUpdatingOrder = false; // disable/able the select for update order status
	self.showLoadingText = true; // disable/able Loading..
	self.tempArray=[];
	self.detailArray=[];
    self.tempAmount=0;
    self.tempFrameNumber=0;
    self.tempLensNumber=0;

	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = FirstTimeLoadSize;

	customerSourceService.getAll().then(function (data) {
        self.cusSourceList = data;
       // console.log(self.cusSourceList);
      //  self.customerParams = new NgTableParams({}, { dataset: self.customerSourceList});
    });

	orderListService.getOrdersForMgnt(self.amount).then(function (data) {
		self.orderList = data;
		self.orderList.forEach(calculateOrderTotal);
		self.tableParams = new NgTableParams({}, { dataset: self.orderList});
		self.showLoadingText = false;
	});
	
	self.updateOrderStatus = function(order){
	    order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
	    self.isUpdatingOrder = true;
		orderListService.updateOrderStatus(order).then(function(data){
			self.responseStr = data.replyStr;
			self.isUpdatingOrder = false;
		});
	}

	self.updateCusSource = function(order){
        self.isUpdatingOrder = true;
        orderListService.updateCusSource(order).then(function(data){
            self.responseStr = data.replyStr;
            self.isUpdatingOrder = false;
        });
    }

    self.clearAmount = function() {
        self.tempAmount = 0;
        self.tempArray.forEach((dataOne, index, array) => {
           dataOne.picked = false;
       });
        self.tempArray=[];
        self.detailArray = [];
        self.copyText = '';
    }

    self.copyClipBoard = function() {


       console.log(self.copyText);


       var copyTextarea = angular.element(document.getElementById("js-copytextarea"));
         copyTextarea.focus();
         copyTextarea.select();

         try {
           var successful = document.execCommand('copy');
           var msg = successful ? 'successful' : 'unsuccessful';
           console.log('Copying text command was ' + msg);
         } catch (err) {
           console.log('Oops, unable to copy');
         }

    }

    self.selectAllAmount = function() {
        self.tempAmount = 0;
        self.tempFrameNumber=0;
        self.tempLensNumber=0;
        self.detailArray = [];
        self.tempArray = self.tableParams.data;
        self.tempArray.forEach((dataOne, index, array) => {
           dataOne.picked = true;
           self.tempAmount += dataOne.total;
           self.tempFrameNumber += dataOne.frameNumber;
           self.tempLensNumber += dataOne.lensNumber;
           self.detailArray = self.detailArray.concat(dataOne.orderDetails);
       });
    }

    self.calculateAmount = function(one) {
        self.tempAmount = 0;
        self.tempFrameNumber=0;
        self.tempLensNumber=0;
        if(one.picked){
            self.tempArray.push(one);
            self.detailArray = self.detailArray.concat(one.orderDetails);
        }else{
            var index = self.tempArray.indexOf(one);
            self.tempArray.splice(index,1);
            self.detailArray = self.detailArray.filter(i => i.orderId != one.id);
        }
        self.tempArray.forEach((dataOne, index, array) => {
           self.tempAmount += dataOne.total;
           self.tempFrameNumber += dataOne.frameNumber;
           self.tempLensNumber += dataOne.lensNumber;
       });
       buildText();
    }

	self.deleteOrder = function(order){
        self.responseStr = false;
        self.responseStrFail = false;
        orderListService.deleteOrder(order).then(function (data) {
            self.responseStr = data;
            var index = self.orderList.indexOf(order);
            self.orderList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.orderList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }
	
	self.getOrderByTerm = function(){
		orderListService.getOrdersForMgnt(self.amount).then(function (data) {
			self.orderList = data;
			self.orderList.forEach(calculateOrderTotal);
			self.tableParams = new NgTableParams({}, { dataset: self.orderList});
			engineerOrderList();
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
	
	function engineerOrderList(){
		self.statusNumber.ordered = 0;
        self.statusNumber.paid = 0;
        self.statusNumber.shipped = 0;
        self.statusNumber.done = 0;
        self.statusNumber.deposit = 0;
        self.statusNumber.userDelete = 0;
		
		for(var i = 0; i < self.orderList.length; i++){

	        switch(self.orderList[i].status) {
              case 0:
                  self.statusNumber.ordered += 1;
                  break;
              case 1:
                  self.statusNumber.paid += 1;
                  break;
              case 2:
                  self.statusNumber.shipped += 1;
                  break;
              case 3:
                  self.statusNumber.done += 1;
                  break;
              case 4:
                  self.statusNumber.deposit += 1;
                  break;
              case 5:
                  self.statusNumber.userDelete += 1;
                  break;
              default:
            }
		}
	}

	function calculateOrderTotal(order){
        var subTotal = 0;
        order.frameNumber = 0;
        order.lensNumber = 0;
        for (var i = 0; i < order.orderDetails.length; i++){
            subTotal += order.orderDetails[i].framePriceAtThatTime*(100 - order.orderDetails[i].frameDiscountAtThatTime)/100*order.orderDetails[i].quantity + order.orderDetails[i].lensPrice;
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
                order.orderDetails[i].odReading = Number(order.orderDetails[i].odSphere) + Number(order.orderDetails[i].odAdd);
                order.orderDetails[i].osReading = Number(order.orderDetails[i].osSphere) + Number(order.orderDetails[i].osAdd);
            }
        }
        order.statusName = OrderStatusArray.find(i => i.value == order.status).name;
        order.subTotal = subTotal;
        order.currentCusSource = order.cusSource;
        order.couponAmount = subTotal*order.couponDiscount/100;
        order.total = subTotal - order.couponAmount;

        switch(order.status) {
          case 0:
              self.statusNumber.ordered += 1;
              break;
          case 1:
              self.statusNumber.paid += 1;
              break;
          case 2:
              self.statusNumber.shipped += 1;
              break;
          case 3:
              self.statusNumber.done += 1;
              break;
          case 4:
              self.statusNumber.deposit += 1;
              break;
          case 5:
              self.statusNumber.userDelete += 1;
              break;
          default:
        }

    }

    function buildText(){
        self.copyText='';
         self.detailArray.forEach((dataOne, index, array) => {
            var mono =  dataOne.monoLens ? '1cái' : '';
            var reading = dataOne.reading ? 'đọc sách' : '';
            self.copyText = self.copyText + '[' + dataOne.orderId +'-'+ dataOne.id +':'+
                                             '('+dataOne.odSphere +' '+dataOne.odCylinder + ')' +
                                             '('+dataOne.osSphere +' '+dataOne.osCylinder + ')/' +
                                             dataOne.lensNote +' '+ mono +' '+ reading +
                                             ']\n'

            ;
        });

    }


//////////// modal section start here. /////////////////
     self.setModal = function(one) {
         self.detailArray = [];
         self.detailArray = self.detailArray.concat(one.orderDetails);
         buildText();
     }

    self.setSummaryModal = function(one) {
        self.theSummaryModal = one;
        console.log(one);
    }

    $('#exampleModal').on('hidden.bs.modal', function (e) {
      self.tempAmount = 0;
      self.tempArray.forEach((dataOne, index, array) => {
         dataOne.picked = false;
      });
      self.tempArray=[];
      self.detailArray = [];
      self.copyText = '';
    })


}]);
