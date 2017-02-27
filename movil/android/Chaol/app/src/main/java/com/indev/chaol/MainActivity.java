package com.indev.chaol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtUsername, txtPassword;
    private Button btnLogin, btnRegister, btnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Se defire la orientación de la pantalla**/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        /**Se inicializan los compotentes a utilizar**/
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnForgotPassword = (Button) findViewById(R.id.btn_forgot_password);

        this.setStyles();

        /**Se asignar "this" para direccionar al evento onClick de View.OnClickListener**/
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    /**Metodo implementado del View.OnClickListener**/
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
            default:
                break;
        }
    }

    /**Valida los elementos del login**/
    public void validationLogin() {
        openNavigation();
    }

    /**Inicia el NavigationDrawerActivity y sale del login**/
    private void openNavigation() {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
    }

    private void openRegister() {
        Intent intent = new Intent(this, MainRegisterActivity.class);
        startActivity(intent);
    }

    /**Asiga el estilo de subrayado al texto del boton**/
    private void setStyles() {
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
