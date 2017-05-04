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
import com.indev.chaol.adapters.TractoresAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class TractoresFragment extends Fragment implements View.OnClickListener {

    private static List<Tractores> tractoresList;
    private static RecyclerView recyclerViewTractores;
    private TractoresAdapter tractoresAdapter;
    private static TractoresAdapter adapter;
    private ProgressDialog pDialog;
    private static NavigationDrawerInterface navigationDrawerInterface;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drTractores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tractores, container, false);

        recyclerViewTractores = (RecyclerView) view.findViewById(R.id.recycler_view_tractores);
        tractoresAdapter = new TractoresAdapter();
        tractoresAdapter.setOnClickListener(this);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drTractores = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drTractores.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tractoresAdapter = new TractoresAdapter();
                tractoresList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psTractores : postSnapshot.child(Constants.FB_KEY_MAIN_TRACTORES).getChildren()) {
                        Tractores tractor = psTractores.getValue(Tractores.class);
                        tractor.setFirebaseIdTransportista(postSnapshot.getKey());
                        tractoresList.add(tractor);
                    }
                }

                onPreRenderTractores();

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
    private void onPreRenderTractores() {

        if (tractoresList.size() > 0) {
            tractoresAdapter.addAll(tractoresList);

            recyclerViewTractores.setAdapter(tractoresAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerViewTractores.setLayoutManager(linearLayoutManager);
        } else {
            Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
        }

        adapter = tractoresAdapter;
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

    /**Permite redireccionar a los metodos correspondientes dependiendo la cción deseada**/
    public static void onListenerAction(DecodeItem decodeItem) {
        /**Inicializa DecodeItem en la activity principal**/
        navigationDrawerInterface.setDecodeItem(decodeItem);

        switch (decodeItem.getIdView()) {
            case R.id.item_btn_editar_tractor:
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR,MainRegisterActivity.class);
                break;
            case R.id.item_btn_eliminar_tractor:
                navigationDrawerInterface.showQuestion();
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        tractoresList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }
}
