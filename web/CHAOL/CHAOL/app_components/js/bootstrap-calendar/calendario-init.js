function iniciar_calendario(div) {
    var currentYear = new Date().getFullYear();

    //DETERMINAR FECHA DE HOY
    var hoy = new Date(currentYear, new Date().getMonth(), new Date().getDate()).getTime();

    //CONSULTAR LOS FLETES CORRESPONDIENTES
    var auth = $firebaseAuth();
    var usuario = auth.$getAuth();
    var fletes;

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
                        arrayFletes.push({
                            id: fletes.flete.idFlete,
                            cliente: fletes.flete.cliente,
                            trasnportista: "ninguno",
                            name: "este es el nombre",
                            startDate: fletes.flete.fechaDeSalida,
                            endDate: fletes.flete.fechaDeSalida,
                            estatus: fletes.flete.estatus
                        });
                    });
                    fletes = arrayFletes;
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

    $(div).calendar({
        enableContextMenu: true,

        customDayRenderer: function (element, date) {
            if (date.getTime() == hoy) {
                $(element).css('font-weight', 'bold');
                $(element).css('font-size', '15px');
                $(element).css('color', 'green');
            }
        },

        enableRangeSelection: true,
        language: 'es',
        contextMenuItems: [
            {
                text: 'Detalle'
                //click: editEvent
            }
        ],
        selectRange: function (e) {
            editEvent({ startDate: e.startDate, endDate: e.endDate });
        },
        mouseOnDay: function (e) {
            if (e.events.length > 0) {
                var content = '';

                for (var i in e.events) {
                    content += '<div class="event-tooltip-content">'
                        + '<div class="event-name" style="color:' + e.events[i].color + '">Cliente: ' + e.events[i].cliente + '</div>'
                        + '<div class="event-location">Transportista: ' + e.events[i].transportista + '</div>'
                        + '</div>';
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
}