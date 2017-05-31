//ENCABEZADO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('encabezadoController', function ($scope, $firebaseAuth, $firebaseObject, $location) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        $scope.displayName = usuario.displayName;

        //BUSQUEDA DE FOTO EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refObjeto;
            var objeto;
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    break;
                case 'cliente':
                    refObjeto = firebase.database().ref('clientes').child(firebaseUsuario.$id).child('cliente');
                    objeto = $firebaseObject(refObjeto);
                    $scope.redirect = '#/CHAOL/Clientes/' + usuario.uid;
                    break;
                case 'transportista':
                    refObjeto = firebase.database().ref('transportistas').child(firebaseUsuario.$id).child('transportista');
                    objeto = $firebaseObject(refObjeto);
                    $scope.redirect = '#/CHAOL/Transportistas/' + usuario.uid;
                    break;
                case 'chofer':
                    refObjeto = firebase.database().ref('choferes').child(firebaseUsuario.$id);
                    objeto = $firebaseObject(refObjeto);
                    $scope.redirect = '#/CHAOL/Choferes/' + usuario.uid;
                    break;
                default:
            }
            if (objeto) {
                objeto.$loaded().then(function () {
                    if (usuario.photoURL) {
                        $scope.src = usuario.photoURL;
                    }
                    else {
                        $scope.src = objeto.imagenURL;
                    }
                });
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------