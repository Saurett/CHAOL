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
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroChoferesFragment extends Fragment implements View.OnClickListener, AlertDialog.OnClickListener {

    private static final String TAG = RegistroChoferesFragment.class.getName();

    private Button btnTitulo;
    private EditText txtNombre, txtNumeroLicencia, txtNSS, txtCURP, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular1, txtCelular2, txtCorreoElectronico, txtPassword;
    private LinearLayout linearLayoutPassword;
    private Spinner spinnerEmpresa;
    private FloatingActionButton fabChoferes;
    private ProgressDialog pDialog;

    private static List<String> transportistasList;
    private List<Transportistas> transportistas;

    private static MainRegisterActivity activityInterface;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Choferes _choferActual;

    private FirebaseDatabase database;
    private DatabaseReference drTransportistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_choferes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_choferes);
        txtNombre = (EditText) view.findViewById(R.id.txt_choferes_nombre);
        txtNumeroLicencia = (EditText) view.findViewById(R.id.txt_choferes_licencia);
        txtNSS = (EditText) view.findViewById(R.id.txt_choferes_nss);
        txtCURP = (EditText) view.findViewById(R.id.txt_choferes_curp);
        txtEstado = (EditText) view.findViewById(R.id.txt_choferes_estado);
        txtCiudad = (EditText) view.findViewById(R.id.txt_choferes_ciudad);
        txtColonia = (EditText) view.findViewById(R.id.txt_choferes_colonia);
        txtCodigoPostal = (EditText) view.findViewById(R.id.txt_choferes_codigo_postal);
        txtCalle = (EditText) view.findViewById(R.id.txt_choferes_calle);
        txtNumInt = (EditText) view.findViewById(R.id.txt_choferes_num_int);
        txtNumExt = (EditText) view.findViewById(R.id.txt_choferes_num_ext);
        txtTelefono = (EditText) view.findViewById(R.id.txt_choferes_telefono);
        txtCelular1 = (EditText) view.findViewById(R.id.txt_choferes_celular_opc1);
        txtCelular2 = (EditText) view.findViewById(R.id.txt_choferes_celular_opc2);
        txtCorreoElectronico = (EditText) view.findViewById(R.id.txt_choferes_correo_electronico);
        txtPassword = (EditText) view.findViewById(R.id.txt_choferes_password);

        spinnerEmpresa = (Spinner) view.findViewById(R.id.spinner_choferes_empresa);
        linearLayoutPassword = (LinearLayout) view.findViewById(R.id.item_choferes_password);

        fabChoferes = (FloatingActionButton) view.findViewById(R.id.fab_choferes);
        fabChoferes.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        drTransportistas = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        /**Metodo que llama la lista de transportistas**/
        drTransportistas.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                transportistasList = new ArrayList<>();
                transportistas = new ArrayList<>();

                transportistasList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psTransportista : postSnapshot.getChildren()) {

                        Transportistas transportista = psTransportista.getValue(Transportistas.class);

                        if (Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA.equals(transportista.getTipoDeUsuario())) {

                            if (Constants.FB_KEY_ITEM_ESTATUS_ACTIVO.equals(transportista.getEstatus())) {

                                String nombre = transportista.getNombre();
                                String firebaseID = transportista.getFirebaseId();

                                transportistasList.add(nombre);
                                transportistas.add(new Transportistas(firebaseID, nombre));

                            }
                        }
                    }
                }

                onCargarSpinnerTransportistas();

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
        Choferes chofer = (Choferes) _MAIN_DECODE.getDecodeItem().getItemModel();

        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CHOFERES).child(chofer.getFirebaseId());

        final ProgressDialog pDialogRender = new ProgressDialog(getContext());
        pDialogRender.setMessage(getString(R.string.default_loading_msg));
        pDialogRender.setIndeterminate(false);
        pDialogRender.setCancelable(false);
        pDialogRender.show();

        dbChofer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Choferes chofer = dataSnapshot.getValue(Choferes.class);
                /**Se asigna el chofer actual a la memoria**/
                _choferActual = chofer;

                txtNombre.setText(chofer.getNombre());
                /**Asigna valores del item seleccionado**/
                txtNumeroLicencia.setText(chofer.getNumeroDeLicencia());
                txtNSS.setText(chofer.getNumeroDeSeguroSocial());
                txtCURP.setText(chofer.getCURP());
                txtEstado.setText(chofer.getEstado());
                txtCiudad.setText(chofer.getCiudad());
                txtColonia.setText(chofer.getColonia());
                txtCodigoPostal.setText(chofer.getCodigoPostal());
                txtCalle.setText(chofer.getCalle());
                txtNumInt.setText(chofer.getNumeroInterior());
                txtNumExt.setText(chofer.getNumeroExterior());
                txtTelefono.setText(chofer.getTelefono());
                txtCelular1.setText(chofer.getCelular1());
                txtCelular2.setText(chofer.getCelular2());
                txtCorreoElectronico.setText(chofer.getCorreoElectronico());

                txtCorreoElectronico.setTag(txtCorreoElectronico.getKeyListener());
                txtCorreoElectronico.setKeyListener(null);

                spinnerEmpresa.setEnabled(false);

                linearLayoutPassword.setVisibility(View.GONE);

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabChoferes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_choferes:
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

        if (spinnerEmpresa.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerEmpresa.getSelectedView();
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

        Choferes chofer = new Choferes();

        chofer.setNombre(txtNombre.getText().toString().trim());
        chofer.setNumeroDeLicencia(txtNumeroLicencia.getText().toString().trim());
        chofer.setNumeroDeSeguroSocial(txtNSS.getText().toString().trim());
        chofer.setCURP(txtCURP.getText().toString().trim());
        chofer.setEstado(txtEstado.getText().toString().trim());
        chofer.setCiudad(txtCiudad.getText().toString().trim());
        chofer.setColonia(txtColonia.getText().toString().trim());
        chofer.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        chofer.setCalle(txtCalle.getText().toString().trim());
        chofer.setNumeroInterior(txtNumInt.getText().toString().trim());
        chofer.setNumeroExterior(txtNumExt.getText().toString().trim());
        chofer.setTelefono(txtTelefono.getText().toString().trim());
        chofer.setCelular1(txtCelular1.getText().toString().trim());
        chofer.setCelular2(txtCelular2.getText().toString().trim());
        chofer.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        chofer.setContraseña(txtPassword.getText().toString().trim());

        chofer.setFirebaseIdDelTransportista(getSelectTransportista());

        /**metodo principal para crear usuario**/
        activityInterface.createUserChofer(chofer);
    }

    /**Verifica los campos obligatorios para editar**/
    private void validationEditer() {

        Boolean authorized = true;

        String email = txtCorreoElectronico.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtCorreoElectronico.setError("El campo es obligatorio", null);
            txtCorreoElectronico.requestFocus();
            authorized = false;
        }

        if (authorized) {
            this.updateUserChofer();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUserChofer() {

        Choferes chofer = new Choferes();

        chofer.setNombre(txtNombre.getText().toString().trim());
        chofer.setNumeroDeLicencia(txtNumeroLicencia.getText().toString().trim());
        chofer.setNumeroDeSeguroSocial(txtNSS.getText().toString().trim());
        chofer.setCURP(txtCURP.getText().toString().trim());
        chofer.setEstado(txtEstado.getText().toString().trim());
        chofer.setCiudad(txtCiudad.getText().toString().trim());
        chofer.setColonia(txtColonia.getText().toString().trim());
        chofer.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        chofer.setCalle(txtCalle.getText().toString().trim());
        chofer.setNumeroInterior(txtNumInt.getText().toString().trim());
        chofer.setNumeroExterior(txtNumExt.getText().toString().trim());
        chofer.setTelefono(txtTelefono.getText().toString().trim());
        chofer.setCelular1(txtCelular1.getText().toString().trim());
        chofer.setCelular2(txtCelular2.getText().toString().trim());
        chofer.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        chofer.setContraseña(txtPassword.getText().toString().trim());

        chofer.setFirebaseId(_choferActual.getFirebaseId());
        chofer.setFechaDeCreacion(_choferActual.getFechaDeCreacion());
        chofer.setFirebaseIdDelTransportista(_choferActual.getFirebaseIdDelTransportista());
        chofer.setEstatus(_choferActual.getEstatus());

        /**metodo principal para actualizar usuario**/
        activityInterface.updateChofer(chofer);
    }


    private void onCargarSpinnerTransportistas() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, transportistasList);

        int itemSelection = onPreRenderSelectTransportista();

        spinnerEmpresa.setAdapter(adapter);
        spinnerEmpresa.setSelection(itemSelection);
    }

    /**Obtiene el firebaseID del transportista seleccionado**/
    private String getSelectTransportista() {

        String firebaseID = "";

        for (Transportistas transportista:
                transportistas) {
            if (transportista.getNombre().equals(spinnerEmpresa.getSelectedItem().toString())) {
                firebaseID = transportista.getFirebaseId();
                break;
            }
        }

        return  firebaseID;
    }

    private int onPreRenderSelectTransportista() {
        int item = 0;

        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
            for (Transportistas transportista : transportistas) {
                item++;
                if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                    break;
                }
            }
        } else if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            Choferes chofer = (Choferes) _MAIN_DECODE.getDecodeItem().getItemModel();
            for (Transportistas transportista : transportistas) {
                item++;
                if (transportista.getFirebaseId().equals(chofer.getFirebaseIdDelTransportista())) {
                    break;
                }
            }
        }

        return item;
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
