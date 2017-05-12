(function () {
    var app = angular.module('app');

    app.config(function ($routeProvider, $locationProvider) {
        $locationProvider.hashPrefix('');
        $routeProvider
            .when("/Inicio", {
                templateUrl: "login.html",
                controller: "loginController",
                css: 'estilos/login.css'
            })
            .when("/Registro", {
                templateUrl: "registro.html",
                controller: "registroController",
                css: 'estilos/registro.css'
            })
            //CLIENTES
            .when("/clientes/listado", {
                templateUrl: "modulos/administracion/clientes/listado/listadoClientes.html"
            })
            .when("/clientes/nuevo", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html"
            })
            .when("/clientes/editar", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html"
            })
            //TRANSPORTISTAS
            .when("/transportistas/listado", {
                templateUrl: "modulos/administracion/transportistas/listado/listadoTransportistas.html"
            })
            .when("/transportistas/nuevo", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html"
            })
            .when("/transportistas/editar", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html"
            })
            //CHOFERES
            .when("/choferes/listado", {
                templateUrl: "modulos/administracion/choferes/listado/listadoChoferes.html"
            })
            .when("/choferes/nuevo", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html"
            })
            .when("/choferes/editar", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html"
            })
            //TRACTORES
            .when("/tractores/listado", {
                templateUrl: "modulos/administracion/tractores/listado/listadoTractores.html"
            })
            .when("/tractores/nuevo", {
                templateUrl: "modulos/administracion/tractores/captura/capturaTractor.html"
            })
            .when("/tractores/editar", {
                templateUrl: "modulos/administracion/tractores/captura/capturaTractor.html"
            })
            //REMOLQUES
            .when("/remolques/listado", {
                templateUrl: "modulos/administracion/remolques/listado/listadoRemolques.html"
            })
            .when("/remolques/nuevo", {
                templateUrl: "modulos/administracion/remolques/captura/capturaRemolque.html"
            })
            .when("/remolques/editar", {
                templateUrl: "modulos/administracion/remolques/captura/capturaRemolque.html"
            })
            //PERFIL
            .when("/perfil", {
                templateUrl: "modulos/cuenta/perfil/perfil.html"
            })
            //AGENDA
            .when("/fletes/agenda", {
                templateUrl: "modulos/fletes/agenda/agenda.html"
            })
            .when("/fletes/nuevo", {
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