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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.R;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesProcesoFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = FletesProcesoFragment.class.getName();

    private Button btnTitulo;
    private TextView txtEstatus;
    private LinearLayout linearLayout;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    private static MainFletes _mainFletesActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proceso_fletes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_proceso_fletes);
        linearLayout = (LinearLayout) view.findViewById(R.id.proceso_container);

        txtEstatus = (TextView) view.findViewById(R.id.txt_proceso_estatus);

        btnTitulo.setOnClickListener(this);
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

        DatabaseReference dbFlete =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                        .child(agenda.getFirebaseID());

        final ProgressDialog pDialogRender = new ProgressDialog(getContext());
        pDialogRender.setMessage(getString(R.string.default_loading_msg));
        pDialogRender.setIndeterminate(false);
        pDialogRender.setCancelable(false);
        pDialogRender.show();

        dbFlete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(TAG, "onPreRenderEditar: " + dataSnapshot.getKey());

                _mainFletesActual = new MainFletes();

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);

                txtEstatus.setText(Constants.TITLE_ESTATUS_FLETES.get(flete.getEstatus()));

                _mainFletesActual.setFlete(flete);

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceso_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {

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

                break;
        }
    }
}
