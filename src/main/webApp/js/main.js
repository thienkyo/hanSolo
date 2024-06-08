/*  ---------------------------------------------------
    Template Name: Male Fashion
    Description: Male Fashion - ecommerce teplate
    Author: Colorib
    Author URI: https://www.colorib.com/
    Version: 1.0
    Created: Colorib
---------------------------------------------------------  */

'use strict';

(function ($) {

    /*------------------
        Preloader
    --------------------*/
    $(window).on('load', function () {
        $(".loader").delay(1000).fadeOut();
        $("#preloder").delay(1000).fadeOut("slow");
        /*------------------
            Gallery filter
        --------------------*/
        /*$('.filter__controls li').on('click', function () {
            $('.filter__controls li').removeClass('active');
            $(this).addClass('active');
        });
        if ($('.product__filter').length > 0) {
            var containerEl = document.querySelector('.product__filter');
            var mixer = mixitup(containerEl);
        }*/
    });

   /*------------------
           Background Set
       --------------------*/
       $('.set-bg').each(function () {
           var bg = $(this).data('setbg');
           $(this).css('background-image', 'url(' + bg + ')');
       });

       //Search Switch
       $('.search-switch').on('click', function () {
           $('.search-model').fadeIn(400);
       });

       $('.search-close-switch').on('click', function () {
           $('.search-model').fadeOut(400, function () {
               $('#search-input').val('');
           });
       });

       /*------------------
   		Navigation
   	--------------------*/
       $(".mobile-menu").slicknav({
           prependTo: '#mobile-menu-wrap',
           allowParentLinks: true
       });

       /*------------------
           Accordin Active
       --------------------*/
       $('.collapse').on('shown.bs.collapse', function () {
           $(this).prev().addClass('active');
       });

       $('.collapse').on('hidden.bs.collapse', function () {
           $(this).prev().removeClass('active');
       });

       //Canvas Menu
       $(".canvas__open").on('click', function () {
           $(".offcanvas-menu-wrapper").addClass("active");
           $(".offcanvas-menu-overlay").addClass("active");
       });

       $(".offcanvas-menu-overlay").on('click', function () {
           $(".offcanvas-menu-wrapper").removeClass("active");
           $(".offcanvas-menu-overlay").removeClass("active");
       });



    /*--------------------------
        Select
    ----------------------------*/
    $("select").niceSelect();

    /*-------------------
		Radio Btn
	--------------------- */
    $(".product__color__select label, .shop__sidebar__size label, .product__details__option__size label").on('click', function () {
        $(".product__color__select label, .shop__sidebar__size label, .product__details__option__size label").removeClass('active');
        $(this).addClass('active');     
    });

    /*-------------------
		Scroll
	--------------------- */
    $(".nice-scroll").niceScroll({
        cursorcolor: "#0d0d0d",
        cursorwidth: "5px",
        background: "#e5e5e5",
        cursorborder: "",
        autohidemode: true,
        horizrailenabled: false
    });



    /*------------------
		Magnific
	--------------------*/
    $('.video-popup').magnificPopup({
        type: 'iframe'
    });



})(jQuery);