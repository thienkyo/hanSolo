'use strict';
angular.module('productUpsertModule')
	.controller('productUpsertController',['$rootScope','$routeParams','$location',
										   'productUpsertService','memberService',
										   'productDetailService','CommonStatusArray',
										   'ProductDO','Upload','$timeout','categoryService',
										   'supplierService','uploadService',
	function($rootScope, $routeParams,$location,
			productUpsertService,memberService,
			productDetailService,CommonStatusArray,
			ProductDO,Upload,$timeout,categoryService,supplierService,uploadService) {
		
	var self = this;
	self.statusList = CommonStatusArray;

	if(!memberService.isAdmin()){
        $location.path('#/');
    }

	/// load category
	categoryService.getAllCategories().then(function (data) {
	   self.groupList = data;
    });

    // load supplier
    supplierService.getAllSuppliers().then(function (data) {
        self.supplierList = data
    });

    // load product
    if($routeParams.prodId > 0){
        productUpsertService.getProductById($routeParams.prodId)
            .then(function (data) {
                self.product = data;
        });
    }else{
        self.product = new ProductDO();
    }


    self.pickGroup = function(opt){
        self.product.categories.push(opt);

    //    console.log(self.product);
    }

    self.removeGroup = function(opt){
        var index = self.product.categories.indexOf(opt);
       self.product.categories.splice(index,1);

   //  self.groupList.push(opt);
   //    console.log(self.product);
    }

	
	self.upsert = function(){
		self.responseStr = false;
		console.log(self.product);
		if(self.picFile){
		    console.log(self.picFile.result);
			if(self.picFile.result){
				self.product.images = self.picFile.result;
				self.product.thumbnail=self.picFile.result.split(',')[0];
			}
		}
		
		productUpsertService.upsert(self.product)
		.then(function (data) {
				self.responseStr = data.replyStr;
		});
	}
	
	self.uploadPic = function(files,oldNames) {


	    uploadService.uploadFilesFunction(files,oldNames);




	/*
		    file.upload = Upload.upload({
		      url: url,
		      data: {oldName: url == 'mgnt/uploadFiles' ? self.product.image : '', file: file},
		      headers:{'Content-Type':'application/x-www-form-urlencoded;charset=utf-8'}
		    });

		    file.upload.then(function (response) {
		      $timeout(function () {
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