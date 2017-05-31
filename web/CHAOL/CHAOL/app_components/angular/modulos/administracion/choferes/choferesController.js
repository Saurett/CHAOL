//LISTADO CHOFERES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('choferesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        var refChoferes = firebase.database().ref().child('choferes').orderByChild('nombre');
        $scope.choferes = $firebaseArray(refChoferes);
        refChoferes.on('value', function (snap) {
            $scope.choferes.$value = snap.key;
        });

        $scope.cambiarEstatus = function (chofer) {
            //NODO CHOFERES
            var refChofer = firebase.database().ref('choferes').child(chofer.firebaseId);
            var firebaseChofer = $firebaseObject(refChofer);
            //CARGA DEL CHOFER
            firebaseChofer.$loaded().then(function () {
                firebaseChofer.estatus = chofer.estatus;
                firebaseChofer.$save().then(function () {
                    console.log('Estatus changed to ' + chofer.estatus);
                    var refTransportistas = firebase.database().ref().child('transportistas');
                    refTransportistas.orderByChild('transportista/nombre').equalTo(chofer.empresaTransportista).on("child_added", function (snapshot) {
                        refTransportistas.child(snapshot.key).child('choferes').child(chofer.firebaseId).child('estatus').set(chofer.estatus);
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (chofer) {
            //NODO CHOFERES
            var refChofer = firebase.database().ref('choferes').child(chofer.firebaseId);
            var firebaseChofer = $firebaseObject(refChofer);
            //CARGA DEL CHOFER
            firebaseChofer.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#choferes')))
                    .title('¿Eliminar chofer?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseChofer.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CHOFER
                    firebaseChofer.estatus = "eliminado";
                    firebaseChofer.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });

                    var refTransportistas = firebase.database().ref().child('transportistas');
                    refTransportistas.orderByChild('transportista/nombre').equalTo(chofer.empresaTransportista).on("child_added", function (snapshot) {
                        refTransportistas.child(snapshot.key).child('choferes').child(chofer.firebaseId).child('estatus').set('eliminado');
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
