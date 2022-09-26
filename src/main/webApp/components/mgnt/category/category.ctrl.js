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
		//    console.log(data);
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

        }
		
}]);

