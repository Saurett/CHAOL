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
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class PanelTractoresFragment extends Fragment implements View.OnClickListener {

    private Button btnTitulo;
    private TextView txtNumNoAsignado, txtNumAsignado;
    private static NavigationDrawerInterface activityInterface;
    private static FloatingActionButton fabTractores;
    private ProgressDialog pDialog;

    private static Usuarios _SESSION_USER;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drTractores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_panel_tractores, container, false);

        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_tractores);
        txtNumNoAsignado = (TextView) view.findViewById(R.id.item_num_no_asignado_panel_tractores);
        txtNumAsignado = (TextView) view.findViewById(R.id.item_num_asignado_panel_tractores);
        fabTractores = (FloatingActionButton) view.findViewById(R.id.fab_panel_tractores);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drTractores = database.getReference(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drTractores.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int countActivo = 0, countInactivo = 0;

                txtNumNoAsignado.setText(String.valueOf(countInactivo));
                txtNumAsignado.setText(String.valueOf(countActivo));

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psTractor : postSnapshot.child(Constants.FB_KEY_MAIN_TRACTORES).getChildren()) {

                        Tractores tractor = psTractor.getValue(Tractores.class);

                        if (tractor.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)) {
                            countActivo++;
                            txtNumNoAsignado.setText(String.valueOf(countActivo));
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
        fabTractores.setOnClickListener(this);

        return view;
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
            case R.id.btn_titulo_tractores:
                activityInterface.onChangeMainFragment(R.id.menu_item_tractores);
                break;
            case R.id.fab_panel_tractores:
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
