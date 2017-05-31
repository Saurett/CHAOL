//INICIO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('inicioController', function ($scope, $mdSidenav, $firebaseAuth, $firebaseObject) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ESTATUS MENU
        $scope.administracion = {
            clientes: false,
            transportistas: false,
            choferes: false,
            tractores: false,
            remolques: false
        }

        $scope.fletes = {
            agenda: false
        }

        $scope.cuenta = {
            miPerfil: false,
            cerrarSesion: false
        }

        //BUSQUEDA DE FOTO EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    $scope.inicio = true;
                    $scope.administracion.clientes = true;
                    $scope.administracion.transportistas = true;
                    $scope.administracion.choferes = true;
                    $scope.administracion.tractores = true;
                    $scope.administracion.remolques = true;
                    $scope.fletes.agenda = true;
                    $scope.cuenta.miPerfil = false;
                    $scope.cuenta.cerrarSesion = true;
                    break;
                case 'cliente':
                    $scope.inicio = true;
                    $scope.administracion = false;
                    $scope.fletes.agenda = true;
                    $scope.cuenta.miPerfil = true;
                    $scope.cuenta.cerrarSesion = true;
                    break;
                case 'transportista':
                    $scope.inicio = true;
                    $scope.administracion.clientes = false;
                    $scope.administracion.transportistas = false;
                    $scope.administracion.choferes = true;
                    $scope.administracion.tractores = true;
                    $scope.administracion.remolques = true;
                    $scope.fletes.agenda = true;
                    $scope.cuenta.miPerfil = true;
                    $scope.cuenta.cerrarSesion = true;
                    break;
                case 'chofer':
                    $scope.inicio = true;
                    $scope.administracion = false;
                    $scope.fletes.agenda = true;
                    $scope.cuenta.miPerfil = true;
                    $scope.cuenta.cerrarSesion = true;
                    break;
                default:
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------