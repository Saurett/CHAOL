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