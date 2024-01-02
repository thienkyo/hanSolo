'use strict';
angular.module('storeOrderModule')
	.controller('storeOrderController',['$routeParams','$location','memberService','orderListService','SmsUserInfoDO',
										 'OrderStatusArray','cartService','OrderDO','OrderDetailDO','SmsJobDO',
										 'ajaxService','genderArray','smsJobService','AreaCodeList','searchService','storeOrderService',
										 'orderCacheService','commonService','$route','shopListCacheService','clientService',
										 'clientInfoCacheService','currentShopCacheService','clientListCacheService','queryRequestDO',
										 'oneClientShopListCacheService',
	function($routeParams,$location,memberService,orderListService,SmsUserInfoDO,
	            OrderStatusArray,cartService,OrderDO,OrderDetailDO,SmsJobDO,
	            ajaxService,genderArray,smsJobService,AreaCodeList,searchService,storeOrderService,
	            orderCacheService,commonService,$route,shopListCacheService,clientService,
	            clientInfoCacheService,currentShopCacheService,clientListCacheService,queryRequestDO,
	            oneClientShopListCacheService
	            ) {
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
	self.isGodLike = memberService.isGodLike();

	self.splitOrderDetailList = [];
	self.theSplitOrder = new OrderDO();

	self.year = (new Date()).getFullYear();
	self.queryRequest = queryRequestDO;
	self.queryRequest.clientCode = clientInfoCacheService.get().clientCode;
	self.queryRequest.shopCode = currentShopCacheService.get().shopCode;

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
        if(self.theOrder.id != 0){
           newOrderDetail.orderId = self.theOrder.id;
        }
        self.theOrder.orderDetails.push(newOrderDetail);
    }

    self.remove1Tab = function(index,orderDetail){
        self.isSaveButtonPressed = true;
        console.log(orderDetail);
        storeOrderService.deleteOrderDetail(orderDetail).then(function (data) {
            console.log(data);
             if(data.errorCode == 'SUCCESS' ){
                if(self.theOrder.orderDetails.length > 1){
                    self.theOrder.orderDetails.splice(index,1);
                }
                self.calculateOrderTotal(self.theOrder);
                self.isErrorMsg = false;
                self.isSaveButtonPressed = false;
                self.order_return_status = data.errorMessage;
             }else{
                self.isErrorMsg = data.errorMessage;
             }
         });

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

    self.querySearchLens = function(searchText){
        self.queryRequest.generalPurpose = searchText;
        return searchService.searchLensProduct2(self.queryRequest);
    }

   /* self.querySearchOrder = function(searchText){
        if(searchText){
            var url = "search/orderMngt/"+searchText;
            console.log('old search: name');
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });
        }
    }

    self.querySearchOrderByPhone = function(searchText){
        if(searchText){
            var url = "search/orderByPhoneMngt/"+searchText;
            console.log('old search: phone');
            return ajaxService.get(url,null,{}).then(function(response){
                return response.data;
            });
        }
    }*/

    // replace 2 function above.from here
    self.querySearchOrderByNamePhone = function(searchText){
        self.queryRequest.generalPurpose = searchText;
        return searchService.getOrderByNamePhone(self.queryRequest).then(function(data){
            return data;
        });

    }



    self.dummyData =function() {

        var first = ['Nguyễn','Trần','Lê','Phạm','Hoàng','Võ','Phan','Trương','Bùi','Đặng'];
        var middle = ['Văn','Hữu','Thị','Minh','Thuỵ','Võ','Uyên','Ngọc','Bùi','Đặng'];
        var middle2 = ['Bảo','Khánh','Thị','Trọng','Phúc','Quang','Phú','Thái','Bùi','Tài'];
        var last = ['Thiện','Linh','Khâm','Thịnh','Như','Thảo','Lan','Tiên','Mẫn','Lệ'];
        //var dummyData = new OrderDO;
        self.theOrder.shippingAddress = '22/1 đường xx, phường 21, quận 3, Tp hcm';
        self.theOrder.shippingName = first[Math.floor(Math.random() * 10)] + ' ' +middle[Math.floor(Math.random() * 10)] + ' ' +
                                     middle2[Math.floor(Math.random() * 10)] + ' ' +last[Math.floor(Math.random() * 10)] ;
        self.theOrder.shippingPhone ='0912345678'+Math.floor(Math.random() * 10);
        self.theOrder.gender = true;

        //var dummyOrderDetail = new OrderDetailDO();

        self.theOrder.orderDetails[0].clientId ='0';
        self.theOrder.orderDetails[0].shopId ='0';

        self.theOrder.orderDetails[0].framePriceAfterSale = Math.floor(Math.random() * 1000)*1000;
        self.theOrder.orderDetails[0].framePriceAtThatTime = Math.floor(Math.random() * 1000)*1000;
        self.theOrder.orderDetails[0].frameNote = 'velo 30-4500';

        self.theOrder.orderDetails[0].lensNote = 'essilor ASX 1.71';
        self.theOrder.orderDetails[0].lensPrice = Math.floor(Math.random() * 1000)*1000;


        self.theOrder.orderDetails[0].osVasc = '4/10';
        self.theOrder.orderDetails[0].osVacc = '10/10';
        self.theOrder.orderDetails[0].osSphere = '-625';
        self.theOrder.orderDetails[0].osCylinder = '-375';
        self.theOrder.orderDetails[0].osAxis = '165';
        self.theOrder.orderDetails[0].osPrism = '';

        self.theOrder.orderDetails[0].odVasc = '5/10';
        self.theOrder.orderDetails[0].odVacc = '10/10';
        self.theOrder.orderDetails[0].odSphere = '-825';
        self.theOrder.orderDetails[0].odCylinder = '-500';
        self.theOrder.orderDetails[0].odAxis = '20';
        self.theOrder.orderDetails[0].odPrism = '';

        self.theOrder.orderDetails[0].osAdd = '';
        self.theOrder.orderDetails[0].odAdd = '';
        self.theOrder.orderDetails[0].pd = '67';
        self.theOrder.orderDetails[0].wd = '';
        self.theOrder.orderDetails[0].vaNear = '';

        self.theOrder.orderDetails[0].name = 'Lê Nguyễn Thiện Thoại';
        self.theOrder.orderDetails[0].yob ='1983';
        self.theOrder.orderDetails[0].phone = '';
        self.theOrder.orderDetails[0].address = '';
        self.theOrder.orderDetails[0].relationship = '';
        self.theOrder.orderDetails[0].recommendedSpectacles = '';
        self.theOrder.orderDetails[0].orderDetailNote = '';

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
        self.theOrder.total = Math.floor(subTotal - self.theOrder.couponAmount - self.theOrder.customDiscountAmount);
        self.theOrder.remain = subTotal - self.theOrder.couponAmount - self.theOrder.customDiscountAmount - self.theOrder.deposit;
    }

    self.getCoupon = function(code) {
        if(code ==''){
            //self.isErrorMsg ='Cần nhập coupon code.';
            self.theOrder.couponDiscount = 0;
            self.theOrder.couponCode = '';
            self.calculateOrderTotal(self.theOrder);
            return;
        }

        self.queryRequest.generalPurpose = code+"|"+"BILL";
        console.log(self.queryRequest);
        if(self.queryRequest.clientCode == 'GODLIKE'){
            self.queryRequest.clientCode = self.theOrder.clientCode;
            self.queryRequest.shopCode = self.theOrder.shopCode;
        }
        console.log(self.queryRequest);

        storeOrderService.getCoupon3(self.queryRequest).then(function (data) {

             if(data.errorCode == 'SUCCESS' && data.obj.clientCode == self.theOrder.clientCode){
                self.theOrder.couponDiscount = data.obj.value;
                self.theOrder.couponCode = code;
              //  self.isCouponApplied = true;
                self.calculateOrderTotal(self.theOrder);
                self.isErrorMsg = false;
             }else if(data.errorCode == 'SUCCESS' && data.obj.clientCode != self.theOrder.clientCode){
                self.isErrorMsg = 'không hợp lệ';
             }else{
                self.isErrorMsg = data.errorMessage;
             }
        });
    }

    self.getFrameCoupon = function(orderDetail) {
        if(orderDetail.frameDiscountCode.length >4){

            self.queryRequest.generalPurpose = orderDetail.frameDiscountCode+"|"+"FRAME";
            //storeOrderService.getCoupon(orderDetail.frameDiscountCode,'FRAME').then(function (data) {
            storeOrderService.getCoupon3(self.queryRequest).then(function (data) {
                console.log(data);
                 if(data.errorCode == 'SUCCESS'){
                    orderDetail.frameDiscountAmount = data.obj.value;
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
            self.queryRequest.generalPurpose = orderDetail.lensDiscountCode+"|"+"LENS";
            //storeOrderService.getCoupon(orderDetail.lensDiscountCode,'LENS').then(function (data) {
            storeOrderService.getCoupon3(self.queryRequest).then(function (data) {
                 if(data.errorCode == 'SUCCESS'){
                    orderDetail.lensDiscountAmount = data.obj.value;
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

        if(self.theOrder.shippingName && self.theOrder.shippingPhone && self.theOrder.shopCode && self.theOrder.shopCode != 'ALL'){
            if(memberService.isMod()){
                self.isSaveButtonPressed=true;

                self.currentMember = memberService.getCurrentMember();
                self.theOrder.lastModifiedBy = self.currentMember.name+'|'+self.currentMember.phone;

                var gotFrameDiscount = false;
                var gotLensDiscount = false;
                for (var i = 0; i < self.theOrder.orderDetails.length; i++){
                    self.theOrder.orderDetails[i].clientCode = self.theOrder.clientCode;
                    self.theOrder.orderDetails[i].shopCode = self.theOrder.shopCode;
                    if(self.theOrder.orderDetails[i].frameDiscountCode){
                        gotFrameDiscount = true;
                    }
                    if(self.theOrder.orderDetails[i].lensDiscountCode){
                        gotLensDiscount = true;
                    }
                }
                if(self.theOrder.couponCode && (gotFrameDiscount || gotLensDiscount)){
                    self.isErrorMsg ='Không thể áp dụng 2 loại giảm giá cùng lúc';
                    self.isSaveButtonPressed=false;
                    return;
                }
                self.theOrder.customDiscountAmount = self.theOrder.customDiscountAmount ? self.theOrder.customDiscountAmount : 0;
                orderCacheService.addOneOrder(self.theOrder);
                cartService.placeOrder(self.theOrder).then(function (data) {
                    self.order_return_status = data.errorMessage; // return after saving order, order_return_status would be orderid
                    self.isSaveButtonPressed=false;
                    self.isErrorMsg=false;
                    self.theOrder = data.obj;
                    self.theOrder.currentCouponCode = self.theOrder.couponCode;
                    self.theOrder.orderDetails.forEach(self.calculateFramePriceAfterSale);
                    self.calculateOrderTotal();
                    orderCacheService.addOneOrder(self.theOrder);
                    self.newOrderId = self.theOrder.id;

                    if(self.isGodLike){
                        var shop = self.shadowShopList.find(i => i.shopCode == self.theOrder.shopCode );
                        currentShopCacheService.set(shop);
                        var client = self.clientList.find(i => i.clientCode == self.theOrder.clientCode );
                        clientInfoCacheService.set(client);
                    }

                    $location.path('/mgnt/storeOrder/'+data.obj.id);

                });
            }


        }else{
            self.isErrorMsg ='Cần nhập tên/số điện thoại(tối thiểu 3 số), chọn shop.';
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

    self.filterShopByClientCode = function(clientCode){
        self.shopList = self.shadowShopList.filter(i => i.clientCode == clientCode );
    }

    self.setCurrentShopCache = function(shopCode){
        var shop = self.shopList.find(i => i.shopCode == shopCode );
        if(shop){
            currentShopCacheService.set(shop);
        }else{
            currentShopCacheService.clear();
        }
    }

/// get concurrence edit mod
    storeOrderService.getEditMode().then(function (data) {
        self.isMultipleEdit = data;
    });

    self.toggleMultipleEdit = function(){
        self.isSaveButtonPressed=true;
        storeOrderService.toggleMultipleEdit().then(function (data) {
            self.isSaveButtonPressed=false;
            self.isMultipleEdit = data;
        });
    }

////// run when loading page/////
    self.dynamicPopover = {
        content: 'Hello, World!',
        templateUrl: 'myPopoverTemplate.html',
        title: 'Title'
    };


	if(!memberService.isMod()){
		$location.path('#/');
	}

	self.shopList = shopListCacheService.get();
	if(self.isGodLike){ // only godlike get new data from db.
	     self.clientList = clientListCacheService.get();
	     self.shadowClientList = clientListCacheService.get();;
         //self.shopList = shopListCacheService.get();
         self.shadowShopList = shopListCacheService.get();
    }

    if($routeParams.orderId > 0){
        self.queryRequest.generalPurpose = $routeParams.orderId;
        storeOrderService.getOrderById(self.queryRequest)
            .then(function (data) {
                self.theOrder = data.obj;
                console.log(self.theOrder);
                if(self.theOrder){
                    if(!self.theOrder.clientCode){
                        self.theOrder.clientCode = clientInfoCacheService.get().clientCode;
                    }

                    if(self.isGodLike){
                        self.shopList = self.shadowShopList.filter(i => i.clientCode == self.theOrder.clientCode );
                    }

                    if(!self.shopList.find(i => i.shopCode == self.theOrder.shopCode)){
                        self.shopList = oneClientShopListCacheService.get();
                    }

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
                }else{
                    $location.path('/mgnt/storeOrder/0');
                }

        });
    }else{
        self.theOrder = new OrderDO;
        self.theOrder.location='STORE';// buy from
        self.theOrder.orderDetails = [new OrderDetailDO()];
        self.theOrder.gender=true;
        self.theOrder.orderDetails[0].gender=true;
        self.selectedJob = firstSmsJob;
        self.copyActive = true;

        if(!self.isGodLike){
            self.theOrder.clientCode = clientInfoCacheService.get().clientCode;
            if(currentShopCacheService.get()){
                self.theOrder.shopCode = currentShopCacheService.get().shopCode;
            }
        }
    }

    self.isSaveButtonPressed=false;// the "save order" button is pressed or not.

    //// collect specific job.
    smsJobService.getDataForMgnt(0).then(function (data) {
        var tempArray = data.filter(i => i.jobType == 'SPECIFIC' && i.status == true );
        self.smsJobList = self.smsJobList.concat(tempArray);
    });

}]);