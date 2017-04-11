package com.indev.chaol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();

    private TextInputEditText txtUsername, txtPassword, txtEmail;
    private Button btnLogin, btnRegister, btnForgotPassword, btnBack, btnSendEmail;
    private LinearLayout formForgot, formLogin;

    private ProgressDialog pDialog;
    /*** Declaraciones para Firebase**/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Se inicializan los compotentes a utilizar**/
        formForgot = (LinearLayout) findViewById(R.id.form_forgot);
        formLogin = (LinearLayout) findViewById(R.id.form_login);

        txtUsername = (TextInputEditText) findViewById(R.id.txt_login_username);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_login_password);
        txtEmail = (TextInputEditText) findViewById(R.id.txt_login_email);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnForgotPassword = (Button) findViewById(R.id.btn_forgot_password);
        btnSendEmail = (Button) findViewById(R.id.btn_send_email);
        btnBack = (Button) findViewById(R.id.btn_back_login);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();
        /**Responde a los cambios de estato en la session**/
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    checkIfEmailVerified();
                    cleanLoginForm();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
                this.validationLogin();
                break;
            case R.id.btn_register:
                this.openRegister();
                break;
            case R.id.btn_forgot_password:
            case R.id.btn_back_login:
                this.showLoginActions();
                break;
            case R.id.btn_send_email:
                this.validationEmail();
                break;
            default:
                break;
        }
    }

    /**
     * Valida los elementos del login
     **/
    public void validationLogin() {

        Boolean authorized = true;

        String email = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

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
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**Valida los elementos para recuperar contrseña**/
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
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(getApplicationContext(), "Enviamos un correo de restauración para recuperar su contraseña",
                                        Toast.LENGTH_SHORT).show();
                                cleanLoginForm();
                                showLoginActions();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**Valida que el email este verificado**/
    private void checkIfEmailVerified() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
            openNavigation();
            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(getApplicationContext(), "Es necesario activar su cuenta", Toast.LENGTH_SHORT).show();
        }
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
    private void openNavigation() {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
    }

    private void cleanLoginForm() {
        txtEmail.setText(null);
        txtPassword.setText(null);
        txtUsername.setText(null);
    }
}
