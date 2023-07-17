'use strict';
angular.module('memberListModule')
	.controller('memberListController',['$rootScope','$location','memberService','FirstTimeLoadSize','RoleList',
										 'memberListService','NgTableParams','CommonStatusArray','AmountList',
										 'MemberRoleDO',
	function($rootScope,$location,memberService,FirstTimeLoadSize,RoleList,
	        memberListService,NgTableParams,CommonStatusArray,AmountList,
	        MemberRoleDO
	        ){
	var self = this;
	self.statusList = CommonStatusArray;
	self.statusStyle = { "width": "60px" };
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

	self.isSuperAdmin = memberService.isSuperAdmin();
	self.amountList=AmountList;
	self.roleList=RoleList;
	self.amount = FirstTimeLoadSize;
	
	memberListService.getMembersForMgnt(0).then(function (data) {
		self.members = data;
		console.log(self.members);
		self.tableParams = new NgTableParams({}, { dataset: self.members});
	});
	
	self.getMemberByTerm = function(){
		memberListService.getMembersForMgnt(0).then(function (data) {
			self.members = data;
			self.tableParams = new NgTableParams({}, { dataset: self.members});
		});
	}

	self.updateStatus = function(mem){
        self.isUpdating = true;
        memberListService.updateMemberStatus(mem).then(function(data){
            self.isUpdating = false;
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

	self.updateRole = function(mem){
	    console.log(mem);
	    var currentRole = mem.memberRoles.find(i => i.role == mem.roleToBe);
        console.log(currentRole);
        if(currentRole){
            memberListService.deleteRole(currentRole).then(function (data) {
                console.log(data);
                if(data.errorCode == 'SUCCESS'){
                    mem.memberRoles = mem.memberRoles.filter(i => i.role != mem.roleToBe);
                }

            });
        }else{
            var newRole = new MemberRoleDO();
            newRole.name = mem.fullName;
            newRole.phone = mem.phone;
            newRole.memberId = mem.id;
            newRole.role = mem.roleToBe;
            mem.memberRoles.push(newRole);
            memberListService.upsert(mem).then(function (data) {
                console.log(data);
                if(data.errorCode == 'FAIL'){
                    var index = mem.memberRoles.indexOf(newRole);
                    mem.memberRoles.splice(index,1);

                }
            });
        }

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
			self.responseStr = data.obj;
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
			self.statusStyle = { "width": "60px" }
		}
		return self.statusStyle;
	}
	
}]);