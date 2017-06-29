package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private static final String TAG = FletesEquipoFragment.class.getName();

    private Button btnTitulo;
    private EditText txtLicencia, txtCelular, txtTractorMarca, txtTractorModelo, txtTractorPlaca, txtRemolqueMarca, txtRemolqueModelo, txtRemolquePlaca;
    private Spinner spinnerChofer, spinnerTractor, spinnerRemolque;
    private LinearLayout linearLayout;
    private ProgressDialog pDialog;

    private static List<String> choferesList;
    private List<Choferes> choferes;

    private static List<String> tractoresList;
    private List<Tractores> tractores;

    private static List<String> remolquesList;
    private List<Remolques> remolques;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

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

        txtLicencia = (EditText) view.findViewById(R.id.txt_equipo_asignado_numero_licencia);
        txtCelular = (EditText) view.findViewById(R.id.txt_equipo_asignado_celular);
        txtTractorMarca = (EditText) view.findViewById(R.id.txt_equipo_asignado_tractor_marca);
        txtTractorModelo = (EditText) view.findViewById(R.id.txt_equipo_asignado_tractor_modelo);
        txtTractorPlaca = (EditText) view.findViewById(R.id.txt_equipo_asignado_tractor_placa);
        txtRemolqueMarca = (EditText) view.findViewById(R.id.txt_equipo_asignado_remolque_marca);
        txtRemolqueModelo = (EditText) view.findViewById(R.id.txt_equipo_asignado_remolque_modelo);
        txtRemolquePlaca = (EditText) view.findViewById(R.id.txt_equipo_asignado_remolque_placa);

        spinnerChofer = (Spinner) view.findViewById(R.id.spinner_equipo_asignado_chofer);
        spinnerTractor = (Spinner) view.findViewById(R.id.spinner_equipo_asignado_tractor);
        spinnerRemolque = (Spinner) view.findViewById(R.id.spinner_equipo_asignado_remolque);

        btnTitulo = (Button) view.findViewById(R.id.btn_equipo_fletes);
        linearLayout = (LinearLayout) view.findViewById(R.id.equipo_container);

        database = FirebaseDatabase.getInstance();
        drChofer = database.getReference(Constants.FB_KEY_MAIN_CHOFERES);

        btnTitulo.setOnClickListener(this);

        spinnerChofer.setOnItemSelectedListener(this);
        spinnerTractor.setOnItemSelectedListener(this);
        spinnerRemolque.setOnItemSelectedListener(this);

        linearLayout.setVisibility(View.GONE);

        this.onPreRender();

        return view;
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

        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    public void onPreRender() {
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                onPreRenderEditar();
                break;
            case Constants.ACCION_REGISTRAR:
                break;
        }
    }

    private void onPreRenderEditar() {

        /**Obtiene el item selecionado en el fragmento de lista**/
        Agendas agenda = (Agendas) _MAIN_DECODE.getDecodeItem().getItemModel();

        DatabaseReference dbFlete =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                        .child(agenda.getFirebaseID());

        final ProgressDialog pDialogRender = new ProgressDialog(getContext());
        pDialogRender.setMessage(getString(R.string.default_loading_msg));
        pDialogRender.setIndeterminate(false);
        pDialogRender.setCancelable(false);
        pDialogRender.show();

        dbFlete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(TAG, "onPreRenderEditar: " + dataSnapshot.getKey());

                _mainFletesActual = new MainFletes();

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);

                for (DataSnapshot dsTransportistaSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                    Transportistas transportistaSeleccionado = dsTransportistaSeleccionado.getValue(Transportistas.class);

                    _mainFletesActual.setTransportistaSeleccionado(transportistaSeleccionado);

                    onPreRenderSpinnerChoferes();

                    break;
                }

                for (DataSnapshot dsChoferSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_CHOFER_SELECCIONADO).getChildren()) {
                    Choferes choferSeleccionado = dsChoferSeleccionado.getValue(Choferes.class);

                    _mainFletesActual.setChoferSeleccionado(choferSeleccionado);

                    txtLicencia.setText(choferSeleccionado.getNumeroDeLicencia());
                    //TODO FOTO IMAGENURL
                    txtCelular.setText(choferSeleccionado.getCelular1());

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

                _mainFletesActual.setFlete(flete);

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void onPreRenderSpinnerChoferes() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drChofer.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                choferesList = new ArrayList<>();
                choferes = new ArrayList<>();

                choferesList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.i(TAG, "addListenerForSingleValueEvent Choferes : " + postSnapshot.getKey());

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
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void onPreRenderSpinnerTractores() {

        DatabaseReference drTractor = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                .child(_mainFletesActual.getTransportistaSeleccionado().getFirebaseId())
                .child(Constants.FB_KEY_MAIN_TRACTORES);

        drTractor.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tractoresList = new ArrayList<>();
                tractores = new ArrayList<>();

                tractoresList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.i(TAG, "addListenerForSingleValueEvent Tractores : " + postSnapshot.getKey());

                    Tractores tractor = postSnapshot.getValue(Tractores.class);

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
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void onPreRenderSpinnerRemolques() {

        DatabaseReference drRemolque = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                .child(_mainFletesActual.getTransportistaSeleccionado().getFirebaseId())
                .child(Constants.FB_KEY_MAIN_REMOLQUES);

        drRemolque.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                remolquesList = new ArrayList<>();
                remolques = new ArrayList<>();

                remolquesList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.i(TAG, "addListenerForSingleValueEvent Remolques : " + postSnapshot.getKey());

                    Remolques remolque = postSnapshot.getValue(Remolques.class);

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

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_equipo_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {

                }
                break;
        }
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
}
