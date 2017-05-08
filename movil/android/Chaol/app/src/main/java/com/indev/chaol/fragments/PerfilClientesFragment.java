package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.NavigationDrawerActivity;
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

public class PerfilClientesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private Button btnTitulo;
    private EditText txtNombre, txtRFC, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular, txtCorreoElectronico, txtPassword;
    private LinearLayout linearLayoutPassword;
    private Spinner spinnerMetodoPago;
    private FloatingActionButton fabClientes;
    private ProgressDialog pDialog;

    private static List<String> metodosPagoList;
    private List<MetodosPagos> metodosPagos;

    private static NavigationDrawerActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Clientes _clienteActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_clientes, container, false);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

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
        txtCorreoElectronico = (EditText) view.findViewById(R.id.txt_clientes_email);
        txtPassword = (EditText) view.findViewById(R.id.txt_clientes_password);

        linearLayoutPassword = (LinearLayout) view.findViewById(R.id.item_clientes_password);

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
            activityInterface = (NavigationDrawerActivity) getActivity();
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
                /**Carga los valores iniciales al editar**/
                this.onPreRenderEditar();
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

    private void onPreRenderEditar() {
        /**Obtiene el item selecionado en el fragmento de lista**/
        FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES).child(user.getUid()).child(Constants.FB_KEY_ITEM_CLIENTE);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        dbCliente.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Clientes cliente = dataSnapshot.getValue(Clientes.class);
                /**Se asigna el chofer actual a la memoria**/
                _clienteActual = cliente;

                txtNombre.setText(cliente.getNombre());
                /**Asigna valores del item seleccionado**/
                onSelectMetodoPago(cliente.getMetodoPago());
                txtRFC.setText(cliente.getRfc());
                txtEstado.setText(cliente.getEstado());
                txtCiudad.setText(cliente.getCiudad());
                txtColonia.setText(cliente.getColonia());
                txtCodigoPostal.setText(cliente.getCodigoPostal());
                txtCalle.setText(cliente.getCalle());
                txtNumInt.setText(cliente.getNumInterior());
                txtNumExt.setText(cliente.getNumExterior());
                txtTelefono.setText(cliente.getTelefono());
                txtCelular.setText(cliente.getCelular());
                txtCorreoElectronico.setText(cliente.getCorreoElectronico());
                txtCorreoElectronico.setTag(txtCorreoElectronico.getKeyListener());
                txtCorreoElectronico.setKeyListener(null);

                linearLayoutPassword.setVisibility(View.GONE);

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabClientes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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

    /**
     * Verifica los campos obligatorios para editar
     **/
    private void validationEditer() {

        Boolean authorized = true;

        String email = txtCorreoElectronico.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtCorreoElectronico.setError("El campo es obligatorio", null);
            txtCorreoElectronico.requestFocus();
            authorized = false;
        }

        if (authorized) {
            this.updateUserCliente();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void createSimpleValidUser() {
        /*activityInterface.createSimpleUser(txtEmail.getText().toString(),
                txtPassword.getText().toString());
                */
    }

    private void updateUserCliente() {

        Clientes cliente = new Clientes();

        cliente.setNombre(txtNombre.getText().toString().trim());
        cliente.setRfc(txtRFC.getText().toString().trim());
        cliente.setEstado(txtEstado.getText().toString().trim());
        cliente.setCiudad(txtCiudad.getText().toString().trim());
        cliente.setColonia(txtColonia.getText().toString().trim());
        cliente.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        cliente.setCalle(txtCalle.getText().toString().trim());
        cliente.setNumInterior(txtNumInt.getText().toString().trim());
        cliente.setNumExterior(txtNumExt.getText().toString().trim());
        cliente.setMetodoPago(spinnerMetodoPago.getSelectedItem().toString());
        cliente.setTelefono(txtTelefono.getText().toString().trim());
        cliente.setCelular(txtCelular.getText().toString().trim());
        cliente.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        cliente.setContraseña(txtPassword.getText().toString().trim());

        cliente.setFechaDeCreacion(_clienteActual.getFechaDeCreacion());
        cliente.setEstatus(_clienteActual.getEstatus());

        /**metodo principal para actualizar usuario**/
        activityInterface.updateUserCliente(cliente);
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
                this.validationEditer();
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

    private void onSelectMetodoPago(String miMetodoPago) {
        for (MetodosPagos metodoPago :
                metodosPagos) {
            if (metodoPago.getMetodoPago().equals(miMetodoPago)) {
                spinnerMetodoPago.setSelection(metodoPago.getId());
                break;
            }
        }
    }
}
