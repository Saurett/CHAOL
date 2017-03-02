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
import com.indev.chaol.adapters.RemolquesAdapter;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class RemolquesFragment extends Fragment implements View.OnClickListener {

    private static List<Remolques> remolquesList;
    private static RecyclerView recyclerViewRemolques;
    private RemolquesAdapter remolquesAdapter;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choferes, container, false);

        recyclerViewRemolques = (RecyclerView) view.findViewById(R.id.recycler_view_remolques);
        remolquesAdapter = new RemolquesAdapter();
        remolquesAdapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_BUSCAR_REMOLQUES);
        wsTaskList.execute();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    @Override
    public void onClick(View v) {

    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<Remolques> tempRemolquesList;
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
                        List<Remolques> remolques = new ArrayList<>();

                        tempRemolquesList.addAll(remolques);

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
                remolquesList = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempRemolquesList.size() > 0) {
                        remolquesList.addAll(tempRemolquesList);
                        remolquesAdapter.addAll(remolquesList);

                        recyclerViewRemolques.setAdapter(remolquesAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerViewRemolques.setLayoutManager(linearLayoutManager);

                    } else {
                        Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La lista  se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}