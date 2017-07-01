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
                    $scope.ownUser = true;
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
                    refTransportistas.child(chofer.firebaseIdDelTransportista).child('choferes').child(chofer.firebaseId).child('contrasena').set(null);
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