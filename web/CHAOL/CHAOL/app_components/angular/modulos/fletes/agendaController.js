//AGENDA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('agendaController', function ($scope, $location, $firebaseAuth, $firebaseObject) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        var fletes;

        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.$value) {
                case 'administrador':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            fletes = childSnapshot.val();
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            var estatusFlete = buscarEstatusFlete(fletes);
                            arrayFletes.push({
                                id: fletes.flete.idFlete,
                                firebaseId: fletes.flete.firebaseId,
                                cliente: fletes.flete.cliente,
                                transportista: transportistaSeleccionado,
                                name: fletes.flete.cliente,
                                startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                estatus: estatusFlete
                            });
                            if (estatusFlete === 'Por Cotizar'
                                || estatusFlete === 'Esperando Por Transportista'
                                || estatusFlete === 'Transportisa Por Confirmar'
                                || estatusFlete === 'Unidades Por Asignar') {
                                arrayListado.push({
                                    id: fletes.flete.idFlete,
                                    firebaseId: fletes.flete.firebaseId,
                                    cliente: fletes.flete.cliente,
                                    transportista: transportistaSeleccionado,
                                    name: fletes.flete.cliente,
                                    startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    estatus: estatusFlete
                                });
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
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
                    break;
                default:
            }
        });

        //BUSCAR LOS TRANSPORTISTAS SELECCIONADOS DEL FLETE
        var buscarTransportistaSeleccionado = function (fletes, snapshot) {
            var transportistaSeleccionado = "Sin transportista asignado";
            if (fletes.transportistaSeleccionado !== undefined) {
                snapshot.forEach(function (childSnapshot) {
                    if (childSnapshot.key === 'transportistaSeleccionado') {
                        childSnapshot.forEach(function (transportistaSnapshot) {
                            var transportista = transportistaSnapshot.val();
                            transportistaSeleccionado = transportista.nombre;
                        })
                    }
                });
            }
            return transportistaSeleccionado;
        }

        //BUSCAR ESTATUS DEL FLETE
        var buscarEstatusFlete = function (fletes) {
            var estatusFlete = "";
            switch (fletes.flete.estatus) {
                case "fletePorCotizar":
                    estatusFlete = "Por Cotizar";
                    break;
                case "esperandoPorTransportista":
                    estatusFlete = "Esperando Por Transportista";
                    break;
                case "transportistaPorConfirmar":
                    estatusFlete = "Transportisa Por Confirmar";
                    break;
                case "unidadesPorAsignar":
                    estatusFlete = "Unidades Por Asignar";
                    break;
                case "enProgreso":
                    estatusFlete = "En Progreso";
                    break;
                case "envioPorIniciar":
                    estatusFlete = "Envio Por Iniciar"
                    break;
                case "entregado":
                    estatusFlete = "Entregado";
                case "finalizado":
                    estatusFlete = "Finalizado";
                    break;
                case "cancelado":
                    estatusFlete = "Cancelado";
                    break;
                default:
                    break;
            }
            return estatusFlete;
        }

        var iniciar_calendario = function (div, fletes) {
            var currentYear = new Date().getFullYear();

            //DETERMINAR FECHA DE HOY
            var hoy = new Date(currentYear, new Date().getMonth(), new Date().getDate()).getTime();

            $(div).calendar({
                enableContextMenu: true,

                customDayRenderer: function (element, date) {
                    if (date.getTime() == hoy) {
                        $(element).css('background-color', '#F34235');
                        $(element).css('font-weight', 'bold');
                        $(element).css('color', 'white');
                        $(element).css('padding', '0px');
                    }
                },

                enableRangeSelection: true,
                language: 'es',
                contextMenuItems: [
                    {
                        text: 'Consultar',
                        click: detalle
                    }
                ],
                selectRange: function (e) {
                    editEvent({ startDate: e.startDate, endDate: e.endDate });
                },
                mouseOnDay: function (e) {
                    if (e.events.length > 0) {
                        var content = '';

                        for (var i in e.events) {
                            if (i < 3) {
                                content += '<div class="event-tooltip-content">'
                                    + '<div class="event-name"><span>Estatus: </span><span style="color:' + e.events[i].color + '"><b>' + e.events[i].estatus + '</b></span></div>'
                                    + '<div class="event-name">Cliente: <b>' + e.events[i].cliente + '</b></div>'
                                    + '<div class="event-location">Transportista: <b>' + e.events[i].transportista + '</b></div>'
                                    + '</div>'
                                    + '<hr/>';
                            }
                            else {
                                content += '<div class="event-tooltip-content">'
                                    + '<div class="event-name text-center">' + (e.events.length - 3).toString() + ' fletes más</div>';
                                break;
                            }
                        }

                        $(e.element).popover({
                            trigger: 'manual',
                            container: 'body',
                            html: true,
                            content: content
                        });

                        $(e.element).popover('show');
                    }
                },
                mouseOutDay: function (e) {
                    if (e.events.length > 0) {
                        $(e.element).popover('hide');
                    }
                },
                dayContextMenu: function (e) {
                    $(e.element).popover('hide');
                },
                dataSource: fletes
            });

            function detalle(event) {
                document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                var id = event.firebaseId;
                $location.path('/CHAOL/Fletes/' + id);
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }
        }
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
