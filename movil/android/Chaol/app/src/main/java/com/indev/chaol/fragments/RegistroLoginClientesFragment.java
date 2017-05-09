package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indev.chaol.MainRegisterActivity;
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

public class RegistroLoginClientesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private Button btnTitulo;
    private EditText txtNombre, txtRFC, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular, txtEmail, txtPassword;
    private Spinner spinnerMetodoPago;
    private FloatingActionButton fabClientes;
    private ProgressDialog pDialog;

    private static List<String> metodosPagoList;
    private List<MetodosPagos> metodosPagos;

    private static MainRegisterActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_clientes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);

        txtNombre = (EditText) view.findViewById(R.id.txt_clientes_nombre);
        txtRFC = (EditText) view.findViewById(R.id.txt_clientes_rfc);
        txtEstado = (EditText) view.findViewById(R.id.txt_clientes_estado);
        txtCiudad = (EditText) view.findViewById(R.id.txt_clientes_ciudad);
        txtColonia = (EditText) view.findViewById(R.id.txt_clientes_colonia);
        txtCodigoPostal = (EditText) view.findViewById(R.id.txt_clientes_codigo_postal);
        txtCalle = (EditText) view.findViewById(R.id.txt_clientes_calle);
        txtNumInt = (EditText) view.findViewById(R.id.txt_clientes_num_int);
        txtNumExt = (EditText) view.findViewById(R.id.txt_clientes_num_ext);
        txtTelefono = (EditText) view.findViewById(R.id.txt_clientes_telefono);
        txtCelular = (EditText) view.findViewById(R.id.txt_clientes_celular);
        txtEmail = (EditText) view.findViewById(R.id.txt_clientes_email);
        txtPassword = (EditText) view.findViewById(R.id.txt_clientes_password);

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

        if (tag.equals(Constants.FRAGMENT_LOGIN_REGISTER)) {
            btnTitulo.setBackgroundColor(getResources().getColor(R.color.colorIcons));
            btnTitulo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        /**Carga los elementos del combo**/
        this.onCargarMetodosPagos();
        this.onCargarSpinnerMetodosPagos();

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
                    this.validationRegister();
                }
                break;
        }
    }

    /**
     * Verifica los campos obligatorios para registro de cliente
     **/
    private void validationRegister() {

        Boolean authorized = true;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("El campo es obligatorio", null);
            txtEmail.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("El campo es obligatorio", null);
            txtPassword.requestFocus();
            authorized = false;
        }

        if (spinnerMetodoPago.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerMetodoPago.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

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

        Clientes clientes = new Clientes();

        clientes.setNombre(txtNombre.getText().toString().trim());
        clientes.setRFC(txtRFC.getText().toString().trim());
        clientes.setEstado(txtEstado.getText().toString().trim());
        clientes.setCiudad(txtCiudad.getText().toString().trim());
        clientes.setColonia(txtColonia.getText().toString().trim());
        clientes.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        clientes.setCalle(txtCalle.getText().toString().trim());
        clientes.setNumInterior(txtNumInt.getText().toString().trim());
        clientes.setNumExterior(txtNumExt.getText().toString().trim());
        clientes.setMetodoPago(spinnerMetodoPago.getSelectedItem().toString());
        clientes.setTelefono(txtTelefono.getText().toString().trim());
        clientes.setCelular(txtCelular.getText().toString().trim());
        clientes.setCorreoElectronico(txtEmail.getText().toString().trim());
        clientes.setContraseña(txtPassword.getText().toString().trim());

        /**metodo principal para crear usuario**/
        activityInterface.createUserCliente(clientes);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void onCargarSpinnerMetodosPagos() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, metodosPagoList);

        spinnerMetodoPago.setAdapter(adapter);
        spinnerMetodoPago.setSelection(0);
    }
}
