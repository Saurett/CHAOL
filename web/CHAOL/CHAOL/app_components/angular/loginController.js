(function () {
    var app = angular.module('app');

    app.controller('loginController', function ($scope, $location, $firebaseAuth) {
        $scope.registro = function () {
            $location.path("/Registro");
        };
    });
})();
