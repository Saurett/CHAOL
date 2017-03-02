package com.indev.chaol.utils;

import android.support.v4.app.Fragment;

import com.indev.chaol.R;
import com.indev.chaol.fragments.ListadoChoferesFragment;
import com.indev.chaol.fragments.ListadoClientesFragment;
import com.indev.chaol.fragments.ListadoRemolquesFragment;
import com.indev.chaol.fragments.ListadoTractoresFragment;
import com.indev.chaol.fragments.ListadoTransportistasFragment;
import com.indev.chaol.fragments.MainPanelFragment;
import com.indev.chaol.fragments.RegistroClientesFragment;

import java.util.HashMap;

/**
 * Created by saurett on 24/02/2017.
 */

public final class Constants {

    /**
     * Identificadores para buscar
     **/
    public static final int WS_KEY_BUSCAR_CLIENTES = 1;
    public static final int WS_KEY_BUSCAR_TRANSPORTISTAS = 2;
    public static final int WS_KEY_BUSCAR_CHOFERES = 3;
    public static final int WS_KEY_BUSCAR_TRACTORES = 4;
    public static final int WS_KEY_BUSCAR_REMOLQUES = 5;

    //region FRAGMENT TAGS
    /**
     * Fragmentos principales de panel
     **/
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

    /**
     * Fragmento secundario para registro de clientes
     **/
    public static final String FRAGMENT_MAIN_REGISTER = "fragment_main_register";

    /**
     * Fragmentos secundario para listados
     **/
    public static final String FRAGMENT_CLIENTES = "fragment_clientes";
    public static final String FRAGMENT_TRANSPORTISTAS = "fragment_transportistas";
    public static final String FRAGMENT_CHOFERES = "fragment_choferes";
    public static final String FRAGMENT_TRACTORES = "fragment_tractores";
    public static final String FRAGMENT_REMOLQUES = "fragment_remolques";

    /**
     * Contiene el TAG que le corresponde a cada ITEM del Menu
     **/
    public static final HashMap<Integer, String> ITEM_MENU_FRAGMENT;

    static {
        ITEM_MENU_FRAGMENT = new HashMap<>();
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_inicio, FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_clientes, FRAGMENT_LISTADO_CLIENTES);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_transportistas, FRAGMENT_LISTADO_TRANSPORTISTAS);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_choferes, FRAGMENT_LISTADO_CHOFERES);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_tractores, FRAGMENT_LISTADO_TRACTORES);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_remolques, FRAGMENT_LISTADO_REMOLQUES);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_agenda, FRAGMENT_LISTADO_CLIENTES);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_perfil, FRAGMENT_MAIN_REGISTER);
    }

    /**
     * Contiene el fragmento que le corresponde a cada TAG de Fragment
     **/
    public static final HashMap<String, Fragment> TAG_FRAGMENT;

    static {
        TAG_FRAGMENT = new HashMap<>();
        TAG_FRAGMENT.put(FRAGMENT_MAIN_PANEL, new MainPanelFragment());
        TAG_FRAGMENT.put(FRAGMENT_MAIN_REGISTER, new RegistroClientesFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_CLIENTES, new ListadoClientesFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_TRANSPORTISTAS, new ListadoTransportistasFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_CHOFERES, new ListadoChoferesFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_TRACTORES, new ListadoTractoresFragment());
        TAG_FRAGMENT.put(FRAGMENT_LISTADO_REMOLQUES, new ListadoRemolquesFragment());
    }


    //endregion
}
