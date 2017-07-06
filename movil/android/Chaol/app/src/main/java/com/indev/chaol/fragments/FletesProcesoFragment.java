package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesProcesoFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = FletesProcesoFragment.class.getName();

    private Button btnTitulo, btnIniciar, btnEntregado, btnRecibido;
    private TextView txtEstatus;
    private LinearLayout linearLayout;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    private static MainRegisterActivity activityInterface;

    private static MainFletes _mainFletesActual;
    private int _idOrigenView;

    /**
     * Declaraciones para Firebase
     **/
    DatabaseReference dbFlete;
    private ValueEventListener listenerFletes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proceso_fletes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_proceso_fletes);
        btnIniciar = (Button) view.findViewById(R.id.btn_iniciar_proceso);
        btnEntregado = (Button) view.findViewById(R.id.btn_entregado_proceso);
        btnRecibido = (Button) view.findViewById(R.id.btn_recibido_proceso);

        linearLayout = (LinearLayout) view.findViewById(R.id.proceso_container);

        txtEstatus = (TextView) view.findViewById(R.id.txt_proceso_estatus);

        btnTitulo.setOnClickListener(this);
        btnIniciar.setOnClickListener(this);
        btnEntregado.setOnClickListener(this);
        btnRecibido.setOnClickListener(this);

        linearLayout.setVisibility(View.GONE);

        this.onPreRender();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != listenerFletes) dbFlete.removeEventListener(listenerFletes);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityInterface = (MainRegisterActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    public void onPreRender() {
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                onPreRenderEditar();
                break;
            case Constants.ACCION_REGISTRAR:
                break;
        }
    }

    private void onPreRenderEditar() {

        /**Obtiene el item selecionado en el fragmento de lista**/
        Agendas agenda = (Agendas) _MAIN_DECODE.getDecodeItem().getItemModel();

        dbFlete = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                .child(agenda.getFirebaseID());

        listenerFletes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(TAG, "onPreRenderEditar: " + dataSnapshot.getKey());

                _mainFletesActual = new MainFletes();

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);

                for (DataSnapshot dsTransportistaSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                    Transportistas transportistaSeleccionado = dsTransportistaSeleccionado.getValue(Transportistas.class);

                    _mainFletesActual.setTransportistaSeleccionado(transportistaSeleccionado);

                    break;
                }

                for (DataSnapshot dsChoferSeleccionado : dataSnapshot.child(Constants.FB_KEY_MAIN_CHOFER_SELECCIONADO).getChildren()) {
                    Choferes choferSeleccionado = dsChoferSeleccionado.getValue(Choferes.class);

                    _mainFletesActual.setChoferSeleccionado(choferSeleccionado);

                    break;
                }

                txtEstatus.setText(Constants.TITLE_ESTATUS_FLETES.get(flete.getEstatus()));

                switch (flete.getEstatus()) {
                    case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);

                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                                btnIniciar.setVisibility(View.VISIBLE);
                                break;
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                                Transportistas transportista = _mainFletesActual.getTransportistaSeleccionado();

                                if (null != transportista) {
                                    if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                                        btnIniciar.setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                                Choferes chofer = _mainFletesActual.getChoferSeleccionado();

                                if (null != chofer) {
                                    if (chofer.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                                        btnIniciar.setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                        }

                        break;
                    case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);

                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                                btnEntregado.setVisibility(View.VISIBLE);
                                break;
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                                Transportistas transportista = _mainFletesActual.getTransportistaSeleccionado();

                                if (null != transportista) {
                                    if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                                        btnEntregado.setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                                Choferes chofer = _mainFletesActual.getChoferSeleccionado();

                                if (null != chofer) {
                                    if (chofer.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                                        btnEntregado.setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);

                        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_ADMINISTRADOR)
                                || _SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE)) {
                            btnRecibido.setVisibility(View.VISIBLE);
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                        btnIniciar.setVisibility(View.GONE);
                        btnEntregado.setVisibility(View.GONE);
                        btnRecibido.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

                _mainFletesActual.setFlete(flete);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbFlete.addValueEventListener(listenerFletes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceso_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_iniciar_proceso:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea iniciar envio?");
                }
                break;
            case R.id.btn_entregado_proceso:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea entregar envio?");
                }
                break;
            case R.id.btn_recibido_proceso:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea recibir envio?");
                }
                break;
        }
    }

    private void showQuestion(String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle(btnTitulo.getText());
        ad.setMessage(message);
        ad.setCancelable(false);
        ad.setNegativeButton(getString(R.string.default_alert_dialog_no), this);
        ad.setPositiveButton(getString(R.string.default_alert_dialog_si), this);
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                switch (_idOrigenView) {
                    case R.id.btn_iniciar_proceso:
                        this.validationIniciar();
                        break;
                    case R.id.btn_entregado_proceso:
                        this.validationEntregado();
                        break;
                    case R.id.btn_recibido_proceso:
                        this.validationRecibido();
                        break;
                }

                break;
        }
    }

    private void validationIniciar() {
        Boolean authorized = true;

        String estatus = _mainFletesActual.getFlete().getEstatus();

        if (!estatus.equals(Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR)) {
            authorized = false;
        }

        if (authorized) {
            this.updateIniciar();
        } else {
            Toast.makeText(getContext(), "El estatus del flete a cambiado a " + estatus,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void validationEntregado() {
        Boolean authorized = true;

        String estatus = _mainFletesActual.getFlete().getEstatus();

        if (!estatus.equals(Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO)) {
            authorized = false;
        }

        if (authorized) {
            this.updateEntregado();
        } else {
            Toast.makeText(getContext(), "El estatus del flete a cambiado a " + estatus,
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void validationRecibido() {
        Boolean authorized = true;

        String estatus = _mainFletesActual.getFlete().getEstatus();

        if (!estatus.equals(Constants.FB_KEY_ITEM_STATUS_ENTREGADO)) {
            authorized = false;
        }

        if (authorized) {
            this.updateRecibido();
        } else {
            Toast.makeText(getContext(), "El estatus del flete a cambiado a " + estatus,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateIniciar() {
        Fletes flete = _mainFletesActual.getFlete();

        flete.setEstatus(Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO);

        activityInterface.updateSolicitudEnvio(flete);
    }

    private void updateEntregado() {
        Fletes flete = _mainFletesActual.getFlete();

        flete.setEstatus(Constants.FB_KEY_ITEM_STATUS_ENTREGADO);

        activityInterface.updateSolicitudEnvio(flete);
    }

    private void updateRecibido() {
        Fletes flete = _mainFletesActual.getFlete();

        flete.setEstatus(Constants.FB_KEY_ITEM_STATUS_FINALIZADO);

        activityInterface.updateSolicitudEnvio(flete);
    }


}
