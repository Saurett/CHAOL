//INICIO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('inicioController', function ($scope, $mdSidenav, $firebaseAuth, $firebaseObject, $firebaseArray) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        //ESTATUS PANELES
        $scope.paneles = {
            clientes: {
                autorizados: "0",
                noAutorizados: "0"
            },
            transportistas: {
                autorizados: "0",
                noAutorizados: "0"
            },
            choferes: {
                autorizados: "0",
                noAutorizados: "0"
            },
            tractores: {
                registrados: "0"
            },
            remolques: {
                registrados: "0"
            },
            fletes: {
                porCotizar: "0",
                esperandoPorTransportista: "0",
                transportistaPorConfirmar: "0",
                unidadesPorAsignar: "0",
                envioPorIniciar: "0",
                enProgreso: "0",
                entregado: "0",
                finalizado: "0",
                cancelado: "0"
            }
        }

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/estatus');
                    buscarFletes(refFletes);
                    buscarClientes();
                    buscarTransportistas();
                    var refChoferes = firebase.database().ref().child('choferes').orderByChild('estatus');
                    buscarChoferes(refChoferes);
                    var refTractores = firebase.database().ref().child('transportistas').orderByChild('tractores/estatus');
                    buscarTractores(refTractores);
                    var refRemolques = firebase.database().ref().child('transportistas').orderByChild('remolques/estatus');
                    buscarRemolques(refRemolques);
                    break;
                case 'cliente':
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/estatus');
                    buscarFletes(refFletes);
                    $scope.paneles.clientes = false;
                    $scope.paneles.transportistas = false;
                    $scope.paneles.choferes = false;
                    $scope.paneles.tractores = false;
                    $scope.paneles.remolques = false;
                    break;
                case 'transportista':
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/estatus');
                    buscarFletes(refFletes);
                    $scope.paneles.clientes = false;
                    $scope.paneles.transportistas = false;
                    $scope.paneles.choferes = true;
                    $scope.paneles.tractores = true;
                    $scope.paneles.remolques = true;
                    $scope.paneles.fletes = true;
                    break;
                case 'chofer':
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/estatus');
                    buscarFletes(refFletes);
                    $scope.paneles.clientes = false;
                    $scope.paneles.transportistas = false;
                    $scope.paneles.choferes = false;
                    $scope.paneles.tractores = false;
                    $scope.paneles.remolques = false;
                    break;
                default:
            }
        });

        //BUSQUEDA DE FLETES
        var buscarFletes = function (refFletes) {
            //POR COTIZAR
            refFletesPorCotizar = refFletes.equalTo('fletePorCotizar');
            firebaseFletesPorCotizar = $firebaseArray(refFletesPorCotizar);
            firebaseFletesPorCotizar.$loaded().then(function () {
                $scope.paneles.fletes.porCotizar = firebaseFletesPorCotizar.length;
            });
            refFletesPorCotizar.on('value', function (dataSnapShot) {
                firebaseFletesPorCotizar = $firebaseArray(refFletesPorCotizar);
                firebaseFletesPorCotizar.$loaded().then(function () {
                    $scope.paneles.fletes.porCotizar = firebaseFletesPorCotizar.length;
                });
            });

            //ESPERANDO POR TRANSPORTISTA
            refFletesEsperandoPorTransportista = refFletes.equalTo('esperandoPorTransportista');
            firebaseFletesEsperandoPorTransportista = $firebaseArray(refFletesEsperandoPorTransportista);
            firebaseFletesEsperandoPorTransportista.$loaded().then(function () {
                $scope.paneles.fletes.esperandoPorTransportista = firebaseFletesEsperandoPorTransportista.length;
            });
            refFletesEsperandoPorTransportista.on('value', function (dataSnapShot) {
                firebaseFletesEsperandoPorTransportista = $firebaseArray(refFletesEsperandoPorTransportista);
                firebaseFletesEsperandoPorTransportista.$loaded().then(function () {
                    $scope.paneles.fletes.esperandoPorTransportista = firebaseFletesEsperandoPorTransportista.length;
                });
            });

            //TRANSPORTISTA POR CONFIRMAR
            refFletesTransportistaPorConfirmar = refFletes.equalTo('transportistaPorConfirmar');
            firebaseFletesTransportistaPorConfirmar = $firebaseArray(refFletesTransportistaPorConfirmar);
            firebaseFletesTransportistaPorConfirmar.$loaded().then(function () {
                $scope.paneles.fletes.transportistaPorConfirmar = firebaseFletesTransportistaPorConfirmar.length;
            });
            refFletesTransportistaPorConfirmar.on('value', function (dataSnapShot) {
                firebaseFletesTransportistaPorConfirmar = $firebaseArray(refFletesTransportistaPorConfirmar);
                firebaseFletesTransportistaPorConfirmar.$loaded().then(function () {
                    $scope.paneles.fletes.transportistaPorConfirmar = firebaseFletesTransportistaPorConfirmar.length;
                });
            });

            //UNIDADES POR ASIGNAR
            refFletesUnidadesPorAsignar = refFletes.equalTo('unidadesPorAsignar');
            firebaseFletesUnidadesPorAsignar = $firebaseArray(refFletesUnidadesPorAsignar);
            firebaseFletesUnidadesPorAsignar.$loaded().then(function () {
                $scope.paneles.fletes.unidadesPorAsignar = firebaseFletesUnidadesPorAsignar.length;
            });
            refFletesUnidadesPorAsignar.on('value', function (dataSnapShot) {
                firebaseFletesUnidadesPorAsignar = $firebaseArray(refFletesUnidadesPorAsignar);
                firebaseFletesUnidadesPorAsignar.$loaded().then(function () {
                    $scope.paneles.fletes.unidadesPorAsignar = firebaseFletesUnidadesPorAsignar.length;
                });
            });

            //EN PROGRESO
            refFletesEnProgreso = refFletes.equalTo('enProgreso');
            firebaseFletesEnProgreso = $firebaseArray(refFletesEnProgreso);
            firebaseFletesEnProgreso.$loaded().then(function () {
                $scope.paneles.fletes.enProgreso = firebaseFletesEnProgreso.length;
            });
            refFletesEnProgreso.on('value', function (dataSnapShot) {
                firebaseFletesEnProgreso = $firebaseArray(refFletesEnProgreso);
                firebaseFletesEnProgreso.$loaded().then(function () {
                    $scope.paneles.fletes.enProgreso = firebaseFletesEnProgreso.length;
                });
            });

            //ENVIO POR INICIAR
            refFletesEnviosPorIniciar = refFletes.equalTo('envioPorIniciar');
            firebaseFletesEnviosPorIniciar = $firebaseArray(refFletesEnviosPorIniciar);
            firebaseFletesEnviosPorIniciar.$loaded().then(function () {
                $scope.paneles.fletes.envioPorIniciar = firebaseFletesEnviosPorIniciar.length;
            });
            refFletesEnviosPorIniciar.on('value', function (dataSnapShot) {
                firebaseFletesEnviosPorIniciar = $firebaseArray(refFletesEnviosPorIniciar);
                firebaseFletesEnviosPorIniciar.$loaded().then(function () {
                    $scope.paneles.fletes.envioPorIniciar = firebaseFletesEnviosPorIniciar.length;
                });
            });

            //ENTREGADO
            refFletesEntregados = refFletes.equalTo('entregado');
            firebaseFletesEntregados = $firebaseArray(refFletesEntregados);
            firebaseFletesEntregados.$loaded().then(function () {
                $scope.paneles.fletes.entregado = firebaseFletesEntregados.length;
            });
            refFletesEntregados.on('value', function (dataSnapShot) {
                firebaseFletesEntregados = $firebaseArray(refFletesEntregados);
                firebaseFletesEntregados.$loaded().then(function () {
                    $scope.paneles.fletes.entregado = firebaseFletesEntregados.length;
                });
            });

            //FINALIZADO
            refFletesFinalizados = refFletes.equalTo('finalizado');
            firebaseFletesFinalizados = $firebaseArray(refFletesFinalizados);
            firebaseFletesFinalizados.$loaded().then(function () {
                $scope.paneles.fletes.finalizado = firebaseFletesFinalizados.length;
            });
            refFletesFinalizados.on('value', function (dataSnapShot) {
                firebaseFletesFinalizados = $firebaseArray(refFletesFinalizados);
                firebaseFletesFinalizados.$loaded().then(function () {
                    $scope.paneles.fletes.finalizado = firebaseFletesFinalizados.length;
                });
            });

            //CANCELADOS
            refFletesCancelados = refFletes.equalTo('cancelado');
            firebaseFletesCancelados = $firebaseArray(refFletesCancelados);
            firebaseFletesCancelados.$loaded().then(function () {
                $scope.paneles.fletes.cancelado = firebaseFletesCancelados.length;
            });
            refFletesCancelados.on('value', function (dataSnapShot) {
                firebaseFletesCancelados = $firebaseArray(refFletesCancelados);
                firebaseFletesCancelados.$loaded().then(function () {
                    $scope.paneles.fletes.cancelado = firebaseFletesCancelados.length;
                });
            });
        };

        //BUSQUEDA DE CLIENTES
        var buscarClientes = function () {
            refClientes = firebase.database().ref().child('clientes').orderByChild('cliente/estatus');
            //AUTORIZADOS
            refClientesAutorizados = refClientes.equalTo('activo');
            firebaseClientesAutorizados = $firebaseArray(refClientesAutorizados);
            firebaseClientesAutorizados.$loaded().then(function () {
                $scope.paneles.clientes.autorizados = firebaseClientesAutorizados.length;
            });
            refClientesAutorizados.on('value', function (dataSnapShot) {
                firebaseClientesAutorizados = $firebaseArray(refClientesAutorizados);
                firebaseClientesAutorizados.$loaded().then(function () {
                    $scope.paneles.clientes.autorizados = firebaseClientesAutorizados.length;
                });
            });

            //NO AUTORIZADOS
            refClientesNoAutorizados = refClientes.equalTo('inactivo');
            firebaseClientesNoAutorizados = $firebaseArray(refClientesNoAutorizados);
            firebaseClientesNoAutorizados.$loaded().then(function () {
                $scope.paneles.clientes.noAutorizados = firebaseClientesNoAutorizados.length;
            });
            refClientesNoAutorizados.on('value', function (dataSnapShot) {
                firebaseClientesNoAutorizados = $firebaseArray(refClientesNoAutorizados);
                firebaseClientesNoAutorizados.$loaded().then(function () {
                    $scope.paneles.clientes.noAutorizados = firebaseClientesNoAutorizados.length;
                });
            });
        };

        //TRANSPORTISTAS
        var buscarTransportistas = function () {
            refTransportistas = firebase.database().ref().child('transportistas').orderByChild('transportista/estatus');
            //AUTORIZADOS
            refTransportistasAutorizados = refTransportistas.equalTo('activo');
            firebaseTransportistasAutorizados = $firebaseArray(refTransportistasAutorizados);
            firebaseTransportistasAutorizados.$loaded().then(function () {
                $scope.paneles.transportistas.autorizados = firebaseTransportistasAutorizados.length;
            });
            refTransportistasAutorizados.on('value', function (dataSnapShot) {
                firebaseTransportistasAutorizados = $firebaseArray(refTransportistasAutorizados);
                firebaseTransportistasAutorizados.$loaded().then(function () {
                    $scope.paneles.transportistas.autorizados = firebaseTransportistasAutorizados.length;
                });
            });

            //NO AUTORIZADOS
            refTransportistasNoAutorizados = refTransportistas.equalTo('inactivo');
            firebaseTransportistasNoAutorizados = $firebaseArray(refTransportistasNoAutorizados);
            firebaseTransportistasNoAutorizados.$loaded().then(function () {
                $scope.paneles.transportistas.noAutorizados = firebaseTransportistasNoAutorizados.length;
            });
            refTransportistasNoAutorizados.on('value', function (dataSnapShot) {
                firebaseTransportistasNoAutorizados = $firebaseArray(refTransportistasNoAutorizados);
                firebaseTransportistasNoAutorizados.$loaded().then(function () {
                    $scope.paneles.transportistas.noAutorizados = firebaseTransportistasNoAutorizados.length;
                });
            });
        };

        //CHOFERES
        var buscarChoferes = function (refChoferes) {
            //AUTORIZADOS
            refChoferesAutorizados = refChoferes.equalTo('activo');
            firebaseChoferesAutorizados = $firebaseArray(refChoferesAutorizados);
            firebaseChoferesAutorizados.$loaded().then(function () {
                $scope.paneles.choferes.autorizados = firebaseChoferesAutorizados.length;
            });
            refChoferesAutorizados.on('value', function (dataSnapShot) {
                firebaseChoferesAutorizados = $firebaseArray(refChoferesAutorizados);
                firebaseChoferesAutorizados.$loaded().then(function () {
                    $scope.paneles.choferes.autorizados = firebaseChoferesAutorizados.length;
                });
            });

            //NO AUTORIZADOS
            refChoferesNoAutorizados = refChoferes.equalTo('inactivo');
            firebaseChoferesNoAutorizados = $firebaseArray(refChoferesNoAutorizados);
            firebaseChoferesNoAutorizados.$loaded().then(function () {
                $scope.paneles.choferes.noAutorizados = firebaseChoferesNoAutorizados.length;
            });
            refChoferesNoAutorizados.on('value', function (dataSnapShot) {
                firebaseChoferesNoAutorizados = $firebaseArray(refChoferesNoAutorizados);
                firebaseChoferesNoAutorizados.$loaded().then(function () {
                    $scope.paneles.choferes.noAutorizados = firebaseChoferesNoAutorizados.length;
                });
            });
        };

        //TRACTORES
        var buscarTractores = function (refTractores) {
            //TOTAL
            refTractoresNoAutorizados = refTractores;
            firebaseTractoresNoAutorizados = $firebaseArray(refTractoresNoAutorizados);
            firebaseTractoresNoAutorizados.$loaded().then(function () {
                $scope.paneles.tractores.registrados = firebaseTractoresNoAutorizados.length;
            });
            refTractoresNoAutorizados.on('value', function (dataSnapShot) {
                firebaseTractoresNoAutorizados = $firebaseArray(refTractoresNoAutorizados);
                firebaseTractoresNoAutorizados.$loaded().then(function () {
                    $scope.paneles.tractores.registrados = firebaseTractoresNoAutorizados.length;
                });
            });
        };

        //REMOLQUES
        var buscarRemolques = function (refRemolques) {
            //TOTAL
            refRemolquesAutorizados = refRemolques;
            firebaseRemolquesAutorizados = $firebaseArray(refRemolquesAutorizados);
            firebaseRemolquesAutorizados.$loaded().then(function () {
                $scope.paneles.remolques.registrados = firebaseRemolquesAutorizados.length;
            });
            refRemolquesAutorizados.on('value', function (dataSnapShot) {
                firebaseRemolquesAutorizados = $firebaseArray(refRemolquesAutorizados);
                firebaseRemolquesAutorizados.$loaded().then(function () {
                    $scope.paneles.remolques.registrados = firebaseRemolquesAutorizados.length;
                });
            });
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------