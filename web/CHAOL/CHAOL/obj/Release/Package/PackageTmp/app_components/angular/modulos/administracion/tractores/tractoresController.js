//LISTADO TRACTOR CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('tractoresController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refTractores;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    refTractores = firebase.database().ref().child('transportistas');
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            transportistas = childSnapshot.val();
                            if (transportistas.transportista.estatus !== 'eliminado') {
                                childSnapshot.forEach(function (tractorSnapshot) {
                                    if (tractorSnapshot.key === "tractores") {
                                        tractorSnapshot.forEach(function (tractorChildSnapshot) {
                                            var tractor = tractorChildSnapshot.val();
                                            if (tractor.estatus !== 'eliminado') {
                                                arrayTractores.push(tractor);
                                            }
                                        });
                                    }
                                })
                            }
                        });
                        $scope.tractores = arrayTractores;
                    });
                    break;
                case "transportista":
                    refTractores = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "tractores") {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    var tractor = bodegaSnapshot.val()
                                    if (tractor.estatus !== 'eliminado') {
                                        arrayTractores.push(tractor);
                                    }
                                })
                            }
                        });
                        $scope.tractores = arrayTractores;
                    });
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.eliminar = function (tractor) {
            //NODO CLIENTES
            var refTractor = firebase.database().ref('transportistas').child(tractor.firebaseIdDelTransportista).child('tractores').child(tractor.firebaseId);
            var firebaseTractor = $firebaseObject(refTractor);
            //CARGA DEL CLIENTE
            firebaseTractor.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#tractores')))
                    .title('¿Eliminar tractor?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseTractor.marca + ' - ' + firebaseTractor.modelo + ' - ' + firebaseTractor.placa + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseTractor.estatus = "eliminado";
                    firebaseTractor.$save().then(function () {
                        console.log('Estatus changed to deleted');
                    }).catch(function (error) {
                        console.log(error);
                    });
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
