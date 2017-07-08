package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.MainRegisterInterface;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroFletesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private Button btnTitulo;
    private static BootstrapProgressBar fleteProgressBar;
    private MainRegisterInterface activityInterface;
    private static FrameLayout frameCotizacion, frameAsignacion, frameEquipo, frameProceso;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_fletes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        frameCotizacion = (FrameLayout) view.findViewById(R.id.fragment_cotizacion_container);
        frameAsignacion = (FrameLayout) view.findViewById(R.id.fragment_asignacion_container);
        frameEquipo = (FrameLayout) view.findViewById(R.id.fragment_equipo_container);
        frameProceso = (FrameLayout) view.findViewById(R.id.fragment_proceso_container);

        fleteProgressBar = (BootstrapProgressBar) view.findViewById(R.id.flete_progressBar);
        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_fletes);

        activityInterface.removeSecondaryFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction mainFragment = fragmentManager.beginTransaction();

        mainFragment.replace(R.id.fragment_datos_generales_container, new FletesDatosGeneralesFragment(), Constants.DATOS_GENERALES_FLETES_CONTAINER);
        mainFragment.replace(R.id.fragment_cotizacion_container, new FletesCotizacionFragment(), Constants.COTIZACION_FLETES_CONTAINER);
        mainFragment.replace(R.id.fragment_asignacion_container, new FletesAsignacionFragment(), Constants.ASIGNACION_FLETES_CONTAINER);
        mainFragment.replace(R.id.fragment_equipo_container, new FletesEquipoFragment(), Constants.EQUIPO_FLETES_CONTAINER);
        mainFragment.replace(R.id.fragment_proceso_container, new FletesProcesoFragment(), Constants.PROCESO_FLETES_CONTAINER);

        mainFragment.commit();

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
            activityInterface = (MainRegisterInterface) getActivity();
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

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {

                }
                break;
        }
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

    public static void setFleteProgressBar(int progress) {
        fleteProgressBar.setProgress(progress);
    }

    public static void setFrameCotizacion(int visible) { frameCotizacion.setVisibility(visible);}

    public static void setFrameAsignacion(int visible) { frameAsignacion.setVisibility(visible);}

    public static void setFrameEquipo(int visible) { frameEquipo.setVisibility(visible);}

    public static void setFrameProgreso(int visible) { frameProceso.setVisibility(visible);}
}
