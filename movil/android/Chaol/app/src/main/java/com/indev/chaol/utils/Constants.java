package com.indev.chaol.utils;

import android.support.v4.app.Fragment;

import com.indev.chaol.R;
import com.indev.chaol.fragments.MainPanelFragment;

import java.util.HashMap;

/**
 * Created by saurett on 24/02/2017.
 */

public final class Constants {

    //region FRAGMENT TAGS
    public static final String FRAGMENT_MAIN_PANEL = "fragment_main_panel";

    /**
     * Contenido del FRAGMENT_MAIN_PANEL
     **/
    public static final String PANEL_FLETES_CONTAINER = "panel_fletes_container";
    public static final String PANEL_CLIENTES_CONTAINER = "panel_clientes_container";
    public static final String PANEL_TRANSPORTISTAS_CONTAINER = "panel_transportistas_container";

    public static final HashMap<Integer, String> ITEM_MENU_FRAGMENT;
    static {
        ITEM_MENU_FRAGMENT = new HashMap<>();
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_inicio,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_clientes,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_transportistas,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_choferes,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_tractores,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_remolques,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_agenda,FRAGMENT_MAIN_PANEL);
        ITEM_MENU_FRAGMENT.put(R.id.menu_item_perfil,FRAGMENT_MAIN_PANEL);
    }

    public static final HashMap<String, Fragment> TAG_FRAGMENT;
    static {
        TAG_FRAGMENT = new HashMap<>();
        TAG_FRAGMENT.put(FRAGMENT_MAIN_PANEL,new MainPanelFragment());
    }



    //endregion
}