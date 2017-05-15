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

import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.adapters.AgendasAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.DecodeItem;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agendas, container, false);

        recyclerViewAgendas = (RecyclerView) view.findViewById(R.id.recycler_view_agendas);
        agendasAdapter = new AgendasAdapter();
        agendasAdapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_BUSCAR_REMOLQUES);
        wsTaskList.execute();
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

                        agendas.add(new Agendas(1,1,"Asignación Pendiente","Francisco Javier Díaz Saurett","",3));
                        agendas.add(new Agendas(2,2,"Descripción del estado actual","Nombre del Cliente","Nombre del Transportista",1));
                        agendas.add(new Agendas(3,3,"Asignación En proceso","Francisco Javier Díaz Saurett","Transportes CHAOL", 2));
                        agendas.add(new Agendas(4,4,"Asignación Pendiente de pago","Francisco Javier Díaz Saurett","Transportes SGOH",3));
                        agendas.add(new Agendas(1,1,"Asignación Pendiente","Francisco Javier Díaz Saurett","",4));

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
