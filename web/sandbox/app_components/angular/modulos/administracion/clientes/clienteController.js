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