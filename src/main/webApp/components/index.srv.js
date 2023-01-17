'use strict';
angular.module('app')
.factory('memberService',['store', function(store) {
	var currentMember = null;
	var cartName = 'kinhNguyenCart'; //
	var memberService = {
		setCurrentMember : setCurrentMember,
		getCurrentMember : getCurrentMember,
		makeLoginStr : makeLoginStr,
	//	makeSignupStr : makeSignupStr,
		isLogin : isLogin,
		isAdmin :isAdmin,
		isSuperAdmin :isSuperAdmin,
		isAccountant :isAccountant,
		isSuperAccountant :isSuperAccountant,
		isMod : isMod
		};
	return memberService;
	
	function setCurrentMember(member){
	//	var te = new BannerDO;
		currentMember = member;
        store.set('member', member);
        return currentMember;
	}
	
	function getCurrentMember(){
		if (!currentMember) {
            currentMember = store.get('member');
        }
        return currentMember;
	}
	
	function isLogin(){ 
		if (!currentMember) {
            currentMember = store.get('member');
        }
		if(currentMember){
			return true;
		}
		return false;
	}
	
	function isAdmin(){
		if(isLogin() && currentMember.roles.indexOf("ADMIN") != -1){
			return true;
		}
		return false;
	}

	function isSuperAdmin(){
        if(isLogin() && currentMember.roles.indexOf("SUPER_ADMIN") != -1){
            return true;
        }
        return false;
    }
	
	function isMod(){
		if(isLogin() && currentMember.roles.indexOf("MOD") != -1){
			return true;
		}
		return false;
	}

	function isAccountant(){
        if(isLogin() && currentMember.roles.indexOf("ACCOUNTANT") != -1){
            return true;
        }
        return false;
    }

    function isSuperAccountant(){

        if(isLogin() && isAccountant() && currentMember.roles.indexOf("SUPER_ACCOUNTANT") != -1){
            return true;
        }
        return false;
    }
	
	function makeLoginStr(email,pass){
		var deli = 'd3m';
		var result = randomString(getRandomArbitrary(10,20)) + deli;
		
		for(var i = 0; i < 2; i++){
			result += randomString(getRandomArbitrary(10,20)) +deli;
		}
		result += email +deli;
		
		for(var i = 0; i < 10; i++){
			result += randomString(getRandomArbitrary(10,20)) +deli;
		}
		
		result += pass + deli;
		for(var i = 0; i < 10; i++){
			result += randomString(getRandomArbitrary(10,20)) +deli;
		}
		result += randomString(getRandomArbitrary(10,20));
		return btoa(result);
	}
	
	function makeSignupStr(email,pass,fullName,phone){
		var deli = 'o3k';
		var result = randomString(getRandomArbitrary(10,20)) + deli;
		
		for(var i = 0; i < 2; i++){
			result += randomString(getRandomArbitrary(10,20)) +deli;
		}
		result += email +deli;
		
		for(var i = 0; i < 10; i++){
			result += randomString(getRandomArbitrary(10,20)) +deli;
		}
		
		result += pass + deli;
		for(var i = 0; i < 8; i++){
			result += randomString(getRandomArbitrary(10,20)) +deli;
		}
		result += phone;
		return btoa(result) +deli+ fullName;
	}
	
	function randomString(length, chars) {
		var chars = '012456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var result = '';
        for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
        return result;
    }
    function getRandomArbitrary(min, max) {
        return Math.random() * (max - min) + min;
    }

}])
.factory('cartStoreService',['store', function(store) {
	var currentCart = [];
	var currentOrderId = '';
	var cartName = 'kinhNguyenCart';
	var cartStoreService = {
		setCurrentCart : setCurrentCart,
		getCurrentCart : getCurrentCart,
		getQuantity : getQuantity,
		clearCart : clearCart,
//		setOrderId : setOrderId,
//		getOrderId : getOrderId,
//		clearOrderId : clearOrderId,
		};
	return cartStoreService;
	
	function setCurrentCart(cart){
		currentCart = cart;
        store.set(cartName, cart);
        return currentCart;
	}
	
	function getCurrentCart(){
		if (store.get(cartName)) {
			currentCart = store.get(cartName);
        }else{
        	store.set(cartName, currentCart);
        }
        return currentCart;
	}
	
	function getQuantity(){
		if(getCurrentCart()){
			return getCurrentCart().length;
		}
		return 0;
	}
	
	function clearCart(){
		currentCart = [];
		store.set(cartName, currentCart);
	}

}])
.factory('uploadService',['Upload','$timeout', function(Upload,$timeout) {
	var shipList = [];
	var newPicName='tete';
	var uploadService = {
		uploadFunction : uploadFunction,
		uploadFilesFunction : uploadFilesFunction
		};
	return uploadService;

	function uploadFunction(file, type) {

        file.upload = Upload.upload({
          url: 'mgnt/uploadFile',
          data: {oldName: file.oldName ? file.oldName : '' , file: file, type: type},
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

    function uploadFilesFunction(files, oldNames) {

        Upload.upload({
          url: 'mgnt/uploadFiles',
          data: {oldNames: oldNames ? oldNames : '' , files: files},
          arrayKey: '',
          headers:{'Content-Type':'application/x-www-form-urlencoded;charset=utf-8'}
        }).then(function (response) {
          $timeout(function () {
          files.result = response.data;
          });
        }, function (response) {
          if (response.status > 0)
            self.errorMsg = response.status + ': ' + response.data;
        }, function (evt) {
          // Math.min is to fix IE which reports 200% sometimes
          files.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
        });
    }

}])
.factory('paginationService',['store','PaginationItemDO','PaginationDO', function(store,PaginationItemDO,PaginationDO) {
	var pagination = new PaginationDO;
	var paginationService = {
		builder : builder
		};
	return paginationService;
	
	function builder(pageable){ // pageable = current page
		pagination.clear();
	   for(var i=1 ; i <= pageable.totalPages ; i++){ 
		   var temp = new PaginationItemDO();
		   temp.first = i == 1 ? true : false;
		   temp.last  = i == pageable.totalPages ? true : false;
		   temp.number = i ;
		   temp.status = i == (pageable.number + 1) ? true : false;
		   pagination.list.push(temp);
	   }
	   pagination.totalElements = pageable.totalElements;
	   pagination.currentFirstItemIndex = pageable.size*pageable.number +1;
	   pagination.currentLastItemIndex = (pageable.number + 1) == pageable.totalPages ? (pagination.currentFirstItemIndex + pageable.numberOfElements -1)  : pageable.size*(pageable.number + 1);

	   pagination.currentNumber = parseInt(pageable.number) +1;
	   pagination.nextNumber = parseInt(pagination.currentNumber) + 1;
	   pagination.previousNumber = pagination.currentNumber == 1 ? 1 : parseInt(pagination.currentNumber) - 1;
	   
	   return pagination;
	}
	
}])

;