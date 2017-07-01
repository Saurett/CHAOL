//INICIO CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('inicioController', function ($scope, $mdSidenav, $firebaseAuth, $firebaseObject, $firebaseArray, $timeout) {
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
                libres: "0",
                asignados: "0",
                noAutorizados: "0"
            },
            tractores: {
                asignados: "0",
                libres: "0"
            },
            remolques: {
                asignados: "0",
                libres: "0"
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
                cancelado: "0",
                nuevo: true
            },
            bodegas: {
                registrados: "0"
            }
        }

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'administrador':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var fletesPorCotizar = 0;
                        var esperandoPorTransportista = 0;
                        var transportistaPorConfirmar = 0;
                        var unidadesPorAsignar = 0;
                        var enProgreso = 0;
                        var envioPorIniciar = 0;
                        var entregado = 0;
                        var finalizado = 0;
                        var cancelado = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var fletes = childSnapshot.val();
                            switch (fletes.flete.estatus) {
                                case 'fletePorCotizar':
                                    fletesPorCotizar++;
                                    break;
                                case 'esperandoPorTransportista':
                                    esperandoPorTransportista++;
                                    break;
                                case 'transportistaPorConfirmar':
                                    transportistaPorConfirmar++;
                                    break;
                                case 'unidadesPorAsignar':
                                    unidadesPorAsignar++;
                                    break;
                                case 'enProgreso':
                                    enProgreso++;
                                    break;
                                case 'envioPorIniciar':
                                    envioPorIniciar++;
                                    break;
                                case 'entregado':
                                    entregado++;
                                    break;
                                case 'finalizado':
                                    finalizado++;
                                    break;
                                case 'cancelado':
                                    cancelado++;
                                    break;
                                default:
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.fletes.porCotizar = fletesPorCotizar.toString();
                            $scope.paneles.fletes.esperandoPorTransportista = esperandoPorTransportista.toString();
                            $scope.paneles.fletes.transportistaPorConfirmar = transportistaPorConfirmar.toString();
                            $scope.paneles.fletes.unidadesPorAsignar = unidadesPorAsignar.toString();
                            $scope.paneles.fletes.enProgreso = enProgreso.toString();
                            $scope.paneles.fletes.envioPorIniciar = envioPorIniciar.toString();
                            $scope.paneles.fletes.entregado = entregado.toString();
                            $scope.paneles.fletes.finalizado = finalizado.toString();
                            $scope.paneles.fletes.cancelado = cancelado.toString();
                        });
                    });

                    //CLIENTES
                    var refClientes = firebase.database().ref().child('clientes');
                    refClientes.on("value", function (snapshot) {
                        var autorizados = 0;
                        var noAutorizados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (nodoSnapshot) {
                                if (nodoSnapshot.key === 'cliente') {
                                    var cliente = nodoSnapshot.val();
                                    switch (cliente.estatus) {
                                        case "activo":
                                            autorizados++;
                                            break;
                                        case "inactivo":
                                            noAutorizados++;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        });
                        $timeout(function () {
                            $scope.paneles.clientes.autorizados = autorizados.toString();
                            $scope.paneles.clientes.noAutorizados = noAutorizados.toString();
                        });
                    });

                    //TRANSPORTISTAS
                    var refTransportistas = firebase.database().ref().child('transportistas');
                    refTransportistas.on("value", function (snapshot) {
                        var autorizados = 0;
                        var noAutorizados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (nodoSnapshot) {
                                if (nodoSnapshot.key === 'transportista') {
                                    var transportista = nodoSnapshot.val();
                                    switch (transportista.estatus) {
                                        case "activo":
                                            autorizados++;
                                            break;
                                        case "inactivo":
                                            noAutorizados++;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        });
                        $timeout(function () {
                            $scope.paneles.transportistas.autorizados = autorizados.toString();
                            $scope.paneles.transportistas.noAutorizados = noAutorizados.toString();
                        });
                    });

                    //CHOFERES
                    var refChoferes = firebase.database().ref().child('choferes');
                    refChoferes.on("value", function (snapshot) {
                        var libres = 0;
                        var asignados = 0;
                        var noAutorizados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var chofer = childSnapshot.val();
                            switch (chofer.estatus) {
                                case "libre":
                                    libres++;
                                    break;
                                case "asignado":
                                    asignados++;
                                    break;
                                case "inactivo":
                                    noAutorizados++;
                                    break;
                                default:
                                    break;
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.choferes.libres = libres.toString();
                            $scope.paneles.choferes.asignados = asignados.toString();
                            $scope.paneles.choferes.noAutorizados = noAutorizados.toString();
                        });
                    });

                    //TRACTORES
                    var refTractores = firebase.database().ref().child('transportistas');
                    refTractores.on("value", function (snapshot) {
                        var libres = 0;
                        var asignados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (nodoSnapshot) {
                                if (nodoSnapshot.key === "tractores") {
                                    nodoSnapshot.forEach(function (tractorSnapshot) {
                                        var tractor = tractorSnapshot.val();
                                        switch (tractor.estatus) {
                                            case "libre":
                                                libres++;
                                                break;
                                            case "asignado":
                                                asignados++;
                                                break;
                                            default:
                                                break;
                                        }
                                    });
                                }
                            })
                        });
                        $timeout(function () {
                            $scope.paneles.tractores.libres = libres.toString();
                            $scope.paneles.tractores.asignados = asignados.toString();
                        });
                    });

                    //REMOLQUES
                    var refRemolques = firebase.database().ref().child('transportistas');
                    refRemolques.on("value", function (snapshot) {
                        var libres = 0;
                        var asignados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (nodoSnapshot) {
                                if (nodoSnapshot.key === "remolques") {
                                    nodoSnapshot.forEach(function (remolqueSnapshot) {
                                        var remolque = remolqueSnapshot.val();
                                        switch (remolque.estatus) {
                                            case "libre":
                                                libres++;
                                                break;
                                            case "asignado":
                                                asignados++;
                                                break;
                                            default:
                                                break;
                                        }
                                    });
                                }
                            })
                        });
                        $timeout(function () {
                            $scope.paneles.remolques.libres = libres.toString();
                            $scope.paneles.remolques.asignados = asignados.toString();
                        });
                    });

                    //BODEGAS
                    var refBodegas = firebase.database().ref().child('clientes');
                    refBodegas.on("value", function (snapshot) {
                        var activos = 0;
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (nodoSnapshot) {
                                if (nodoSnapshot.key === "bodegas") {
                                    nodoSnapshot.forEach(function (bodegaSnapshot) {
                                        var bodega = bodegaSnapshot.val();
                                        switch (bodega.estatus) {
                                            case "activo":
                                                activos++;
                                                break;
                                            default:
                                                break;
                                        }
                                    });
                                }
                            })
                        });
                        $timeout(function () {
                            $scope.paneles.bodegas.registrados = activos.toString();
                        });
                    });
                    break;
                case 'cliente':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var fletesPorCotizar = 0;
                        var esperandoPorTransportista = 0;
                        var transportistaPorConfirmar = 0;
                        var unidadesPorAsignar = 0;
                        var enProgreso = 0;
                        var envioPorIniciar = 0;
                        var entregado = 0;
                        var finalizado = 0;
                        var cancelado = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var fletes = childSnapshot.val();
                            if (fletes.bodegaDeCarga.firebaseIdDelCliente === usuario.uid) {
                                switch (fletes.flete.estatus) {
                                    case 'fletePorCotizar':
                                        fletesPorCotizar++;
                                        break;
                                    case 'esperandoPorTransportista':
                                        esperandoPorTransportista++;
                                        break;
                                    case 'transportistaPorConfirmar':
                                        transportistaPorConfirmar++;
                                        break;
                                    case 'unidadesPorAsignar':
                                        unidadesPorAsignar++;
                                        break;
                                    case 'enProgreso':
                                        enProgreso++;
                                        break;
                                    case 'envioPorIniciar':
                                        envioPorIniciar++;
                                        break;
                                    case 'entregado':
                                        entregado++;
                                        break;
                                    case 'finalizado':
                                        finalizado++;
                                        break;
                                    case 'cancelado':
                                        cancelado++;
                                        break;
                                    default:
                                }
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.fletes.porCotizar = fletesPorCotizar.toString();
                            $scope.paneles.fletes.esperandoPorTransportista = esperandoPorTransportista.toString();
                            $scope.paneles.fletes.transportistaPorConfirmar = transportistaPorConfirmar.toString();
                            $scope.paneles.fletes.unidadesPorAsignar = unidadesPorAsignar.toString();
                            $scope.paneles.fletes.enProgreso = enProgreso.toString();
                            $scope.paneles.fletes.envioPorIniciar = envioPorIniciar.toString();
                            $scope.paneles.fletes.entregado = entregado.toString();
                            $scope.paneles.fletes.finalizado = finalizado.toString();
                            $scope.paneles.fletes.cancelado = cancelado.toString();
                        });
                    });

                    //CLIENTES
                    $scope.paneles.clientes = false;

                    //TRANSPORTISTAS
                    $scope.paneles.transportistas = false;

                    //CHOFERES
                    $scope.paneles.choferes = false;

                    //TRACTORES
                    $scope.paneles.tractores = false;

                    //REMOLQUES
                    $scope.paneles.remolques = false;

                    //BODEGAS
                    var refBodegas = firebase.database().ref().child('clientes').child(usuario.uid);
                    refBodegas.on("value", function (snapshot) {
                        var activos = 0;
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "bodegas") {
                                childSnapshot.forEach(function (bodegaSnapshot) {
                                    var bodega = bodegaSnapshot.val();
                                    switch (bodega.estatus) {
                                        case "activo":
                                            activos++;
                                            break;
                                        default:
                                            break;
                                    }
                                });
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.bodegas.registrados = activos.toString();
                        });
                    });
                    break;
                case 'transportista':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var fletesPorCotizar = 0;
                        var esperandoPorTransportista = 0;
                        var transportistaPorConfirmar = 0;
                        var unidadesPorAsignar = 0;
                        var enProgreso = 0;
                        var envioPorIniciar = 0;
                        var entregado = 0;
                        var finalizado = 0;
                        var cancelado = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var fletes = childSnapshot.val();
                            switch (fletes.flete.estatus) {
                                case 'esperandoPorTransportista':
                                    esperandoPorTransportista++;
                                    break;
                                case 'transportistaPorConfirmar':
                                    childSnapshot.forEach(function (nodoFleteSnapshot) {
                                        if (nodoFleteSnapshot.key === 'transportistasInteresados') {
                                            var existe = false;
                                            nodoFleteSnapshot.forEach(function (transportistasInteresadosSnapshot) {
                                                var transportistasInteresados = transportistasInteresadosSnapshot.val();
                                                if (transportistasInteresados.firebaseId === usuario.uid) {
                                                    existe = true;
                                                }
                                            });
                                            if (existe) {
                                                transportistaPorConfirmar++;
                                            }
                                            else {
                                                esperandoPorTransportista++;
                                            }
                                        }
                                    });
                                    break;
                                case 'unidadesPorAsignar':
                                    childSnapshot.forEach(function (nodoFleteSnapshot) {
                                        if (nodoFleteSnapshot.key === 'transportistaSeleccionado') {
                                            nodoFleteSnapshot.forEach(function (transportistasSeleccionadoSnapshot) {
                                                var transportistasSeleccionado = transportistasSeleccionadoSnapshot.val();
                                                if (transportistasSeleccionado.firebaseId === usuario.uid) {
                                                    unidadesPorAsignar++;
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case 'enProgreso':
                                    childSnapshot.forEach(function (nodoFleteSnapshot) {
                                        if (nodoFleteSnapshot.key === 'transportistaSeleccionado') {
                                            nodoFleteSnapshot.forEach(function (transportistasSeleccionadoSnapshot) {
                                                var transportistasSeleccionado = transportistasSeleccionadoSnapshot.val();
                                                if (transportistasSeleccionado.firebaseId === usuario.uid) {
                                                    enProgreso++;
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case 'envioPorIniciar':
                                    childSnapshot.forEach(function (nodoFleteSnapshot) {
                                        if (nodoFleteSnapshot.key === 'transportistaSeleccionado') {
                                            nodoFleteSnapshot.forEach(function (transportistasSeleccionadoSnapshot) {
                                                var transportistasSeleccionado = transportistasSeleccionadoSnapshot.val();
                                                if (transportistasSeleccionado.firebaseId === usuario.uid) {
                                                    envioPorIniciar++;
                                                }
                                            });
                                        }
                                    });
                                    break;
                                default:
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.fletes.esperandoPorTransportista = esperandoPorTransportista.toString();
                            $scope.paneles.fletes.transportistaPorConfirmar = transportistaPorConfirmar.toString();
                            $scope.paneles.fletes.unidadesPorAsignar = unidadesPorAsignar.toString();
                            $scope.paneles.fletes.enProgreso = enProgreso.toString();
                            $scope.paneles.fletes.envioPorIniciar = envioPorIniciar.toString();
                        });
                    });

                    //FLETES INHABILITADOS
                    $scope.paneles.fletes.nuevo = false;
                    $scope.paneles.fletes.porCotizar = false;
                    $scope.paneles.fletes.entregado = false;
                    $scope.paneles.fletes.finalizado = false;
                    $scope.paneles.fletes.cancelado = false;

                    //CLIENTES
                    $scope.paneles.clientes = false;

                    //TRANSPORTISTAS
                    $scope.paneles.transportistas = false;

                    //CHOFERES
                    var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child("choferes");
                    refChoferes.on("value", function (snapshot) {
                        var libres = 0;
                        var asignados = 0;
                        var noAutorizados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var chofer = childSnapshot.val();
                            switch (chofer.estatus) {
                                case "libre":
                                    libres++;
                                    break;
                                case "asignado":
                                    asignados++;
                                    break;
                                case "inactivo":
                                    noAutorizados++;
                                    break;
                                default:
                                    break;
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.choferes.libres = libres.toString();
                            $scope.paneles.choferes.asignados = asignados.toString();
                            $scope.paneles.choferes.noAutorizados = noAutorizados.toString();
                        });
                    });

                    //TRACTORES
                    var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores');
                    refTractores.on("value", function (snapshot) {
                        var libres = 0;
                        var asignados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var tractor = childSnapshot.val();
                            switch (tractor.estatus) {
                                case "libre":
                                    libres++;
                                    break;
                                case "asignado":
                                    asignados++;
                                    break;
                                default:
                                    break;
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.tractores.libres = libres.toString();
                            $scope.paneles.tractores.asignados = asignados.toString();
                        });
                    });

                    //REMOLQUES
                    var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques');
                    refRemolques.on("value", function (snapshot) {
                        var libres = 0;
                        var asignados = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var remolque = childSnapshot.val();
                            switch (remolque.estatus) {
                                case "libre":
                                    libres++;
                                    break;
                                case "asignado":
                                    asignados++;
                                    break;
                                default:
                                    break;
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.remolques.libres = libres.toString();
                            $scope.paneles.remolques.asignados = asignados.toString();
                        });
                    });

                    //BODEGAS
                    $scope.paneles.bodegas = false;
                    break;
                case 'chofer':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var fletesPorCotizar = 0;
                        var esperandoPorTransportista = 0;
                        var transportistaPorConfirmar = 0;
                        var unidadesPorAsignar = 0;
                        var enProgreso = 0;
                        var envioPorIniciar = 0;
                        var entregado = 0;
                        var finalizado = 0;
                        var cancelado = 0;
                        snapshot.forEach(function (childSnapshot) {
                            var fletes = childSnapshot.val();
                            switch (fletes.flete.estatus) {
                                case 'enProgreso':
                                    childSnapshot.forEach(function (nodoFleteSnapshot) {
                                        if (nodoFleteSnapshot.key === 'choferSeleccionado') {
                                            nodoFleteSnapshot.forEach(function (choferSeleccionadoSnapshot) {
                                                var choferSeleccionado = choferSeleccionadoSnapshot.val();
                                                if (choferSeleccionado.firebaseId === usuario.uid) {
                                                    enProgreso++;
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case 'envioPorIniciar':
                                    childSnapshot.forEach(function (nodoFleteSnapshot) {
                                        if (nodoFleteSnapshot.key === 'choferSeleccionado') {
                                            nodoFleteSnapshot.forEach(function (choferSeleccionadoSnapshot) {
                                                var choferSeleccionado = choferSeleccionadoSnapshot.val();
                                                if (choferSeleccionado.firebaseId === usuario.uid) {
                                                    envioPorIniciar++;
                                                }
                                            });
                                        }
                                    });
                                    break;
                                default:
                            }
                        });
                        $timeout(function () {
                            $scope.paneles.fletes.enProgreso = enProgreso.toString();
                            $scope.paneles.fletes.envioPorIniciar = envioPorIniciar.toString();
                        });
                    });

                    //FLETES INHABILITADOS
                    $scope.paneles.fletes.nuevo = false;
                    $scope.paneles.fletes.porCotizar = false;
                    $scope.paneles.fletes.esperandoPorTransportista = false;
                    $scope.paneles.fletes.transportistaPorConfirmar = false;
                    $scope.paneles.fletes.unidadesPorAsignar = false;
                    $scope.paneles.fletes.entregado = false;
                    $scope.paneles.fletes.finalizado = false;
                    $scope.paneles.fletes.cancelado = false;

                    //CLIENTES
                    $scope.paneles.clientes = false;

                    //TRANSPORTISTAS
                    $scope.paneles.transportistas = false;

                    //CHOFERES
                    $scope.paneles.choferes = false;

                    //TRACTORES
                    $scope.paneles.tractores = false;

                    //REMOLQUES
                    $scope.paneles.remolques = false;

                    //BODEGAS
                    $scope.paneles.bodegas = false;
                    break;
                default:
            }
        });
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------