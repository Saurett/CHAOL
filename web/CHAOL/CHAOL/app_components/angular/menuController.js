//MENU CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('menuController', function ($scope, $timeout, $mdSidenav, $firebaseAuth, $firebaseObject) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        $scope.displayName = usuario.displayName;

        $scope.toggleLeft = buildToggler('left');
        $scope.toggleRight = buildToggler('right');

        $scope.cerrarSesion = function () {
            auth.$signOut();
        }

        function buildToggler(componentId) {
            return function () {
                $mdSidenav(componentId).toggle();
            };
        }

        //ESTATUS MENU
        $scope.menu = {
            inicio: false,
            administracion: {
                clientes: false,
                transportistas: false,
                choferes: false,
                tractores: false,
                remolques: false
            },
            fletes: {
                agenda: false
            },
            cuenta: {
                miPerfil: false,
                href: "",
                cerrarSesion: false
            }
        }

        //BUSQUEDA DE FOTO EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    $scope.menu.inicio = true;
                    $scope.menu.administracion.clientes = true;
                    $scope.menu.administracion.transportistas = true;
                    $scope.menu.administracion.choferes = true;
                    $scope.menu.administracion.tractores = true;
                    $scope.menu.administracion.remolques = true;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = false;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                case 'cliente':
                    $scope.menu.cuenta.href = '#/CHAOL/Clientes/' + usuario.uid;
                    $scope.menu.inicio = true;
                    $scope.menu.administracion = false;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = true;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                case 'transportista':
                    $scope.menu.cuenta.href = '#/CHAOL/Transportistas/' + usuario.uid;
                    $scope.menu.inicio = true;
                    $scope.menu.administracion.clientes = false;
                    $scope.menu.administracion.transportistas = false;
                    $scope.menu.administracion.choferes = true;
                    $scope.menu.administracion.tractores = true;
                    $scope.menu.administracion.remolques = true;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = true;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                case 'chofer':
                    $scope.menu.cuenta.href = '#/CHAOL/Choferes/' + usuario.uid;
                    $scope.menu.inicio = true;
                    $scope.menu.administracion = false;
                    $scope.menu.fletes.agenda = true;
                    $scope.menu.cuenta.miPerfil = true;
                    $scope.menu.cuenta.cerrarSesion = true;
                    break;
                default:
                    break;
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------