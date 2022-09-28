'use strict';
angular.module('bannerModule')
	.controller('bannerController',['$rootScope','$location','memberService','bannerService',
									'NgTableParams','CommonStatusArray','BannerDO','uploadService','$timeout',
	function($rootScope,$location,memberService,bannerService,NgTableParams,CommonStatusArray,BannerDO,uploadService,$timeout) {
	var self = this;
	self.theBanner = new BannerDO;
	self.statusList = CommonStatusArray;

    self.needTextList=[
    	{name : 'need text', value:true },
    	{name : 'not text', value:false }
    ];

    self.typeListForUpsert=[
        {name : 'Home banner', value:'HOMEBANNER' },
        {name : 'Home collection', value:'HOMECOLLECTION' }
    ];

	self.currentType = 'ALL';
	self.typeList=[
    	{name : 'All', value:'ALL' },
    	{name : 'Home banner', value:'HOMEBANNER' },
        {name : 'Home collection', value:'HOMECOLLECTION' }
    ];

	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	bannerService.getBannerForMgnt().then(function (data) {
	    console.log(data);
	    self.shadowBannerList = data;
		self.BannerList = data;
		self.tableParams = new NgTableParams({}, { dataset: self.BannerList});
	});

    self.filterBannerByType = function(type){
        if(type == 'ALL'){
            self.BannerList = self.shadowBannerList;
        }else{
            self.BannerList = self.shadowBannerList.filter(i => i.type == type);
        }
        self.tableParams = new NgTableParams({}, { dataset: self.BannerList});
    }

	
	self.uploadPic = function(file) {


	    uploadService.uploadFunction(file,'BANNER');
	    self.isShowUploadPic = true;

	    /*console.log(file);
	    file.upload = Upload.upload({
	      url: url,
	      data: {oldName: self.theBanner.image, file: file},
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
	    });*/
	}
	
	self.upsert = function(banner){
		if(self.picFile){
			if(self.picFile.result){
				self.theBanner.image = self.picFile.result;
			}
		}
		
		self.responseStr = false;
		self.responseStrFail = false;
		bannerService.upsert(banner).then(function (data) {
			self.responseStr = data;
			if(banner.bannerId == 0){
				self.BannerList.push(data);
				self.tableParams = new NgTableParams({}, { dataset: self.BannerList});
			}
		});
	}
	
	self.updateBanner = function(banner){
		self.theBanner = banner;
		self.responseStr = false;
		self.responseStrFail = false;
	}
	
	self.clear = function(){
		self.responseStr = false;
		self.responseStrFail = false;
		self.theBanner = new BannerDO;
	}
}]);