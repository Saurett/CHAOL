package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesEquipoFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    private BootstrapCircleThumbnail bctPerfil;
    private Button btnTitulo, btnGuardar, btnActualizar;
    private EditText txtLicencia, txtCelular, txtTractorMarca, txtTractorModelo, txtTractorPlaca, txtRemolqueMarca, txtRemolqueModelo, txtRemolquePlaca;
    private Spinner spinnerChofer, spinnerTractor, spinnerRemolque;
    private LinearLayout linearLayout;
    private FloatingActionButton fabPerfil;

    private static List<String> choferesList;
    private List<Choferes> choferes;

    private static List<String> tractoresList;
    private List<Tractores> tractores;

    private static List<String> remolquesList;
    private List<Remolques> remolques;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    private static MainRegisterActivity activityInterface;

    private DatabaseReference dbFlete;
    private String firebaseIdChofer;
    private String firebaseIdTractor;
    private String firebaseIdRemolque;

    private ValueEventListener listenerFletes;

    private Choferes _choferActual;
    private Tractores _tractorActual;
    private Remolques _remolqueActual;

    private int _idOrigenView;

    /**
     * Declaraciones para Firebase
     **/
    private static MainFletes _mainFletesActual;

    private FirebaseDatabase database;
    private DatabaseReference drChofer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipo_fletes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        bctPerfil = (BootstrapCircleThumbnail) view.findViewById(R.id.bct_chofer_equipo);

        txtLicencia = (EditText) view.findViewById(R.id.txt_equipo_asignado_numero_licencia);
        txtCelular = (EditText) view.findViewById(R.id.txt_equipo_asignado_celular);
        txtTractorMarca = (EditText) view.findViewById(R.id.txt_equipo_asignado_tractor_marca);
        txtTractorModelo = (EditText) view.findViewById(R.id.txt_equipo_asignado_tractor_modelo);
        txtTractorPlaca = (EditText) view.findViewById(R.id.txt_equipo_asignado_tractor_placa);
        txtRemolqueMarca = (EditText) view.findViewById(R.id.txt_equipo_asignado_remolque_marca);
        txtRemolqueModelo = (EditText) view.findViewById(R.id.txt_equipo_asignado_remolque_modelo);
        txtRemolquePlaca = (EditText) view.findViewById(R.id.txt_equipo_asignado_remolque_placa);

        fabPerfil = (FloatingActionButton) view.findViewById(R.id.fab_img_chofer_equipo);

        spinnerChofer = (Spinner) view.findViewById(R.id.spinner_equipo_asignado_chofer);
        spinnerTractor = (Spinner) view.findViewById(R.id.spinner_equipo_asignado_tractor);
        spinnerRemolque = (Spinner) view.findViewById(R.id.spinner_equipo_asignado_remolque);

        btnTitulo = (Button) view.findViewById(R.id.btn_equipo_fletes);
        btnGuardar = (Button) view.findViewById(R.id.btn_guardar_equipo);
        btnActualizar = (Button) view.findViewById(R.id.btn_actualizar_equipo);

        linearLayout = (LinearLayout) view.findViewById(R.id.equipo_container);

        database = FirebaseDatabase.getInstance();
        drChofer = database.getReference(Constants.FB_KEY_MAIN_CHOFERES);

        btnTitulo.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);

        spinnerChofer.setOnItemSelectedListener(this);
        spinnerTractor.setOnItemSelectedListener(this);
        spinnerRemolque.setOnItemSelectedListener(this);

        txtLicencia.setTag(txtLicencia.getKeyListener());
        txtLicencia.setKeyListener(null);

        txtCelular.setTag(txtCelular.getKeyListener());
        txtCelular.setKeyListener(null);

        txtTractorMarca.setTag(txtTractorMarca.getKeyListener());
        txtTractorMarca.setKeyListener(null);

        txtTractorModelo.setTag(txtTractorModelo.getKeyListener());
        txtTractorModelo.setKeyListener(null);

        txtTractorPlaca.setTag(txtTractorPlaca.getKeyListener());
        txtTractorPlaca.setKeyListener(null);

        txtRemolqueMarca.setTag(txtRemolqueMarca.getKeyListener());
        txtRemolqueMarca.setKeyListener(null);

        txtRemolqueModelo.setTag(txtRemolqueModelo.getKeyListener());
        txtRemolqueModelo.setKeyListener(null);

        txtRemolquePlaca.setTag(txtRemolquePlaca.getKeyListener());
        txtRemolquePlaca.setKeyListener(null);

        linearLayout.setVisibility(View.GONE);

        RegistroFletesFragment.setFrameEquipo(View.GONE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.onPreRender();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != dbFlete) dbFlete.removeEventListener(listenerFletes);
    }

    private void onCargarSpinnerChoferes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, choferesList);

        int itemSelection = onPreRenderSelectChofer();

        spinnerChofer.setAdapter(adapter);
        spinnerChofer.setSelection(itemSelection);
    }

    private void onCargarSpinnerTractores() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tractoresList);

        int itemSelection = onPreRenderSelectTractor();

        spinnerTractor.setAdapter(adapter);
        spinnerTractor.setSelection(itemSelection);
    }

    private void onCargarSpinnerRemolques() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, remolquesList);

        int itemSelection = onPreRenderSelectRemolque();

        spinnerRemolque.setAdapter(adapter);
        spinnerRemolque.setSelection(itemSelection);
    }

    private int onPreRenderSelectChofer() {
        int item = 0;

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (Choferes miChofer : choferes) {

                if (null == _mainFletesActual.getChoferSeleccionado()) break;

                item++;
                if (miChofer.getFirebaseId().equals(
                        _mainFletesActual.getChoferSeleccionado().getFirebaseId())) {
                    break;
                }
            }
        }

        return item;
    }

    private int onPreRenderSelectTractor() {
        int item = 0;

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (Tractores miTractor : tractores) {

                if (null == _mainFletesActual.getTractorSeleccionado()) break;

                item++;
                if (miTractor.getFirebaseId().equals(
                        _mainFletesActual.getTractorSeleccionado().getFirebaseId())) {
                    break;
                }
            }
        }

        return item;
    }

    private int onPreRenderSelectRemolque() {
        int item = 0;

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (Remolques miRemolque : remolques) {

                if (null == _mainFletesActual.getRemolqueSeleccionado()) break;

                item++;
                if (miRemolque.getFirebaseId().equals(
                        _mainFletesActual.getRemolqueSeleccionado().getFirebaseId())) {
                    break;
                }
            }
        }

        return item;
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
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                this.onPreRenderEditar();
                break;
        }
    }

    private void onPreRenderEditar() {

        /**Obtiene el item selecionado en el fragmento de lista**/
        Agendas agenda = (Agendas) _MAIN_DECODE.getDecodeItem().getItemModel();

        dbFlete = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                .child(agenda.getFirebaseID());

        listenerFletes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                _mainFletesActual = new MainFletes();

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);

                for (DataSnapshot dsChoferSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_CHOFER_SELECCIONADO).getChildren()) {
                    Choferes choferSeleccionado = dsChoferSeleccionado.getValue(Choferes.class);

                    _mainFletesActual.setChoferSeleccionado(choferSeleccionado);

                    txtLicencia.setText(choferSeleccionado.getNumeroDeLicencia());
                    txtCelular.setText(choferSeleccionado.getCelular1());

                    fabPerfil.setVisibility(View.GONE);
                    bctPerfil.setVisibility(View.GONE);

                    if (null == choferSeleccionado.getImagenURL())
                        choferSeleccionado.setImagenURL("");

                    if (!choferSeleccionado.getImagenURL().isEmpty()) {

                        final ProgressDialog pdThumbnail = new ProgressDialog(getContext());
                        pdThumbnail.setMessage(getString(R.string.default_loading_msg));
                        pdThumbnail.setIndeterminate(false);
                        pdThumbnail.setCancelable(false);
                        pdThumbnail.show();

                        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(choferSeleccionado.getImagenURL());

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

                    break;
                }

                for (DataSnapshot dsTractorSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_TRACTOR_SELECCIONADO).getChildren()) {
                    Tractores tractorSeleccionado = dsTractorSeleccionado.getValue(Tractores.class);
                    _mainFletesActual.setTractorSeleccionado(tractorSeleccionado);

                    txtTractorMarca.setText(tractorSeleccionado.getMarca());
                    txtTractorModelo.setText(tractorSeleccionado.getModelo());
                    txtTractorPlaca.setText(tractorSeleccionado.getPlaca());

                    break;
                }

                for (DataSnapshot dsRemolqueSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_REMOLQUE_SELECCIONADO).getChildren()) {
                    Remolques remlqueSeleccionado = dsRemolqueSeleccionado.getValue(Remolques.class);
                    _mainFletesActual.setRemolqueSeleccionado(dsRemolqueSeleccionado.getValue(Remolques.class));

                    txtRemolqueMarca.setText(remlqueSeleccionado.getMarca());
                    txtRemolqueModelo.setText(remlqueSeleccionado.getModelo());
                    txtRemolquePlaca.setText(remlqueSeleccionado.getPlaca());

                    break;
                }

                for (DataSnapshot dsTransportistaSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                    Transportistas transportistaSeleccionado = dsTransportistaSeleccionado.getValue(Transportistas.class);

                    _mainFletesActual.setTransportistaSeleccionado(transportistaSeleccionado);

                    onPreRenderSpinnerChoferes();

                    break;
                }

                switch (flete.getEstatus()) {
                    case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                        RegistroFletesFragment.setFrameEquipo(View.GONE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                        RegistroFletesFragment.setFrameEquipo(View.GONE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                        RegistroFletesFragment.setFrameEquipo(View.GONE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                        RegistroFletesFragment.setFrameEquipo(View.VISIBLE);

                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                                spinnerChofer.setEnabled(false);
                                spinnerRemolque.setEnabled(false);
                                spinnerTractor.setEnabled(false);
                                break;
                            default:
                                spinnerChofer.setEnabled(true);
                                spinnerRemolque.setEnabled(true);
                                spinnerTractor.setEnabled(true);
                                break;
                        }

                        btnGuardar.setVisibility(View.VISIBLE);
                        btnActualizar.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                        RegistroFletesFragment.setFrameEquipo(View.VISIBLE);

                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                                spinnerChofer.setEnabled(false);
                                spinnerRemolque.setEnabled(false);
                                spinnerTractor.setEnabled(false);
                                break;
                            default:
                                spinnerChofer.setEnabled(true);
                                spinnerRemolque.setEnabled(true);
                                spinnerTractor.setEnabled(true);
                                btnActualizar.setVisibility(View.VISIBLE);
                                break;
                        }

                        btnGuardar.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                        RegistroFletesFragment.setFrameEquipo(View.VISIBLE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                        RegistroFletesFragment.setFrameEquipo(View.VISIBLE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                        RegistroFletesFragment.setFrameEquipo(View.VISIBLE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                        RegistroFletesFragment.setFrameEquipo(View.VISIBLE);

                        btnGuardar.setVisibility(View.GONE);
                        btnActualizar.setVisibility(View.GONE);

                        spinnerChofer.setEnabled(false);
                        spinnerRemolque.setEnabled(false);
                        spinnerTractor.setEnabled(false);

                        break;
                    default:
                        break;
                }

                _mainFletesActual.setFlete(flete);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbFlete.addValueEventListener(listenerFletes);
    }


    private void onPreRenderSpinnerChoferes() {

        drChofer.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                choferesList = new ArrayList<>();
                choferes = new ArrayList<>();

                choferesList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Choferes chofer = postSnapshot.getValue(Choferes.class);

                    if (chofer.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)
                            || chofer.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ASIGNADO)
                            || chofer.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_LIBRE)) {

                        if (!chofer.getFirebaseIdDelTransportista().equals(
                                _mainFletesActual.getTransportistaSeleccionado().getFirebaseId())) {
                            continue;
                        }

                        choferesList.add(chofer.getNombre());
                        choferes.add(chofer);
                    }
                }

                onCargarSpinnerChoferes();
                onPreRenderSpinnerTractores();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void onPreRenderSpinnerTractores() {

        DatabaseReference drTractor = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                .child(_mainFletesActual.getTransportistaSeleccionado().getFirebaseId());

        drTractor.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tractoresList = new ArrayList<>();
                tractores = new ArrayList<>();

                tractoresList.add("Seleccione ...");

                for (DataSnapshot psTractor : dataSnapshot.child(Constants.FB_KEY_MAIN_TRACTORES).getChildren()) {

                    Tractores tractor = psTractor.getValue(Tractores.class);

                    tractor.setFirebaseIdDelTransportista(dataSnapshot.getKey());

                    if (tractor.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)
                            || tractor.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ASIGNADO)
                            || tractor.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_LIBRE)) {

                        if (!tractor.getFirebaseIdDelTransportista().equals(
                                _mainFletesActual.getTransportistaSeleccionado().getFirebaseId())) {
                            continue;
                        }

                        tractoresList.add(tractor.getNumeroEconomico());
                        tractores.add(tractor);
                    }
                }

                onCargarSpinnerTractores();
                onPreRenderSpinnerRemolques();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void onPreRenderSpinnerRemolques() {

        DatabaseReference drRemolque = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                .child(_mainFletesActual.getTransportistaSeleccionado().getFirebaseId());

        drRemolque.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                remolquesList = new ArrayList<>();
                remolques = new ArrayList<>();

                remolquesList.add("Seleccione ...");

                for (DataSnapshot psRemolques : dataSnapshot.child(Constants.FB_KEY_MAIN_REMOLQUES).getChildren()) {

                    Remolques remolque = psRemolques.getValue(Remolques.class);

                    remolque.setFirebaseIdDelTransportista(dataSnapshot.getKey());

                    if (remolque.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)
                            || remolque.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ASIGNADO)
                            || remolque.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_LIBRE)
                            ) {

                        if (!remolque.getFirebaseIdDelTransportista().equals(
                                _mainFletesActual.getTransportistaSeleccionado().getFirebaseId())) {
                            continue;
                        }

                        remolquesList.add(remolque.getNumeroEconomico());
                        remolques.add(remolque);
                    }
                }

                onCargarSpinnerRemolques();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_equipo_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_guardar_equipo:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea guardar su asignación?");
                }
                break;
            case R.id.btn_actualizar_equipo:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea actualizar su asignación?");
                }
                break;
        }
    }

    private void showQuestion(String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle(btnTitulo.getText());
        ad.setMessage(message);
        ad.setCancelable(false);
        ad.setNegativeButton(getString(R.string.default_alert_dialog_cancelar), this);
        ad.setPositiveButton(getString(R.string.default_alert_dialog_aceptar), this);
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                switch (_idOrigenView) {
                    case R.id.btn_guardar_equipo:
                        this.validationRegister();
                        break;
                    case R.id.btn_actualizar_equipo:
                        this.validationEditer();
                        break;
                }
                break;
        }
    }

    private void validationRegister() {

        Boolean authorized = true;

        if (spinnerChofer.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerChofer.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (spinnerTractor.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerTractor.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (spinnerRemolque.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerRemolque.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (authorized) {
            this.createSolicitudEquipo();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void validationEditer() {

        Boolean authorized = true;

        if (spinnerChofer.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerChofer.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (spinnerTractor.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerTractor.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (spinnerRemolque.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerRemolque.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (authorized) {
            this.updateSolicitudEquipo();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void createSolicitudEquipo() {
        MainFletes mainFlete = new MainFletes();

        Fletes flete = _mainFletesActual.getFlete();

        String estatus = (Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR).equals(flete.getEstatus())
                ? Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR : flete.getEstatus();

        flete.setEstatus(estatus);

        Choferes chofer = new Choferes();

        chofer.setCelular1(_choferActual.getCelular1());
        chofer.setFirebaseId(_choferActual.getFirebaseId());
        chofer.setImagenURL(_choferActual.getImagenURL());
        chofer.setNombre(_choferActual.getNombre());
        chofer.setNumeroDeLicencia(_choferActual.getNumeroDeLicencia());

        Tractores tractor = new Tractores();

        tractor.setFirebaseId(_tractorActual.getFirebaseId());
        tractor.setMarca(_tractorActual.getMarca());
        tractor.setModelo(_tractorActual.getModelo());
        tractor.setNumeroEconomico(_tractorActual.getNumeroEconomico());
        tractor.setPlaca(_tractorActual.getPlaca());

        Remolques remolque = new Remolques();

        remolque.setFirebaseId(_remolqueActual.getFirebaseId());
        remolque.setMarca(_remolqueActual.getMarca());
        remolque.setModelo(_remolqueActual.getModelo());
        remolque.setNumeroEconomico(_remolqueActual.getNumeroEconomico());
        remolque.setPlaca(_remolqueActual.getPlaca());

        mainFlete.setFlete(flete);
        mainFlete.setChoferSeleccionado(chofer);
        mainFlete.setTractorSeleccionado(tractor);
        mainFlete.setRemolqueSeleccionado(remolque);

        activityInterface.createSolicitudEquipo(mainFlete);
    }

    private void updateSolicitudEquipo() {
        MainFletes mainFlete = new MainFletes();

        Choferes chofer = new Choferes();

        chofer.setCelular1(_choferActual.getCelular1());
        chofer.setFirebaseId(_choferActual.getFirebaseId());
        chofer.setImagenURL(_choferActual.getImagenURL());
        chofer.setNombre(_choferActual.getNombre());
        chofer.setNumeroDeLicencia(_choferActual.getNumeroDeLicencia());

        Tractores tractor = new Tractores();

        tractor.setFirebaseId(_tractorActual.getFirebaseId());
        tractor.setMarca(_tractorActual.getMarca());
        tractor.setModelo(_tractorActual.getModelo());
        tractor.setNumeroEconomico(_tractorActual.getNumeroEconomico());
        tractor.setPlaca(_tractorActual.getPlaca());

        Remolques remolque = new Remolques();

        remolque.setFirebaseId(_remolqueActual.getFirebaseId());
        remolque.setMarca(_remolqueActual.getMarca());
        remolque.setModelo(_remolqueActual.getModelo());
        remolque.setNumeroEconomico(_remolqueActual.getNumeroEconomico());
        remolque.setPlaca(_remolqueActual.getPlaca());

        mainFlete.setFlete(_mainFletesActual.getFlete());
        mainFlete.setChoferSeleccionado(chofer);
        mainFlete.setTractorSeleccionado(tractor);
        mainFlete.setRemolqueSeleccionado(remolque);

        activityInterface.updateSolicitudEquipo(mainFlete, _mainFletesActual);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_equipo_asignado_chofer:
                if (position > 0) {

                    Choferes chofer = choferes.get(position - 1);
                    firebaseIdChofer = chofer.getFirebaseId();

                    this.loadDatosChofer();
                } else {
                    this.cleanChofer();
                }
                break;
            case R.id.spinner_equipo_asignado_tractor:
                if (position > 0) {

                    Tractores tractor = tractores.get(position - 1);
                    firebaseIdTractor = tractor.getFirebaseId();

                    this.loadDatosTractor();
                } else {
                    this.cleanTractor();
                }
                break;
            case R.id.spinner_equipo_asignado_remolque:
                if (position > 0) {

                    Remolques remolque = remolques.get(position - 1);
                    firebaseIdRemolque = remolque.getFirebaseId();

                    this.loadDatosRemolque();
                } else {
                    this.cleanRemolque();
                }
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadDatosChofer() {
        DatabaseReference drChoferSpinner = database.getReference(Constants.FB_KEY_MAIN_CHOFERES)
                .child(firebaseIdChofer);

        /**Metodo que llama la lista**/
        drChoferSpinner.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Choferes choferSeleccionado = dataSnapshot.getValue(Choferes.class);

                txtLicencia.setText(choferSeleccionado.getNumeroDeLicencia());
                txtCelular.setText(choferSeleccionado.getCelular1());

                fabPerfil.setVisibility(View.GONE);
                bctPerfil.setVisibility(View.GONE);

                if (null == choferSeleccionado.getImagenURL()) choferSeleccionado.setImagenURL("");

                if (!choferSeleccionado.getImagenURL().isEmpty()) {

                    final ProgressDialog pdThumbnail = new ProgressDialog(getContext());
                    pdThumbnail.setMessage(getString(R.string.default_loading_msg));
                    pdThumbnail.setIndeterminate(false);
                    pdThumbnail.setCancelable(false);
                    pdThumbnail.show();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(choferSeleccionado.getImagenURL());

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

                _choferActual = choferSeleccionado;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void cleanChofer() {
        Choferes chofer = new Choferes();

        txtLicencia.setText(chofer.getNumeroDeLicencia());
        txtCelular.setText(chofer.getCelular1());

        _choferActual = chofer;
    }

    private void loadDatosTractor() {
        DatabaseReference drTractor = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                .child(_mainFletesActual.getTransportistaSeleccionado().getFirebaseId())
                .child(Constants.FB_KEY_MAIN_TRACTORES)
                .child(firebaseIdTractor);

        drTractor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tractores tractor = dataSnapshot.getValue(Tractores.class);

                txtTractorMarca.setText(tractor.getMarca());
                txtTractorModelo.setText(tractor.getModelo());
                txtTractorPlaca.setText(tractor.getPlaca());

                _tractorActual = tractor;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cleanTractor() {
        Tractores tractor = new Tractores();

        txtTractorMarca.setText(tractor.getMarca());
        txtTractorModelo.setText(tractor.getModelo());
        txtTractorPlaca.setText(tractor.getPlaca());

        _tractorActual = tractor;
    }

    private void loadDatosRemolque() {

        DatabaseReference drRemolque = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                .child(_mainFletesActual.getTransportistaSeleccionado().getFirebaseId())
                .child(Constants.FB_KEY_MAIN_REMOLQUES)
                .child(firebaseIdRemolque);

        drRemolque.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Remolques remolque = dataSnapshot.getValue(Remolques.class);

                txtRemolqueMarca.setText(remolque.getMarca());
                txtRemolqueModelo.setText(remolque.getModelo());
                txtRemolquePlaca.setText(remolque.getPlaca());

                _remolqueActual = remolque;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void cleanRemolque() {

        Remolques remolque = new Remolques();

        txtRemolqueMarca.setText(remolque.getMarca());
        txtRemolqueModelo.setText(remolque.getModelo());
        txtRemolquePlaca.setText(remolque.getPlaca());

        _remolqueActual = remolque;

    }

}
