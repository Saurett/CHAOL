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
                return refUsuarios.child(transportista.firebaseId).set('transportista');
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
                            crearTransportistaBD(transportista);
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
                        crearTransportistaBD($scope.firebaseTransportista.transportista);
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