(function () {
    var app = angular.module('app');

    app.controller('registroController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage) {
        var usuariosRef = firebase.database().ref().child('usuarios');
        $scope.progress = false;

        //CLIENTE
        //INICIALIZAR CLIENTE
        $scope.objetoCliente = {
            cliente: {
                nombre: "",
                rfc: "",
                numeroInterior: "",
                numeroExterior: "",
                calle: "",
                colonia: "",
                ciudad: "",
                estado: "",
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
        }

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
        var clienteRef = firebase.database().ref().child('clientes');

        $scope.registrarCliente = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            $scope.usuarioCliente = $firebaseAuth();
            var usuario = $scope.usuarioCliente.$getAuth();
            //SE CREA EL USUARIO
            $scope.usuarioCliente.$createUserWithEmailAndPassword($scope.objetoCliente.cliente.correoElectronico, $scope.objetoCliente.cliente.contrasena).then(function (usuario) {
                console.log('User created.');
                $scope.objetoCliente.cliente.firebaseId = usuario.uid;

                //CARGA IMAGEN DE PERFIL
                var archivo = document.getElementById("input_foto").files[0];
                var nombreArchivo = usuario.uid + 'jpg';
                var almacenamiento = firebase.storage().ref().child('FotosDePerfil/' + nombreArchivo);
                var cargar = almacenamiento.put(archivo);
                console.log('Image loaded');
                $scope.objetoCliente.cliente.imagenURL = nombreArchivo;

                //CREACIÓN DE CLIENTE EN BD
                clienteRef.child(usuario.uid).child('cliente').set($scope.objetoCliente.cliente);
                clienteRef.child(usuario.uid).child('cliente').child('contrasena').set(null);

                //CREACIÓN DE PERFIL
                usuario.updateProfile({
                    displayName: $scope.objetoCliente.cliente.nombre
                }).then(function () {
                    //ENVÍO DE CORREO
                    usuario.sendEmailVerification().then(function () {
                        console.log('Email sent')
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
                console.log('Client created.');

                //CREAR USUARIO EN DB
                usuariosRef.child(usuario.uid).set('cliente').then(function () {
                    console.log('User added in DB.');
                });

                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                $scope.usuarioCliente.$signOut();
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
                if (error.code === 'auth/email-already-in-use') {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#registro')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/> <p>Al parecer ' + $scope.objetoCliente.cliente.correoElectronico + ' ya se encuentra registrado. </p> <p>Por favor, intenta con un correo electrónico diferente.</p> <br/> <p>¿<b>' + $scope.objetoCliente.cliente.correoElectronico + '</b> es tu cuenta de correo?<br/> Recupera tu contraseña <a href="#/RecuperarContrasena"><i>aquí</i><a/></p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
            });
        };

        $scope.guardarFoto = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var imagen = document.getElementById('input_foto').files; // FileList object
            if (imagen[0].type.match('image.*')) {
                var reader = new FileReader();
                reader.onload = (function (archivo) {
                    return function (e) {
                        document.getElementById('img_perfil').src = e.target.result;
                        document.getElementById('img_perfil').className = 'imagen-perfil';
                        document.getElementById('i_perfil').style.display = 'none';
                    };
                })(imagen[0]);
                reader.readAsDataURL(imagen[0]);
            }
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        };
    });
})();
