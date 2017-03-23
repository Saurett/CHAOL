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
        })
        //TRANSPORTISTAS
        .when("/transportistas/listado", {
            templateUrl: "administracion/transportistas/listado/listadoTransportistas.html"
        })
        .when("/transportistas/nuevo", {
            templateUrl: "administracion/transportistas/captura/capturaTransportista.html"
        })
        .when("/transportistas/editar", {
            templateUrl: "administracion/transportistas/captura/capturaTransportista.html"
        })
        //CHOFERES
        .when("/choferes/listado", {
            templateUrl: "administracion/choferes/listado/listadoChoferes.html"
        })
        .when("/choferes/nuevo", {
            templateUrl: "administracion/choferes/captura/capturaChofer.html"
        })
        .when("/choferes/editar", {
            templateUrl: "administracion/choferes/captura/capturaChofer.html"
        })
        //TRACTORES
        .when("/tractores/listado", {
            templateUrl: "administracion/tractores/listado/listadoTractores.html"
        })
        .when("/tractores/nuevo", {
            templateUrl: "administracion/tractores/captura/capturaTractor.html"
        })
        .when("/tractores/editar", {
            templateUrl: "administracion/tractores/captura/capturaTractor.html"
        })
        //REMOLQUES
        .when("/remolques/listado", {
            templateUrl: "administracion/remolques/listado/listadoRemolques.html"
        })
        .when("/remolques/nuevo", {
            templateUrl: "administracion/remolques/captura/capturaRemolque.html"
        })
        .when("/remolques/editar", {
            templateUrl: "administracion/remolques/captura/capturaRemolque.html"
        })
        //PERFIL
        .when("/perfil", {
            templateUrl: "cuenta/perfil/perfil.html"
        })
        //AGENDA
        .when("/fletes/agenda", {
            templateUrl: "fletes/agenda/agenda.html"
        })
        .when("/fletes/nuevo", {
            templateUrl: "fletes/captura/capturaFlete.html"
        });
});