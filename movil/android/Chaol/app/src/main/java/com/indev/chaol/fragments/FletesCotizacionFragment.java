package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Time;
import java.util.Calendar;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesCotizacionFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private Button btnTitulo, btnGuardar;
    private EditText txtPrecio;
    private LinearLayout linearLayout;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    private static MainFletes _mainFletesActual;

    private static MainRegisterActivity activityInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotizacion_fletes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_cotizacion_fletes);
        btnGuardar = (Button) view.findViewById(R.id.btn_guardar_cotizacion);
        txtPrecio = (EditText) view.findViewById(R.id.txt_cotización_precio);
        linearLayout = (LinearLayout) view.findViewById(R.id.cotizacion_container);

        btnTitulo.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        linearLayout.setVisibility(View.GONE);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        this.onPreRender();

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
            activityInterface = (MainRegisterActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    public void onPreRender() {
        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                this.onPreRenderEditar();
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

                txtPrecio.setText(_mainFletesActual.getFlete().getPrecio());

                if (!txtPrecio.getText().toString().isEmpty()) {
                    btnGuardar.setText("Actualizar");
                }

                switch (_SESSION_USER.getTipoDeUsuario()) {
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_ADMINISTRADOR:
                        btnGuardar.setVisibility(View.VISIBLE);
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
            case R.id.btn_cotizacion_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_guardar_cotizacion:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                }
                break;
        }
    }

    private void showQuestion() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle(btnTitulo.getText());
        ad.setMessage("¿Esta seguro que desea editar?");
        ad.setCancelable(false);
        ad.setNegativeButton(getString(R.string.default_alert_dialog_cancelar), this);
        ad.setPositiveButton(getString(R.string.default_alert_dialog_aceptar), this);
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                this.validationEditer();
                break;
        }
    }

    private void validationEditer() {
        Boolean authorized = true;

        String precio = txtPrecio.getText().toString();

        if (TextUtils.isEmpty(precio)) {
            txtPrecio.setError("El campo es obligatorio", null);
            txtPrecio.requestFocus();
            authorized = false;
        }

        if (authorized) {
            this.updateCotizacion();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCotizacion() {
        Fletes flete = _mainFletesActual.getFlete();

        flete.setPrecio(txtPrecio.getText().toString().trim());

        flete.setFechaDeCreacion(_mainFletesActual.getFlete().getFechaDeCreacion());
        flete.setFirebaseId(_mainFletesActual.getFlete().getFirebaseId());

        String estatus = (Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR.equals(flete.getEstatus())
                ? Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA : flete.getEstatus());

        flete.setEstatus(estatus);

        activityInterface.updateSolicitudCotizacion(flete);
    }
}
