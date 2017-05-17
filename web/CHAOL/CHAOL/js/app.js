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

    app.run(function ($rootScope, $location) {
        $rootScope.$on('$routeChangeError', function (event, next, previous, error) {
            if (error === 'AUTH_REQUIRED') {
                console.log('No cuenta con los permisos suficientes');
                $location.path('#/Inicio');
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

    app.factory("Auth", function ($firebaseAuth) {
        return $firebaseAuth();
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//COMPONENTS
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.component('encabezado', {
        templateUrl: "/header.html"
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

    app.controller('loginController', function ($scope, $location, $firebaseObject, $firebaseAuth, $mdDialog) {
        $scope.auth = $firebaseAuth();
        $scope.auth.$signOut();

        //ENVIO AL FORMULARIO DE REGISTRO
        $scope.registro = function () {
            $location.path("/Registro");
        };

        //INICIO DE SESIÓN
        $scope.iniciarSesion = function () {
            $scope.auth = $firebaseAuth();
            $scope.auth.$signInWithEmailAndPassword($scope.usuario.usuario, $scope.usuario.contrasena).then(function () {
                console.log($scope.usuario.usuario + ' logged in');
                var user = $scope.auth.$getAuth();
                var refUsuario = firebase.database().ref('usuarios').child(user.uid);
                var usuario = $firebaseObject(refUsuario);
                usuario.$loaded(function () {
                    if (usuario.$value === 'administrador') {
                        console.log('Admin');
                    }
                }).catch(function (error) {
                    console.log(error);
                });
                $location.path("/CHAOL");
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
            $scope.auth = $firebaseAuth();
            var usuario = $scope.auth.$getAuth();
            //SE CREA EL USUARIO
            $scope.auth.$createUserWithEmailAndPassword($scope.firebaseCliente.cliente.correoElectronico, $scope.firebaseCliente.cliente.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.firebaseCliente.cliente.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto_cliente").files[0];
                if (archivo) {
                    var nombreArchivo = usuario.uid + '.jpg';
                    var refAlmacenamiento = firebase.storage().ref().child('FotosDePerfil/' + nombreArchivo);
                    var cargar = refAlmacenamiento.put(archivo);
                    console.log('Image loaded');
                    $scope.firebaseCliente.cliente.imagenURL = nombreArchivo;
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
                refUsuarios.child(usuario.uid).set('cliente').then(function () {
                    console.log('User added in DB.');
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                $scope.auth.$signOut();
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
            $scope.auth = $firebaseAuth();
            var usuario = $scope.auth.$getAuth();
            //SE CREA EL USUARIO
            $scope.auth.$createUserWithEmailAndPassword($scope.firebaseTransportista.transportista.correoElectronico, $scope.firebaseTransportista.transportista.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.firebaseTransportista.transportista.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto_transportista").files[0];
                if (archivo) {
                    var nombreArchivo = usuario.uid + '.jpg';
                    var refAlmacenamiento = firebase.storage().ref().child('FotosDePerfil/' + nombreArchivo);
                    var cargar = refAlmacenamiento.put(archivo);
                    console.log('Image loaded');
                    $scope.firebaseTransportista.transportista.imagenURL = nombreArchivo;
                }
                else {
                    console.log('Image not selected');
                }

                //CREACIÓN DE TRANSPORTISTA EN BD
                refTransportista.child(usuario.uid).child('transportista').set($scope.firebaseTransportista.transportista);
                refTransportista.child(usuario.uid).child('transportista').child('contrasena').set(null);

                //CREACIÓN DE PERFIL
                usuario.updateProfile({
                    displayName: $scope.firebaseTransportista.transportista.nombre
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
                refUsuarios.child(usuario.uid).set('transportista').then(function () {
                    console.log('User added in DB.');
                });

                //CREACIÓN DE TRANSPORTISTA EN LISTADO DE TRANSPORTISTAS
                var refListadoTransportista = firebase.database().ref('listaDeTransportistas');
                refListadoTransportista.child(usuario.uid).set($scope.firebaseTransportista.transportista.nombre).then(function () {
                    console.log('Added in list.');
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                $scope.auth.$signOut();
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
            chofer: {
                nombre: "",
                empresaTransportista: "",
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
        };

        //LISTADO TRANSPORTISTAS
        var refTransportistas = firebase.database().ref().child('listaDeTransportistas').orderByValue();
        $scope.empresasTransportista = $firebaseArray(refTransportistas);
        refTransportistas.on('value', function (snap) {
            $scope.empresasTransportista.$value = snap.key;
        });

        //GUARDAR TRANSPORTISTA
        var refChofer = firebase.database().ref().child('choferes');

        $scope.registrarChofer = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            $scope.auth = $firebaseAuth();
            var usuario = $scope.auth.$getAuth();
            //SE CREA EL USUARIO
            $scope.auth.$createUserWithEmailAndPassword($scope.firebaseChofer.chofer.correoElectronico, $scope.firebaseChofer.chofer.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.firebaseChofer.chofer.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto_chofer").files[0];
                if (archivo) {
                    var nombreArchivo = usuario.uid + '.jpg';
                    var refAlmacenamiento = firebase.storage().ref().child('FotosDePerfil/' + nombreArchivo);
                    var cargar = refAlmacenamiento.put(archivo);
                    console.log('Image loaded');
                    $scope.firebaseChofer.chofer.imagenURL = nombreArchivo;
                }
                else {
                    console.log('Image not selected');
                }

                //CREACIÓN DE CHOFER EN BD
                refChofer.child(usuario.uid).set($scope.firebaseChofer.chofer);
                refChofer.child(usuario.uid).child('contrasena').set(null);

                //CREACIÓN DE PERFIL
                usuario.updateProfile({
                    displayName: $scope.firebaseChofer.chofer.nombre
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
                refUsuarios.child(usuario.uid).set('chofer').then(function () {
                    console.log('User added in DB.');
                });

                //CREAR CHOFER EN TRANSPORTISTA
                var refTransportista = firebase.database().ref('transportistas').child($scope.firebaseChofer.chofer.empresaTransportista).child('choferes');
                refTransportista.child(usuario.uid).set($scope.firebaseChofer.chofer).then(function () {
                    console.log('Client added in Transportist');
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                $scope.auth.$signOut();
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
                            .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseChofer.chofer.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/> <p>¿<b>' + $scope.firebaseChofer.chofer.correoElectronico + '</b> es tu cuenta de correo?<br/> Recupera tu contraseña <a href="#/RecuperarContrasena"><i>aquí</i><a/></p>')
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
            $scope.auth = $firebaseAuth();
            //RESET DE CONTRASEÑA
            $scope.auth.$sendPasswordResetEmail($scope.usuario.usuario).then(function () {
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

    app.controller('menuController', function ($scope, $timeout, $mdSidenav) {
        $scope.toggleLeft = buildToggler('left');
        $scope.toggleRight = buildToggler('right');

        $scope.cerrarSesion = function () {
            $scope.auth = $firebaseAuth();
            $scope.auth.$signOut();
        }

        function buildToggler(componentId) {
            return function () {
                $mdSidenav(componentId).toggle();
            };
        }
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//INICIO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('inicioController', function ($scope, $mdSidenav, $firebaseAuth) {
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//CLIENTES
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('clientesController', function ($scope, $firebaseArray, $firebaseObject) {
        var refClientes = firebase.database().ref().child('clientes').orderByChild('cliente/nombre');
        $scope.clientes = $firebaseArray(refClientes);
        refClientes.on('value', function (snap) {
            $scope.clientes.$value = snap.key;
        });

        $scope.cambiarEstatus = function (cliente) {
            //NODO CLIENTES
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            //CARGA DEL CLIENTE
            firebaseCliente.$loaded().then(function () {
                firebaseCliente.estatus = cliente.estatus;
                firebaseCliente.$save().then(function () {
                    console.log('Estatus changed');
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (cliente) {
            //NODO CLIENTE
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            firebaseCliente.$loaded().then(function () {
                var refTipoUsuario = firebase.database().ref('usaurios').child(firebaseCliente.firebaseId);
                var tipoUsuario = $firebaseObject(refTipoUsuario);
                tipoUsuario.$loaded(function () {
                    tipoUsuario.$remove().then(function () {
                        console.log('User type removed');
                    }).catch(function (error) {
                        console.log(error);
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            }).catch(error);
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
