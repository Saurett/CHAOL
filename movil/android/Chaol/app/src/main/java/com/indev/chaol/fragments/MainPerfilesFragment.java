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
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class MainPerfilesFragment extends Fragment {

    private static Usuarios _SESSION_USER;

    private FragmentManager fragmentManager;
    private FragmentTransaction mainFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfiles, container, false);

        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        fragmentManager = getActivity().getSupportFragmentManager();
        mainFragment = fragmentManager.beginTransaction();

        this.onPreRender();

        return view;
    }

    /**Carga los elementos iniciales del fragmento**/
    private void onPreRender() {
        this.onPreRenderSessionFragment();
    }

    /**Selecciona el fragmento a editar**/
    private void onPreRenderSessionFragment() {

        DecodeItem decodeItem = new DecodeItem();
        decodeItem.setIdView(R.id.menu_item_perfil);

        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setAccionFragmento(Constants.ACCION_EDITAR);
        extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(R.id.menu_item_perfil));
        extraParams.setDecodeItem(decodeItem);

        getActivity().getIntent().putExtra(Constants.KEY_MAIN_DECODE,extraParams);

        switch (_SESSION_USER.getTipoUsuario()) {
            case Constants.FB_KEY_USUARIO_CLIENTE:
                mainFragment.replace(R.id.panel_perfiles_container, new PerfilClientesFragment(), Constants.FRAGMENT_MAIN_PERFIL);
                mainFragment.commit();
                break;
            case Constants.FB_KEY_USUARIO_TRANSPORTISTA:
                mainFragment.replace(R.id.panel_perfiles_container, new PerfilTransportistasFragment(), Constants.FRAGMENT_MAIN_PERFIL);
                mainFragment.commit();
                break;
            case Constants.FB_KEY_USUARIO_CHOFER:
                mainFragment.replace(R.id.panel_perfiles_container, new PerfilChoferesFragment(), Constants.FRAGMENT_MAIN_PERFIL);
                mainFragment.commit();
                break;
            default:
                mainFragment.replace(R.id.panel_perfiles_container, new PerfilAdministradorFragment(), Constants.FRAGMENT_MAIN_PERFIL);
                mainFragment.commit();
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
