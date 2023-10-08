'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$routeParams','$location','memberService','orderListService','SmsUserInfoDO',
										 'OrderStatusArray','cartService','OrderDO','OrderDetailDO','SmsJobDO',
										 'ajaxService','genderArray','smsJobService','AreaCodeList','searchService','storeOrderService',
										 'orderCacheService','commonService','$route',
	function($routeParams,$location,memberService,orderListService,SmsUserInfoDO,
	            OrderStatusArray,cartService,OrderDO,OrderDetailDO,SmsJobDO,
	            ajaxService,genderArray,smsJobService,AreaCodeList,searchService,storeOrderService,
	            orderCacheService,commonService,$route) {
	var self = this;
	//self.orderDetailList = new Array(3).fill(new OrderDetailDO(false));
	//self.orderDetailList.unshift(new OrderDetailDO(true));

	self.DPisOpen = false;
	self.couponDiscount = 0; //%
    self.couponCode = '';
    self.theSpecificSmsUserInfo = new SmsUserInfoDO();

	self.OrderStatusArray=OrderStatusArray;
	self.genderArray=genderArray;
	self.AreaCodeList=AreaCodeList;
	self.statusStyle = { "width": "120px" };
    var firstSmsJob = new SmsJobDO();
	self.smsJobList = [firstSmsJob];
	self.isLocalWeb = commonService.isLocalWeb();

	self.splitOrderDetailList = [];
	self.theSplitOrder = new OrderDO();

	self.year = (new Date()).getFullYear();
	console.log(self.year);

//////////////// function section ////////////

    self.updatePrice = function(){
        self.calculateOrderTotal();
        if(self.theOrder.deposit > 1000){
            self.theOrder.status = 4; // 4 = deposit
        }
    }

    self.copy1Tab = function(tab){
        var newTab = Object.assign({}, tab);
        newTab.id = 0;
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
        var newOrderDetail = new OrderDetailDO();
        newOrderDetail.address = self.theOrder.orderDetails[0].address;
        newOrderDetail.orderId = self.theOrder.id;
        self.theOrder.orderDetails.push(newOrderDetail);
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

   /*
    self.querySearchLens = function(searchText){
        if(searchText){
            var url = "search/productMngt/"+searchText;
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });

        }
    }
    */

    self.querySearchLens = function(searchText){
        return searchService.searchLensProduct(searchText);
    }

    self.querySearchOrder = function(searchText){
        if(searchText){
            var url = "search/orderMngt/"+searchText;
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });
        }
    }

    self.querySearchOrderByPhone = function(searchText){
        if(searchText){
            var url = "search/orderByPhoneMngt/"+searchText;
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });
        }
    }

    self.dummyData =function() {

        var dummyData = new OrderDO;
        dummyData.shippingAddress = '22/1 đường xx, phường 21, quận 3, Tp hcm';
        dummyData.shippingName = 'Lý Thị Minh Trang';
        dummyData.shippingPhone ='09123456789';
        dummyData.gender = true;

        var dummyOrderDetail = new OrderDetailDO();

        dummyOrderDetail.framePriceAfterSale = 3200000;
        dummyOrderDetail.frameNote = 'velo 30-4500';

        dummyOrderDetail.lensNote = 'essilor ASX 1.71';
        dummyOrderDetail.lensPrice = 1450000;


        dummyOrderDetail.osVasc = '4/10';
        dummyOrderDetail.osVacc = '10/10';
        dummyOrderDetail.osSphere = '-625';
        dummyOrderDetail.osCylinder = '-375';
        dummyOrderDetail.osAxis = '165';
        dummyOrderDetail.osPrism = '';

        dummyOrderDetail.odVasc = '5/10';
        dummyOrderDetail.odVacc = '10/10';
        dummyOrderDetail.odSphere = '-825';
        dummyOrderDetail.odCylinder = '-500';
        dummyOrderDetail.odAxis = '20';
        dummyOrderDetail.odPrism = '';

        dummyOrderDetail.osAdd = '';
        dummyOrderDetail.odAdd = '';
        dummyOrderDetail.pd = '67';
        dummyOrderDetail.wd = '';
        dummyOrderDetail.vaNear = '';

        dummyOrderDetail.name = 'Lý Thị Minh Mai Ca';
        dummyOrderDetail.yob ='1983';
        dummyOrderDetail.phone = '';
        dummyOrderDetail.address = '';
        dummyOrderDetail.relationship = '';
        dummyOrderDetail.recommendedSpectacles = '';
        dummyOrderDetail.orderDetailNote = '';
        dummyOrderDetail.gender = false;

        dummyData.orderDetails.push(dummyOrderDetail);

        self.theOrder = dummyData;

        console.log(self.theOrder);
    }

    self.searchTextChange =function(text) {
        //console.log('Text changed to ' + text);
    }

    self.searchLensTextChange =function(text) {
        //console.log('Text changed to ' + text);
    }

    self.selectedItemChange = function(one,orderDetail) {
        if(one){
          orderDetail.product = one;
          orderDetail.frameNote = one.name;
         // orderDetail.framePriceAfterSale = orderDetail.product.sellPrice*(100 - orderDetail.product.discount)/100 * orderDetail.quantity;
          orderDetail.framePriceAfterSale = orderDetail.product.sellPrice*(100 - orderDetail.product.discount)/100;
        }
        self.calculateOrderTotal();
    }

    self.selectedLensChange = function(one,orderDetail) {
        if(one){
          orderDetail.lensNote = one.lensNote;
          orderDetail.lensPrice = one.sellPrice;
        }
        self.calculateOrderTotal();
    }

    self.removeSearchResult = function(orderDetail){
        orderDetail.product = null;
        self.calculateOrderTotal();
    }

    self.searchOrderTextChange =function(text) {
       if(self.copyActive){
            self.nameCopy();
        }
    }

    self.selectedOrderChange = function(searchText) {
        if(self.theSelectedOrder){
            self.theOrder.shippingName = self.theSelectedOrder.shippingName;
            self.theOrder.shippingPhone = self.theSelectedOrder.shippingPhone;
            self.theOrder.shippingAddress = self.theSelectedOrder.shippingAddress;
            self.theOrder.areaCode = self.theSelectedOrder.areaCode;
            self.theOrder.gender = self.theSelectedOrder.gender;

            self.theOrder.orderDetails[0].name = self.theSelectedOrder.shippingName;
            self.theOrder.orderDetails[0].phone = self.theSelectedOrder.shippingPhone;
            self.theOrder.orderDetails[0].address = self.theSelectedOrder.shippingAddress;
            self.theOrder.orderDetails[0].gender = self.theSelectedOrder.gender;
        }
        self.copyActive = false; // disable nameCopy.
    }

/////////// md-autoComplete for phone////////
    self.searchOrderByPhoneTextChange =function(text) {
        if(self.copyActive){
            self.phoneCopy();
        }
    }

    self.calculateOrderTotal = function(){
        var subTotal = 0;
        var temp = 0;
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            if(self.theOrder.orderDetails[i].product){
                self.theOrder.orderDetails[i].framePriceAtThatTime = self.theOrder.orderDetails[i].product.sellPrice;
                self.theOrder.orderDetails[i].frameDiscountAtThatTime = self.theOrder.orderDetails[i].product.discount;
            }else{
                self.theOrder.orderDetails[i].framePriceAtThatTime = self.theOrder.orderDetails[i].framePriceAfterSale;
                self.theOrder.orderDetails[i].frameDiscountAtThatTime = 0;
            }
            temp = self.theOrder.orderDetails[i].framePriceAtThatTime;
            if(self.theOrder.orderDetails[i].frameDiscountAmount && self.theOrder.orderDetails[i].frameDiscountAmount > 0){
                temp = self.theOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theOrder.orderDetails[i].frameDiscountAmount)/100
            }
            subTotal += temp*(100 - self.theOrder.orderDetails[i].frameDiscountAtThatTime)/100*self.theOrder.orderDetails[i].quantity +
                        self.theOrder.orderDetails[i].lensPrice*self.theOrder.orderDetails[i].lensQuantity*(100 - self.theOrder.orderDetails[i].lensDiscountAmount)/100 +
                        self.theOrder.orderDetails[i].otherPrice;
        }
    //  self.theOrder.statusName = OrderStatusArray.find(i => i.value == self.theOrder.status).name;
        self.theOrder.subTotal = subTotal;
        self.theOrder.couponAmount = subTotal*self.theOrder.couponDiscount/100;
        self.theOrder.total = subTotal - self.theOrder.couponAmount;
        self.theOrder.remain = subTotal - self.theOrder.couponAmount - self.theOrder.deposit;
    }

    self.getCoupon = function(code) {
        if(code ==''){
            //self.isErrorMsg ='Cần nhập coupon code.';
            self.theOrder.couponDiscount = 0;
            self.theOrder.couponCode = '';
            self.calculateOrderTotal(self.theOrder);
            return;
        }
         cartService.getCoupon2(code,'BILL').then(function (data) {
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

    self.getFrameCoupon = function(orderDetail) {
        if(orderDetail.frameDiscountCode.length >4){
            cartService.getCoupon2(orderDetail.frameDiscountCode,'FRAME').then(function (data) {
                 if(data.errorCode == 'SUCCESS'){
                    orderDetail.frameDiscountAmount = data.replyStr;
                    self.calculateOrderTotal(self.theOrder);
                    self.isErrorMsg = false;
                 }else{
                    self.isErrorMsg = data.errorMessage;
                    orderDetail.frameDiscountAmount = 0;
                    orderDetail.frameDiscountCode = '';
                 }
             });

        }else if(orderDetail.frameDiscountCode.length == 0){
            orderDetail.frameDiscountAmount = 0;
            orderDetail.frameDiscountCode = '';
            self.calculateOrderTotal(self.theOrder);
        }
    }

    ///// get lens coupon
    self.getLensCoupon = function(orderDetail) {
        if(orderDetail.lensDiscountCode.length >4){
            cartService.getCoupon2(orderDetail.lensDiscountCode,'LENS').then(function (data) {
                 if(data.errorCode == 'SUCCESS'){
                    orderDetail.lensDiscountAmount = data.replyStr;
                    self.calculateOrderTotal(self.theOrder);
                    self.isErrorMsg = false;
                 }else{
                    self.isErrorMsg = data.errorMessage;
                    orderDetail.lensDiscountAmount = 0;
                    orderDetail.lensDiscountCode = '';
                 }
             });

        }else if(orderDetail.lensDiscountCode.length == 0){
            orderDetail.lensDiscountAmount = 0;
            orderDetail.lensDiscountCode = '';
            self.calculateOrderTotal(self.theOrder);
        }
    }

    self.saveOrder = function(){
        console.log(self.theOrder);
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
            if(memberService.isMod()){
                self.isSaveButtonPressed=true;

                self.currentMember = memberService.getCurrentMember();
                self.theOrder.lastModifiedBy = self.currentMember.name+self.currentMember.phone;

                orderCacheService.addOneOrder(self.theOrder);
                cartService.placeOrder(self.theOrder).then(function (data) {
                    self.order_return_status = data.errorMessage; // return after saving order, order_return_status would be orderid
                    self.isSaveButtonPressed=false;
                    self.isErrorMsg=false;
                    self.theOrder = data.obj;
                    self.theOrder.currentCouponCode = self.theOrder.couponCode;
                    self.theOrder.orderDetails.forEach(self.calculateFramePriceAfterSale);
                    self.calculateOrderTotal();
                    self.newOrderId = self.theOrder.id;
                    $location.path('/mgnt/storeOrder/'+data.obj.id);
                    console.log(self.theOrder);
                });
            }
        }else{
            self.isErrorMsg ='Cần nhập tên/số điện thoại(tối thiểu 3 số).';
        }

    }

    self.calculateFramePriceAfterSale = function(orderDetail){
        orderDetail.framePriceAfterSale = orderDetail.framePriceAtThatTime*(100 - orderDetail.frameDiscountAtThatTime)/100;
        orderDetail.currentLensDiscountCode = orderDetail.lensDiscountCode;
        orderDetail.currentFrameDiscountCode = orderDetail.frameDiscountCode;

    }

    self.closeAlert = function(index) {
        self.order_return_status = false;
        self.isErrorMsg = false;
    };

    self.tempF = function() {
       // console.log(self.selectedJob);
    };

    self.controlSplitButton = function(detail) {
        for (var i = 0; i < self.theOrder.orderDetails.length; i++){
            if(self.theOrder.orderDetails[i].isSplit){
                self.wannaSplit = true;
                break;
            }
            self.wannaSplit = false;
        }
        //self.theSplitOrder.orderDetails
        self.theSplitOrder.orderDetails.push(detail);
        self.theSplitOrder.orderDetails = self.theSplitOrder.orderDetails.filter(item => item.isSplit == true);

        var subTotal = 0;
        var temp = 0;
        self.theSplitOrder.subTotal = 0;
        for (var i = 0; i < self.theSplitOrder.orderDetails.length; i++){
            if(self.theSplitOrder.orderDetails[i].product){
                self.theSplitOrder.orderDetails[i].framePriceAtThatTime = self.theSplitOrder.orderDetails[i].product.sellPrice;
                self.theSplitOrder.orderDetails[i].frameDiscountAtThatTime = self.theSplitOrder.orderDetails[i].product.discount;
            }else{
                self.theSplitOrder.orderDetails[i].framePriceAtThatTime = self.theSplitOrder.orderDetails[i].framePriceAfterSale;
                self.theSplitOrder.orderDetails[i].frameDiscountAtThatTime = 0;
            }
            temp = self.theSplitOrder.orderDetails[i].framePriceAtThatTime;
            if(self.theSplitOrder.orderDetails[i].frameDiscountAmount && self.theSplitOrder.orderDetails[i].frameDiscountAmount > 0){
                temp = self.theSplitOrder.orderDetails[i].framePriceAtThatTime*(100 - self.theSplitOrder.orderDetails[i].frameDiscountAmount)/100
            }
            subTotal += temp*(100 - self.theSplitOrder.orderDetails[i].frameDiscountAtThatTime)/100*self.theSplitOrder.orderDetails[i].quantity + self.theSplitOrder.orderDetails[i].lensPrice*(100 - self.theSplitOrder.orderDetails[i].lensDiscountAmount)/100 + self.theSplitOrder.orderDetails[i].otherPrice;
        }

        self.theSplitOrder.subTotal = subTotal;

        if(self.theOrder.orderDetails.filter(item => item.isSplit === undefined || item.isSplit == false ).length == 0){
            self.wannaSplit = false;
        }

    };

    self.splitOrder = function() {
        self.isSaveButtonPressed=true;
        var theSplitOrder = Object.assign({}, self.theOrder);

        theSplitOrder.id = 0;
        theSplitOrder.gmtCreate = (new Date()).getTime();
        theSplitOrder.orderDetails = theSplitOrder.orderDetails.filter(item => item.isSplit == true);
        self.theOrder.orderDetails =  self.theOrder.orderDetails.filter(item => item.isSplit === undefined || item.isSplit == false);

        theSplitOrder.extInfo = theSplitOrder.extInfo + " tách từ order " + self.theOrder.id;
        theSplitOrder.deposit = 0;
        self.theOrder.deposit = 0;
        console.log(theSplitOrder);
        console.log(self.theOrder);

        var orderList = [];
        orderList.push(self.theOrder,theSplitOrder);


        storeOrderService.splitOrder(orderList).then(function(data){
            self.calculateOrderTotal(self.theOrder);
            self.order_return_status = data.errorMessage; // return after saving order,
            self.isSaveButtonPressed=false;
            self.isErrorMsg=false;
            self.wannaSplit = false;
       });

    };

    self.deselectOrder = function() {
       for (var i = 0; i < self.theOrder.orderDetails.length; i++){
           self.theOrder.orderDetails[i].isSplit = false;
       }
       self.theSplitOrder.orderDetails = [];
       self.wannaSplit = false;
    };

////// run when loading page/////
	if(!memberService.isMod()){
		$location.path('#/');
	}

    self.theOrder = new OrderDO;
	 // load product
    if($routeParams.orderId > 0){
        orderListService.getOrderById($routeParams.orderId)
            .then(function (data) {
                self.theOrder = data;

                console.log(self.theOrder);
                self.theOrder.currentCouponCode = self.theOrder.couponCode;
                if(self.theOrder.orderDetails.length > 0){
                    self.theOrder.orderDetails.forEach(self.calculateFramePriceAfterSale);
                    self.calculateOrderTotal(self.theOrder);
                }

                // load jobid for sms send
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
                }
                console.log(self.theOrder);
        });
    }else{
        self.theOrder = new OrderDO;
        self.theOrder.location='STORE';
        self.theOrder.orderDetails = [new OrderDetailDO()];
        self.theOrder.gender=true;
        self.theOrder.orderDetails[0].gender=true;
        self.selectedJob = firstSmsJob;
        self.copyActive = true;
        console.log(self.theOrder);
    }

    self.isSaveButtonPressed=false;// the "save order" button is pressed or not.

    //// collect specific job.
    smsJobService.getDataForMgnt(0).then(function (data) {
        var tempArray = data.filter(i => i.jobType == 'SPECIFIC' && i.status == true );
        self.smsJobList = self.smsJobList.concat(tempArray);
    });

}]);