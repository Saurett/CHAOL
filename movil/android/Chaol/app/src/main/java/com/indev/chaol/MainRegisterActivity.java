package com.indev.chaol;

import android.os.Bundle;
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

import com.indev.chaol.fragments.interfaces.MainRegisterInterface;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.List;

public class MainRegisterActivity extends AppCompatActivity implements MainRegisterInterface, View.OnClickListener {

    private static Button btnFormCliente, btnFormTransportista;
    private static LinearLayout linearLayoutSwitch;
    private static ScrollView scrollViewRegister;
    private static DecodeExtraParams _MAIN_DECODE;

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
        linearLayoutSwitch = (LinearLayout) findViewById(R.id.linear_switch_form);
        scrollViewRegister = (ScrollView) findViewById(R.id.scrollView_register);

        _MAIN_DECODE = (DecodeExtraParams) getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        btnFormCliente.setOnClickListener(this);
        btnFormTransportista.setOnClickListener(this);

        setTitle(_MAIN_DECODE.getTituloActividad());

        this.onPreRender();

        Log.i("Log", "Check create action - MainRegisterActivity");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_form_cliente:

                scrollViewRegister.fullScroll(ScrollView.FOCUS_UP);

                closeFragment(_MAIN_DECODE.getFragmentTag());
                _MAIN_DECODE.setFragmentTag(Constants.FRAGMENT_MAIN_REGISTER);
                openFragment(_MAIN_DECODE.getFragmentTag());

                /**Boton seleccionado**/
                btnFormCliente.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btnFormCliente.setTextColor(getResources().getColor(R.color.colorIcons));

                /**Boton deseleccionado**/
                btnFormTransportista.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormTransportista.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                break;
            case R.id.btn_form_transportista:

                scrollViewRegister.fullScroll(ScrollView.FOCUS_UP);

                closeFragment(_MAIN_DECODE.getFragmentTag());
                _MAIN_DECODE.setFragmentTag(Constants.FRAGMENT_TRANSPORTISTAS_REGISTER);
                openFragment(_MAIN_DECODE.getFragmentTag());

                /**Boton seleccionado**/
                btnFormCliente.setBackgroundColor(getResources().getColor(R.color.colorIcons));
                btnFormCliente.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                /**Boton deseleccionado**/
                btnFormTransportista.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btnFormTransportista.setTextColor(getResources().getColor(R.color.colorIcons));
                break;
        }

    }
}
