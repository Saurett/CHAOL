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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.adapters.BodegasAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class BodegasFragment extends Fragment implements View.OnClickListener {

    private static List<Bodegas> bodegasList;
    private static RecyclerView recyclerViewBodegas;
    private BodegasAdapter bodegasAdapter;
    private static BodegasAdapter adapter;
    private ProgressDialog pDialog;
    private static NavigationDrawerInterface navigationDrawerInterface;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bodegas, container, false);

        recyclerViewBodegas = (RecyclerView) view.findViewById(R.id.recycler_view_bodegas);
        bodegasAdapter = new BodegasAdapter();
        bodegasAdapter.setOnClickListener(this);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drClientes = database.getReference(Constants.FB_KEY_MAIN_CLIENTES);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drClientes.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bodegasAdapter = new BodegasAdapter();
                bodegasList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psBodegas : postSnapshot.child(Constants.FB_KEY_MAIN_BODEGAS).getChildren()) {

                        Bodegas bodega = psBodegas.getValue(Bodegas.class);

                        if (bodega.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)) {
                            bodega.setFirebaseIdCliente(postSnapshot.getKey());
                            bodegasList.add(bodega);
                        }
                    }
                }

                onPreRenderBodegas();

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });


        return view;
    }

    /**Carga el listado predeterminado de firebase**/
    private void onPreRenderBodegas() {

        bodegasAdapter.addAll(bodegasList);

        recyclerViewBodegas.setAdapter(bodegasAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewBodegas.setLayoutManager(linearLayoutManager);

        if (bodegasList.size() == 0) {
            Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
        }

        adapter = bodegasAdapter;
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
        Toast.makeText(getContext(), "Boton de fletes, añadir fletes", Toast.LENGTH_SHORT).show();
    }

    /**
     * Permite redireccionar a los metodos correspondientes dependiendo la cción deseada
     **/
    public static void onListenerAction(DecodeItem decodeItem) {
        /**Inicializa DecodeItem en la activity principal**/
        navigationDrawerInterface.setDecodeItem(decodeItem);

        switch (decodeItem.getIdView()) {
            case R.id.item_btn_editar_bodega:
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                break;
            case R.id.item_btn_eliminar_bodega:
                navigationDrawerInterface.showQuestion();
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        bodegasList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }
}
