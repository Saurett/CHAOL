//CONFIG
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.config(function ($routeProvider, $locationProvider, $mdDateLocaleProvider) {
        $locationProvider.hashPrefix('');
        //INICIO
        $routeProvider.when("/Inicio", {
            css: 'estilos/login.css',
            templateUrl: "login.html",
            controller: "loginController"
        });
        //REGISTRO
        $routeProvider.when("/Registro", {
            templateUrl: "registro.html",
            controller: "registroController"
        })
        //RECUPERAR CONTRASEÑA
        $routeProvider.when("/RecuperarContrasena", {
            css: 'estilos/login.css',
            templateUrl: "recuperarContrasena.html",
            controller: "recuperarContrasenaController"
        })
        //INICIO
        $routeProvider.when("/CHAOL", {
            templateUrl: "modulos/inicio.html",
            controller: "inicioController",
            resolve: {
                "currentAuth": ["Auth", function (Firebase) {
                    return Firebase.Auth.$requireSignIn();
                }]
            }
        })
        //COLABORADORES
        $routeProvider.when("/CHAOL/Colaboradores", {
            templateUrl: "modulos/administracion/colaboradores/listado/listadoColaboradores.html",
            controller: "colaboradoresController",
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
        $routeProvider.when("/CHAOL/Colaboradores/Nuevo", {
            templateUrl: "modulos/administracion/colaboradores/captura/capturaColaborador.html",
            controller: "colaboradorController",
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
        $routeProvider.when("/CHAOL/Colaborador/:ID", {
            templateUrl: "modulos/administracion/colaboradores/captura/capturaColaborador.html",
            controller: "colaboradorController",
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
        //CLIENTES
        $routeProvider.when("/CHAOL/Clientes", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Clientes/Nuevo", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Clientes/:ID", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Transportistas", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Transportistas/Nuevo", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Transportistas/:ID", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Choferes", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Choferes/Nuevo", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Choferes/:ID", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Tractores", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Tractores/Nuevo", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Tractores/:ID", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Remolques", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Remolques/Nuevo", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Remolques/:ID", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Bodegas", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Bodegas/Nuevo", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Bodegas/:ID", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Perfil", {
            templateUrl: "construccion.html"
        })
        //AGENDA
        $routeProvider.when("/CHAOL/Fletes", {
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
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Fletes/Nuevo", {
            templateUrl: "modulos/fletes/captura/capturaFlete.html",
            controller: "fleteController",
            resolve: {
                "currentAuth": ["Auth", function (Firebase) {
                    var auth = Firebase.Auth;
                    var usuario = auth.$getAuth();
                    var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                    var firebaseUsuario = Firebase.Object(refUsuario);
                    firebaseUsuario.$loaded().then(function () {
                        switch (firebaseUsuario.tipoDeUsuario) {
                            case 'administrador':
                            case 'colaborador':
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
        $routeProvider.when("/CHAOL/Fletes/:ID", {
            templateUrl: "modulos/fletes/captura/capturaFlete.html",
            controller: "fleteController",
            resolve: {
                "currentAuth": ["Auth", function (Firebase) {
                    var auth = Firebase.Auth;
                    var usuario = auth.$getAuth();
                    var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                    var firebaseUsuario = Firebase.Object(refUsuario);
                    firebaseUsuario.$loaded().then(function () {
                        switch (firebaseUsuario.tipoDeUsuario) {
                            case 'administrador':
                            case 'colaborador':
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
        $routeProvider.otherwise({
            redirectTo: '/Inicio'
        });

        // DATEPICKER SPANISH
        $mdDateLocaleProvider.months = [
            'Enero',
            'Febrero',
            'Marzo',
            'Abril',
            'Mayo',
            'Junio',
            'Julio',
            'Agosto',
            'Septiembre',
            'Octubre',
            'Noviembre',
            'Diciembre'
        ];

        $mdDateLocaleProvider.shortMonths = [
            'Ene',
            'Feb',
            'Mar',
            'Abr',
            'May',
            'Jun',
            'Jul',
            'Ago',
            'Sep',
            'Oct',
            'Nov',
            'Dic'
        ];
        $mdDateLocaleProvider.days = [
            'Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'
        ];
        $mdDateLocaleProvider.shortDays = [
            'Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'
        ];

        $mdDateLocaleProvider.msgCalendar = 'Calendario';
        $mdDateLocaleProvider.msgOpenCalendar = 'Abre el calendario';

        // Example uses moment.js to parse and format dates.
        $mdDateLocaleProvider.parseDate = function (dateString) {
            moment.lang("es").format('L');
            var m = moment(dateString, 'L', true);
            return m.isValid() ? m.toDate() : new Date(NaN);
        };

        $mdDateLocaleProvider.formatDate = function (date) {
            var m = moment(date);
            return m.isValid() ? m.format('L') : '';
        };

        //$mdDateLocaleProvider.monthHeaderFormatter = function (date) {
        //    return myShortMonths[date.getMonth()] + ' ' + date.getFullYear();
        //};
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

    //AGREGAR HORA A LA FECHA
    Date.prototype.addHours = function (hours) {
        this.setHours(this.getHours() + hours);
        return this;
    }
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------