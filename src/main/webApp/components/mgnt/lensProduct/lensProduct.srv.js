'use strict';
angular.module('lensProductModule')
.factory('lensProductService', ['ajaxService',function(ajaxService) {
		var lensProductService = {
                getDataForMgnt : getDataForMgnt,
                upsert : upsert,
                prepareData : prepareData,
                deleteOne : deleteOne,
                deleteMany : deleteMany
			};
	return lensProductService;

	function getDataForMgnt(amount){
        var url = "mgnt/getlensProductForMgnt/"+amount;
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

    function upsert(one){
        var url = "mgnt/upsertLensProduct";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

    function prepareData(){
        var url = "mgnt/prepareLensProductData/";
        return ajaxService.get(url,null,{}).then(function(response){
            return response.data;
        });
    }

	function deleteOne(one){
        var url = "mgnt/deleteLensProduct";
        return ajaxService.post(url,one,{}).then(function(response){
            return response.data;
        });
    }

    function deleteMany(many){
        var url = "mgnt/deleteManyLensProduct";
        return ajaxService.post(url,many,{}).then(function(response){
            return response.data;
        });
    }
      
 }]);