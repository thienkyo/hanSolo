'use strict';
angular.module('customerSourceModule')
.controller('customerSourceController', ['$scope','customerSourceService','NgTableParams','memberService','CommonStatusArray','CustomerSourceDO','Upload','$timeout','uploadService',
	function($scope,customerSourceService,NgTableParams,memberService,CommonStatusArray,CustomerSourceDO,Upload,$timeout,uploadService) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theOne = new CustomerSourceDO;
		self.statusStyle = {};
		self.discountOrderNumber = 0;
		self.totalDiscountAmount = 0;

		if(!memberService.isAdmin()){
			$location.path('#/');
		}
		
		customerSourceService.getAll().then(function (data) {
			self.customerSourceList = data;
			console.log(data);
			self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});
		});
		
		self.setTheOne = function(one){
			self.theOne = one;
			self.responseStr = false;
			self.responseStrFail = false;
		}
		
		self.upsert = function(customerSource){

			self.responseStr = false;
			self.responseStrFail = false;
			customerSourceService.upsert(customerSource).then(function (data) {
				self.responseStr = data.errorMessage;
				//console.log(data);
				if(customerSource.id == 0){
					self.customerSourceList.unshift(data.customerSource);
					self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});
				}
			});
		}
		
		self.deleteOne = function(customerSource){
			self.responseStr = false;
			self.responseStrFail = false;
			customerSourceService.deleteOne(customerSource).then(function (data) {
				self.responseStr = data;
				var index = self.customerSourceList.indexOf(customerSource);
				self.customerSourceList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.customerSourceList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theOne = new CustomerSourceDO;
			self.isShowUploadPic = false;
		}
		
}]);

