/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//APP
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app', [
        'ngRoute',
        'ngMaterial',
        'ngMessages',
        'routeStyles',
        'firebase',
        'ngSanitize'
    ]);

    app.run(function ($rootScope, $location, $firebaseAuth) {
        $rootScope.$on('$routeChangeError', function (event, next, previous, error) {
            if (error === 'AUTH_REQUIRED') {
                console.log('No cuenta con los permisos suficientes');
                $location.path('/Inicio');
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
//SERVICES
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.factory('unixTime', function () {
        var getTime = function () {
            return Math.round((new Date()).getTime() / 1000);
        };
        return getTime;
    });

    app.factory("Auth", function ($firebaseAuth, $firebaseObject, $location) {
        var firebase = {
            Auth: $firebaseAuth(),
            Object: $firebaseObject,
            Location: $location
        }
        return firebase
    });

    //SERCIVIO QUE TERMINARÁ LA SESIÓN DEL USAURIO EN CASO DE QUE SEA ELIMINADO O INACTIVO
    app.factory('WatchUser', function ($firebaseAuth, $firebaseObject, $location) {
        return {
            checkUser: function (refUser) {
                refUser.on('value', function (snap) {
                    var user = $firebaseObject(refUser);
                    user.$loaded().then(function () {
                        var auth = $firebaseAuth();
                        var userAuth = auth.$getAuth();
                        if (user.firebaseId === userAuth.uid) {
                            if (user.estatus === 'inactivo' || user.estatus === 'eliminado') {
                                auth.$signOut();
                                $location.path('/Inicio');
                            }
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                })
            }
        }
    });

    //SERVICIO PARA DETERMINAR SI EL USUARIO ESTÁ ACTIVO AL MOMENTO DE INICIAR SESIÓN
    app.factory('AllowLogIn', function ($location, $firebaseObject, $firebaseAuth, $mdDialog, WatchUser) {
        return {
            getValidation: function (usuario) {
                usuario.$loaded().then(function () {
                    var refObjeto;
                    var objeto;
                    switch (usuario.tipoDeUsuario) {
                        case 'administrador':
                            $location.path('/CHAOL');
                            break;
                        case 'cliente':
                            refObjeto = firebase.database().ref('clientes').child(usuario.$id).child('cliente');
                            objeto = $firebaseObject(refObjeto);
                            break;
                        case 'transportista':
                            refObjeto = firebase.database().ref('transportistas').child(usuario.$id).child('transportista');
                            objeto = $firebaseObject(refObjeto);
                            break;
                        case 'chofer':
                            refObjeto = firebase.database().ref('choferes').child(usuario.$id);
                            objeto = $firebaseObject(refObjeto);
                            break;
                        default:
                    }
                    if (objeto) {
                        objeto.$loaded().then(function () {
                            if (objeto.estatus === 'inactivo' || objeto.estatus === "eliminado") {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#login')))
                                        .clickOutsideToClose(false)
                                        .title('Oops! Algo salió mal')
                                        .htmlContent('<br/> <p>Al parecer el administrador del sistema no ha activado esta cuenta.</p> <p>Por favor, espera a que seas activado o envía un mensaje a <b>chaolapp@gmail.com</b> para solicitar tu activación.</p>')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar')
                                );
                                var session = $firebaseAuth();
                                session.$signOut();
                            }
                            else {
                                WatchUser.checkUser(refObjeto);
                                $location.path('/CHAOL');
                            }
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            }
        }
    });

    app.factory('createUserService', function () {
        var firebaseConfig = {
            apiKey: "AIzaSyBtXFJ35HfMROCXKeBEWUAkXow51Z57C2U",
            authDomain: "chaol-75d99.firebaseapp.com",
            databaseURL: "https://chaol-75d99.firebaseio.com",
            projectId: "chaol-75d99",
            storageBucket: "chaol-75d99.appspot.com",
            messagingSenderId: "152632874107"
        };

        var createUserService = firebase.initializeApp(firebaseConfig, 'Secondary');
        return createUserService;
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//COMPONENTS
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
//LOGIN CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('loginController', function ($scope, $location, $firebaseObject, $firebaseAuth, $mdDialog, AllowLogIn) {
        var auth = $firebaseAuth();
        auth.$signOut();

        //ENVIO AL FORMULARIO DE REGISTRO
        $scope.registro = function () {
            $location.path("/Registro");
        };

        //INICIO DE SESIÓN
        $scope.iniciarSesion = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            auth = $firebaseAuth();
            auth.$signInWithEmailAndPassword($scope.usuario.usuario, $scope.usuario.contrasena).then(function () {
                console.log($scope.usuario.usuario + ' logged in');
                var user = auth.$getAuth();
                var refUsuario = firebase.database().ref('usuarios').child(user.uid);
                var usuario = $firebaseObject(refUsuario);
                AllowLogIn.getValidation(usuario);
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }).catch(function (error) {
                //ERROR
                if (error.code === 'auth/user-not-found') {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#login')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/> <p>No existen registros del usuario correspondiente a <b>' + $scope.usuario.usuario + '</b>.</p> <p>El usuario pudo haber sido eliminado. Por favor, valida la dirección que ingresaste.</p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
                else if (error.code === 'auth/wrong-password') {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#login')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/><p>La contraseña es inválida o <b>' + $scope.usuario.usuario + '</b> no cuenta con una.</p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
                else {
                    console.log(error);
                }
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//REGISTRO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('registroController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage) {
        var refUsuarios = firebase.database().ref().child('usuarios');

        //CLIENTE
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //INICIALIZAR CLIENTE
        $scope.firebaseCliente = {
            cliente: {
                nombre: "",
                rfc: "",
                numeroInterior: "",
                numeroExterior: "",
                calle: "",
                colonia: "",
                ciudad: "",
                estado: "",
                codigoPostal: "",
                metodoDePago: "",
                telefono: "",
                celular: "",
                correoElectronico: "",
                contrasena: "",
                estatus: "inactivo",
                fechaDeCreacion: unixTime(),
                fechaDeEdicion: unixTime(),
                imagenURL: "",
                tipoDeUsuario: "cliente",
                firebaseId: ""
            }
        };

        //LISTADO MÉTODOS DE PAGO
        $scope.metodosDePago =
            [
                "Efectivo",
                "Cheque",
                "Transferencia Electrónica",
                "Tarjeta de Crédito",
                "Dinero Electróncio",
                "Tarjeta de Débito"
            ];

        //GUARDAR CLIENTE
        var refCliente = firebase.database().ref().child('clientes');

        $scope.registrarCliente = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var auth = $firebaseAuth();
            var usuario = auth.$getAuth();
            //SE CREA EL USUARIO
            auth.$createUserWithEmailAndPassword($scope.firebaseCliente.cliente.correoElectronico, $scope.firebaseCliente.cliente.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.firebaseCliente.cliente.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto_cliente").files[0];
                if (archivo) {
                    var nombreArchivo = usuario.uid + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //CREACIÓN DE CLIENTE EN BD
                            refCliente.child(usuario.uid).child('cliente').child('imagenURL').set(url);
                            console.log(url)
                            $scope.firebaseCliente.cliente.imagenURL = url;
                            usuario.updateProfile({
                                photoURL: $scope.firebaseCliente.cliente.imagenURL
                            });
                        }).catch(function (error) {
                            console.log(error);
                        });
                    });
                }
                else {
                    console.log('Image not selected');
                }

                //CREACIÓN DE CLIENTE EN BD
                refCliente.child(usuario.uid).child('cliente').set($scope.firebaseCliente.cliente);
                refCliente.child(usuario.uid).child('cliente').child('contrasena').set(null);

                //CREACIÓN DE PERFIL
                usuario.updateProfile({
                    displayName: $scope.firebaseCliente.cliente.nombre
                }).then(function () {
                    //ENVÍO DE CORREO
                    usuario.sendEmailVerification().then(function () {
                        console.log('Email sent');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
                console.log('Client created.');

                //CREAR USUARIO EN DB
                refUsuarios.child(usuario.uid).child('tipoDeUsuario').set('cliente').then(function () {
                    console.log('User added in DB.');
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                auth.$signOut();
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';

                //ALERTA
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#registro')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent('<br/> <p>Muchas gracias por registrarte. </p> <p> Hemos enviado un mensaje a tu cuenta de correo electrónico. Por favor valida tu cuenta abriendo el enlace que compartimos.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path("/Inicio");
                });
            }).catch(function (error) {
                //ERROR
                if (error.code === 'auth/email-already-in-use') {
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseCliente.cliente.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/> <p>¿<b>' + $scope.firebaseCliente.cliente.correoElectronico + '</b> es tu cuenta de correo?<br/> Recupera tu contraseña <a href="#/RecuperarContrasena"><i>aquí</i><a/></p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
            });
        };

        //CARGA DE FOTO DE CLIENTE
        $scope.guardarFotoCliente = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_cliente').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_cliente').src = e.target.result;
                        document.getElementById('img_perfil_cliente').className = 'imagen-perfil';
                        document.getElementById('i_perfil_cliente').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };

        //TRANSPORTISTA
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //INICIALIZAR TRANSPORTISTA
        $scope.firebaseTransportista = {
            transportista: {
                nombre: "",
                representanteLegal: "",
                rfc: "",
                numeroInterior: "",
                numeroExterior: "",
                calle: "",
                colonia: "",
                ciudad: "",
                estado: "",
                codigoPostal: "",
                telefono: "",
                celular: "",
                proveedorGPS: "",
                correoElectronico: "",
                contrasena: "",
                estatus: "inactivo",
                fechaDeCreacion: unixTime(),
                fechaDeEdicion: unixTime(),
                imagenURL: "",
                tipoDeUsuario: "transportista",
                firebaseId: ""
            }
        };

        //GUARDAR TRANSPORTISTA
        var refTransportista = firebase.database().ref().child('transportistas');

        $scope.registrarTransportista = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var auth = $firebaseAuth();
            var usuario = auth.$getAuth();
            //SE CREA EL USUARIO
            auth.$createUserWithEmailAndPassword($scope.firebaseTransportista.transportista.correoElectronico, $scope.firebaseTransportista.transportista.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.firebaseTransportista.transportista.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto_transportista").files[0];
                if (archivo) {
                    var nombreArchivo = usuario.uid + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //ACTUALIZACIÓN DE IMAGEN EN BD
                            refTransportista.child(usuario.uid).child('transportista').child('imagenURL').set(url);
                            console.log(url);
                            $scope.firebaseTransportista.transportista.imagenURL = url;
                            usuario.updateProfile({
                                photoURL: $scope.firebaseTransportista.transportista.imagenURL
                            });
                        }).catch(function (error) {
                            console.log(error);
                        });
                    });
                }
                else {
                    console.log('Image not selected');
                }

                //CREACIÓN DE TRANSPORTISTA EN BD
                refTransportista.child(usuario.uid).child('transportista').set($scope.firebaseTransportista.transportista);
                refTransportista.child(usuario.uid).child('transportista').child('contrasena').set(null);

                //CREACIÓN DE PERFIL
                usuario.updateProfile({
                    displayName: $scope.firebaseTransportista.transportista.nombre,
                    photoURL: $scope.firebaseTransportista.transportista.imagenURL
                }).then(function () {
                    //ENVÍO DE CORREO
                    usuario.sendEmailVerification().then(function () {
                        console.log('Email sent');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
                console.log('Client created.');

                //CREAR USUARIO EN DB
                refUsuarios.child(usuario.uid).child('tipoDeUsuario').set('transportista').then(function () {
                    console.log('User added in DB.');
                });

                //CREACIÓN DE TRANSPORTISTA EN LISTADO DE TRANSPORTISTAS
                var refListadoTransportista = firebase.database().ref('listaDeTransportistas');
                refListadoTransportista.child(usuario.uid).set($scope.firebaseTransportista.transportista.nombre).then(function () {
                    console.log('Added in list.');
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                auth.$signOut();
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';

                //ALERTA
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#registro')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent('<br/> <p>Muchas gracias por registrarte. </p> <p> Hemos enviado un mensaje a tu cuenta de correo electrónico. Por favor valida tu cuenta abriendo el enlace que compartimos.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path("/Inicio");
                });
            }).catch(function (error) {
                //ERROR
                if (error.code === 'auth/email-already-in-use') {
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseTransportista.transportista.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/> <p>¿<b>' + $scope.firebaseTransportista.transportista.correoElectronico + '</b> es tu cuenta de correo?<br/> Recupera tu contraseña <a href="#/RecuperarContrasena"><i>aquí</i><a/></p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
            });
        };

        //CARGA DE FOTO DE TRANSPORTISTA
        $scope.guardarFotoTransportista = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_transportista').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_transportista').src = e.target.result;
                        document.getElementById('img_perfil_transportista').className = 'imagen-perfil';
                        document.getElementById('i_perfil_transportista').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };

        //CHOFER
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //INICIALIZAR CHOFER
        $scope.firebaseChofer = {
            nombre: "",
            firebaseIdDelTransportista: "",
            numeroDeLicencia: "",
            numeroDeSeguroSocial: "",
            curp: "",
            numeroInterior: "",
            numeroExterior: "",
            calle: "",
            colonia: "",
            ciudad: "",
            estado: "",
            codigoPostal: "",
            telefono: "",
            celular1: "",
            celular2: "",
            correoElectronico: "",
            contrasena: "",
            estatus: "inactivo",
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            imagenURL: "",
            tipoDeUsuario: "chofer",
            firebaseId: ""
        };

        //LISTADO TRANSPORTISTAS
        var refTransportistas = firebase.database().ref().child('listaDeTransportistas').orderByValue();
        $scope.empresasTransportista = $firebaseArray(refTransportistas);
        refTransportistas.on('value', function (snap) {
            $scope.empresasTransportista.$value = snap.key;
        });

        //GUARDAR CHOFER
        var refChofer = firebase.database().ref().child('choferes');

        $scope.registrarChofer = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var auth = $firebaseAuth();
            var usuario = auth.$getAuth();
            //SE CREA EL USUARIO
            auth.$createUserWithEmailAndPassword($scope.firebaseChofer.correoElectronico, $scope.firebaseChofer.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.firebaseChofer.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto_chofer").files[0];
                if (archivo) {
                    var nombreArchivo = usuario.uid + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //ACTUALIZACIÓN DE IMAGEN EN BD
                            refChofer.child(usuario.uid).child('imagenURL').set(url);
                            var refTransportista = firebase.database().ref('transportistas').child($scope.firebaseChofer.firebaseIdDelTransportista).child('choferes');
                            refTransportista.child(usuario.uid).child('imagenURL').set(url);
                            console.log(url);
                            $scope.firebaseChofer.imagenURL = url;
                            usuario.updateProfile({
                                photoURL: $scope.firebaseChofer.imagenURL
                            });
                        }).catch(function (error) {
                            console.log(error);
                        });
                    });
                }
                else {
                    console.log('Image not selected');
                }

                //CREACIÓN DE CHOFER EN BD
                refChofer.child(usuario.uid).set($scope.firebaseChofer);
                refChofer.child(usuario.uid).child('contrasena').set(null);

                //CREACIÓN DE PERFIL
                usuario.updateProfile({
                    displayName: $scope.firebaseChofer.nombre,
                    photoURL: $scope.firebaseChofer.imagenURL
                }).then(function () {
                    //ENVÍO DE CORREO
                    usuario.sendEmailVerification().then(function () {
                        console.log('Email sent');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
                console.log('Transportist created.');

                //CREAR USUARIO EN DB
                refUsuarios.child(usuario.uid).child('tipoDeUsuario').set('chofer').then(function () {
                    console.log('User added in DB.');
                });

                //CREAR CHOFER EN TRANSPORTISTA
                var refTransportista = firebase.database().ref('transportistas');
                refTransportista.on("value", function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var transportista = childSnapshot.val();
                        if (transportista.transportista.firebaseId === $scope.firebaseChofer.firebaseIdDelTransportista) {
                            var id = transportista.transportista.firebaseId;
                            var refTrans = firebase.database().ref('transportistas').child(id).child('choferes').child(usuario.uid);
                            refTrans.set($scope.firebaseChofer).then(function () {
                                console.log('Client added in Transportist');
                            });
                        }
                    });
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                auth.$signOut();
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';

                //ALERTA
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#registro')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent('<br/> <p>Muchas gracias por registrarte. </p> <p> Hemos enviado un mensaje a tu cuenta de correo electrónico. Por favor valida tu cuenta abriendo el enlace que compartimos.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path("/Inicio");
                });
            }).catch(function (error) {
                //ERROR
                if (error.code === 'auth/email-already-in-use') {
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseChofer.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/> <p>¿<b>' + $scope.firebaseChofer.correoElectronico + '</b> es tu cuenta de correo?<br/> Recupera tu contraseña <a href="#/RecuperarContrasena"><i>aquí</i><a/></p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
            });
        };

        //CARGA DE FOTO DE CHOFER
        $scope.guardarFotoChofer = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_chofer').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_chofer').src = e.target.result;
                        document.getElementById('img_perfil_chofer').className = 'imagen-perfil';
                        document.getElementById('i_perfil_chofer').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//RECUPERAR CONTRASEÑA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('recuperarContrasenaController', function ($scope, $location, $firebaseAuth, $mdDialog) {
        $scope.regresar = function () {
            $location.path("/Inicio");
        };

        //RECUPERACION DE CONTRASEÑA
        $scope.enviarContrasena = function () {
            var auth = $firebaseAuth();
            //RESET DE CONTRASEÑA
            auth.$sendPasswordResetEmail($scope.usuario.usuario).then(function () {
                //ALERTA
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#recuperarContrasena')))
                        .clickOutsideToClose(false)
                        .title('Correo electrónico enviado')
                        .htmlContent('<br/><p> Hemos enviado un mensaje a <b>' + $scope.usuario.usuario + '</b>. Por favor valida tu cuenta abriendo el enlace que compartimos.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path("/Inicio");
                });
                console.log('sent');
            }).catch(function (error) {
                //ERROR
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#recuperarContrasena')))
                        .clickOutsideToClose(false)
                        .title('Usuario no encontrado')
                        .htmlContent('<br/> <p>No existen registros del usuario correspondiente a <b>' + $scope.usuario.usuario + '</b>.</p> <p>El usuario pudo haber sido eliminado. Por favor, valida la dirección que ingresaste.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                );
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//MENU CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('menuController', function ($scope, $timeout, $mdSidenav, $firebaseAuth, $firebaseObject) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        $scope.displayName = usuario.displayName;

        $scope.toggleLeft = buildToggler('left');
        $scope.toggleRight = buildToggler('right');

        $scope.cerrarSesion = function () {
            auth.$signOut();
        }

        function buildToggler(componentId) {
            return function () {
                $mdSidenav(componentId).toggle();
            };
        }

        //ESTATUS MENU
        $scope.menu = {
            inicio: false,
            administracion: {
                clientes: false,
                transportistas: false,
                choferes: false,
                tractores: false,
                remolques: false,
                bodegas: false
            },
            fletes: {
                agenda: false
            },
            cuenta: {
                miPerfil: false,
                href: "",
                cerrarSesion: false
            }
        }

        //BUSQUEDA DE FOTO EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    $scope.menu.inicio = true;
                    $scope.menu.administracion.clientes = true;
                    $scope.menu.administracion.transportistas = true;
                    $scope.menu.administracion.choferes = true;
                    $scope.menu.administracion.tractores = true;
                    $scope.menu.administracion.remolques = true;
                    $scope.menu.administracion.bodegas = true;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = false;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                case 'cliente':
                    $scope.menu.cuenta.href = '#/CHAOL/Clientes/' + usuario.uid;
                    $scope.menu.inicio = true;
                    $scope.menu.administracion.bodegas = true;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = true;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                case 'transportista':
                    $scope.menu.cuenta.href = '#/CHAOL/Transportistas/' + usuario.uid;
                    $scope.menu.inicio = true;
                    $scope.menu.administracion.choferes = true;
                    $scope.menu.administracion.tractores = true;
                    $scope.menu.administracion.remolques = true;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = true;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                case 'chofer':
                    $scope.menu.cuenta.href = '#/CHAOL/Choferes/' + usuario.uid;
                    $scope.menu.inicio = true;
                    $scope.menu.administracion = false;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = true;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                default:
                    break;
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//ENCABEZADO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('encabezadoController', function ($scope, $firebaseAuth, $firebaseObject, $location) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        $scope.displayName = usuario.displayName;

        //BUSQUEDA DE FOTO EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refObjeto;
            var objeto;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    break;
                case 'cliente':
                    refObjeto = firebase.database().ref('clientes').child(firebaseUsuario.$id).child('cliente');
                    objeto = $firebaseObject(refObjeto);
                    $scope.redirect = '#/CHAOL/Clientes/' + usuario.uid;
                    break;
                case 'transportista':
                    refObjeto = firebase.database().ref('transportistas').child(firebaseUsuario.$id).child('transportista');
                    objeto = $firebaseObject(refObjeto);
                    $scope.redirect = '#/CHAOL/Transportistas/' + usuario.uid;
                    break;
                case 'chofer':
                    refObjeto = firebase.database().ref('choferes').child(firebaseUsuario.$id);
                    objeto = $firebaseObject(refObjeto);
                    $scope.redirect = '#/CHAOL/Choferes/' + usuario.uid;
                    break;
                default:
            }
            if (objeto) {
                objeto.$loaded().then(function () {
                    if (usuario.photoURL) {
                        $scope.src = usuario.photoURL;
                    }
                    else {
                        $scope.src = objeto.imagenURL;
                    }
                });
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//INICIO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('inicioController', function ($scope, $mdSidenav, $firebaseAuth, $firebaseObject, $firebaseArray) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ESTATUS PANELES
        $scope.paneles = {
            clientes: {
                autorizados: "0",
                noAutorizados: "0"
            },
            transportistas: {
                autorizados: "0",
                noAutorizados: "0"
            },
            choferes: {
                libres: "0",
                asignados: "0",
                noAutorizados: "0"
            },
            tractores: {
                asignados: "0",
                libres: "0"
            },
            remolques: {
                asignados: "0",
                libres: "0"
            },
            fletes: {
                porCotizar: "0",
                esperandoPorTransportista: "0",
                transportistaPorConfirmar: "0",
                unidadesPorAsignar: "0",
                envioPorIniciar: "0",
                enProgreso: "0",
                entregado: "0",
                finalizado: "0",
                cancelado: "0"
            },
            bodegas: {
                registrados: "0"
            }
        }

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            arrayFletes.push(fletes.flete);
                        });
                        buscarFletes(arrayFletes);
                    });

                    //CLIENTES
                    var refClientes = firebase.database().ref().child('clientes');
                    refClientes.on("value", function (snapshot) {
                        var arrayClientes = [];
                        snapshot.forEach(function (childSnapshot) {
                            clientes = childSnapshot.val();
                            arrayClientes.push(clientes.cliente);
                        });
                        buscarClientes(arrayClientes);
                    });

                    //TRANSPORTISTAS
                    var refTransportistas = firebase.database().ref().child('transportistas');
                    refTransportistas.on("value", function (snapshot) {
                        var arrayTransportistas = [];
                        snapshot.forEach(function (childSnapshot) {
                            transportistas = childSnapshot.val();
                            arrayTransportistas.push(transportistas.transportista);
                        });
                        buscarTransportistas(arrayTransportistas);
                    });

                    //CHOFERES
                    var refChoferes = firebase.database().ref().child('choferes');
                    refChoferes.on("value", function (snapshot) {
                        var arrayChoferes = [];
                        snapshot.forEach(function (childSnapshot) {
                            choferes = childSnapshot.val();
                            arrayChoferes.push(choferes);
                        });
                        buscarChoferes(arrayChoferes);
                    });

                    //TRACTORES
                    var refTractores = firebase.database().ref().child('transportistas');
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (tractorSnapshot) {
                                if (tractorSnapshot.key === "tractores") {
                                    tractorSnapshot.forEach(function (tractor) {
                                        arrayTractores.push(tractor.val());
                                    });
                                }
                            })
                        });
                        buscarTractores(arrayTractores);
                    });

                    //REMOLQUES
                    var refRemolques = firebase.database().ref().child('transportistas');
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (remolqueSnapshot) {
                                if (remolqueSnapshot.key === "remolques") {
                                    remolqueSnapshot.forEach(function (remolque) {
                                        arrayRemolques.push(remolque.val());
                                    });
                                }
                            })
                        });
                        buscarRemolques(arrayRemolques);
                    });

                    //BODEGAS
                    var refBodegas = firebase.database().ref().child('clientes');
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (bodegaSnapshot) {
                                if (bodegaSnapshot.key === "bodegas") {
                                    bodegaSnapshot.forEach(function (bodega) {
                                        arrayBodegas.push(bodega.val());
                                    });
                                }
                            })
                        });
                        buscarBodegas(arrayBodegas);
                    });
                    break;
                case 'cliente':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.bodegaDeCarga.firebaseIdDelCliente === usuario.uid) {
                                arrayFletes.push(fletes.flete);
                            }
                        });
                        buscarFletes(arrayFletes);
                    });

                    //CLIENTES
                    $scope.paneles.clientes = false;

                    //TRANSPORTISTAS
                    $scope.paneles.transportistas = false;

                    //CHOFERES
                    $scope.paneles.choferes = false;

                    //TRACTORES
                    $scope.paneles.tractores = false;

                    //REMOLQUES
                    $scope.paneles.remolques = false;

                    //BODEGAS
                    var refBodegas = firebase.database().ref().child('clientes').child(usuario.uid);
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "bodegas") {
                                childSnapshot.forEach(function (bodega) {
                                    arrayBodegas.push(bodega.val());
                                });
                            }
                        });
                        buscarBodegas(arrayBodegas);
                    });
                    break;
                case 'transportista':
                    //FLETES DISPONIBLES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.flete.estatus === "esperandoPorTransportista") {
                                arrayFletes.push(fletes.flete);
                            }
                        });
                        buscarEsperandoPorTransportista(arrayFletes);
                    });

                    //FLETES INTERESADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.transportistasInteresados !== undefined) {
                                if (fletes.transportistasInteresados.key === usuario.uid && fletes.flete.estatus === "transportistaPorConfirmar") {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarTransportistaPorConfirmar(arrayFletes);
                    });

                    //FLETES ASIGNADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.transportistaSeleccionado !== undefined) {
                                if (fletes.transportistaSeleccionado.key === usuario.uid) {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarUnidadesPorAsignar(arrayFletes);
                        buscarEnvioPorIniciar(arrayFletes);
                        buscarEnProgreso(arrayFletes);
                    });

                    //FLETES INHABILITADOS
                    $scope.paneles.fletes.porCotizar = false;
                    $scope.paneles.fletes.entregado = false;
                    $scope.paneles.fletes.finalizado = false;
                    $scope.paneles.fletes.cancelado = false;

                    //CLIENTES
                    $scope.paneles.clientes = false;

                    //TRANSPORTISTAS
                    $scope.paneles.transportistas = false;

                    //CHOFERES
                    var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child("choferes");
                    refChoferes.on("value", function (snapshot) {
                        var arrayChoferes = [];
                        snapshot.forEach(function (childSnapshot) {
                            choferes = childSnapshot.val();
                            arrayChoferes.push(choferes);
                        });
                        buscarChoferes(arrayChoferes);
                    });

                    //TRACTORES
                    var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "tractores") {
                                childSnapshot.forEach(function (tractorSnapshot) {
                                    tractor = tractorSnapshot.val();
                                    if (tractor.estatus !== 'eliminado') {
                                        arrayTractores.push(tractor);
                                    }
                                })
                            }
                        });
                        buscarTractores(arrayTractores);
                    });

                    //REMOLQUES
                    var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "remolques") {
                                childSnapshot.forEach(function (remolqueSnapshot) {
                                    remolque = remolqueSnapshot.val();
                                    if (remolque.estatus !== 'eliminado') {
                                        arrayRemolques.push(remolque);
                                    }
                                })
                            }
                        });
                        buscarRemolques(arrayRemolques);
                    });

                    //BODEGAS
                    $scope.paneles.bodegas = false;
                    break;
                case 'chofer':
                    //FLETES ASIGNADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.choferSeleccionado !== undefined) {
                                if (fletes.choferSeleccionado.key === usuario.uid && (fletes.flete.estatus === "envioPorIniciar" || fletes.flete.estatus === "enProgreso")) {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarEnvioPorIniciar(arrayFletes);
                        buscarEnProgreso(arrayFletes);
                    });

                    //FLETES INHABILITADOS
                    $scope.paneles.fletes.porCotizar = false;
                    $scope.paneles.fletes.esperandoPorTransportista = false;
                    $scope.paneles.fletes.transportistaPorConfirmar = false;
                    $scope.paneles.fletes.unidadesPorAsignar = false;
                    $scope.paneles.fletes.entregado = false;
                    $scope.paneles.fletes.finalizado = false;
                    $scope.paneles.fletes.cancelado = false;

                    //CLIENTES
                    $scope.paneles.clientes = false;

                    //TRANSPORTISTAS
                    $scope.paneles.transportistas = false;

                    //CHOFERES
                    $scope.paneles.choferes = false;

                    //TRACTORES
                    $scope.paneles.tractores = false;

                    //REMOLQUES
                    $scope.paneles.remolques = false;

                    //BODEGAS
                    $scope.paneles.bodegas = false;
                    break;
                default:
            }
        });

        //BUSQUEDA DE FLETES
        var buscarFletesPorCotizar = function (arreglo) {
            var fletesPorCotizar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'fletePorCotizar':
                        fletesPorCotizar++;
                        break;
                }
            });
            $scope.paneles.fletes.porCotizar = fletesPorCotizar.toString();
        }

        var buscarEsperandoPorTransportista = function (arreglo) {
            var esperandoPorTransportista = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'esperandoPorTransportista':
                        esperandoPorTransportista++;
                        break;
                }
            });
            $scope.paneles.fletes.esperandoPorTransportista = esperandoPorTransportista.toString();
        }

        var buscarTransportistaPorConfirmar = function (arreglo) {
            var transportistaPorConfirmar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'transportistaPorConfirmar':
                        transportistaPorConfirmar++;
                        break;
                }
            });
            $scope.paneles.fletes.transportistaPorConfirmar = transportistaPorConfirmar.toString();
        }

        var buscarUnidadesPorAsignar = function (arreglo) {
            var unidadesPorAsignar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'unidadesPorAsignar':
                        unidadesPorAsignar++;
                        break;
                }
            });
            $scope.paneles.fletes.unidadesPorAsignar = unidadesPorAsignar.toString();
        }

        var buscarEnProgreso = function (arreglo) {
            var enProgreso = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'enProgreso':
                        enProgreso++;
                        break;
                }
            });
            $scope.paneles.fletes.enProgreso = enProgreso.toString();
        }

        var buscarEnvioPorIniciar = function (arreglo) {
            var envioPorIniciar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'envioPorIniciar':
                        envioPorIniciar++;
                        break;
                }
            });
            $scope.paneles.fletes.envioPorIniciar = envioPorIniciar.toString();
        }

        var buscarEntregado = function (arreglo) {
            var entregado = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'entregado':
                        entregado++;
                        break;
                }
            });
            $scope.paneles.fletes.entregado = entregado.toString();
        }

        var buscarFinalizado = function (arreglo) {
            var finalizado = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'finalizado':
                        finalizado++;
                        break;
                }
            });
            $scope.paneles.fletes.finalizado = finalizado.toString();
        }

        var buscarCancelado = function (arreglo) {
            var cancelado = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'cancelado':
                        cancelado++;
                        break;
                }
            });
            $scope.paneles.fletes.cancelado = cancelado.toString();
        }

        var buscarFletes = function (arreglo) {
            buscarFletesPorCotizar(arreglo);
            buscarEsperandoPorTransportista(arreglo);
            buscarTransportistaPorConfirmar(arreglo);
            buscarUnidadesPorAsignar(arreglo);
            buscarEnvioPorIniciar(arreglo);
            buscarEnProgreso(arreglo);
            buscarEntregado(arreglo);
            buscarFinalizado(arreglo);
            buscarCancelado(arreglo);
        };

        //BUSQUEDA DE CLIENTES
        var buscarClientes = function (arreglo) {
            var autorizados = 0;
            var noAutorizados = 0;

            arreglo.forEach(function (cliente) {
                switch (cliente.estatus) {
                    case "activo":
                        autorizados++;
                        break;
                    case "inactivo":
                        noAutorizados++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.clientes.autorizados = autorizados.toString();
            $scope.paneles.clientes.noAutorizados = noAutorizados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE TRANSPORTISTAS
        var buscarTransportistas = function (arreglo) {
            var autorizados = 0;
            var noAutorizados = 0;

            arreglo.forEach(function (transportista) {
                switch (transportista.estatus) {
                    case "activo":
                        autorizados++;
                        break;
                    case "inactivo":
                        noAutorizados++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.transportistas.autorizados = autorizados.toString();
            $scope.paneles.transportistas.noAutorizados = noAutorizados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE CHOFERES
        var buscarChoferes = function (arreglo) {
            var libres = 0;
            var asignados = 0;
            var noAutorizados = 0;

            arreglo.forEach(function (chofer) {
                switch (chofer.estatus) {
                    case "libre":
                        libres++;
                        break;
                    case "asignado":
                        asignados++;
                        break;
                    case "inactivo":
                        noAutorizados++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.choferes.libres = libres.toString();
            $scope.paneles.choferes.asignados = asignados.toString();
            $scope.paneles.choferes.noAutorizados = noAutorizados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE TRACTORES
        var buscarTractores = function (arreglo) {
            var libres = 0;
            var asignados = 0;

            arreglo.forEach(function (tractor) {
                switch (tractor.estatus) {
                    case "libre":
                        libres++;
                        break;
                    case "asignado":
                        asignados++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.tractores.libres = libres.toString();
            $scope.paneles.tractores.asignados = asignados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE REMOLQUES
        var buscarRemolques = function (arreglo) {
            var libres = 0;
            var asignados = 0;

            arreglo.forEach(function (remolque) {
                switch (remolque.estatus) {
                    case "libre":
                        libres++;
                        break;
                    case "asignado":
                        asignados++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.remolques.libres = libres.toString();
            $scope.paneles.remolques.asignados = asignados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE BODEGAS
        var buscarBodegas = function (arreglo) {
            var activos = 0;

            arreglo.forEach(function (bodega) {
                switch (bodega.estatus) {
                    case "activo":
                        activos++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.bodegas.registrados = activos.toString();
            $scope.$apply();
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//LISTADO CLIENTES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('clientesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //CLIENTES
        var refClientes = firebase.database().ref().child('clientes');
        refClientes.on("value", function (snapshot) {
            var arrayClientes = [];
            snapshot.forEach(function (childSnapshot) {
                clientes = childSnapshot.val();
                if (clientes.cliente.estatus !== 'eliminado') {
                    arrayClientes.push(clientes);
                }
            });
            $scope.clientes = arrayClientes;
        });

        $scope.cambiarEstatus = function (cliente) {
            //NODO CLIENTES
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            //CARGA DEL CLIENTE
            firebaseCliente.$loaded().then(function () {
                firebaseCliente.estatus = cliente.estatus;
                firebaseCliente.$save().then(function () {
                    console.log('Estatus changed to ' + cliente.estatus);
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (cliente) {
            //NODO CLIENTES
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            //CARGA DEL CLIENTE
            firebaseCliente.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#clientes')))
                    .title('¿Eliminar cliente?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseCliente.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseCliente.estatus = "eliminado";
                    firebaseCliente.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//REGISTRO CLIENTE CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('clienteController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        var refUsuarios = firebase.database().ref().child('usuarios');
        var refCliente = firebase.database().ref().child('clientes');

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = function () {
            return $firebaseAuth();
        }
        var usuario = function () {
            return auth().$getAuth();
        }
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //CLIENTE
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //INICIALIZAR CLIENTE
        $scope.firebaseCliente = {
            cliente: {
                nombre: "",
                rfc: "",
                numeroInterior: "",
                numeroExterior: "",
                calle: "",
                colonia: "",
                ciudad: "",
                estado: "",
                codigoPostal: "",
                metodoDePago: "",
                telefono: "",
                celular: "",
                correoElectronico: "",
                contrasena: "",
                estatus: "inactivo",
                fechaDeCreacion: unixTime(),
                fechaDeEdicion: unixTime(),
                imagenURL: "",
                tipoDeUsuario: "cliente",
                firebaseId: ""
            }
        };

        //LISTADO MÉTODOS DE PAGO
        $scope.metodosDePago =
            [
                "Efectivo",
                "Cheque",
                "Transferencia Electrónica",
                "Tarjeta de Crédito",
                "Dinero Electróncio",
                "Tarjeta de Débito"
            ];

        //CONSULTAR CLIENTE
        if ($routeParams.ID) {
            var cliente = $firebaseObject(refCliente.child($routeParams.ID).child('cliente'));
            cliente.$loaded().then(function () {
                $scope.firebaseCliente.cliente = cliente;
                console.log('Client found');
                if (usuario().uid === $routeParams.ID) {
                    $scope.correoElectronicoAnterior = $scope.firebaseCliente.cliente.correoElectronico;
                    $scope.ownUser = true;
                }
            }).catch(function (error) {
                console.log(error);
            });
        }
        else {
            $scope.ownUser = true;
        }

        //GUARDAR CLIENTE
        $scope.registrarCliente = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var usuarioCliente;

            //CREAR USUARIO EN BASE DE DATOS
            var crearUsuarioBD = function (cliente) {
                return refUsuarios.child(cliente.firebaseId).child('tipoDeUsuario').set('cliente');
            }

            //ACTUALIZACIÓN DE CLIENTE EN BD
            var actualizarClienteBD = function (cliente) {
                var objetoCliente = $firebaseObject(refCliente.child(cliente.firebaseId).child('cliente'));
                objetoCliente.nombre = cliente.nombre;
                objetoCliente.rfc = cliente.rfc;
                objetoCliente.numeroInterior = cliente.numeroInterior;
                objetoCliente.numeroExterior = cliente.numeroExterior;
                objetoCliente.calle = cliente.calle;
                objetoCliente.colonia = cliente.colonia;
                objetoCliente.ciudad = cliente.ciudad;
                objetoCliente.estado = cliente.estado;
                objetoCliente.codigoPostal = cliente.codigoPostal;
                objetoCliente.metodoDePago = cliente.metodoDePago;
                objetoCliente.telefono = cliente.telefono;
                objetoCliente.celular = cliente.celular;
                objetoCliente.correoElectronico = cliente.correoElectronico;
                objetoCliente.fechaDeCreacion = cliente.fechaDeCreacion;
                objetoCliente.fechaDeEdicion = unixTime();
                objetoCliente.imagenURL = cliente.imagenURL;
                objetoCliente.firebaseId = cliente.firebaseId;
                objetoCliente.tipoDeUsuario = "cliente";
                objetoCliente.estatus = cliente.estatus;

                objetoCliente.$save().then(function () {
                    console.log('Client updated');
                })
            }

            //CREACIÓN DE CLIENTE EN BD
            var crearClienteBD = function (cliente) {
                refCliente.child(cliente.firebaseId).child('cliente').set(cliente);
                refCliente.child(cliente.firebaseId).child('cliente').child('contrasena').set(null);
                console.log('Client created in DB');
            }

            //ENVIAR CORREO
            var enviarCorreoConfirmacion = function (usuarioCliente) {
                return usuarioCliente.sendEmailVerification();
            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#registro')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CARGA IMAGEN DE PERFIL
            var cargarImagen = function (cliente) {
                var archivo = document.getElementById("input_foto_cliente").files[0];
                if (archivo) {
                    var nombreArchivo = cliente.firebaseId + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //CREACIÓN DE CLIENTE EN BD
                            console.log('Client image loaded to ' + url)
                            cliente.imagenURL = url;
                            actualizarClienteBD(cliente);
                            actualizarPerfil(usuarioCliente, cliente);
                        }).catch(function (error) {
                            console.log(error);
                        });
                    });
                }
                else {
                    console.log('Image not selected');
                }
            }

            //ACTUALIZACIÓN DE PERFIL
            var actualizarPerfil = function (usuarioCliente, cliente) {
                usuarioCliente.updateProfile({
                    displayName: cliente.nombre,
                    photoURL: cliente.imagenURL
                })
                console.log('Profile updated');
            }

            //CREACIÓN DEL CLIENTE
            if (!$scope.firebaseCliente.cliente.firebaseId) {
                //SE CREA EL USUARIO
                createUserService.auth().createUserWithEmailAndPassword($scope.firebaseCliente.cliente.correoElectronico, $scope.firebaseCliente.cliente.contrasena).then(function (user) {
                    console.log('User created.');
                    usuarioCliente = user;
                    $scope.firebaseCliente.cliente.firebaseId = user.uid;

                    crearUsuarioBD($scope.firebaseCliente.cliente).then(function () {
                        console.log('User created in DB');
                        crearClienteBD($scope.firebaseCliente.cliente);
                        enviarCorreoConfirmacion(usuarioCliente).then(function () {
                            console.log('Email sent');
                        }).catch(function (error) {
                            console.log(error);
                        });
                        cargarImagen($scope.firebaseCliente.cliente);
                        actualizarPerfil(usuarioCliente, $scope.firebaseCliente.cliente);
                        alerta('<br/> <p>Cliente registrado. </p> <p> Hemos enviado un mensaje a su cuenta de correo electrónico. Es necesario que la cuenta sea validada desde el enlace que compartimos.</p>', '/CHAOL/Clientes');
                    }).catch(function (error) {
                        console.log(error);
                    });
                    //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                }).catch(function (error) {
                    //ERROR
                    if (error.code === 'auth/email-already-in-use') {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#registro')))
                                .clickOutsideToClose(false)
                                .title('Oops! Algo salió mal')
                                .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseCliente.cliente.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/>')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar')
                        );
                    }
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                });
            }
            //ACTUALIZACIÓN DEL CLIENTE
            else {
                //VALIDAR SI SE TRATA DEL USUARIO CORRECTO
                if (usuario().uid === $scope.firebaseCliente.cliente.firebaseId) {
                    $mdDialog.show(
                        $mdDialog.prompt()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Confirmación de credenciales')
                            .htmlContent('<br/><p>Para confirmar tu identidad, ingresa la contraseña que utilizaste para <b>' + usuario().email + '</b>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                            .cancel('Cancelar')
                    ).then(function (result) {
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                        //OBTENER CREDENCIALES
                        const credential = firebase.auth.EmailAuthProvider.credential(usuario().email, result);
                        const user = firebase.auth().currentUser;
                        usuarioCliente = user;
                        user.reauthenticateWithCredential(credential).then(function () {
                            //ACTUALIZAR EMAIL
                            auth().$updateEmail($scope.firebaseCliente.cliente.correoElectronico).then(function () {
                                console.log('Email updated');
                                //ACTUALIZAR CONTRASEÑA
                                auth().$updatePassword($scope.firebaseCliente.cliente.contrasena).then(function () {
                                    actualizarClienteBD($scope.firebaseCliente.cliente);
                                    cargarImagen($scope.firebaseCliente.cliente);
                                    auth().$signInWithEmailAndPassword($scope.firebaseCliente.cliente.correoElectronico, result).then(function () {
                                        console.log('Login succesful');
                                        alerta('<br/> <p>Cuenta actualizada. </p> <p> Hemos actualizado los datos de tu cuenta exitosamente.</p>', '/CHAOL');
                                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                    }).catch(function (error) {
                                        console.log(error)
                                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                    })
                                }).catch(function (error) {
                                    console.log(error);
                                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                });
                            }).catch(function (error) {
                                console.log(error);
                                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            })
                        }).catch(function (error) {
                            console.log(error);
                            if (error.code === 'auth/wrong-password') {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#registro')))
                                        .clickOutsideToClose(false)
                                        .title('Oops! Algo salió mal')
                                        .htmlContent('<br/> <p>Al parecer esta no es tu contraseña. Por favor, intenta de nuevo. </p>')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar')
                                );
                            }
                            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                        });
                    });
                }
                else {
                    actualizarClienteBD($scope.firebaseCliente.cliente);
                    cargarImagen($scope.firebaseCliente.cliente);
                    alerta('<br/> <p>Cliente actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseCliente.cliente.nombre + '</b> exitosamente.</p>', '/CHAOL/Clientes');
                }
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };

        //CARGA DE FOTO DE CLIENTE
        $scope.guardarFotoCliente = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_cliente').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_cliente').src = e.target.result;
                        document.getElementById('img_perfil_cliente').className = 'imagen-perfil';
                        document.getElementById('i_perfil_cliente').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//LISTADO TRANSPORTISTAS CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('transportistasController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        var refTransportistas = firebase.database().ref().child('transportistas');
        refTransportistas.on("value", function (snapshot) {
            var arrayTransportistas = [];
            snapshot.forEach(function (childSnapshot) {
                transportistas = childSnapshot.val();
                if (transportistas.transportista.estatus !== 'eliminado') {
                    arrayTransportistas.push(transportistas);
                }
            });
            $scope.transportistas = arrayTransportistas;
        });

        $scope.cambiarEstatus = function (transportista) {
            //NODO transportistas
            var refTransportista = firebase.database().ref('transportistas').child(transportista.firebaseId).child('transportista');
            var firebaseTransportista = $firebaseObject(refTransportista);
            var refListaTransportista = firebase.database().ref('listaDeTransportistas').child(transportista.firebaseId);
            var firebaseListaTransportista = $firebaseObject(refListaTransportista);
            //CARGA DEL transportista
            firebaseTransportista.$loaded().then(function () {
                firebaseTransportista.estatus = transportista.estatus;
                firebaseTransportista.$save().then(function () {
                    console.log('Estatus changed to ' + transportista.estatus);
                    if (firebaseTransportista.estatus == 'activo') {
                        firebase.database().ref('listaDeTransportistas').child(transportista.firebaseId).set(transportista.nombre);
                    }
                    else {
                        firebaseListaTransportista.$remove().then(function () {
                            console.log('Transportist removed form list')
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (transportista) {
            //NODO transportistas
            var refTransportista = firebase.database().ref('transportistas').child(transportista.firebaseId).child('transportista');
            var firebaseTransportista = $firebaseObject(refTransportista);
            var refListaTransportista = firebase.database().ref('listaDeTransportistas').child(transportista.firebaseId);
            var firebaseListaTransportista = $firebaseObject(refListaTransportista);
            //CARGA DEL transportista
            firebaseTransportista.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#transportistas')))
                    .title('¿Eliminar transportista?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseTransportista.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL transportista
                    firebaseTransportista.estatus = "eliminado";
                    firebaseTransportista.$save().then(function () {
                        console.log('Estatus changed to deleted');
                        firebaseListaTransportista.$remove().then(function () {
                            console.log('Transportist removed form list')
                        }).catch(function (error) {
                            console.log(error);
                        });

                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//REGISTRO TRANSPORTISTA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('transportistaController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        var refUsuarios = firebase.database().ref().child('usuarios');
        var refTransportista = firebase.database().ref().child('transportistas');
        var refListaTransportista = firebase.database().ref().child('listaDeTransportistas');

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = function () {
            return $firebaseAuth();
        }
        var usuario = function () {
            return auth().$getAuth();
        }
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //TRANSPORTISTA
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //INICIALIZAR TRANSPORTISTA
        $scope.firebaseTransportista = {
            transportista: {
                nombre: "",
                representanteLegal: "",
                rfc: "",
                numeroInterior: "",
                numeroExterior: "",
                calle: "",
                colonia: "",
                ciudad: "",
                estado: "",
                codigoPostal: "",
                telefono: "",
                celular: "",
                proveedorGPS: "",
                correoElectronico: "",
                contrasena: "",
                estatus: "inactivo",
                fechaDeCreacion: unixTime(),
                fechaDeEdicion: unixTime(),
                imagenURL: "",
                tipoDeUsuario: "transportista",
                firebaseId: ""
            }
        };

        //CONSULTAR TRANSPORTISTA
        if ($routeParams.ID) {
            var transportista = $firebaseObject(refTransportista.child($routeParams.ID).child('transportista'));
            transportista.$loaded().then(function () {
                $scope.firebaseTransportista.transportista = transportista;
                console.log('Transportist found');
                if (usuario().uid === $routeParams.ID) {
                    $scope.correoElectronicoAnterior = $scope.firebaseTransportista.transportista.correoElectronico;
                    $scope.ownUser = true;
                }
            }).catch(function (error) {
                console.log(error);
            });
        }
        else {
            $scope.ownUser = true;
        }

        //GUARDAR TRANSPORTISTA
        $scope.registrarTransportista = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var usuarioTransportista;

            //CREAR USUARIO EN BASE DE DATOS
            var crearUsuarioBD = function (transportista) {
                return refUsuarios.child(transportista.firebaseId).child('tipoDeUsuario').set('transportista');
            }

            //ACTUALIZACIÓN DE TRANSPORTISTA EN BD
            var actualizarTransportistaBD = function (transportista) {
                var objetoTransportista = $firebaseObject(refTransportista.child(transportista.firebaseId).child('transportista'));
                objetoTransportista.nombre = transportista.nombre;
                objetoTransportista.representanteLegal = transportista.representanteLegal;
                objetoTransportista.rfc = transportista.rfc;
                objetoTransportista.numeroInterior = transportista.numeroInterior;
                objetoTransportista.numeroExterior = transportista.numeroExterior;
                objetoTransportista.calle = transportista.calle;
                objetoTransportista.colonia = transportista.colonia;
                objetoTransportista.ciudad = transportista.ciudad;
                objetoTransportista.estado = transportista.estado;
                objetoTransportista.codigoPostal = transportista.codigoPostal;
                objetoTransportista.telefono = transportista.telefono;
                objetoTransportista.celular = transportista.celular;
                objetoTransportista.proveedorGPS = transportista.proveedorGPS;
                objetoTransportista.correoElectronico = transportista.correoElectronico;
                objetoTransportista.fechaDeCreacion = transportista.fechaDeCreacion;
                objetoTransportista.fechaDeEdicion = unixTime();
                objetoTransportista.imagenURL = transportista.imagenURL;
                objetoTransportista.firebaseId = transportista.firebaseId;
                objetoTransportista.tipoDeUsuario = "transportista";
                objetoTransportista.estatus = transportista.estatus;

                objetoTransportista.$save().then(function () {
                    console.log('Transportist updated');
                })

                refListaTransportista.child(transportista.firebaseId).set(objetoTransportista.nombre);
            }

            //CREACIÓN DE TRANSPORTISTA EN BD
            var crearTransportistaBD = function (transportista) {
                refTransportista.child(transportista.firebaseId).child('transportista').set(transportista);
                refTransportista.child(transportista.firebaseId).child('transportista').child('contrasena').set(null);
                console.log('Transportist created in DB');
            }

            //CREACIÓN DE TRANSPORTISTA EN LISTA DE TRABSPORTISTA BD
            var crearTransportistaEnLista = function (transportista) {
                refListaTransportista.child(transportista.firebaseId).set(transportista.nombre);
                console.log('Transportist created in List');
            }

            //ENVIAR CORREO
            var enviarCorreoConfirmacion = function (usuarioTransportista) {
                return usuarioTransportista.sendEmailVerification();
            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#registro')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CARGA IMAGEN DE PERFIL
            var cargarImagen = function (transportista) {
                var archivo = document.getElementById("input_foto_transportista").files[0];
                if (archivo) {
                    var nombreArchivo = transportista.firebaseId + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //CREACIÓN DE TRANSPORTISTA EN BD
                            console.log('Transportist image loaded to ' + url)
                            transportista.imagenURL = url;
                            actualizarTransportistaBD(transportista);
                            actualizarPerfil(usuarioTransportista, transportista);
                        }).catch(function (error) {
                            console.log(error);
                        });
                    });
                }
                else {
                    console.log('Image not selected');
                }
            }

            //ACTUALIZACIÓN DE PERFIL
            var actualizarPerfil = function (usuarioTransportista, transportista) {
                usuarioTransportista.updateProfile({
                    displayName: transportista.nombre,
                    photoURL: transportista.imagenURL
                })
                console.log('Profile updated');
            }

            //CREACIÓN DEL TRANSPORTISTA
            if (!$scope.firebaseTransportista.transportista.firebaseId) {
                //SE CREA EL USUARIO
                createUserService.auth().createUserWithEmailAndPassword($scope.firebaseTransportista.transportista.correoElectronico, $scope.firebaseTransportista.transportista.contrasena).then(function (user) {
                    console.log('User created.');
                    usuarioTransportista = user;
                    $scope.firebaseTransportista.transportista.firebaseId = user.uid;

                    crearUsuarioBD($scope.firebaseTransportista.transportista).then(function () {
                        console.log('User created in DB');
                        actualizarTransportistaBD($scope.firebaseTransportista.transportista);
                        crearTransportistaEnLista($scope.firebaseTransportista.transportista);
                        enviarCorreoConfirmacion(usuarioTransportista).then(function () {
                            console.log('Email sent');
                        }).catch(function (error) {
                            console.log(error);
                        });
                        cargarImagen($scope.firebaseTransportista.transportista);
                        actualizarPerfil(usuarioTransportista, $scope.firebaseTransportista.transportista);
                        alerta('<br/> <p>Transportista registrado. </p> <p> Hemos enviado un mensaje a su cuenta de correo electrónico. Es necesario que la cuenta sea validada desde el enlace que compartimos.</p>', '/CHAOL/Transportistas');
                    }).catch(function (error) {
                        console.log(error);
                    });
                    //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                }).catch(function (error) {
                    //ERROR
                    if (error.code === 'auth/email-already-in-use') {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#registro')))
                                .clickOutsideToClose(false)
                                .title('Oops! Algo salió mal')
                                .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseTransportista.transportista.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/>')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar')
                        );
                    }
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                });
            }
            //ACTUALIZACIÓN DEL TRANSPORTISTA
            else {
                //VALIDAR SI SE TRATA DEL USUARIO CORRECTO
                if (usuario().uid === $scope.firebaseTransportista.transportista.firebaseId) {
                    $mdDialog.show(
                        $mdDialog.prompt()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Confirmación de credenciales')
                            .htmlContent('<br/><p>Para confirmar tu identidad, ingresa la contraseña que utilizaste para <b>' + usuario().email + '</b>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                            .cancel('Cancelar')
                    ).then(function (result) {
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                        //OBTENER CREDENCIALES
                        const credential = firebase.auth.EmailAuthProvider.credential(usuario().email, result);
                        const user = firebase.auth().currentUser;
                        usuarioTransportista = user;
                        user.reauthenticateWithCredential(credential).then(function () {
                            //ACTUALIZAR EMAIL
                            auth().$updateEmail($scope.firebaseTransportista.transportista.correoElectronico).then(function () {
                                console.log('Email updated');
                                //ACTUALIZAR CONTRASEÑA
                                auth().$updatePassword($scope.firebaseTransportista.transportista.contrasena).then(function () {
                                    actualizarTransportistaBD($scope.firebaseTransportista.transportista);
                                    cargarImagen($scope.firebaseTransportista.transportista);
                                    auth().$signInWithEmailAndPassword($scope.firebaseTransportista.transportista.correoElectronico, result).then(function () {
                                        console.log('Login succesful');
                                        alerta('<br/> <p>Cuenta actualizada. </p> <p> Hemos actualizado los datos de tu cuenta exitosamente.</p>', '/CHAOL');
                                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                    }).catch(function (error) {
                                        console.log(error)
                                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                    })
                                }).catch(function (error) {
                                    console.log(error);
                                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                });
                            }).catch(function (error) {
                                console.log(error);
                                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            })
                        }).catch(function (error) {
                            console.log(error);
                            if (error.code === 'auth/wrong-password') {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#registro')))
                                        .clickOutsideToClose(false)
                                        .title('Oops! Algo salió mal')
                                        .htmlContent('<br/> <p>Al parecer esta no es tu contraseña. Por favor, intenta de nuevo. </p>')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar')
                                );
                            }
                            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                        });
                    });
                }
                else {
                    actualizarTransportistaBD($scope.firebaseTransportista.transportista);
                    cargarImagen($scope.firebaseTransportista.transportista);
                    alerta('<br/> <p>Transportista actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseTransportista.transportista.nombre + '</b> exitosamente.</p>', '/CHAOL/Transportistas');
                }
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };

        //CARGA DE FOTO DE TRANSPORTISTA
        $scope.guardarFotoTransportista = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_transportista').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_transportista').src = e.target.result;
                        document.getElementById('img_perfil_transportista').className = 'imagen-perfil';
                        document.getElementById('i_perfil_transportista').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//LISTADO CHOFERES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('choferesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refChoferes;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    var refChoferes = firebase.database().ref().child('choferes').orderByChild('nombre');
                    break;
                case "transportista":
                    var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes').orderByChild('nombre');
                    break;
            }

            refChoferes.on("value", function (snapshot) {
                var arrayChoferes = [];
                snapshot.forEach(function (childSnapshot) {
                    choferes = childSnapshot.val();
                    if (choferes.estatus !== 'eliminado') {
                        arrayChoferes.push(choferes);
                    }
                });
                $scope.choferes = arrayChoferes;
            });
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.cambiarEstatus = function (chofer) {
            //NODO CHOFERES
            var refChofer = firebase.database().ref('choferes').child(chofer.firebaseId);
            var firebaseChofer = $firebaseObject(refChofer);
            //CARGA DEL CHOFER
            firebaseChofer.$loaded().then(function () {
                firebaseChofer.estatus = chofer.estatus;
                firebaseChofer.$save().then(function () {
                    console.log('Estatus changed to ' + chofer.estatus);
                    //CREAR CHOFER EN TRANSPORTISTA
                    var refTransportista = firebase.database().ref('transportistas');
                    refTransportista.once("value").then(function (snapshot) {
                        snapshot.forEach(function (childSnapshot) {
                            var transportista = childSnapshot.val();
                            if (transportista.transportista.firebaseId === chofer.firebaseIdDelTransportista) {
                                var id = transportista.transportista.firebaseId;
                                var refTrans = firebase.database().ref('transportistas').child(id).child('choferes').child(chofer.firebaseId).child('estatus');
                                refTrans.set(firebaseChofer.estatus).then(function () {
                                    console.log('Client added in Transportist');
                                });
                            }
                        });
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (chofer) {
            //NODO CHOFERES
            var refChofer = firebase.database().ref('choferes').child(chofer.firebaseId);
            var firebaseChofer = $firebaseObject(refChofer);
            //CARGA DEL CHOFER
            firebaseChofer.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#choferes')))
                    .title('¿Eliminar chofer?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseChofer.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CHOFER
                    firebaseChofer.estatus = "eliminado";
                    firebaseChofer.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });

                    var refTransportistas = firebase.database().ref().child('transportistas');
                    refTransportistas.child(chofer.firebaseIdDelTransportista).on("child_added", function (snapshot) {
                        refTransportistas.child(chofer.firebaseIdDelTransportista).child('choferes').child(chofer.firebaseId).child('estatus').set('eliminado');
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//REGISTRO CHOFER CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('choferController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        //INICIALIZAR CHOFER
        $scope.firebaseChofer = {
            nombre: "",
            firebaseIdDelTransportista: "",
            numeroDeLicencia: "",
            numeroDeSeguroSocial: "",
            curp: "",
            numeroInterior: "",
            numeroExterior: "",
            calle: "",
            colonia: "",
            ciudad: "",
            estado: "",
            codigoPostal: "",
            telefono: "",
            celular1: "",
            celular2: "",
            correoElectronico: "",
            contrasena: "",
            estatus: "inactivo",
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            imagenURL: "",
            tipoDeUsuario: "chofer",
            firebaseId: ""
        }

        var refUsuarios = firebase.database().ref().child('usuarios');
        var refChofer = firebase.database().ref().child('choferes');
        var refTransportistas = firebase.database().ref().child('transportistas');
        var refListadoTransportistas = firebase.database().ref().child('listaDeTransportistas').orderByValue();

        //LISTADO TRANSPORTISTAS
        $scope.empresasTransportista = $firebaseArray(refListadoTransportistas);
        refListadoTransportistas.on('value', function (snap) {
            $scope.empresasTransportista.$value = snap.key;
        });

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refChoferes;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    $scope.administrador = true;
                    break;
                case "transportista":
                    $scope.transportista = true;
                    $scope.firebaseChofer.firebaseIdDelTransportista = usuario.uid;
                    break;
                case "chofer":
                    $scope.chofer = true;
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //CHOFER
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CONSULTAR CHOFER
        if ($routeParams.ID) {
            var chofer = $firebaseObject(refChofer.child($routeParams.ID));
            chofer.$loaded().then(function () {
                $scope.firebaseChofer = chofer;
                console.log('Chofer found');
                if (usuario.uid === $routeParams.ID) {
                    $scope.correoElectronicoAnterior = $scope.firebaseChofer.correoElectronico;
                }
            }).catch(function (error) {
                console.log(error);
            });
        }
        else {
            $scope.ownUser = true;
        }

        //GUARDAR CHOFER
        $scope.registrarChofer = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var usuarioChofer;

            //CREAR USUARIO EN BASE DE DATOS
            var crearUsuarioBD = function (chofer) {
                return refUsuarios.child(chofer.firebaseId).child('tipoDeUsuario').set('chofer');
            }

            //ACTUALIZACIÓN DE CHOFER EN BD
            var actualizarChoferBD = function (chofer) {
                var objetoChofer = $firebaseObject(refChofer.child(chofer.firebaseId));
                objetoChofer.nombre = chofer.nombre;
                objetoChofer.firebaseIdDelTransportista = chofer.firebaseIdDelTransportista;
                objetoChofer.numeroDeLicencia = chofer.numeroDeLicencia;
                objetoChofer.numeroDeSeguroSocial = chofer.numeroDeSeguroSocial;
                objetoChofer.curp = chofer.curp;
                objetoChofer.numeroInterior = chofer.numeroInterior;
                objetoChofer.numeroExterior = chofer.numeroExterior;
                objetoChofer.calle = chofer.calle;
                objetoChofer.colonia = chofer.colonia;
                objetoChofer.ciudad = chofer.ciudad;
                objetoChofer.estado = chofer.estado;
                objetoChofer.codigoPostal = chofer.codigoPostal;
                objetoChofer.telefono = chofer.telefono;
                objetoChofer.celular1 = chofer.celular1;
                objetoChofer.celular2 = chofer.celular2;
                objetoChofer.correoElectronico = chofer.correoElectronico;
                objetoChofer.fechaDeCreacion = chofer.fechaDeCreacion;
                objetoChofer.fechaDeEdicion = unixTime();
                objetoChofer.imagenURL = chofer.imagenURL;
                objetoChofer.firebaseId = chofer.firebaseId;
                objetoChofer.tipoDeUsuario = "chofer";
                objetoChofer.estatus = chofer.estatus;

                objetoChofer.$save().then(function () {
                    console.log('Chofer updated');
                });

                refTransportistas.child(chofer.firebaseIdDelTransportista).on("child_added", function (snapshot) {
                    var ObjetoChoferTransportista = $firebaseObject(refTransportistas.child(chofer.firebaseIdDelTransportista).child('choferes').child(chofer.firebaseId));
                    ObjetoChoferTransportista.nombre = chofer.nombre;
                    ObjetoChoferTransportista.firebaseIdDelTransportista = chofer.firebaseIdDelTransportista;
                    ObjetoChoferTransportista.numeroDeLicencia = chofer.numeroDeLicencia;
                    ObjetoChoferTransportista.numeroDeSeguroSocial = chofer.numeroDeSeguroSocial;
                    ObjetoChoferTransportista.curp = chofer.curp;
                    ObjetoChoferTransportista.numeroInterior = chofer.numeroInterior;
                    ObjetoChoferTransportista.numeroExterior = chofer.numeroExterior;
                    ObjetoChoferTransportista.calle = chofer.calle;
                    ObjetoChoferTransportista.colonia = chofer.colonia;
                    ObjetoChoferTransportista.ciudad = chofer.ciudad;
                    ObjetoChoferTransportista.estado = chofer.estado;
                    ObjetoChoferTransportista.codigoPostal = chofer.codigoPostal;
                    ObjetoChoferTransportista.telefono = chofer.telefono;
                    ObjetoChoferTransportista.celular1 = chofer.celular1;
                    ObjetoChoferTransportista.celular2 = chofer.celular2;
                    ObjetoChoferTransportista.correoElectronico = chofer.correoElectronico;
                    ObjetoChoferTransportista.fechaDeCreacion = chofer.fechaDeCreacion;
                    ObjetoChoferTransportista.fechaDeEdicion = unixTime();
                    ObjetoChoferTransportista.imagenURL = chofer.imagenURL;
                    ObjetoChoferTransportista.firebaseId = chofer.firebaseId;
                    ObjetoChoferTransportista.tipoDeUsuario = "chofer";
                    ObjetoChoferTransportista.estatus = chofer.estatus;

                    ObjetoChoferTransportista.$save().then(function () {
                        console.log('Chofer updated in Transportist');
                    });
                });

            }

            //CREACIÓN DE CHOFER EN BD
            var crearChoferBD = function (chofer) {
                refChofer.child(chofer.firebaseId).set(chofer);
                refChofer.child(chofer.firebaseId).child('contrasena').set(null);
                console.log('Chofer created in DB');
            }

            //CREACIÓN DE CHOFER EN TRANSPORTISTA
            var crearChoferTransportista = function (chofer) {
                refTransportistas.child(chofer.firebaseIdDelTransportista).on("child_added", function (snapshot) {
                    refTransportistas.child(chofer.firebaseIdDelTransportista).child('choferes').child(chofer.firebaseId).set(chofer);
                    refTransportistas.child(chofer.firebaseIdDelTransportista).child('choferes').child(chofer.firebaseId).child(contrasena).set(null);;
                });
                console.log('Chofer created in Transportist DB');
            }

            //ENVIAR CORREO
            var enviarCorreoConfirmacion = function (usuarioChofer) {
                return usuarioChofer.sendEmailVerification();
            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#registro')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CARGA IMAGEN DE PERFIL
            var cargarImagen = function (chofer) {
                var archivo = document.getElementById("input_foto_chofer").files[0];
                if (archivo) {
                    var nombreArchivo = chofer.firebaseId + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //CREACIÓN DE CHOFER EN BD
                            console.log('Chofer image loaded to ' + url)
                            chofer.imagenURL = url;
                            actualizarChoferBD(chofer);
                            actualizarPerfil(usuarioChofer, chofer);
                        }).catch(function (error) {
                            console.log(error);
                        });
                    });
                }
                else {
                    console.log('Image not selected');
                }
            }

            //ACTUALIZACIÓN DE PERFIL
            var actualizarPerfil = function (usuarioChofer, chofer) {
                usuarioChofer.updateProfile({
                    displayName: chofer.nombre,
                    photoURL: chofer.imagenURL
                })
                console.log('Profile updated');
            }

            //CREACIÓN DEL CHOFER
            if (!$scope.firebaseChofer.firebaseId) {
                //SE CREA EL USUARIO
                createUserService.auth().createUserWithEmailAndPassword($scope.firebaseChofer.correoElectronico, $scope.firebaseChofer.contrasena).then(function (user) {
                    console.log('User created.');
                    usuarioChofer = user;
                    $scope.firebaseChofer.firebaseId = user.uid;

                    crearUsuarioBD($scope.firebaseChofer).then(function () {
                        console.log('User created in DB');
                        crearChoferBD($scope.firebaseChofer);
                        crearChoferTransportista($scope.firebaseChofer);
                        enviarCorreoConfirmacion(usuarioChofer).then(function () {
                            console.log('Email sent');
                        }).catch(function (error) {
                            console.log(error);
                        });
                        cargarImagen($scope.firebaseChofer);
                        actualizarPerfil(usuarioChofer, $scope.firebaseChofer);
                        alerta('<br/> <p>Chofer registrado. </p> <p> Hemos enviado un mensaje a su cuenta de correo electrónico. Es necesario que la cuenta sea validada desde el enlace que compartimos.</p>', '/CHAOL/Choferes');
                    }).catch(function (error) {
                        console.log(error);
                    });
                    //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                }).catch(function (error) {
                    //ERROR
                    if (error.code === 'auth/email-already-in-use') {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#registro')))
                                .clickOutsideToClose(false)
                                .title('Oops! Algo salió mal')
                                .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseChofer.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/>')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar')
                        );
                    }
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                });
            }
            //ACTUALIZACIÓN DEL CHOFER
            else {
                //VALIDAR SI SE TRATA DEL USUARIO CORRECTO
                if (usuario.uid === $scope.firebaseChofer.firebaseId) {
                    $mdDialog.show(
                        $mdDialog.prompt()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Confirmación de credenciales')
                            .htmlContent('<br/><p>Para confirmar tu identidad, ingresa la contraseña que utilizaste para <b>' + usuario.email + '</b>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                            .cancel('Cancelar')
                    ).then(function (result) {
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                        //OBTENER CREDENCIALES
                        const credential = firebase.auth.EmailAuthProvider.credential(usuario.email, result);
                        const user = firebase.auth().currentUser;
                        user.reauthenticateWithCredential(credential).then(function () {
                            //ACTUALIZAR EMAIL
                            auth.$updateEmail($scope.firebaseChofer.correoElectronico).then(function () {
                                console.log('Email updated');
                                //ACTUALIZAR CONTRASEÑA
                                auth.$updatePassword($scope.firebaseChofer.contrasena).then(function () {
                                    actualizarChoferBD($scope.firebaseChofer);
                                    cargarImagen($scope.firebaseChofer);
                                    auth.$signInWithEmailAndPassword($scope.firebaseChofer.correoElectronico, result).then(function () {
                                        console.log('Login succesful');
                                        alerta('<br/> <p>Cuenta actualizada. </p> <p> Hemos actualizado los datos de tu cuenta exitosamente.</p>', '/CHAOL');
                                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                    }).catch(function (error) {
                                        console.log(error)
                                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                    })
                                }).catch(function (error) {
                                    console.log(error);
                                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                });
                            }).catch(function (error) {
                                console.log(error);
                                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            })
                        }).catch(function (error) {
                            console.log(error);
                            if (error.code === 'auth/wrong-password') {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#registro')))
                                        .clickOutsideToClose(false)
                                        .title('Oops! Algo salió mal')
                                        .htmlContent('<br/> <p>Al parecer esta no es tu contraseña. Por favor, intenta de nuevo. </p>')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar')
                                );
                            }
                            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                        });
                    });
                }
                else {
                    actualizarChoferBD($scope.firebaseChofer);
                    cargarImagen($scope.firebaseChofer);
                    alerta('<br/> <p>Chofer actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseChofer.nombre + '</b> exitosamente.</p>', '/CHAOL/Choferes');
                }
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };

        //CARGA DE FOTO DE CHOFER
        $scope.guardarFotoChofer = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_chofer').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_chofer').src = e.target.result;
                        document.getElementById('img_perfil_chofer').className = 'imagen-perfil';
                        document.getElementById('i_perfil_chofer').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//REGISTRO BODEGA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('bodegaController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        //CATALOGO DE ESTADOS
        $scope.estados = [
            "CDMX",
            "Chihuahua",
            "Otro"
        ]

        //INICIALIZAR BODEGA
        $scope.firebaseBodega = {
            nombreDelCliente: "",
            nombreDeLaBodega: "",
            numeroInterior: "",
            numeroExterior: "",
            calle: "",
            colonia: "",
            ciudad: "",
            estado: "",
            codigoPostal: "",
            estatus: "activo",
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            firebaseIdBodega: "",
            firebaseIdDelCliente: ""
        }

        var refUsuarios = firebase.database().ref().child('usuarios');
        var refCliente = firebase.database().ref().child('clientes');

        //LISTADO CLIENTES
        var refClientes = firebase.database().ref().child('clientes');
        refClientes.on("value", function (snapshot) {
            var arrayBodegas = [];
            snapshot.forEach(function (childSnapshot) {
                clientes = childSnapshot.val();
                if (clientes.cliente.estatus !== 'eliminado') {
                    childSnapshot.forEach(function (clienteSnapShot) {
                        if (clienteSnapShot.key === 'cliente') {
                            var cliente = clienteSnapShot.val();
                            arrayBodegas.push(cliente);
                        }
                    })
                }
            });
            $scope.clientes = arrayBodegas;
        });

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    $scope.administrador = true;
                    break;
                case "cliente":
                    $scope.cliente = true;
                    var refClienteUsuario = firebase.database().ref('clientes').child(usuario.uid).child('cliente');
                    var firebaseClienteUsuario = $firebaseObject(refClienteUsuario);
                    firebaseClienteUsuario.$loaded().then(function () {
                        $scope.firebaseBodega.nombreDelCliente = firebaseClienteUsuario.nombre;
                    })
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //BODEGA
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CONSULTAR BODEGA
        if ($routeParams.ID) {
            var refBodegas = firebase.database().ref().child('clientes');
            refBodegas.on("value", function (snapshot) {
                var arrayBodegas = [];
                snapshot.forEach(function (childSnapshot) {
                    childSnapshot.forEach(function (bodegaSnapshot) {
                        if (bodegaSnapshot.key === "bodegas") {
                            bodegaSnapshot.forEach(function (bodegaChildSnapshot) {
                                var bodega = bodegaChildSnapshot.val();
                                if (bodega.firebaseIdBodega === $routeParams.ID) {
                                    $scope.firebaseBodega = bodega;
                                }
                            });
                        }
                    })
                });
            });
        }

        //GUARDAR BODEGA
        $scope.registrarBodega = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';

            //ACTUALIZACIÓN DE CLIENTE EN BD
            var actualizarBodegaBD = function (bodega) {
                var objetoBodega = $firebaseObject(refCliente.child(bodega.firebaseIdDelCliente).child('bodegas').child(bodega.firebaseIdBodega));
                objetoBodega.nombreDelCliente = bodega.nombreDelCliente;
                objetoBodega.nombreDeLaBodega = bodega.nombreDeLaBodega;
                objetoBodega.numeroInterior = bodega.numeroInterior;
                objetoBodega.numeroExterior = bodega.numeroExterior;
                objetoBodega.calle = bodega.calle;
                objetoBodega.colonia = bodega.colonia;
                objetoBodega.ciudad = bodega.ciudad;
                objetoBodega.estado = bodega.estado;
                objetoBodega.codigoPostal = bodega.codigoPostal;
                objetoBodega.fechaDeCreacion = bodega.fechaDeCreacion;
                objetoBodega.fechaDeEdicion = unixTime();
                objetoBodega.firebaseIdBodega = bodega.firebaseIdBodega;
                objetoBodega.firebaseIdDelCliente = bodega.firebaseIdDelCliente;
                objetoBodega.estatus = 'activo';

                objetoBodega.$save().then(function () {
                    console.log('Store updated');
                })
            }

            //CREACIÓN DE BODEGA EN BD
            var crearBodegaBD = function (bodega) {
                var refClientes = firebase.database().ref().child('clientes');
                refClientes.once("value", function (snapshot) {
                    var arrayClientes = [];
                    snapshot.forEach(function (childSnapshot) {
                        clientes = childSnapshot.val();
                        if (clientes.cliente.nombre === $scope.firebaseBodega.nombreDelCliente) {
                            bodega.firebaseIdDelCliente = clientes.cliente.firebaseId;
                            var firebaseBodega = refCliente.child(bodega.firebaseIdDelCliente).child('bodegas').push();
                            bodega.firebaseIdBodega = firebaseBodega.key;
                            $scope.firebaseBodega = bodega;
                            firebaseBodega.set(bodega);
                            console.log('Store created in DB');
                        }
                    });
                });

            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#bodega')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CREACIÓN DE LA BODEGA
            if (!$scope.firebaseBodega.firebaseIdBodega) {
                crearBodegaBD($scope.firebaseBodega);
                alerta('<br/> <p>Bodega registrada. </p> <p> A partir de este momento, puede utilizar esta bodega.</p>', '/CHAOL/Bodegas');
                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
            //ACTUALIZACIÓN DE LA BODEGA
            else {
                actualizarBodegaBD($scope.firebaseBodega);
                alerta('<br/> <p>Bodega actualizada. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseBodega.nombreDeLaBodega + '</b> exitosamente.</p>', '/CHAOL/Bodegas');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//LISTADO BODEGAS CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('bodegasController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refBodegas;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    refBodegas = firebase.database().ref().child('clientes');
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            clientes = childSnapshot.val();
                            if (clientes.cliente.estatus !== 'eliminado') {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    if (bodegaSnapshot.key === "bodegas") {
                                        bodegaSnapshot.forEach(function (bodegaChildSnapshot) {
                                            var bodega = bodegaChildSnapshot.val();
                                            if (bodega.estatus !== 'eliminado') {
                                                arrayBodegas.push(bodega);
                                            }
                                        });
                                    }
                                })
                            }
                        });
                        $scope.bodegas = arrayBodegas;
                    });
                    break;
                case "cliente":
                    refBodegas = firebase.database().ref().child('clientes').child(usuario.uid);
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "bodegas") {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    var bodega = bodegaSnapshot.val()
                                    if (bodega.estatus !== 'eliminado') {
                                        arrayBodegas.push(bodega);
                                    }
                                })
                            }
                        });
                        $scope.bodegas = arrayBodegas;
                    });
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.eliminar = function (bodega) {
            //NODO CLIENTES
            var refBodega = firebase.database().ref('clientes').child(bodega.firebaseIdDelCliente).child('bodegas').child(bodega.firebaseIdBodega);
            var firebaseBodega = $firebaseObject(refBodega);
            //CARGA DEL CLIENTE
            firebaseBodega.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#bodegas')))
                    .title('¿Eliminar bodega?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseBodega.nombreDeLaBodega + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseBodega.estatus = "eliminado";
                    firebaseBodega.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//REGISTRO TRACTOR CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('tractorController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        //INICIALIZAR TRACTOR
        $scope.firebaseTractor = {
            marca: "",
            modelo: "",
            numeroDeSerie: "",
            numeroEconomico: "",
            placa: "",
            estatus: "libre",
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            firebaseId: "",
            firebaseIdDelTransportista: ""
        }

        var refUsuarios = firebase.database().ref().child('usuarios');
        var refTransportista = firebase.database().ref().child('transportistas');
        var refListadoTransportistas = firebase.database().ref().child('listaDeTransportistas').orderByValue();

        //LISTADO TRANSPORTISTAS
        $scope.empresasTransportista = $firebaseArray(refListadoTransportistas);
        refListadoTransportistas.on('value', function (snap) {
            $scope.empresasTransportista.$value = snap.key;
        });

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    $scope.administrador = true;
                    break;
                case "transportista":
                    $scope.transportista = true;
                    $scope.firebaseTractor.firebaseIdDelTransportista = usuario.uid;
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //TRACTOR
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CONSULTAR TRACTOR
        if ($routeParams.ID) {
            var refTractores = firebase.database().ref().child('transportistas');
            refTractores.on("value", function (snapshot) {
                snapshot.forEach(function (childSnapshot) {
                    childSnapshot.forEach(function (tractorSnapshot) {
                        if (tractorSnapshot.key === "tractores") {
                            tractorSnapshot.forEach(function (tractorChildSnapshot) {
                                var tractor = tractorChildSnapshot.val();
                                if (tractor.firebaseId === $routeParams.ID) {
                                    $scope.firebaseTractor = tractor;
                                }
                            });
                        }
                    })
                });
            });
        }

        //GUARDAR TRACTOR
        $scope.registrarTractor = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';

            //ACTUALIZACIÓN DE CLIENTE EN BD
            var actualizarTractorBD = function (tractor) {
                var objetoTractor = $firebaseObject(refTransportista.child(tractor.firebaseIdDelTransportista).child('tractores').child(tractor.firebaseId));
                objetoTractor.firebaseIdDelTransportista = tractor.firebaseIdDelTransportista;
                objetoTractor.marca = tractor.marca;
                objetoTractor.modelo = tractor.modelo;
                objetoTractor.numeroDeSerie = tractor.numeroDeSerie;
                objetoTractor.numeroEconomico = tractor.numeroEconomico;
                objetoTractor.placa = tractor.placa;
                objetoTractor.idGPS = tractor.idGPS;
                objetoTractor.fechaDeCreacion = tractor.fechaDeCreacion;
                objetoTractor.fechaDeEdicion = unixTime();
                objetoTractor.firebaseId = tractor.firebaseId;
                objetoTractor.firebaseIdDelTransportista = tractor.firebaseIdDelTransportista;
                objetoTractor.estatus = 'libre';

                objetoTractor.$save().then(function () {
                    console.log('Store updated');
                })
            }

            //CREACIÓN DE TRACTOR EN BD
            var crearTractorBD = function (tractor) {
                var refTransportistas = firebase.database().ref().child('transportistas');
                refTransportistas.once("value", function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        transportistas = childSnapshot.val();
                        if (transportistas.transportista.firebaseId === $scope.firebaseTractor.firebaseIdDelTransportista) {
                            tractor.firebaseIdDelTransportista = transportistas.transportista.firebaseId;
                            var firebaseTractor = refTransportista.child(tractor.firebaseIdDelTransportista).child('tractores').push();
                            tractor.firebaseId = firebaseTractor.key;
                            $scope.firebaseTractor = tractor;
                            firebaseTractor.set(tractor);
                            console.log('Store created in DB');
                        }
                    });
                });

            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#tractor')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CREACIÓN DE LA TRACTOR
            if (!$scope.firebaseTractor.firebaseId) {
                crearTractorBD($scope.firebaseTractor);
                alerta('<br/> <p>Tractor registrado. </p> <p> A partir de este momento, puede utilizar este tractor.</p>', '/CHAOL/Tractores');
                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
            //ACTUALIZACIÓN DE LA TRACTOR
            else {
                actualizarTractorBD($scope.firebaseTractor);
                alerta('<br/> <p>Tractor actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseTractor.marca + ' - ' + $scope.firebaseTractor.modelo + ' - ' + $scope.firebaseTractor.placa + '</b> exitosamente.</p>', '/CHAOL/Tractores');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//LISTADO TRACTOR CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('tractoresController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refTractores;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    refTractores = firebase.database().ref().child('transportistas');
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            transportistas = childSnapshot.val();
                            if (transportistas.transportista.estatus !== 'eliminado') {
                                childSnapshot.forEach(function (tractorSnapshot) {
                                    if (tractorSnapshot.key === "tractores") {
                                        tractorSnapshot.forEach(function (tractorChildSnapshot) {
                                            var tractor = tractorChildSnapshot.val();
                                            if (tractor.estatus !== 'eliminado') {
                                                arrayTractores.push(tractor);
                                            }
                                        });
                                    }
                                })
                            }
                        });
                        $scope.tractores = arrayTractores;
                    });
                    break;
                case "transportista":
                    refTractores = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "tractores") {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    var tractor = bodegaSnapshot.val()
                                    if (tractor.estatus !== 'eliminado') {
                                        arrayTractores.push(tractor);
                                    }
                                })
                            }
                        });
                        $scope.tractores = arrayTractores;
                    });
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.eliminar = function (tractor) {
            //NODO CLIENTES
            var refTractor = firebase.database().ref('transportistas').child(tractor.firebaseIdDelTransportista).child('tractores').child(tractor.firebaseId);
            var firebaseTractor = $firebaseObject(refTractor);
            //CARGA DEL CLIENTE
            firebaseTractor.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#tractores')))
                    .title('¿Eliminar tractor?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseTractor.marca + ' - ' + firebaseTractor.modelo + ' - ' + firebaseTractor.placa + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseTractor.estatus = "eliminado";
                    firebaseTractor.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//REGISTRO TRACTOR CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('remolqueController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        //TIPOS DE REMOLQUE
        $scope.tiposRemolques = [
            "Caja Refrigerada"
        ]

        //INICIALIZAR TRACTOR
        $scope.firebaseRemolque = {
            marca: "",
            modelo: "",
            numeroDeSerie: "",
            numeroEconomico: "",
            placa: "",
            tipoDeRemolque: "",
            estatus: "libre",
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            firebaseId: "",
            firebaseIdDelTransportista: ""
        }

        var refUsuarios = firebase.database().ref().child('usuarios');
        var refTransportista = firebase.database().ref().child('transportistas');
        var refListadoTransportistas = firebase.database().ref().child('listaDeTransportistas').orderByValue();

        //LISTADO TRANSPORTISTAS
        $scope.empresasTransportista = $firebaseArray(refListadoTransportistas);
        refListadoTransportistas.on('value', function (snap) {
            $scope.empresasTransportista.$value = snap.key;
        });

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    $scope.administrador = true;
                    break;
                case "transportista":
                    $scope.transportista = true;
                    $scope.firebaseRemolque.firebaseIdDelTransportista = usuario.uid;
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //BODEGA
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CONSULTAR BODEGA
        if ($routeParams.ID) {
            var refRemolques = firebase.database().ref().child('transportistas');
            refRemolques.on("value", function (snapshot) {
                var arrayRemolques = [];
                snapshot.forEach(function (childSnapshot) {
                    childSnapshot.forEach(function (remolqueSnapshot) {
                        if (remolqueSnapshot.key === "remolques") {
                            remolqueSnapshot.forEach(function (remolqueChildSnapshot) {
                                var remolque = remolqueChildSnapshot.val();
                                if (remolque.firebaseId === $routeParams.ID) {
                                    $scope.firebaseRemolque = remolque;
                                }
                            });
                        }
                    })
                });
            });
        }

        //GUARDAR REMOLQUE
        $scope.registrarRemolque = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';

            //ACTUALIZACIÓN DE CLIENTE EN BD
            var actualizarRemolqueBD = function (remolque) {
                var objetoRemolque = $firebaseObject(refTransportista.child(remolque.firebaseIdDelTransportista).child('remolques').child(remolque.firebaseId));
                objetoRemolque.firebaseIdDelTransportista = remolque.firebaseIdDelTransportista;
                objetoRemolque.marca = remolque.marca;
                objetoRemolque.modelo = remolque.modelo;
                objetoRemolque.numeroDeSerie = remolque.numeroDeSerie;
                objetoRemolque.numeroEconomico = remolque.numeroEconomico;
                objetoRemolque.placa = remolque.placa;
                objetoRemolque.idGPS = remolque.idGPS;
                objetoRemolque.tipoDeRemolque = remolque.tipoDeRemolque;
                objetoRemolque.fechaDeCreacion = remolque.fechaDeCreacion;
                objetoRemolque.fechaDeEdicion = unixTime();
                objetoRemolque.firebaseId = remolque.firebaseId;
                objetoRemolque.firebaseIdDelTransportista = remolque.firebaseIdDelTransportista;
                objetoRemolque.estatus = 'libre';

                objetoRemolque.$save().then(function () {
                    console.log('Store updated');
                })
            }

            //CREACIÓN DE TRACTOR EN BD
            var crearRemolqueBD = function (remolque) {
                var refTransportistas = firebase.database().ref().child('transportistas');
                refTransportistas.once("value", function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        transportistas = childSnapshot.val();
                        if (transportistas.transportista.firebaseId === $scope.firebaseRemolque.firebaseIdDelTransportista) {
                            remolque.firebaseIdDelTransportista = transportistas.transportista.firebaseId;
                            var firebaseRemolque = refTransportista.child(remolque.firebaseIdDelTransportista).child('remolques').push();
                            remolque.firebaseId = firebaseRemolque.key;
                            $scope.firebaseRemolque = remolque;
                            firebaseRemolque.set(remolque);
                            console.log('Store created in DB');
                        }
                    });
                });

            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#remolque')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CREACIÓN DE LA BODEGA
            if (!$scope.firebaseRemolque.firebaseId) {
                crearRemolqueBD($scope.firebaseRemolque);
                alerta('<br/> <p>Remolque registrado. </p> <p> A partir de este momento, puede utilizar este remolque.</p>', '/CHAOL/Remolques');
                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
            //ACTUALIZACIÓN DE LA BODEGA
            else {
                actualizarRemolqueBD($scope.firebaseRemolque);
                alerta('<br/> <p>Tractor actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseRemolque.marca + ' - ' + $scope.firebaseRemolque.modelo + ' - ' + $scope.firebaseRemolque.placa + '</b> exitosamente.</p>', '/CHAOL/Remolques');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//LISTADO REMOLQUES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('remolquesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refRemolques;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    refRemolques = firebase.database().ref().child('transportistas');
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            transportistas = childSnapshot.val();
                            if (transportistas.transportista.estatus !== 'eliminado') {
                                childSnapshot.forEach(function (remolqueSnapshot) {
                                    if (remolqueSnapshot.key === "remolques") {
                                        remolqueSnapshot.forEach(function (remolqueChildSnapshot) {
                                            var remolque = remolqueChildSnapshot.val();
                                            if (remolque.estatus !== 'eliminado') {
                                                arrayRemolques.push(remolque);
                                            }
                                        });
                                    }
                                })
                            }
                        });
                        $scope.remolques = arrayRemolques;
                    });
                    break;
                case "transportista":
                    refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "remolques") {
                                childSnapshot.forEach(function (remolqueSnapshot) {
                                    var remolque = remolqueSnapshot.val()
                                    if (remolque.estatus !== 'eliminado') {
                                        arrayRemolques.push(remolque);
                                    }
                                })
                            }
                        });
                        $scope.remolques = arrayRemolques;
                    });
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.eliminar = function (remolque) {
            //NODO CLIENTES
            var refRemolque = firebase.database().ref('transportistas').child(remolque.firebaseIdDelTransportista).child('remolques').child(remolque.firebaseId);
            var firebaseRemolque = $firebaseObject(refRemolque);
            //CARGA DEL CLIENTE
            firebaseRemolque.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#tractores')))
                    .title('¿Eliminar remolque?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseRemolque.marca + ' - ' + firebaseRemolque.modelo + ' - ' + firebaseRemolque.placa + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseRemolque.estatus = "eliminado";
                    firebaseRemolque.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//AGENDA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('agendaController', function ($scope, $location, $firebaseAuth, $firebaseObject) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        var fletes;

        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            //TRANSPORTISTA SELECCIONADO
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            //ESTATUS DEL FLETE
                            var estatusFlete = buscarEstatusFlete(fletes);
                            //ICONO QUE INDICA LA ALERTA
                            var alerta = false;
                            if (estatusFlete === 'Por Cotizar'
                                || estatusFlete === 'Transportista Por Confirmar') {
                                alerta = true;
                            }

                            //ANEXO A ARREGLO PARA CALENDARIO
                            arrayFletes.push({
                                id: fletes.flete.idFlete,
                                firebaseId: fletes.flete.firebaseId,
                                cliente: fletes.flete.cliente,
                                transportista: transportistaSeleccionado,
                                name: fletes.flete.cliente,
                                startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                estatus: estatusFlete,
                                alert: alerta
                            });

                            //ANEXO A ARREGLO PARA LA LISTA DE LA DERECHA
                            if (estatusFlete === 'Por Cotizar'
                                || estatusFlete === 'Esperando Por Transportista'
                                || estatusFlete === 'Transportisa Por Confirmar'
                                || estatusFlete === 'Unidades Por Asignar'
                                || estatusFlete === 'Envío Por Iniciar') {
                                arrayListado.push({
                                    id: fletes.flete.idFlete,
                                    firebaseId: fletes.flete.firebaseId,
                                    cliente: fletes.flete.cliente,
                                    transportista: transportistaSeleccionado,
                                    name: fletes.flete.cliente,
                                    startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    estatus: estatusFlete
                                });
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                case 'cliente':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            if (fletes.bodegaDeCarga.firebaseIdDelCliente === usuario.uid) {
                                //TRANSPORTISTA SELECCIONADO
                                var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                                //ESTATUS DEL FLETE
                                var estatusFlete = buscarEstatusFlete(fletes);
                                //ICONO QUE INDICA LA ALERTA
                                var alerta = false;

                                //ANEXO A ARREGLO PARA CALENDARIO
                                arrayFletes.push({
                                    id: fletes.flete.idFlete,
                                    firebaseId: fletes.flete.firebaseId,
                                    cliente: fletes.flete.cliente,
                                    transportista: transportistaSeleccionado,
                                    name: fletes.flete.cliente,
                                    startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    estatus: estatusFlete,
                                    alert: alerta
                                });

                                //ANEXO A ARREGLO PARA LA LISTA DE LA DERECHA
                                if (estatusFlete === 'Por Cotizar'
                                    || estatusFlete === 'Esperando Por Transportista'
                                    || estatusFlete === 'Transportisa Por Confirmar'
                                    || estatusFlete === 'Unidades Por Asignar'
                                    || estatusFlete === 'Envío Por Iniciar') {
                                    arrayListado.push({
                                        id: fletes.flete.idFlete,
                                        firebaseId: fletes.flete.firebaseId,
                                        cliente: fletes.flete.cliente,
                                        transportista: transportistaSeleccionado,
                                        name: fletes.flete.cliente,
                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                        estatus: estatusFlete
                                    });
                                }
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                case 'transportista':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            //TRANSPORTISTA SELECCIONADO
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            //ESTATUS DEL FLETE
                            var estatusFlete = buscarEstatusFlete(fletes);
                            //ICONO QUE INDICA LA ALERTA
                            var alerta = true;

                            switch (estatusFlete) {
                                case 'Esperando Por Transportista':
                                case 'Transportista Por Confirmar':
                                    snapshot.forEach(function (childSnapshot) {
                                        if (childSnapshot.key === 'transportistasInteresados') {
                                            childSnapshot.forEach(function (interesadosSnapshot) {
                                                var interesado = interesadosSnapshot.val();
                                                if (interesado.firebaseId === usuario.uid) {
                                                    alerta = false;
                                                }
                                            });
                                        }
                                    });
                                    arrayFletes.push({
                                        id: fletes.flete.idFlete,
                                        firebaseId: fletes.flete.firebaseId,
                                        cliente: fletes.flete.cliente,
                                        transportista: transportistaSeleccionado,
                                        name: fletes.flete.cliente,
                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                        estatus: estatusFlete,
                                        alert: alerta
                                    });
                                    break;
                                case 'Unidades Por Asignar':
                                case 'Envío Por Iniciar':
                                case 'Entregado':
                                case 'Finalizado':
                                case 'Cancelado':
                                    if (estatusFlete === 'Entregado'
                                        || estatusFlete == 'Finalizado'
                                        || estatusFlete === 'Cancelado') {
                                        alerta = false;
                                    }
                                    snapshot.forEach(function (childSnapshot) {
                                        if (childSnapshot.key === 'transportistaSeleccionado') {
                                            childSnapshot.forEach(function (transportistaSnapshot) {
                                                var transportista = transportistaSnapshot.val();
                                                if (transportista.firebaseId === usuario.uid) {
                                                    arrayFletes.push({
                                                        id: fletes.flete.idFlete,
                                                        firebaseId: fletes.flete.firebaseId,
                                                        cliente: fletes.flete.cliente,
                                                        transportista: transportistaSeleccionado,
                                                        name: fletes.flete.cliente,
                                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        estatus: estatusFlete,
                                                        alert: alerta
                                                    });
                                                    arrayListado.push({
                                                        id: fletes.flete.idFlete,
                                                        firebaseId: fletes.flete.firebaseId,
                                                        cliente: fletes.flete.cliente,
                                                        transportista: transportistaSeleccionado,
                                                        name: fletes.flete.cliente,
                                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        estatus: estatusFlete
                                                    });
                                                }
                                            })
                                        }
                                    });
                                    break;
                                default:
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                case 'chofer':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            //TRANSPORTISTA SELECCIONADO
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            //ESTATUS DEL FLETE
                            var estatusFlete = buscarEstatusFlete(fletes);
                            //ICONO QUE INDICA LA ALERTA
                            var alerta = false;

                            switch (estatusFlete) {
                                case 'Envío Por Iniciar':
                                case 'Entregado':
                                case 'Finalizado':
                                case 'Cancelado':
                                    snapshot.forEach(function (childSnapshot) {
                                        if (childSnapshot.key === 'transportistaSeleccionado') {
                                            childSnapshot.forEach(function (transportistaSnapshot) {
                                                var transportista = transportistaSnapshot.val();
                                                if (transportista.firebaseId === usuario.uid) {
                                                    arrayFletes.push({
                                                        id: fletes.flete.idFlete,
                                                        firebaseId: fletes.flete.firebaseId,
                                                        cliente: fletes.flete.cliente,
                                                        transportista: transportistaSeleccionado,
                                                        name: fletes.flete.cliente,
                                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        estatus: estatusFlete
                                                    });
                                                    arrayListado.push({
                                                        id: fletes.flete.idFlete,
                                                        firebaseId: fletes.flete.firebaseId,
                                                        cliente: fletes.flete.cliente,
                                                        transportista: transportistaSeleccionado,
                                                        name: fletes.flete.cliente,
                                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        estatus: estatusFlete
                                                    });
                                                }
                                            })
                                        }
                                    });
                                    break;
                                default:
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                    //FLETES ASIGNADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.choferSeleccionado !== undefined) {
                                if (fletes.choferSeleccionado.key === usuario.uid && (fletes.flete.estatus === "envioPorIniciar" || fletes.flete.estatus === "enProgreso")) {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarEnvioPorIniciar(arrayFletes);
                        buscarEnProgreso(arrayFletes);
                    });
                    break;
                default:
            }
        });

        //BUSCAR LOS TRANSPORTISTAS SELECCIONADOS DEL FLETE
        var buscarTransportistaSeleccionado = function (fletes, snapshot) {
            var transportistaSeleccionado = "Sin transportista asignado";
            if (fletes.transportistaSeleccionado !== undefined) {
                snapshot.forEach(function (childSnapshot) {
                    if (childSnapshot.key === 'transportistaSeleccionado') {
                        childSnapshot.forEach(function (transportistaSnapshot) {
                            var transportista = transportistaSnapshot.val();
                            transportistaSeleccionado = transportista.nombre;
                        })
                    }
                });
            }
            return transportistaSeleccionado;
        }

        //BUSCAR ESTATUS DEL FLETE
        var buscarEstatusFlete = function (fletes) {
            var estatusFlete = "";
            switch (fletes.flete.estatus) {
                case "fletePorCotizar":
                    estatusFlete = "Por Cotizar";
                    break;
                case "esperandoPorTransportista":
                    estatusFlete = "Esperando Por Transportista";
                    break;
                case "transportistaPorConfirmar":
                    estatusFlete = "Transportisa Por Confirmar";
                    break;
                case "unidadesPorAsignar":
                    estatusFlete = "Unidades Por Asignar";
                    break;
                case "enProgreso":
                    estatusFlete = "En Progreso";
                    break;
                case "envioPorIniciar":
                    estatusFlete = "Envío Por Iniciar"
                    break;
                case "entregado":
                    estatusFlete = "Entregado";
                case "finalizado":
                    estatusFlete = "Finalizado";
                    break;
                case "cancelado":
                    estatusFlete = "Cancelado";
                    break;
                default:
                    break;
            }
            return estatusFlete;
        }

        var iniciar_calendario = function (div, fletes) {
            var currentYear = new Date().getFullYear();

            //DETERMINAR FECHA DE HOY
            var hoy = new Date(currentYear, new Date().getMonth(), new Date().getDate()).getTime();

            $(div).calendar({
                enableContextMenu: true,

                customDayRenderer: function (element, date) {
                    if (date.getTime() == hoy) {
                        $(element).css('background-color', '#F34235');
                        $(element).css('font-weight', 'bold');
                        $(element).css('color', 'white');
                        $(element).css('padding', '0px');
                    }
                },

                enableRangeSelection: true,
                language: 'es',
                contextMenuItems: [
                    {
                        text: 'Consultar',
                        click: detalle
                    }
                ],
                selectRange: function (e) {
                    editEvent({ startDate: e.startDate, endDate: e.endDate });
                },
                mouseOnDay: function (e) {
                    if (e.events.length > 0) {
                        var content = '';

                        for (var i in e.events) {
                            if (i < 3) {
                                content += '<div class="event-tooltip-content">'
                                    + '<div class="event-name"><span>Estatus: </span><span style="color:' + e.events[i].color + '"><b>' + e.events[i].estatus + '</b></span></div>'
                                    + '<div class="event-name">Cliente: <b>' + e.events[i].cliente + '</b></div>'
                                    + '<div class="event-location">Transportista: <b>' + e.events[i].transportista + '</b></div>'
                                    + '</div>'
                                    + '<hr/>';
                            }
                            else {
                                content += '<div class="event-tooltip-content">'
                                    + '<div class="event-name text-center">' + (e.events.length - 3).toString() + ' fletes más</div>';
                                break;
                            }
                        }

                        $(e.element).popover({
                            trigger: 'manual',
                            container: 'body',
                            html: true,
                            content: content
                        });

                        $(e.element).popover('show');
                    }
                },
                mouseOutDay: function (e) {
                    if (e.events.length > 0) {
                        $(e.element).popover('hide');
                    }
                },
                dayContextMenu: function (e) {
                    $(e.element).popover('hide');
                },
                dataSource: fletes
            });

            function detalle(event) {
                document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                var id = event.firebaseId;
                $location.path('/CHAOL/Fletes/' + id);
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        }
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
