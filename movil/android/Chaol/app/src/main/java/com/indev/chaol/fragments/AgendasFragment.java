package com.indev.chaol.fragments;

import android.app.ProgressDialog;
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
import com.indev.chaol.R;
import com.indev.chaol.adapters.AgendasAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class AgendasFragment extends Fragment implements View.OnClickListener {

    private static List<Agendas> agendasList;
    private static RecyclerView recyclerViewAgendas;
    private AgendasAdapter agendasAdapter;
    private static AgendasAdapter adapter;
    private ProgressDialog pDialog;
    private static NavigationDrawerInterface navigationDrawerInterface;

    private static List<String> _FIREBASE_LIST;

    private int _idOrigenView;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drFletes;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agendas, container, false);

        _FIREBASE_LIST = (List<String>) getActivity().getIntent().getExtras().getSerializable("lista");

        recyclerViewAgendas = (RecyclerView) view.findViewById(R.id.recycler_view_agendas);
        agendasAdapter = new AgendasAdapter();
        agendasAdapter.setOnClickListener(this);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drFletes = database.getReference(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drFletes.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                agendasAdapter = new AgendasAdapter();
                agendasList = new ArrayList<>();

                String nombreDelTransportista = "";

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (!_FIREBASE_LIST.contains(postSnapshot.getKey())) continue;

                    Fletes flete = postSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);
                    Bodegas bodegaCarga = postSnapshot.child(Constants.FB_KEY_MAIN_BODEGA_DE_CARGA).getValue(Bodegas.class);

                    for (DataSnapshot psInteresados : postSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTAS_INTERESADOS).getChildren()) {
                        //firebaseIDTransportistas.add(psInteresados.getKey());
                    }

                    for (DataSnapshot psTransportista : postSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                        Transportistas transportista = psTransportista.getValue(Transportistas.class);
                        nombreDelTransportista = ((null != transportista) ? transportista.getNombre() : nombreDelTransportista);
                        break;
                    }

                    for (DataSnapshot psChofer : postSnapshot.child(Constants.FB_KEY_MAIN_CHOFER_SELECCIONADO).getChildren()) {
                        //firebaseIDChofer = psChofer.getKey();
                    }

                    if (null == bodegaCarga) continue;

                    if (null == flete) continue;

                    Agendas agenda = new Agendas();

                    agenda.setEstatus(flete.getEstatus());
                    agenda.setNombre(bodegaCarga.getNombreDelCliente());
                    agenda.setNombreDelTransportista(nombreDelTransportista);
                    agenda.setFirebaseID(flete.getFirebaseId());

                    agendasList.add(agenda);

                }

                onPreRenderBandejaFletes();

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });


        return view;
    }

    private void onPreRenderBandejaFletes() {

        agendasAdapter.addAll(agendasList);

        recyclerViewAgendas.setAdapter(agendasAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAgendas.setLayoutManager(linearLayoutManager);

        if (agendasList.size() == 0) {
            //Toast.makeText(getActivity().getApplicationContext(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
        }

        adapter = agendasAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            navigationDrawerInterface = (NavigationDrawerInterface) getActivity();
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
        navigationDrawerInterface.setDecodeItem(decodeItem);

        switch (decodeItem.getIdView()) {
            case R.id.item_btn_eliminar_remolque:
                navigationDrawerInterface.showQuestion();
                break;
            case R.id.item_color_agenda:

                //navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                Boolean cancelable = false;

                Agendas agenda = (Agendas) decodeItem.getItemModel();
                switch (agenda.getEstatus()) {
                    case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                    case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                    case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                    case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                    case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                        cancelable = true;
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                    case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                    case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                    case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                        cancelable = false;
                        break;
                    default:
                        break;
                }

                navigationDrawerInterface.showQuestionAgenda(cancelable);
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        agendasList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }
}
