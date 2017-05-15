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
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class PanelClientesFragment extends Fragment implements View.OnClickListener {

    private Button btnTitulo;
    private TextView txtNumClientes;
    private static NavigationDrawerInterface activityInterface;
    private static FloatingActionButton fabClientes;
    private ProgressDialog pDialog;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_panel_clientes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);
        txtNumClientes = (TextView) view.findViewById(R.id.item_num_panel_clientes);
        fabClientes = (FloatingActionButton) view.findViewById(R.id.fab_panel_clientes);

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
                txtNumClientes.setText(String.valueOf(count));

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Clientes cliente = postSnapshot.child(Constants.FB_KEY_ITEM_CLIENTE).getValue(Clientes.class);

                    if (cliente.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)) {
                        count++;
                        txtNumClientes.setText(String.valueOf(count));
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
        fabClientes.setOnClickListener(this);

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
            case R.id.btn_titulo_clientes:
                activityInterface.onChangeMainFragment(R.id.menu_item_clientes);
                break;
            case R.id.fab_panel_clientes:
                DecodeExtraParams extraParams = new DecodeExtraParams();

                extraParams.setTituloActividad(getString(Constants.TITLE_ACTIVITY.get(view.getId())));
                extraParams.setTituloFormulario(getString(R.string.default_form_title_new));
                extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
                extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(view.getId()));

                Intent intent = new Intent(getActivity(), MainRegisterActivity.class);
                intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
                startActivity(intent);
                break;
        }
    }
}
