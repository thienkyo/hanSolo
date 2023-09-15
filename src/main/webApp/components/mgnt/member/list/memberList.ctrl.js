'use strict';
angular.module('memberListModule')
	.controller('memberListController',['$rootScope','$location','memberService','FirstTimeLoadSize','RoleList',
										 'memberListService','NgTableParams','CommonStatusArray','AmountList',
										 'MemberRoleDO','clientService','ClientDO','ShopDO','clientInfoCacheService',
										 'shopListCacheService','currentShopCacheService','clientListCacheService',
	function($rootScope,$location,memberService,FirstTimeLoadSize,RoleList,
	        memberListService,NgTableParams,CommonStatusArray,AmountList,
	        MemberRoleDO,clientService,ClientDO,ShopDO,clientInfoCacheService,
	        shopListCacheService,currentShopCacheService,clientListCacheService
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
	// self.amount = FirstTimeLoadSize;

	self.queryRequest={};
    self.queryRequest.amount = 0;

	self.isGodLike = memberService.isGodLike();
	resetList();

///////////////////////////////////////////////////////

    self.filterShopByClientCode = function(clientCode){
        self.shopList2 = self.shadowShopList.filter(i => i.clientCode == clientCode || i.shopCode == 'ALL' );
    }

    self.addMember = function(){
        self.theMember = new MemberDO();
        self.clientList2 =  [...self.clientList];
    }

    self.setTheMember = function(mem){
        self.theMember = mem;
        self.responseStr = false;
        self.responseStrFail = false;
        self.clientList2 =  [...self.clientList];
        self.shopList2 = self.shopList.filter(i => i.clientCode == mem.clientCode || i.shopCode == 'ALL' );
    }


	self.filterMemberAndShopByClientCode = function(clientCode){
	   if(clientCode == 'ALL'){
	        self.shopList = self.shopList;
	        self.queryRequest.shopCode = 'ALL';
	        self.memberList = self.shadowMemberList;
	   }else{
	        self.shopList = self.shadowShopList.filter(i => i.clientCode == clientCode || i.shopCode == 'ALL');
            self.memberList = self.shadowMemberList.filter(i => i.clientCode == clientCode);
            self.queryRequest.shopCode = 'ALL';
	   }
	   self.tableParams = new NgTableParams({}, { dataset: self.memberList});
    }

    function resetList() {
         self.queryRequest.clientCode = null;
         self.queryRequest.shopCode = null;
         self.clientList = clientListCacheService.get();
         self.shopList = shopListCacheService.get();
         self.shadowShopList = self.shopList;
         if(self.isGodLike){
               self.queryRequest.clientCode = 'ALL';
               self.queryRequest.shopCode = 'ALL';

               memberListService.getMemberByTerms(self.queryRequest).then(function (data) {
                  data.forEach(getShopName);
                  self.memberList = data;
                  self.shadowMemberList = data;
                  self.tableParams = new NgTableParams({}, { dataset: self.memberList});
               });

        }else{
            //self.clientList = clientListCacheService.get();
            self.queryRequest.clientCode = clientInfoCacheService.get().clientCode;
            //self.shopList = shopListCacheService.get();
            if(self.shopList.length == 1){
                self.queryRequest.shopCode = self.shopList[0].shopCode;
                //currentShopCacheService.set(self.shopList[0]);
            }else{
                self.queryRequest.shopCode = 'ALL';
            }

            memberListService.getMemberByTerms(self.queryRequest).then(function (data) {
               data.forEach(getShopName);
               self.memberList = data;
               self.shadowMemberList = data;
               self.tableParams = new NgTableParams({}, { dataset: self.memberList});
            });

        }
    }
    self.resetList = resetList;


    function getShopName(mem){
        if(mem.shopCode){
            mem.shopName = self.shopList.find(i => i.shopCode == mem.shopCode).shopName;
        }

    }

    self.filterMemberByShopCode = function(){
       if(self.queryRequest.shopCode != 'ALL'){
           self.memberList = self.shadowMemberList.filter(i => i.clientCode == self.queryRequest.clientCode && i.shopCode == self.queryRequest.shopCode);
       }else{
           self.memberList = self.shadowMemberList.filter(i => i.clientCode == self.queryRequest.clientCode);
       }
       self.tableParams = new NgTableParams({}, { dataset: self.memberList});
    }


	self.updateStatus = function(mem){
        self.isUpdating = true;
        memberListService.updateMemberStatus(mem).then(function(data){
            self.isUpdating = false;
        });
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
	    /*var isAdmin = mem.memberRoles.find(i => i.role == 'ADMIN');
	    if(self.adminRole){
	        if(!isAdmin){
	            role.name = mem.name;
	            role.phone = mem.phone;
	            mem.memberRoles.push(role);
	        }
	    }else if(isAdmin){
            mem.memberRoles = mem.memberRoles.filter(i => i.role != 'ADMIN');
	    }*/
		self.responseStr = false;
		self.responseStrFail = false;
		if(mem.clientCode && mem.shopCode && mem.phone && mem.fullName){
		    memberListService.upsertMemberByAdmin(mem).then(function (data) {
                self.responseStr = data.obj;
                console.log(data);

                if(mem.id == 0){
                    self.memberList.unshift(data.obj);
                    self.tableParams = new NgTableParams({}, { dataset: self.memberList});
                }

            });
		}else{
		    self.responseStrFail = 'empty name/phone/client/shop code.';
		}

	}
	
	self.clear = function(){
		self.responseStr = false;
		self.responseStrFail = false;
		self.theMember = {};
	}

    self.closeAlert = function() {
        self.responseStr = false;
        self.responseStrFail = false;
    };
	
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