package com.indev.chaol.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.MainRegisterInterface;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Estados;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by saurett on 24/02/2017.
 */

public class ResumenFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnTitulo;
    private LinearLayout linearLayoutClientes, linearLayout;
    private EditText txtCliente, txtFechaSalida, txtHoraSalida, txtCarga;
    private EditText txtCiudadCarga, txtColoniaCarga, txtCodigoPostalCarga, txtCalleCarga, txtNumIntCarga, txtNumExtCarga,txtPrecio;
    private EditText txtCiudadDescarga, txtColoniaDescarga, txtCodigoPostalDescarga, txtCalleDescarga, txtNumIntDescarga, txtNumExtDescarga;
    private EditText txtTransportista, txtChofer, txtLicencia, txtTractor, txtPlacasTracor, txtRemolque, txtPlacasRemolque;
    private Spinner spinnerBodegaCarga, spinnerBodegaDescarga, spinnerOrigenEstado, spinnerDestinoEstado;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;

    private static List<String> bodegasCargasList;
    private List<Bodegas> bodegasCargas;
    private static Bodegas _bodegaCargaActual;

    private static List<String> bodegasDescargasList;
    private List<Bodegas> bodegasDescargas;
    private static Bodegas _bodegaDescargaActual;

    private String firebaseIdCliente;

    private static List<String> tiposOrigenEstadoList, tipoDestinoEstadoList;
    private List<Estados> tiposOrigenEstados, tiposDestinoEstados;

    private MainRegisterInterface activityInterface;

    private static Usuarios _SESSION_USER;
    private static DecodeExtraParams _MAIN_DECODE;

    /**
     * Declaraciones para Firebase
     **/
    private static MainFletes _mainFletesActual;

    private FirebaseDatabase database;
    private DatabaseReference drClientes;
    private DatabaseReference drBodegaCarga;
    private DatabaseReference drBodegaDescarga;
    private DatabaseReference dbFlete;
    private ValueEventListener listenerFletes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_resumen_fletes);

        linearLayoutClientes = (LinearLayout) view.findViewById(R.id.item_resumen_cliente);
        linearLayout = (LinearLayout) view.findViewById(R.id.resumen_container);

        txtCliente = (EditText) view.findViewById(R.id.txt_resumen_cliente);
        spinnerBodegaCarga = (Spinner) view.findViewById(R.id.spinner_resumen_bodega_carga);
        spinnerBodegaDescarga = (Spinner) view.findViewById(R.id.spinner_resumen_bodega_descarga);
        spinnerOrigenEstado = (Spinner) view.findViewById(R.id.spinner_resumen_origen_estado);
        spinnerDestinoEstado = (Spinner) view.findViewById(R.id.spinner_resumen_destino_estado);

        txtFechaSalida = (EditText) view.findViewById(R.id.txt_resumen_fecha_salida);
        txtHoraSalida = (EditText) view.findViewById(R.id.txt_resumen_hora_salida);
        txtCarga = (EditText) view.findViewById(R.id.txt_resumen_carga);

        txtCiudadCarga = (EditText) view.findViewById(R.id.txt_resumen_ciudad_carga);
        txtColoniaCarga = (EditText) view.findViewById(R.id.txt_resumen_colonia_carga);
        txtCodigoPostalCarga = (EditText) view.findViewById(R.id.txt_resumen_codigo_postal_carga);
        txtCalleCarga = (EditText) view.findViewById(R.id.txt_resumen_calle_carga);
        txtNumIntCarga = (EditText) view.findViewById(R.id.txt_resumen_num_int_carga);
        txtNumExtCarga = (EditText) view.findViewById(R.id.txt_resumen_num_ext_carga);
        txtPrecio = (EditText) view.findViewById(R.id.txt_resumen_cotizacion);

        txtCiudadDescarga = (EditText) view.findViewById(R.id.txt_resumen_ciudad_destino);
        txtColoniaDescarga = (EditText) view.findViewById(R.id.txt_resumen_colonia_destino);
        txtCodigoPostalDescarga = (EditText) view.findViewById(R.id.txt_resumen_codigo_postal_destino);
        txtCalleDescarga = (EditText) view.findViewById(R.id.txt_resumen_calle_destino);
        txtNumIntDescarga = (EditText) view.findViewById(R.id.txt_resumen_num_int_destino);
        txtNumExtDescarga = (EditText) view.findViewById(R.id.txt_resumen_num_ext_destino);

        txtTransportista = (EditText) view.findViewById(R.id.txt_resumen_transportista);
        txtChofer = (EditText) view.findViewById(R.id.txt_resumen_chofer);
        txtLicencia = (EditText) view.findViewById(R.id.txt_resumen_licencia);
        txtTractor = (EditText) view.findViewById(R.id.txt_resumen_tractor);
        txtPlacasTracor = (EditText) view.findViewById(R.id.txt_resumen_placas_tractor);
        txtRemolque = (EditText) view.findViewById(R.id.txt_resumen_remolque);
        txtPlacasRemolque = (EditText) view.findViewById(R.id.txt_resumen_remolque_placas);

        btnTitulo.setOnClickListener(this);

        spinnerBodegaCarga.setOnItemSelectedListener(this);
        spinnerBodegaDescarga.setOnItemSelectedListener(this);

        linearLayout.setVisibility(View.GONE);

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
                //myCalendar.set(Calendar.MINUTE, minute);
                updateTxtTime();
            }
        };

        spinnerOrigenEstado.setEnabled(false);

        txtCiudadCarga.setTag(txtCiudadCarga.getKeyListener());
        txtCiudadCarga.setKeyListener(null);

        txtColoniaCarga.setTag(txtColoniaCarga.getKeyListener());
        txtColoniaCarga.setKeyListener(null);

        txtCodigoPostalCarga.setTag(txtCodigoPostalCarga.getKeyListener());
        txtCodigoPostalCarga.setKeyListener(null);

        txtCalleCarga.setTag(txtCalleCarga.getKeyListener());
        txtCalleCarga.setKeyListener(null);

        txtNumIntCarga.setTag(txtNumIntCarga.getKeyListener());
        txtNumIntCarga.setKeyListener(null);

        txtNumExtCarga.setTag(txtNumExtCarga.getKeyListener());
        txtNumExtCarga.setKeyListener(null);

        spinnerBodegaDescarga.setEnabled(false);
        spinnerDestinoEstado.setEnabled(false);

        txtCiudadDescarga.setTag(txtCiudadDescarga.getKeyListener());
        txtCiudadDescarga.setKeyListener(null);

        txtColoniaDescarga.setTag(txtColoniaDescarga.getKeyListener());
        txtColoniaDescarga.setKeyListener(null);

        txtCodigoPostalDescarga.setTag(txtCodigoPostalDescarga.getKeyListener());
        txtCodigoPostalDescarga.setKeyListener(null);

        txtCalleDescarga.setTag(txtCalleDescarga.getKeyListener());
        txtCalleDescarga.setKeyListener(null);

        txtNumIntDescarga.setTag(txtNumIntDescarga.getKeyListener());
        txtNumIntDescarga.setKeyListener(null);

        txtNumExtDescarga.setTag(txtNumExtDescarga.getKeyListener());
        txtNumExtDescarga.setKeyListener(null);

        database = FirebaseDatabase.getInstance();
        drClientes = database.getReference(Constants.FB_KEY_MAIN_CLIENTES);

        RegistroFletesFragment.setFrameResumen(View.GONE);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.onPreRender();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != dbFlete) dbFlete.removeEventListener(listenerFletes);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityInterface = (MainRegisterInterface) getActivity();
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

        dbFlete = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR)
                .child(agenda.getFirebaseID());

        final ProgressDialog pDialogRender = new ProgressDialog(getContext());
        pDialogRender.setMessage(getString(R.string.default_loading_msg));
        pDialogRender.setIndeterminate(false);
        pDialogRender.setCancelable(false);
        pDialogRender.show();

        listenerFletes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Fletes flete = dataSnapshot.child(Constants.FB_KEY_MAIN_FLETE).getValue(Fletes.class);
                Bodegas bodegaCarga = dataSnapshot.child(Constants.FB_KEY_MAIN_BODEGA_DE_CARGA).getValue(Bodegas.class);



                for (DataSnapshot dsTransportista : dataSnapshot.child(Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_SELECCIONADO).getChildren()) {
                    Transportistas transportista = dsTransportista.getValue(Transportistas.class);

                    txtTransportista.setText(transportista.getNombre());
                }

                txtTransportista.setTag(txtTransportista.getKeyListener());
                txtTransportista.setKeyListener(null);

                for (DataSnapshot dsChofer : dataSnapshot.child(Constants.FB_KEY_MAIN_CHOFER_SELECCIONADO).getChildren()) {
                    Choferes chofer = dsChofer.getValue(Choferes.class);

                    txtChofer.setText(chofer.getNombre());
                    txtLicencia.setText(chofer.getNumeroDeLicencia());
                }

                txtChofer.setTag(txtChofer.getKeyListener());
                txtChofer.setKeyListener(null);

                txtLicencia.setTag(txtLicencia.getKeyListener());
                txtLicencia.setKeyListener(null);

                for (DataSnapshot dsTractor : dataSnapshot.child(Constants.FB_KEY_MAIN_TRACTOR_SELECCIONADO).getChildren()) {

                    Tractores tractor = dsTractor.getValue(Tractores.class);

                    txtTractor.setText(tractor.getNumeroEconomico());
                    txtPlacasTracor.setText(tractor.getPlaca());
                }

                txtTractor.setTag(txtTractor.getKeyListener());
                txtTractor.setKeyListener(null);

                txtPlacasTracor.setTag(txtPlacasTracor.getKeyListener());
                txtPlacasTracor.setKeyListener(null);

                for (DataSnapshot dsRemolque : dataSnapshot.child(Constants.FB_KEY_MAIN_REMOLQUE_SELECCIONADO).getChildren()) {
                    Remolques remolque = dsRemolque.getValue(Remolques.class);

                    txtRemolque.setText(remolque.getNumeroEconomico());
                    txtPlacasRemolque.setText(remolque.getPlaca());
                }

                txtRemolque.setTag(txtRemolque.getKeyListener());
                txtRemolque.setKeyListener(null);

                txtPlacasRemolque.setTag(txtPlacasRemolque.getKeyListener());
                txtPlacasRemolque.setKeyListener(null);

                _mainFletesActual = new MainFletes();

                _mainFletesActual.setFlete(flete);
                _mainFletesActual.setBodegaDeCarga(bodegaCarga);

                /**Cargar información del flete**/
                String date = DateTimeUtils.getParseTimeStamp(flete.getFechaDeSalida());
                CalendarDay calendarDay = DateTimeUtils.getCalendarDay(date);

                txtCliente.setText(bodegaCarga.getNombreDelCliente());
                firebaseIdCliente = bodegaCarga.getFirebaseIdDelCliente();

                txtPrecio.setText((null != flete.getPrecio()? flete.getPrecio() : "$ 0.0"));

                txtPrecio.setTag(txtPrecio.getKeyListener());
                txtPrecio.setKeyListener(null);

                loadBodegaCarga();
                loadBodegaDescarga();

                myCalendar.set(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay());
                updateTxtDate(); /**Actualiza la fecha del calendario**/

                Calendar c = Calendar.getInstance();
                c.set(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay()
                        , _mainFletesActual.getFlete().getHoraDeSalida(), 00, 00);

                myCalendar.setTime(new Time(c.getTimeInMillis()));
                updateTxtTime(); /**Actualiza la hora del calendario**/
                /**Combo se hace en el evento onCargarSpinnerBodegaDescarga**/
                txtCarga.setText(_mainFletesActual.getFlete().getCarga());

                txtCliente.setTag(txtCliente.getKeyListener());
                txtCliente.setKeyListener(null);

                txtCarga.setTag(txtCarga.getKeyListener());
                txtCarga.setKeyListener(null);

                txtFechaSalida.setTag(txtFechaSalida.getKeyListener());
                txtFechaSalida.setKeyListener(null);
                txtFechaSalida.setOnClickListener(null);

                spinnerBodegaCarga.setEnabled(false);
                spinnerOrigenEstado.setEnabled(false);

                txtCiudadCarga.setTag(txtCiudadCarga.getKeyListener());
                txtCiudadCarga.setKeyListener(null);

                txtColoniaCarga.setTag(txtColoniaCarga.getKeyListener());
                txtColoniaCarga.setKeyListener(null);

                txtCodigoPostalCarga.setTag(txtCodigoPostalCarga.getKeyListener());
                txtCodigoPostalCarga.setKeyListener(null);

                txtCalleCarga.setTag(txtCalleCarga.getKeyListener());
                txtCalleCarga.setKeyListener(null);

                txtNumIntCarga.setTag(txtNumIntCarga.getKeyListener());
                txtNumIntCarga.setKeyListener(null);

                txtNumExtCarga.setTag(txtNumExtCarga.getKeyListener());
                txtNumExtCarga.setKeyListener(null);

                spinnerBodegaDescarga.setEnabled(false);
                spinnerDestinoEstado.setEnabled(false);

                txtCiudadDescarga.setTag(txtCiudadDescarga.getKeyListener());
                txtCiudadDescarga.setKeyListener(null);

                txtColoniaDescarga.setTag(txtColoniaDescarga.getKeyListener());
                txtColoniaDescarga.setKeyListener(null);

                txtCodigoPostalDescarga.setTag(txtCodigoPostalDescarga.getKeyListener());
                txtCodigoPostalDescarga.setKeyListener(null);

                txtCalleDescarga.setTag(txtCalleDescarga.getKeyListener());
                txtCalleDescarga.setKeyListener(null);

                txtNumIntDescarga.setTag(txtNumIntDescarga.getKeyListener());
                txtNumIntDescarga.setKeyListener(null);

                txtNumExtDescarga.setTag(txtNumExtDescarga.getKeyListener());
                txtNumExtDescarga.setKeyListener(null);

                RegistroFletesFragment.setFrameResumen(View.VISIBLE);

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbFlete.addValueEventListener(listenerFletes);
    }

    private void updateTxtDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        txtFechaSalida.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTxtTime() {
        String myFormat = "HH"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        txtHoraSalida.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resumen_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_resumen_bodega_carga:

                if (position > 0) {
                    Bodegas bodega = bodegasCargas.get(position - 1);
                    this.loadDireccionCarga(bodega);
                } else {
                    this.cleanBodegaCarga();
                }

                break;
            case R.id.spinner_resumen_bodega_descarga:

                if (position > 0) {
                    Bodegas bodega = bodegasCargas.get(position - 1);
                    this.loadDireccionDescarga(bodega);
                } else {
                    this.cleanBodegaDescarga();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadBodegaCarga() {
        drBodegaCarga = database.getReference(Constants.FB_KEY_MAIN_CLIENTES)
                .child(firebaseIdCliente)
                .child(Constants.FB_KEY_MAIN_BODEGAS);

        /**Metodo que llama la lista**/
        drBodegaCarga.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bodegasCargasList = new ArrayList<>();
                bodegasCargas = new ArrayList<>();

                bodegasCargasList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Bodegas bodega = postSnapshot.getValue(Bodegas.class);

                    bodegasCargasList.add(bodega.getNombreDeLaBodega());
                    bodegasCargas.add(bodega);
                }

                onCargarSpinnerBodegaCarga();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
    }

    private void loadBodegaDescarga() {
        drBodegaDescarga = database.getReference(Constants.FB_KEY_MAIN_CLIENTES)
                .child(firebaseIdCliente)
                .child(Constants.FB_KEY_MAIN_BODEGAS);

        /**Metodo que llama la lista**/
        drBodegaDescarga.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bodegasDescargasList = new ArrayList<>();
                bodegasDescargas = new ArrayList<>();

                bodegasDescargasList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Bodegas bodega = postSnapshot.getValue(Bodegas.class);

                    bodegasDescargasList.add(bodega.getNombreDeLaBodega());
                    bodegasDescargas.add(bodega);
                }

                onCargarSpinnerBodegaDescarga();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
    }

    private void onCargarSpinnerBodegaCarga() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, bodegasCargasList);

        int itemSelection = onPreRenderSelectBodegaCarga();

        spinnerBodegaCarga.setAdapter(adapter);
        spinnerBodegaCarga.setSelection(itemSelection);
    }

    private void onCargarSpinnerBodegaDescarga() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, bodegasDescargasList);

        int itemSelection = onPreRenderSelectBodegaDescarga();

        spinnerBodegaDescarga.setAdapter(adapter);
        spinnerBodegaDescarga.setSelection(itemSelection);
    }

    private int onPreRenderSelectBodegaDescarga() {
        int item = 0;

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (Bodegas bodegaDescarga : bodegasDescargas) {
                item++;
                if (_mainFletesActual.getBodegaDeCarga().getFirebaseIdBodega()
                        .equals(bodegaDescarga.getFirebaseIdBodega())) {
                    break;
                }
            }
        }

        return item;
    }

    private int onPreRenderSelectBodegaCarga() {
        int item = 0;

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (Bodegas bodegaCarga : bodegasCargas) {
                item++;
                if (_mainFletesActual.getBodegaDeCarga().getFirebaseIdBodega()
                        .equals(bodegaCarga.getFirebaseIdBodega())) {
                    break;
                }
            }
        }

        return item;
    }

    private void onCargarOrigenEstados() {
        tiposOrigenEstadoList = new ArrayList<>();
        tiposOrigenEstados = new ArrayList<>();
        tiposOrigenEstadoList.add("Seleccione ...");

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

        tipoDestinoEstadoList.add("CD MX");
        tipoDestinoEstadoList.add("Chihuahua");
        tipoDestinoEstadoList.add("Otro");

        tiposDestinoEstados.add(new Estados(1, "CD MX"));
        tiposDestinoEstados.add(new Estados(2, "Chihuahua"));
        tiposDestinoEstados.add(new Estados(3, "Otro"));
    }

    private void onPreRenderOrigenEstados(String estadoBodega) {
        this.onCargarOrigenEstados();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tiposOrigenEstadoList);

        int itemSlection = this.onPreRenderSelectEstadoOrigen(estadoBodega);

        spinnerOrigenEstado.setAdapter(adapter);
        spinnerOrigenEstado.setSelection(itemSlection);
    }

    private void onPreRenderDestinoEstados(String estadoBodega) {
        this.onCargarDestinoEstados();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tipoDestinoEstadoList);

        int itemSlection = this.onPreRenderSelectEstadoOrigen(estadoBodega);

        spinnerDestinoEstado.setAdapter(adapter);
        spinnerDestinoEstado.setSelection(itemSlection);
    }

    private int onPreRenderSelectEstadoOrigen(String estadoBodega) {
        int item = 0;

        if (null != estadoBodega) {

            for (Estados estado : tiposOrigenEstados) {
                item++;
                if (estado.getEstado().equals(estadoBodega)) break;
            }

        }

        return item;
    }

    private void loadDireccionCarga(Bodegas bodega) {

        this.onPreRenderOrigenEstados(bodega.getEstado());

        txtCiudadCarga.setText(bodega.getCiudad());
        txtColoniaCarga.setText(bodega.getColonia());
        txtCodigoPostalCarga.setText(bodega.getCodigoPostal());
        txtCalleCarga.setText(bodega.getCalle());
        txtNumIntCarga.setText(bodega.getNumeroInterior());
        txtNumExtCarga.setText(bodega.getNumeroExterior());

        _bodegaCargaActual = bodega;
    }

    private void loadDireccionDescarga(Bodegas bodega) {

        this.onPreRenderDestinoEstados(bodega.getEstado());

        txtCiudadDescarga.setText(bodega.getCiudad());
        txtColoniaDescarga.setText(bodega.getColonia());
        txtCodigoPostalDescarga.setText(bodega.getCodigoPostal());
        txtCalleDescarga.setText(bodega.getCalle());
        txtNumIntDescarga.setText(bodega.getNumeroInterior());
        txtNumExtDescarga.setText(bodega.getNumeroExterior());

        _bodegaDescargaActual = bodega;
    }

    private void cleanBodegaCarga() {

        _bodegaCargaActual = new Bodegas();

        this.onPreRenderOrigenEstados(_bodegaCargaActual.getEstado());

        txtCiudadCarga.setText(_bodegaCargaActual.getCiudad());
        txtColoniaCarga.setText(_bodegaCargaActual.getColonia());
        txtCodigoPostalCarga.setText(_bodegaCargaActual.getCodigoPostal());
        txtCalleCarga.setText(_bodegaCargaActual.getCalle());
        txtNumIntCarga.setText(_bodegaCargaActual.getNumeroInterior());
        txtNumExtCarga.setText(_bodegaCargaActual.getNumeroExterior());
    }

    private void cleanBodegaDescarga() {

        _bodegaDescargaActual = new Bodegas();

        this.onPreRenderDestinoEstados(_bodegaDescargaActual.getEstado());

        txtCiudadDescarga.setText(_bodegaDescargaActual.getCiudad());
        txtColoniaDescarga.setText(_bodegaDescargaActual.getColonia());
        txtCodigoPostalDescarga.setText(_bodegaDescargaActual.getCodigoPostal());
        txtCalleDescarga.setText(_bodegaDescargaActual.getCalle());
        txtNumIntDescarga.setText(_bodegaDescargaActual.getNumeroInterior());
        txtNumExtDescarga.setText(_bodegaDescargaActual.getNumeroExterior());
    }

}
