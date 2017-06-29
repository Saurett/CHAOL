package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.adapters.AsignacionesTransportistasAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.indev.chaol.utils.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class AsignacionTransportistasFragment extends Fragment implements View.OnClickListener {

    private static List<Transportistas> transportistasList;
    private static RecyclerView recyclerViewAsignaciones;
    private AsignacionesTransportistasAdapter transportistasAdapter;
    private static AsignacionesTransportistasAdapter adapter;
    private static MainRegisterActivity activityInterface;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;
    private static MainFletes _mainFletesActual;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drFletes;

    private ValueEventListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asignaciones_transportistas, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        recyclerViewAsignaciones = (RecyclerView) view.findViewById(R.id.recycler_view_asignaciones_transportistas);
        transportistasAdapter = new AsignacionesTransportistasAdapter();
        transportistasAdapter.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        drFletes = database.getReference(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.onPreRender();
    }

    public void onPreRender() {
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                this.onPreRenderEditar();
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

                transportistasAdapter = new AsignacionesTransportistasAdapter();
                transportistasList = new ArrayList<>();
                String firebaseIdTransportistaSeleccionado = "";

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);
                _mainFletesActual = new MainFletes();

                _mainFletesActual.setFlete(flete);

                for (DataSnapshot psSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                    Transportistas transportista = psSeleccionado.getValue(Transportistas.class);
                    firebaseIdTransportistaSeleccionado = transportista.getFirebaseId();
                    FletesAsignacionFragment.showMessageAsignacion(View.GONE);
                    break;
                }

                for (DataSnapshot psInteresados : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTAS_INTERESADOS).getChildren()) {
                    Transportistas transportista = psInteresados.getValue(Transportistas.class);


                    String estatus = (firebaseIdTransportistaSeleccionado.isEmpty())
                            ? Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_INTERESADO
                            : Constants.FB_KEY_ITEM_ESTATUS_INACTIVO;

                    transportista.setEstatus((firebaseIdTransportistaSeleccionado.equals(transportista.getFirebaseId()))
                            ? Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_SELECCIONADO
                            : estatus);

                    transportistasList.add(transportista);
                }

                onPreRenderTransportistas();

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    @Override
    public void onClick(View v) {

    }

    /**
     * Permite redireccionar a los metodos correspondientes dependiendo la cción deseada
     **/
    public static void onListenerAction(DecodeItem decodeItem) {
        /**Inicializa DecodeItem en la activity principal**/
        activityInterface.setDecodeItem(decodeItem);

        switch (decodeItem.getIdView()) {
            case R.id.item_btn_perfil_asignacion_transportista:
                activityInterface.openExternalActivity(Constants.ACCION_VER, MainRegisterActivity.class);
                break;
            case R.id.item_btn_autorizar_asinacion_transportista:
                //navigationDrawerInterface.showQuestion();
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        transportistasList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }

    /**
     * Carga el listado predeterminado de firebase
     **/
    private void onPreRenderTransportistas() {

        transportistasAdapter.addAll(transportistasList);

        recyclerViewAsignaciones.setAdapter(transportistasAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAsignaciones.setLayoutManager(linearLayoutManager);

        adapter = transportistasAdapter;
    }
}
