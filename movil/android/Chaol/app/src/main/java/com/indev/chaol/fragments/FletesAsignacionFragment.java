package com.indev.chaol.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesAsignacionFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private Button btnTitulo;
    private static TextView txtEstatusTransportista;
    private LinearLayout linearLayout;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    private static MainFletes _mainFletesActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignacion_fletes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_datos_asignacion_fletes);
        txtEstatusTransportista = (TextView) view.findViewById(R.id.txt_asignacion_estatus_transportista);

        linearLayout = (LinearLayout) view.findViewById(R.id.asignacion_container);

        btnTitulo.setOnClickListener(this);
        linearLayout.setVisibility(View.GONE);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        this.onPreRender();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
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
                //Fletes fletes = (Fletes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                break;
            case Constants.ACCION_REGISTRAR:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_datos_asignacion_fletes:

                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                if (linearLayout.getVisibility() == View.VISIBLE) {

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction mainFragment = fragmentManager.beginTransaction();
                    mainFragment.replace(R.id.listado_asignaciones_container, new AsignacionTransportistasFragment(), Constants.ASIGNACION_TRANSPORTISTAS_FLETES_CONTAINER);
                    mainFragment.commit();

                }
                break;
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {

                }
                break;
        }
    }

    public static void showMessageAsignacion(int visible) {
        txtEstatusTransportista.setVisibility(visible);
    }

    private void showQuestion() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle(btnTitulo.getText());
        ad.setMessage("¿Esta seguro que desea editar?");
        ad.setCancelable(false);
        ad.setNegativeButton(getString(R.string.default_alert_dialog_cancelar), this);
        ad.setPositiveButton(getString(R.string.default_alert_dialog_aceptar), this);
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                break;
        }
    }
}
