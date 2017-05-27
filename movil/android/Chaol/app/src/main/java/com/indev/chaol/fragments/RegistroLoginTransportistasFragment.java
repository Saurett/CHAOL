package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroLoginTransportistasFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private Button btnTitulo;
    private EditText txtNombre, txtRepresentanteLegal, txtRFC, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular, txtProveedorGPS, txtCorreoElectronico, txtPassword;
    private FloatingActionButton fabTransportistas;
    private ProgressDialog pDialog;

    private static MainRegisterActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_transportistas, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_transportistas);
        txtNombre = (EditText) view.findViewById(R.id.txt_transportistas_nombre);
        txtRepresentanteLegal = (EditText) view.findViewById(R.id.txt_transportistas_representante);
        txtRFC = (EditText) view.findViewById(R.id.txt_transportistas_rfc);
        txtEstado = (EditText) view.findViewById(R.id.txt_transportistas_estado);
        txtCiudad = (EditText) view.findViewById(R.id.txt_transportistas_ciudad);
        txtColonia = (EditText) view.findViewById(R.id.txt_transportistas_colonia);
        txtCodigoPostal = (EditText) view.findViewById(R.id.txt_transportistas_codigo_postal);
        txtCalle = (EditText) view.findViewById(R.id.txt_transportistas_calle);
        txtNumInt = (EditText) view.findViewById(R.id.txt_transportistas_num_int);
        txtNumExt = (EditText) view.findViewById(R.id.txt_transportistas_num_ext);
        txtTelefono = (EditText) view.findViewById(R.id.txt_transportistas_telefono);
        txtCelular = (EditText) view.findViewById(R.id.txt_transportistas_celular);
        txtProveedorGPS = (EditText) view.findViewById(R.id.txt_transportistas_proveedor_gps);
        txtCorreoElectronico = (EditText) view.findViewById(R.id.txt_transportistas_correo_electronico);
        txtPassword = (EditText) view.findViewById(R.id.txt_transportistas_password);

        fabTransportistas = (FloatingActionButton) view.findViewById(R.id.fab_transportistas);

        fabTransportistas.setOnClickListener(this);

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
            activityInterface = (MainRegisterActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    public void onPreRender() {

        String tag = _MAIN_DECODE.getFragmentTag();

        if (tag.equals(Constants.FRAGMENT_LOGIN_TRANSPORTISTAS_REGISTER)) {
            btnTitulo.setBackgroundColor(getResources().getColor(R.color.colorIcons));
            btnTitulo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                Transportistas transportistas = (Transportistas) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNombre.setText(transportistas.getNombre());

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabTransportistas.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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
            case R.id.fab_transportistas:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                   this.validationRegister();
                }
                break;
        }
    }

    private void validationRegister() {
        Boolean authorized = true;

        String email = txtCorreoElectronico.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtCorreoElectronico.setError("El campo es obligatorio", null);
            txtCorreoElectronico.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("El campo es obligatorio", null);
            txtPassword.requestFocus();
            authorized = false;
        }

        if (authorized) {
            this.createSimpleValidUser();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createSimpleValidUser() {

        Transportistas transportista = new Transportistas();

        transportista.setNombre(txtNombre.getText().toString().trim());
        transportista.setRepresentanteLegal(txtRepresentanteLegal.getText().toString().trim());
        transportista.setRFC(txtRFC.getText().toString().trim());
        transportista.setEstado(txtEstado.getText().toString().trim());
        transportista.setCiudad(txtCiudad.getText().toString().trim());
        transportista.setColonia(txtColonia.getText().toString().trim());
        transportista.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        transportista.setCalle(txtCalle.getText().toString().trim());
        transportista.setNumeroInterior(txtNumInt.getText().toString().trim());
        transportista.setNumeroExterior(txtNumExt.getText().toString().trim());
        transportista.setTelefono(txtTelefono.getText().toString().trim());
        transportista.setCelular(txtCelular.getText().toString().trim());
        transportista.setProveedorGPS(txtProveedorGPS.getText().toString().trim());
        transportista.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        transportista.setContraseña(txtPassword.getText().toString().trim());

        /**metodo principal para crear usuario**/
        activityInterface.createUserTransportista(transportista);
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
                AsyncCallWS asyncCallWS = new AsyncCallWS(Constants.WS_KEY_EDITAR_TRANSPORTISTAS);
                asyncCallWS.execute();
                break;
        }
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
                    case Constants.WS_KEY_EDITAR_TRANSPORTISTAS:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_AGREGAR_TRANSPORTISTAS:
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
                        case Constants.WS_KEY_EDITAR_TRANSPORTISTAS:
                            getActivity().finish();
                            Toast.makeText(getContext(), "Editado correctamente...", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.WS_KEY_AGREGAR_TRANSPORTISTAS:
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
