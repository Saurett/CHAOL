//RECUPERAR CONTRASEÑA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('recuperarContrasenaController', function ($scope, $location, $firebaseAuth, $mdDialog) {
        $scope.regresar = function () {
            $location.path("/Inicio");
        };

        //RECUPERACION DE CONTRASEÑA
        $scope.enviarContrasena = function () {
            $scope.auth = $firebaseAuth();
            //RESET DE CONTRASEÑA
            $scope.auth.$sendPasswordResetEmail($scope.usuario.usuario).then(function () {
                //ALERTA
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#recuperarContrasena')))
                        .clickOutsideToClose(false)
                        .title('Correo electrónico enviado')
                        .htmlContent('<br/><p> Hemos enviado un mensaje a <b>' + $scope.usuario.usuario + '</b>. Por favor valida tu cuenta abriendo el enlace que compartimos.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                ).then(function () {
                    $location.path("/Inicio");
                });
                console.log('sent');
            }).catch(function (error) {
                //ERROR
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#recuperarContrasena')))
                        .clickOutsideToClose(false)
                        .title('Usuario no encontrado')
                        .htmlContent('<br/> <p>No existen registros del usuario correspondiente a <b>' + $scope.usuario.usuario + '</b>.</p> <p>El usuario pudo haber sido eliminado. Por favor, valida la dirección que ingresaste.</p>')
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Aceptar')
                );
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------