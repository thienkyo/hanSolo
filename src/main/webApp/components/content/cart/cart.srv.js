'use strict';
angular.module('cartModule')
.factory('cartService', ['$rootScope','ajaxService','cartStoreService',function($rootScope,ajaxService,cartStoreService) {
	var cartService = {
		//	getFirst6Product : getFirst6Product,
			getProductForCart : getProductForCart,
			addToCart : addToCart,
			placeOrder : placeOrder,
			getShipCost : getShipCost,
			getActiveShipCost : getActiveShipCost,
			placeGuestOrder : placeGuestOrder
		};
	return cartService;
/*	
   function getFirst6Product(){
		var url = "products/first6";
		return ajaxService.get(url,null,{}).then(function(data){
			return data.data;
		});
   }*/
	      
   function getProductForCart(prodIds){
		var url = "products/getProductForCart";
		return ajaxService.post(url,prodIds,{}).then(function(response){
			return response.data;
		});
   }
  
   function addToCart(prod,qty){
	   prod.description = '';
   	   var currentItem={prod:prod,quantity:qty};
   	   var currentCart=cartStoreService.getCurrentCart();
		
		if(cartStoreService.getQuantity() == 0){
			currentCart.push(currentItem);
		}else{
			var flag = false;
			for (var i = 0; i < currentCart.length; i++){
			    if(currentCart[i].prod.prodId == prod.prodId){
			    	currentCart[i].quantity = currentCart[i].quantity + 1;
			    	flag = false;
			    	break;
			    }else{
			    	flag = true;
			    }
			}
			if(flag){
				currentCart.push(currentItem);
			}
		}
		cartStoreService.setCurrentCart(currentCart);
		$rootScope.$broadcast('addToCart');
   }
   
   function placeOrder(order){
	   var url = "authenticated/saveOrder";
	   return ajaxService.post(url,order,{}).then(function(response){
			return response.data;
	   });
   }

   function placeGuestOrder(order){
   	   var url = "guest/saveOrder";
   	   return ajaxService.post(url,order,{}).then(function(response){
   			return response.data;
   	   });
      }
   
   function getShipCost(){
		var url = "shipcost";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
   }

   function getActiveShipCost(){
   		var url = "shipcost/active";
   		return ajaxService.get(url,null,{}).then(function(response){
   			return response.data;
   		});
      }
   
 }]);
