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
	self.clientList = clientListCacheService.get();
    self.shopList = shopListCacheService.get();
    self.clientList2 = clientListCacheService.get();
    self.shopList2 = shopListCacheService.get();
    self.queryRequest={};
    self.queryRequest.amount = FirstTimeLoadSize;

	if(!memberService.isMod()){
		$location.path('#/');
	}

	if(self.isGodLike){
        self.queryRequest.clientCode = 'ALL';
        self.queryRequest.shopCode = 'ALL';
    }else{
        self.theBizExpense.clientCode = clientInfoCacheService.get().clientCode;
        self.queryRequest.clientCode  = clientInfoCacheService.get().clientCode;
        if(self.shopList.length == 1){
            self.queryRequest.shopCode = self.shopList[0].shopCode;

        }else{
            self.theBizExpense.shopCode = self.shopList[1].shopCode;
            self.queryRequest.shopCode = 'ALL';
        }
    }

	self.currentMember = memberService.getCurrentMember();
	self.theBizExpense.owner = self.currentMember.name;
	self.theBizExpense.ownerPhone = self.currentMember.phone;

	self.isAdmin = memberService.isAdmin();
    self.isSuperAdmin = memberService.isSuperAdmin();

	self.amountList=AmountList;
	getBizExpenseByCondition(self.queryRequest);

//////////////////////////
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
        });
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