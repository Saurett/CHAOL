function click_menu(elemento) {
    $('#nav_menu').toggleClass('abierto');
    $('#nav_menu').toggleClass('cerrado');
    $('#div_menu_opciones').toggleClass('opacity-1');
    $('#div_menu_opciones').toggleClass('opacity-0');
    if (elemento !== undefined) {
        $('#ul_menu li').removeClass('activo');
        $(elemento.parentElement).addClass('activo');
    }
}