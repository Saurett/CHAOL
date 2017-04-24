package com.indev.chaol;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indev.chaol.fragments.interfaces.MainRegisterInterface;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.indev.chaol.utils.ErrorMessages;

import java.util.List;

public class MainRegisterActivity extends AppCompatActivity implements MainRegisterInterface, View.OnClickListener {

    private static final String TAG = MainRegisterActivity.class.getName();

    private static Button btnFormCliente, btnFormTransportista, btnFormChofer;
    private static LinearLayout linearLayoutSwitch;
    private static ScrollView scrollViewRegister;
    private static DecodeExtraParams _MAIN_DECODE;

    private ProgressDialog pDialog;
    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_register);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        btnFormCliente = (Button) findViewById(R.id.btn_form_cliente);
        btnFormTransportista = (Button) findViewById(R.id.btn_form_transportista);
        btnFormChofer = (Button) findViewById(R.id.btn_form_chofer);
        linearLayoutSwitch = (LinearLayout) findViewById(R.id.linear_switch_form);
        scrollViewRegister = (ScrollView) findViewById(R.id.scrollView_register);

        _MAIN_DECODE = (DecodeExtraParams) getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        btnFormCliente.setOnClickListener(this);
        btnFormTransportista.setOnClickListener(this);
        btnFormChofer.setOnClickListener(this);

        setTitle(_MAIN_DECODE.getTituloActividad());

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();
        /**Responde a los cambios de estato en la session**/
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        this.onPreRender();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void onPreRender() {

        this.showSwitchBtn();

        /**Adinistrar los fragmentos dinamicos**/
        closeFragment(_MAIN_DECODE.getFragmentTag());
        openFragment(_MAIN_DECODE.getFragmentTag());
    }

    private void showSwitchBtn() {
        String tag = _MAIN_DECODE.getFragmentTag();

        switch (tag) {
            case Constants.FRAGMENT_LOGIN_REGISTER:
                linearLayoutSwitch.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeFragment(_MAIN_DECODE.getFragmentTag());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeFragment(_MAIN_DECODE.getFragmentTag());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Valida el tag enviado y cierra si existe el fragmento
     **/
    private void closeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

    }

    /**
     * Abre el fragmento mediante el tag seleccionado
     **/
    private void openFragment(String tag) {
        FragmentTransaction mainFragment = getSupportFragmentManager().beginTransaction();
        mainFragment.add(R.id.fragment_main_register_container, Constants.TAG_FRAGMENT.get(tag), tag);
        mainFragment.addToBackStack(tag);
        mainFragment.commit();
    }

    @Override
    public void onChangeMainFragment(int idView) {

    }

    @Override
    public void removeSecondaryFragment() {
        List<String> secondaryFragments = Constants.FLETES_TAG_FRAGMENTS;

        for (String tag :
                secondaryFragments) {
            closeFragment(tag);
        }
    }

    @Override
    public void showQuestion() {

    }

    @Override
    public void openExternalActivity(int action, Class<?> externalActivity) {

    }

    @Override
    public void setDecodeItem(DecodeItem decodeItem) {

    }

    @Override
    public DecodeItem getDecodeItem() {
        return null;
    }

    @Override
    public void createUserCliente(final Clientes cliente) {

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        mAuth.createUserWithEmailAndPassword(cliente.getCorreoElectronico(), cliente.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pDialog.dismiss();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseRegistroCliente(cliente);
                            sendEmailVerification();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void createUserTransportista(final Transportistas transportista) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        mAuth.createUserWithEmailAndPassword(transportista.getCorreoElectronico(), transportista.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pDialog.dismiss();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseRegistroTransportista(transportista);
                            sendEmailVerification();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void createUserChofer(final Choferes chofer) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        mAuth.createUserWithEmailAndPassword(chofer.getCorreoElectronico(), chofer.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pDialog.dismiss();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseRegistroChoferes(chofer);
                            sendEmailVerification();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**Registra en firebase al cliente y lo agrega como usuario**/
    public void firebaseRegistroCliente(Clientes cliente) {
        FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como usuario**/
        DatabaseReference dbUsuario =
                FirebaseDatabase.getInstance().getReference()
                        .child("usuarios");

        /**obtiene la instancia como cliente**/
        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child("clientes");

        cliente.setTipoUsuario("cliente");
        cliente.setFirebaseID(user.getUid());
        cliente.setEstatus("activo");
        cliente.setContraseña(null);
        cliente.setFechaDeCreacion(DateTimeUtils.getTimeStamp());

        dbCliente.child(user.getUid()).setValue(cliente);
        dbUsuario.child(user.getUid()).setValue(cliente.getTipoUsuario());

        Log.i(TAG,"firebaseRegistroCliente: Registrado correctamente" + user.getUid());
    }

    /**Registra en firebase al transportista y lo agrega como usuario**/
    private void firebaseRegistroTransportista(Transportistas transportista) {
        FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como usuario**/
        DatabaseReference dbUsuario =
                FirebaseDatabase.getInstance().getReference()
                        .child("usuarios");

        /**obtiene la instancia como transportista**/
        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child("transportistas");

        /**obtiene la instancia como listaDeTransportistas**/
        DatabaseReference dbListaTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child("listaDeTransportistas");

        transportista.setTipoUsuario("transportista");
        transportista.setFirebaseID(user.getUid());
        transportista.setContraseña(null);
        transportista.setEstatus("activo");
        transportista.setFechaDeCreacion(DateTimeUtils.getTimeStamp());

        dbTransportista.child(user.getUid()).child("transportista").setValue(transportista);
        dbUsuario.child(user.getUid()).setValue(transportista.getTipoUsuario());
        dbListaTransportista.child(user.getUid()).setValue(transportista.getNombre());

        Log.i(TAG,"firebaseRegistroTransportista: Registrado correctamente" + user.getUid());
    }

    /**Registra en firebase al transportista y lo agrega como usuario**/
    private void firebaseRegistroChoferes(Choferes chofer) {
        FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como usuario**/
        DatabaseReference dbUsuario =
                FirebaseDatabase.getInstance().getReference()
                        .child("usuarios");

        /**obtiene la instancia como transportista**/
        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child("transportistas");

        /**obtiene la instancia como chofer**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child("choferes");

        chofer.setTipoUsuario("chofer");
        chofer.setFirebaseID(user.getUid());
        chofer.setContraseña(null);
        chofer.setEstatus("inactivo");
        chofer.setFechaDeCreacion(DateTimeUtils.getTimeStamp());

        dbChofer.child(user.getUid()).setValue(chofer);
        dbUsuario.child(user.getUid()).setValue(chofer.getTipoUsuario());
        //dbTransportista.child(user.getUid()).child("chofer").setValue(chofer);

        Log.i(TAG,"firebaseRegistroChoferes: Registrado correctamente" + user.getUid());

    }

    /**
     * Envia correo de verificación para ser un usuario validado
     **/
    public void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Si el email se envio correctamente cierra sessión
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_form_cliente:

                scrollViewRegister.fullScroll(ScrollView.FOCUS_UP);

                closeFragment(_MAIN_DECODE.getFragmentTag());
                _MAIN_DECODE.setFragmentTag(Constants.FRAGMENT_LOGIN_REGISTER);
                getIntent().putExtra(Constants.KEY_MAIN_DECODE, _MAIN_DECODE); /**Para que se actualice en los fragmentos**/
                openFragment(_MAIN_DECODE.getFragmentTag());

                /**Boton seleccionado**/
                btnFormCliente.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btnFormCliente.setTextColor(getResources().getColor(R.color.colorIcons));

                /**Boton deseleccionado**/
                btnFormTransportista.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormTransportista.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                btnFormChofer.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormChofer.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                break;
            case R.id.btn_form_transportista:

                scrollViewRegister.fullScroll(ScrollView.FOCUS_UP);

                closeFragment(_MAIN_DECODE.getFragmentTag());
                _MAIN_DECODE.setFragmentTag(Constants.FRAGMENT_LOGIN_TRANSPORTISTAS_REGISTER); /**Para que se actualice en los fragmentos**/
                getIntent().putExtra(Constants.KEY_MAIN_DECODE, _MAIN_DECODE);
                openFragment(_MAIN_DECODE.getFragmentTag());

                /**Boton deseleccionado**/
                btnFormCliente.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormCliente.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                btnFormChofer.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormChofer.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                /**Boton seleccionado**/
                btnFormTransportista.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btnFormTransportista.setTextColor(getResources().getColor(R.color.colorIcons));
                break;
            case R.id.btn_form_chofer:
                scrollViewRegister.fullScroll(ScrollView.FOCUS_UP);

                closeFragment(_MAIN_DECODE.getFragmentTag());
                _MAIN_DECODE.setFragmentTag(Constants.FRAGMENT_LOGIN_CHOFERES_REGISTER); /**Para que se actualice en los fragmentos**/
                getIntent().putExtra(Constants.KEY_MAIN_DECODE, _MAIN_DECODE);
                openFragment(_MAIN_DECODE.getFragmentTag());

                /**Boton seleccionado**/
                btnFormChofer.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btnFormChofer.setTextColor(getResources().getColor(R.color.colorIcons));

                /**Boton deseleccionado**/
                btnFormTransportista.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormTransportista.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                btnFormCliente.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormCliente.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }

    }
}
