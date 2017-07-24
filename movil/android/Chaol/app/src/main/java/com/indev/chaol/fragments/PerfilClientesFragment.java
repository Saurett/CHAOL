package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.indev.chaol.NavigationDrawerActivity;
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

public class PerfilClientesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private Boolean CHANGE_PHOTO = false;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoPathCROP;

    private BootstrapCircleThumbnail bctPerfil;
    private Button btnTitulo, btnCamara;
    private EditText txtNombre, txtRFC, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular, txtCorreoElectronico, txtPassword;
    private LinearLayout linearLayoutPassword;
    private Spinner spinnerMetodoPago;
    private FloatingActionButton fabClientes, fabPerfil;
    ;
    private ProgressDialog pDialog;

    private static List<String> metodosPagoList;
    private List<MetodosPagos> metodosPagos;

    private static NavigationDrawerActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Clientes _clienteActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_clientes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

        bctPerfil = (BootstrapCircleThumbnail) view.findViewById(R.id.bct_cliente_perfil);
        btnCamara = (Button) view.findViewById(R.id.btn_camara_registro_clientes);
        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);
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
        txtCorreoElectronico = (EditText) view.findViewById(R.id.txt_clientes_email);
        txtPassword = (EditText) view.findViewById(R.id.txt_clientes_password);

        linearLayoutPassword = (LinearLayout) view.findViewById(R.id.item_clientes_password);

        spinnerMetodoPago = (Spinner) view.findViewById(R.id.spinner_clientes_metodo_pago);

        fabClientes = (FloatingActionButton) view.findViewById(R.id.fab_clientes);
        fabPerfil = (FloatingActionButton) view.findViewById(R.id.fab_img_cliente_perfil);

        btnCamara.setOnClickListener(this);
        fabClientes.setOnClickListener(this);
        spinnerMetodoPago.setOnItemSelectedListener(this);

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

        /**Carga los elementos del combo**/
        this.onCargarMetodosPagos();
        this.onCargarSpinnerMetodosPagos();

        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Carga los valores iniciales al editar**/
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

        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES).child(user.getUid()).child(Constants.FB_KEY_ITEM_CLIENTE);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        dbCliente.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Clientes cliente = dataSnapshot.getValue(Clientes.class);
                /**Se asigna el chofer actual a la memoria**/
                _clienteActual = cliente;

                fabPerfil.setVisibility(View.GONE);
                bctPerfil.setVisibility(View.GONE);

                if (null == cliente.getImagenURL()) cliente.setImagenURL("");

                if (!cliente.getImagenURL().isEmpty()) {

                    final ProgressDialog pdThumbnail = new ProgressDialog(getContext());
                    pdThumbnail.setMessage(getString(R.string.default_loading_msg));
                    pdThumbnail.setIndeterminate(false);
                    pdThumbnail.setCancelable(false);
                    pdThumbnail.show();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(cliente.getImagenURL());

                    bctPerfil.setVisibility(View.VISIBLE);

                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bctPerfil.setImageBitmap(decodedByte);

                            pdThumbnail.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            fabPerfil.setVisibility(View.VISIBLE);
                            bctPerfil.setVisibility(View.GONE);
                            pdThumbnail.dismiss();
                        }
                    });
                } else {
                    fabPerfil.setVisibility(View.VISIBLE);
                }

                txtNombre.setText(cliente.getNombre());
                /**Asigna valores del item seleccionado**/
                onSelectMetodoPago(cliente.getMetodoDePago());
                txtRFC.setText(cliente.getRFC());
                txtEstado.setText(cliente.getEstado());
                txtCiudad.setText(cliente.getCiudad());
                txtColonia.setText(cliente.getColonia());
                txtCodigoPostal.setText(cliente.getCodigoPostal());
                txtCalle.setText(cliente.getCalle());
                txtNumInt.setText(cliente.getNumeroInterior());
                txtNumExt.setText(cliente.getNumeroExterior());
                txtTelefono.setText(cliente.getTelefono());
                txtCelular.setText(cliente.getCelular());
                txtCorreoElectronico.setText(cliente.getCorreoElectronico());

                txtCorreoElectronico.setTag(txtCorreoElectronico.getKeyListener());
                txtCorreoElectronico.setKeyListener(null);

                linearLayoutPassword.setVisibility(View.GONE);

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabClientes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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

    /**
     * Verifica los campos obligatorios para editar
     **/
    private void validationEditer() {

        Boolean authorized = true;

        String email = txtCorreoElectronico.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtCorreoElectronico.setError("El campo es obligatorio", null);
            txtCorreoElectronico.requestFocus();
            authorized = false;
        }

        if (authorized) {
            this.updateUserCliente();
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

    private void updateUserCliente() {

        Clientes cliente = new Clientes();
        Bitmap bitmap = null;

        cliente.setNombre(txtNombre.getText().toString().trim());
        cliente.setRFC(txtRFC.getText().toString().trim());
        cliente.setEstado(txtEstado.getText().toString().trim());
        cliente.setCiudad(txtCiudad.getText().toString().trim());
        cliente.setColonia(txtColonia.getText().toString().trim());
        cliente.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        cliente.setCalle(txtCalle.getText().toString().trim());
        cliente.setNumeroInterior(txtNumInt.getText().toString().trim());
        cliente.setNumeroExterior(txtNumExt.getText().toString().trim());
        cliente.setMetodoDePago(spinnerMetodoPago.getSelectedItem().toString());
        cliente.setTelefono(txtTelefono.getText().toString().trim());
        cliente.setCelular(txtCelular.getText().toString().trim());
        cliente.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        cliente.setPassword(txtPassword.getText().toString().trim());

        cliente.setFechaDeCreacion(_clienteActual.getFechaDeCreacion());
        cliente.setEstatus(_clienteActual.getEstatus());
        cliente.setImagenURL(_clienteActual.getImagenURL());

        if (CHANGE_PHOTO) {
            bctPerfil.setDrawingCacheEnabled(true);
            bctPerfil.buildDrawingCache();
            bitmap = bctPerfil.getDrawingCache();
        }

        /**metodo principal para actualizar usuario**/
        activityInterface.updateUserCliente(cliente, bitmap);
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

    private void onSelectMetodoPago(String miMetodoPago) {
        for (MetodosPagos metodoPago :
                metodosPagos) {
            if (metodoPago.getMetodoPago().equals(miMetodoPago)) {
                spinnerMetodoPago.setSelection(metodoPago.getId());
                break;
            }
        }
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
