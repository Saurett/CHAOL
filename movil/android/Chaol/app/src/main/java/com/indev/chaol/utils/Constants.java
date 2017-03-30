package com.indev.chaol.utils;

import android.support.v4.app.Fragment;

import com.indev.chaol.R;
import com.indev.chaol.fragments.ListadoAgendaFragment;
import com.indev.chaol.fragments.ListadoChoferesFragment;
import com.indev.chaol.fragments.ListadoClientesFragment;
import com.indev.chaol.fragments.ListadoRemolquesFragment;
import com.indev.chaol.fragments.ListadoTractoresFragment;
import com.indev.chaol.fragments.ListadoTransportistasFragment;
import com.indev.chaol.fragments.MainPanelFragment;
import com.indev.chaol.fragments.MainPerfilesFragment;
import com.indev.chaol.fragments.RegistroChoferesFragment;
import com.indev.chaol.fragments.RegistroClientesFragment;
import com.indev.chaol.fragments.RegistroFletesFragment;
import com.indev.chaol.fragments.RegistroRemolquesFragment;
import com.indev.chaol.fragments.RegistroTractoresFragment;
import com.indev.chaol.fragments.RegistroTransportistasFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saurett on 24/02/2017.
 */

public final class Constants {

    /**Key Extraparam**/
    public static String KEY_MAIN_DECODE = "key_main_decode";

    /**Dias de la semana**/
    public static String LUNES = "Lun.";
    public static String MARTES = "Mar.";
    public static String MIERCOLES = "Mié.";
    public static String JUEVES = "Jue.";
    public static String VIERNES = "Vié.";
    public static String SABADO = "Sab.";
    public static String DOMINGO = "Dom.";

    /**Acciones generales**/
    public static final int ACCION_SIN_DEFINIR = 0;
    public static final int ACCION_REGISTRAR = 1;
    public static final int ACCION_EDITAR = 2;
    public static final int ACCION_BUSCAR = 3;

    /**Identificadores para buscar**/
    public static final int WS_KEY_PRE_RENDER = 0;
    public static final int WS_KEY_BUSCAR_CLIENTES = 1;
    public static final int WS_KEY_ELIMINAR_CLIENTES = 10;
    public static final int WS_KEY_EDITAR_CLIENTES = 11;
    public static final int WS_KEY_AGREGAR_CLIENTES = 12;
    public static final int WS_KEY_BUSCAR_TRANSPORTISTAS = 2;
    public static final int WS_KEY_ELIMINAR_TRANSPORTISTAS = 20;
    public static final int WS_KEY_EDITAR_TRANSPORTISTAS = 21;
    public static final int WS_KEY_AGREGAR_TRANSPORTISTAS = 22;
    public static final int WS_KEY_BUSCAR_CHOFERES = 3;
    public static final int WS_KEY_ELIMINAR_CHOFERES = 30;
    public static final int WS_KEY_EDITAR_CHOFERES = 31;
    public static final int WS_KEY_AGREGAR_CHOFERES = 32;
    public static final int WS_KEY_BUSCAR_TRACTORES = 4;
    public static final int WS_KEY_ELIMINAR_TRACTORES = 40;
    public static final int WS_KEY_EDITAR_TRACTORES = 41;
    public static final int WS_KEY_AGREGAR_TRACTORES = 42;
    public static final int WS_KEY_BUSCAR_REMOLQUES = 5;
    public static final int WS_KEY_ELIMINAR_REMOLQUES = 50;
    public static final int WS_KEY_EDITAR_REMOLQUES = 51;
    public static final int WS_KEY_AGREGAR_REMOLQUES = 52;
    public static final int WS_KEY_BUSCAR_FLETES = 6;
    public static final int WS_KEY_ELIMINAR_FLETES = 60;
    public static final int WS_KEY_EDITAR_FLETES = 61;
    public static final int WS_KEY_AGREGAR_FLETES = 62;
    public static final int WS_KEY_REGISTER_ACTIVITY = 100;
    public static final int WS_KEY_NAVIGATION_ACTIVITY = 101;

    //region FRAGMENT TAGS
    /**Fragmentos principales de panel**/
    public static final String FRAGMENT_MAIN_PANEL = "fragment_main_panel";

    /**
     * Fragmentos principales de lista
     **/
    public static final String FRAGMENT_LISTADO_CLIENTES = "fragment_listado_clientes";
    public static final String FRAGMENT_LISTADO_TRANSPORTISTAS = "fragment_listado_transportiistas";
    public static final String FRAGMENT_LISTADO_CHOFERES = "fragment_listado_choferes";
    public static final String FRAGMENT_LISTADO_TRACTORES = "fragment_listado_tractores";
    public static final String FRAGMENT_LISTADO_REMOLQUES = "fragment_listado_remolques";
    public static final String FRAGMENT_LISTADO_AGENDA = "fragment_listado_agenda";

    /**
     * Contenido del FRAGMENT_MAIN_PANEL
     **/
    public static final String PANEL_FLETES_CONTAINER = "panel_fletes_container";
    public static final String PANEL_CLIENTES_CONTAINER = "panel_clientes_container";
    public static final String PANEL_TRANSPORTISTAS_CONTAINER = "panel_transportistas_container";
    public static final String PANEL_CHOFERES_CONTAINER = "panel_choferes_container";
    public static final String PANEL_TRACTORES_CONTAINER = "panel_tractores_container";
    public static final String PANEL_REMOLQUES_CONTAINER = "panel_remolques_container";

    /**Fragmentos para registro de fletes**/
    public static final String DATOS_GENERALES_FLETES_CONTAINER = "datos_generales_fletes_container";
    public static final String COTIZACION_FLETES_CONTAINER = "cotizacion_fletes_container";
    public static final String ASIGNACION_FLETES_CONTAINER = "asignacion_fletes_container";
    public static final String EQUIPO_FLETES_CONTAINER = "equipo_fletes_container";
    public static final String PROCESO_FLETES_CONTAINER = "proceso_fletes_container";
    public static final String ASIGNACION_TRANSPORTISTAS_FLETES_CONTAINER = "asignacion_transportostas_fletes_container";

    /**Colección de fragmentos fletes**/
    public static final List<String>  FLETES_TAG_FRAGMENTS;
    static {
        FLETES_TAG_FRAGMENTS = new ArrayList<>();
        FLETES_TAG_FRAGMENTS.add(DATOS_GENERALES_FLETES_CONTAINER);
        FLETES_TAG_FRAGMENTS.add(COTIZACION_FLETES_CONTAINER);
        FLETES_TAG_FRAGMENTS.add(ASIGNACION_FLETES_CONTAINER);
        FLETES_TAG_FRAGMENTS.add(EQUIPO_FLETES_CONTAINER);
        FLETES_TAG_FRAGMENTS.add(PROCESO_FLETES_CONTAINER);
        FLETES_TAG_FRAGMENTS.add(ASIGNACION_TRANSPORTISTAS_FLETES_CONTAINER);
    }

    /**
     * Fragmento principal para perfiles
     **/
    public static final String FRAGMENT_MAIN_PERFILES = "fragment_main_perfiles";

    /**Fragmento secundario de formularios**/
    public static final String FRAGMENT_MAIN_REGISTER = "fragment_main_register";
    public static final String FRAGMENT_TRANSPORTISTAS_REGISTER = "fragment_transportistas_register";
    public static final String FRAGMENT_CHOFERES_REGISTER = "fragment_choferes_register";
    public static final String FRAGMENT_TRACTORES_REGISTER = "fragment_tractores_register";
    public static final String FRAGMENT_REMOLQUES_REGISTER = "fragment_remolques_register";
    public static final String FRAGMENT_FLETES_REGISTER = "fragment_fletes_register";

    /**
     * Fragmentos secundario para listados
     **/
    public static final String FRAGMENT_CLIENTES = "fragment_clientes";
    public static final String FRAGMENT_TRANSPORTISTAS = "fragment_transportistas";
    public static final String FRAGMENT_CHOFERES = "fragment_choferes";
    public static final String FRAGMENT_TRACTORES = "fragment_tractores";
    public static final String FRAGMENT_REMOLQUES = "fragment_remolques";
    public static final String FRAGMENT_AGENDA = "fragment_agenda";

    /**Colección de fragmentos secundarios**/
    public static final List<String>  SECONDARY_TAG_FRAGMENTS;
    static {
        SECONDARY_TAG_FRAGMENTS = new ArrayList<>();
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_CLIENTES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_TRANSPORTISTAS);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_CHOFERES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_TRACTORES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_REMOLQUES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_AGENDA);
    }

    /**Contiene el TAG que le corresponde a cada ITEM que abra fragmentos**/
    public static final HashMap<Integer, String> ITEM_FRAGMENT;
    static {
        ITEM_FRAGMENT = new HashMap<>();
        ITEM_FRAGMENT.put(R.id.menu_item_inicio, FRAGMENT_MAIN_PANEL);
        ITEM_FRAGMENT.put(R.id.menu_item_clientes, FRAGMENT_LISTADO_CLIENTES);
        ITEM_FRAGMENT.put(R.id.menu_item_transportistas, FRAGMENT_LISTADO_TRANSPORTISTAS);
        ITEM_FRAGMENT.put(R.id.menu_item_choferes, FRAGMENT_LISTADO_CHOFERES);
        ITEM_FRAGMENT.put(R.id.menu_item_tractores, FRAGMENT_LISTADO_TRACTORES);
        ITEM_FRAGMENT.put(R.id.menu_item_remolques, FRAGMENT_LISTADO_REMOLQUES);
        ITEM_FRAGMENT.put(R.id.menu_item_agenda, FRAGMENT_LISTADO_AGENDA);
        ITEM_FRAGMENT.put(R.id.menu_item_perfil, FRAGMENT_MAIN_PERFILES);
        ITEM_FRAGMENT.put(R.id.item_btn_editar_cliente, FRAGMENT_MAIN_REGISTER);
        ITEM_FRAGMENT.put(R.id.item_btn_editar_transportista, FRAGMENT_TRANSPORTISTAS_REGISTER);
        ITEM_FRAGMENT.put(R.id.item_btn_editar_chofer, FRAGMENT_CHOFERES_REGISTER);
        ITEM_FRAGMENT.put(R.id.item_btn_editar_tractor, FRAGMENT_TRACTORES_REGISTER);
        ITEM_FRAGMENT.put(R.id.item_btn_editar_remolque, FRAGMENT_REMOLQUES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_listado_clientes, FRAGMENT_MAIN_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_listado_transportistas, FRAGMENT_TRANSPORTISTAS_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_listado_choferes, FRAGMENT_CHOFERES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_listado_tractores, FRAGMENT_TRACTORES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_listado_remolques, FRAGMENT_REMOLQUES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_panel_clientes, FRAGMENT_MAIN_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_panel_transportistas, FRAGMENT_TRANSPORTISTAS_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_panel_choferes, FRAGMENT_CHOFERES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_panel_tractores, FRAGMENT_TRACTORES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_panel_remolques, FRAGMENT_REMOLQUES_REGISTER);
        ITEM_FRAGMENT.put(R.id.fab_panel_fletes, FRAGMENT_FLETES_REGISTER);
    }

    /**
     * Contiene el fragmento que le corresponde a cada TAG de Fragment
     **/
    public static final HashMap<String, Fragment> TAG_FRAGMENT;
    static {
        TAG_FRAGMENT = new HashMap<>();
        TAG_FRAGMENT.put(FRAGMENT_MAIN_PANEL, new MainPanelFragment());
        TAG_FRAGMENT.put(FRAGMENT_MAIN_PERFILES, new MainPerfilesFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_CLIENTES, new ListadoClientesFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_TRANSPORTISTAS, new ListadoTransportistasFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_CHOFERES, new ListadoChoferesFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_TRACTORES, new ListadoTractoresFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_AGENDA, new ListadoAgendaFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_REMOLQUES, new ListadoRemolquesFragment());
        TAG_FRAGMENT.put(FRAGMENT_MAIN_REGISTER, new RegistroClientesFragment());
        TAG_FRAGMENT.put(FRAGMENT_TRANSPORTISTAS_REGISTER, new RegistroTransportistasFragment());
        TAG_FRAGMENT.put(FRAGMENT_CHOFERES_REGISTER, new RegistroChoferesFragment());
        TAG_FRAGMENT.put(FRAGMENT_TRACTORES_REGISTER, new RegistroTractoresFragment());
        TAG_FRAGMENT.put(FRAGMENT_REMOLQUES_REGISTER, new RegistroRemolquesFragment());
        TAG_FRAGMENT.put(FRAGMENT_FLETES_REGISTER, new RegistroFletesFragment());
    }

    /**Contiene el titulo correspondiente a la acción origen de ciertos botones**/
    public static final HashMap<Integer, Integer> TITLE_ACTIVITY;
    static {
        TITLE_ACTIVITY = new HashMap<>();
        TITLE_ACTIVITY.put(R.id.item_btn_editar_cliente, R.string.default_item_menu_title_clientes);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_transportista, R.string.default_item_menu_title_transportistas);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_chofer, R.string.default_item_menu_title_choferes);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_tractor, R.string.default_item_menu_title_tractores);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_remolque, R.string.default_item_menu_title_remolques);
        TITLE_ACTIVITY.put(R.id.fab_listado_clientes, R.string.default_item_menu_title_clientes);
        TITLE_ACTIVITY.put(R.id.fab_listado_transportistas, R.string.default_item_menu_title_transportistas);
        TITLE_ACTIVITY.put(R.id.fab_listado_choferes, R.string.default_item_menu_title_choferes);
        TITLE_ACTIVITY.put(R.id.fab_listado_tractores, R.string.default_item_menu_title_tractores);
        TITLE_ACTIVITY.put(R.id.fab_listado_remolques, R.string.default_item_menu_title_remolques);
        TITLE_ACTIVITY.put(R.id.fab_panel_clientes, R.string.default_item_menu_title_clientes);
        TITLE_ACTIVITY.put(R.id.fab_panel_transportistas, R.string.default_item_menu_title_transportistas);
        TITLE_ACTIVITY.put(R.id.fab_panel_choferes, R.string.default_item_menu_title_choferes);
        TITLE_ACTIVITY.put(R.id.fab_panel_tractores, R.string.default_item_menu_title_tractores);
        TITLE_ACTIVITY.put(R.id.fab_panel_remolques, R.string.default_item_menu_title_remolques);
        TITLE_ACTIVITY.put(R.id.fab_panel_fletes, R.string.default_item_menu_title_fletes);
    }

    /**Contiene el titulo correspondiente a la acción del formulario**/
    public static final HashMap<Integer,Integer> TITLE_FORM_ACTION;
    static {
        TITLE_FORM_ACTION = new HashMap<>();
        TITLE_FORM_ACTION.put(Constants.ACCION_REGISTRAR,R.string.default_form_title_new);
        TITLE_FORM_ACTION.put(Constants.ACCION_EDITAR,R.string.default_form_title_edit);
    }
    //endregion
}
