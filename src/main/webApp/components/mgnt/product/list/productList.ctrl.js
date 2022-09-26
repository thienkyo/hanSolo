'use strict';
angular.module('productListModule')
	.controller('productListController',['$rootScope','$location',
										 'memberService','productListService',
										 'NgTableParams','categoryService','AmountList','CategoryDO',
	function($rootScope,$location,memberService,productListService,NgTableParams,categoryService,AmountList,CategoryDO) {
	var self = this;
	self.statusStyle = { "width": "100px" };
	if(!memberService.isAdmin()){
		$location.path('#/');
	}

	self.brand =[{brandId:0,categoryName:'all brand'}];

	var firstCategory = new CategoryDO('all categorise');
	var firstBrand = new CategoryDO('all brand');
	//firstCategory.name = 'all categorise';
	//self.categoryList =[firstCategory];

	self.amountList=AmountList;
	self.amount = 50;
	self.categoryId = 0;
	self.brandId = 0;
	
	productListService.getProductsForMgnt(self.categoryId,self.amount).then(function (data) {
		self.products = data;
		console.log(data);
		self.tableParams = new NgTableParams({}, { dataset: self.products});
	});
	
	categoryService.getAllCategories().then(function (data) {
		self.categoryList = data.filter(i => i.type == 'CATEGORY');
		self.categoryList.push(firstCategory);

		self.brandList = data.filter(i => i.type == 'BRANDING');
        self.brandList.push(firstBrand);
	});
	
	self.getProductByTerm = function(cateId,amount,type){
	    console.log(cateId);
	    console.log(amount);
	    console.log(type);
	    switch(type) {
          case 'amount':
            self.categoryId = 0;
            self.brandId = 0;
            break;
          case 'category':
            self.brandId = 0;
            break;
          case 'brand':
              self.categoryId = 0;
              break;
          default:
            // code block
        }

		productListService.getProductsForMgnt(cateId,amount).then(function (data) {
			self.products = data;
			console.log(data);
			self.tableParams = new NgTableParams({}, { dataset: self.products});
		});
	}
	
	self.setStyle = function(status){
		if(status==0){
			self.statusStyle.color = "crimson";
		}else if(status==1){
			self.statusStyle.color = "blue";
		}
		else{
			self.statusStyle = { "width": "100px" }
		}
		return self.statusStyle;
	}
	
}]);