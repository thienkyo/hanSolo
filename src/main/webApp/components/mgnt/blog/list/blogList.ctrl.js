'use strict';
angular.module('blogListModule')
	.controller('blogListController',['$rootScope','$location','memberService','blogListService','NgTableParams','AmountList',
	function($rootScope,$location,memberService,blogListService,NgTableParams,AmountList) {
	var self = this;
	self.statusStyle = { "width": "100px" };
	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	
	self.amount = 50;
	
	
	blogListService.getBlogsForMgnt(self.amount).then(function (data) {
		self.blogList = data;
		self.tableParams = new NgTableParams({}, { dataset: self.blogList});
	});	
	
	self.getBlogByTerm = function(){
		blogListService.getBlogsForMgnt(self.amount).then(function (data) {
			self.orderList = data;;
			self.tableParams = new NgTableParams({}, { dataset: self.orderList});
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