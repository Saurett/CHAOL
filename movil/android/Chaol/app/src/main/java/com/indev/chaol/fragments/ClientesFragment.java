package com.indev.chaol.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.adapters.ClientesAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class ClientesFragment extends Fragment implements View.OnClickListener {

    private static List<Clientes> clientesList;
    private static RecyclerView recyclerViewClientes;
    private ClientesAdapter clientesAdapter;
    private static ClientesAdapter adapter;
    private ProgressDialog pDialog;
    private static NavigationDrawerInterface navigationDrawerInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        recyclerViewClientes = (RecyclerView) view.findViewById(R.id.recycler_view_clientes);
        clientesAdapter = new ClientesAdapter();
        clientesAdapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_BUSCAR_CLIENTES);
        wsTaskList.execute();
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
            case R.id.item_btn_editar_cliente:
                navigationDrawerInterface.openExternalActivity(Constants.ACCION_EDITAR, MainRegisterActivity.class);
                break;
            case R.id.item_btn_eliminar_cliente:
                navigationDrawerInterface.showQuestion();
                break;
        }
    }

    public static void deleteItem(DecodeItem decodeItem) {
        clientesList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<Clientes> tempClientesList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempClientesList = new ArrayList<>();
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
                    case Constants.WS_KEY_BUSCAR_CLIENTES:
                        tempClientesList = new ArrayList<>();
                        List<Clientes> clientes = new ArrayList<>();

                        clientes.add(new Clientes("Francisco Javier Díaz Saurett"));
                        clientes.add(new Clientes("Fred Gomez Leyva"));
                        clientes.add(new Clientes("Jorge Chaol Strayer"));
                        clientes.add(new Clientes("Carlos Moreno Cantinflas"));

                        tempClientesList.addAll(clientes);

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
                clientesList = new ArrayList<>();
                pDialog.dismiss();
                if (success) {
                    switch (webServiceOperation) {
                        case Constants.WS_KEY_BUSCAR_CLIENTES:

                            if (tempClientesList.size() > 0) {
                                clientesList.addAll(tempClientesList);
                                clientesAdapter.addAll(clientesList);

                                recyclerViewClientes.setAdapter(clientesAdapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerViewClientes.setLayoutManager(linearLayoutManager);

                            } else {
                                Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }

                } else {
                    String tempText = (textError.isEmpty() ? "Error desconocido" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }

                adapter = clientesAdapter;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
