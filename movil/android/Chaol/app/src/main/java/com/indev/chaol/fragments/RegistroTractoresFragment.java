package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroTractoresFragment extends Fragment {

    private Button btnTitulo;
    private EditText txtNombre;
    private FloatingActionButton fabClientes;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_tractores, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_tractores);
        txtNombre = (EditText) view.findViewById(R.id.txt_tractores_nombre);
        fabClientes = (FloatingActionButton) view.findViewById(R.id.fab_tractores);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        this.onPreRender();

        return view;
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

    public void onPreRender() {
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                Tractores tractores = (Tractores) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNombre.setText(tractores.getNombre());

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabClientes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
                break;
            case Constants.ACCION_REGISTRAR:
                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                break;
            default:
                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText("Perfil");
                break;
        }
    }
}