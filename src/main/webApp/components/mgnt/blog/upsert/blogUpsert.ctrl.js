'use strict';
angular.module('blogUpsertModule')
	.controller('blogUpsertController',['$rootScope','$routeParams','$location',
										   'blogUpsertService','memberService',
										   'CommonStatusArray',
										   'ArticleDO','Upload','$timeout','uploadService',
	function($rootScope, $routeParams,$location,
			blogUpsertService,memberService,
			CommonStatusArray,
			ArticleDO,Upload,$timeout,uploadService) {
		
	var self = this;
	self.statusList = CommonStatusArray;
	
	if(!memberService.isAdmin()){
		$location.path('#/');
	}
	
	if($routeParams.articleId > 0){
		blogUpsertService.getArticleById($routeParams.articleId)
			.then(function (data) {
				self.article = data;
		});

	}else{
		self.article = new ArticleDO();
	}
	
	self.clear = function(){
		self.responseStr = false;
		self.article = new ArticleDO();
	}
	
	self.upsert = function(){
		if(self.picFile){
			if(self.picFile.result){
				self.article.thumbnail = self.picFile.result;
			}
		}
		
		blogUpsertService.upsert(self.article)
		.then(function (data) {
				self.responseStr = data.replyStr;
		});
	}

	self.uploadPic2 = function(file) {
        uploadService.uploadFunction(file,'BLOG');
        self.isShowUploadPic = true;

    }

    self.uploadPic = function(file) {
        uploadService.uploadFunction(file,'BLOG.DETAIL');
        self.isShowUploadPic = true;

    }


	/////////////////////////tinyMCE/////////////////////
	self.tinymceOptions = { 
		      height: 800,
			  theme: 'modern',
			  plugins: [
			    'advlist autolink lists link image charmap print preview hr anchor pagebreak',
			    'searchreplace wordcount visualblocks visualchars code fullscreen',
			    'insertdatetime media nonbreaking save table contextmenu directionality',
			    'emoticons template paste textcolor colorpicker textpattern imagetools codesample toc'
			  ],
			  toolbar1: 'undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
			  toolbar2: 'print preview media | forecolor backcolor emoticons | codesample',
			  image_advtab: true,
			  templates: [
			    { title: 'Test template 1', content: 'Test 1' },
			    { title: 'Test template 2', content: 'Test 2' }
			  ]
		  };
	
}]);