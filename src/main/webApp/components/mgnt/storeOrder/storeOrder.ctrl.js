'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$routeParams','$location','memberService','orderListService','SmsUserInfoDO',
										 'OrderStatusArray','cartService','OrderDO','OrderDetailDO','SmsJobDO',
										 'ajaxService','genderArray','smsJobService',
	function($routeParams,$location,memberService,orderListService,SmsUserInfoDO,
	            OrderStatusArray,cartService,OrderDO,OrderDetailDO,SmsJobDO,
	            ajaxService,genderArray,smsJobService) {
	var self = this;
	//self.orderDetailList = new Array(3).fill(new OrderDetailDO(false));
	//self.orderDetailList.unshift(new OrderDetailDO(true));

	self.DPisOpen = false;
	self.couponDiscount = 0; //%
    self.couponCode = '';
    self.theSpecificSmsUserInfo = new SmsUserInfoDO();

	self.OrderStatusArray=OrderStatusArray;
	self.genderArray=genderArray;
	self.statusStyle = { "width": "120px" };
    var firstSmsJob = new SmsJobDO();
	self.smsJobList = [firstSmsJob];

//////////////// function section ////////////

    self.updatePrice = function(){
        self.calculateOrderTotal();
        if(self.theOrder.deposit > 1000){
            self.theOrder.status = 4; // 4 = deposit
        }
    }

    self.copy1Tab = function(tab){
        var newTab = Object.assign({}, tab);
        newTab.id = null;
        newTab.product = null;
        newTab.framePriceAfterSale = 0;
        newTab.framePriceAtThatTime = 0;
        newTab.frameDiscountAtThatTime = 0;
        newTab.frameNote = "";
        newTab.lensNote = "";
        newTab.lensPrice = 0;
        self.theOrder.orderDetails.push(newTab);
    }

    self.add1Tab = function(){
        self.theOrder.orderDetails.push(new OrderDetailDO());
    }

    self.removeLastTab = function(){
         if(self.theOrder.orderDetails.length > 1){
            self.theOrder.orderDetails.pop();
        }
    }

    self.remove1Tab = function(index){
             if(self.theOrder.orderDetails.length > 1){
                self.theOrder.orderDetails.splice(index,1);
            }
        }

    self.nameCopy = function(){
        self.theOrder.orderDetails[0].name = self.theOrder.shippingName;
    }

    self.phoneCopy = function(){
        self.theOrder.orderDetails[0].phone = self.theOrder.shippingPhone;
    }

    self.addressCopy = function(){
        self.theOrder.orderDetails[0].address = self.theOrder.shippingAddress;
    }

    self.genderCopy = function(){
        self.theOrder.orderDetails[0].gender = self.theOrder.gender;
    }

    // open datePicker
    self.openDP = function() {
        self.DPisOpen = true;
        self.isPickDP = true;
    };

    self.querySearch = function(searchText){
        if(searchText){
            var url = "search/product/"+searchText;
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });

        }else{
            return {id:0,name:'no result',type:1,image:''};
        }
    }
    self.querySearchLens = function(searchText){
            if(searchText){
                var url = "search/productMngt/"+searchText;
                return ajaxService.get(url,null,{}).then(function(response){
                    return response.data;
                });

            }else{
                return {id:0,name:'no result',type:1,image:''};
            }
        }

    self.searchTextChange =function(text) {
        //console.log('Text changed to ' + text);
    }

    self.searchLensTextChange =function(text) {
        //console.log('Text changed to ' + text);
    }

    self.selectedItemChange = function(one,orderDetail) {
        if(one){
       //   console.log('selectedItemChange in if');
          orderDetail.product = one;
          orderDetail.frameNote = one.name;
         // orderDetail.framePriceAtThatTime = one.name;
          orderDetail.framePriceAfterSale = orderDetail.product.sellPrice*(100 - orderDetail.product.discount)/100 * orderDetail.quantity;
        }
        self.calculateOrderTotal();
    }

    self.selectedLensChange = function(one,orderDetail) {
            if(one){
              orderDetail.lensNote = one.name;
              orderDetail.lensPrice = one.sellPrice;
            }
            self.calculateOrderTotal();
        }

    self.removeSearchResult = function(orderDetail){
        orderDetail.product = null;
        self.calculateOrderTotal();
    }

    self.calculateOrderTotal = function(){
        var subTotal = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            if(self.theOrder.orderDetails[i].product){
                self.theOrder.orderDetails[i].framePriceAtThatTime = self.theOrder.orderDetails[i].product.sellPrice;
                self.theOrder.orderDetails[i].frameDiscountAtThatTime = self.theOrder.orderDetails[i].product.discount;
            }else{
                self.theOrder.orderDetails[i].framePriceAtThatTime = self.theOrder.orderDetails[i].framePriceAfterSale;
                self.theOrder.orderDetails[i].frameDiscountAtThatTime = 0;
            }
            subTotal += self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100*self.theOrder.orderDetails[i].quantity + self.theOrder.orderDetails[i].lensPrice;
        }
    //  self.theOrder.statusName = OrderStatusArray.find(i => i.value == self.theOrder.status).name;
        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount;
        self.theOrder.remain = subTotal - self.theOrder.couponAmount - self.theOrder.deposit;
    }

    self.getCoupon = function(code) {
        if(code ==''){
            self.isErrorMsg ='Cần nhập coupon code.';
            return;
        }
         cartService.getCoupon(code).then(function (data) {

         if(data.errorCode == 'SUCCESS'){
            self.theOrder.couponDiscount = data.replyStr;
            self.theOrder.couponCode = code;
          //  self.isCouponApplied = true;
            self.calculateOrderTotal(self.theOrder);
            self.isErrorMsg = false;
         }else{
            self.isErrorMsg = data.errorMessage;
         }
         });
    }

    self.saveOrder = function(){
        self.order_return_status = null;
        if(self.isPickDP){
            self.theOrder.gmtModify = self.theOrder.gmtCreate;
            for (var i = 0; i < self.theOrder.orderDetails.length; i++){
                self.theOrder.orderDetails[i].gmtCreate = self.theOrder.gmtCreate;
                self.theOrder.orderDetails[i].gmtModify = self.theOrder.gmtCreate;
            }
        }

        self.theOrder.specificJobId = self.selectedJob.id;
        self.theOrder.specificJobName = self.selectedJob.jobName;

        if(self.theOrder.shippingName && self.theOrder.shippingPhone ){
            if(memberService.isAdmin()){
                self.isSaveButtonPressed=true;
                cartService.placeOrder(self.theOrder).then(function (data) {
                    self.order_return_status = data; // return after saving order, order_return_status would be orderid
                    self.newOrderId = data.replyStr;
                    self.isSaveButtonPressed=false;
                    self.isErrorMsg=false;
                    $location.path('/mgnt/storeOrder/'+self.newOrderId);
                });
            }
        }else{
            self.isErrorMsg ='Cần nhập tên/số điện thoại(tối thiểu 3 số).';
        }

    }

    self.calculateFramePriceAfterSale = function(orderDetail){
        orderDetail.framePriceAfterSale = orderDetail.framePriceAtThatTime*(100 - orderDetail.frameDiscountAtThatTime)/100 * orderDetail.quantity;
    }

    self.closeAlert = function(index) {
        self.order_return_status = false;
    };

    self.tempF = function() {
        console.log(self.selectedJob);
    };

////// run when loading page/////
	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	 // load product
    if($routeParams.orderId > 0){
        orderListService.getOrderById($routeParams.orderId)
            .then(function (data) {
                self.theOrder = data;
                if(self.theOrder.orderDetails.length > 0){
                    self.theOrder.orderDetails.forEach(self.calculateFramePriceAfterSale);
                    self.calculateOrderTotal(self.theOrder);
                }
                console.log(self.theOrder);

                if(self.theOrder.specificJobId && self.theOrder.specificJobId > 0){
                    var smsJobOption = self.smsJobList.find(i => i.id == self.theOrder.specificJobId);
                    if(smsJobOption){
                        self.selectedJob = smsJobOption;
                    }else{
                        var tempSmsJob = new SmsJobDO();
                        tempSmsJob.id = self.theOrder.specificJobId;
                        tempSmsJob.jobName = self.theOrder.specificJobName;
                        self.smsJobList.unshift(tempSmsJob);
                        self.selectedJob = tempSmsJob;
                    }
                }else{
                    self.selectedJob = firstSmsJob;
                    console.log('test');
                    console.log(self.selectedJob);
                }
        });
    }else{
        self.theOrder = new OrderDO;
        self.theOrder.location='STORE';
        self.theOrder.orderDetails = [new OrderDetailDO()];
        self.theOrder.gender=true;
        self.theOrder.orderDetails[0].gender=true;
    }

    self.isSaveButtonPressed=false;// the "save order" button is pressed or not.

    //// collect specific job.
    smsJobService.getDataForMgnt(0).then(function (data) {
        console.log(self.theOrder);
        var tempArray = data.filter(i => i.jobType == 'SPECIFIC' && i.status == true );
        console.log(tempArray);
        self.smsJobList = self.smsJobList.concat(tempArray);

        console.log(self.smsJobList);
    });

}]);