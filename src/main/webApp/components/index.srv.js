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
	
	function isMod(){
		if(isLogin() && currentMember.roles.indexOf("MOD") != -1){
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
		console.log(result);
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
		console.log(result);
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
/*
	function setOrderId(orderId){
        currentOrderId = orderId;
        store.set('orderId', orderId);
        return currentOrderId;
    }*/
/*
    function getOrderId(){
        return store.get('orderId');
    }

    function clearOrderId(){
        currentOrderId = '';
        store.set('orderId', currentOrderId);
    }*/

}])
/*.factory('shipStoreService',['store', function(store) {
	var shipList = [];
	var shipStoreService = {
		setShipList : setShipList,
		getShipList : getShipList,
		isAvailable : isAvailable 
		};
	return shipStoreService;
	
	function setShipList(list){
		shipList = list;
        store.set('shipList', list);
        return shipList;
	}
	
	function getShipList(){
		if (store.get('shipList')) {
			shipList = store.get('shipList');
        }else{
        	store.set(cartName, shipList);
        }
        return shipList;
	}
	
	function isAvailable(){ 
		if (!shipList) {
			shipList = store.get('shipList');
        }
		if(shipList){
			return true;
		}
		return false;
	}
}])*/

.factory('paginationService',['store','PaginationItemDO','PaginationDO', function(store,PaginationItemDO,PaginationDO) {
	var pagination = new PaginationDO;
	var paginationService = {
		builder : builder
		};
	return paginationService;
	
	function builder(pageable){
		pagination.clear();
	   for(var i=1 ; i <= pageable.totalPages ; i++){ 
		   var temp = new PaginationItemDO();
		   temp.first = i == 1 ? true : false;
		   temp.last  = i == pageable.totalPages ? true : false;
		   temp.number = i ;
		   temp.status = i == (pageable.number + 1) ? true : false;
		   pagination.list.push(temp);
	   }
	   pagination.currentNumber = parseInt(pageable.number) +1;
	   pagination.nextNumber = parseInt(pagination.currentNumber) + 1;
	   pagination.previousNumber = pagination.currentNumber == 1 ? 1 : parseInt(pagination.currentNumber) - 1;
	   
	   return pagination;
	}
	
}])

;