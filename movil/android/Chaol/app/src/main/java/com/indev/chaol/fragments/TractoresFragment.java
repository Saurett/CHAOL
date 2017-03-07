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
import com.indev.chaol.adapters.TractoresAdapter;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Choferes;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tractores, container, false);

        recyclerViewTractores = (RecyclerView) view.findViewById(R.id.recycler_view_tractores);
        tractoresAdapter = new TractoresAdapter();
        tractoresAdapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_BUSCAR_TRACTORES);
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
        tractoresList.remove(decodeItem.getPosition());
        adapter.removeItem(decodeItem.getPosition());
    }


    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<Tractores> tempTractoresList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempTractoresList = new ArrayList<>();
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
                    case Constants.WS_KEY_BUSCAR_TRACTORES:

                        tempTractoresList = new ArrayList<>();
                        List<Tractores> tractores = new ArrayList<>();

                        tractores.add(new Tractores("John Deere"));
                        tractores.add(new Tractores("Massey Ferguson"));
                        tractores.add(new Tractores("Lamborghini"));

                        tempTractoresList.addAll(tractores);

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
                tractoresList = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempTractoresList.size() > 0) {
                        tractoresList.addAll(tempTractoresList);
                        tractoresAdapter.addAll(tractoresList);

                        recyclerViewTractores.setAdapter(tractoresAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerViewTractores.setLayoutManager(linearLayoutManager);

                    } else {
                        Toast.makeText(getActivity(), "La lista se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La lista  se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }

                adapter = tractoresAdapter;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
