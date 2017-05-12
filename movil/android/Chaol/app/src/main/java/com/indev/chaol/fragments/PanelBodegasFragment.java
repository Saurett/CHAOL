package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class PanelBodegasFragment extends Fragment implements View.OnClickListener {

    private Button btnTitulo;
    private TextView txtNumBodegas;
    private static NavigationDrawerInterface activityInterface;
    private static FloatingActionButton fabBodegas;
    private ProgressDialog pDialog;

    private static Usuarios _SESSION_USER;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_panel_bodegas, container, false);

        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_bodegas);
        txtNumBodegas = (TextView) view.findViewById(R.id.item_num_panel_bodegas);
        fabBodegas = (FloatingActionButton) view.findViewById(R.id.fab_panel_bodegas);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drClientes = database.getReference(Constants.FB_KEY_MAIN_CLIENTES);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drClientes.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count = 0;
                txtNumBodegas.setText(String.valueOf(count));

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psBodegas : postSnapshot.child(Constants.FB_KEY_MAIN_BODEGAS).getChildren()) {

                        Bodegas bodega = psBodegas.getValue(Bodegas.class);

                        if (bodega.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)) {
                            count++;
                            txtNumBodegas.setText(String.valueOf(count));
                        }

                    }
                }

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });

        btnTitulo.setOnClickListener(this);
        fabBodegas.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityInterface = (NavigationDrawerInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_titulo_bodegas:
                activityInterface.onChangeMainFragment(R.id.menu_item_bodegas);
                break;
            case R.id.fab_panel_bodegas:
                DecodeExtraParams extraParams = new DecodeExtraParams();

                extraParams.setTituloActividad(getString(Constants.TITLE_ACTIVITY.get(view.getId())));
                extraParams.setTituloFormulario(getString(R.string.default_form_title_new));
                extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
                extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(view.getId()));

                Intent intent = new Intent(getActivity(), MainRegisterActivity.class);
                intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
                intent.putExtra(Constants.KEY_SESSION_USER, _SESSION_USER);
                startActivity(intent);
                break;
        }
    }
}
