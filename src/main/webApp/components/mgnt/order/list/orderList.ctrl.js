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
    self.OneDayReport={};
    self.isSuperAdmin = memberService.isSuperAdmin();

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
		//console.log(self.orderList);
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
       var copyTextarea = angular.element(document.getElementById("js-copytextarea"));
         copyTextarea.focus();
         copyTextarea.select();

         try {
           var successful = document.execCommand('copy');
           var msg = successful ? 'successful' : 'unsuccessful';
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

       // for one day report modal
       self.tempForOneDayReport = one;
    }

    self.getOneDayReport = function() {
        self.OneDayReport.totalAmount = 0;
        self.OneDayReport.subTotalAmount = 0;
        self.OneDayReport.couponAmount = 0;
        self.OneDayReport.depositAmount = 0;
        self.OneDayReport.frameNumber = 0;
        self.OneDayReport.lensNumber = 0;
        self.OneDayReport.actualAmount = 0;
        self.OneDayReport.remainAmount = 0;

        self.OneDayReport.data = self.orderList.filter(i => sameDay(new Date(i.gmtCreate), new Date(self.tempForOneDayReport.gmtCreate)));
        var date = (new Date(self.tempForOneDayReport.gmtCreate)).getDate();
        date =  date > 9 ? date : '0' + date;
        var month = (new Date(self.tempForOneDayReport.gmtCreate)).getMonth() + 1;
        month =  month > 9 ? month : '0' + month;

        self.OneDayReport.date =  date +'-'+ month +'-'+ (new Date(self.tempForOneDayReport.gmtCreate)).getFullYear() ;

        self.OneDayReport.data.forEach((dataOne, index, array) => {
            self.OneDayReport.totalAmount += dataOne.total;
            self.OneDayReport.subTotalAmount += dataOne.subTotal;
            self.OneDayReport.depositAmount += dataOne.deposit;
            self.OneDayReport.frameNumber += dataOne.frameNumber;
            self.OneDayReport.lensNumber += dataOne.lensNumber;
            if(dataOne.deposit > 1000 && dataOne.status == 4){
                self.OneDayReport.actualAmount += dataOne.deposit;
                self.OneDayReport.remainAmount += dataOne.remain;
            }else{
                self.OneDayReport.actualAmount += dataOne.total;
            }
        });

        console.log(self.OneDayReport);
    }

    function sameDay(d1, d2) {
      return d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate();
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
            var mono =  dataOne.monoLens ? ', 1cái' : '';
            var reading = dataOne.reading ? ', đọc sách' : '';
            var odSphere = dataOne.reading ? dataOne.odReading : dataOne.odSphere;
            var osSphere = dataOne.reading ? dataOne.osReading : dataOne.osSphere;
            self.copyText = self.copyText + '[' + dataOne.orderId +'-'+ dataOne.id +':'+
                                            '('+odSphere +' '+dataOne.odCylinder + ')' +
                                            '('+osSphere +' '+dataOne.osCylinder + ')/' +
                                            dataOne.lensNote + mono +' '+ reading +
                                            ']\n'
            ;
        });
        //console.log(self.detailArray);
    }


//////////// modal section start here. /////////////////
     self.setModal = function(one) {
         self.detailArray = [];
         self.detailArray = self.detailArray.concat(one.orderDetails);
         buildText();
     }

    self.setSummaryModal = function(one) {
        self.theSummaryModal = one;
        console.log(self.theSummaryModal);
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
