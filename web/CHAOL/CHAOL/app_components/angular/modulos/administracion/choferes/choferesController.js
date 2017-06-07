//LISTADO CHOFERES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('choferesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refChoferes;
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    var refChoferes = firebase.database().ref().child('choferes').orderByChild('nombre');
                    break;
                case "transportista":
                    var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes').orderByChild('nombre');
                    break;
            }

            refChoferes.on("value", function (snapshot) {
                var arrayChoferes = [];
                snapshot.forEach(function (childSnapshot) {
                    choferes = childSnapshot.val();
                    if (choferes.estatus !== 'eliminado') {
                        arrayChoferes.push(choferes);
                    }
                });
                $scope.choferes = arrayChoferes;
            });
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.cambiarEstatus = function (chofer) {
            //NODO CHOFERES
            var refChofer = firebase.database().ref('choferes').child(chofer.firebaseId);
            var firebaseChofer = $firebaseObject(refChofer);
            //CARGA DEL CHOFER
            firebaseChofer.$loaded().then(function () {
                firebaseChofer.estatus = chofer.estatus;
                firebaseChofer.$save().then(function () {
                    console.log('Estatus changed to ' + chofer.estatus);
                    //CREAR CHOFER EN TRANSPORTISTA
                    var refTransportista = firebase.database().ref('transportistas');
                    refTransportista.once("value").then(function (snapshot) {
                        snapshot.forEach(function (childSnapshot) {
                            var transportista = childSnapshot.val();
                            if (transportista.transportista.nombre === chofer.empresaTransportista) {
                                var id = transportista.transportista.firebaseId;
                                var refTrans = firebase.database().ref('transportistas').child(id).child('choferes').child(chofer.firebaseId).child('estatus');
                                refTrans.set(chofer.estatus).then(function () {
                                    console.log('Client added in Transportist');
                                });
                            }
                        });
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
