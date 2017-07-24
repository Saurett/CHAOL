package com.indev.chaol.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;

import static android.view.View.GONE;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesAsignacionFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private static Button btnTitulo, btnSolicitarViaje, btnCancelarViaje;
    private static TextView txtEstatusTransportista;
    private LinearLayout linearLayout;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    private static MainFletes _mainFletesActual;
    private static MainRegisterActivity activityInterface;

    private static int _idOrigenView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignacion_fletes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_datos_asignacion_fletes);
        btnSolicitarViaje = (Button) view.findViewById(R.id.btn_solicitar_viaje_asignacion);
        btnCancelarViaje = (Button) view.findViewById(R.id.btn_cancelar_viaje_asignacion);
        txtEstatusTransportista = (TextView) view.findViewById(R.id.txt_asignacion_estatus_transportista);

        linearLayout = (LinearLayout) view.findViewById(R.id.asignacion_container);

        btnTitulo.setOnClickListener(this);
        btnSolicitarViaje.setOnClickListener(this);
        btnCancelarViaje.setOnClickListener(this);
        linearLayout.setVisibility(GONE);

        RegistroFletesFragment.setFrameAsignacion(View.GONE);

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
                this.onPreRenderEditar();
                break;
            case Constants.ACCION_REGISTRAR:
                break;
        }
    }

    private void onPreRenderEditar() {
        /**Obtiene el item selecionado en el fragmento de lista**/
        Agendas agenda = (Agendas) _MAIN_DECODE.getDecodeItem().getItemModel();

        DatabaseReference dbFlete =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                        .child(agenda.getFirebaseID());

        dbFlete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);
                _mainFletesActual = new MainFletes();

                _mainFletesActual.setFlete(flete);

                switch (_SESSION_USER.getTipoDeUsuario()) {
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_ADMINISTRADOR:
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                        break;
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                        break;
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                        btnSolicitarViaje.setVisibility(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                        break;
                }

                switch (_mainFletesActual.getFlete().getEstatus()) {
                    case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                        RegistroFletesFragment.setFrameAsignacion(View.GONE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                        RegistroFletesFragment.setFrameAsignacion(View.VISIBLE);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_datos_asignacion_fletes:

                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? GONE : View.VISIBLE);

                if (linearLayout.getVisibility() == View.VISIBLE) {

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction mainFragment = fragmentManager.beginTransaction();
                    mainFragment.replace(R.id.listado_asignaciones_container, new AsignacionTransportistasFragment(), Constants.ASIGNACION_TRANSPORTISTAS_FLETES_CONTAINER);
                    mainFragment.commit();

                }
                break;
            case R.id.btn_solicitar_viaje_asignacion:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea solicitar viaje?");
                }
                break;
            case R.id.btn_cancelar_viaje_asignacion:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    _idOrigenView = v.getId();
                    this.showQuestion("¿Esta seguro que desea cancelar viaje?");
                }
                break;
        }
    }

    public static void showMessageAsignacion(int visible, String message) {
        txtEstatusTransportista.setVisibility(visible);
        txtEstatusTransportista.setText(message);
    }

    public static void showCancelAsignacion(int visible) {
        btnCancelarViaje.setVisibility(visible);
        btnSolicitarViaje.setVisibility((visible == View.VISIBLE) ? GONE : View.VISIBLE);
    }

    public static void showCancelAsignacionSolo(int visible) {
        btnCancelarViaje.setVisibility(visible);
    }

    private void showQuestion(String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle(btnTitulo.getText());
        ad.setMessage(message);
        ad.setCancelable(false);
        ad.setNegativeButton(getString(R.string.default_alert_dialog_cancelar), this);
        ad.setPositiveButton(getString(R.string.default_alert_dialog_aceptar), this);
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                switch (_idOrigenView) {
                    case R.id.btn_solicitar_viaje_asignacion:
                        this.validationEditer();
                        break;
                    case R.id.btn_cancelar_viaje_asignacion:
                        this.validationCancelar();
                        break;
                }
                break;
        }
    }

    private void validationCancelar() {
        Boolean authorized = true;

        //Validar el estatus

        if (authorized) {
            this.updateCancelacion();
        }
    }

    private void validationEditer() {
        Boolean authorized = true;

        for (Transportistas transportista : AsignacionTransportistasFragment.getTransportistasList()) {
            if (transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                authorized = false;
                break;
            }
        }

        if (authorized) {
            this.updateAsignacion();
        } else {
            Toast.makeText(getContext(), "Intente nuevamente ...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAsignacion() {
        final Fletes flete = _mainFletesActual.getFlete();

        String estatus = (Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA.equals(flete.getEstatus())
                ? Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR : flete.getEstatus());

        flete.setEstatus(estatus);

        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(_SESSION_USER.getFirebaseId())
                        .child(Constants.FB_KEY_ITEM_TRANSPORTISTA);

        dbTransportista.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Transportistas transportista = dataSnapshot.getValue(Transportistas.class);

                Transportistas transportistaSeleccionado = new Transportistas();

                transportistaSeleccionado.setNombre(transportista.getNombre());
                transportistaSeleccionado.setFirebaseId(_SESSION_USER.getFirebaseId());

                activityInterface.updateSolicitudTransportistaInteresado(flete, transportistaSeleccionado);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateCancelacion() {
        final Fletes flete = _mainFletesActual.getFlete();

        int total = 0;

        for (Transportistas transportista : AsignacionTransportistasFragment.getTransportistasList()) {
            if (!transportista.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                total++;
            }
        }

        flete.setEstatus((total == 0) ? Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA : flete.getEstatus());

        Transportistas transportistaSeleccionado = AsignacionTransportistasFragment.getTransportistaSeleccionado();

        if (null != transportistaSeleccionado) {
            /**Elimina todos los transportistas, por que el transportista seleecionado limpia las condiciones atte carlos de la mora**/

            if (transportistaSeleccionado.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                flete.setEstatus(Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA);
                activityInterface.removeSolicitudTransportistaSeleccionado(flete, _SESSION_USER.getFirebaseId());
            } else {
                /**Elimina solo al transportista seleccionado*/
                activityInterface.removeSolicitudTransportistaInteresado(flete, _SESSION_USER.getFirebaseId());
            }
        } else {
            /**Elimina solo al transportista seleccionado*/
            activityInterface.removeSolicitudTransportistaInteresado(flete, _SESSION_USER.getFirebaseId());
        }
    }


}
