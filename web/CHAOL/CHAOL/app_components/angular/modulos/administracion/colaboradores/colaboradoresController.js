//LISTADO COLABORADORES CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('colaboradoresController', function ($scope, $firebaseArray, $firebaseObject, $firebaseAuth, $mdDialog) {
        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ACTIVACIÓN DINÁMICA DE CONTROLES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            var refChoferes;
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    var refColaboradores = firebase.database().ref().child('administradores').orderByChild('nombre');
                    break;
            }

            refColaboradores.on("value", function (snapshot) {
                var arrayColaboradores = [];
                snapshot.forEach(function (childSnapshot) {
                    colaboradores = childSnapshot.val();
                    if (colaboradores.estatus !== 'eliminado' && colaboradores.tipoDeUsuario === 'colaborador') {
                        arrayColaboradores.push(colaboradores);
                    }
                });
                $scope.colaboradores = arrayColaboradores;
            });
        });
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        $scope.cambiarEstatus = function (colaborador) {
            //NODO COLABORADORES
            var refColaborador = firebase.database().ref('administradores').child(colaborador.firebaseId);
            var firebaseColaborador = $firebaseObject(refColaborador);
            //CARGA DEL COLABORADOR
            firebaseColaborador.$loaded().then(function () {
                firebaseColaborador.estatus = colaborador.estatus;
                firebaseColaborador.$save().then(function () {
                    console.log('Estatus changed to ' + colaborador.estatus);
                }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        $scope.eliminar = function (colaborador) {
            //NODO COLABORADORES
            var refColaborador = firebase.database().ref('administradores').child(colaborador.firebaseId);
            var firebaseColaborador = $firebaseObject(refColaborador);
            //CARGA DEL CHOFER
            firebaseColaborador.$loaded().then(function () {
                //MENSAJE DE CONFIRMACIÓN
                $mdDialog.show($mdDialog.confirm()
                    .parent(angular.element(document.querySelector('#colaboradores')))
                    .title('¿Eliminar colaborador?')
                    .htmlContent('<br/> <p>¿Estás seguro que deseas eliminar a <b>' + firebaseColaborador.nombre + '</b>?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Sí, deseo eliminarlo')
                    .cancel('No, prefiero conservarlo')
                ).then(function () {
                    //ELIMINACIÓN DEL COLABORADOR
                    firebaseColaborador.estatus = "eliminado";
                    firebaseColaborador.$save().then(function () {
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
