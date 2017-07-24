package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.Services.FileServices;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.MetodosPagos;
import com.indev.chaol.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroLoginClientesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private Boolean CHANGE_PHOTO = false;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoPathCROP;

    private BootstrapCircleThumbnail bctPerfil;
    private Button btnTitulo, btnCamara;
    private EditText txtNombre, txtRFC, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular, txtEmail, txtPassword;
    private Spinner spinnerMetodoPago;
    private FloatingActionButton fabClientes, fabPerfil;
    private ProgressDialog pDialog;

    private static List<String> metodosPagoList;
    private List<MetodosPagos> metodosPagos;

    private static MainRegisterActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_clientes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);
        btnCamara = (Button) view.findViewById(R.id.btn_camara_registro_clientes);

        bctPerfil = (BootstrapCircleThumbnail) view.findViewById(R.id.bct_cliente_perfil);

        txtNombre = (EditText) view.findViewById(R.id.txt_clientes_nombre);
        txtRFC = (EditText) view.findViewById(R.id.txt_clientes_rfc);
        txtEstado = (EditText) view.findViewById(R.id.txt_clientes_estado);
        txtCiudad = (EditText) view.findViewById(R.id.txt_clientes_ciudad);
        txtColonia = (EditText) view.findViewById(R.id.txt_clientes_colonia);
        txtCodigoPostal = (EditText) view.findViewById(R.id.txt_clientes_codigo_postal);
        txtCalle = (EditText) view.findViewById(R.id.txt_clientes_calle);
        txtNumInt = (EditText) view.findViewById(R.id.txt_clientes_num_int);
        txtNumExt = (EditText) view.findViewById(R.id.txt_clientes_num_ext);
        txtTelefono = (EditText) view.findViewById(R.id.txt_clientes_telefono);
        txtCelular = (EditText) view.findViewById(R.id.txt_clientes_celular);
        txtEmail = (EditText) view.findViewById(R.id.txt_clientes_email);
        txtPassword = (EditText) view.findViewById(R.id.txt_clientes_password);

        spinnerMetodoPago = (Spinner) view.findViewById(R.id.spinner_clientes_metodo_pago);

        fabClientes = (FloatingActionButton) view.findViewById(R.id.fab_clientes);
        fabPerfil = (FloatingActionButton) view.findViewById(R.id.fab_img_cliente_perfil);

        btnCamara.setOnClickListener(this);
        fabClientes.setOnClickListener(this);
        spinnerMetodoPago.setOnItemSelectedListener(this);

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
            activityInterface = (MainRegisterActivity) getActivity();
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

        /**Carga los elementos del combo**/
        this.onCargarMetodosPagos();
        this.onCargarSpinnerMetodosPagos();

        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                Clientes clientes = (Clientes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNombre.setText(clientes.getNombre());

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabClientes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
                break;
            case Constants.ACCION_REGISTRAR:
                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabPerfil.setVisibility(View.VISIBLE);
                bctPerfil.setVisibility(View.GONE);
                break;
            default:
                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText("Perfil");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                    this.validationRegister();
                }
                break;
            case R.id.btn_camara_registro_clientes:
                this.dispatchTakePictureIntent();
                break;
        }
    }

    /**
     * Verifica los campos obligatorios para registro de cliente
     **/
    private void validationRegister() {

        Boolean authorized = true;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("El campo es obligatorio", null);
            txtEmail.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("El campo es obligatorio", null);
            txtPassword.requestFocus();
            authorized = false;
        }

        if (spinnerMetodoPago.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerMetodoPago.getSelectedView();
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

        Clientes clientes = new Clientes();
        Bitmap bitmap = null;

        clientes.setNombre(txtNombre.getText().toString().trim());
        clientes.setRFC(txtRFC.getText().toString().trim());
        clientes.setEstado(txtEstado.getText().toString().trim());
        clientes.setCiudad(txtCiudad.getText().toString().trim());
        clientes.setColonia(txtColonia.getText().toString().trim());
        clientes.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        clientes.setCalle(txtCalle.getText().toString().trim());
        clientes.setNumeroInterior(txtNumInt.getText().toString().trim());
        clientes.setNumeroExterior(txtNumExt.getText().toString().trim());
        clientes.setMetodoDePago(spinnerMetodoPago.getSelectedItem().toString());
        clientes.setTelefono(txtTelefono.getText().toString().trim());
        clientes.setCelular(txtCelular.getText().toString().trim());
        clientes.setCorreoElectronico(txtEmail.getText().toString().trim());
        clientes.setPassword(txtPassword.getText().toString().trim());

        if (CHANGE_PHOTO) {
            bctPerfil.setDrawingCacheEnabled(true);
            bctPerfil.buildDrawingCache();
            bitmap = bctPerfil.getDrawingCache();
        }

        /**metodo principal para crear usuario**/
        activityInterface.createUserCliente(clientes, bitmap);
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
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Metodos privados en la clase AsynTask
     **/
    private void onCargarMetodosPagos() {
        metodosPagoList = new ArrayList<>();
        metodosPagos = new ArrayList<>();
        metodosPagoList.add("Seleccione ...");

        metodosPagoList.add("Efectivo");
        metodosPagoList.add("Cheque");
        metodosPagoList.add("Transferencia Electronica");
        metodosPagoList.add("Tarjeta de Crédito");
        metodosPagoList.add("Dinero Electrónico");
        metodosPagoList.add("Tarjeta de Débito");
        metodosPagoList.add("NA");
        metodosPagoList.add("Otros");

        metodosPagos.add(new MetodosPagos(1, "Efectivo"));
        metodosPagos.add(new MetodosPagos(2, "Cheque"));
        metodosPagos.add(new MetodosPagos(3, "Transferencia Electronica"));
        metodosPagos.add(new MetodosPagos(4, "Tarjeta de Crédito"));
        metodosPagos.add(new MetodosPagos(5, "Dinero Electrónico"));
        metodosPagos.add(new MetodosPagos(6, "Tarjeta de Débito"));
        metodosPagos.add(new MetodosPagos(7, "NA"));
        metodosPagos.add(new MetodosPagos(8, "Otros"));
    }

    private void onCargarSpinnerMetodosPagos() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, metodosPagoList);

        spinnerMetodoPago.setAdapter(adapter);
        spinnerMetodoPago.setSelection(0);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = FileServices.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (null != photoFile) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            fabPerfil.setVisibility(View.GONE);
            bctPerfil.setVisibility(View.VISIBLE);

            try {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
                performCrop(contentUri);
            } catch (Exception e) {
                Toast.makeText(getContext(), "No es posible obtener la foto, intente nuevamente...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    CHANGE_PHOTO = FileServices.setPic(bctPerfil, mCurrentPhotoPathCROP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            File photoFile = null;
            try {
                photoFile = FileServices.createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                try {
                    mCurrentPhotoPathCROP = photoFile.getAbsolutePath();
                    File f = new File(mCurrentPhotoPathCROP);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(cropIntent, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Tus dispositivo no soporta esta acción ...", Toast.LENGTH_SHORT).show();
        }
    }
}
