package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indev.chaol.R;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class MainPanelFragment extends Fragment {

    private static Usuarios _SESSION_USER;
    public FragmentTransaction mainFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_panel, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        mainFragment = fragmentManager.beginTransaction();
        onPreRender();
        mainFragment.commit(); /**SIEMPRE EJECUTAR DEPSUES DEL onPreRender()**/

        return view;
    }

    /**Carga los paneles iniciales**/
    public void onPreRender() {
        this.onPreRenderSessionPanel();
    }

    /**Carga los paneles por el tipo de usuario**/
    public void onPreRenderSessionPanel() {
        switch (_SESSION_USER.getTipoDeUsuario()) {
            case  Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                mainFragment.add(R.id.panel_fletes_container, new PanelFletesFragment(), Constants.PANEL_FLETES_CONTAINER);
                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                mainFragment.add(R.id.panel_fletes_container, new PanelFletesFragment(), Constants.PANEL_FLETES_CONTAINER);
                mainFragment.add(R.id.panel_choferes_container, new PanelChoferesFragment(), Constants.PANEL_CHOFERES_CONTAINER);
                mainFragment.add(R.id.panel_tractores_container, new PanelTractoresFragment(), Constants.PANEL_TRACTORES_CONTAINER);
                mainFragment.add(R.id.panel_remolques_container, new PanelRemolquesFragment(), Constants.PANEL_REMOLQUES_CONTAINER);
                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                mainFragment.add(R.id.panel_fletes_container, new PanelFletesFragment(), Constants.PANEL_FLETES_CONTAINER);
                break;
            default:
                mainFragment.add(R.id.panel_fletes_container, new PanelFletesFragment(), Constants.PANEL_FLETES_CONTAINER);
                mainFragment.add(R.id.panel_clientes_container, new PanelClientesFragment(), Constants.PANEL_CLIENTES_CONTAINER);
                mainFragment.add(R.id.panel_transportistas_container, new PanelTransportistasFragment(), Constants.PANEL_TRANSPORTISTAS_CONTAINER);
                mainFragment.add(R.id.panel_choferes_container, new PanelChoferesFragment(), Constants.PANEL_CHOFERES_CONTAINER);
                mainFragment.add(R.id.panel_tractores_container, new PanelTractoresFragment(), Constants.PANEL_TRACTORES_CONTAINER);
                mainFragment.add(R.id.panel_remolques_container, new PanelRemolquesFragment(), Constants.PANEL_REMOLQUES_CONTAINER);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }
}
