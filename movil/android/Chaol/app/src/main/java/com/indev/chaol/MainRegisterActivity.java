package com.indev.chaol;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.indev.chaol.fragments.interfaces.MainRegisterInterface;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
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
    private static Usuarios _SESSION_USER;

    private ProgressDialog pDialog;

    private static DecodeItem _decodeItem;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth secondaryAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth.AuthStateListener sAuthListener;

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
        _SESSION_USER = (Usuarios) getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnFormCliente.setOnClickListener(this);
        btnFormTransportista.setOnClickListener(this);
        btnFormChofer.setOnClickListener(this);

        setTitle(_MAIN_DECODE.getTituloActividad());

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

        /**Crea segunda instancia de la app**/
        FirebaseOptions fo = FirebaseOptions.fromResource(getApplicationContext());

        FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(),
                fo, String.valueOf(DateTimeUtils.getTimeStamp()));

        secondaryAuth = FirebaseAuth.getInstance(myApp);

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
        mainFragment.replace(R.id.fragment_main_register_container, Constants.TAG_FRAGMENT.get(tag), tag);
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
        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setTituloActividad(getString(Constants.TITLE_ACTIVITY.get(this.getDecodeItem().getIdView())));
        extraParams.setTituloFormulario(getString(Constants.TITLE_FORM_ACTION.get(action)));
        extraParams.setAccionFragmento(action);
        extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(this.getDecodeItem().getIdView()));
        extraParams.setDecodeItem(this.getDecodeItem());

        Intent intent = new Intent(this, externalActivity);
        intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
        intent.putExtra(Constants.KEY_SESSION_USER, _SESSION_USER);
        startActivity(intent);
    }

    @Override
    public void setDecodeItem(DecodeItem decodeItem) {
        _decodeItem = decodeItem;
    }

    @Override
    public DecodeItem getDecodeItem() {
        return _decodeItem;
    }

    @Override
    public void createUserCliente(final Clientes cliente) {

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        secondaryAuth.createUserWithEmailAndPassword(cliente.getCorreoElectronico(), cliente.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseRegistroCliente(cliente);
                        }
                    }
                });
    }

    /**
     * Registra en firebase al cliente y lo agrega como usuario
     **/
    public void firebaseRegistroCliente(final Clientes cliente) {
        FirebaseUser user = secondaryAuth.getCurrentUser();

        /**obtiene la instancia como cliente**/
        final DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES);

        cliente.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE);
        cliente.setFirebaseId(user.getUid());
        cliente.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_INACTIVO);
        cliente.setPassword(null);
        cliente.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
        cliente.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbCliente.child(user.getUid()).child(Constants.FB_KEY_ITEM_CLIENTE).setValue(cliente, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                pDialog.dismiss();

                if (databaseError == null) {
                    /**obtiene la instancia como usuario**/
                    DatabaseReference dbUsuario =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_USUARIOS);
                    dbUsuario.child(cliente.getFirebaseId()).child(Constants.FB_KEY_ITEM_TIPO_USUARIO).setValue(cliente.getTipoDeUsuario(), new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {
                                sendEmailVerification();
                            }
                        }
                    });
                }
            }
        });


        Log.i(TAG, "firebaseRegistroCliente: Registrado correctamente" + user.getUid());
    }

    @Override
    public void createUserTransportista(final Transportistas transportista) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        secondaryAuth.createUserWithEmailAndPassword(transportista.getCorreoElectronico(), transportista.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseRegistroTransportista(transportista);
                        }
                    }
                });
    }

    /**
     * Registra en firebase al transportista y lo agrega como usuario
     **/
    private void firebaseRegistroTransportista(final Transportistas transportista) {
        FirebaseUser user = secondaryAuth.getCurrentUser();

        /**obtiene la instancia como transportista**/
        final DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        transportista.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA);
        transportista.setFirebaseId(user.getUid());
        transportista.setContraseña(null);
        transportista.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_INACTIVO);
        transportista.setFechaDeCreacion(DateTimeUtils.getTimeStamp());

        dbTransportista.child(user.getUid()).child(Constants.FB_KEY_ITEM_TRANSPORTISTA).setValue(transportista, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    /**obtiene la instancia como usuario**/
                    DatabaseReference dbUsuario =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_USUARIOS);

                    dbUsuario.child(transportista.getFirebaseId()).child(Constants.FB_KEY_ITEM_TIPO_USUARIO).setValue(transportista.getTipoDeUsuario(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {

                                /**obtiene la instancia como listaDeTransportistas**/
                                DatabaseReference dbListaTransportista =
                                        FirebaseDatabase.getInstance().getReference()
                                                .child(Constants.FB_KEY_MAIN_LISTA_TRANSPORTISTAS);

                                dbListaTransportista.child(transportista.getFirebaseId()).setValue(transportista.getNombre(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        if (databaseError == null) {
                                            sendEmailVerification();
                                        }
                                    }
                                });
                            } else {
                                pDialog.dismiss();
                            }
                        }
                    });
                } else {
                    pDialog.dismiss();
                }

            }
        });

        Log.i(TAG, "firebaseRegistroTransportista: Registrado correctamente" + user.getUid());
    }


    @Override
    public void createUserChofer(final Choferes chofer) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        secondaryAuth.createUserWithEmailAndPassword(chofer.getCorreoElectronico(), chofer.getContraseña())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseRegistroChoferes(chofer);
                        }
                    }
                });
    }

    /**
     * Registra en firebase al transportista y lo agrega como usuario
     **/
    private void firebaseRegistroChoferes(final Choferes chofer) {
        FirebaseUser user = secondaryAuth.getCurrentUser();

        /**obtiene la instancia como transportista**/
        final DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        /**obtiene la instancia como chofer**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CHOFERES);

        chofer.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER);
        chofer.setFirebaseId(user.getUid());
        chofer.setContraseña(null);
        chofer.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_INACTIVO);
        chofer.setFechaDeCreacion(DateTimeUtils.getTimeStamp());

        dbChofer.child(chofer.getFirebaseId()).setValue(chofer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    /**obtiene la instancia como usuario**/
                    DatabaseReference dbUsuario =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_USUARIOS);

                    dbUsuario.child(chofer.getFirebaseId()).child(Constants.FB_KEY_ITEM_TIPO_USUARIO).setValue(chofer.getTipoDeUsuario(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {

                                dbTransportista.child(chofer.getFirebaseIdDelTransportista())
                                        .child(Constants.FB_KEY_MAIN_CHOFERES).child(chofer.getFirebaseId()).setValue(chofer, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        if (databaseError == null) {
                                            sendEmailVerification();
                                        }
                                    }
                                });

                            } else {
                                pDialog.dismiss();
                            }
                        }
                    });
                } else {
                    pDialog.dismiss();
                }
            }
        });
    }

    /**
     * Envia correo de verificación para ser un usuario validado
     **/
    public void sendEmailVerification() {
        FirebaseUser user = secondaryAuth.getCurrentUser();

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pDialog.dismiss();
                if (task.isSuccessful()) {
                    //Si el email se envio correctamente cierra sessión
                    //FirebaseAuth.getInstance().signOut();
                    secondaryAuth.signOut();
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Registrado correctamente...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void reautheticate() {
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential("chaolapp@gmail.com", "transportes");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                });
                */

        /*
        mAuth.signInWithEmailAndPassword("chaolapp@gmail.com", "transportes")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    ErrorMessages.showErrorMessage(task.getException()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
        //this.inputPassword();
    }

    private void inputPassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainRegisterActivity.this);
        alertDialog.setTitle("CONTRASEÑA");
        alertDialog.setMessage("Escribe tu contraseña");

        final EditText input = new EditText(MainRegisterActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.common_google_signin_btn_icon_dark_normal);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        alertDialog.show();
    }

    @Override
    public void createTractores(final Tractores tractor) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        firebaseRegistroTractores(tractor);
    }

    private void firebaseRegistroTractores(final Tractores tractor) {
        /**obtiene la instancia como transportista**/
        final DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(tractor.getFirebaseIdDelTransportista());

        String remolqueKey = dbTransportista.child(Constants.FB_KEY_MAIN_TRACTORES).push().getKey();

        tractor.setFirebaseId(remolqueKey);
        tractor.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO);
        tractor.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
        tractor.setFirebaseIdDelTransportista(null);

        dbTransportista.child(Constants.FB_KEY_MAIN_TRACTORES).child(tractor.getFirebaseId())
                .setValue(tractor, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        pDialog.dismiss();
                        if (databaseError == null) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void createRemolques(Remolques remolque) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        firebaseRegistroRemolque(remolque);
    }

    private void firebaseRegistroRemolque(Remolques remolque) {
        /**obtiene la instancia como transportista**/
        final DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(remolque.getFirebaseIdDelTransportista());

        String remolqueKey = dbTransportista.child(Constants.FB_KEY_MAIN_REMOLQUES).push().getKey();

        remolque.setFirebaseId(remolqueKey);
        remolque.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO);
        remolque.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
        remolque.setFirebaseIdDelTransportista(null);

        dbTransportista.child(Constants.FB_KEY_MAIN_REMOLQUES).child(remolque.getFirebaseId())
                .setValue(remolque, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        pDialog.dismiss();
                        if (databaseError == null) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void createBodegas(Bodegas bodega) {

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        firebaseRegistroBodega(bodega);
    }

    private void firebaseRegistroBodega(Bodegas bodega) {
        /**obtiene la instancia como transportista**/
        final DatabaseReference dbClientes =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES)
                        .child(bodega.getFirebaseIdDelCliente());

        String bodegasKey = dbClientes.child(Constants.FB_KEY_MAIN_BODEGAS).push().getKey();

        bodega.setFirebaseIdBodega(bodegasKey);
        bodega.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO);
        bodega.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
        bodega.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbClientes.child(Constants.FB_KEY_MAIN_BODEGAS).child(bodega.getFirebaseIdBodega())
                .setValue(bodega, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        pDialog.dismiss();
                        if (databaseError == null) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void updateBodega(Bodegas bodega) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateBodega(bodega);
    }

    @Override
    public void updateCliente(Clientes cliente) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateCliente(cliente);
    }

    private void firebaseUpdateCliente(Clientes cliente) {
        String firebaseID = cliente.getFirebaseId();

        /**obtiene la instancia como cliente**/
        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES);

        cliente.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE);
        cliente.setFirebaseId(firebaseID);
        cliente.setEstatus(cliente.getEstatus());
        cliente.setPassword(null);
        cliente.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbCliente.child(cliente.getFirebaseId()).child(Constants.FB_KEY_ITEM_CLIENTE).setValue(cliente, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseUpdateCliente: Actualizado correctamente" + firebaseID);
    }

    @Override
    public void updateTransportista(Transportistas transportista) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateTransportista(transportista);
    }

    private void firebaseUpdateTransportista(final Transportistas transportista) {
        String firebaseID = transportista.getFirebaseId();

        /**obtiene la instancia como transportista**/
        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        transportista.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA);
        transportista.setFirebaseId(firebaseID);
        transportista.setContraseña(null);
        transportista.setEstatus(transportista.getEstatus());
        transportista.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbTransportista.child(transportista.getFirebaseId()).child(Constants.FB_KEY_ITEM_TRANSPORTISTA).setValue(transportista, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                pDialog = new ProgressDialog(MainRegisterActivity.this);
                pDialog.setMessage(getString(R.string.default_loading_msg));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                if (databaseError == null) {

                    /**obtiene la instancia como listaDeTransportistas**/
                    DatabaseReference dbListaTransportista =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_LISTA_TRANSPORTISTAS);

                    dbListaTransportista.child(transportista.getFirebaseId()).setValue(transportista.getNombre(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pDialog.dismiss();

                            if (databaseError == null) {
                                finish();
                                Toast.makeText(getApplicationContext(),
                                        "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        Log.i(TAG, "firebaseUpdateTransportista: Actualizado correctamente" + firebaseID);
    }


    @Override
    public void updateChofer(Choferes chofer) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateChofer(chofer);
    }

    private void firebaseUpdateChofer(final Choferes chofer) {

        String firebaseID = chofer.getFirebaseId();

        /**obtiene la instancia como chofer**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CHOFERES);

        chofer.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER);
        chofer.setFirebaseId(firebaseID);
        chofer.setContraseña(null);
        chofer.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbChofer.child(chofer.getFirebaseId()).setValue(chofer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {

                    pDialog = new ProgressDialog(MainRegisterActivity.this);
                    pDialog.setMessage(getString(R.string.default_loading_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    /**obtiene la instancia como transportista**/
                    DatabaseReference dbTransportista =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

                    dbTransportista.child(chofer.getFirebaseIdDelTransportista())
                            .child(Constants.FB_KEY_ITEM_CHOFER).child(chofer.getFirebaseId()).setValue(chofer, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pDialog.dismiss();

                            if (databaseError == null) {
                                finish();
                                Toast.makeText(getApplicationContext(),
                                        "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Log.i(TAG, "firebaseUpdateChofer: Actualizado correctamente" + firebaseID);

    }

    private void firebaseUpdateBodega(final Bodegas bodega) {

        final FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como chofer**/
        DatabaseReference dbBodega =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES)
                        .child(bodega.getFirebaseIdDelCliente())
                        .child(Constants.FB_KEY_MAIN_BODEGAS);

        bodega.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbBodega.child(bodega.getFirebaseIdBodega()).setValue(bodega, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseRegistroChoferes: Actualizado correctamente" + user.getUid());

    }

    @Override
    public void updateTractores(Tractores tractor) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateTractor(tractor);
    }

    private void firebaseUpdateTractor(final Tractores tractor) {

        final DatabaseReference dbTractor =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(tractor.getFirebaseIdDelTransportista())
                        .child(Constants.FB_KEY_MAIN_TRACTORES)
                        .child(tractor.getFirebaseId());

        tractor.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbTractor.setValue(tractor, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseUpdateRemolque: Actualizado correctamente" + tractor.getFirebaseId());

    }

    @Override
    public void updateRemolques(Remolques remolque) {
        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateRemolque(remolque);
    }

    private void firebaseUpdateRemolque(final Remolques remolque) {

        final DatabaseReference dbRemolque =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(remolque.getFirebaseIdDelTransportista())
                        .child(Constants.FB_KEY_MAIN_REMOLQUES)
                        .child(remolque.getFirebaseId());

        remolque.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbRemolque.setValue(remolque, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseUpdateRemolque: Actualizado correctamente" + remolque.getFirebaseId());

    }


    @Override
    public void createSolicitudCotizacion(final Fletes flete, final Bodegas _bodegaCargaActual, final Bodegas _bodegaDescargaActual) {

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final DatabaseReference dbFlete =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_FLETE_ID);

        dbFlete.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    Integer fleteId = dataSnapshot.getValue(Integer.class);

                    /**obtiene la instancia como transportista**/
                    DatabaseReference dbFletes =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR);

                    String fletesKey = dbFletes.child(Constants.FB_KEY_MAIN_FLETE).push().getKey();

                    flete.setIdFlete(String.valueOf(fleteId));
                    flete.setFirebaseId(fletesKey);
                    flete.setEstatus(Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR);
                    flete.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
                    flete.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

                    dbFletes.child(flete.getFirebaseId()).child(Constants.FB_KEY_MAIN_FLETE)
                            .setValue(flete, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    pDialog.dismiss();
                                    if (databaseError == null) {

                                        createBodegaCarga(flete.getFirebaseId(), _bodegaCargaActual, _bodegaDescargaActual);
                                    }
                                }
                            });

                } else {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Se ha presentado un error...", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void createBodegaCarga(final String firebaseID, final Bodegas _bodegaCargaActual, final Bodegas _bodegaDescargaActual) {

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        /**obtiene la instancia como transportista**/
        DatabaseReference dbFletes =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                        .child(firebaseID);

        _bodegaCargaActual.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
        _bodegaCargaActual.setFechaDeEdicion(DateTimeUtils.getTimeStamp());
        _bodegaCargaActual.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO);

        dbFletes.child(Constants.FB_KEY_MAIN_BODEGA_DE_CARGA)
                .setValue(_bodegaCargaActual, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        pDialog.dismiss();
                        if (databaseError == null) {
                            createBodegaDescarga(firebaseID, _bodegaDescargaActual);
                        }
                    }
                });
    }

    private void createBodegaDescarga(String firebaseID, Bodegas _bodegaDescargaActual) {

        pDialog = new ProgressDialog(MainRegisterActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        /**obtiene la instancia como transportista**/
        DatabaseReference dbFletes =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                        .child(firebaseID);

        _bodegaDescargaActual.setFechaDeCreacion(DateTimeUtils.getTimeStamp());
        _bodegaDescargaActual.setFechaDeEdicion(DateTimeUtils.getTimeStamp());
        _bodegaDescargaActual.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO);

        dbFletes.child(Constants.FB_KEY_MAIN_BODEGA_DE_DESCARGA)
                .setValue(_bodegaDescargaActual, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        pDialog.dismiss();
                        if (databaseError == null) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Registrado correctamente...", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Log.i(TAG, "createBodegaDescarga: Creado correctamente" + firebaseID);
    }

    @Override
    public void updateSolicitudCotizacion(Fletes flete) {

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
