package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainActivity;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 24/02/2017.
 */

public class PanelFletesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = PanelFletesFragment.class.getName();

    private Button btnTitulo;
    private TextView txtNumPorCotizar, txtNumEsperandoTransportista, txtNumTransportistaPorConfirmar, txtNumUnidadesPorAsignar, txtNumEnvioPorIniciar, txtNumEnProgreso, txtNumEntregado, txtNumFinalizado, txtNumCancelado;
    private LinearLayout linearFletePorCotizar, linearEsperandoTransportista, linearTransportistaPorConfirmar, linearUnidadesPorAsignar, linearEnvioPorIniciar, linearEnProgreso, linearEntregado, linearFinalizado, linearCancelado;
    private static NavigationDrawerInterface activityInterface;
    private FloatingActionButton fabFletes;
    private ProgressDialog pDialog;

    private static Usuarios _SESSION_USER;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drClientes;

    private String firebaseIDCliente;
    private String firebaseIDransportista;
    private String firebaseIDChofer;
    private List<String> firebaseIDTransportistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_panel_fletes, container, false);

        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        txtNumPorCotizar = (TextView) view.findViewById(R.id.item_num_flete_por_cotizar);
        txtNumEsperandoTransportista = (TextView) view.findViewById(R.id.item_num_esperando_transportista);
        txtNumTransportistaPorConfirmar = (TextView) view.findViewById(R.id.item_num_transportista_por_confirmar);
        txtNumUnidadesPorAsignar = (TextView) view.findViewById(R.id.item_num_unidades_por_asignar);
        txtNumEnvioPorIniciar = (TextView) view.findViewById(R.id.item_num_envio_por_iniciar);
        txtNumEnProgreso = (TextView) view.findViewById(R.id.item_num_en_progreso);
        txtNumEntregado = (TextView) view.findViewById(R.id.item_num_entregado);
        txtNumFinalizado = (TextView) view.findViewById(R.id.item_num_finalizado);
        txtNumCancelado = (TextView) view.findViewById(R.id.item_num_cancelado);

        linearFletePorCotizar = (LinearLayout) view.findViewById(R.id.linear_item_flete_por_cotizar);
        linearEsperandoTransportista = (LinearLayout) view.findViewById(R.id.linear_item_esperando_transportista);
        linearTransportistaPorConfirmar = (LinearLayout) view.findViewById(R.id.linear_item_transportista_por_confirmar);
        linearUnidadesPorAsignar = (LinearLayout) view.findViewById(R.id.linear_unidades_por_asignar);
        linearEnvioPorIniciar = (LinearLayout) view.findViewById(R.id.linear_item_envio_por_iniciar);
        linearEnProgreso = (LinearLayout) view.findViewById(R.id.linear_item_en_progreso);
        linearEntregado = (LinearLayout) view.findViewById(R.id.linear_item_entregado);
        linearFinalizado = (LinearLayout) view.findViewById(R.id.linear_item_finalizado);
        linearCancelado = (LinearLayout) view.findViewById(R.id.linear_item_cancelado);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_fletes);
        fabFletes = (FloatingActionButton) view.findViewById(R.id.fab_panel_fletes);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drClientes = database.getReference(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        drClientes.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count = 0, countEsperandoTransportista = 0, countTransportistaPorConfirmar = 0;
                int countUnidadesPorAsignar = 0, countEnvioPorIniciar = 0, countEnProgreso = 0;
                int countEntregado = 0, countFinalizado = 0, countCancelado = 0;

                firebaseIDCliente = "";
                firebaseIDransportista = "";
                firebaseIDChofer = "";
                firebaseIDTransportistas = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.i(TAG,"addValueEventListener Flete " + postSnapshot.getKey());

                    Fletes flete = postSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);
                    Bodegas bodegaCarga = postSnapshot.child(Constants.FB_KEY_MAIN_BODEGA_DE_CARGA).getValue(Bodegas.class);

                    for (DataSnapshot psInteresados : postSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTAS_INTERESADOS).getChildren()) {
                        firebaseIDTransportistas.add(psInteresados.getKey());
                    }

                    for (DataSnapshot psTransportista : postSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                        firebaseIDransportista = psTransportista.getKey();
                    }

                    for (DataSnapshot psChofer : postSnapshot.child(Constants.FB_KEY_MAIN_CHOFER_SELECCIONADO).getChildren()) {
                        firebaseIDChofer = psChofer.getKey();
                    }

                    if (null == bodegaCarga) continue;

                    if (null == flete) continue;

                    firebaseIDCliente = bodegaCarga.getFirebaseIdDelCliente();

                    //TODO transportistaSeleccionado/firebaseID

                    switch (flete.getEstatus()) {
                        case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:

                            if (checkInvisibleCount()) continue;

                            count++;
                            txtNumPorCotizar.setText(String.valueOf(count));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:

                            if (checkInvisibleCount()) continue;

                            countEsperandoTransportista++;
                            txtNumEsperandoTransportista.setText(String.valueOf(countEsperandoTransportista));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:

                            if (checkInvisibleCount()) continue;

                            countTransportistaPorConfirmar++;
                            txtNumTransportistaPorConfirmar.setText(String.valueOf(countTransportistaPorConfirmar));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:

                            if (checkInvisibleCount()) continue;

                            countUnidadesPorAsignar++;
                            txtNumUnidadesPorAsignar.setText(String.valueOf(countUnidadesPorAsignar));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:

                            if (checkInvisibleCount()) continue;

                            countEnvioPorIniciar++;
                            txtNumEnvioPorIniciar.setText(String.valueOf(countEnvioPorIniciar));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:

                            if (checkInvisibleCount()) continue;

                            countEnProgreso++;
                            txtNumEnProgreso.setText(String.valueOf(countEnProgreso));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:

                            if (checkInvisibleCount()) continue;

                            countEntregado++;
                            txtNumEntregado.setText(String.valueOf(countEntregado));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:

                            if (checkInvisibleCount()) continue;

                            countFinalizado++;
                            txtNumFinalizado.setText(String.valueOf(countFinalizado));
                            break;
                        case Constants.FB_KEY_ITEM_STATUS_CANCELADO:

                            if (checkInvisibleCount()) continue;

                            countCancelado++;
                            txtNumCancelado.setText(String.valueOf(countCancelado));
                            break;
                        default:
                            break;
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
        fabFletes.setOnClickListener(this);

        this.onPreRender();


        return view;
    }

    private boolean checkInvisibleCount() {
        boolean participation = false;

        switch (_SESSION_USER.getTipoDeUsuario()) {
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:

                participation = (!_SESSION_USER.getFirebaseId().equals(firebaseIDCliente));

                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:

                participation = (!firebaseIDTransportistas.contains(_SESSION_USER.getFirebaseId()));

                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:

                participation = (!_SESSION_USER.getFirebaseId().equals(firebaseIDChofer));

                break;
            default:
                /**Sin restricciones para el admin**/
                break;
        }


        return  participation;
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

    private void onPreRender() {

        switch (_SESSION_USER.getTipoDeUsuario()) {
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                /**Sin condiciones para el cliente**/
                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                linearFletePorCotizar.setVisibility(View.GONE);
                linearEnProgreso.setVisibility(View.GONE);
                linearEntregado.setVisibility(View.GONE);
                linearFinalizado.setVisibility(View.GONE);
                linearCancelado.setVisibility(View.GONE);

                fabFletes.setVisibility(View.INVISIBLE);
                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                linearFletePorCotizar.setVisibility(View.GONE);
                linearEsperandoTransportista.setVisibility(View.GONE);
                linearTransportistaPorConfirmar.setVisibility(View.GONE);
                linearUnidadesPorAsignar.setVisibility(View.GONE);
                linearEntregado.setVisibility(View.GONE);
                linearFinalizado.setVisibility(View.GONE);
                linearCancelado.setVisibility(View.GONE);

                fabFletes.setVisibility(View.INVISIBLE);
                break;
            default:
                /**Sin restricciones para el admin**/
                //linearFletePorCotizar.setVisibility(View.GONE);
                //linearEsperandoTransportista.setVisibility(View.GONE);
                //linearTransportistaPorConfirmar.setVisibility(View.GONE);
                //linearUnidadesPorAsignar.setVisibility(View.GONE);
                //linearEnvioPorIniciar.setVisibility(View.GONE);
                //linearEnProgreso.setVisibility(View.GONE);
                //linearEntregado.setVisibility(View.GONE);
                //linearFinalizado.setVisibility(View.GONE);
                //linearCancelado.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_titulo_fletes:
                activityInterface.onChangeMainFragment(R.id.menu_item_agenda);
                break;
            case R.id.fab_panel_fletes:
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
