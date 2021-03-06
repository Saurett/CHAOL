package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.adapters.AsignacionesTransportistasAdapter;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class AsignacionTransportistasFragment extends Fragment implements View.OnClickListener {

    private static List<Transportistas> transportistasList;
    private static Transportistas transportistaSeleccionado;
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
    private ValueEventListener listenerFletes;

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

        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_ADMINISTRADOR)
                || _SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR)) {
            recyclerViewAsignaciones.setVisibility(View.VISIBLE);
        }

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

    @Override
    public void onStop() {
        super.onStop();
        if (null != listenerFletes) drFletes.removeEventListener(listenerFletes);
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

        listenerFletes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                transportistasAdapter = new AsignacionesTransportistasAdapter();
                transportistasList = new ArrayList<>();
                String firebaseIdTransportistaSeleccionado = "";

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);
                _mainFletesActual = new MainFletes();

                _mainFletesActual.setFlete(flete);

                if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
                    FletesAsignacionFragment.showCancelAsignacion(View.GONE);
                }

                transportistaSeleccionado = new Transportistas();

                for (DataSnapshot psSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                    Transportistas transportista = psSeleccionado.getValue(Transportistas.class);
                    firebaseIdTransportistaSeleccionado = transportista.getFirebaseId();

                    FletesAsignacionFragment.showMessageAsignacion(View.VISIBLE, "En espera de autorizar transportista");

                    switch (_SESSION_USER.getTipoDeUsuario()) {
                        case Constants.FB_KEY_ITEM_TIPO_USUARIO_ADMINISTRADOR:
                        case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                        case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                        case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                            FletesAsignacionFragment.showMessageAsignacion(View.VISIBLE, "Transportista seleccionado : " + transportista.getNombre());
                            break;
                        case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                            if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId()))
                                FletesAsignacionFragment.showMessageAsignacion(View.VISIBLE, "Transportista seleccionado : " + transportista.getNombre());
                            break;
                    }

                    transportistaSeleccionado = transportista;
                }

                for (DataSnapshot psInteresados : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTAS_INTERESADOS).getChildren()) {
                    Transportistas transportista = psInteresados.getValue(Transportistas.class);


                    String estatus = (firebaseIdTransportistaSeleccionado.isEmpty())
                            ? Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_INTERESADO
                            : Constants.FB_KEY_ITEM_ESTATUS_INACTIVO;

                    transportista.setEstatus((firebaseIdTransportistaSeleccionado.equals(transportista.getFirebaseId()))
                            ? Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_SELECCIONADO
                            : estatus);

                    if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
                        if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId()))
                            FletesAsignacionFragment.showCancelAsignacion(View.VISIBLE);
                    }

                    transportistasList.add(transportista);
                }

                switch (_mainFletesActual.getFlete().getEstatus()) {
                    case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                    case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                    case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                    case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                        FletesAsignacionFragment.showCancelAsignacionSolo(View.GONE);
                        break;
                }

                onPreRenderTransportistas();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbFlete.addValueEventListener(listenerFletes);
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
                activityInterface.showQuestion("¿Esta seguro que desea autorizar transportista?");
                break;
            case R.id.item_btn_eliminar_asinacion_transportista:
                activityInterface.showQuestion("¿Esta seguro que desea eliminar transportista?");
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        transportistasList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }

    public static List<Transportistas> getTransportistasList() {
        return transportistasList;
    }

    public static Transportistas getTransportistaSeleccionado() {
        return transportistaSeleccionado;
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

    public static MainFletes getMainFletes() {
        return _mainFletesActual;
    }
}
