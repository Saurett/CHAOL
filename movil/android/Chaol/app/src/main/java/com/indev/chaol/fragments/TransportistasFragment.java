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

import com.indev.chaol.R;
import com.indev.chaol.adapters.ClientesAdapter;
import com.indev.chaol.adapters.TransportistasAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class TransportistasFragment extends Fragment implements View.OnClickListener {

    private static List<Transportistas> transportistasList;
    private static RecyclerView recyclerViewTransportistas;
    private TransportistasAdapter transportistasAdapter;
    private static TransportistasAdapter adapter;
    private ProgressDialog pDialog;
    private static NavigationDrawerInterface navigationDrawerInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transportistas, container, false);

        recyclerViewTransportistas = (RecyclerView) view.findViewById(R.id.recycler_view_transportistas);
        transportistasAdapter = new TransportistasAdapter();
        transportistasAdapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_BUSCAR_TRANSPORTISTAS);
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

    }

    public static void onListenerAction(DecodeItem decodeItem) {
        navigationDrawerInterface.showQuestion(decodeItem);
    }

    public static void deleteItem(DecodeItem decodeItem) {
        transportistasList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<Transportistas> tempTransportistasList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempTransportistasList = new ArrayList<>();
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
                    case Constants.WS_KEY_BUSCAR_TRANSPORTISTAS:

                        tempTransportistasList = new ArrayList<>();
                        List<Transportistas> transportes = new ArrayList<>();

                        transportes.add(new Transportistas("Transportes CHAOL"));
                        transportes.add(new Transportistas("Transportes LOAHC"));
                        transportes.add(new Transportistas("Transportes Leyva"));
                        transportes.add(new Transportistas("Transportes Saurett"));

                        tempTransportistasList.addAll(transportes);

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
                transportistasList = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempTransportistasList.size() > 0) {
                        transportistasList.addAll(tempTransportistasList);
                        transportistasAdapter.addAll(transportistasList);

                        recyclerViewTransportistas.setAdapter(transportistasAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerViewTransportistas.setLayoutManager(linearLayoutManager);

                    } else {
                        Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La lista  se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }

                adapter = transportistasAdapter;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
