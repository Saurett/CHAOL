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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroTransportistasFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private Button btnTitulo;
    private EditText txtNombre, txtRepresentanteLegal, txtRFC, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular, txtProveedorGPS, txtCorreoElectronico, txtPassword;
    private LinearLayout linearLayoutPassword;
    private FloatingActionButton fabTransportistas;
    private ProgressDialog pDialog;

    private static MainRegisterActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Transportistas _transportistActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_transportistas, container, false);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

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

        linearLayoutPassword = (LinearLayout) view.findViewById(R.id.item_transportistas_password);

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
        Transportistas transportista = (Transportistas) _MAIN_DECODE.getDecodeItem().getItemModel();

        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS).child(transportista.getFirebaseId())
                        .child(Constants.FB_KEY_ITEM_TRANSPORTISTA);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        dbTransportista.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Transportistas transportista = dataSnapshot.getValue(Transportistas.class);
                /**se asigna el transportista actual a la memoria**/
                _transportistActual = transportista;

                txtNombre.setText(transportista.getNombre());
                txtRepresentanteLegal.setText(transportista.getRepresentanteLegal());
                txtRFC.setText(transportista.getRFC());
                txtEstado.setText(transportista.getEstado());
                txtCiudad.setText(transportista.getCiudad());
                txtCiudad.setText(transportista.getCiudad());
                txtColonia.setText(transportista.getColonia());
                txtCodigoPostal.setText(transportista.getCodigoPostal());
                txtCalle.setText(transportista.getCalle());
                txtNumInt.setText(transportista.getNumeroInterior());
                txtNumExt.setText(transportista.getNumeroExterior());
                txtTelefono.setText(transportista.getTelefono());
                txtCelular.setText(transportista.getCelular());
                txtProveedorGPS.setText(transportista.getProoveedorGPS());
                txtCorreoElectronico.setText(transportista.getCorreoElectronico());

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
        fabTransportistas.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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

    private void createSimpleValidUser() {

    }

    private void validationEditer() {

        Boolean authorized = true;

        String email = txtCorreoElectronico.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtCorreoElectronico.setError("El campo es obligatorio", null);
            txtCorreoElectronico.requestFocus();
            authorized = false;
        }

        if (authorized) {
            this.updateUserTransportista();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUserTransportista() {

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
        transportista.setProoveedorGPS(txtProveedorGPS.getText().toString().trim());
        transportista.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        transportista.setContraseña(txtPassword.getText().toString().trim());

        transportista.setFechaDeCreacion(_transportistActual.getFechaDeCreacion());
        transportista.setEstatus(_transportistActual.getEstatus());

        /**metodo principal para actualizar usuario**/
        //activityInterface.updateUserTransportista(transportista);
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
}
