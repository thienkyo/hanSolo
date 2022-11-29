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
		//self.currentMember = memberService.getCurrentMember();
		
		supplierService.getAllSuppliers().then(function (data) {
			self.supplierList = data
			self.tableParams = new NgTableParams({}, { dataset: self.supplierList});
		});
		
		self.updateSupplier = function(supplier){
			self.theSupplier = supplier;
			self.responseStr = false;
			self.responseStrFail = false;
           // self.picFile = null;
		}
		
		self.upsert = function(supplier){

		    if(self.picFile){
                if(self.picFile.result){
                    self.theSupplier.logo = self.picFile.result;
                }
            }

			self.responseStr = false;
			self.responseStrFail = false;
			supplierService.upsert(supplier).then(function (data) {

				self.responseStr = data.errorMessage;
				if(supplier.id == 0){
					self.supplierList.unshift(data.supplier);
					self.tableParams = new NgTableParams({}, { dataset: self.supplierList});
				}
			});
		}
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theSupplier = new SupplierDO;
			self.isShowUploadPic = false;
			self.picFile.result = null;
			self.picFile = null;
		}

		self.uploadPic = function(file) {
		    uploadService.uploadFunction(file,'SUPPLIERLOGO');
		    self.isShowUploadPic = true;
        }
		
}]);

