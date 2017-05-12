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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroBodegasFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private static final String TAG = RegistroBodegasFragment.class.getName();

    private Button btnTitulo;
    private EditText txtNombre, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt;
    private LinearLayout linearLayoutClientes;
    private Spinner spinnerCliente;
    private FloatingActionButton fabBodegas;
    private ProgressDialog pDialog;

    private static List<String> clientesList;
    private List<Clientes> clientes;

    private static MainRegisterActivity activityInterface;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Bodegas _bodegaActual;

    private FirebaseDatabase database;
    private DatabaseReference drClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_bodegas, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_bodegas);

        txtNombre = (EditText) view.findViewById(R.id.txt_bodegas_nombre);
        txtEstado = (EditText) view.findViewById(R.id.txt_bodegas_estado);
        txtCiudad = (EditText) view.findViewById(R.id.txt_bodegas_ciudad);
        txtColonia = (EditText) view.findViewById(R.id.txt_bodegas_colonia);
        txtCodigoPostal = (EditText) view.findViewById(R.id.txt_bodegas_codigo_postal);
        txtCalle = (EditText) view.findViewById(R.id.txt_bodegas_calle);
        txtNumInt = (EditText) view.findViewById(R.id.txt_bodegas_num_int);
        txtNumExt = (EditText) view.findViewById(R.id.txt_bodegas_num_ext);

        linearLayoutClientes = (LinearLayout) view.findViewById(R.id.item_bodegas_cliente);
        spinnerCliente = (Spinner) view.findViewById(R.id.spinner_bodegas_cliente);

        fabBodegas = (FloatingActionButton) view.findViewById(R.id.fab_bodegas);

        fabBodegas.setOnClickListener(this);
        spinnerCliente.setOnItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
        drClientes = database.getReference(Constants.FB_KEY_MAIN_CLIENTES);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        /**Metodo que llama la lista de transportistas**/
        drClientes.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clientesList = new ArrayList<>();
                clientes = new ArrayList<>();

                clientesList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psCliente : postSnapshot.getChildren()) {

                        Clientes cliente = psCliente.getValue(Clientes.class);

                        if (cliente.getFirebaseId() == null) continue;

                        clientesList.add(cliente.getNombre());
                        clientes.add(cliente);
                    }
                }

                onCargarSpinnerClientes();

                if (!_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE)) {
                    linearLayoutClientes.setVisibility(View.VISIBLE);
                }
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

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
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                Clientes clientes = (Clientes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNombre.setText(clientes.getNombre());

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabBodegas.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));

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
        Bodegas bodega = (Bodegas) _MAIN_DECODE.getDecodeItem().getItemModel();

        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES).child(bodega.getFirebaseIdCliente())
                        .child(Constants.FB_KEY_MAIN_BODEGAS);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        dbCliente.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bodegas bodega = dataSnapshot.getValue(Bodegas.class);
                /**Se asigna el chofer actual a la memoria**/
                _bodegaActual = bodega;

                txtNombre.setText(bodega.getNombreDeLaBodega());
                txtEstado.setText(bodega.getEstado());
                txtCiudad.setText(bodega.getCiudad());
                txtColonia.setText(bodega.getColonia());
                txtCodigoPostal.setText(bodega.getCodigoPostal());
                txtCalle.setText(bodega.getCalle());
                txtNumInt.setText(bodega.getNumeroInterior());
                txtNumExt.setText(bodega.getNumeroExterior());

                linearLayoutClientes.setVisibility(View.GONE);

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabBodegas.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_bodegas:
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

        String nombre = txtNombre.getText().toString();

        if (TextUtils.isEmpty(nombre)) {
            txtNombre.setError("El campo es obligatorio", null);
            txtNombre.requestFocus();
            authorized = false;
        }


        if (!_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE)) {
            if (spinnerCliente.getSelectedItemId() <= 0L) {
                TextView errorTextSE = (TextView) spinnerCliente.getSelectedView();
                errorTextSE.setError("El campo es obligatorio");
                errorTextSE.setTextColor(Color.RED);
                errorTextSE.setText("El campo es obligatorio");
                errorTextSE.requestFocus();

                authorized = false;
            }
        }

        if (authorized) {
            this.createSimpleValidBodega();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void createSimpleValidBodega() {
        Bodegas bodega = new Bodegas();

        bodega.setNombreDeLaBodega(txtNombre.getText().toString().trim());
        bodega.setEstado(txtEstado.getText().toString().trim());
        bodega.setCiudad(txtCiudad.getText().toString().trim());
        bodega.setColonia(txtColonia.getText().toString().trim());
        bodega.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        bodega.setCalle(txtCalle.getText().toString().trim());
        bodega.setNumeroInterior(txtNumInt.getText().toString().trim());
        bodega.setNumeroExterior(txtNumExt.getText().toString().trim());

        Clientes cliente = getSelectCliente();

        bodega.setFirebaseIdCliente(cliente.getFirebaseId());
        bodega.setNombreDelCliente(cliente.getNombre());

        /**metodo principal para crear usuario**/
        activityInterface.createBodegas(bodega);
    }

    /**
     * Asigna los valores de los transportista a su combo
     **/
    private void onCargarSpinnerClientes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, clientesList);

        int itemSelection = onPreRenderSelectCliente();

        spinnerCliente.setAdapter(adapter);
        spinnerCliente.setSelection(itemSelection);
    }

    private int onPreRenderSelectCliente() {
        int item = 0;

        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE)) {
            for (Clientes cliente : clientes) {
                item++;
                if (cliente.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                    break;
                }
            }
        } else if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            Clientes cliente = (Clientes) _MAIN_DECODE.getDecodeItem().getItemModel();
            for (Clientes miCliente : clientes) {
                item++;
                if (miCliente.getFirebaseId().equals(cliente.getFirebaseId())) {
                    break;
                }
            }
        }

        return item;
    }

    private Clientes getSelectCliente() {
        Clientes miCliente = new Clientes();

        for (Clientes cliente :
                clientes) {
            if (cliente.getNombre().equals(spinnerCliente.getSelectedItem().toString())) {
                miCliente = cliente;
                break;
            }
        }

        return miCliente;
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
}
