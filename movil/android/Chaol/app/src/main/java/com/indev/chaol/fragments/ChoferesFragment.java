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
import com.indev.chaol.adapters.ChoferesAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class ChoferesFragment extends Fragment implements View.OnClickListener {

    private static List<Choferes> choferesList;
    private static RecyclerView recyclerViewChoferes;
    private ChoferesAdapter choferesAdapter;
    private static ChoferesAdapter adapter;
    private ProgressDialog pDialog;
    private static NavigationDrawerInterface navigationDrawerInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choferes, container, false);

        recyclerViewChoferes = (RecyclerView) view.findViewById(R.id.recycler_view_choferes);
        choferesAdapter = new ChoferesAdapter();
        choferesAdapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_BUSCAR_CHOFERES);
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
        choferesList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<Choferes> tempChoferesList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempChoferesList = new ArrayList<>();
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
                    case Constants.WS_KEY_BUSCAR_CHOFERES:

                        tempChoferesList = new ArrayList<>();
                        List<Choferes> choferes = new ArrayList<>();

                        choferes.add(new Choferes("Lola la trailera"));
                        choferes.add(new Choferes("Nacho ghost raider"));
                        choferes.add(new Choferes("Roman Kenshi"));
                        choferes.add(new Choferes("Jorge Strayer"));

                        tempChoferesList.addAll(choferes);

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
                choferesList = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempChoferesList.size() > 0) {
                        choferesList.addAll(tempChoferesList);
                        choferesAdapter.addAll(choferesList);

                        recyclerViewChoferes.setAdapter(choferesAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerViewChoferes.setLayoutManager(linearLayoutManager);

                    } else {
                        Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La lista  se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }

                adapter = choferesAdapter;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
