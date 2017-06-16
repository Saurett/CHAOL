//LISTADO BODEGAS CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('bodegasController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refBodegas;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    refBodegas = firebase.database().ref().child('clientes');
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            clientes = childSnapshot.val();
                            if (clientes.cliente.estatus !== 'eliminado') {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    if (bodegaSnapshot.key === "bodegas") {
                                        bodegaSnapshot.forEach(function (bodegaChildSnapshot) {
                                            var bodega = bodegaChildSnapshot.val();
                                            if (bodega.estatus !== 'eliminado') {
                                                arrayBodegas.push(bodega);
                                            }
                                        });
                                    }
                                })
                            }
                        });
                        $scope.bodegas = arrayBodegas;
                    });
                    break;
                case "cliente":
                    refBodegas = firebase.database().ref().child('clientes').child(usuario.uid);
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "bodegas") {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    var bodega = bodegaSnapshot.val()
                                    if (bodega.estatus !== 'eliminado') {
                                        arrayBodegas.push(bodega);
                                    }
                                })
                            }
                        });
                        $scope.bodegas = arrayBodegas;
                    });
                    break;
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.eliminar = function (bodega) {
            //NODO CLIENTES
            var refBodega = firebase.database().ref('clientes').child(bodega.firebaseIdDelCliente).child('bodegas').child(bodega.firebaseIdBodega);
            var firebaseBodega = $firebaseObject(refBodega);
            //CARGA DEL CLIENTE
            firebaseBodega.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#bodegas')))
                    .title('¿Eliminar bodega?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseBodega.nombreDeLaBodega + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseBodega.estatus = "eliminado";
                    firebaseBodega.$save().then(function () {
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
