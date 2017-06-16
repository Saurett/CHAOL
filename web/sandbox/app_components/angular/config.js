//CONFIG
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
                    "currentAuth": ["Auth", function (Firebase) {
                        return Firebase.Auth.$requireSignIn();
                    }]
                }
            })
            //CLIENTES
            .when("/CHAOL/Clientes", {
                templateUrl: "modulos/administracion/clientes/listado/listadoClientes.html",
                controller: "clientesController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Clientes/Nuevo", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html",
                controller: "clienteController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Clientes/:ID", {
                templateUrl: "modulos/administracion/clientes/captura/capturaCliente.html",
                controller: "clienteController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            //TRANSPORTISTAS
            .when("/CHAOL/Transportistas", {
                templateUrl: "modulos/administracion/transportistas/listado/listadoTransportistas.html",
                controller: "transportistasController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Transportistas/Nuevo", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html",
                controller: "transportistaController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Transportistas/:ID", {
                templateUrl: "modulos/administracion/transportistas/captura/capturaTransportista.html",
                controller: "transportistaController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            //CHOFERES
            .when("/CHAOL/Choferes", {
                templateUrl: "modulos/administracion/choferes/listado/listadoChoferes.html",
                controller: "choferesController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Choferes/Nuevo", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html",
                controller: "choferController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Choferes/:ID", {
                templateUrl: "modulos/administracion/choferes/captura/capturaChofer.html",
                controller: "choferController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            //TRACTORES
            .when("/CHAOL/Tractores", {
                templateUrl: "modulos/administracion/tractores/listado/listadoTractores.html",
                controller: "tractoresController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Tractores/Nuevo", {
                templateUrl: "modulos/administracion/tractores/captura/capturaTractor.html",
                controller: "tractorController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Tractores/:ID", {
                templateUrl: "modulos/administracion/tractores/captura/capturaTractor.html",
                controller: "tractorController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            //REMOLQUES
            .when("/CHAOL/Remolques", {
                templateUrl: "modulos/administracion/remolques/listado/listadoRemolques.html",
                controller: "remolquesController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Remolques/Nuevo", {
                templateUrl: "modulos/administracion/remolques/captura/capturaRemolque.html",
                controller: "remolqueController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Remolques/:ID", {
                templateUrl: "modulos/administracion/remolques/captura/capturaRemolque.html",
                controller: "remolqueController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            //BODEGAS
            .when("/CHAOL/Bodegas", {
                templateUrl: "modulos/administracion/bodegas/listado/listadoBodegas.html",
                controller: "bodegasController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Bodegas/Nuevo", {
                templateUrl: "modulos/administracion/bodegas/captura/capturaBodega.html",
                controller: "bodegaController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Bodegas/:ID", {
                templateUrl: "modulos/administracion/bodegas/captura/capturaBodega.html",
                controller: "bodegaController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    break;
                                case 'transportista':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                case 'chofer':
                                    Firebase.Location.path('/Inicio');
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            //PERFIL
            .when("/CHAOL/Perfil", {
                templateUrl: "construccion.html"
            })
            //AGENDA
            .when("/CHAOL/Fletes", {
                templateUrl: "modulos/fletes/agenda/agenda.html",
                controller: "agendaController",
                resolve: {
                    "currentAuth": ["Auth", function (Firebase) {
                        var auth = Firebase.Auth;
                        var usuario = auth.$getAuth();
                        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                        var firebaseUsuario = Firebase.Object(refUsuario);
                        firebaseUsuario.$loaded().then(function () {
                            switch (firebaseUsuario.tipoDeUsuario) {
                                case 'administrador':
                                    break;
                                case 'cliente':
                                    break;
                                case 'transportista':
                                    break;
                                case 'chofer':
                                    break;
                                default:
                            }
                            return Firebase.Auth.$requireSignIn();
                        });
                    }]
                }
            })
            .when("/CHAOL/Fletes/Nuevo", {
                templateUrl: "construccion.html"
            })
            .when("/CHAOL/Fletes/:ID", {
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