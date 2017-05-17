//CLIENTES
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('clientesController', function ($scope, $firebaseArray, $firebaseObject) {
        var refClientes = firebase.database().ref().child('clientes').orderByChild('cliente/nombre');
        $scope.clientes = $firebaseArray(refClientes);
        refClientes.on('value', function (snap) {
            $scope.clientes.$value = snap.key;
        });

        $scope.cambiarEstatus = function (cliente) {
            //NODO CLIENTES
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            //CARGA DEL CLIENTE
            firebaseCliente.$loaded().then(function () {
                firebaseCliente.estatus = cliente.estatus;
                firebaseCliente.$save().then(function () {
                    console.log('Estatus changed');
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (cliente) {
            //NODO CLIENTE
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            firebaseCliente.$loaded().then(function () {
                var refTipoUsuario = firebase.database().ref('usaurios').child(firebaseCliente.firebaseId);
                var tipoUsuario = $firebaseObject(refTipoUsuario);
                tipoUsuario.$loaded(function () {
                    tipoUsuario.$remove().then(function () {
                        console.log('User type removed');
                    }).catch(function (error) {
                        console.log(error);
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            }).catch(error);
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
