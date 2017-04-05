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
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.MetodosPagos;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroClientesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private Button btnTitulo;
    private EditText txtNombre;
    private Spinner spinnerMetodoPago;
    private FloatingActionButton fabClientes;
    private ProgressDialog pDialog;

    private static List<String> metodosPagoList;
    private List<MetodosPagos> metodosPagos;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_clientes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);
        txtNombre = (EditText) view.findViewById(R.id.txt_clientes_nombre);

        spinnerMetodoPago = (Spinner) view.findViewById(R.id.spinner_clientes_metodo_pago);

        fabClientes = (FloatingActionButton) view.findViewById(R.id.fab_clientes);

        fabClientes.setOnClickListener(this);

        spinnerMetodoPago.setOnItemSelectedListener(this);

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

        String tag = _MAIN_DECODE.getFragmentTag();

        if (tag.equals(Constants.FRAGMENT_LOGIN_REGISTER)) {
            btnTitulo.setBackgroundColor(getResources().getColor(R.color.colorIcons));
            btnTitulo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                Clientes clientes = (Clientes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNombre.setText(clientes.getNombre());

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                    AsyncCallWS asyncCallWS = new AsyncCallWS(Constants.WS_KEY_AGREGAR_CLIENTES);
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
                        this.onCargarMetodosPagos();
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_EDITAR_CLIENTES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_AGREGAR_CLIENTES:
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
                                    R.layout.text_spinner, metodosPagoList);

                            /*
                            int selectionState = (null != _PROFILE_MANAGER.getAddressProfile().getIdItemState())
                                    ? _PROFILE_MANAGER.getAddressProfile().getIdItemState() : 0;
                                    */

                            spinnerMetodoPago.setAdapter(adapter);
                            spinnerMetodoPago.setSelection(0);

                            break;
                        case Constants.WS_KEY_EDITAR_CLIENTES:
                            if (_MAIN_DECODE.getDecodeItem().getIdView() != R.id.menu_item_perfil) {
                                getActivity().finish();
                            }
                            Toast.makeText(getContext(), "Editado correctamente...", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.WS_KEY_AGREGAR_CLIENTES:
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

        /**
         * Metodos privados en la clase AsynTask
         **/
        private void onCargarMetodosPagos() {
            metodosPagoList = new ArrayList<>();
            metodosPagos = new ArrayList<>();
            metodosPagoList.add("Seleccione ...");

            //TODO Metodo para llamar al servidor
            metodosPagoList.add("Efectivo");
            metodosPagoList.add("Cheque");
            metodosPagoList.add("Transferencia Electronica");
            metodosPagoList.add("Tarjeta de Crédito");
            metodosPagoList.add("Dinero Electrónico");
            metodosPagoList.add("Tarjeta de Débito");
            metodosPagoList.add("NA");
            metodosPagoList.add("Otros");

            metodosPagos.add(new MetodosPagos(1, "Efectivo"));
            metodosPagos.add(new MetodosPagos(2, "Cheque"));
            metodosPagos.add(new MetodosPagos(3, "Transferencia Electronica"));
            metodosPagos.add(new MetodosPagos(4, "Tarjeta de Crédito"));
            metodosPagos.add(new MetodosPagos(5, "Dinero Electrónico"));
            metodosPagos.add(new MetodosPagos(6, "Tarjeta de Débito"));
            metodosPagos.add(new MetodosPagos(7, "NA"));
            metodosPagos.add(new MetodosPagos(8, "Otros"));
        }
    }
}
