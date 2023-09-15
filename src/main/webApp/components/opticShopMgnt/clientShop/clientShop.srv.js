'use strict';
angular.module('clientShopModule')
.factory('clientService', ['ajaxService',function(ajaxService) {
		var mainService = {
				getAll : getAll,
				upsert : upsert,
				deleteOne : deleteOne,
				getClientShopList : getClientShopList,
				getClientShopList2 : getClientShopList2
			};
	return mainService;

    function getClientShopList2(){
        var url = "mgnt/getClientShopList/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function getClientShopList(){
        var url = "Hmgnt/getClientShopList/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function getAll(){
		var url = "Hmgnt/getAllClient";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "Hmgnt/upsertClient";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function deleteOne(one){
		var url = "Hmgnt/deleteClient";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}


}])
.factory('shopService', ['ajaxService',function(ajaxService) {
		var mainService = {
				getAll : getAll,
				upsert : upsert,
				deleteOne : deleteOne
			};
	return mainService;


	function getAll(clientId){
		var url = "Hmgnt/getAllShopOneClient/"+clientId;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

	function upsert(one){
		var url = "Hmgnt/upsertShop";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}

	function deleteOne(one){
		var url = "Hmgnt/deleteShop";
		return ajaxService.post(url,one,{}).then(function(response){
			return response.data;
		});
	}


}])

;