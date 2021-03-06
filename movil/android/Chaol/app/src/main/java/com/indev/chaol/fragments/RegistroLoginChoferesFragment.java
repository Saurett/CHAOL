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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
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

public class RegistroLoginChoferesFragment extends Fragment implements View.OnClickListener, AlertDialog.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = RegistroLoginChoferesFragment.class.getName();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private Boolean CHANGE_PHOTO = false;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoPathCROP;

    private BootstrapCircleThumbnail bctPerfil;
    private Button btnTitulo, btnCamara;
    private EditText txtNombre, txtNumeroLicencia, txtNSS, txtCURP, txtEstado, txtCiudad, txtColonia, txtCodigoPostal, txtCalle, txtNumInt, txtNumExt, txtTelefono, txtCelular1, txtCelular2, txtCorreoElectronico, txtPassword;
    private Spinner spinnerEmpresa;
    private FloatingActionButton fabChoferes, fabPerfil;
    private ProgressDialog pDialog;

    private static List<String> transportistasList;
    private List<Transportistas> transportistas;

    private static MainRegisterActivity activityInterface;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    FirebaseDatabase database;
    DatabaseReference drTransportistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_choferes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        bctPerfil = (BootstrapCircleThumbnail) view.findViewById(R.id.bct_chofer_perfil);
        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_choferes);
        btnCamara = (Button) view.findViewById(R.id.btn_camara_registro_choferes);
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

        spinnerEmpresa = (Spinner) view.findViewById(R.id.spinner_choferes_empresa);

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

                onCargarSpinnerTransportistas();
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                pDialog.dismiss();
            }
        });

        fabChoferes = (FloatingActionButton) view.findViewById(R.id.fab_choferes);
        fabPerfil = (FloatingActionButton) view.findViewById(R.id.fab_img_chofer_perfil);

        fabChoferes.setOnClickListener(this);
        spinnerEmpresa.setOnItemSelectedListener(this);
        btnCamara.setOnClickListener(this);

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

        if (tag.equals(Constants.FRAGMENT_LOGIN_CHOFERES_REGISTER)) {
            btnTitulo.setBackgroundColor(getResources().getColor(R.color.colorIcons));
            btnTitulo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                Choferes choferes = (Choferes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                txtNombre.setText(choferes.getNombre());

                /**Modifica valores predeterminados de ciertos elementos**/
                btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
                fabChoferes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
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

    /**
     * Asigna los valores de los transportista a su combo
     **/
    private void onCargarSpinnerTransportistas() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, transportistasList);

                            /*
                            int selectionState = (null != _PROFILE_MANAGER.getAddressProfile().getIdItemState())
                                    ? _PROFILE_MANAGER.getAddressProfile().getIdItemState() : 0;
                                    */

        spinnerEmpresa.setAdapter(adapter);
        spinnerEmpresa.setSelection(0);
    }

    /**
     * Obtiene el firebaseID del transportista seleccionado
     **/
    private String getSelectTransportista() {
        String firebaseID = "";

        for (Transportistas transportista :
                transportistas) {
            if (transportista.getNombre().equals(spinnerEmpresa.getSelectedItem().toString())) {
                firebaseID = transportista.getFirebaseId();
                break;
            }
        }

        return firebaseID;
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

        if (spinnerEmpresa.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerEmpresa.getSelectedView();
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

        Choferes chofer = new Choferes();
        Bitmap bitmap = null;

        chofer.setNombre(txtNombre.getText().toString().trim());
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

        chofer.setFirebaseIdDelTransportista(getSelectTransportista());

        if (CHANGE_PHOTO) {
            bctPerfil.setDrawingCacheEnabled(true);
            bctPerfil.buildDrawingCache();
            bitmap = bctPerfil.getDrawingCache();
        }

        /**metodo principal para crear usuario**/
        activityInterface.createUserChofer(chofer, bitmap);
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

        if (position > 0) {

        }

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
                //CHANGE_PHOTO = FileServices.setPic(bctPerfil, mCurrentPhotoPath);
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
