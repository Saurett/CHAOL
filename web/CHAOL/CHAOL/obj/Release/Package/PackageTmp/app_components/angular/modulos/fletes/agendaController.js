//AGENDA CONTROLLER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    var app = angular.module('app');

    app.controller('agendaController', function ($scope, $location, $firebaseAuth, $firebaseObject, $mdDialog, unixTime) {
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();
        var fletes;

        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        firebaseUsuario.$loaded().then(function () {
            switch (firebaseUsuario.tipoDeUsuario) {
                case 'colaborador':
                case 'administrador':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            //TRANSPORTISTA SELECCIONADO
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            //ESTATUS DEL FLETE
                            var estatusFlete = buscarEstatusFlete(fletes);
                            //ICONO QUE INDICA LA ALERTA
                            var alerta = false;
                            if (estatusFlete === 'Por Cotizar'
                                || estatusFlete === 'Transportista Por Confirmar') {
                                alerta = true;
                            }

                            //ANEXO A ARREGLO PARA CALENDARIO
                            arrayFletes.push({
                                id: fletes.flete.idFlete,
                                firebaseId: fletes.flete.firebaseId,
                                cliente: fletes.flete.cliente,
                                transportista: transportistaSeleccionado,
                                name: fletes.flete.cliente,
                                startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                estatus: estatusFlete,
                                alert: alerta
                            });

                            //ANEXO A ARREGLO PARA LA LISTA DE LA DERECHA
                            if (estatusFlete === 'Por Cotizar'
                                || estatusFlete === 'Esperando Por Transportista'
                                || estatusFlete === 'Transportista Por Confirmar'
                                || estatusFlete === 'Unidades Por Asignar'
                                || estatusFlete === 'Envío Por Iniciar') {
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
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            if (fletes.bodegaDeCarga.firebaseIdDelCliente === usuario.uid) {
                                //TRANSPORTISTA SELECCIONADO
                                var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                                //ESTATUS DEL FLETE
                                var estatusFlete = buscarEstatusFlete(fletes);
                                //ICONO QUE INDICA LA ALERTA
                                var alerta = false;

                                //ANEXO A ARREGLO PARA CALENDARIO
                                arrayFletes.push({
                                    id: fletes.flete.idFlete,
                                    firebaseId: fletes.flete.firebaseId,
                                    cliente: fletes.flete.cliente,
                                    transportista: transportistaSeleccionado,
                                    name: fletes.flete.cliente,
                                    startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                    estatus: estatusFlete,
                                    alert: alerta
                                });

                                //ANEXO A ARREGLO PARA LA LISTA DE LA DERECHA
                                if (estatusFlete === 'Por Cotizar'
                                    || estatusFlete === 'Esperando Por Transportista'
                                    || estatusFlete === 'Transportista Por Confirmar'
                                    || estatusFlete === 'Unidades Por Asignar'
                                    || estatusFlete === 'Envío Por Iniciar') {
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
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                case 'transportista':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            //TRANSPORTISTA SELECCIONADO
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            //ESTATUS DEL FLETE
                            var estatusFlete = buscarEstatusFlete(fletes);
                            //ICONO QUE INDICA LA ALERTA
                            var alerta = true;

                            switch (estatusFlete) {
                                case 'Esperando Por Transportista':
                                case 'Transportista Por Confirmar':
                                    childSnapshot.forEach(function (nodoSnapshot) {
                                        if (nodoSnapshot.key === 'transportistasInteresados') {
                                            nodoSnapshot.forEach(function (interesadosSnapshot) {
                                                var interesado = interesadosSnapshot.val();
                                                var existe = false;
                                                if (interesado.firebaseId === usuario.uid) {
                                                    var existe = true;
                                                    alerta = false;
                                                }
                                                if (!existe) {
                                                    estatusFlete = 'Esperando Por Transportista';
                                                }
                                            });
                                        }
                                    });
                                    arrayFletes.push({
                                        id: fletes.flete.idFlete,
                                        firebaseId: fletes.flete.firebaseId,
                                        cliente: fletes.flete.cliente,
                                        transportista: transportistaSeleccionado,
                                        name: fletes.flete.cliente,
                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                        estatus: estatusFlete,
                                        alert: alerta
                                    });
                                    break;
                                case 'Unidades Por Asignar':
                                case 'Envío Por Iniciar':
                                case 'En Progreso':
                                case 'Entregado':
                                case 'Finalizado':
                                case 'Cancelado':
                                    if (estatusFlete === 'Entregado'
                                        || estatusFlete == 'Finalizado'
                                        || estatusFlete === 'Cancelado') {
                                        alerta = false;
                                    }
                                    childSnapshot.forEach(function (nodoSnapshot) {
                                        if (nodoSnapshot.key === 'transportistaSeleccionado') {
                                            nodoSnapshot.forEach(function (transportistaSnapshot) {
                                                var transportista = transportistaSnapshot.val();
                                                if (transportista.firebaseId === usuario.uid) {
                                                    arrayFletes.push({
                                                        id: fletes.flete.idFlete,
                                                        firebaseId: fletes.flete.firebaseId,
                                                        cliente: fletes.flete.cliente,
                                                        transportista: transportistaSeleccionado,
                                                        name: fletes.flete.cliente,
                                                        startDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        endDate: new Date(parseInt(fletes.flete.fechaDeSalida * 1000)),
                                                        estatus: estatusFlete,
                                                        alert: alerta
                                                    });
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
                                            })
                                        }
                                    });
                                    break;
                                default:
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                case 'chofer':
                    //FLETES
                    var refFletes = firebase.database().ref().child('fletesPorAsignar').orderByChild('flete/fechaDeSalida');
                    refFletes.on("value", function (snapshot) {
                        var arrayListado = [];
                        var arrayFletes = [];
                        snapshot.forEach(function (childSnapshot) {
                            //FLETE ENCONTRADO
                            fletes = childSnapshot.val();
                            //TRANSPORTISTA SELECCIONADO
                            var transportistaSeleccionado = buscarTransportistaSeleccionado(fletes, childSnapshot);
                            //ESTATUS DEL FLETE
                            var estatusFlete = buscarEstatusFlete(fletes);
                            //ICONO QUE INDICA LA ALERTA
                            var alerta = false;

                            switch (estatusFlete) {
                                case 'Envío Por Iniciar':
                                case 'En Progreso':
                                case 'Entregado':
                                case 'Finalizado':
                                case 'Cancelado':
                                    childSnapshot.forEach(function (nodoSnapshot) {
                                        if (nodoSnapshot.key === 'choferSeleccionado') {
                                            nodoSnapshot.forEach(function (transportistaSnapshot) {
                                                var transportista = transportistaSnapshot.val();
                                                if (transportista.firebaseId === usuario.uid) {
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
                                            })
                                        }
                                    });
                                    break;
                                default:
                            }
                        });
                        $scope.listadoFletes = arrayListado;
                        iniciar_calendario('#div_calendario', arrayFletes);
                        document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                    });
                    break;
                default:
                    break;
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
                    estatusFlete = "Transportista Por Confirmar";
                    break;
                case "unidadesPorAsignar":
                    estatusFlete = "Unidades Por Asignar";
                    break;
                case "enProgreso":
                    estatusFlete = "En Progreso";
                    break;
                case "envioPorIniciar":
                    estatusFlete = "Envío Por Iniciar"
                    break;
                case "entregado":
                    estatusFlete = "Entregado";
                    break;
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
                    },
                    {
                        text: 'Cancelar',
                        click: cancelar
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
                $scope.$apply();
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            }

            function cancelar(event) {
                document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                var id = event.firebaseId;

                var refFlete = firebase.database().ref().child('fletesPorAsignar').child(id).child('flete');
                var firebaseFlete = $firebaseObject(refFlete);
                firebaseFlete.$loaded().then(function () {
                    switch (firebaseFlete.estatus) {
                        case 'fletePorCotizar':
                        case 'esperandoPorTransportista':
                        case 'transportistaPorConfirmar':
                        case 'unidadesPorAsignar':
                        case 'envioPorIniciar':
                            var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
                            var firebaseUsuario = $firebaseObject(refUsuario);
                            firebaseUsuario.$loaded().then(function () {
                                switch (firebaseUsuario.tipoDeUsuario) {
                                    case 'administrador':
                                    case 'colaborador':
                                    case 'cliente':
                                        $mdDialog.show($mdDialog.confirm()
                                            .parent(angular.element(document.querySelector('#tractores')))
                                            .title('¿Cancelar flete?')
                                            .htmlContent('<br/> <p>¿Estás seguro que deseas cancelar este flete?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                                            .ariaLabel('Alert Dialog Demo')
                                            .ok('Sí, deseo cancelarlo')
                                            .cancel('No, prefiero continuar')
                                        ).then(function () {
                                            //CANCELACIÓN DEL FLETE
                                            firebaseFlete.fechaDeEdicion = unixTime();
                                            firebaseFlete.estatus = "cancelado";
                                            firebaseFlete.$save();
                                            $mdDialog.show(
                                                $mdDialog.alert()
                                                    .parent(angular.element(document.querySelector('#flete')))
                                                    .clickOutsideToClose(false)
                                                    .title('Registro correcto')
                                                    .htmlContent('<br /> <p>Flete cancelado correctamente. </p> <p> Hemos cancelado correctamente el flete correspondiente.</p>')
                                                    .ariaLabel('Alert Dialog Demo')
                                                    .ok('Aceptar')
                                            ).then(function () {
                                                removerListener();
                                                $location.path('/CHAOL/Fletes');
                                            });
                                        });
                                        break;
                                    case 'transportista':
                                    case 'chofer':
                                        $mdDialog.show(
                                            $mdDialog.alert()
                                                .parent(angular.element(document.querySelector('#registro')))
                                                .clickOutsideToClose(false)
                                                .title('Oops! Algo ha ocurrido')
                                                .htmlContent('<br/> <p>Al parecer no cuenta con los permisos necesarios para cancelar el flete.</p><p>Por favor, contacte al administrador del sistema para más información.</p> <br/>')
                                                .ariaLabel('Alert Dialog Demo')
                                                .ok('Aceptar')
                                        );
                                        break;
                                }
                                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            });
                            break;
                        default:
                            $mdDialog.show(
                                $mdDialog.alert()
                                    .parent(angular.element(document.querySelector('#registro')))
                                    .clickOutsideToClose(false)
                                    .title('Oops! Algo ha ocurrido')
                                    .htmlContent('<br/> <p>No es posible cancelar el flete en este momento.</p><p>Por favor, verifique que el estatus del flete permita la cancelación o contacte al administrador del sistema para más información.</p> <br/>')
                                    .ariaLabel('Alert Dialog Demo')
                                    .ok('Aceptar')
                            );
                            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            break;
                    }
                });
            }
        }
    });
})();
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
