package com.indev.chaol.utils;

import android.support.v4.app.Fragment;

import com.indev.chaol.R;
import com.indev.chaol.fragments.ListadoChoferesFragment;
import com.indev.chaol.fragments.ListadoClientesFragment;
import com.indev.chaol.fragments.ListadoRemolquesFragment;
import com.indev.chaol.fragments.ListadoTractoresFragment;
import com.indev.chaol.fragments.ListadoTransportistasFragment;
import com.indev.chaol.fragments.MainPanelFragment;
import com.indev.chaol.fragments.MainPerfilesFragment;
import com.indev.chaol.fragments.RegistroClientesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saurett on 24/02/2017.
 */

public final class Constants {

    /**Key Extraparam**/
    public static String KEY_MAIN_DECODE = "key_main_decode";

    /**Acciones generales**/
    public static final int ACCION_SIN_DEFINIR = 0;
    public static final int ACCION_REGISTRAR = 1;
    public static final int ACCION_EDITAR = 2;
    public static final int ACCION_BUSCAR = 3;

    /**Identificadores para buscar**/
    public static final int WS_KEY_BUSCAR_CLIENTES = 1;
    public static final int WS_KEY_ELIMINAR_CLIENTES = 10;
    public static final int WS_KEY_BUSCAR_TRANSPORTISTAS = 2;
    public static final int WS_KEY_ELIMINAR_TRANSPORTISTAS = 20;
    public static final int WS_KEY_BUSCAR_CHOFERES = 3;
    public static final int WS_KEY_ELIMINAR_CHOFERES = 30;
    public static final int WS_KEY_BUSCAR_TRACTORES = 4;
    public static final int WS_KEY_ELIMINAR_TRACTORES = 40;
    public static final int WS_KEY_BUSCAR_REMOLQUES = 5;
    public static final int WS_KEY_ELIMINAR_REMOLQUES = 50;

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

    /**
     * Contenido del FRAGMENT_MAIN_PANEL
     **/
    public static final String PANEL_FLETES_CONTAINER = "panel_fletes_container";
    public static final String PANEL_CLIENTES_CONTAINER = "panel_clientes_container";
    public static final String PANEL_TRANSPORTISTAS_CONTAINER = "panel_transportistas_container";
    public static final String PANEL_CHOFERES_CONTAINER = "panel_choferes_container";
    public static final String PANEL_TRACTORES_CONTAINER = "panel_tractores_container";
    public static final String PANEL_REMOLQUES_CONTAINER = "panel_remolques_container";

    /**
     * Fragmento principal para perfiles
     **/
    public static final String FRAGMENT_MAIN_PERFILES = "fragment_main_perfiles";

    /**Fragmento secundario de formularios**/
    public static final String FRAGMENT_MAIN_REGISTER = "fragment_main_register";

    /**
     * Fragmentos secundario para listados
     **/
    public static final String FRAGMENT_CLIENTES = "fragment_clientes";
    public static final String FRAGMENT_TRANSPORTISTAS = "fragment_transportistas";
    public static final String FRAGMENT_CHOFERES = "fragment_choferes";
    public static final String FRAGMENT_TRACTORES = "fragment_tractores";
    public static final String FRAGMENT_REMOLQUES = "fragment_remolques";

    /**Colección de fragmentos secundarios**/
    public static final List<String>  SECONDARY_TAG_FRAGMENTS;
    static {
        SECONDARY_TAG_FRAGMENTS = new ArrayList<>();
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_CLIENTES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_TRANSPORTISTAS);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_CHOFERES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_TRACTORES);
        SECONDARY_TAG_FRAGMENTS.add(FRAGMENT_REMOLQUES);
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
        ITEM_FRAGMENT.put(R.id.menu_item_agenda, FRAGMENT_LISTADO_CLIENTES);
        ITEM_FRAGMENT.put(R.id.menu_item_perfil, FRAGMENT_MAIN_PERFILES);
        ITEM_FRAGMENT.put(R.id.item_btn_editar_cliente, FRAGMENT_MAIN_REGISTER);
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
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_REMOLQUES, new ListadoRemolquesFragment());
        TAG_FRAGMENT.put(FRAGMENT_MAIN_REGISTER, new RegistroClientesFragment());
    }

    /**Contiene el titulo correspondiente a la acción origen de ciertos botones**/
    public static final HashMap<Integer, Integer> TITLE_ACTIVITY;
    static {
        TITLE_ACTIVITY = new HashMap<>();
        TITLE_ACTIVITY.put(R.id.item_btn_editar_cliente,R.string.default_item_menu_title_clientes);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_transportista,R.string.default_item_menu_title_transportistas);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_chofer,R.string.default_item_menu_title_choferes);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_tractor,R.string.default_item_menu_title_tractores);
        TITLE_ACTIVITY.put(R.id.item_btn_editar_remolque,R.string.default_item_menu_title_remolques);
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
