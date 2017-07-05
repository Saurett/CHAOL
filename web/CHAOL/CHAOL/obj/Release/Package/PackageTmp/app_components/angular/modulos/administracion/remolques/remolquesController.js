//LISTADO REMOLQUES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('remolquesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refRemolques;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'colaborador':
                case 'administrador':
                    refRemolques = firebase.database().ref().child('transportistas');
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            transportistas = childSnapshot.val();
                            if (transportistas.transportista.estatus !== 'eliminado') {
                                childSnapshot.forEach(function (remolqueSnapshot) {
                                    if (remolqueSnapshot.key === "remolques") {
                                        remolqueSnapshot.forEach(function (remolqueChildSnapshot) {
                                            var remolque = remolqueChildSnapshot.val();
                                            if (remolque.estatus !== 'eliminado') {
                                                arrayRemolques.push(remolque);
                                            }
                                        });
                                    }
                                })
                            }
                        });
                        $scope.remolques = arrayRemolques;
                    });
                    break;
                case "transportista":
                    refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "remolques") {
                                childSnapshot.forEach(function (remolqueSnapshot) {
                                    var remolque = remolqueSnapshot.val()
                                    if (remolque.estatus !== 'eliminado') {
                                        arrayRemolques.push(remolque);
                                    }
                                })
                            }
                        });
                        $scope.remolques = arrayRemolques;
                    });
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.eliminar = function (remolque) {
            //NODO CLIENTES
            var refRemolque = firebase.database().ref('transportistas').child(remolque.firebaseIdDelTransportista).child('remolques').child(remolque.firebaseId);
            var firebaseRemolque = $firebaseObject(refRemolque);
            //CARGA DEL CLIENTE
            firebaseRemolque.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#tractores')))
                    .title('¿Eliminar remolque?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseRemolque.marca + ' - ' + firebaseRemolque.modelo + ' - ' + firebaseRemolque.placa + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseRemolque.estatus = "eliminado";
                    firebaseRemolque.$save().then(function () {
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
