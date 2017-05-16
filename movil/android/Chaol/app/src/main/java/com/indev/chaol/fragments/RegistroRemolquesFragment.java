package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.TiposRemolques;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroRemolquesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = RegistroRemolquesFragment.class.getName();

    private Button btnTitulo;
    private EditText txtNumEconomico, txtMarca, txtModelo, txtNumSerie, txtPlaca, txtIdGPS;
    private Spinner spinnerEmpresa, spinnerTipoRemolque;
    private LinearLayout linearLayoutEmpresa;
    private FloatingActionButton fabRemolques;
    private ProgressDialog pDialog;

    private static List<String> tiposRemolquesList;
    private List<TiposRemolques> tiposRemolques;

    private static List<String> transportistasList;
    private List<Transportistas> transportistas;

    private static MainRegisterActivity activityInterface;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    FirebaseDatabase database;
    DatabaseReference drTransportistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_remolques, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_remolques);
        txtNumEconomico = (EditText) view.findViewById(R.id.txt_remolques_num_economico);
        txtMarca = (EditText) view.findViewById(R.id.txt_remolques_marca);
        txtModelo = (EditText) view.findViewById(R.id.txt_remolques_modelo);
        txtNumSerie = (EditText) view.findViewById(R.id.txt_remolques_num_serie);
        txtPlaca = (EditText) view.findViewById(R.id.txt_remolques_placas);
        txtIdGPS = (EditText) view.findViewById(R.id.txt_remolques_id_gps);

        spinnerEmpresa = (Spinner) view.findViewById(R.id.spinner_remolques_empresa);
        spinnerTipoRemolque = (Spinner) view.findViewById(R.id.spinner_remolques_tipo_remolque);

        linearLayoutEmpresa = (LinearLayout) view.findViewById(R.id.linear_spinner_remolques_empresa);

        fabRemolques = (FloatingActionButton) view.findViewById(R.id.fab_remolques);

        fabRemolques.setOnClickListener(this);

        spinnerTipoRemolque.setOnItemSelectedListener(this);
        spinnerEmpresa.setOnItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
        drTransportistas = database.getReference("listaDeTransportistas");

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

                    String nombre = postSnapshot.getValue(String.class);
                    String firebaseID = postSnapshot.getKey();

                    Transportistas transportista = new Transportistas(firebaseID, nombre);

                    transportistasList.add(nombre);
                    transportistas.add(transportista);
                }

                onCargarSpinnerTransportistas();

                if (!_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
                    linearLayoutEmpresa.setVisibility(View.VISIBLE);
                }
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

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

        this.onCargarTiposRemolques();
        this.onCargarSpinnerTiposRemolques();

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
        Remolques remolques = (Remolques) _MAIN_DECODE.getDecodeItem().getItemModel();
        DatabaseReference dbTractor =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(remolques.getFirebaseIdTransportista())
                        .child(Constants.FB_KEY_MAIN_REMOLQUES)
                        .child(remolques.getFirebaseId());

        final ProgressDialog pDialogRender;

        pDialogRender = new ProgressDialog(getContext());
        pDialogRender.setMessage(getString(R.string.default_loading_msg));
        pDialogRender.setIndeterminate(false);
        pDialogRender.setCancelable(false);
        pDialogRender.show();

        dbTractor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Remolques remolque = dataSnapshot.getValue(Remolques.class);

                txtNumEconomico.setText(remolque.getNumeroEconomico());
                txtMarca.setText(remolque.getMarca());
                txtModelo.setText(remolque.getModelo());
                txtNumSerie.setText(remolque.getNumeroDeSerie());
                txtPlaca.setText(remolque.getPlaca());
                txtIdGPS.setText(remolque.getIdGPS());

                onCargarSpinnerTiposRemolques();

                spinnerTipoRemolque.setEnabled(false);

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**Modifica valores predeterminados de ciertos elementos**/
        btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        fabRemolques.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_remolques:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                    this.validationRegister();
                }
                break;
        }
    }

    private void validationRegister() {
        Boolean authorized = true;

        String numEconomico = txtNumEconomico.getText().toString();
        String numSerie = txtNumSerie.getText().toString();

        if (TextUtils.isEmpty(numEconomico)) {
            txtNumEconomico.setError("El campo es obligatorio", null);
            txtNumEconomico.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(numSerie)) {
            txtNumSerie.setError("El campo es obligatorio", null);
            txtNumSerie.requestFocus();
            authorized = false;
        }

        if (!_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
            if (spinnerEmpresa.getSelectedItemId() <= 0L) {
                TextView errorTextSE = (TextView) spinnerEmpresa.getSelectedView();
                errorTextSE.setError("El campo es obligatorio");
                errorTextSE.setTextColor(Color.RED);
                errorTextSE.setText("El campo es obligatorio");//changes t
                errorTextSE.requestFocus();

                authorized = false;
            }
        }

        if (spinnerTipoRemolque.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerTipoRemolque.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (authorized) {
            this.createSimpleRow();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createSimpleRow() {

        Remolques remolque = new Remolques();

        remolque.setNumeroEconomico(txtNumEconomico.getText().toString().trim());
        remolque.setMarca(txtMarca.getText().toString().trim());
        remolque.setModelo(txtModelo.getText().toString().trim());
        remolque.setNumeroDeSerie(txtNumSerie.getText().toString().trim());
        remolque.setPlaca(txtPlaca.getText().toString().trim());
        remolque.setIdGPS(txtIdGPS.getText().toString().trim());
        remolque.setTipoRemolque(spinnerTipoRemolque.getSelectedItem().toString());

        remolque.setFirebaseIdTransportista(getSelectTransportista());

        /**metodo principal para crear usuario**/
        activityInterface.createRemolques(remolque);
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

        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
            for (Transportistas transportista : transportistas) {
                item++;
                if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                    break;
                }
            }
        } else if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            Remolques remolque = (Remolques) _MAIN_DECODE.getDecodeItem().getItemModel();
            for (Transportistas transportista : transportistas) {
                item++;
                if (transportista.getFirebaseId().equals(remolque.getFirebaseIdTransportista())) {
                    break;
                }
            }
        }

        return item;
    }

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


    public void showQuestion() {
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

    private void onCargarTiposRemolques() {
        tiposRemolquesList = new ArrayList<>();
        tiposRemolques = new ArrayList<>();
        tiposRemolquesList.add("Seleccione ...");

        //TODO Metodo para llamar al servidor
        tiposRemolquesList.add("Caja Refrijerada");
        tiposRemolquesList.add("Caja Seca");

        tiposRemolques.add(new TiposRemolques(1, "Caja Refrijerada"));
        tiposRemolques.add(new TiposRemolques(2, "Caja Seca"));

    }

    private void onCargarSpinnerTiposRemolques() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tiposRemolquesList);

        int itemSelection = onPreRenderSelectTipoRemolque();

        spinnerTipoRemolque.setAdapter(adapter);
        spinnerTipoRemolque.setSelection(itemSelection);
    }

    private int onPreRenderSelectTipoRemolque() {
        int item = 0;

        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {

            if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                Remolques remolque = (Remolques) _MAIN_DECODE.getDecodeItem().getItemModel();
                for (TiposRemolques tipoRemolque : tiposRemolques) {
                    item++;
                    if (tipoRemolque.getTipoRemolque().equals(remolque.getTipoRemolque())) {
                        break;
                    }
                }
            }
        } else if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            Remolques remolque = (Remolques) _MAIN_DECODE.getDecodeItem().getItemModel();
            for (TiposRemolques tipoRemolque : tiposRemolques) {
                item++;
                if (tipoRemolque.getTipoRemolque().equals(remolque.getTipoRemolque())) {
                    break;
                }
            }
        }

        return item;
    }

}
