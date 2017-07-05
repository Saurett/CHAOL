//REGISTRO CHOFER CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('colaboradorController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        //INICIALIZAR CHOFER
        $scope.firebaseColaborador = {
            nombre: '',
            correoElectronico: '',
            estatus: 'inactivo',
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            firebaseId: '',
            tipoDeUsuario: 'colaborador'
        }

        var refUsuarios = firebase.database().ref().child('usuarios');
        var refColaborador = firebase.database().ref().child('administradores');

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
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //COLABORADOR
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CONSULTAR COLABORADOR
        if ($routeParams.ID) {
            var colaborador = $firebaseObject(refChofer.child($routeParams.ID));
            colaborador.$loaded().then(function () {
                $scope.firebaseColaborador = chofer;
                console.log('Colaborador found');
            }).catch(function (error) {
                console.log(error);
            });
        }
        else {
            $scope.ownUser = true;
        }

        //GUARDAR COLABORADOR
        $scope.registrarColaborador = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var usuarioColaborador;

            //CREAR USUARIO EN BASE DE DATOS
            var crearUsuarioBD = function (chofer) {
                return refUsuarios.child(chofer.firebaseId).child('tipoDeUsuario').set('colaborador');
            }

            //ACTUALIZACIÓN DE CHOFER EN BD
            var actualizarColaboradorBD = function (colaborador) {
                var objetoColaborador = $firebaseObject(refColaborador.child(colaborador.firebaseId));
                objetoColaborador.nombre = colaborador.nombre;
                objetoColaborador.correoElectronico = colaborador.correoElectronico;
                objetoColaborador.fechaDeCreacion = colaborador.fechaDeCreacion;
                objetoColaborador.fechaDeEdicion = unixTime();
                objetoColaborador.estatus = colaborador.estatus;
                objetoColaborador.firebaseId = colaborador.firebaseId;

                objetoColaborador.$save().then(function () {
                    console.log('Colaborador updated');
                });
            }

            //CREACIÓN DE COLABORADOR EN BD
            var crearColaboradorBD = function (colaborador) {
                refColaborador.child(colaborador.firebaseId).set(colaborador);
                refColaborador.child(colaborador.firebaseId).child('contrasena').set(null);
                console.log('Colaborador created in DB');
            }

            //ENVIAR CORREO
            var enviarCorreoConfirmacion = function (usuarioColaborador) {
                return usuarioColaborador.sendEmailVerification();
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
                var archivo = document.getElementById("input_foto_colaborador").files[0];
                if (archivo) {
                    var nombreArchivo = chofer.firebaseId + '.jpg';
                    var refAlmacenamiento = firebase.storage();
                    var refPath = refAlmacenamiento.ref('FotosDePerfil/' + nombreArchivo);
                    var cargar = refPath.put(archivo).then(function () {
                        console.log('Image loaded');
                        refPath.getDownloadURL().then(function (url) {
                            //CREACIÓN DE COLABORADOR EN BD
                            console.log('Colaborador image loaded to ' + url)
                            colaborador.imagenURL = url;
                            actualizarColaboradorBD(colaborador);
                            actualizarPerfil(usuarioChofer, colaborador);
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
            var actualizarPerfil = function (usuarioColaborador, colaborador) {
                usuarioColaborador.updateProfile({
                    displayName: colaborador.nombre,
                    photoURL: colaborador.imagenURL
                })
                console.log('Profile updated');
            }

            //CREACIÓN DEL COLABORADOR
            if (!$scope.firebaseColaborador.firebaseId) {
                //SE CREA EL USUARIO
                createUserService.auth().createUserWithEmailAndPassword($scope.firebaseColaborador.correoElectronico, $scope.firebaseColaborador.contrasena).then(function (user) {
                    console.log('User created.');
                    usuarioColaborador = user;
                    $scope.firebaseColaborador.firebaseId = user.uid;

                    crearUsuarioBD($scope.firebaseColaborador).then(function () {
                        console.log('User created in DB');
                        crearColaboradorBD($scope.firebaseColaborador);
                        enviarCorreoConfirmacion(usuarioColaborador).then(function () {
                            console.log('Email sent');
                        }).catch(function (error) {
                            console.log(error);
                        });
                        cargarImagen($scope.firebaseColaborador);
                        actualizarPerfil(usuarioColaborador, $scope.firebaseColaborador);
                        alerta('<br/> <p>Colaborador registrado. </p> <p> Hemos enviado un mensaje a su cuenta de correo electrónico. Es necesario que la cuenta sea validada desde el enlace que compartimos.</p>', '/CHAOL/Colaboradores');
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
                                .htmlContent('<br/> <p>Al parecer ' + $scope.firebaseColaborador.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/>')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar')
                        );
                    }
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                });
            }
            //ACTUALIZACIÓN DEL COLABORADOR
            else {
                //VALIDAR SI SE TRATA DEL USUARIO CORRECTO
                if (usuario.uid === $scope.firebaseColaborador.firebaseId) {
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
                            auth.$updateEmail($scope.firebaseColaborador.correoElectronico).then(function () {
                                console.log('Email updated');
                                //ACTUALIZAR CONTRASEÑA
                                auth.$updatePassword($scope.firebaseColaborador.contrasena).then(function () {
                                    actualizarChoferBD($scope.firebaseColaborador);
                                    cargarImagen($scope.firebaseColaborador);
                                    auth.$signInWithEmailAndPassword($scope.firebaseColaborador.correoElectronico, result).then(function () {
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
                    actualizarColaboradorBD($scope.firebaseColaborador);
                    cargarImagen($scope.firebaseColaborador);
                    alerta('<br/> <p>Chofer actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseColaborador.nombre + '</b> exitosamente.</p>', '/CHAOL/Colaboradores');
                }
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };

        //CARGA DE FOTO DE CHOFER
        $scope.guardarFotoColaborador = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto_colaborador').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil_colaborador').src = e.target.result;
                        document.getElementById('img_perfil_colaborador').className = 'imagen-perfil';
                        document.getElementById('i_perfil_colaborador').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------