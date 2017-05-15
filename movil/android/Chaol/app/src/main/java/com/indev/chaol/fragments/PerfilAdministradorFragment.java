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
import com.indev.chaol.models.Administradores;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class PerfilAdministradorFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private Button btnTitulo;
    private EditText txtNombre, txtCorreoElectronico, txtPassword;
    private LinearLayout linearLayoutPassword;
    private FloatingActionButton fabAdministradores;
    private ProgressDialog pDialog;

    private static NavigationDrawerActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Administradores _administradorActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_administradores, container, false);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_administradores);
        txtNombre = (EditText) view.findViewById(R.id.txt_administradores_nombre);
        txtCorreoElectronico = (EditText) view.findViewById(R.id.txt_administradores_correo_electronico);
        txtPassword = (EditText) view.findViewById(R.id.txt_administradores_password);

        linearLayoutPassword = (LinearLayout) view.findViewById(R.id.item_administradores_password);

        fabAdministradores = (FloatingActionButton) view.findViewById(R.id.fab_administradores);

        fabAdministradores.setOnClickListener(this);

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
        FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child("administradores").child(user.getUid());

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        dbTransportista.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Administradores administrador = dataSnapshot.getValue(Administradores.class);
                /**e asigna el transportista actual a la memoria**/
                _administradorActual = administrador;

                txtNombre.setText(administrador.getNombre());
                txtCorreoElectronico.setText(administrador.getCorreoElectronico());

                linearLayoutPassword.setVisibility(View.GONE);

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabAdministradores.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_administradores:
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
            this.updateUserAdministrador();
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

    private void updateUserAdministrador() {

        Administradores administrador = new Administradores();

        administrador.setNombre(txtNombre.getText().toString().trim());
        administrador.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        administrador.setContraseña(txtPassword.getText().toString().trim());

        administrador.setFechaDeCreacion(_administradorActual.getFechaDeCreacion());
        administrador.setEstatus(_administradorActual.getEstatus());

        /**metodo principal para actualizar usuario**/
        activityInterface.updateUserAdministrador(administrador);
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
}
