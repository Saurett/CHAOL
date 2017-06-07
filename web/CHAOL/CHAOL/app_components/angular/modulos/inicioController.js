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
                cancelado: "0"
            },
            bodegas: {
                registrados: "0"
            }
        }

        //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            arrayFletes.push(fletes.flete);
                        });
                        buscarFletes(arrayFletes);
                    });

                    //CLIENTES
                    var refClientes = firebase.database().ref().child('clientes');
                    refClientes.on("value", function (snapshot) {
                        var arrayClientes = [];
                        snapshot.forEach(function (childSnapshot) {
                            clientes = childSnapshot.val();
                            arrayClientes.push(clientes.cliente);
                        });
                        buscarClientes(arrayClientes);
                    });

                    //TRANSPORTISTAS
                    var refTransportistas = firebase.database().ref().child('transportistas');
                    refTransportistas.on("value", function (snapshot) {
                        var arrayTransportistas = [];
                        snapshot.forEach(function (childSnapshot) {
                            transportistas = childSnapshot.val();
                            arrayTransportistas.push(transportistas.transportista);
                        });
                        buscarTransportistas(arrayTransportistas);
                    });

                    //CHOFERES
                    var refChoferes = firebase.database().ref().child('choferes');
                    refChoferes.on("value", function (snapshot) {
                        var arrayChoferes = [];
                        snapshot.forEach(function (childSnapshot) {
                            choferes = childSnapshot.val();
                            arrayChoferes.push(choferes);
                        });
                        buscarChoferes(arrayChoferes);
                    });

                    //TRACTORES
                    var refTractores = firebase.database().ref().child('transportistas');
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (tractorSnapshot) {
                                if (tractorSnapshot.key === "tractores") {
                                    tractorSnapshot.forEach(function (tractor) {
                                        arrayTractores.push(tractor.val());
                                    });
                                }
                            })
                        });
                        buscarTractores(arrayTractores);
                    });

                    //REMOLQUES
                    var refRemolques = firebase.database().ref().child('transportistas');
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (remolqueSnapshot) {
                                if (remolqueSnapshot.key === "remolques") {
                                    remolqueSnapshot.forEach(function (remolque) {
                                        arrayRemolques.push(remolque.val());
                                    });
                                }
                            })
                        });
                        buscarRemolques(arrayRemolques);
                    });

                    //BODEGAS
                    var refBodegas = firebase.database().ref().child('clientes');
                    refBodegas.on("value", function (snapshot) {
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            childSnapshot.forEach(function (bodegaSnapshot) {
                                if (bodegaSnapshot.key === "bodegas") {
                                    bodegaSnapshot.forEach(function (bodega) {
                                        arrayBodegas.push(bodega.val());
                                    });
                                }
                            })
                        });
                        buscarBodegas(arrayBodegas);
                    });
                    break;
                case 'cliente':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.bodegaDeCarga.firebaseIdDelCliente === usuario.uid) {
                                arrayFletes.push(fletes.flete);
                            }
                        });
                        buscarFletes(arrayFletes);
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
                        var arrayBodegas = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "bodegas") {
                                childSnapshot.forEach(function (bodega) {
                                    arrayBodegas.push(bodega.val());
                                });
                            }
                        });
                        buscarBodegas(arrayBodegas);
                    });
                    break;
                case 'transportista':
                    //FLETES DISPONIBLES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.flete.estatus === "esperandoPorTransportista") {
                                arrayFletes.push(fletes.flete);
                            }
                        });
                        buscarEsperandoPorTransportista(arrayFletes);
                    });

                    //FLETES INTERESADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.transportistasInteresados !== undefined) {
                                if (fletes.transportistasInteresados.key === usuario.uid && fletes.flete.estatus === "transportistaPorConfirmar") {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarTransportistaPorConfirmar(arrayFletes);
                    });

                    //FLETES ASIGNADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.transportistaSeleccionado !== undefined) {
                                if (fletes.transportistaSeleccionado.key === usuario.uid) {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarUnidadesPorAsignar(arrayFletes);
                        buscarEnvioPorIniciar(arrayFletes);
                        buscarEnProgreso(arrayFletes);
                    });

                    //FLETES INHABILITADOS
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
                        var arrayChoferes = [];
                        snapshot.forEach(function (childSnapshot) {
                            choferes = childSnapshot.val();
                            arrayChoferes.push(choferes);
                        });
                        buscarChoferes(arrayChoferes);
                    });

                    //TRACTORES
                    var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refTractores.on("value", function (snapshot) {
                        var arrayTractores = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "tractores") {
                                childSnapshot.forEach(function (tractorSnapshot) {
                                    tractor = tractorSnapshot.val();
                                    if (tractor.estatus !== 'eliminado') {
                                        arrayTractores.push(tractor);
                                    }
                                })
                            }
                        });
                        buscarTractores(arrayTractores);
                    });

                    //REMOLQUES
                    var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid);
                    refRemolques.on("value", function (snapshot) {
                        var arrayRemolques = [];
                        snapshot.forEach(function (childSnapshot) {
                            if (childSnapshot.key === "remolques") {
                                childSnapshot.forEach(function (remolqueSnapshot) {
                                    remolque = remolqueSnapshot.val();
                                    if (remolque.estatus !== 'eliminado') {
                                        arrayRemolques.push(remolque);
                                    }
                                })
                            }
                        });
                        buscarRemolques(arrayRemolques);
                    });

                    //BODEGAS
                    $scope.paneles.bodegas = false;
                    break;
                case 'chofer':
                    //FLETES ASIGNADOS
                    var refFletes = firebase.database().ref().child('fletesPorAsignar');
                    refFletes.on("value", function (snapshot) {
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            if (fletes.choferSeleccionado !== undefined) {
                                if (fletes.choferSeleccionado.key === usuario.uid && (fletes.flete.estatus === "envioPorIniciar" || fletes.flete.estatus === "enProgreso")) {
                                    arrayFletes.push(fletes.flete);
                                }
                            }
                        });
                        buscarEnvioPorIniciar(arrayFletes);
                        buscarEnProgreso(arrayFletes);
                    });

                    //FLETES INHABILITADOS
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

        //BUSQUEDA DE FLETES
        var buscarFletesPorCotizar = function (arreglo) {
            var fletesPorCotizar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'fletePorCotizar':
                        fletesPorCotizar++;
                        break;
                }
            });
            $scope.paneles.fletes.porCotizar = fletesPorCotizar.toString();
        }

        var buscarEsperandoPorTransportista = function (arreglo) {
            var esperandoPorTransportista = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'esperandoPorTransportista':
                        esperandoPorTransportista++;
                        break;
                }
            });
            $scope.paneles.fletes.esperandoPorTransportista = esperandoPorTransportista.toString();
        }

        var buscarTransportistaPorConfirmar = function (arreglo) {
            var transportistaPorConfirmar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'transportistaPorConfirmar':
                        transportistaPorConfirmar++;
                        break;
                }
            });
            $scope.paneles.fletes.transportistaPorConfirmar = transportistaPorConfirmar.toString();
        }

        var buscarUnidadesPorAsignar = function (arreglo) {
            var unidadesPorAsignar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'unidadesPorAsignar':
                        unidadesPorAsignar++;
                        break;
                }
            });
            $scope.paneles.fletes.unidadesPorAsignar = unidadesPorAsignar.toString();
        }

        var buscarEnProgreso = function (arreglo) {
            var enProgreso = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'enProgreso':
                        enProgreso++;
                        break;
                }
            });
            $scope.paneles.fletes.enProgreso = enProgreso.toString();
        }

        var buscarEnvioPorIniciar = function (arreglo) {
            var envioPorIniciar = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'envioPorIniciar':
                        envioPorIniciar++;
                        break;
                }
            });
            $scope.paneles.fletes.envioPorIniciar = envioPorIniciar.toString();
        }

        var buscarEntregado = function (arreglo) {
            var entregado = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'entregado':
                        entregado++;
                        break;
                }
            });
            $scope.paneles.fletes.entregado = entregado.toString();
        }

        var buscarFinalizado = function (arreglo) {
            var finalizado = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'finalizado':
                        finalizado++;
                        break;
                }
            });
            $scope.paneles.fletes.finalizado = finalizado.toString();
        }

        var buscarCancelado = function (arreglo) {
            var cancelado = 0;
            arreglo.forEach(function (flete) {
                switch (flete.estatus) {
                    case 'cancelado':
                        cancelado++;
                        break;
                }
            });
            $scope.paneles.fletes.cancelado = cancelado.toString();
        }

        var buscarFletes = function (arreglo) {
            buscarFletesPorCotizar(arreglo);
            buscarEsperandoPorTransportista(arreglo);
            buscarTransportistaPorConfirmar(arreglo);
            buscarUnidadesPorAsignar(arreglo);
            buscarEnvioPorIniciar(arreglo);
            buscarEnProgreso(arreglo);
            buscarEntregado(arreglo);
            buscarFinalizado(arreglo);
            buscarCancelado(arreglo);
        };

        //BUSQUEDA DE CLIENTES
        var buscarClientes = function (arreglo) {
            var autorizados = 0;
            var noAutorizados = 0;

            arreglo.forEach(function (cliente) {
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
            });

            $scope.paneles.clientes.autorizados = autorizados.toString();
            $scope.paneles.clientes.noAutorizados = noAutorizados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE TRANSPORTISTAS
        var buscarTransportistas = function (arreglo) {
            var autorizados = 0;
            var noAutorizados = 0;

            arreglo.forEach(function (transportista) {
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
            });

            $scope.paneles.transportistas.autorizados = autorizados.toString();
            $scope.paneles.transportistas.noAutorizados = noAutorizados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE CHOFERES
        var buscarChoferes = function (arreglo) {
            var libres = 0;
            var asignados = 0;
            var noAutorizados = 0;

            arreglo.forEach(function (chofer) {
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

            $scope.paneles.choferes.libres = libres.toString();
            $scope.paneles.choferes.asignados = asignados.toString();
            $scope.paneles.choferes.noAutorizados = noAutorizados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE TRACTORES
        var buscarTractores = function (arreglo) {
            var libres = 0;
            var asignados = 0;

            arreglo.forEach(function (tractor) {
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

            $scope.paneles.tractores.libres = libres.toString();
            $scope.paneles.tractores.asignados = asignados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE REMOLQUES
        var buscarRemolques = function (arreglo) {
            var libres = 0;
            var asignados = 0;

            arreglo.forEach(function (remolque) {
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

            $scope.paneles.remolques.libres = libres.toString();
            $scope.paneles.remolques.asignados = asignados.toString();
            $scope.$apply();
        };

        //BUSQUEDA DE BODEGAS
        var buscarBodegas = function (arreglo) {
            var activos = 0;

            arreglo.forEach(function (bodega) {
                switch (bodega.estatus) {
                    case "activo":
                        activos++;
                        break;
                    default:
                        break;
                }
            });

            $scope.paneles.bodegas.registrados = activos.toString();
            $scope.$apply();
        };
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------