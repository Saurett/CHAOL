/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app = angular.module('app', ['ngRoute']);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/", {
            templateUrl: "inicio.html"
        })
        //CLIENTES
        .when("/clientes/listado", {
            templateUrl: "administracion/clientes/listado/listadoClientes.html"
        })
        .when("/clientes/nuevo", {
            templateUrl: "administracion/clientes/captura/capturaCliente.html"
        })
        .when("/clientes/editar", {
            templateUrl: "administracion/clientes/captura/capturaCliente.html"
        });
});