//LOGIN CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('loginController', function ($scope, $location, $firebaseObject, $firebaseAuth, $mdDialog) {
        $scope.auth = $firebaseAuth();
        $scope.auth.$signOut();

        //ENVIO AL FORMULARIO DE REGISTRO
        $scope.registro = function () {
            $location.path("/Registro");
        };

        //INICIO DE SESIÓN
        $scope.iniciarSesion = function () {
            $scope.auth = $firebaseAuth();
            $scope.auth.$signInWithEmailAndPassword($scope.usuario.usuario, $scope.usuario.contrasena).then(function () {
                console.log($scope.usuario.usuario + ' logged in');
                var user = $scope.auth.$getAuth();
                var refUsuario = firebase.database().ref('usuarios').child(user.uid);
                var usuario = $firebaseObject(refUsuario);
                usuario.$loaded(function () {
                    if (usuario.$value === 'administrador') {
                        console.log('Admin');
                    }
                }).catch(function (error) {
                    console.log(error);
                });
                $location.path("/CHAOL");
            }).catch(function (error) {
                //ERROR
                if (error.code === 'auth/user-not-found') {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#login')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/> <p>No existen registros del usuario correspondiente a <b>' + $scope.usuario.usuario + '</b>.</p> <p>El usuario pudo haber sido eliminado. Por favor, valida la dirección que ingresaste.</p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
                else if (error.code === 'auth/wrong-password') {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#login')))
                            .clickOutsideToClose(false)
                            .title('Oops! Algo salió mal')
                            .htmlContent('<br/><p>La contraseña es inválida o <b>' + $scope.usuario.usuario + '</b> no cuenta con una.</p>')
                            .ariaLabel('Alert Dialog Demo')
                            .ok('Aceptar')
                    );
                }
                else {
                    console.log(error);
                }
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
