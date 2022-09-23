'use strict';
angular.module('supplierModule')
.controller('supplierController', ['$scope','supplierService','NgTableParams','memberService','CommonStatusArray','CategoryDO','Upload','$timeout','uploadService',
	function($scope,supplierService,NgTableParams,memberService,CommonStatusArray,CategoryDO,Upload,$timeout,uploadService) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theSupplier = null;
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
		
		self.updateCategory = function(cate){
			self.theSupplier = cate;
			self.responseStr = false;
			self.responseStrFail = false;
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
		
		self.deleteCategory = function(supplier){
			self.responseStr = false;
			self.responseStrFail = false;
			supplierService.deleteSupplier(supplier).then(function (data) {
				self.responseStr = data;
				var index = self.supplierList.indexOf(cate);
				self.supplierList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.supplierList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theSupplier = new CategoryDO;
			self.picFile = null;
		}
		
		self.setStyle = function(status){
			if(status==0){
				self.statusStyle.color = "crimson";
			}else if(status==1){
				self.statusStyle.color = "blue";
			}
			else{
				self.statusStyle = { "width": "120px" }
			}
			return self.statusStyle;
		}

		self.uploadPic = function(file) {
		    uploadService.uploadFunction(file,'SUPPLIERLOGO');
		
		
            /*file.upload = Upload.upload({
              url: 'mgnt/uploadFile',
              data: {oldName: self.theSupplier.logo , file: file, type: 'CATEGORY.COLLECTION'},
              headers:{'Content-Type':'application/x-www-form-urlencoded;charset=utf-8'}
            });

            file.upload.then(function (response) {
              $timeout(function () {
              console.log(response);
                file.result = response.data;
              });
            }, function (response) {
              if (response.status > 0)
                self.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
              // Math.min is to fix IE which reports 200% sometimes
              file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
            });*/
        }
		
}]);

