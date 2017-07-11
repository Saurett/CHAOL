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
import com.indev.chaol.adapters.ColaboradoresAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Administradores;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class ColaboradoresFragment extends Fragment implements View.OnClickListener {

    private static List<Administradores> administradoresList;
    private static RecyclerView recyclerViewColaboradores;
    private ColaboradoresAdapter colaboradoresAbapter;
    private static ColaboradoresAdapter adapter;
    private static NavigationDrawerInterface navigationDrawerInterface;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drColaboradores;
    private ValueEventListener listenerColaboradores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_colaboradores, container, false);

        recyclerViewColaboradores = (RecyclerView) view.findViewById(R.id.recycler_view_colaboradores);
        colaboradoresAbapter = new ColaboradoresAdapter();
        colaboradoresAbapter.setOnClickListener(this);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drColaboradores = database.getReference(Constants.FB_KEY_MAIN_ADMINISTRADORES);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Carga el listado predeterminado de firebase
     **/
    private void onPreRenderClientes() {

        colaboradoresAbapter.addAll(administradoresList);

        recyclerViewColaboradores.setAdapter(colaboradoresAbapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewColaboradores.setLayoutManager(linearLayoutManager);

        if (administradoresList.size() == 0) {
            Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
        }

        adapter = colaboradoresAbapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        listenerColaboradores = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                colaboradoresAbapter = new ColaboradoresAdapter();
                administradoresList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Administradores colaborador = postSnapshot.getValue(Administradores.class);
                    if (!colaborador.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO)) {

                        if (colaborador.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_ADMINISTRADOR)) continue;

                        administradoresList.add(colaborador);
                    }
                }

                onPreRenderClientes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        drColaboradores.addValueEventListener(listenerColaboradores);
    }

    @Override
    public void onStop() {
        super.onStop();
        drColaboradores.removeEventListener(listenerColaboradores);
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
            case R.id.item_btn_editar_colaboradores:
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                break;
            case R.id.item_btn_eliminar_colaborador:
                navigationDrawerInterface.showQuestion();
                break;
            case R.id.item_switch_activar_colaborador:
                Administradores colaborador = (Administradores) decodeItem.getItemModel();
                navigationDrawerInterface.updateUserColaborador(colaborador);
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        administradoresList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }
}
