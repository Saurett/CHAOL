function iniciar_calendario(div) {
    var currentYear = new Date().getFullYear();

    $(div).calendar({
        enableContextMenu: true,
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
        dataSource: [
            {
                id: 0,
                cliente: 'Fred Gómez Leyva',
                transportista: 'Alberto Pérez Martínez',
                name: 'Fred Gómez Leyva',
                startDate: new Date(currentYear, 0, 28),
                endDate: new Date(currentYear, 0, 28),
                idEstatus: 1
            },
            {
                id: 1,
                cliente: 'Francisco Javier Díaz Saurett',
                transportista: 'Camila Rodríguez Reyes',
                name: 'Francisco Javier Díaz Saurett',
                startDate: new Date(currentYear, 2, 16),
                endDate: new Date(currentYear, 2, 16),
                idEstatus: 1
            },
            {
                id: 2,
                cliente: 'Tadeo Bernat Rodríguez',
                transportista: 'Carlos Alberto Florez Cazarín',
                name: 'Tadeo Bernat Rodríguez',
                startDate: new Date(currentYear, 3, 29),
                endDate: new Date(currentYear, 3, 29),
                idEstatus: 1
            },
            {
                id: 3,
                cliente: 'Víctor Hugo Gómez Martínez',
                transportista: 'José Luis Torres Salazar',
                name: 'Víctor Hugo Gómez Martínez',
                startDate: new Date(currentYear, 8, 1),
                endDate: new Date(currentYear, 8, 1),
                idEstatus: 1
            },
            {
                id: 4,
                cliente: 'Francisco Javier Díaz Saurett',
                transportista: 'Carlos Alberto Florez Cazarín',
                name: 'Francisco Javier Díaz Saurett',
                startDate: new Date(currentYear, 2, 16),
                endDate: new Date(currentYear, 2, 16),
                idEstatus: 1
            },
            {
                id: 5,
                cliente: 'Tadeo Bernat Rodríguez',
                transportista: 'José Luis Torres Salazar',
                name: 'Tadeo Bernat Rodríguez',
                startDate: new Date(currentYear, 3, 29),
                endDate: new Date(currentYear, 3, 29),
                idEstatus: 1
            },
            {
                id: 6,
                cliente: 'Víctor Hugo Gómez Martínez',
                transportista: 'Camila Rodríguez Reyes',
                name: 'Víctor Hugo Gómez Martínez',
                startDate: new Date(currentYear, 3, 29),
                endDate: new Date(currentYear, 3, 29),
                idEstatus: 1
            },
            {
                id: 7,
                cliente: 'Víctor Hugo Gómez Martínez',
                transportista: 'José Luis Torres Salazar',
                name: 'Víctor Hugo Gómez Martínez',
                startDate: new Date(currentYear, 8, 1),
                endDate: new Date(currentYear, 8, 1),
                idEstatus: 1
            },
            {
                id: 8,
                cliente: 'Fred Gómez Leyva',
                transportista: 'Camila Rodríguez Reyes',
                name: 'Fred Gómez Leyva',
                startDate: new Date(currentYear, 8, 1),
                endDate: new Date(currentYear, 8, 1),
                idEstatus: 1
            },
            {
                id: 9,
                cliente: 'Tadeo Bernat Rodríguez',
                transportista: 'Carlos Alberto Florez Cazarín',
                name: 'Tadeo Bernat Rodríguez',
                startDate: new Date(currentYear, 8, 1),
                endDate: new Date(currentYear, 8, 1),
                idEstatus: 1
            }
        ]
    });
}