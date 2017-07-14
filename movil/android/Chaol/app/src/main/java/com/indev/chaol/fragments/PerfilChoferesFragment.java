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
import android.util.Log;
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
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by saurett on 24/02/2017.
 */

public class PerfilChoferesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, Spinner.OnItemSelectedListener {

    private static final String TAG = RegistroChoferesFragment.class.getName();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private Boolean CHANGE_PHOTO = false;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoPathCROP;

    private BootstrapCircleThumbnail bctPerfil;
    private Button btnTitulo, btnCamara;
    private EditText txtNombre, txtNumeroLicencia, txtNSS, txtCURP, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular1, txtCelular2, txtCorreoElectronico, txtPassword;
    private Spinner spinnerEmpresa;
    private LinearLayout linearLayoutPassword;
    private FloatingActionButton fabChoferes, fabPerfil;
    private ProgressDialog pDialog;

    private static NavigationDrawerActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Choferes _choferActual;

    private static List<String> transportistasList;
    private List<Transportistas> transportistas;

    private FirebaseDatabase database;
    private DatabaseReference drTransportistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_choferes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();

        bctPerfil = (BootstrapCircleThumbnail) view.findViewById(R.id.bct_chofer_perfil);
        btnCamara = (Button) view.findViewById(R.id.btn_camara_registro_choferes);
        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_choferes);
        txtNombre = (EditText) view.findViewById(R.id.txt_choferes_nombre);
        txtNumeroLicencia = (EditText) view.findViewById(R.id.txt_choferes_licencia);
        txtNSS = (EditText) view.findViewById(R.id.txt_choferes_nss);
        txtCURP = (EditText) view.findViewById(R.id.txt_choferes_curp);
        txtEstado = (EditText) view.findViewById(R.id.txt_choferes_estado);
        txtCiudad = (EditText) view.findViewById(R.id.txt_choferes_ciudad);
        txtColonia = (EditText) view.findViewById(R.id.txt_choferes_colonia);
        txtCodigoPostal = (EditText) view.findViewById(R.id.txt_choferes_codigo_postal);
        txtCalle = (EditText) view.findViewById(R.id.txt_choferes_calle);
        txtNumInt = (EditText) view.findViewById(R.id.txt_choferes_num_int);
        txtNumExt = (EditText) view.findViewById(R.id.txt_choferes_num_ext);
        txtTelefono = (EditText) view.findViewById(R.id.txt_choferes_telefono);
        txtCelular1 = (EditText) view.findViewById(R.id.txt_choferes_celular_opc1);
        txtCelular2 = (EditText) view.findViewById(R.id.txt_choferes_celular_opc2);
        txtCorreoElectronico = (EditText) view.findViewById(R.id.txt_choferes_correo_electronico);
        txtPassword = (EditText) view.findViewById(R.id.txt_choferes_password);

        linearLayoutPassword = (LinearLayout) view.findViewById(R.id.item_choferes_password);

        spinnerEmpresa = (Spinner) view.findViewById(R.id.spinner_choferes_empresa);

        fabChoferes = (FloatingActionButton) view.findViewById(R.id.fab_choferes);
        fabPerfil = (FloatingActionButton) view.findViewById(R.id.fab_img_chofer_perfil);
        fabChoferes.setOnClickListener(this);
        btnCamara.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        drTransportistas = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        /**Metodo que llama la lista de transportistas**/
        drTransportistas.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                transportistasList = new ArrayList<>();
                transportistas = new ArrayList<>();

                transportistasList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psTransportista : postSnapshot.getChildren()) {

                        Transportistas transportista = psTransportista.getValue(Transportistas.class);

                        if (Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA.equals(transportista.getTipoDeUsuario())) {

                            if (Constants.FB_KEY_ITEM_ESTATUS_ACTIVO.equals(transportista.getEstatus())) {

                                String nombre = transportista.getNombre();
                                String firebaseID = transportista.getFirebaseId();

                                transportistasList.add(nombre);
                                transportistas.add(new Transportistas(firebaseID, nombre));

                            }
                        }
                    }
                }

                onPreRender();

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

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

    /**
     * Asigna los valores de los transportista a su combo
     **/
    private void onCargarSpinnerTransportistas() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, transportistasList);

        int itemSelection = onPreRenderSelectTransportista();

        spinnerEmpresa.setAdapter(adapter);
        spinnerEmpresa.setSelection(itemSelection);
    }

    private int onPreRenderSelectTransportista() {
        int item = 0;

        for (Transportistas transportista : transportistas) {
            item++;
            if (transportista.getFirebaseId()
                    .equals(_choferActual.getFirebaseIdDelTransportista())) {
                break;
            }
        }

        return item;
    }

    public void onPreRender() {

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

        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CHOFERES).child(user.getUid());

        dbChofer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Choferes chofer = dataSnapshot.getValue(Choferes.class);
                /**Se asigna el chofer actual a la memoria**/
                _choferActual = chofer;

                fabPerfil.setVisibility(View.GONE);
                bctPerfil.setVisibility(View.GONE);

                if (null == chofer.getImagenURL()) chofer.setImagenURL("");

                if (!chofer.getImagenURL().isEmpty()) {

                    final ProgressDialog pdThumbnail = new ProgressDialog(getContext());
                    pdThumbnail.setMessage(getString(R.string.default_loading_msg));
                    pdThumbnail.setIndeterminate(false);
                    pdThumbnail.setCancelable(false);
                    pdThumbnail.show();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(chofer.getImagenURL());

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
                            Log.i(TAG, "addOnSuccessListener : " + exception.getMessage());
                        }
                    });
                } else {
                    fabPerfil.setVisibility(View.VISIBLE);
                }

                txtNombre.setText(chofer.getNombre());
                /**Asigna valores del item seleccionado**/
                onCargarSpinnerTransportistas();
                txtNumeroLicencia.setText(chofer.getNumeroDeLicencia());
                txtNSS.setText(chofer.getNumeroDeSeguroSocial());
                txtCURP.setText(chofer.getCURP());
                txtEstado.setText(chofer.getEstado());
                txtCiudad.setText(chofer.getCiudad());
                txtColonia.setText(chofer.getColonia());
                txtCodigoPostal.setText(chofer.getCodigoPostal());
                txtCalle.setText(chofer.getCalle());
                txtNumInt.setText(chofer.getNumeroInterior());
                txtNumExt.setText(chofer.getNumeroExterior());
                txtTelefono.setText(chofer.getTelefono());
                txtCelular1.setText(chofer.getCelular1());
                txtCelular2.setText(chofer.getCelular2());
                txtCorreoElectronico.setText(chofer.getCorreoElectronico());

                txtCorreoElectronico.setTag(txtCorreoElectronico.getKeyListener());
                txtCorreoElectronico.setKeyListener(null);

                spinnerEmpresa.setEnabled(false);

                linearLayoutPassword.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabChoferes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_choferes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                    this.validationRegister();
                }
                break;
            case R.id.btn_camara_registro_choferes:
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
            this.updateUserChofer();
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

    private void updateUserChofer() {

        Choferes chofer = new Choferes();
        Bitmap bitmap = null;

        chofer.setNombre(txtNombre.getText().toString().trim());
        //chofer.setEmpresaTransportista(spinnerEmpresa.getSelectedItem().toString().trim());
        chofer.setNumeroDeLicencia(txtNumeroLicencia.getText().toString().trim());
        chofer.setNumeroDeSeguroSocial(txtNSS.getText().toString().trim());
        chofer.setCURP(txtCURP.getText().toString().trim());
        chofer.setEstado(txtEstado.getText().toString().trim());
        chofer.setCiudad(txtCiudad.getText().toString().trim());
        chofer.setColonia(txtColonia.getText().toString().trim());
        chofer.setCodigoPostal(txtCodigoPostal.getText().toString().trim());
        chofer.setCalle(txtCalle.getText().toString().trim());
        chofer.setNumeroInterior(txtNumInt.getText().toString().trim());
        chofer.setNumeroExterior(txtNumExt.getText().toString().trim());
        chofer.setTelefono(txtTelefono.getText().toString().trim());
        chofer.setCelular1(txtCelular1.getText().toString().trim());
        chofer.setCelular2(txtCelular2.getText().toString().trim());
        chofer.setCorreoElectronico(txtCorreoElectronico.getText().toString().trim());
        chofer.setContraseña(txtPassword.getText().toString().trim());

        chofer.setFechaDeCreacion(_choferActual.getFechaDeCreacion());
        chofer.setFirebaseIdDelTransportista(_choferActual.getFirebaseIdDelTransportista());
        chofer.setEstatus(_choferActual.getEstatus());
        chofer.setImagenURL(_choferActual.getImagenURL());

        if (CHANGE_PHOTO) {
            bctPerfil.setDrawingCacheEnabled(true);
            bctPerfil.buildDrawingCache();
            bitmap = bctPerfil.getDrawingCache();
        }

        /**metodo principal para actualizar usuario**/
        activityInterface.updateUserChofer(chofer, bitmap);
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
