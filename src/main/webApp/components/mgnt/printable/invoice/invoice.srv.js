'use strict';
angular.module('invoiceModule')
.factory('invoiceService', ['ajaxService',function(ajaxService) {
    var invoiceService = {
        getOneOrder : getOneOrder
    };
	return invoiceService;

	function getOneOrder(id){
        var url = "mgnt/getOrderById/"+id;
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }
}])
.factory('orderCacheService',['store', function(store) {
	var currentOrderCache = [];
	var cacheName = 'kinhNguyenOrderCache';
	var orderCacheService = {
		setCurrentOrderCache : setCurrentOrderCache,
		getCurrentOrderCache : getCurrentOrderCache,
		addOneOrder : addOneOrder,
		getQuantity : getQuantity,
		getOneOrder : getOneOrder,
		clearCache : clearCache
		}
	return orderCacheService;

	function getOneOrder(orderId){
	    currentOrderCache = getCurrentOrderCache();
        return currentOrderCache.find(i => i.id == orderId);
	}

	function addOneOrder(order){
	    currentOrderCache = getCurrentOrderCache();
	    var found = currentOrderCache.find(i => i.id == order.id);

	    if(found){
	        var index = currentOrderCache.indexOf(found);
            currentOrderCache[index] = order;
	    }else{
            currentOrderCache.unshift(order);
            if(getQuantity() > 100){
                currentOrderCache.pop();
            }
	    }
	    setCurrentOrderCache(currentOrderCache);
    }

	function setCurrentOrderCache(orderList){
        currentOrderCache = orderList;
        store.set(cacheName, orderList);
        return currentOrderCache;
    }

    function getCurrentOrderCache(){
        if (store.get(cacheName)) {
            currentOrderCache = store.get(cacheName);
        }else{
            store.set(cacheName, currentOrderCache);
        }
        return currentOrderCache;
    }

    function getQuantity(){
        if(getCurrentOrderCache()){
            return getCurrentOrderCache().length;
        }
        return 0;
    }

    function clearCache(){
        currentOrderCache = [];
        store.set(cacheName, currentOrderCache);
    }

}])

 ;