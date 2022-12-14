'use strict';
angular.module('productListModule')
.factory('productListService', ['ajaxService',function(ajaxService) {
		var productListService = {
			get20Products : get20Products,
			getProductsForMgnt : getProductsForMgnt,
			updateProductStatus : updateProductStatus
			};
	return productListService;
	
	function get20Products(){
		var url = "mgnt/get20Products";
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}
	
	function getProductsForMgnt(cateId,amount){
		var url = "mgnt/getProductsForMgnt/"+cateId+"/"+amount;
		return ajaxService.get(url,null,{}).then(function(response){
			return response.data;
		});
	}

    function updateProductStatus(product){
        var url = "mgnt/updateProductStatus";
        return ajaxService.post(url,product,{}).then(function(response){
            return response.data;
        });
    }
 }]);