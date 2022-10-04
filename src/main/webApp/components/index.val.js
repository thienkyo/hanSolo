'use strict';
var OrderStatusArray=[
	{name : 'đã đặt', value:0 },
	{name : 'nhận tiền', value:1 },
	{name : 'đã chuyển hàng ', value:2 },
	{name : 'xong', value:3 }
];

var CommonStatusArray=[
	{name : 'active', value:true },
	{name : 'inactive', value:false }
];

var AmountList=[
	{name : '50', value:50 },
    {name : 'all', value:0 }
];

angular
		.module('app') 
		.value('MemberDO', MemberDO)
		.value('OrderDO',OrderDO)
		.value('OrderDetailDO',OrderDetailDO)
		.value('OrderStatusArray',OrderStatusArray)
		.value('CommonStatusArray',CommonStatusArray)
		.value('AmountList',AmountList)
		.value('ProductDO',ProductDO)
		.value('CategoryDO',CategoryDO)
		.value('SupplierDO',SupplierDO)
		.value('ArticleDO',ArticleDO)
		.value('BannerDO',BannerDO)
		.value('CouponDO',CouponDO)
		.value('ShipCostDO',ShipCostDO)
		.value('PaginationItemDO',PaginationItemDO)
		.value('PaginationDO',PaginationDO);

function OrderDO () {
	this.id = 0;
	this.couponCode = '';
	this.couponDiscount = 0;
	this.extInfo = '';
	this.shippingAddress = '';
	this.status = 0;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
	this.relationship = '';
	this.location = '';
	this.deposit = 0;
	this.shippingId = '';
	this.member = null;
	this.shippingName = '';
	this.shippingPhone ='';
	this.orderDetails = [];
}

function OrderDetailDO () {
	this.id = 0;
	this.product = null;
	this.framePriceAtThatTime = 0;
	this.frameDiscountAtThatTime = 0;

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
	this.phone = '';
	this.relationship = '';
	this.recommendedSpectacles = '';

	this.quantity = 1;
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
	this.name = 'coupon name';
	this.status = true;
	this.value = null;
	this.image = '';
	this.code = '';
	this.lifespan = null;
	this.quantity = null;
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
	this.name = 'banner name';
	this.status = true;
	this.needText = true;
	this.gmtCreate = (new Date()).getTime();
    this.gmtModify = (new Date()).getTime();
	this.image='';
	this.description='description';
	this.link='';
	this.type='HOMEBANNER';
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
