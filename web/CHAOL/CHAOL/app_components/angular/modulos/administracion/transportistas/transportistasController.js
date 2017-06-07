//LISTADO TRANSPORTISTAS CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('transportistasController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        var refTransportistas = firebase.database().ref().child('transportistas');
        refTransportistas.on("value", function (snapshot) {
            var arrayTransportistas = [];
            snapshot.forEach(function (childSnapshot) {
                transportistas = childSnapshot.val();
                if (transportistas.transportista.estatus !== 'eliminado') {
                    arrayTransportistas.push(transportistas);
                }
            });
            $scope.transportistas = arrayTransportistas;
        });

        $scope.cambiarEstatus = function (transportista) {
            //NODO transportistas
            var refTransportista = firebase.database().ref('transportistas').child(transportista.firebaseId).child('transportista');
            var firebaseTransportista = $firebaseObject(refTransportista);
            var refListaTransportista = firebase.database().ref('listaDeTransportistas').child(transportista.firebaseId);
            var firebaseListaTransportista = $firebaseObject(refListaTransportista);
            //CARGA DEL transportista
            firebaseTransportista.$loaded().then(function () {
                firebaseTransportista.estatus = transportista.estatus;
                firebaseTransportista.$save().then(function () {
                    console.log('Estatus changed to ' + transportista.estatus);
                    if (firebaseTransportista.estatus == 'activo') {
                        firebase.database().ref('listaDeTransportistas').child(transportista.firebaseId).set(transportista.nombre);
                    }
                    else {
                        firebaseListaTransportista.$remove().then(function () {
                            console.log('Transportist removed form list')
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (transportista) {
            //NODO transportistas
            var refTransportista = firebase.database().ref('transportistas').child(transportista.firebaseId).child('transportista');
            var firebaseTransportista = $firebaseObject(refTransportista);
            var refListaTransportista = firebase.database().ref('listaDeTransportistas').child(transportista.firebaseId);
            var firebaseListaTransportista = $firebaseObject(refListaTransportista);
            //CARGA DEL transportista
            firebaseTransportista.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#transportistas')))
                    .title('¿Eliminar transportista?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseTransportista.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL transportista
                    firebaseTransportista.estatus = "eliminado";
                    firebaseTransportista.$save().then(function () {
                        console.log('Estatus changed to deleted');
                        firebaseListaTransportista.$remove().then(function () {
                            console.log('Transportist removed form list')
                        }).catch(function (error) {
                            console.log(error);
                        });

                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
