﻿//CONFIG
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.config(function ($routeProvider, $locationProvider) {
        $locationProvider.hashPrefix('');
        $routeProvider
            //LOGIN
            .when("/Inicio", {
                css: 'estilos/login.css',
                templateUrl: "login.html",
                controller: "loginController"
            })
            //REGISTRO
            .when("/Registro", {
                templateUrl: "registro.html",
                controller: "registroController"
            })
            //RECUPERAR CONTRASEÑA
            .when("/RecuperarContrasena", {
                css: 'estilos/login.css',
                templateUrl: "recuperarContrasena.html",
                controller: "recuperarContrasenaController"
            })
            //INICIO
            .when("/CHAOL", {
                templateUrl: "modulos/inicio.html",
                controller: "inicioController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            //CLIENTES
            .when("/CHAOL/Clientes", {
                templateUrl: "modulos/administracion/clientes/listado/listadoClientes.html",
                controller: "clientesController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            .when("/CHAOL/Clientes/Nuevo", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html",
                controller: "clienteController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            .when("/CHAOL/Clientes/:ID", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html",
                controller: "clienteController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            //TRANSPORTISTAS
            .when("/CHAOL/Transportistas", {
                templateUrl: "modulos/administracion/transportistas/listado/listadoTransportistas.html",
                controller: "transportistasController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            .when("/CHAOL/Transportistas/Nuevo", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html",
                controller: "transportistaController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            .when("/CHAOL/Transportistas/:ID", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html",
                controller: "transportistaController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            //CHOFERES
            .when("/CHAOL/Choferes", {
                templateUrl: "modulos/administracion/choferes/listado/listadoChoferes.html",
                controller: "choferesController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            .when("/CHAOL/Choferes/Nuevo", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html",
                controller: "choferController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            .when("/CHAOL/Choferes/:ID", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html",
                controller: "choferController",
                resolve: {
                    "currentAuth": ["Auth", function (Auth) {
                        return Auth.$requireSignIn();
                    }]
                }
            })
            //TRACTORES
            .when("/CHAOL/Tractores", {
                templateUrl: "construccion.html"
            })
            .when("/CHAOL/Tractores/Nuevo", {
                templateUrl: "construccion.html"
            })
            .when("/CHAOL/Tractores/Editar", {
                templateUrl: "construccion.html"
            })
            //REMOLQUES
            .when("/CHAOL/Remolques", {
                templateUrl: "construccion.html"
            })
            .when("/CHAOL/Remolques/Nuevo", {
                templateUrl: "construccion.html"
            })
            .when("/CHAOL/Remolques/Editar", {
                templateUrl: "construccion.html"
            })
            //PERFIL
            .when("/CHAOL/Perfil", {
                templateUrl: "construccion.html"
            })
            //AGENDA
            .when("/CHAOL/Fletes", {
                templateUrl: "construccion.html"
            })
            .when("/CHAOL/Fletes/Nuevo", {
                templateUrl: "construccion.html"
            })
            .otherwise({
                redirectTo: '/Inicio'
            });
    });

    //FIREBASE
    var firebaseConfig = {
        apiKey: "AIzaSyA1b0PdoMsklE-_PrZ3eCyj8tctrwKxhR4",
        authDomain: "chaol-75d99.firebaseapp.com",
        databaseURL: "https://chaol-75d99.firebaseio.com",
        projectId: "chaol-75d99",
        storageBucket: "chaol-75d99.appspot.com",
        messagingSenderId: "152632874107"
    };
    firebase.initializeApp(firebaseConfig);
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------