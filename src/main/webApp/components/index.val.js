'use strict';
var OrderStatusArray=[
	{name : 'đã đặt', value:0 },
	{name : 'nhận tiền', value:1 },
	{name : 'đã chuyển hàng ', value:2 },
	{name : 'xong', value:3 },
	{name : 'cọc', value:4 },
	{name : 'khách xoá', value:5 },
	{name : 'Không đặt', value:6 }
];

var BizExpenseStatusArray=[
	{name : 'đã tạo', value:0 },
	{name : 'xong', value:1 }
];

var FirstTimeLoadSize=100;

var CommonStatusArray=[
	{name : 'active', value:true },
	{name : 'inactive', value:false }
];

var genderArray=[
	{name : 'Male', value:true },
	{name : 'Female', value:false }
];

var AmountList=[
	{name : '100', value:100 },
    {name : 'all', value:0 }
];
// for sms job
var JobTypeList=[
	{name : 'COMMON', value:'COMMON' },
    {name : 'SPECIFIC', value:'SPECIFIC' },
    {name : 'PARTICULAR', value:'PARTICULAR' },
    {name : 'FASTSMS', value:'FASTSMS' }
];

var AreaCodeList=[
	{name : 'HCM,Thủ Đức,Đồng Nai,Tây Ninh,Vũng Tàu,Long An, Tiền Giang', value:'NEARHCM' },
    {name : '---khác---', value:'OTHERS' },
];

var CouponTypeList=[
	{name : 'BILL', value:'BILL' },
    {name : 'FRAME', value:'FRAME' },
    {name : 'LENS', value:'LENS' }
];

var CouponCreatedByList=[
	{name : 'MANUAL', value:'MANUAL' },
    {name : 'TOOL', value:'TOOL' }
];

var RoleList=[
	{name : 'MOD', value:'MOD' },
	{name : 'ADMIN', value:'ADMIN' },
    {name : 'SUPER_ADMIN', value:'SUPER_ADMIN' }
];

var cacheName = {
    shopInfoCacheName : 'shopInfoCache',
    cusSourceCacheName : 'cusSourceCache'
};

angular.module('app')
		.value('MemberDO', MemberDO)
		.value('OrderDO',OrderDO)
		.value('OrderDetailDO',OrderDetailDO)
		.value('OrderStatusArray',OrderStatusArray)
		.value('CommonStatusArray',CommonStatusArray)
		.value('genderArray',genderArray)
		.value('AmountList',AmountList)
		.value('ProductDO',ProductDO)
		.value('CategoryDO',CategoryDO)
		.value('SupplierDO',SupplierDO)
		.value('ArticleDO',ArticleDO)
		.value('BannerDO',BannerDO)
		.value('BizExpenseDO',BizExpenseDO)
		.value('CouponDO',CouponDO)
		.value('ShipCostDO',ShipCostDO)
		.value('PaginationItemDO',PaginationItemDO)
		.value('BizExpenseStatusArray',BizExpenseStatusArray)
		.value('CustomerSourceDO',CustomerSourceDO)
		.value('CustomerSourceReportDO',CustomerSourceReportDO)
		.value('BizReportDO',BizReportDO)
		.value('ModifiedReportDO',ModifiedReportDO)
		.value('FirstTimeLoadSize',FirstTimeLoadSize)
		.value('SmsUserInfoDO',SmsUserInfoDO)
		.value('SmsQueueDO',SmsQueueDO)
		.value('SmsJobDO',SmsJobDO)
		.value('JobTypeList',JobTypeList)
		.value('KeyManagementDO',KeyManagementDO)
		.value('AreaCodeList',AreaCodeList)
		.value('CouponTypeList',CouponTypeList)
		.value('CouponCreatedByList',CouponCreatedByList)
		.value('PaginationDO',PaginationDO)
		.value('ContractDO',ContractDO)
		.value('SalaryDO',SalaryDO)
		.value('LensProductDO',LensProductDO)
		.value('StrategyDO',StrategyDO)
		.value('ShopConfigDO',ShopConfigDO)
		.value('RoleList',RoleList)
		.value('MemberRoleDO',MemberRoleDO)
		.value('cacheName',cacheName)
		.value('ClientDO',ClientDO)
		.value('ShopDO',ShopDO)
		;

function ShopDO(){
    this.id = 0;
    this.shopName = '';
    this.shopOwnerName = '';
    this.shopPhone = '';
    this.shopOwnerPhone = '';
    this.shopAddress = '';
    this.shopCode = '';
    this.images = '';
    this.description = '';
    this.startDate = (new Date()).getTime();
    this.endDate = '';
    this.description = '';
    this.price = 0;
    this.clientId = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function ClientDO(){
	this.id = 0;
	this.name = '';
	this.phone = '';
    this.address = '';
    this.clientCode = '';
    this.isUnlockSmsFeature = false;
    this.startDate = (new Date()).getTime();
    this.endDate = '';
    this.description = '';
    this.price = 0;
    this.brandName = '';
    this.images = '';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function MemberRoleDO(){
    this.id = 0;
    this.name = '';
    this.phone = '';
    this.role = '';
    this.level = '0';
    this.memberId = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function ShopConfigDO(){
    this.id = 0;
    this.shopName = '';
    this.shopOwner = '';
    this.shopPhone = '';
    this.shopOwnerPhone = '';
    this.address = '';
    this.logo1 = '';
    this.logo2 = '';
    this.isUnlockSmsFeature = false;
    this.startDate = '';
    this.description = '';
    this.price = 0;
    this.otherPrice = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function StrategyDO(){
    this.id = 0;
    this.name = '';
    this.templateContent = '';
    this.isCouponCreated = true;
    this.isSmsCreated = true;
    this.discountValue = null;
    this.gender = 'ALL';
    this.location = 'ALL';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function LensProductDO(){
	this.id = 0;
	this.lensNote = '';
	this.lensDetail = '';
	this.buyPrice = null;
    this.sellPrice = null;
    this.extInfo = '';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function ContractDO(){
	this.id = 0;
	this.employeeName = '';
	this.phone = '';
	this.birthDay = '';
    this.salary = '';
    this.address = '';
    this.note = '';
    this.endDay = '';
    this.beginDay = '';
    this.memberId = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function SalaryDO(){
	this.id = 0;
	this.employeeName = '';
	this.amount = 0;
	this.month = '';
    this.year = '';
    this.bonus = 0;
    this.note = '';
    this.contractId = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function CustomerSourceReportDO(){
	this.id = 0;
	this.name = '';
	this.count = 0;
	this.month = '';
    this.year = '';
    this.customerSourceId = '';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function KeyManagementDO(){
	this.id = 0;
	this.keyPurpose = '';
	this.secretKey = 'secretkey';
	this.timeout = 0;
	this.token = '';
	this.roles = '';
	this.name = '';
	this.isActivate = false;
    this.lastSendSmsDate = (new Date()).getTime();
    this.orderCreateDate = (new Date()).getTime();
}

function ModifiedReportDO(year){
	this.year = year;
	this.details = [];
	this.income = 0;
	this.outcome = 0;
	this.orders = 0;
	this.frames = 0;
	this.lenses = 0;
	this.discountAmount = 0;
}

function BizReportDO(){
	this.id = 0;
	this.month = '';
	this.year = '';
	this.income = 0;
	this.outcome = 0;
	this.discountAmount = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function SmsUserInfoDO(){
    this.id = 0;
    this.phone = '';
    this.gender = false;
    this.lastSendSmsDate = (new Date()).getTime();
    this.orderCreateDate = (new Date()).getTime();
    this.name = '';
    this.jobIdList = '';
    this.location = 'MANUAL';
    this.address = '';
    this.isTestUser = false;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function SmsQueueDO(){
    this.id = 0;
    this.receiverName = '';
    this.receiverPhone = '';
    this.gender = false;
    this.content = '';
    this.status = 'INIT';
    this.weight = '1';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function SmsJobDO(){
    this.id = 0;
    this.jobName = '';
    this.intervalTime = '';
    this.msgContentTemplate = '';
    this.status = false;
    this.specificPhones = '';
    this.noSmsDays = 1;
    this.weight = '1';
    this.isTest = true;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function OrderDO () {
	this.id = 0;
	this.couponCode = '';
	this.currentCouponCode = '';
	this.couponDiscount = 0;
	this.extInfo = '';
	this.shippingAddress = '';
	this.status = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
	this.relationship = '';
	this.location = 'STORE';
	this.deposit = 0;
	this.shippingId = '';
	this.member = null;
	this.lastModifiedBy = '';
	this.shippingName = '';
	this.shippingPhone ='';
	this.orderDetails = [];
	this.cusSource = 0;
	this.lensNumber = 0;
	this.frameNumber = 0;
	this.areaCode = '';
}

function OrderDetailDO () {
	this.id = 0;
	this.product = null;
	this.framePriceAtThatTime = 0;
	this.frameDiscountAtThatTime = 0;
	this.framePriceAfterSale = 0;
	this.frameNote = '';
	this.frameDiscountCode = '';
	this.frameDiscountAmount = 0;
	this.tempFramePrice = 0;

	this.lensNote = '';
	this.lensPrice = 0;
	this.lensDiscountCode = '';
	this.lensDiscountAmount = 0;

	this.otherNote = '';
    this.otherPrice = 0;

	this.osVasc = '';
	this.osVacc = '';
	this.osSphere = '';
	this.osCylinder = '';
	this.osAxis = '';
	this.osPrism = '';

	this.odVasc = '';
    this.odVacc = '';
    this.odSphere = '';
    this.odCylinder = '';
    this.odAxis = '';
    this.odPrism = '';

	this.osAdd = '';
	this.odAdd = '';
	this.pd = '';
	this.wd = '';
	this.vaNear = '';

	this.name = '';
	this.yob ='';
	this.phone = '';
	this.address = '';
	this.relationship = '';
	this.recommendedSpectacles = '';
	this.orderDetailNote = '';

	this.searchText = ''; // for each md-autocomplete in orderDetail array

	this.quantity = 1;
	this.lensQuantity = 1;
	this.weight = 1;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function MemberDO () {
	this.memberId = 0;
	this.fullName = '';
	this.email = '';
	this.pass  = '';
	this.phone = '';
	this.country = '';
	this.city    = '';
	this.district = '';
	this.street   = '';
	this.address  = '';
	this.postCode = '';
	this.status   = 1;
	this.partnerCode='';
	this.gmtCreate = (new Date()).getTime();
	this.gmtModify = (new Date()).getTime();
	
		this.clear = function() { 
		this.memberId = 0;
		this.fullName = '';
		this.email = '';
		this.pass  = '';
		this.phone = '';
		this.country = '';
		this.city    = '';
		this.district = '';
		this.street   = '';
		this.address  = '';
		this.partnerCode = '';
		this.status   = 1;
		this.gmtModify ='';
		this.gmtCreate ='';
		}
}

function ProductDO(){
	this.id = 0;
	this.description = '';
	this.discount = 0;
	this.supplier = null;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
	this.notification = 'còn hàng';
	this.buyPrice = 1000;
	this.sellPrice = 1000;
	this.name = 'name';
	this.quantity = 1;
	this.status = true;
	this.weight = 0.1;
	this.merchantProductId='';
	this.categories=[];
	this.images = null;
	this.thumbnail = '';
}

function CategoryDO(name, type){
	this.id = 0;
	this.name = 'category name';
	this.status = true;
	this.type = 'CATEGORY';
	this.thumbnail = '';
	this.parentId = null;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();

    if(name && name != ''){
        this.name = name;
    }

    if(type && type != ''){
        this.type = type;
    }
}

function SupplierDO(){
	this.id = 0;
	this.name = 'supplier name';
	this.status = true;
	this.logo = '';
	this.address = '';
	this.phone = '';
	this.prefix = '';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function CouponDO(){
	this.id = 0;
	this.name = null;
	this.status = true;
	this.value = null;
	this.image = '';
	this.code = '';
	this.lifespan = null;
	this.quantity = null;
	this.couponType = 'BILL';
	this.createdBy = 'MANUAL';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function ArticleDO(){
	this.id = 0;
	this.description = '';
	this.content = '';
	this.name = '';
	this.thumbnail = '';
	this.status = true;
	this.author = '';
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function BannerDO(){
	this.id = 0;
	this.name = '';
	this.status = true;
	this.needText = true;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
	this.image='';
	this.description='description';
	this.link='';
	this.type='HOMEBANNER';
}

function CustomerSourceDO(){
	this.id = 0;
	this.name = '';
	this.count = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
}

function BizExpenseDO(){
	this.id = 0;
	this.description = '';
	this.status = 0;
	this.orderId = null;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
	this.image='';
	this.amount=null;
	this.billDate=null;
}

function ShipCostDO(){
	this.shipCostId = 0;
	this.region = 'region name';
	this.status = 0;
	this.price=0;
	this.distance=0
}

function PaginationItemDO() {
	this.first = false;
	this.last = false;
	this.number = 0;
	this.status = false;
}

function PaginationDO() {
	this.currentNumber = 1;
	this.previousNumber = 1;
	this.nextNumber = 1;
	this.totalElements = 1;
	this.currentFirstItemIndex = 1;
    this.currentLastItemIndex = 1;
	this.list = [];
	this.clear = function() { 
		this.list = [];
	}
}
