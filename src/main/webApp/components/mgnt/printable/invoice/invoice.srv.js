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


 ;