'use strict';
angular.module('app')
.filter('tel', function () {
    return function (tel) {
        if (!tel) { return ''; }

     //   var value = tel.toString().trim().replace(/^\+/, '');
        var value = tel.toString().trim();

       /* if (value.match(/[^0-9]/)) {
            return tel;
        }*/

        var country, city, number;

        switch (value.length) {
            case 10: // +1PPP####### -> C (PPP) ###-####
                country = 1;
                city = value.slice(0, 3);
                number = value.slice(3);
                break;

            case 11: // +CPPP####### -> CCC (PP) ###-####
                country = "";
                city = value.slice(0, 4);
                number = value.slice(4);
                break;

            case 12: // +CCCPP####### -> CCC (PP) ###-####
                country = value.slice(0, 3);
                city = value.slice(3, 5);
                number = value.slice(5);
                break;

            default:
                return tel;
        }

        if (country == 1) {
            country = "";
        }

        number = number.slice(0, 3) + ' ' + number.slice(3);

        return (country + " " + city + " " + number).trim();
    };
})
.filter('arrayToString', function () {
    return function (array) {
        if (!array) { return ''; }
        var returnStr='';
        array.forEach((dataOne, index, array) => {
            returnStr +=  dataOne.name + ',';

        });
        return returnStr;
    };
})

;