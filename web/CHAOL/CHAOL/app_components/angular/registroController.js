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
                refUsuarios.child(usuario.uid).set('cliente').then(function () {
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
                refUsuarios.child(usuario.uid).set('transportista').then(function () {
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
                            var refTransportista = firebase.database().ref('transportistas').child($scope.firebaseChofer.empresaTransportista).child('choferes');
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
                refUsuarios.child(usuario.uid).set('chofer').then(function () {
                    console.log('User added in DB.');
                });

                //CREAR CHOFER EN TRANSPORTISTA
                var refTransportista = firebase.database().ref('transportistas').child($scope.firebaseChofer.empresaTransportista).child('choferes');
                refTransportista.child(usuario.uid).set($scope.firebaseChofer).then(function () {
                    console.log('Client added in Transportist');
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