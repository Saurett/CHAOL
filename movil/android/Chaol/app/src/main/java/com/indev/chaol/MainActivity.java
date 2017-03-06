package com.indev.chaol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txtUsername, txtPassword, txtEmail;
    private Button btnLogin, btnRegister, btnForgotPassword, btnBack, btnSendEmail;
    private LinearLayout formForgot, formLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Se defire la orientación de la pantalla**/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                openRegister();
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
        openNavigation();
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
        try {
            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Tag", e.getMessage() + "PTM");
        }
    }

    /**Inicia el MainRegisterActivity añadiendole extraParams**/
    private void openRegister() {
        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setTituloActividad("Registrar usuario");
        extraParams.setTituloFormulario("Nuevo");
        extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
        extraParams.setFragmentTag(Constants.FRAGMENT_MAIN_REGISTER);

        Intent intent = new Intent(this, MainRegisterActivity.class);
        intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
        startActivity(intent);
    }

    /**
     * Asiga el estilo de subrayado al texto del boton
     **/
    private void onPreRender() {
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
