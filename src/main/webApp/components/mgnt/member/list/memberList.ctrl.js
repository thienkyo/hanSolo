'use strict';
angular.module('memberListModule')
	.controller('memberListController',['$rootScope','$location','memberService','FirstTimeLoadSize','RoleList',
										 'memberListService','NgTableParams','CommonStatusArray','AmountList',
										 'MemberRoleDO','clientService','ClientDO','ShopDO',
	function($rootScope,$location,memberService,FirstTimeLoadSize,RoleList,
	        memberListService,NgTableParams,CommonStatusArray,AmountList,
	        MemberRoleDO,clientService,ClientDO,ShopDO
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

	self.isGodLike = memberService.isGodLike();
	resetList();

///////////////////////////////////////////////////////

    self.filterShopByClientCode = function(clientCode){
        self.shopList2 = self.shadowShopList.filter(i => i.clientCode == clientCode || i.shopCode == 'ALL' );
    }

    self.addMember = function(){
        self.theMember = new MemberDO();
        self.clientList2 =  [...self.shadowClientList];
    }

    self.setTheMember = function(mem){
        self.theMember = mem;
        self.responseStr = false;
        self.responseStrFail = false;
        self.clientList2 =  [...self.shadowClientList];
        self.shopList2 = self.shadowShopList.filter(i => i.clientCode == mem.clientCode || i.shopCode == 'ALL' );
    }


	self.filterMemberAndShopByClientCode = function(clientCode){

	   if(clientCode == 'ALL'){
	        self.shopList = self.shadowShopList;
	        self.shopCode = 'ALL';
	        self.memberList = self.shadowMemberList;
	   }else{
	        self.shopList = self.shadowShopList.filter(i => i.clientCode == clientCode || i.shopCode == 'ALL');
            self.memberList = self.shadowMemberList.filter(i => i.clientCode == clientCode);
            self.shopCode = 'ALL';
	   }
	   self.tableParams = new NgTableParams({}, { dataset: self.memberList});


    }

    function resetList() {
         self.clientCode = null;
         self.shopCode = null;
         if(self.isGodLike){
            clientService.getClientShopList().then(function (data) {
                  // console.log(data.obj);

                   var allClient = new ClientDO();
                   allClient.clientCode = 'ALL';
                   allClient.brandName = 'all';
                   data.obj.clientList.unshift(allClient);
                   self.clientCode = 'ALL';
                   self.clientList = data.obj.clientList;
                   self.shadowClientList = data.obj.clientList;

                   self.shopList = data.obj.shopList;
                   var allShop = new ClientDO();
                   allShop.shopCode = 'ALL';
                   allShop.shopName = 'all';
                   allShop.shopAddress = 'all';
                   data.obj.shopList.unshift(allShop);
                   self.shopCode = 'ALL';
                   self.shopList = data.obj.shopList;
                   self.shadowShopList = data.obj.shopList;
               });
               memberListService.getMembersForMgnt(0).then(function (data) {
                   data.forEach(getShopName);
                   self.memberList = data;
                   self.shadowMemberList = data;
                   console.log(self.memberList);
                   self.tableParams = new NgTableParams({}, { dataset: self.memberList});
               });
        }else{

        }
    }
    self.resetList = resetList;


    function getShopName(mem){
        //console.log(mem);
        //console.log(self.shopList);
        if(mem.shopCode){
            mem.shopName = self.shopList.find(i => i.shopCode == mem.shopCode).shopName;
        }

    }

    self.filterMemberByShopCode = function(){
       console.log(self.shopCode);

       if(self.shopCode != 'ALL'){
           self.memberList = self.shadowMemberList.filter(i => i.clientCode == self.clientCode && i.shopCode == self.shopCode);
       }else{
           self.memberList = self.shadowMemberList.filter(i => i.clientCode == self.clientCode);
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
		if(mem.clientCode){
		    memberListService.upsertMemberByAdmin(mem).then(function (data) {
                self.responseStr = data.obj;
                console.log(data);

                if(mem.id == 0){
                    self.memberList.unshift(data.obj);
                }

            });
		}else{
		    self.responseStrFail = 'empty client code.';
		}

	}
	
	self.clear = function(){
		self.responseStr = false;
		self.responseStrFail = false;
		self.theMember = {};
	}

    self.closeAlert = function(index) {
        self.responseStr = false;
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