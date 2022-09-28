'use strict';
angular.module('memberListModule')
	.controller('memberListController',['$rootScope','$location','memberService',
										 'memberListService','NgTableParams','CommonStatusArray','AmountList',
	function($rootScope,$location,memberService,memberListService,NgTableParams,CommonStatusArray,AmountList) {
	var self = this;
	self.statusList = CommonStatusArray;
	self.statusStyle = { "width": "100px" };
	
	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = 50;
	
	memberListService.getMembersForMgnt(self.amount).then(function (data) {
	    console.log(data);
		self.members = data;
		self.tableParams = new NgTableParams({}, { dataset: self.members});
	});
	
	self.getMemberByTerm = function(){
		memberListService.getMembersForMgnt(self.amount).then(function (data) {
		    console.log(data);
			self.members = data;
			self.tableParams = new NgTableParams({}, { dataset: self.members});
		});
	}
	
	self.updateMember = function(mem){
		self.theMember = mem;
		self.responseStr = false;
		self.responseStrFail = false;
	}
	
	self.upsert = function(mem){
		self.responseStr = false;
		self.responseStrFail = false;
		memberListService.upsert(mem).then(function (data) {
			self.responseStr = data;
		});
	}
	
	self.clear = function(){
		self.responseStr = false;
		self.responseStrFail = false;
		self.theMember = {};
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