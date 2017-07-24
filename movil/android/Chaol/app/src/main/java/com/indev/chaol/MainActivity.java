package com.indev.chaol;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.Services.FileServices;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.ErrorMessages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static Boolean navigationActive = false;

    private Switch recordar;
    private TextInputEditText txtUsername, txtPassword, txtEmail;
    private Button btnLogin, btnRegister, btnForgotPassword, btnBack, btnSendEmail, btnCondiciones;
    private LinearLayout formForgot, formLogin;

    private ProgressDialog pDialog;
    /*** Declaraciones para Firebase**/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences prefs;
    private SharedPreferences prefsSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(Constants.KEY_PREF_FIREBASE, Context.MODE_PRIVATE);
        prefsSession = getSharedPreferences(Constants.KEY_PREF_SESSION, Context.MODE_PRIVATE);

        /**Se inicializan los compotentes a utilizar**/
        formForgot = (LinearLayout) findViewById(R.id.form_forgot);
        formLogin = (LinearLayout) findViewById(R.id.form_login);

        recordar = (Switch) findViewById(R.id.switch_recordar);
        txtUsername = (TextInputEditText) findViewById(R.id.txt_login_username);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_login_password);
        txtEmail = (TextInputEditText) findViewById(R.id.txt_login_email);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnForgotPassword = (Button) findViewById(R.id.btn_forgot_password);
        btnSendEmail = (Button) findViewById(R.id.btn_send_email);
        btnBack = (Button) findViewById(R.id.btn_back_login);
        btnCondiciones = (Button) findViewById(R.id.btn_condiciones);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

        /**Responde a los cambios de estato en la session**/
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Si el email se envio correctamente cierra sessión
                                    Toast.makeText(getApplicationContext(),
                                            "Correo de activación enviado...", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    // User is signed in
                    checkIfEmailVerified();
                }
            }
        };

        /**se cargan estilos**/
        this.onPreRender();

        /**Se asignar "this" para direccionar al evento onClick de View.OnClickListener**/
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnSendEmail.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        recordar.setOnClickListener(this);
        btnCondiciones.setOnClickListener(this);

        checkAndRequestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseAuth.getInstance().signOut();

        Boolean session = prefsSession.getBoolean(Constants.KEY_MAIN_SESSION,false);
        String correo = prefs.getString(Constants.KEY_MAIN_EMAIL, "");
        String password = prefs.getString(Constants.KEY_MAIN_PASSWORD, "");
        Boolean recordarPassword = prefs.getBoolean(Constants.KEY_MAIN_RECORDAR, false);

        txtUsername.setText(correo);
        txtPassword.setText(password);
        recordar.setChecked(recordarPassword);

        if (session) {
            txtUsername.setText(prefsSession.getString(Constants.KEY_MAIN_EMAIL, ""));
            txtPassword.setText(prefsSession.getString(Constants.KEY_MAIN_PASSWORD, ""));
            this.validationLogin();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) mAuth.removeAuthStateListener(mAuthListener);
    }

    /**
     * Metodo implementado del View.OnClickListener
     **/
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_login:
                if (checkAndRequestPermissions()) this.validationLogin();
                break;
            case R.id.btn_register:
                if (checkAndRequestPermissions()) this.openRegister();
                break;
            case R.id.btn_forgot_password:
            case R.id.btn_back_login:
                if (checkAndRequestPermissions()) this.showLoginActions();
                break;
            case R.id.btn_send_email:
                this.validationEmail();
                break;
            case R.id.switch_recordar:
                this.recordarPassword();
                break;
            case R.id.btn_condiciones:
                this.showCondiciones();
                break;
            default:
                break;
        }
    }

    private void showCondiciones() {
        /*
        File file = new File("https://firebasestorage.googleapis.com/v0/b/chaol-75d99.appspot.com/o/Documentos%2Fterminos_y_condiciones.pdf?alt=media&token=878e3a1e-0308-47c6-b47d-57112d29831d");
        Uri uriFile = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uriFile, "application/pdf");
        startActivity(intent);
        */

        /**
         * Opcion # 1
         * Se abre el navegador y se descarga el archivo
         * */
        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        */

        /**
         * Opcion # 2
         * Se descarga local y se abre el pdf
         */
        AsyncCallWS document = new AsyncCallWS();
        document.execute();
    }

    private void recordarPassword() {
        Boolean check = recordar.isChecked();

        if (!check) {

            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            cleanLoginForm();
        }
    }

    /**
     * Valida los elementos del login
     **/
    public void validationLogin() {

        Boolean authorized = true;

        final String email = txtUsername.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            txtUsername.setError("El campo es obligatorio", null);
            //txtEmail.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("El campo es obligatorio", null);
            //txtPassword.requestFocus();
            authorized = false;
        }

        if (authorized) {

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.default_loading_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                pDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        ErrorMessages.showErrorMessage(task.getException()),
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valida los elementos para recuperar contrseña
     **/
    public void validationEmail() {

        Boolean authorized = true;

        String email = txtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("El campo es obligatorio", null);
            txtEmail.requestFocus();
            authorized = false;
        }

        if (authorized) {

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.default_loading_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Enviamos un correo de restauración para recuperar su contraseña",
                                        Toast.LENGTH_SHORT).show();
                                cleanLoginForm();
                                showLoginActions();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        ErrorMessages.showErrorMessage(task.getException()),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valida que el email este verificado
     **/
    private void checkIfEmailVerified() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user.isEmailVerified()) {
            //Obtiene el usuario del firebase database

            if (pDialog == null) {
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage(getString(R.string.default_loading_msg));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            getLoginUser();

        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            pDialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Es necesario activar su cuenta", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Obtiene los datos oficiales del usuario ya registrado y validado
     **/
    private void getLoginUser() {
        final FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        user.getToken(true);

        DatabaseReference dbUsuario =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_USUARIOS)
                        .child(user.getUid())
                        .child(Constants.FB_KEY_ITEM_TIPO_USUARIO);

        dbUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String tipoUsuario = dataSnapshot.getValue(String.class);

                    Boolean isSession = prefsSession.getBoolean(Constants.KEY_MAIN_SESSION,false);

                    if (!isSession) {
                        SharedPreferences.Editor session = prefsSession.edit();
                        session.putString(Constants.KEY_MAIN_EMAIL, txtUsername.getText().toString());
                        session.putString(Constants.KEY_MAIN_PASSWORD, txtPassword.getText().toString());
                        session.putBoolean(Constants.KEY_MAIN_SESSION, true);
                        session.commit();
                    }

                    if (recordar.isChecked()) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(Constants.KEY_MAIN_EMAIL, txtUsername.getText().toString());
                        editor.putString(Constants.KEY_MAIN_PASSWORD, txtPassword.getText().toString());
                        editor.putBoolean(Constants.KEY_MAIN_RECORDAR, true);
                        editor.commit();
                    } else {
                        cleanLoginForm();
                    }

                    //Ejecuta el intent de navigationDrawer
                    openNavigation(new Usuarios(tipoUsuario, user.getUid(), ""));

                } else {
                    FirebaseAuth.getInstance().signOut();
                }

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Muestra y oculta componentes dependiendo la accion
     **/
    public void showLoginActions() {

        formLogin.setVisibility((formLogin.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        formForgot.setVisibility((formForgot.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
    }

    /**
     * Inicia el NavigationDrawerActivity y sale del login
     **/
    private void openNavigation(Usuarios usuario) {

        if (!navigationActive) {
            navigationActive = true;
            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            intent.putExtra(Constants.KEY_SESSION_USER, usuario);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
        }
    }

    /**
     * Inicia el MainRegisterActivity añadiendole extraParams
     **/
    private void openRegister() {
        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setTituloActividad("Registrar");
        extraParams.setTituloFormulario("Nuevo");
        extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
        extraParams.setFragmentTag(Constants.FRAGMENT_LOGIN_REGISTER);

        Intent intent = new Intent(this, MainRegisterActivity.class);
        intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Asiga el estilo de subrayado al texto del boton
     **/
    private void onPreRender() {
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnCondiciones.setPaintFlags(btnCondiciones.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void cleanLoginForm() {
        txtEmail.setText(null);
        txtPassword.setText(null);
        txtUsername.setText(null);
    }

    private boolean checkAndRequestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int writeStoragePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int callPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }

        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, listPermissionsNeeded.toArray(
                            new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                            showDialogOK("Acciones de permiso de cámara necesarios para esta aplicación",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, getString(R.string.default_enable_permissions), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private String realPath;

        private AsyncCallWS() {
            realPath = "";
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.default_loading_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean validOperation = false;
            try {
                String uriDownload = FileServices.downloadFile(Constants.KEY_URL_PDF);
                realPath = FileServices.getPath(getApplicationContext(), Uri.parse(uriDownload));
                validOperation = (null != realPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                pDialog.dismiss();
                if (aBoolean) {
                    File file = new File(realPath);
                    Uri uriFile = Uri.fromFile(file);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uriFile, "application/pdf");
                    startActivity(intent);
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "Es necesario instalar una aplicación para leer PDF", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
