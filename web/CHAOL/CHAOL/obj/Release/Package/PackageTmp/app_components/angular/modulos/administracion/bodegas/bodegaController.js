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
                if (clientes.cliente.estatus !== 'eliminado' && clientes.cliente.estatus !== 'inactivo') {
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
                case 'colaborador':
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