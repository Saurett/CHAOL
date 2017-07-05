(function () {
    var app = angular.module('app');

    app.controller('fleteController', function ($scope, $firebaseAuth, $firebaseObject, $firebaseArray, $location, $routeParams, $mdDialog, unixTime, convertToUnixTime) {
        //PROPIEDADES DE PANELES
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.paneles = {
            progreso: '0%',
            resumen: {
                controles: {
                    estatus: true,
                    cliente: true,
                    transportista: true,
                    iniciarEnvio: true,
                    entregado: true,
                    recibido: true,
                    cancelar: true,
                    solicitarViaje: true,
                    cancelarSolicitud: true
                }
            },
            datosGenerales: {
                enabled: true,
                iconClass: "",
                tabClass: "collapse",
                cardClass: "card",
                controles: {
                    cliente: true,
                    fechaDeSalida: true,
                    horaDeSalida: true,
                    bodegaDeCarga: true,
                    bodegaDeDescarga: true,
                    tipoDeRemolque: true,
                    carga: true,
                    numeroDeEmbarque: true,
                    destinatario: true,
                    solicitarCotizacion: true,
                    guardarCambios: true
                }
            },
            cotizacion: {
                enabled: true,
                iconClass: "",
                tabClass: "collapse",
                cardClass: "card",
                controles: {
                    precio: true,
                    guardar: true,
                    solicitarViaje: true
                }
            },
            solicitudTransportista: {
                enabled: true,
                iconClass: "",
                tabClass: "collapse",
                cardClass: "card",
                controles: {
                    transportistas: true,
                    autorizarTransportista: true
                }
            },
            equipo: {
                enabled: true,
                iconClass: "",
                tabClass: "collapse",
                cardClass: "card",
                controles: {
                    chofer: true,
                    tractor: true,
                    remolque: true,
                    asignarEquipo: true
                }
            }
        }

        //INICIALIZAR FLETE
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.firebaseFlete = {
            flete: {
                bodegaDeCarga: "",
                bodegaDeDescarga: "",
                carga: "",
                cliente: "",
                destinatario: "",
                estatus: "Nuevo",
                fechaDeCreacion: unixTime(),
                fechaDeEdicion: unixTime(),
                fechaDeSalida: new Date(),
                firebaseId: "",
                horaDeSalida: 0,
                idFlete: "",
                numeroDeEmbarque: "",
                precio: "",
                tipoDeRemolque: ""
            }
        }

        //USUARIO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var auth = $firebaseAuth();
        var usuario = auth.$getAuth();

        var refUsuario = firebase.database().ref('usuarios').child(usuario.uid);
        var firebaseUsuario = $firebaseObject(refUsuario);
        var counter = 0;

        //LISTADO CLIENTES
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var refClientes = firebase.database().ref().child('clientes');
        refClientes.on("value", function (snapshot) {
            var arrayClientes = [];
            snapshot.forEach(function (childSnapshot) {
                clientes = childSnapshot.val();
                if (clientes.cliente.estatus !== 'eliminado' && clientes.cliente.estatus !== 'inactivo') {
                    childSnapshot.forEach(function (clienteSnapShot) {
                        if (clienteSnapShot.key === 'cliente') {
                            var cliente = clienteSnapShot.val();
                            arrayClientes.push(cliente);
                        }
                    })
                }
            });
            $scope.clientes = arrayClientes;
        });

        //LISTADO REMOLQUES
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.tiposDeRemolque = [
            "Refrigerante"
        ]

        //BUSCAR BODEGAS DE CARGA DEL CLIENTE
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.buscarBodegas = function () {
            var nombreCliente = $scope.firebaseFlete.flete.cliente;
            var refClientes = firebase.database().ref().child('clientes');
            refClientes.on("value", function (snapshot) {
                snapshot.forEach(function (childSnapshot) {
                    clientes = childSnapshot.val();
                    if (clientes.cliente.nombre === nombreCliente) {
                        var firebaseIdDelCliente = clientes.cliente.firebaseId;
                        var refBodega = firebase.database().ref().child('clientes').child(firebaseIdDelCliente)
                        refBodega.on('value', function (snapshot) {
                            var arrayBodegas = [];
                            snapshot.forEach(function (childSnapshot) {
                                if (childSnapshot.key === 'bodegas') {
                                    childSnapshot.forEach(function (nodoSnapshot) {
                                        var bodega = nodoSnapshot.val();
                                        if (bodega.estatus !== 'eliminado') {
                                            arrayBodegas.push(bodega);
                                        }
                                    });
                                }
                            });
                            $scope.bodegas = arrayBodegas;
                        });
                    }
                });
            });
        }

        //BUSCAR CHOFER SELECCIONADO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.buscarChofer = function () {
            var refChoferes = firebase.database().ref().child('choferes').child($scope.choferSeleccionado);
            refChoferes.on("value", function (snapshot) {
                chofer = snapshot.val();
                $scope.firebaseFlete.choferSeleccionado = {
                    celular1: chofer.celular1,
                    firebaseId: chofer.firebaseId,
                    imagenURL: chofer.imagenURL,
                    nombre: chofer.nombre,
                    numeroDeLicencia: chofer.numeroDeLicencia
                }
            });
        }

        //BUSCAR TRACTOR SELECCIONADO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.buscarTractor = function () {
            var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores').child($scope.tractorSeleccionado);
            refTractores.on("value", function (snapshot) {
                tractor = snapshot.val();
                $scope.firebaseFlete.tractorSeleccionado = {
                    marca: tractor.marca,
                    firebaseId: tractor.firebaseId,
                    modelo: tractor.modelo,
                    numeroEconomico: tractor.numeroEconomico,
                    placa: tractor.placa
                }
            });
        }

        //BUSCAR REMOLQUE SELECCIONADO
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        $scope.buscarRemolque = function () {
            var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques').child($scope.remolqueSeleccionado);
            refRemolques.on("value", function (snapshot) {
                remolque = snapshot.val();
                $scope.firebaseFlete.remolqueSeleccionado = {
                    marca: remolque.marca,
                    firebaseId: remolque.firebaseId,
                    modelo: remolque.modelo,
                    numeroEconomico: remolque.numeroEconomico,
                    placa: remolque.placa
                }
            });
        }

        //CONSULTA DE ID DE FLETE
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var refFlete;
        if ($routeParams.ID) {
            refFlete = firebase.database().ref('fletesPorAsignar').child($routeParams.ID);
            $scope.firebaseFlete = $firebaseObject(refFlete);
            refFlete.on('value', function (snap) {
                var fleteBD = snap.val();
                if ($scope.firebaseFlete.flete !== undefined) {
                    if (fleteBD.flete.fechaDeEdicion !== $scope.firebaseFlete.flete.fechaDeEdicion && $location.path() === '/CHAOL/Fletes/' + fleteBD.flete.firebaseId) {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#registro')))
                                .clickOutsideToClose(false)
                                .title('Oops! Algo ha cambiado')
                                .htmlContent('<br/> <p>Al parecer se ha modificado la información del flete. Por favor, verifica la información antes de guardar los cambios.</p> <br/>')
                                .ariaLabel('Alert Dialog Demo')
                                .ok('Aceptar')
                        );
                    }
                }

                $scope.firebaseFlete.$value = snap.key;

                //ACTIVACIÓN DINÁMICA DE PANELES EN EL OBJETO
                //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                firebaseUsuario.$loaded().then(function () {
                    switch (firebaseUsuario.tipoDeUsuario) {
                        case 'administrador':
                    case 'colaborador':
                            //ACTIVAR LOS PANELES
                            $scope.paneles.datosGenerales.disabled = false;
                            $scope.paneles.cotizacion.disabled = false;
                            $scope.paneles.solicitudTransportista.disabled = false;
                            $scope.paneles.equipo.disabled = false;

                            //VALIDACIÓN DE FLETE
                            $scope.firebaseFlete.$loaded().then(function () {
                                $scope.firebaseFlete.flete.fechaDeSalida = new Date(parseInt($scope.firebaseFlete.flete.fechaDeSalida * 1000));
                                $scope.buscarBodegas();
                                switch ($scope.firebaseFlete.flete.estatus) {
                                    case "fletePorCotizar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '15%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-waiting";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-clock-o";
                                        $scope.paneles.solicitudTransportista.cardClass = "card";
                                        $scope.paneles.solicitudTransportista.iconClass = "";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = true;
                                        $scope.paneles.cotizacion.controles.guardar = true;
                                        $scope.paneles.cotizacion.controles.solicitarViaje = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Flete Por Cotizar";
                                        break;
                                    case "esperandoPorTransportista":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '30%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-waiting";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-clock-o";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = true;
                                        $scope.paneles.cotizacion.controles.guardar = true;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = true;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = true;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Esperando Por Transportista";
                                        break;
                                    case "transportistaPorConfirmar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '45%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-waiting";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-clock-o";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = true;
                                        $scope.paneles.cotizacion.controles.guardar = true;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = true;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = true;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Transportista Por Confirmar";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                arrayInteresados.push(transportistas);
                                            });
                                            $scope.transportistasInteresados = arrayInteresados;
                                        });
                                        break;
                                    case "unidadesPorAsignar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '70%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-waiting";
                                        $scope.paneles.equipo.iconClass = "fa fa-clock-o";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = true;
                                        $scope.paneles.cotizacion.controles.guardar = true;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = true;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = true;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Unidades Por Asignar";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                arrayInteresados.push(transportistas);
                                            });
                                            $scope.transportistasInteresados = arrayInteresados;
                                        });
                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                            });
                                        });
                                        break;
                                    case "envioPorIniciar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '80%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = true;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = true;
                                        $scope.paneles.cotizacion.controles.guardar = true;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Envío Por Iniciar";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                arrayInteresados.push(transportistas);
                                            });
                                            $scope.transportistasInteresados = arrayInteresados;
                                        });
                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                                //LISTADO CHOFERES
                                                var refChoferes = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('choferes');
                                                refChoferes.on("value", function (snapshot) {
                                                    var arrayChoferes = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        choferes = childSnapshot.val();
                                                        if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                            arrayChoferes.push(choferes);
                                                        }
                                                    });
                                                    $scope.choferes = arrayChoferes;
                                                    //BUSCAR CHOFER SELECCIONADO
                                                    var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                                    refChoferSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.choferSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                                //LISTADO TRACTORES
                                                var refTractores = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('tractores');
                                                refTractores.on("value", function (snapshot) {
                                                    var arrayTractores = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        tractores = childSnapshot.val();
                                                        if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                            arrayTractores.push(tractores);
                                                        }
                                                    });
                                                    $scope.tractores = arrayTractores;
                                                    //BUSCAR TRACTOR SELECCIONADO
                                                    var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                                    refTractorSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.tractorSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });

                                                //LISTADO REMOLQUES
                                                var refRemolques = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('remolques');
                                                refRemolques.on("value", function (snapshot) {
                                                    var arrayRemolques = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        remolques = childSnapshot.val();
                                                        if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                            arrayRemolques.push(remolques);
                                                        }
                                                    });
                                                    $scope.remolques = arrayRemolques;
                                                    //BUSCAR REMOLQUE SELECCIONADO
                                                    var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                                    refRemolqueSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.remolqueSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                            });
                                        });
                                        break;
                                    case "enProgreso":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '90%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = true;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "En Progreso";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                arrayInteresados.push(transportistas);
                                            });
                                            $scope.transportistasInteresados = arrayInteresados;
                                        });
                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;

                                                //LISTADO CHOFERES
                                                var refChoferes = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('choferes');
                                                refChoferes.on("value", function (snapshot) {
                                                    var arrayChoferes = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        choferes = childSnapshot.val();
                                                        if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                            arrayChoferes.push(choferes);
                                                        }
                                                    });
                                                    $scope.choferes = arrayChoferes;
                                                    //BUSCAR CHOFER SELECCIONADO
                                                    var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                                    refChoferSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.choferSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                                //LISTADO TRACTORES
                                                var refTractores = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('tractores');
                                                refTractores.on("value", function (snapshot) {
                                                    var arrayTractores = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        tractores = childSnapshot.val();
                                                        if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                            arrayTractores.push(tractores);
                                                        }
                                                    });
                                                    $scope.tractores = arrayTractores;
                                                    //BUSCAR TRACTOR SELECCIONADO
                                                    var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                                    refTractorSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.tractorSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });

                                                //LISTADO REMOLQUES
                                                var refRemolques = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('remolques');
                                                refRemolques.on("value", function (snapshot) {
                                                    var arrayRemolques = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        remolques = childSnapshot.val();
                                                        if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                            arrayRemolques.push(remolques);
                                                        }
                                                    });
                                                    $scope.remolques = arrayRemolques;
                                                    //BUSCAR REMOLQUE SELECCIONADO
                                                    var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                                    refRemolqueSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.remolqueSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                            });
                                        });
                                        break;
                                    case "entregado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '95%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = true;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Entregado";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                arrayInteresados.push(transportistas);
                                            });
                                            $scope.transportistasInteresados = arrayInteresados;
                                        });
                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                                //LISTADO CHOFERES
                                                var refChoferes = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('choferes');
                                                refChoferes.on("value", function (snapshot) {
                                                    var arrayChoferes = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        choferes = childSnapshot.val();
                                                        if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                            arrayChoferes.push(choferes);
                                                        }
                                                    });
                                                    $scope.choferes = arrayChoferes;
                                                    //BUSCAR CHOFER SELECCIONADO
                                                    var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                                    refChoferSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.choferSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                                //LISTADO TRACTORES
                                                var refTractores = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('tractores');
                                                refTractores.on("value", function (snapshot) {
                                                    var arrayTractores = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        tractores = childSnapshot.val();
                                                        if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                            arrayTractores.push(tractores);
                                                        }
                                                    });
                                                    $scope.tractores = arrayTractores;
                                                    //BUSCAR TRACTOR SELECCIONADO
                                                    var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                                    refTractorSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.tractorSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });

                                                //LISTADO REMOLQUES
                                                var refRemolques = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('remolques');
                                                refRemolques.on("value", function (snapshot) {
                                                    var arrayRemolques = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        remolques = childSnapshot.val();
                                                        if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                            arrayRemolques.push(remolques);
                                                        }
                                                    });
                                                    $scope.remolques = arrayRemolques;
                                                    //BUSCAR REMOLQUE SELECCIONADO
                                                    var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                                    refRemolqueSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.remolqueSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                            });
                                        });
                                        break;
                                    case "finalizado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '100%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Finalizado";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                arrayInteresados.push(transportistas);
                                            });
                                            $scope.transportistasInteresados = arrayInteresados;
                                        });
                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();

                                                //LISTADO CHOFERES
                                                var refChoferes = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('choferes');
                                                refChoferes.on("value", function (snapshot) {
                                                    var arrayChoferes = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        choferes = childSnapshot.val();
                                                        if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                            arrayChoferes.push(choferes);
                                                        }
                                                    });
                                                    $scope.choferes = arrayChoferes;
                                                    //BUSCAR CHOFER SELECCIONADO
                                                    var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                                    refChoferSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.choferSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                                //LISTADO TRACTORES
                                                var refTractores = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('tractores');
                                                refTractores.on("value", function (snapshot) {
                                                    var arrayTractores = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        tractores = childSnapshot.val();
                                                        if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                            arrayTractores.push(tractores);
                                                        }
                                                    });
                                                    $scope.tractores = arrayTractores;
                                                    //BUSCAR TRACTOR SELECCIONADO
                                                    var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                                    refTractorSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.tractorSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });

                                                //LISTADO REMOLQUES
                                                var refRemolques = firebase.database().ref().child('transportistas').child($scope.idTransportistaSeleccionado).child('remolques');
                                                refRemolques.on("value", function (snapshot) {
                                                    var arrayRemolques = [];
                                                    snapshot.forEach(function (childSnapshot) {
                                                        remolques = childSnapshot.val();
                                                        if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                            arrayRemolques.push(remolques);
                                                        }
                                                    });
                                                    $scope.remolques = arrayRemolques;
                                                    //BUSCAR REMOLQUE SELECCIONADO
                                                    var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                                    refRemolqueSeleccionado.on('value', function (snapshot) {
                                                        snapshot.forEach(function (childSnapshot) {
                                                            $scope.remolqueSeleccionado = childSnapshot.key;
                                                            $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                                        })
                                                    });
                                                });
                                            });
                                        });
                                        break;
                                    case 'cancelado':
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '100%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = true;
                                        $scope.paneles.solicitudTransportista.enabled = true;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        $scope.paneles.cotizacion.controles.solicitarViaje = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Cancelado";
                                        break;
                                    default:
                                        break;
                                }
                            });
                            break;
                        case 'cliente':
                            //ACTIVAR LOS PANELES
                            $scope.paneles.datosGenerales.disabled = false;
                            $scope.paneles.cotizacion.disabled = false;
                            $scope.paneles.solicitudTransportista.disabled = false;
                            $scope.paneles.equipo.disabled = false;

                            //VALIDACIÓN DE FLETE
                            $scope.firebaseFlete.$loaded().then(function () {
                                $scope.firebaseFlete.flete.fechaDeSalida = new Date(parseInt($scope.firebaseFlete.flete.fechaDeSalida * 1000));
                                $scope.buscarBodegas();
                                switch ($scope.firebaseFlete.flete.estatus) {
                                    case "fletePorCotizar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '15%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-waiting";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-clock-o";
                                        $scope.paneles.solicitudTransportista.cardClass = "card";
                                        $scope.paneles.solicitudTransportista.iconClass = "";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = true;
                                        $scope.paneles.cotizacion.controles.guardar = true;
                                        $scope.paneles.cotizacion.controles.solicitarViaje = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Flete Por Cotizar";
                                        break;
                                    case "esperandoPorTransportista":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '30%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-waiting";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-clock-o";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Esperando Por Transportista";
                                        break;
                                    case "transportistaPorConfirmar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '45%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-waiting";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-clock-o";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Transportista Por Confirmar";
                                        break;
                                    case "unidadesPorAsignar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '70%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-waiting";
                                        $scope.paneles.equipo.iconClass = "fa fa-clock-o";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Unidades Por Asignar";
                                        break;
                                    case "envioPorIniciar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '80%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Envío Por Iniciar";

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        break;
                                    case "enProgreso":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '90%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "En Progreso";

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        break;
                                    case "entregado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '95%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = true;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Entregado";

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        break;
                                    case "finalizado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '100%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Finalizado";

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        break;
                                    case 'cancelado':
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '100%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        $scope.paneles.cotizacion.controles.solicitarViaje = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Cancelado";
                                        break;
                                    default:
                                        break;
                                }
                            });
                            break;
                        case 'transportista':
                            //ACTIVAR LOS PANELES
                            $scope.paneles.datosGenerales.disabled = false;
                            $scope.paneles.cotizacion.disabled = false;
                            $scope.paneles.solicitudTransportista.disabled = false;
                            $scope.paneles.equipo.disabled = false;

                            //VALIDACIÓN DE FLETE
                            $scope.firebaseFlete.$loaded().then(function () {
                                $scope.firebaseFlete.flete.fechaDeSalida = new Date(parseInt($scope.firebaseFlete.flete.fechaDeSalida * 1000));
                                $scope.buscarBodegas();
                                switch ($scope.firebaseFlete.flete.estatus) {
                                    case "esperandoPorTransportista":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '30%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-waiting";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-clock-o";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = true;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Esperando Por Transportista";
                                        break;
                                    case "transportistaPorConfirmar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '45%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-waiting";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-clock-o";
                                        $scope.paneles.equipo.cardClass = "card";
                                        $scope.paneles.equipo.iconClass = "";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = false;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = true;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = true;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = true;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = true;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Transportista Por Confirmar";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');
                                        refTransportistas.once("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                if (transportistas.firebaseId === usuario.uid) {
                                                    $scope.paneles.resumen.controles.cancelarSolicitud = true;
                                                    $scope.paneles.resumen.controles.solicitarViaje = false;
                                                }
                                            });
                                        });
                                        break;
                                    case "unidadesPorAsignar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '70%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-waiting";
                                        $scope.paneles.equipo.iconClass = "fa fa-clock-o";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = true;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = true;
                                        $scope.paneles.equipo.controles.tractor = true;
                                        $scope.paneles.equipo.controles.remolque = true;
                                        $scope.paneles.equipo.controles.asignarEquipo = true;

                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Unidades Por Asignar";

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistas.once("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                if (transportistas.firebaseId === usuario.uid) {
                                                    $scope.paneles.resumen.controles.cancelarSolicitud = true;
                                                    $scope.paneles.resumen.controles.solicitarViaje = false;
                                                    $scope.firebaseFlete.transportistaSeleccionado = transportistas;
                                                }
                                                else {
                                                    $location.path('/CHAOL/Fletes');
                                                }
                                            });

                                            //LISTADO CHOFERES
                                            var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes');
                                            refChoferes.on("value", function (snapshot) {
                                                var arrayChoferes = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    choferes = childSnapshot.val();
                                                    if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                        arrayChoferes.push(choferes);
                                                    }
                                                });
                                                $scope.choferes = arrayChoferes;
                                            });
                                            //LISTADO TRACTORES
                                            var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores');
                                            refTractores.on("value", function (snapshot) {
                                                var arrayTractores = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    tractores = childSnapshot.val();
                                                    if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                        arrayTractores.push(tractores);
                                                    }
                                                });
                                                $scope.tractores = arrayTractores;
                                            });

                                            //LISTADO REMOLQUES
                                            var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques');
                                            refRemolques.on("value", function (snapshot) {
                                                var arrayRemolques = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    remolques = childSnapshot.val();
                                                    if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                        arrayRemolques.push(remolques);
                                                    }
                                                });
                                                $scope.remolques = arrayRemolques;
                                            });
                                        });
                                        break;
                                    case "envioPorIniciar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '80%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = true;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = true;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = true;
                                        $scope.paneles.equipo.controles.tractor = true;
                                        $scope.paneles.equipo.controles.remolque = true;
                                        $scope.paneles.equipo.controles.asignarEquipo = true;

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistas.once("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                if (transportistas.firebaseId === usuario.uid) {
                                                    $scope.paneles.resumen.controles.cancelarSolicitud = true;
                                                    $scope.paneles.resumen.controles.solicitarViaje = false;
                                                    $scope.firebaseFlete.transportistaSeleccionado = transportistas;
                                                }
                                                else {
                                                    $location.path('/CHAOL/Fletes');
                                                }
                                            });

                                            //LISTADO CHOFERES
                                            var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes');
                                            refChoferes.on("value", function (snapshot) {
                                                var arrayChoferes = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    choferes = childSnapshot.val();
                                                    if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                        arrayChoferes.push(choferes);
                                                    }
                                                });
                                                $scope.choferes = arrayChoferes;
                                            });
                                            //LISTADO TRACTORES
                                            var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores');
                                            refTractores.on("value", function (snapshot) {
                                                var arrayTractores = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    tractores = childSnapshot.val();
                                                    if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                        arrayTractores.push(tractores);
                                                    }
                                                });
                                                $scope.tractores = arrayTractores;
                                            });

                                            //LISTADO REMOLQUES
                                            var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques');
                                            refRemolques.on("value", function (snapshot) {
                                                var arrayRemolques = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    remolques = childSnapshot.val();
                                                    if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                        arrayRemolques.push(remolques);
                                                    }
                                                });
                                                $scope.remolques = arrayRemolques;
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.buscarChofer();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.buscarTractor();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.buscarRemolque();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Envío Por Iniciar";
                                        break;
                                    case "enProgreso":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '90%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = true;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistas.once("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                if (transportistas.firebaseId === usuario.uid) {
                                                    $scope.firebaseFlete.transportistaSeleccionado = transportistas;
                                                }
                                                else {
                                                    $location.path('/CHAOL/Fletes');
                                                }
                                            });

                                            //LISTADO CHOFERES
                                            var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes');
                                            refChoferes.on("value", function (snapshot) {
                                                var arrayChoferes = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    choferes = childSnapshot.val();
                                                    if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                        arrayChoferes.push(choferes);
                                                    }
                                                });
                                                $scope.choferes = arrayChoferes;
                                            });
                                            //LISTADO TRACTORES
                                            var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores');
                                            refTractores.on("value", function (snapshot) {
                                                var arrayTractores = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    tractores = childSnapshot.val();
                                                    if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                        arrayTractores.push(tractores);
                                                    }
                                                });
                                                $scope.tractores = arrayTractores;
                                            });

                                            //LISTADO REMOLQUES
                                            var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques');
                                            refRemolques.on("value", function (snapshot) {
                                                var arrayRemolques = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    remolques = childSnapshot.val();
                                                    if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                        arrayRemolques.push(remolques);
                                                    }
                                                });
                                                $scope.remolques = arrayRemolques;
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.buscarChofer();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.buscarTractor();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.buscarRemolque();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "En Progreso";
                                        break;
                                    case "entregado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '95%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistas.once("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                if (transportistas.firebaseId === usuario.uid) {
                                                    $scope.firebaseFlete.transportistaSeleccionado = transportistas;
                                                }
                                                else {
                                                    $location.path('/CHAOL/Fletes');
                                                }
                                            });

                                            //LISTADO CHOFERES
                                            var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes');
                                            refChoferes.on("value", function (snapshot) {
                                                var arrayChoferes = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    choferes = childSnapshot.val();
                                                    if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                        arrayChoferes.push(choferes);
                                                    }
                                                });
                                                $scope.choferes = arrayChoferes;
                                            });
                                            //LISTADO TRACTORES
                                            var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores');
                                            refTractores.on("value", function (snapshot) {
                                                var arrayTractores = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    tractores = childSnapshot.val();
                                                    if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                        arrayTractores.push(tractores);
                                                    }
                                                });
                                                $scope.tractores = arrayTractores;
                                            });

                                            //LISTADO REMOLQUES
                                            var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques');
                                            refRemolques.on("value", function (snapshot) {
                                                var arrayRemolques = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    remolques = childSnapshot.val();
                                                    if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                        arrayRemolques.push(remolques);
                                                    }
                                                });
                                                $scope.remolques = arrayRemolques;
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.buscarChofer();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.buscarTractor();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.buscarRemolque();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Entregado";
                                        break;
                                    case "finalizado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '100%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = false;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTAS INTERESADOS
                                        var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistas.once("value", function (snapshot) {
                                            var arrayInteresados = [];
                                            snapshot.forEach(function (childSnapshot) {
                                                transportistas = childSnapshot.val();
                                                if (transportistas.firebaseId === usuario.uid) {
                                                    $scope.firebaseFlete.transportistaSeleccionado = transportistas;
                                                }
                                                else {
                                                    $location.path('/CHAOL/Fletes');
                                                }
                                            });

                                            //LISTADO CHOFERES
                                            var refChoferes = firebase.database().ref().child('transportistas').child(usuario.uid).child('choferes');
                                            refChoferes.on("value", function (snapshot) {
                                                var arrayChoferes = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    choferes = childSnapshot.val();
                                                    if (choferes.estatus !== 'eliminado' && choferes.estatus !== 'inactivo') {
                                                        arrayChoferes.push(choferes);
                                                    }
                                                });
                                                $scope.choferes = arrayChoferes;
                                            });
                                            //LISTADO TRACTORES
                                            var refTractores = firebase.database().ref().child('transportistas').child(usuario.uid).child('tractores');
                                            refTractores.on("value", function (snapshot) {
                                                var arrayTractores = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    tractores = childSnapshot.val();
                                                    if (tractores.estatus !== 'eliminado' && tractores.estatus !== 'inactivo') {
                                                        arrayTractores.push(tractores);
                                                    }
                                                });
                                                $scope.tractores = arrayTractores;
                                            });

                                            //LISTADO REMOLQUES
                                            var refRemolques = firebase.database().ref().child('transportistas').child(usuario.uid).child('remolques');
                                            refRemolques.on("value", function (snapshot) {
                                                var arrayRemolques = [];
                                                snapshot.forEach(function (childSnapshot) {
                                                    remolques = childSnapshot.val();
                                                    if (remolques.estatus !== 'eliminado' && remolques.estatus !== 'inactivo') {
                                                        arrayRemolques.push(remolques);
                                                    }
                                                });
                                                $scope.remolques = arrayRemolques;
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.buscarChofer();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.buscarTractor();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.buscarRemolque();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Finalizado";
                                        break;
                                    default:

                                        break;
                                }
                            });
                            break;
                        case 'chofer':
                            //ACTIVAR LOS PANELES
                            $scope.paneles.datosGenerales.disabled = false;
                            $scope.paneles.cotizacion.disabled = false;
                            $scope.paneles.solicitudTransportista.disabled = false;
                            $scope.paneles.equipo.disabled = false;

                            //VALIDACIÓN DE FLETE
                            $scope.firebaseFlete.$loaded().then(function () {
                                $scope.firebaseFlete.flete.fechaDeSalida = new Date(parseInt($scope.firebaseFlete.flete.fechaDeSalida * 1000));
                                $scope.buscarBodegas();
                                switch ($scope.firebaseFlete.flete.estatus) {
                                    case "envioPorIniciar":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '80%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = true;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Envío Por Iniciar";
                                        break;
                                    case "enProgreso":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '90%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = true;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = true;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Entregado";
                                        break;
                                    case "entregado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '95%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "En Progreso";
                                        break;
                                    case "finalizado":
                                        //ESTILO DE PANELES
                                        $scope.paneles.progreso = '100%';
                                        $scope.paneles.datosGenerales.cardClass = "card card-ok";
                                        $scope.paneles.datosGenerales.iconClass = "fa fa-check";
                                        $scope.paneles.cotizacion.cardClass = "card card-ok";
                                        $scope.paneles.cotizacion.iconClass = "fa fa-check";
                                        $scope.paneles.solicitudTransportista.cardClass = "card card-ok";
                                        $scope.paneles.solicitudTransportista.iconClass = "fa fa-check";
                                        $scope.paneles.equipo.cardClass = "card card-ok";
                                        $scope.paneles.equipo.iconClass = "fa fa-check";

                                        //DESACTIVAR PANELES
                                        $scope.paneles.datosGenerales.enabled = false;
                                        $scope.paneles.cotizacion.enabled = false;
                                        $scope.paneles.solicitudTransportista.enabled = false;
                                        $scope.paneles.equipo.enabled = false;

                                        //INHABILITAR CONTROLES
                                        //RESUMEN
                                        $scope.paneles.resumen.controles.transportista = true;
                                        $scope.paneles.resumen.controles.iniciarEnvio = false;
                                        $scope.paneles.resumen.controles.entregado = false;
                                        $scope.paneles.resumen.controles.recibido = false;
                                        $scope.paneles.resumen.controles.cancelar = false;
                                        $scope.paneles.resumen.controles.solicitarViaje = false;
                                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                                        //DATOS GENERALES
                                        $scope.paneles.datosGenerales.controles.cliente = false;
                                        $scope.paneles.datosGenerales.controles.fechaDeSalida = false;
                                        $scope.paneles.datosGenerales.controles.horaDeSalida = true;
                                        $scope.paneles.datosGenerales.controles.bodegaDeCarga = false;
                                        $scope.paneles.datosGenerales.controles.bodegaDeDescarga = false;
                                        $scope.paneles.datosGenerales.controles.tipoDeRemolque = false;
                                        $scope.paneles.datosGenerales.controles.carga = false;
                                        $scope.paneles.datosGenerales.controles.numeroDeEmbarque = true;
                                        $scope.paneles.datosGenerales.controles.destinatario = false;
                                        $scope.paneles.datosGenerales.controles.solicitarCotizacion = false;
                                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                                        //COTIZACION
                                        $scope.paneles.cotizacion.controles.precio = false;
                                        $scope.paneles.cotizacion.controles.guardar = false;
                                        //SOLICITUD DE TRANSPORTISTA
                                        $scope.paneles.solicitudTransportista.controles.autorizarTransportista = false;
                                        $scope.paneles.solicitudTransportista.controles.transportistas = false;
                                        //EQUIPO ASIGNADO
                                        $scope.paneles.equipo.controles.chofer = false;
                                        $scope.paneles.equipo.controles.tractor = false;
                                        $scope.paneles.equipo.controles.remolque = false;
                                        $scope.paneles.equipo.controles.asignarEquipo = false;

                                        //CARGA DE TRANSPORTISTA SELECCIONADO
                                        var refTransportistaSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
                                        refTransportistaSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.idTransportistaSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.transportistaSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE CHOFER SELECCIONADO
                                        var refChoferSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
                                        refChoferSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.choferSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.choferSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE TRACTOR SELECCIONADO
                                        var refTractorSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
                                        refTractorSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.tractorSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.tractorSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CARGA DE REMOLQUE SELECCIONADO
                                        var refRemolqueSeleccionado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
                                        refRemolqueSeleccionado.on("value", function (snapshot) {
                                            snapshot.forEach(function (childSnapshot) {
                                                $scope.remolqueSeleccionado = childSnapshot.key;
                                                $scope.firebaseFlete.remolqueSeleccionado = childSnapshot.val();
                                            });
                                        });
                                        //CAMBIAR ESTATUS DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = "Finalizado";
                                        break;
                                    default:
                                        break;
                                }
                            });
                            break;
                        default:
                            break;
                    }
                });
            });
        }
        else {
            refFlete = firebase.database().ref('fletesPorAsignar');
            firebaseFlete = $firebaseObject(refFlete);

            firebaseUsuario.$loaded().then(function () {
                switch (firebaseUsuario.tipoDeUsuario) {
                    case 'administrador':
                    case 'colaborador':
                        //ACTIVAR LOS PANELES
                        $scope.paneles.datosGenerales.cardClass = "card card-waiting";
                        $scope.paneles.datosGenerales.iconClass = "fa fa-clock-o";
                        $scope.paneles.datosGenerales.tabClass = "collapse in"
                        $scope.paneles.cotizacion.cardClass = "card";
                        $scope.paneles.cotizacion.iconClass = "";
                        $scope.paneles.solicitudTransportista.cardClass = "card";
                        $scope.paneles.solicitudTransportista.iconClass = "";
                        $scope.paneles.equipo.cardClass = "card";
                        $scope.paneles.equipo.iconClass = "";

                        //DESACTIVAR PANELES
                        $scope.paneles.resumen.controles.transportista = false;
                        $scope.paneles.resumen.controles.iniciarEnvio = false
                        $scope.paneles.resumen.controles.entregado = false;
                        $scope.paneles.resumen.controles.recibido = false;
                        $scope.paneles.resumen.controles.cancelar = false;
                        $scope.paneles.resumen.controles.solicitarViaje = false;
                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                        $scope.paneles.cotizacion.enabled = false;
                        $scope.paneles.solicitudTransportista.enabled = false;
                        $scope.paneles.equipo.enabled = false;
                        break;
                    case 'cliente':
                        $scope.paneles.datosGenerales.cardClass = "card card-waiting";
                        $scope.paneles.datosGenerales.iconClass = "fa fa-clock-o";
                        $scope.paneles.cotizacion.cardClass = "card";
                        $scope.paneles.cotizacion.iconClass = "";
                        $scope.paneles.solicitudTransportista.cardClass = "card";
                        $scope.paneles.solicitudTransportista.iconClass = "";
                        $scope.paneles.equipo.cardClass = "card";
                        $scope.paneles.equipo.iconClass = "";

                        //DESACTIVAR PANELES
                        $scope.paneles.resumen.controles.transportista = false;
                        $scope.paneles.resumen.controles.iniciarEnvio = false
                        $scope.paneles.resumen.controles.entregado = false;
                        $scope.paneles.resumen.controles.recibido = false;
                        $scope.paneles.resumen.controles.cancelar = false;
                        $scope.paneles.resumen.controles.solicitarViaje = false;
                        $scope.paneles.resumen.controles.cancelarSolicitud = false;
                        $scope.paneles.datosGenerales.controles.guardarCambios = false;
                        $scope.paneles.cotizacion.enabled = false;
                        $scope.paneles.solicitudTransportista.enabled = false;
                        $scope.paneles.equipo.enabled = false;

                        //APLICAR CONFIGURACIÓN A CONTROLES
                        var refCliente = firebase.database().ref().child('clientes').child(usuario.uid).child('cliente');
                        refCliente.once('value', function (snapshot) {
                            var Cliente = snapshot.val();
                            $scope.firebaseFlete.flete.cliente = Cliente.nombre;
                            $scope.paneles.datosGenerales.controles.cliente = false;
                            $scope.buscarBodegas();
                        });
                        break;
                    default:
                        break;
                }
            });
        }

        //ALERTA
        var alerta = function (mensaje, url) {
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#flete')))
                    .clickOutsideToClose(false)
                    .title('Registro correcto')
                    .htmlContent(mensaje)
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Aceptar')
            ).then(function () {
                removerListener();
                $location.path(url);
            });
        }

        //EVENTO PARA REMOVER EL EVENTO DE ESCUCHA
        var removerListener = function () {
            if ($routeParams.ID) {
                refFlete = firebase.database().ref('fletesPorAsignar').child($routeParams.ID);
                refFlete.off();
            }
        }

        //EVENTO PARA SOLICITAR COTIZACIÓN
        $scope.datosGeneralesSubmit = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            if ($scope.firebaseFlete.flete.estatus === 'Nuevo') {
                refFlete = firebase.database().ref('fletesPorAsignar');
                var refIdFlete = firebase.database().ref().child('fleteId');
                var firebaseFleteId = $firebaseObject(refIdFlete);
                firebaseFleteId.$loaded().then(function () {
                    $scope.firebaseFlete.flete.idFlete = firebaseFleteId.$value.toString();
                    var firebaseFlete = refFlete.push()

                    //BUSQUEDA DE CLIENTE Y BODEGAS
                    var nombreCliente = $scope.firebaseFlete.flete.cliente;
                    var refClientes = firebase.database().ref().child('clientes');
                    refClientes.once("value", function (snapshot) {
                        snapshot.forEach(function (childSnapshot) {
                            clientes = childSnapshot.val();
                            if (clientes.cliente.nombre === nombreCliente) {
                                var firebaseIdDelCliente = clientes.cliente.firebaseId;
                                var refBodega = firebase.database().ref().child('clientes').child(firebaseIdDelCliente)
                                refBodega.once('value', function (snapshot) {
                                    //BUSQUEDA DE BODEGAS DEL CLIENTE
                                    snapshot.forEach(function (childSnapshot) {
                                        if (childSnapshot.key === 'bodegas') {
                                            childSnapshot.forEach(function (nodoSnapshot) {
                                                var bodega = nodoSnapshot.val();
                                                if (bodega.estatus !== 'eliminado') {
                                                    if (bodega.nombreDeLaBodega === $scope.firebaseFlete.flete.bodegaDeCarga) {
                                                        $scope.firebaseFlete.bodegaDeCarga = bodega;
                                                    }
                                                    else if (bodega.nombreDeLaBodega === $scope.firebaseFlete.flete.bodegaDeDescarga) {
                                                        $scope.firebaseFlete.bodegaDeDescarga = bodega;
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    if ($scope.firebaseFlete.bodegaDeCarga !== undefined && $scope.firebaseFlete.bodegaDeDescarga !== undefined) {
                                        //INSERCIÓN DEL FLETE
                                        $scope.firebaseFlete.flete.estatus = 'fletePorCotizar';
                                        $scope.firebaseFlete.flete.fechaDeSalida = convertToUnixTime($scope.firebaseFlete.flete.fechaDeSalida);
                                        $scope.firebaseFlete.flete.fechaDeEdicion = unixTime();
                                        firebaseFlete.set($scope.firebaseFlete);
                                        $scope.firebaseFlete.flete.firebaseId = firebaseFlete.key;

                                        firebaseFlete.set($scope.firebaseFlete);
                                        firebaseFleteId.$value = parseInt(firebaseFleteId.$value) + 1;
                                        firebaseFleteId.$save();
                                        refFlete.off();
                                        alerta('<br/> <p>Flete registrado correctamente. </p> <p> A partir de ahora esperaremos la cotización del administrador.</p>', '/CHAOL/Fletes');
                                    }
                                    else {
                                        $mdDialog.show(
                                            $mdDialog.alert()
                                                .parent(angular.element(document.querySelector('#flete')))
                                                .clickOutsideToClose(false)
                                                .title('Oops! Algo salió mal')
                                                .htmlContent('<br/> <p>Al parecer existe un problema con las bodegas seleccionadas.</p><p> Por favor, verifique la información</p>')
                                                .ariaLabel('Alert Dialog Demo')
                                                .ok('Aceptar')
                                        );
                                    }
                                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                                });
                            }
                        });
                    });
                });
            }
            else {
                var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete');
                var firebaseFlete = $firebaseObject(refFlete);
                firebaseFlete.$loaded().then(function () {
                    $scope.firebaseFlete.flete.fechaDeEdicion = unixTime();
                    firebaseFlete.fechaDeEdicion = $scope.firebaseFlete.flete.fechaDeEdicion;
                    firebaseFlete.carga = $scope.firebaseFlete.flete.carga;
                    firebaseFlete.horaDeSalida = $scope.firebaseFlete.flete.horaDeSalida;
                    firebaseFlete.numeroDeEmbarque = $scope.firebaseFlete.flete.numeroDeEmbarque;
                    firebaseFlete.destinatario = $scope.firebaseFlete.flete.destinatario;
                    firebaseFlete.$save();
                    alerta('<br/> <p>Flete actualizado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
                    document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                });
            }
        }

        //EVENTO PARA COTIZAR EL FLETE
        $scope.cotizacionSubmit = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete');
            var firebaseFlete = $firebaseObject(refFlete);
            firebaseFlete.$loaded().then(function () {
                $scope.firebaseFlete.flete.fechaDeEdicion = unixTime();
                firebaseFlete.fechaDeEdicion = $scope.firebaseFlete.flete.fechaDeEdicion;
                firebaseFlete.precio = $scope.firebaseFlete.flete.precio;
                if ($scope.firebaseFlete.flete.estatus === 'Flete Por Cotizar') {
                    firebaseFlete.estatus = "esperandoPorTransportista";
                }
                firebaseFlete.$save();
                alerta('<br/> <p>Flete cotizado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            });
        };

        //EVENTO PARA CANCELAR
        $scope.cancelarFlete = function () {
            $mdDialog.show($mdDialog.confirm()
                .parent(angular.element(document.querySelector('#tractores')))
                .title('¿Cancelar flete?')
                .htmlContent('<br/> <p>¿Estás seguro que deseas cancelar este flete?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                .ariaLabel('Alert Dialog Demo')
                .ok('Sí, deseo cancelarlo')
                .cancel('No, prefiero continuar')
            ).then(function () {
                //CANCELACIÓN DEL FLETE
                var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete');
                var firebaseFlete = $firebaseObject(refFlete);
                firebaseFlete.$loaded().then(function () {
                    $scope.firebaseFlete.flete.fechaDeEdicion = unixTime();
                    firebaseFlete.fechaDeEdicion = $scope.firebaseFlete.flete.fechaDeEdicion;
                    firebaseFlete.estatus = "cancelado";
                    firebaseFlete.$save();
                    alerta('<br/> <p>Flete cancelado correctamente. </p> <p> Hemos cancelado correctamente el flete correspondiente.</p>', '/CHAOL/Fletes');
                });
            });
        }

        //EVENTO PARA SOLICITAR EL VIAJE PARA EL TRANSPORTISTA
        $scope.solicitarViaje = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId);
            var firebaseFlete = $firebaseObject(refFlete);
            var refTransportistasInteresados = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados').child(usuario.uid);
            firebaseFlete.$loaded().then(function () {
                $scope.firebaseFlete.flete.fechaDeEdicion = unixTime();
                firebaseFlete.flete.fechaDeEdicion = $scope.firebaseFlete.flete.fechaDeEdicion;
                firebaseFlete.flete.estatus = "transportistaPorConfirmar";
                var refTransportista = firebase.database().ref().child('transportistas').child(usuario.uid).child('transportista');
                var firebaseTransportista = $firebaseObject(refTransportista);
                firebaseTransportista.$loaded().then(function () {
                    firebaseFlete.$save();
                    refFlete.child('transportistasInteresados').child(usuario.uid).child('firebaseId').set(usuario.uid);
                    refFlete.child('transportistasInteresados').child(usuario.uid).child('nombre').set(firebaseTransportista.nombre);
                });
                alerta('<br/> <p>Flete cotizado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            });
        }

        //EVENTO PARA CANCELAR LA SOLICITUD
        $scope.cancelarSolicitud = function () {
            $mdDialog.show($mdDialog.confirm()
                .parent(angular.element(document.querySelector('#tractores')))
                .title('¿Cancelar soliitud?')
                .htmlContent('<br/> <p>¿Estás seguro que deseas cancelar esta solicitud?</p> <p>Recuerda que esta acción no puede deshacerse.</p>')
                .ariaLabel('Alert Dialog Demo')
                .ok('Sí, deseo cancelarlo')
                .cancel('No, prefiero continuar')
            ).then(function () {
                document.getElementById('div_progress').className = 'col-lg-12 div-progress';
                var refTransportista = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados').child(usuario.uid);
                refTransportista.remove();
                //CARGA DE TRANSPORTISTAS INTERESADOS
                var refTransportistas = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados');

                //CAMBIO DE ESTATUS EN EL FLETE
                refTransportistas.once("value", function (snapshot) {
                    var x = 0;
                    snapshot.forEach(function (childSnapshot) {
                        transportistas = childSnapshot.val();
                        x++;
                    });
                    var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete').child('estatus');
                    if (x === 0) {
                        refFlete.set('esperandoPorTransportista');
                    }
                    else {
                        refFlete.set('transportistaPorConfirmar');

                    }
                });
                alerta('<br/> <p>Solicitud cancelada correctamente. </p> <p> Hemos cancelado correctamente la solicitud de servico para este flete.</p>', '/CHAOL/Fletes');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            });
        }

        //EVENTO PARA SELECCIONAR UN TRANSPORTISTA INTERESADO
        $scope.autorizarTransportista = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refTransportista = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistaSeleccionado');
            refTransportista.remove();
            var refTransportistaInteresado = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('transportistasInteresados').child($scope.idTransportistaSeleccionado);
            var firebaseTransportista = $firebaseObject(refTransportistaInteresado);
            firebaseTransportista.$loaded().then(function () {
                refTransportista.child($scope.idTransportistaSeleccionado).child('firebaseId').set($scope.idTransportistaSeleccionado);
                refTransportista.child($scope.idTransportistaSeleccionado).child('nombre').set(firebaseTransportista.nombre);
                var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete');
                refFlete.child('estatus').set('unidadesPorAsignar');
                refFlete.child('fechaDeAsignacionDeTransportista').set(unixTime());
                alerta('<br/> <p>Transportista seleccionado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
            });
        }

        //EVENTO PARA ASIGNAR EQUIPO
        $scope.asignarEquipo = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refChofer = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('choferSeleccionado');
            refChofer.remove();
            refChofer.child($scope.firebaseFlete.choferSeleccionado.firebaseId).set($scope.firebaseFlete.choferSeleccionado);

            var refTractor = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('tractorSeleccionado');
            refTractor.remove();
            refTractor.child($scope.firebaseFlete.tractorSeleccionado.firebaseId).set($scope.firebaseFlete.tractorSeleccionado);

            var refRemolque = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('remolqueSeleccionado');
            refRemolque.remove();
            refRemolque.child($scope.firebaseFlete.remolqueSeleccionado.firebaseId).set($scope.firebaseFlete.remolqueSeleccionado);

            var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete').child('estatus');
            refFlete.set('envioPorIniciar');
            alerta('<br/> <p>Equipo asignado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        }

        //EVENTO PARA ASIGNAR EQUIPO
        $scope.iniciarEnvio = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refChoferTransportista = firebase.database().ref().child('transportistas').child($scope.firebaseFlete.transportistaSeleccionado.firebaseId).child('choferes').child($scope.firebaseFlete.choferSeleccionado.firebaseId);
            var firebaseChoferTransportista = $firebaseObject(refChoferTransportista);
            firebaseChoferTransportista.$loaded().then(function () {
                var refChofer = firebase.database().ref().child('choferes').child($scope.firebaseFlete.choferSeleccionado.firebaseId);
                var firebaseChofer = $firebaseObject(refChofer);
                firebaseChofer.$loaded().then(function () {
                    var refTractor = firebase.database().ref().child('transportistas').child($scope.firebaseFlete.transportistaSeleccionado.firebaseId).child('tractores').child($scope.firebaseFlete.tractorSeleccionado.firebaseId);
                    var firebaseTractor = $firebaseObject(refTractor);
                    firebaseTractor.$loaded().then(function () {
                        var refRemolque = firebase.database().ref().child('transportistas').child($scope.firebaseFlete.transportistaSeleccionado.firebaseId).child('remolques').child($scope.firebaseFlete.remolqueSeleccionado.firebaseId);
                        var firebaseRemolque = $firebaseObject(refRemolque);
                        firebaseRemolque.$loaded().then(function () {
                            if (firebaseChoferTransportista.estatus !== 'asignado' && firebaseChofer.estatus !== 'asignado' && firebaseTractor.estatus !== 'asignado' && firebaseRemolque.estatus !== 'asignado') {
                                var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete').child('estatus');
                                refFlete.set('enProgreso');

                                firebaseChoferTransportista.estatus = 'asignado';
                                firebaseChoferTransportista.$save();
                                firebaseChofer.estatus = 'asignado';
                                firebaseChofer.$save();
                                firebaseTractor.estatus = 'asignado';
                                firebaseTractor.$save();
                                firebaseRemolque.estatus = 'asignado';
                                firebaseRemolque.$save();

                                alerta('<br/> <p>Envío inciado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
                                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            }
                            else {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .parent(angular.element(document.querySelector('#registro')))
                                        .clickOutsideToClose(false)
                                        .title('Oops! Algo ha ocurrido')
                                        .htmlContent('<br/> <p>Al parecer no es posible utilizar a este chofer, tractor y/o remolque en este momento. Por favor, verifica que no se encuentren inactivos o en proceso de entrega de otro flete. Si lo consideras necesario, selecciona otro chofer, remolque o tractor para este envío.</p> <br/>')
                                        .ariaLabel('Alert Dialog Demo')
                                        .ok('Aceptar')
                                );
                                document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                            }
                        });
                    });
                });
            })
        }

        //EVENTO PARA ASIGNAR EQUIPO
        $scope.entregarFlete = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refChoferTransportista = firebase.database().ref().child('transportistas').child($scope.firebaseFlete.transportistaSeleccionado.firebaseId).child('choferes').child($scope.firebaseFlete.choferSeleccionado.firebaseId);
            var firebaseChoferTransportista = $firebaseObject(refChoferTransportista);
            firebaseChoferTransportista.$loaded().then(function () {
                var refChofer = firebase.database().ref().child('choferes').child($scope.firebaseFlete.choferSeleccionado.firebaseId);
                var firebaseChofer = $firebaseObject(refChofer);
                firebaseChofer.$loaded().then(function () {
                    var refTractor = firebase.database().ref().child('transportistas').child($scope.firebaseFlete.transportistaSeleccionado.firebaseId).child('tractores').child($scope.firebaseFlete.tractorSeleccionado.firebaseId);
                    var firebaseTractor = $firebaseObject(refTractor);
                    firebaseTractor.$loaded().then(function () {
                        var refRemolque = firebase.database().ref().child('transportistas').child($scope.firebaseFlete.transportistaSeleccionado.firebaseId).child('remolques').child($scope.firebaseFlete.remolqueSeleccionado.firebaseId);
                        var firebaseRemolque = $firebaseObject(refRemolque);
                        firebaseRemolque.$loaded().then(function () {
                            var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete').child('estatus');
                            refFlete.set('entregado');

                            firebaseChoferTransportista.estatus = 'libre';
                            firebaseChoferTransportista.$save();
                            firebaseChofer.estatus = 'libre';
                            firebaseChofer.$save();
                            firebaseTractor.estatus = 'libre';
                            firebaseTractor.$save();
                            firebaseRemolque.estatus = 'libre';
                            firebaseRemolque.$save();

                            alerta('<br/> <p>Envío entregado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
                            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
                        });
                    });
                });
            });
        }

        //EVENTO PARA ASIGNAR EQUIPO
        $scope.recibirFlete = function () {
            document.getElementById('div_progress').className = 'col-lg-12 div-progress';
            var refFlete = firebase.database().ref().child('fletesPorAsignar').child($scope.firebaseFlete.flete.firebaseId).child('flete').child('estatus');
            refFlete.set('finalizado');
            alerta('<br/> <p>Envío finalizado correctamente. </p> <p> Hemos actualizado correctamente la información que ingresaste.</p>', '/CHAOL/Fletes');
            document.getElementById('div_progress').className = 'col-lg-12 div-progress hidden';
        }
    });
})();