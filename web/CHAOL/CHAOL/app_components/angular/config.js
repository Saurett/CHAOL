//CONFIG
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.config(function ($routeProvider, $locationProvider) {
        $locationProvider.hashPrefix('');
        $routeProvider
            //LOGIN
            .when("/Inicio", {
                templateUrl: "login.html",
                controller: "loginController",
                css: 'estilos/login.css'
            })
            //REGISTRO
            .when("/Registro", {
                templateUrl: "registro.html",
                controller: "registroController",
                css: 'estilos/registro.css'
            })
            //RECUPERAR CONTRASEÑA
            .when("/RecuperarContrasena", {
                templateUrl: "recuperarContrasena.html",
                controller: "recuperarContrasenaController",
                css: 'estilos/login.css'
            })
            //INICIO
            .when('/CHAOL', {
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
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html"
            })
            .when("/CHAOL/Clientes/Editar", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html"
            })
            //TRANSPORTISTAS
            .when("/CHAOL/Transportistas", {
                templateUrl: "modulos/administracion/transportistas/listado/listadoTransportistas.html"
            })
            .when("/CHAOL/Transportistas/Nuevo", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html"
            })
            .when("/CHAOL/Transportistas/Editar", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html"
            })
            //CHOFERES
            .when("/CHAOL/Choferes", {
                templateUrl: "modulos/administracion/choferes/listado/listadoChoferes.html"
            })
            .when("/CHAOL/Choferes/Nuevo", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html"
            })
            .when("/CHAOL/choferes/Editar", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html"
            })
            //TRACTORES
            .when("/CHAOL/Tractores", {
                templateUrl: "modulos/administracion/tractores/listado/listadoTractores.html"
            })
            .when("/CHAOL/Tractores/Nuevo", {
                templateUrl: "modulos/administracion/tractores/captura/capturaTractor.html"
            })
            .when("/CHAOL/Tractores/Editar", {
                templateUrl: "modulos/administracion/tractores/captura/capturaTractor.html"
            })
            //REMOLQUES
            .when("/CHAOL/Remolques", {
                templateUrl: "modulos/administracion/remolques/listado/listadoRemolques.html"
            })
            .when("/CHAOL/Remolques/Nuevo", {
                templateUrl: "modulos/administracion/remolques/captura/capturaRemolque.html"
            })
            .when("/CHAOL/Remolques/Editar", {
                templateUrl: "modulos/administracion/remolques/captura/capturaRemolque.html"
            })
            //PERFIL
            .when("/CHAOL/Perfil", {
                templateUrl: "modulos/cuenta/perfil/perfil.html"
            })
            //AGENDA
            .when("/CHAOL/Fletes", {
                templateUrl: "modulos/fletes/agenda/agenda.html"
            })
            .when("/CHAOL/Fletes/Nuevo", {
                templateUrl: "modulos/fletes/captura/capturaFlete.html"
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