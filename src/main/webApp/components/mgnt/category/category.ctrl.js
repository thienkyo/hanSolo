'use strict';
angular.module('categoryModule')
.controller('categoryController', ['$scope','categoryService','NgTableParams','memberService','CommonStatusArray','CategoryDO','Upload','$timeout','uploadService',
	function($scope,categoryService,NgTableParams,memberService,CommonStatusArray,CategoryDO,Upload,$timeout,uploadService) {
		var self = this;
		self.statusList = CommonStatusArray;
		self.theCategory = new CategoryDO;
		self.cateList = [];
		self.statusStyle = { "width": "120px" };
		
		if(!memberService.isAdmin()){
			$location.path('#/');
		}
		self.currentMember = memberService.getCurrentMember();
		
		categoryService.getAllCategories().then(function (data) {
		    console.log(data);
			self.cateList = data
			self.tableParams = new NgTableParams({}, { dataset: self.cateList});
		});
		
		self.updateCategory = function(cate){
			self.theCategory = cate;
			self.responseStr = false;
			self.responseStrFail = false;
		}
		
		self.upsert = function(cate){
		    console.log(cate);

		    if(self.picFile){
                if(self.picFile.result){
                    self.theCategory.thumbnail = self.picFile.result;
                }

            }

			self.responseStr = false;
			self.responseStrFail = false;
			categoryService.upsert(cate).then(function (data) {
			    console.log(data);
				self.responseStr = data.errorMessage;
				if(cate.id == 0){
					self.cateList.unshift(data.category);
					self.tableParams = new NgTableParams({}, { dataset: self.cateList});
				}
			});
		}
		
		self.deleteCategory = function(cate){
			self.responseStr = false;
			self.responseStrFail = false;
			categoryService.deleteCategory(cate).then(function (data) {
				self.responseStr = data;
				var index = self.cateList.indexOf(cate);
				self.cateList.splice(index,1);
				self.tableParams = new NgTableParams({}, { dataset: self.cateList});
				
			},function(error){
				if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
					self.responseStrFail = error;
				}
			});
		}
		
		self.clear = function(){
			self.responseStr = false;
			self.responseStrFail = false;
			self.theCategory = new CategoryDO;
			self.picFile = null;
			self.isShowUploadPic = false;
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




		    uploadService.uploadFunction(file,'CATEGORY.COLLECTION');

            self.isShowUploadPic = true;

		/*
            file.upload = Upload.upload({
              url: 'mgnt/uploadFile',
              data: {oldName: self.theCategory.thumbnail , file: file, type: 'CATEGORY.COLLECTION'},
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

