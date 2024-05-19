 'use strict';
angular.module('bizExpenseModule')
	.controller('bizExpenseController',['$rootScope','$location','memberService','bizExpenseService','AmountList',
									'NgTableParams','BizExpenseStatusArray','BizExpenseDO','uploadService','$timeout',
									'FirstTimeLoadSize','clientListCacheService','shopListCacheService','clientInfoCacheService',
									'currentShopCacheService',
	function($rootScope,$location,memberService,bizExpenseService,AmountList,
	        NgTableParams,BizExpenseStatusArray,BizExpenseDO,uploadService,$timeout,
	        FirstTimeLoadSize,clientListCacheService,shopListCacheService,clientInfoCacheService,
	        currentShopCacheService) {
	var self = this;
	self.theBizExpense = new BizExpenseDO;
	self.statusList = BizExpenseStatusArray;
	self.statusStyle = {};
	self.isSaveButtonPressed=false;
	self.tempArray=[];
	self.tempAmount=0;
	self.OneDayExpense={};
	self.isGodLike = memberService.isGodLike();

    self.queryRequest={};
    self.queryRequest.amount = FirstTimeLoadSize;
    self.currentMember = memberService.getCurrentMember();
    self.latestBizExpense=[];
    self.latestBizExpenseBK=[];

	if(!memberService.isMod()){
		$location.path('#/');
	}

	/*if(self.isGodLike){
        self.queryRequest.clientCode = 'ALL';
        self.queryRequest.shopCode = 'ALL';
    }else{

        self.queryRequest.clientCode  = clientInfoCacheService.get().clientCode;
        if(self.shopList.length == 1){
            self.queryRequest.shopCode = self.shopList[0].shopCode;
            self.theBizExpense.shopCode = self.shopList[0].shopCode;
        }else{
            self.theBizExpense.shopCode = self.shopList[1].shopCode;
            self.queryRequest.shopCode = 'ALL';
        }
    }*/

    self.theBizExpense.clientCode = clientInfoCacheService.get().clientCode;
    self.theBizExpense.shopCode = currentShopCacheService.get().shopCode;

    self.queryRequest.clientCode  = clientInfoCacheService.get().clientCode;
    self.queryRequest.shopCode = currentShopCacheService.get().shopCode;

    self.clientList = clientListCacheService.get();
    self.shopList = shopListCacheService.get().filter(i => i.clientCode == self.queryRequest.clientCode || i.clientCode == '');
    self.clientList2 = clientListCacheService.get();
    self.shopList2 = shopListCacheService.get().filter(i => i.clientCode == self.queryRequest.clientCode || i.clientCode == '');
    console.log(shopListCacheService.get());
    console.log(self.shopList2);


	self.currentMember = memberService.getCurrentMember();
	self.theBizExpense.owner = self.currentMember.name;
	self.theBizExpense.ownerPhone = self.currentMember.phone;

	self.isAdmin = memberService.isAdmin();
    self.isSuperAdmin = memberService.isSuperAdmin();

	self.amountList=AmountList;
	getBizExpenseByCondition(self.queryRequest);
	getLatestBizExpense(self.queryRequest);

//////////////auto complete////////////

    self.searchChange = function(text){
        if(self.latestBizExpenseBK.length > 0){
            console.log(text);
            self.latestBizExpense = self.latestBizExpenseBK.filter(i => i.lensNote.toLowerCase().includes(text.toLowerCase()));
            console.log(self.latestBizExpense);
        }
    }


    self.resultSelectedChange = function(one,theBizExpense){
        if(one){
            theBizExpense.description = one.enrichData;
        }

    }

/////////////////////////
    self.filterBizExpenseAndShopByClientCode = function(clientCode){
        if(clientCode == 'ALL'){
            self.queryRequest.shopCode = 'ALL';
        }else{
            self.shopList2 = shopListCacheService.get().filter(i => i.clientCode == clientCode || i.shopCode == 'ALL');
            self.queryRequest.shopCode = 'ALL';
        }

        getBizExpenseByCondition(self.queryRequest);
    }

     self.filterBizExpenseByShopCode = function(){
        getBizExpenseByCondition(self.queryRequest);
    }

    function getBizExpenseByCondition(queryRequest) {
        self.tableParams = new NgTableParams({}, { dataset: []});
        bizExpenseService.getBizExpenseForMgnt(queryRequest).then(function (data) {
            self.BizExpenseList = data;
            self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});
            if(self.BizExpenseList.length != 100){
                self.amountList.find(i => i.value == 0).name = self.BizExpenseList.length;
            }else{
                self.amountList.find(i => i.value == 0).name = 'all';
            }
        });
    }

    function getLatestBizExpense(queryRequest) {
        bizExpenseService.getLatestLensProduct(queryRequest).then(function (data) {
            self.latestBizExpenseBK = data.map(enrichOrderDetail);
            self.latestBizExpense = data;
            console.log(self.latestBizExpense);
            console.log(self.latestBizExpenseBK);
        });
    }

    function enrichOrderDetail(detail) {
        var lensDetail = "("+detail.odSphere +" "+ detail.odCylinder+" "+detail.odPrism+")" +
                "("+detail.osSphere +" "+ detail.osCylinder+" "+detail.osPrism+")" ;
        var extInfo = detail.orderId+"-"+detail.id
        detail.enrichData = detail.lensNote +" "+ lensDetail + " " +extInfo;

        return detail;
    }




    self.filterShopByClientCode = function(clientCode){
        self.shopList = shopListCacheService.get().filter(i => i.clientCode == clientCode );
    }

	self.uploadPic = function(file) {
	    uploadService.uploadFunction(file,'BIZEXPENSE');
	    self.isShowUploadPic = true;
	}

	self.clearAmount = function() {
        self.tempAmount = 0;
        self.tempArray.forEach((dataOne, index, array) => {
           dataOne.picked = false;
       });
        self.tempArray=[];
    }

    self.selectAllAmount = function() {
        self.tempAmount = 0;
        self.tempArray = self.tableParams.data;
        self.tempArray.forEach((dataOne, index, array) => {
           dataOne.picked = true;
           self.tempAmount += dataOne.amount;
       });
    }

    self.resetQuickPick = function() {
        self.BizExpenseList.forEach((dataOne, index, array) => {
           dataOne.picked = false;
        });
        //self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});
        getBizExpenseByCondition(self.queryRequest);
        self.tempAmount = 0;
        self.isQPPressed = false;
    }

    self.quickStatusUpdate = function() {
        self.tempArray = self.tempArray.filter(i => i.picked == true);
        console.log(self.tempArray);
        if(self.tempArray && self.tempArray.length > 0){
            bizExpenseService.updateBizExpensesStatus(self.tempArray).then(function (data) {
                console.log(data);
                self.responseStr = data.obj;
                self.isStatusUpdate = false;
                self.tempAmount = 0;
                self.tempArray = [];
                //get update data
                getBizExpenseByCondition(self.queryRequest);

            });
        }
    }

    self.quickPick = function() {
        self.tempArray = self.BizExpenseList.filter(i => i.status == 0 && i.ownerPhone == self.currentMember.phone);
        //self.tempArray = self.tempArray;
        self.tempAmount = 0;
        console.log(self.tempArray);
        self.tempArray.forEach((dataOne, index, array) => {
           dataOne.picked = true;
           self.tempAmount += dataOne.amount;
       });
       self.tableParams = new NgTableParams({}, { dataset: self.tempArray});
    }

    self.getOneDayExpense = function() {
        self.OneDayExpense.totalAmount = 0;
        self.OneDayExpense.data = self.BizExpenseList.filter(i => sameDay(new Date(i.gmtCreate), new Date(self.tempForOneDayExpense.gmtCreate)));
        var date = (new Date(self.tempForOneDayExpense.gmtCreate)).getDate();
        date =  date > 9 ? date : '0' + date;
        var month = (new Date(self.tempForOneDayExpense.gmtCreate)).getMonth();
        month =  month + 1 > 9 ? month +1 : '0' + month;

        self.OneDayExpense.date = (new Date(self.tempForOneDayExpense.gmtCreate)).getFullYear() +'-'+ month +'-'+ date;

        self.OneDayExpense.data.forEach((dataOne, index, array) => {
            self.OneDayExpense.totalAmount += dataOne.amount;

        });
        self.tempAmount = self.OneDayExpense.totalAmount;
    }

    function sameDay(d1, d2) {
      return d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate();
    }

	self.calculateAmount = function(one) {
	    self.tempAmount = 0;
        if(one.picked){
            self.tempArray.push(one);
        }else{
            var index = self.tempArray.indexOf(one);
            self.tempArray.splice(index,1);
        }
       self.tempArray.forEach((dataOne, index, array) => {
           self.tempAmount += dataOne.amount;
       });
       //console.log(self.tableParams);
       // for one day report modal
       self.tempForOneDayExpense = one;
    }

	self.upsert = function(bizExpense){
	    self.isSaveButtonPressed=true;
		if(self.picFile){ // need to be written like this to access "result".
			if(self.picFile.result){
				self.theBizExpense.image = self.picFile.result;
			}
		}

		self.responseStr = false;
		self.responseStrFail = false;

		if(self.theBizExpense.shopCode){
		    bizExpenseService.upsert(bizExpense).then(function (data) {
                self.responseStr = data;
                self.isSaveButtonPressed=false;
                if(bizExpense.id == 0){
                    self.BizExpenseList.unshift(data.obj);
                    self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});
                }
            });
		}else{
		    self.isSaveButtonPressed=false;
		    self.responseStrFail ='Cần chọn shop.';
		}
	}

	self.deleteBizExpense = function(bizExpense){
        self.responseStr = false;
        self.responseStrFail = false;
        bizExpenseService.deleteBizExpense(bizExpense).then(function (data) {
            self.responseStr = data;
            var index = self.BizExpenseList.indexOf(bizExpense);
            self.BizExpenseList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.updateBizExpenseStatus = function(bizExpense){
        self.isUpdatingBizExpenseStatus = true;
        bizExpenseService.updateBizExpenseStatus(bizExpense).then(function(data){
            self.responseStr = data.obj;
            self.isUpdatingBizExpenseStatus = false;
        });
    }

    self.getBizExpenseByTerm = function(){
        getBizExpenseByCondition(self.queryRequest);
    }

    self.closeAlert = function(index) {
        self.responseStr = false;
        self.responseStrFail = false;
    };

    self.promptDelete = function(id){
        self.deletingId = self.deletingId ? false : id;
    }
    self.resetDelete = function(){
        self.deletingId = false;
    }
	

	self.updateBizExpense = function(bizExpense){
		self.theBizExpense = bizExpense;
		self.responseStr = false;
		self.responseStrFail = false;
	}
	
	self.clear = function(){
		self.responseStr = false;
		self.responseStrFail = false;
		self.theBizExpense = new BizExpenseDO;
		self.picFile = null;
	}

	self.setStyle = function(status){
        if(status==0){
            self.statusStyle.color = "crimson";
        }else if(status==1){
            self.statusStyle.color = "blue";
        }
        return self.statusStyle;
    }
}]);