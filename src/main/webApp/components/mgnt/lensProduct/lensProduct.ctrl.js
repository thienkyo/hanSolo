'use strict';
angular.module('lensProductModule')
	.controller('lensProductController',['$rootScope','$routeParams','$location','FirstTimeLoadSize',
										 'memberService','LensProductDO',
										 'NgTableParams','OrderStatusArray','AmountList',
										 'lensProductService',
	function($rootScope, $routeParams,$location,FirstTimeLoadSize,
	        memberService,LensProductDO,
	        NgTableParams,OrderStatusArray,AmountList,
	        lensProductService
	        ){
	var self = this;


	self.theLensProduct = new LensProductDO();

	self.amountList=AmountList;
	self.amount = FirstTimeLoadSize;
    self.isSuperAdmin = memberService.isSuperAdmin();
	if(!memberService.isAdmin()){
		$location.path('#/');
	}


    lensProductService.getDataForMgnt(self.amount).then(function (data) {
        self.lensProductList = data;
        self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});
    });

    self.getLensProductByAmount = function(){
        lensProductService.getDataForMgnt(self.amount).then(function (data) {
            self.lensProductList = data;
            self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});
        });
    }

    self.prepareData = function(){
        lensProductService.prepareData().then(function (data) {
        });
    }

    self.setTheLensProduct = function(one){
        self.theLensProduct = one;
        self.responseStr = false;
    }

    self.clearLensProduct = function(){
        self.responseStr = false;
        self.theLensProduct = new LensProductDO();
    }

    self.upsertLensProduct = function(one){
        self.isSaveButtonPressed=true;
        self.responseStr = false;
        lensProductService.upsert(one).then(function (data) {
            self.responseStr = data;
            self.isSaveButtonPressed=false;
            if(one.id == 0){
                self.lensProductList.unshift(data.obj);
                self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});
            }
        });
    }

//////////////

    self.promptDeleteMany = function(){
        self.isDeleteMany = true;
    }

    self.resetDeleteMany = function(){
        self.isDeleteMany = false;
    }

    self.promptDelete = function(id){
        self.deletingId = self.deletingId ? false : id;
    }

    self.resetDelete = function(){
        self.deletingId = false;
    }

    self.closeAlert = function(index) {
        self.responseStr = false;
    };

    self.deleteOne = function(one){
        self.responseStr = false;
        self.responseStrFail = false;
        lensProductService.deleteOne(one).then(function (data) {
            self.responseStr = data;
            var index = self.lensProductList.indexOf(one);
            self.lensProductList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
            }
        });
    }

    self.deleteMany = function() {
        var lensList = self.lensProductList.filter(item => item.picked == true);
        lensProductService.deleteMany(lensList).then(function(data){

            for (var i = 0; i < lensList.length; i++){
                 var index = self.lensProductList.indexOf(lensList[i]);
                 self.lensProductList.splice(index,1);
            }
            self.tableParams = new NgTableParams({}, { dataset: self.lensProductList});
        });
    }

/// modal

    self.setSummaryModal = function(one) {
        self.theSummaryModal = one;
        console.log(self.theSummaryModal);
    }

}]);
