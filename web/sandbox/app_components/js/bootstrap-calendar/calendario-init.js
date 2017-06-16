//function iniciar_calendario(div, fletes) {
//    var currentYear = new Date().getFullYear();

//    //DETERMINAR FECHA DE HOY
//    var hoy = new Date(currentYear, new Date().getMonth(), new Date().getDate()).getTime();

//    $(div).calendar({
//        enableContextMenu: true,

//        customDayRenderer: function (element, date) {
//            if (date.getTime() == hoy) {
//                $(element).css('background-color', '#F34235');
//                $(element).css('font-weight', 'bold');
//                $(element).css('color', 'white');
//                $(element).css('padding', '0px');
//            }
//        },

//        enableRangeSelection: true,
//        language: 'es',
//        contextMenuItems: [
//            {
//                text: 'Detalle',
//                click: detalle
//            }
//        ],
//        selectRange: function (e) {
//            editEvent({ startDate: e.startDate, endDate: e.endDate });
//        },
//        mouseOnDay: function (e) {
//            if (e.events.length > 0) {
//                var content = '';

//                for (var i in e.events) {
//                    if (i < 3) {
//                        content += '<div class="event-tooltip-content">'
//                            + '<div class="event-name"><span>Estatus: </span><span style="color:' + e.events[i].color + '"><b>' + e.events[i].estatus + '</b></span></div>'
//                            + '<div class="event-name">Cliente: <b>' + e.events[i].cliente + '</b></div>'
//                            + '<div class="event-location">Transportista: <b>' + e.events[i].transportista + '</b></div>'
//                            + '</div>'
//                            + '<hr/>';
//                    }
//                    else {
//                        content += '<div class="event-tooltip-content">'
//                            + '<div class="event-name text-center">' + (e.events.length - 3).toString() + ' fletes más</div>';
//                        break;
//                    }
//                }

//                $(e.element).popover({
//                    trigger: 'manual',
//                    container: 'body',
//                    html: true,
//                    content: content
//                });

//                $(e.element).popover('show');
//            }
//        },
//        mouseOutDay: function (e) {
//            if (e.events.length > 0) {
//                $(e.element).popover('hide');
//            }
//        },
//        dayContextMenu: function (e) {
//            $(e.element).popover('hide');
//        },
//        dataSource: fletes
//    });

//    function detalle(event) {
//        var id = event.firebaseId;
//        console.log(id)
//    }
//}