package com.indev.chaol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txtUsername, txtPassword, txtEmail;
    private Button btnLogin, btnRegister, btnForgotPassword, btnBack, btnSendEmail;
    private LinearLayout formForgot, formLogin;
    private ProgressDialog pDialog;

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

        /**se cargan estilos**/
        this.onPreRender();

        /**Se asignar "this" para direccionar al evento onClick de View.OnClickListener**/
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnSendEmail.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        Log.i("Log", "Check create action - MainActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("Log", "Check start action - MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Log", "Check resume action - MainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Log", "Check pause action - MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Log", "Check stop action - MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Log", "Check restart action - MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Log", "Check destroy action - MainActivity");
    }

    /**
     * Metodo implementado del View.OnClickListener
     **/
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_login:
                validationLogin();
                break;
            case R.id.btn_register:
                AsyncCallWS wsRegister = new AsyncCallWS(Constants.WS_KEY_REGISTER_ACTIVITY);
                wsRegister.execute();
                break;
            case R.id.btn_forgot_password:
            case R.id.btn_back_login:
                showLoginActions();
                break;
            default:
                break;
        }
    }

    /**
     * Valida los elementos del login
     **/
    public void validationLogin() {
        AsyncCallWS wsNavigation = new AsyncCallWS(Constants.WS_KEY_NAVIGATION_ACTIVITY);
        wsNavigation.execute();
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
        startActivity(intent);
    }

    /**Inicia el MainRegisterActivity añadiendole extraParams**/
    private void openRegister() {
        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setTituloActividad("Registrar usuario");
        extraParams.setTituloFormulario("Nuevo");
        extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
        extraParams.setFragmentTag(Constants.FRAGMENT_MAIN_REGISTER);

        Intent registerIntent = new Intent(this, MainRegisterActivity.class);
        registerIntent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
        startActivity(registerIntent);
    }

    /**
     * Asiga el estilo de subrayado al texto del boton
     **/
    private void onPreRender() {
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
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
                switch (webServiceOperation) {
                    case Constants.WS_KEY_REGISTER_ACTIVITY:

                        validOperation = true;

                        break;
                    case Constants.WS_KEY_NAVIGATION_ACTIVITY:

                        validOperation = true;
                        break;
                }
            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                pDialog.dismiss();
                if (success) {
                    switch (webServiceOperation) {
                        case Constants.WS_KEY_REGISTER_ACTIVITY:
                            openRegister();
                            break;
                        case Constants.WS_KEY_NAVIGATION_ACTIVITY:
                            openNavigation();
                            break;
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "Error desconocido" : textError);
                    Toast.makeText(getApplicationContext(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
