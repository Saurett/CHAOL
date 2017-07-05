//SERVICES
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.factory('unixTime', function () {
        var getTime = function () {
            return Math.round((new Date()).getTime() / 1000);
        };
        return getTime;
    });

    app.factory('convertToUnixTime', function () {
        var getTime = function (time) {
            return (new Date(time)).getTime() / 1000;
        }
        return getTime;
    });

    app.factory("Auth", function ($firebaseAuth, $firebaseObject, $location) {
        var firebase = {
            Auth: $firebaseAuth(),
            Object: $firebaseObject,
            Location: $location
        }
        return firebase
    });

    //SERCIVIO QUE TERMINARÁ LA SESIÓN DEL USAURIO EN CASO DE QUE SEA ELIMINADO O INACTIVO
    app.factory('WatchUser', function ($firebaseAuth, $firebaseObject, $location) {
        return {
            checkUser: function (refUser) {
                refUser.on('value', function (snap) {
                    var user = $firebaseObject(refUser);
                    user.$loaded().then(function () {
                        var auth = $firebaseAuth();
                        var userAuth = auth.$getAuth();
                        if (user.firebaseId === userAuth.uid) {
                            if (user.estatus === 'inactivo' || user.estatus === 'eliminado') {
                                auth.$signOut();
                                $location.path('/Inicio');
                            }
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                })
            }
        }
    });

    //SERVICIO PARA DETERMINAR SI EL USUARIO ESTÁ ACTIVO AL MOMENTO DE INICIAR SESIÓN
    app.factory('AllowLogIn', function ($location, $firebaseObject, $firebaseAuth, $mdDialog, WatchUser) {
        return {
            getValidation: function (usuario) {
                usuario.$loaded().then(function () {
                    var refObjeto;
                    var objeto;
                    switch (usuario.tipoDeUsuario) {
                        case 'administrador':
                            $location.path('/CHAOL');
                            break;
                        case 'colaborador':
                            refObjeto = firebase.database().ref('administradores').child(usuario.$id);
                            objeto = $firebaseObject(refObjeto);
                            break;
                        case 'cliente':
                            refObjeto = firebase.database().ref('clientes').child(usuario.$id).child('cliente');
                            objeto = $firebaseObject(refObjeto);
                            break;
                        case 'transportista':
                            refObjeto = firebase.database().ref('transportistas').child(usuario.$id).child('transportista');
                            objeto = $firebaseObject(refObjeto);
                            break;
                        case 'chofer':
                            refObjeto = firebase.database().ref('choferes').child(usuario.$id);
                            objeto = $firebaseObject(refObjeto);
                            break;
                        default:
                    }
                    if (objeto) {
                        objeto.$loaded().then(function () {
                            if (objeto.estatus === 'inactivo' || objeto.estatus === "eliminado") {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#login')))
                                        .clickOutsideToClose(false)
                                        .title('Oops! Algo salió mal')
                                        .htmlContent('<br/> <p>Al parecer el administrador del sistema no ha activado esta cuenta.</p> <p>Por favor, espera a que seas activado o envía un mensaje a <b>chaolapp@gmail.com</b> para solicitar tu activación.</p>')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar')
                                );
                                var session = $firebaseAuth();
                                session.$signOut();
                            }
                            else {
                                WatchUser.checkUser(refObjeto);
                                $location.path('/CHAOL');
                            }
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            }
        }
    });

    app.factory('createUserService', function () {
        var firebaseConfig = {
            apiKey: "AIzaSyBtXFJ35HfMROCXKeBEWUAkXow51Z57C2U",
            authDomain: "chaol-75d99.firebaseapp.com",
            databaseURL: "https://chaol-75d99.firebaseio.com",
            projectId: "chaol-75d99",
            storageBucket: "chaol-75d99.appspot.com",
            messagingSenderId: "152632874107"
        };

        var createUserService = firebase.initializeApp(firebaseConfig, 'Secondary');
        return createUserService;
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------