'use strict';
angular.module('bannerModule')
	.controller('bannerController',['$rootScope','$location','memberService','bannerService',
									'NgTableParams','CommonStatusArray','BannerDO','uploadService','$timeout',
	function($rootScope,$location,memberService,bannerService,NgTableParams,CommonStatusArray,BannerDO,uploadService,$timeout) {
	var self = this;
	self.theBanner = new BannerDO;
	self.statusList = CommonStatusArray;
	self.statusStyle = {};

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
	}
	
	self.upsert = function(banner){
		if(self.picFile){ // need to be written like this to access "result".
			if(self.picFile.result){
				self.theBanner.image = self.picFile.result;
			}
		}

		console.log(banner);
		
		self.responseStr = false;
		self.responseStrFail = false;
		bannerService.upsert(banner).then(function (data) {
			self.responseStr = data;
			if(banner.id == 0){
				self.BannerList.unshift(banner);
				self.tableParams = new NgTableParams({}, { dataset: self.BannerList});
			}
		});
	}

	self.deleteBanner = function(banner){
        self.responseStr = false;
        self.responseStrFail = false;
        bannerService.deleteBanner(banner).then(function (data) {
            self.responseStr = data;
            var index = self.BannerList.indexOf(banner);
            self.BannerList.splice(index,1);
            self.tableParams = new NgTableParams({}, { dataset: self.BannerList});

        },function(error){
            if(error.data.exception == 'org.springframework.dao.DataIntegrityViolationException'){
                self.responseStrFail = error;
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
		self.picFile = null;
	}

	self.setStyle = function(status){
        if(status==0){
            self.statusStyle.color = "crimson";
        }else if(status==1){
            self.statusStyle.color = "blue";
        }
        return self.statusStyle;
    }
}]);