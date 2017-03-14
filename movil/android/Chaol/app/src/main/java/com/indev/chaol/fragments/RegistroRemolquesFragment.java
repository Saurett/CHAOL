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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.MetodosPagos;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.TiposRemolques;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroRemolquesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnTitulo;
    private EditText txtNumEconomico;
    private Spinner spinnerTipoRemolque;
    private FloatingActionButton fabRemolques;
    private ProgressDialog pDialog;

    private static List<String> tiposRemolquesList;
    private List<TiposRemolques> tiposRemolques;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_remolques, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_remolques);
        txtNumEconomico = (EditText) view.findViewById(R.id.txt_remolques_num_economico);

        spinnerTipoRemolque = (Spinner) view.findViewById(R.id.spinner_remolques_tipo_remolque);

        fabRemolques = (FloatingActionButton) view.findViewById(R.id.fab_remolques);

        fabRemolques.setOnClickListener(this);

        spinnerTipoRemolque.setOnItemSelectedListener(this);

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
                Remolques remolques = (Remolques) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNumEconomico.setText(remolques.getNumEconomico());

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabRemolques.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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
            case R.id.fab_remolques:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                    AsyncCallWS asyncCallWS = new AsyncCallWS(Constants.WS_KEY_AGREGAR_REMOLQUES);
                    asyncCallWS.execute();
                }
                break;
        }
    }

    public void showQuestion() {
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
                AsyncCallWS asyncCallWS = new AsyncCallWS(Constants.WS_KEY_EDITAR_REMOLQUES);
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
                        this.onCargarTiposRemolques();
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_EDITAR_REMOLQUES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_AGREGAR_REMOLQUES:
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.text_spinner, tiposRemolquesList);

                            /*
                            int selectionState = (null != _PROFILE_MANAGER.getAddressProfile().getIdItemState())
                                    ? _PROFILE_MANAGER.getAddressProfile().getIdItemState() : 0;
                                    */

                            spinnerTipoRemolque.setAdapter(adapter);
                            spinnerTipoRemolque.setSelection(0);
                            break;
                        case Constants.WS_KEY_EDITAR_REMOLQUES:
                            getActivity().finish();
                            Toast.makeText(getContext(), "Editado correctamente...", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.WS_KEY_AGREGAR_REMOLQUES:
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

        private void onCargarTiposRemolques() {
            tiposRemolquesList = new ArrayList<>();
            tiposRemolques = new ArrayList<>();
            tiposRemolquesList.add("Seleccione ...");

            //TODO Metodo para llamar al servidor
            tiposRemolquesList.add("Frios");

            tiposRemolques.add(new TiposRemolques(1, "Frios"));

        }
    }


}
