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

    }

	self.upsert = function(){
		self.responseStr = false;
		if(self.picFile){
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
	}
	
}]);