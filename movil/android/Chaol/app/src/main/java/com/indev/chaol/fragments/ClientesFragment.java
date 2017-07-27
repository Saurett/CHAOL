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
import com.indev.chaol.adapters.ClientesAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class ClientesFragment extends Fragment implements View.OnClickListener {

    private static List<Clientes> clientesList;
    private static RecyclerView recyclerViewClientes;
    private ClientesAdapter clientesAdapter;
    private static ClientesAdapter adapter;
    private static NavigationDrawerInterface navigationDrawerInterface;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drClientes;
    private ValueEventListener listenerClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        recyclerViewClientes = (RecyclerView) view.findViewById(R.id.recycler_view_clientes);
        clientesAdapter = new ClientesAdapter();
        clientesAdapter.setOnClickListener(this);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drClientes = database.getReference(Constants.FB_KEY_MAIN_CLIENTES);

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

        Collections.sort(clientesList, new Comparator<Clientes>() {
            @Override
            public int compare(Clientes o1, Clientes o2) {
                return (o1.getNombre().compareTo(o2.getNombre()));
            }
        });

        clientesAdapter.addAll(clientesList);

        recyclerViewClientes.setAdapter(clientesAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewClientes.setLayoutManager(linearLayoutManager);

        if (clientesList.size() == 0) {
            Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
        }


        adapter = clientesAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        listenerClientes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clientesAdapter = new ClientesAdapter();
                clientesList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Clientes cliente = postSnapshot.child(Constants.FB_KEY_ITEM_CLIENTE).getValue(Clientes.class);
                    if (!cliente.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO)) {
                        clientesList.add(cliente);
                    }
                }

                onPreRenderClientes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        drClientes.addValueEventListener(listenerClientes);
    }

    @Override
    public void onStop() {
        super.onStop();
        drClientes.removeEventListener(listenerClientes);
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
            case R.id.item_btn_editar_cliente:
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                break;
            case R.id.item_btn_eliminar_cliente:
                navigationDrawerInterface.showQuestion();
                break;
            case R.id.item_switch_activar_cliente:
                Clientes cliente = (Clientes) decodeItem.getItemModel();
                navigationDrawerInterface.updateUserCliente(cliente, null);
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        clientesList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }
}
