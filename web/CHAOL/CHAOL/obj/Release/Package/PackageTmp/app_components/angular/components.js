﻿//COMPONENTS
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.component('encabezado', {
        templateUrl: "/header.html",
        controller: 'encabezadoController'
    });

    app.component('menu', {
        templateUrl: '/menu.html',
        controller: 'menuController'
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------