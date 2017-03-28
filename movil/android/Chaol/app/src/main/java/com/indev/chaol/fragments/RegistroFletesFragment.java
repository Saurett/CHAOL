package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroFletesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private Button btnTitulo, btnDatosGenerales, btnCotizacion, btnSolicitudTransportista;
    private LinearLayout linearDatosGenerales, linearCotizacion, linearSolicitudTransportista;
    private FloatingActionButton fabFletes;
    private ProgressDialog pDialog;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_fletes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_fletes);
        //btnDatosGenerales = (Button) view.findViewById(R.id.btn_datos_generales);
        //btnCotizacion = (Button) view.findViewById(R.id.btn_cotizacion);
        //btnSolicitudTransportista = (Button) view.findViewById(R.id.btn_solicitud_transportista);

        //linearDatosGenerales = (LinearLayout) view.findViewById(R.id.linear_datos_generales);
        //linearCotizacion = (LinearLayout) view.findViewById(R.id.linear_cotización);
        //linearSolicitudTransportista = (LinearLayout) view.findViewById(R.id.linear_solicitud_transportista);

        fabFletes = (FloatingActionButton) view.findViewById(R.id.fab_fletes);

        //btnDatosGenerales.setOnClickListener(this);
        //btnCotizacion.setOnClickListener(this);
        //btnSolicitudTransportista.setOnClickListener(this);

        fabFletes.setOnClickListener(this);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        this.onPreRender();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncCallWS ws = new AsyncCallWS(Constants.WS_KEY_PRE_RENDER);
        ws.execute();
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
                Fletes fletes = (Fletes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabFletes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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
                    AsyncCallWS asyncCallWS = new AsyncCallWS(Constants.WS_KEY_AGREGAR_FLETES);
                    asyncCallWS.execute();
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
                AsyncCallWS asyncCallWS = new AsyncCallWS(Constants.WS_KEY_EDITAR_CLIENTES);
                asyncCallWS.execute();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private String textError;

        public AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.default_loading_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_PRE_RENDER:
                        //TODO Acción desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_EDITAR_FLETES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_AGREGAR_FLETES:
                        //TODO Acción desde el servidor
                        validOperation = true;
                        break;
                }
            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                pDialog.dismiss();
                if (success) {
                    switch (webServiceOperation) {
                        case Constants.WS_KEY_PRE_RENDER:

                            break;
                        case Constants.WS_KEY_EDITAR_FLETES:
                            if (_MAIN_DECODE.getDecodeItem().getIdView() != R.id.menu_item_perfil) {
                                getActivity().finish();
                            }
                            Toast.makeText(getContext(), "Editado correctamente...", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.WS_KEY_AGREGAR_FLETES:
                            getActivity().finish();
                            Toast.makeText(getContext(), "Guardado correctamente...", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "Lo sentimos se ha detectado un error desconocido" : textError);
                    Toast.makeText(getContext(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
