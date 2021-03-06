﻿//LISTADO CLIENTES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('clientesController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //CLIENTES
        var refClientes = firebase.database().ref().child('clientes');
        refClientes.on("value", function (snapshot) {
            var arrayClientes = [];
            snapshot.forEach(function (childSnapshot) {
                clientes = childSnapshot.val();
                if (clientes.cliente.estatus !== 'eliminado') {
                    arrayClientes.push(clientes);
                }
            });
            $scope.clientes = arrayClientes;
        });

        $scope.cambiarEstatus = function (cliente) {
            //NODO CLIENTES
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            //CARGA DEL CLIENTE
            firebaseCliente.$loaded().then(function () {
                firebaseCliente.estatus = cliente.estatus;
                firebaseCliente.$save().then(function () {
                    console.log('Estatus changed to ' + cliente.estatus);
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (cliente) {
            //NODO CLIENTES
            var refCliente = firebase.database().ref('clientes').child(cliente.firebaseId).child('cliente');
            var firebaseCliente = $firebaseObject(refCliente);
            //CARGA DEL CLIENTE
            firebaseCliente.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#clientes')))
                    .title('¿Eliminar cliente?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseCliente.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL CLIENTE
                    firebaseCliente.estatus = "eliminado";
                    firebaseCliente.$save().then(function () {
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
