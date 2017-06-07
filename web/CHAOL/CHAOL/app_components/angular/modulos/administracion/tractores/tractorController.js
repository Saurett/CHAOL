//REGISTRO TRACTOR CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('tractorController', function ($scope, $location, $firebaseObject, $firebaseArray, unixTime, $firebaseAuth, $mdDialog, $firebaseStorage, createUserService, $routeParams) {
        //INICIALIZAR TRACTOR
        $scope.firebaseTractor = {
            empresaTransportista: "",
            marca: "",
            modelo: "",
            numeroDeSerie: "",
            numeroEconomico: "",
            placa: "",
            estatus: "libre",
            fechaDeCreacion: unixTime(),
            fechaDeEdicion: unixTime(),
            firebaseId: "",
            firebaseIdDelTransportista: ""
        }

        var refUsuarios = firebase.database().ref().child('usuarios');
        var refTransportista = firebase.database().ref().child('transportistas');

        //LISTADO TRANSPORTISTAS
        var refTransportistas = firebase.database().ref().child('transportistas');
        refTransportistas.on("value", function (snapshot) {
            var arrayTractores  = [];
            snapshot.forEach(function (childSnapshot) {
                transportistas = childSnapshot.val();
                if (transportistas.transportista.estatus !== 'eliminado') {
                    childSnapshot.forEach(function (transportistaSnapShot) {
                        if (transportistaSnapShot.key === 'transportista') {
                            var transportista = transportistaSnapShot.val();
                            arrayTractores.push(transportista);
                        }
                    })
                }
            });
            $scope.empresasTransportista = arrayTractores;
        });

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    $scope.administrador = true;
                    break;
                case "transportista":
                    $scope.transportista = true;
                    var refTransportistaUsuario = firebase.database().ref('transportistas').child(usuario.uid).child('transportista');
                    var firebaseTransportistaUsuario = $firebaseObject(refTransportistaUsuario);
                    firebaseTransportistaUsuario.$loaded().then(function () {
                        $scope.firebaseTractor.empresaTransportista = firebaseTransportistaUsuario.nombre;
                    })
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //TRACTOR
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CONSULTAR TRACTOR
        if ($routeParams.ID) {
            var refTractores = firebase.database().ref().child('transportistas');
            refTractores.on("value", function (snapshot) {
                snapshot.forEach(function (childSnapshot) {
                    childSnapshot.forEach(function (tractorSnapshot) {
                        if (tractorSnapshot.key === "tractores") {
                            tractorSnapshot.forEach(function (tractorChildSnapshot) {
                                var tractor = tractorChildSnapshot.val();
                                if (tractor.firebaseId === $routeParams.ID) {
                                    $scope.firebaseTractor = tractor;
                                }
                            });
                        }
                    })
                });
            });
        }

        //GUARDAR TRACTOR
        $scope.registrarTractor = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';

            //ACTUALIZACIÓN DE CLIENTE EN BD
            var actualizarTractorBD = function (tractor) {
                var objetoTractor = $firebaseObject(refTransportista.child(tractor.firebaseIdDelTransportista).child('tractores').child(tractor.firebaseId));
                objetoTractor.empresaTransportista = tractor.empresaTransportista;
                objetoTractor.marca = tractor.marca;
                objetoTractor.modelo = tractor.modelo;
                objetoTractor.numeroDeSerie = tractor.numeroDeSerie;
                objetoTractor.numeroEconomico = tractor.numeroEconomico;
                objetoTractor.placa = tractor.placa;
                objetoTractor.idGPS = tractor.idGPS;
                objetoTractor.fechaDeCreacion = tractor.fechaDeCreacion;
                objetoTractor.fechaDeEdicion = unixTime();
                objetoTractor.firebaseId = tractor.firebaseId;
                objetoTractor.firebaseIdDelTransportista = tractor.firebaseIdDelTransportista;
                objetoTractor.estatus = 'libre';

                objetoTractor.$save().then(function () {
                    console.log('Store updated');
                })
            }

            //CREACIÓN DE TRACTOR EN BD
            var crearTractorBD = function (tractor) {
                var refTransportistas = firebase.database().ref().child('transportistas');
                refTransportistas.once("value", function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        transportistas = childSnapshot.val();
                        if (transportistas.transportista.nombre === $scope.firebaseTractor.empresaTransportista) {
                            tractor.firebaseIdDelTransportista = transportistas.transportista.firebaseId;
                            var firebaseTractor = refTransportista.child(tractor.firebaseIdDelTransportista).child('tractores').push();
                            tractor.firebaseId = firebaseTractor.key;
                            $scope.firebaseTractor = tractor;
                            firebaseTractor.set(tractor);
                            console.log('Store created in DB');
                        }
                    });
                });

            }

            //ALERTA
            var alerta = function (mensaje, url) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#tractor')))
                        .clickOutsideToClose(false)
                        .title('Registro correcto')
                        .htmlContent(mensaje)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path(url);
                });
            }

            //CREACIÓN DE LA TRACTOR
            if (!$scope.firebaseTractor.firebaseId) {
                crearTractorBD($scope.firebaseTractor);
                alerta('<br/> <p>Tractor registrado. </p> <p> A partir de este momento, puede utilizar este tractor.</p>', '/CHAOL/Tractores');
                //CERRAR LA SESIÓN CREADA Y OCULTAR PROGRESS
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
            //ACTUALIZACIÓN DE LA TRACTOR
            else {
                actualizarTractorBD($scope.firebaseTractor);
                alerta('<br/> <p>Tractor actualizado. </p> <p> Hemos actualizado la información de <b>' + $scope.firebaseTractor.marca + ' - ' + $scope.firebaseTractor.modelo + ' - ' + $scope.firebaseTractor.placa + '</b> exitosamente.</p>', '/CHAOL/Tractores');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------