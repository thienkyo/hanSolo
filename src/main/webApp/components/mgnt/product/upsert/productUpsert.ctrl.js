'use strict';
angular.module('productUpsertModule')
	.controller('productUpsertController',['$rootScope','$routeParams','$location',
										   'productUpsertService','memberService',
										   'productDetailService','CommonStatusArray',
										   'ProductDO','Upload','$timeout','categoryService',
										   'supplierService',
	function($rootScope, $routeParams,$location,
			productUpsertService,memberService,
			productDetailService,CommonStatusArray,
			ProductDO,Upload,$timeout,categoryService,supplierService) {
		
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
/*
        var index = self.groupList.indexOf(opt);
        self.groupList.splice(index,1);*/


        console.log(self.groupList);
        var temp = self.groupList.filter(i => i.id != opt.id)
        self.groupList = temp;

        console.log(self.groupList);
        console.log(self.product);
    }

    self.removeGroup = function(opt){
        var index = self.product.categories.indexOf(opt);
        self.product.categories.splice(index,1);

        self.groupList.push(opt);

        console.log(self.product);
    }

	
	self.upsert = function(){
		self.responseStr = false;
		if(self.picFile){
			if(self.picFile.result){
				self.product.image = self.picFile.result;
			}
		}
		
		productUpsertService.upsert(self.product)
		.then(function (data) {
				self.responseStr = data.replyStr;
		});
	}
	
	self.uploadPic = function(file,url) {
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
		    });
	}
	
}]);