package com.indev.chaol.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.indev.chaol.R;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Estados;
import com.indev.chaol.models.TiposRemolques;
import com.indev.chaol.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesDatosGeneralesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnTitulo;
    private LinearLayout linearLayout;
    private EditText txtFechaSalida, txtHoraSalida;
    private Spinner spinnerTipoRemolque, spinnerOrigenEstado, spinnerDestinoEstado;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;

    private static List<String> tiposRemolquesList, tiposOrigenEstadoList, tipoDestinoEstadoList;
    private List<TiposRemolques> tiposRemolques;
    private List<Estados> tiposOrigenEstados, tiposDestinoEstados;

    private static DecodeExtraParams _MAIN_DECODE = new DecodeExtraParams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_generales_fletes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_datos_generales_fletes);
        linearLayout = (LinearLayout) view.findViewById(R.id.datos_generales_container);

        spinnerTipoRemolque = (Spinner) view.findViewById(R.id.spinner_dg_tipo_remolque);
        spinnerOrigenEstado = (Spinner) view.findViewById(R.id.spinner_dg_origen_estado);
        spinnerDestinoEstado = (Spinner) view.findViewById(R.id.spinner_dg_destino_estado);

        txtFechaSalida = (EditText) view.findViewById(R.id.txt_dg_fecha_salida);
        txtHoraSalida = (EditText) view.findViewById(R.id.txt_dg_hora_salida);

        btnTitulo.setOnClickListener(this);
        txtFechaSalida.setOnClickListener(this);
        txtHoraSalida.setOnClickListener(this);

        spinnerTipoRemolque.setOnItemSelectedListener(this);

        linearLayout.setVisibility(View.GONE);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTxtDate();
            }
        };

        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTxtTime();
            }
        };

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

        this.onPreRenderTiposRemolques();
        this.onPreRenderOrigenEstados();
        this.onPreRenderDestinoEstados();

        switch (_MAIN_DECODE.getAccionFragmento()) {
            case Constants.ACCION_EDITAR:
                /**Obtiene el item selecionado en el fragmento de lista**/
                //Fletes fletes = (Fletes) _MAIN_DECODE.getDecodeItem().getItemModel();

                /**Asigna valores del item seleccionado**/
                break;
            case Constants.ACCION_REGISTRAR:
                break;
        }
    }

    private void onPreRenderTiposRemolques() {
        this.onCargarTiposRemolques();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tiposRemolquesList);

                            /*
                            int selectionState = (null != _PROFILE_MANAGER.getAddressProfile().getIdItemState())
                                    ? _PROFILE_MANAGER.getAddressProfile().getIdItemState() : 0;
                                    */

        spinnerTipoRemolque.setAdapter(adapter);
        spinnerTipoRemolque.setSelection(0);
    }

    private void onPreRenderOrigenEstados() {
        this.onCargarOrigenEstados();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tiposOrigenEstadoList);

                            /*
                            int selectionState = (null != _PROFILE_MANAGER.getAddressProfile().getIdItemState())
                                    ? _PROFILE_MANAGER.getAddressProfile().getIdItemState() : 0;
                                    */

        spinnerOrigenEstado.setAdapter(adapter);
        spinnerOrigenEstado.setSelection(0);
    }

    private void onPreRenderDestinoEstados() {
        this.onCargarDestinoEstados();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tipoDestinoEstadoList);

                            /*
                            int selectionState = (null != _PROFILE_MANAGER.getAddressProfile().getIdItemState())
                                    ? _PROFILE_MANAGER.getAddressProfile().getIdItemState() : 0;
                                    */

        spinnerDestinoEstado.setAdapter(adapter);
        spinnerDestinoEstado.setSelection(0);
    }

    private void onCargarTiposRemolques() {
        tiposRemolquesList = new ArrayList<>();
        tiposRemolques = new ArrayList<>();
        tiposRemolquesList.add("Seleccione ...");

        //TODO Metodo para llamar al servidor
        tiposRemolquesList.add("Caja Refrijerada");
        tiposRemolquesList.add("Caja Seca");

        tiposRemolques.add(new TiposRemolques(1, "Caja Refrijerada"));
        tiposRemolques.add(new TiposRemolques(2, "Caja Seca"));

    }

    private void onCargarOrigenEstados() {
        tiposOrigenEstadoList = new ArrayList<>();
        tiposOrigenEstados = new ArrayList<>();
        tiposOrigenEstadoList.add("Seleccione ...");

        //TODO Metodo para llamar al servidor
        tiposOrigenEstadoList.add("CD MX");
        tiposOrigenEstadoList.add("Chihuahua");
        tiposOrigenEstadoList.add("Otro");

        tiposOrigenEstados.add(new Estados(1, "CD MX"));
        tiposOrigenEstados.add(new Estados(2, "Chihuahua"));
        tiposOrigenEstados.add(new Estados(3, "Otro"));
    }

    private void onCargarDestinoEstados() {
        tipoDestinoEstadoList = new ArrayList<>();
        tiposDestinoEstados = new ArrayList<>();
        tipoDestinoEstadoList.add("Seleccione ...");

        //TODO Metodo para llamar al servidor
        tipoDestinoEstadoList.add("CD MX");
        tipoDestinoEstadoList.add("Chihuahua");
        tipoDestinoEstadoList.add("Otro");

        tiposDestinoEstados.add(new Estados(1, "CD MX"));
        tiposDestinoEstados.add(new Estados(2, "Chihuahua"));
        tiposDestinoEstados.add(new Estados(3, "Otro"));
    }

    private void updateTxtDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        txtFechaSalida.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTxtTime() {
        String myFormat = "hh:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        txtHoraSalida.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_datos_generales_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.fab_clientes:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {

                }
                break;
            case R.id.txt_dg_fecha_salida:
                new DatePickerDialog(getContext(), R.style.MyCalendarTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.txt_dg_hora_salida:
                new TimePickerDialog(getContext(), R.style.MyCalendarTheme, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        false).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
