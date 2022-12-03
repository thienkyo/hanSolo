'use strict';
angular.module('bizExpenseModule')
	.controller('bizExpenseController',['$rootScope','$location','memberService','bizExpenseService','AmountList',
									'NgTableParams','BizExpenseStatusArray','BizExpenseDO','uploadService','$timeout',
	function($rootScope,$location,memberService,bizExpenseService,AmountList,
	        NgTableParams,BizExpenseStatusArray,BizExpenseDO,uploadService,$timeout) {
	var self = this;
	self.theBizExpense = new BizExpenseDO;
	self.statusList = BizExpenseStatusArray;
	self.statusStyle = {};
	self.isSaveButtonPressed=false;

	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	self.currentMember = memberService.getCurrentMember();
	self.theBizExpense.owner = self.currentMember.name;
	self.theBizExpense.ownerPhone = self.currentMember.phone;
	self.isAccountant = memberService.isAccountant();
	self.isSuperAccountant = memberService.isSuperAccountant();

	self.amountList=AmountList;
    self.amount = 50;
	
	bizExpenseService.getBizExpenseForMgnt(self.amount).then(function (data) {
		self.BizExpenseList = data;
		self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});
	});

	self.uploadPic = function(file) {
	    uploadService.uploadFunction(file,'BIZEXPENSE');
	    self.isShowUploadPic = true;
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
		bizExpenseService.upsert(bizExpense).then(function (data) {
			self.responseStr = data;
			self.isSaveButtonPressed=false;
			if(bizExpense.id == 0){
				self.BizExpenseList.unshift(bizExpense);
				self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});
			}
		});
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
            self.responseStr = data.replyStr;
            self.isUpdatingBizExpenseStatus = false;
        });
    }

    self.getBizExpenseByTerm = function(){
        bizExpenseService.getBizExpenseForMgnt(self.amount).then(function (data) {
            self.BizExpenseList = data;

            self.tableParams = new NgTableParams({}, { dataset: self.BizExpenseList});
        });
    }

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