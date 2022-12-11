'use strict';
angular.module('memberListModule')
	.controller('memberListController',['$rootScope','$location','memberService','FirstTimeLoadSize',
										 'memberListService','NgTableParams','CommonStatusArray','AmountList',
	function($rootScope,$location,memberService,FirstTimeLoadSize,
	        memberListService,NgTableParams,CommonStatusArray,AmountList) {
	var self = this;
	self.statusList = CommonStatusArray;
	self.statusStyle = { "width": "100px" };
	//self.adminRole = false;
	var role = {
               role: "ADMIN",
               level: "0",
               gmtCreate: (new Date()).getTime(),
               gmtModify: (new Date()).getTime()
             }

	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	self.amountList=AmountList;
	self.amount = FirstTimeLoadSize;
	
	memberListService.getMembersForMgnt(self.amount).then(function (data) {
		self.members = data;
		self.tableParams = new NgTableParams({}, { dataset: self.members});
	});
	
	self.getMemberByTerm = function(){
		memberListService.getMembersForMgnt(self.amount).then(function (data) {
			self.members = data;
			self.tableParams = new NgTableParams({}, { dataset: self.members});
		});
	}
	
	self.updateMember = function(mem){
		self.theMember = mem;

		if(self.theMember.memberRoles.find(i => i.role == 'ADMIN')){
		    self.adminRole = true;
		}else{
		    self.adminRole = false;
		}

		self.responseStr = false;
		self.responseStrFail = false;
	}
	
	self.upsert = function(mem){

	    var isAdmin = mem.memberRoles.find(i => i.role == 'ADMIN');
	    if(self.adminRole){
	        if(!isAdmin){
	            role.name = mem.name;
	            role.phone = mem.phone;
	            mem.memberRoles.push(role);
	        }
	    }else if(isAdmin){
            mem.memberRoles = mem.memberRoles.filter(i => i.role != 'ADMIN');
	    }
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