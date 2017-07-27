package com.indev.chaol.fragments;

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
import com.indev.chaol.adapters.ChoferesAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class ChoferesFragment extends Fragment implements View.OnClickListener {

    private static List<Choferes> choferesList;
    private static RecyclerView recyclerViewChoferes;
    private ChoferesAdapter choferesAdapter;
    private static ChoferesAdapter adapter;
    private static NavigationDrawerInterface navigationDrawerInterface;

    private static Usuarios _SESSION_USER;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drChoferes;
    private ValueEventListener listenerChoferes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choferes, container, false);

        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        recyclerViewChoferes = (RecyclerView) view.findViewById(R.id.recycler_view_choferes);
        choferesAdapter = new ChoferesAdapter();
        choferesAdapter.setOnClickListener(this);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drChoferes = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        return view;
    }

    /**
     * Carga el listado predeterminado de firebase
     **/
    private void onPreRenderChoferes() {

        Collections.sort(choferesList, new Comparator<Choferes>() {
            @Override
            public int compare(Choferes o1, Choferes o2) {
                return (o1.getNombre().compareTo(o2.getNombre()));
            }
        });

        choferesAdapter.addAll(choferesList);
        recyclerViewChoferes.setAdapter(choferesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewChoferes.setLayoutManager(linearLayoutManager);

        if (choferesList.size() == 0) {
            Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
        }

        adapter = choferesAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        listenerChoferes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                choferesAdapter = new ChoferesAdapter();
                choferesList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psRemolques : postSnapshot.child(Constants.FB_KEY_MAIN_CHOFERES).getChildren()) {
                        Choferes chofer = psRemolques.getValue(Choferes.class);

                        DataSnapshot psTransportista = postSnapshot.child(Constants.FB_KEY_ITEM_TRANSPORTISTA);
                        Transportistas transportista = psTransportista.getValue(Transportistas.class);

                        if (!Constants.FB_KEY_ITEM_ESTATUS_ACTIVO.equals(transportista.getEstatus())) break;

                        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA)) {
                            if (!_SESSION_USER.getFirebaseId().equals(postSnapshot.getKey())) continue;
                        }

                        if (!chofer.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO)) {
                            chofer.setFirebaseIdDelTransportista(postSnapshot.getKey());
                            chofer.setNombre(transportista.getNombre() + " - " + chofer.getNombre());
                            choferesList.add(chofer);
                        }
                    }
                }



                onPreRenderChoferes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        drChoferes.addValueEventListener(listenerChoferes);

    }

    @Override
    public void onStop() {
        super.onStop();
        drChoferes.removeEventListener(listenerChoferes);
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
            case R.id.item_btn_editar_chofer:
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                break;
            case R.id.item_btn_eliminar_chofer:
                navigationDrawerInterface.showQuestion();
                break;
            case R.id.item_switch_activar_chofer:
                Choferes chofer = (Choferes) decodeItem.getItemModel();
                navigationDrawerInterface.updateUserChofer(chofer,null);
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        choferesList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }
}
