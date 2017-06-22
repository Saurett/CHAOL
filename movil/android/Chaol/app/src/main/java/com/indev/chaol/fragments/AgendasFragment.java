package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.indev.chaol.adapters.AgendasAdapter;
import com.indev.chaol.adapters.ClientesAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;
import com.prolificinteractive.materialcalendarview.CalendarDay;

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

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drFletes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                        nombreDelTransportista =  ((null != transportista) ? transportista.getNombre() : nombreDelTransportista);
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
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        agendasList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<Agendas> tempRemolquesList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempRemolquesList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Buscando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_BUSCAR_REMOLQUES:
                        tempRemolquesList = new ArrayList<>();
                        List<Agendas> agendas = new ArrayList<>();

                        agendas.add(new Agendas("enProgreso", "Francisco Javier Díaz Saurett", "Rocio del Carmenjavier", ""));
                        agendas.add(new Agendas("esperandoPorTransportista", "Francisco Javier Díaz Saurett", "", ""));

                        tempRemolquesList.addAll(agendas);

                        validOperation = true;
                        break;
                }
            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                agendasList = new ArrayList<>();
                pDialog.dismiss();
                if (success) {
                    switch (webServiceOperation) {
                        case Constants.WS_KEY_BUSCAR_REMOLQUES:
                            if (tempRemolquesList.size() > 0) {
                                agendasList.addAll(tempRemolquesList);
                                agendasAdapter.addAll(agendasList);

                                recyclerViewAgendas.setAdapter(agendasAdapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerViewAgendas.setLayoutManager(linearLayoutManager);

                            } else {
                                Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La lista  se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }

                adapter = agendasAdapter;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
