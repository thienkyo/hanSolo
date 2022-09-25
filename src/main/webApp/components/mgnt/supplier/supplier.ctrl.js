'use strict';
angular.module('supplierModule')
.controller('supplierController', ['$scope','supplierService','NgTableParams','memberService','CommonStatusArray','SupplierDO','$timeout','uploadService',
	function($scope,supplierService,NgTableParams,memberService,CommonStatusArray,SupplierDO,$timeout,uploadService) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theSupplier = new SupplierDO();
		self.supplierList = [];
		self.statusStyle = { "width": "120px" };
		
		if(!memberService.isAdmin()){
			$location.path('#/');
		}
		self.currentMember = memberService.getCurrentMember();
		
		supplierService.getAllSuppliers().then(function (data) {
		    console.log(data);
			self.supplierList = data
			self.tableParams = new NgTableParams({}, { dataset: self.supplierList});
		});
		
		self.updateSupplier = function(supplier){
			self.theSupplier = supplier;
			self.responseStr = false;
			self.responseStrFail = false;
            self.picFile = null;
		}
		
		self.upsert = function(supplier){
		    console.log(supplier);

		    if(self.picFile){
                if(self.picFile.result){
                    self.theSupplier.logo = self.picFile.result;
                }
            }

			self.responseStr = false;
			self.responseStrFail = false;
			supplierService.upsert(supplier).then(function (data) {
			    console.log(data);
				self.responseStr = data.errorMessage;
				if(supplier.id == 0){
					self.supplierList.unshift(data.supplier);
					self.tableParams = new NgTableParams({}, { dataset: self.supplierList});
				}
			});
		}
		
	/*	self.deleteCategory = function(supplier){
			self.responseStr = false;
			self.responseStrFail = false;
			supplierService.deleteSupplier(supplier).then(function (data) {
				self.responseStr = data;
				var index = self.supplierList.indexOf(supplier);
				self.supplierList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.supplierList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}*/
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theSupplier = new SupplierDO;
			self.isShowUploadPic = false;
			self.picFile.result = null;
			self.picFile = null;
		}

		self.uploadPic = function(file) {
		    console.log(file);
		    console.log(file.result);
		    uploadService.uploadFunction(file,'SUPPLIERLOGO');
		    self.isShowUploadPic = true;
		   /* console.log(file);
		    console.log(self.picFile);
		    console.log(self.picFile.result);
		    self.theSupplier.logo = file.result;*/
        }
		
}]);

