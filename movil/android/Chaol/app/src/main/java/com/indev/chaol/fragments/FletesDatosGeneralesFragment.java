package com.indev.chaol.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.MainRegisterInterface;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Estados;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.TiposRemolques;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by saurett on 24/02/2017.
 */

public class FletesDatosGeneralesFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = FletesDatosGeneralesFragment.class.getName();

    private Button btnTitulo, btnSolicitarCotizacion, btnActualizar;
    private LinearLayout linearLayoutClientes, linearLayout, linearLayoutZone, linearLayoutIDFlete;
    private EditText txtIDFlete, txtFechaSalida, txtHoraSalida, txtCarga, txtNumEmbarque, txtDestinatario;
    private EditText txtCiudadCarga, txtColoniaCarga, txtCodigoPostalCarga, txtCalleCarga, txtNumIntCarga, txtNumExtCarga;
    private EditText txtCiudadDescarga, txtColoniaDescarga, txtCodigoPostalDescarga, txtCalleDescarga, txtNumIntDescarga, txtNumExtDescarga;
    private Spinner spinnerCliente, spinnerBodegaCarga, spinnerBodegaDescarga, spinnerTipoRemolque, spinnerOrigenEstado, spinnerDestinoEstado;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;
    private ProgressDialog pDialog;

    private static List<String> clientesList;
    private List<Clientes> clientes;

    private static List<String> bodegasCargasList;
    private List<Bodegas> bodegasCargas;
    private static Bodegas _bodegaCargaActual;

    private static List<String> bodegasDescargasList;
    private List<Bodegas> bodegasDescargas;
    private static Bodegas _bodegaDescargaActual;

    private String firebaseIdCliente;
    private String firebaseIdBodegaCarga;
    private String firebaseIdBodegaDescarga;

    private static List<String> tiposRemolquesList, tiposOrigenEstadoList, tipoDestinoEstadoList;
    private List<TiposRemolques> tiposRemolques;
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
        View view = inflater.inflate(R.layout.fragment_datos_generales_fletes, container, false);

        _MAIN_DECODE = (DecodeExtraParams) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_MAIN_DECODE);
        _SESSION_USER = (Usuarios) getActivity().getIntent().getSerializableExtra(Constants.KEY_SESSION_USER);

        btnTitulo = (Button) view.findViewById(R.id.btn_datos_generales_fletes);
        btnSolicitarCotizacion = (Button) view.findViewById(R.id.btn_solicitar_cotizacion_datos_generales);
        btnActualizar = (Button) view.findViewById(R.id.btn_actualizar_datos_generales);

        linearLayoutClientes = (LinearLayout) view.findViewById(R.id.item_datos_generales_cliente);
        linearLayout = (LinearLayout) view.findViewById(R.id.datos_generales_container);
        linearLayoutZone = (LinearLayout) view.findViewById(R.id.linear_btn_zone_datos_generales);
        linearLayoutIDFlete = (LinearLayout) view.findViewById(R.id.linear_id_flete);

        spinnerCliente = (Spinner) view.findViewById(R.id.spinner_datos_generales_cliente);
        spinnerBodegaCarga = (Spinner) view.findViewById(R.id.spinner_datos_generales_bodega_carga);
        spinnerBodegaDescarga = (Spinner) view.findViewById(R.id.spinner_datos_generales_bodega_descarga);
        spinnerTipoRemolque = (Spinner) view.findViewById(R.id.spinner_dg_tipo_remolque);
        spinnerOrigenEstado = (Spinner) view.findViewById(R.id.spinner_dg_origen_estado);
        spinnerDestinoEstado = (Spinner) view.findViewById(R.id.spinner_dg_destino_estado);

        txtIDFlete = (EditText) view.findViewById(R.id.txt_dg_id_flete);
        txtFechaSalida = (EditText) view.findViewById(R.id.txt_dg_fecha_salida);
        txtHoraSalida = (EditText) view.findViewById(R.id.txt_dg_hora_salida);
        txtCarga = (EditText) view.findViewById(R.id.txt_dg_carga);
        txtNumEmbarque = (EditText) view.findViewById(R.id.txt_dg_num_embarque);
        txtDestinatario = (EditText) view.findViewById(R.id.txt_dg_destinatario);

        txtCiudadCarga = (EditText) view.findViewById(R.id.txt_dg_ciudad_carga);
        txtColoniaCarga = (EditText) view.findViewById(R.id.txt_dg_colonia_carga);
        txtCodigoPostalCarga = (EditText) view.findViewById(R.id.txt_dg_codigo_postal_carga);
        txtCalleCarga = (EditText) view.findViewById(R.id.txt_dg_calle_carga);
        txtNumIntCarga = (EditText) view.findViewById(R.id.txt_dg_num_int_carga);
        txtNumExtCarga = (EditText) view.findViewById(R.id.txt_dg_num_ext_carga);

        txtCiudadDescarga = (EditText) view.findViewById(R.id.txt_dg_ciudad_destino);
        txtColoniaDescarga = (EditText) view.findViewById(R.id.txt_dg_colonia_destino);
        txtCodigoPostalDescarga = (EditText) view.findViewById(R.id.txt_dg_codigo_postal_destino);
        txtCalleDescarga = (EditText) view.findViewById(R.id.txt_dg_calle_destino);
        txtNumIntDescarga = (EditText) view.findViewById(R.id.txt_dg_num_int_destino);
        txtNumExtDescarga = (EditText) view.findViewById(R.id.txt_dg_num_ext_destino);

        btnTitulo.setOnClickListener(this);
        btnSolicitarCotizacion.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        txtFechaSalida.setOnClickListener(this);
        txtHoraSalida.setOnClickListener(this);

        spinnerCliente.setOnItemSelectedListener(this);
        spinnerBodegaCarga.setOnItemSelectedListener(this);
        spinnerBodegaDescarga.setOnItemSelectedListener(this);
        spinnerTipoRemolque.setOnItemSelectedListener(this);

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


        txtIDFlete.setTag(txtIDFlete.getKeyListener());
        txtIDFlete.setKeyListener(null);

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

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        /**Metodo que llama la lista**/
        drClientes.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clientesList = new ArrayList<>();
                clientes = new ArrayList<>();

                clientesList.add("Seleccione ...");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot psCliente : postSnapshot.getChildren()) {

                        Clientes cliente = psCliente.getValue(Clientes.class);

                        if (cliente.getFirebaseId() == null) continue;

                        if (cliente.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)) {
                            clientesList.add(cliente.getNombre());
                            clientes.add(cliente);
                        }
                    }
                }

                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_REGISTRAR) {
                    onCargarSpinnerClientes();
                }


                /**Cuando entra el admin a registrar no existe cliente, debe seleccionarlo**/
                if (null != firebaseIdCliente) loadBodegaCarga();

                if (null != firebaseIdCliente) loadBodegaDescarga();

                if (!_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE)) {
                    linearLayoutClientes.setVisibility(View.VISIBLE);
                }
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

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

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_REGISTRAR) {
            onCargarSpinnerTiposRemolques();
        }

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

                Log.i(TAG, "onPreRenderEditar: " + dataSnapshot.getKey());

                _mainFletesActual = new MainFletes();

                _mainFletesActual.setFlete(flete);
                _mainFletesActual.setBodegaDeCarga(bodegaCarga);

                /**Cargar información del flete**/
                String date = DateTimeUtils.getParseTimeStamp(flete.getFechaDeSalida());
                CalendarDay calendarDay = DateTimeUtils.getCalendarDay(date);

                setProgressBar(); /**Actualiza el progreso de la barra**/
                linearLayoutIDFlete.setVisibility(View.VISIBLE);
                txtIDFlete.setText(_mainFletesActual.getFlete().getIdFlete());

                if (null != clientesList && null != clientes) {
                    onCargarSpinnerClientes();
                } else {
                    clientesList = new ArrayList<>();
                    clientes = new ArrayList<>();

                    clientesList.add("Seleccione ...");

                    Clientes cliente = new Clientes();
                    cliente.setFirebaseId(bodegaCarga.getFirebaseIdDelCliente());
                    cliente.setNombre(flete.getCliente());

                    clientes.add(cliente);
                    clientesList.add(cliente.getNombre());

                    onCargarSpinnerClientes();
                }

                myCalendar.set(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay());
                updateTxtDate(); /**Actualiza la fecha del calendario**/

                Calendar c = Calendar.getInstance();
                c.set(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay()
                        , _mainFletesActual.getFlete().getHoraDeSalida(), 00, 00);

                myCalendar.setTime(new Time(c.getTimeInMillis()));
                updateTxtTime(); /**Actualiza la hora del calendario**/
                /**Combo se hace en el evento onCargarSpinnerBodegaDescarga**/
                onCargarSpinnerTiposRemolques(); /**Carga los valores del combo**/
                txtCarga.setText(_mainFletesActual.getFlete().getCarga());
                txtNumEmbarque.setText(_mainFletesActual.getFlete().getNumeroDeEmbarque());
                txtDestinatario.setText(_mainFletesActual.getFlete().getDestinatario());
                /**Combo se hace en el evento onCargarSpinnerBodegaDescarga**/


                txtNumEmbarque.setText(flete.getNumeroDeEmbarque());

                txtIDFlete.setTag(txtIDFlete.getKeyListener());
                txtIDFlete.setKeyListener(null);

                spinnerCliente.setEnabled(false);

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

                spinnerTipoRemolque.setEnabled(false);

                switch (_SESSION_USER.getTipoDeUsuario()) {
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                    case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                        txtHoraSalida.setTag(txtHoraSalida.getKeyListener());
                        txtHoraSalida.setKeyListener(null);
                        txtHoraSalida.setOnClickListener(null);

                        txtCarga.setTag(txtCarga.getKeyListener());
                        txtCarga.setKeyListener(null);

                        txtNumEmbarque.setTag(txtNumEmbarque.getKeyListener());
                        txtNumEmbarque.setKeyListener(null);

                        txtDestinatario.setTag(txtDestinatario.getKeyListener());
                        txtDestinatario.setKeyListener(null);
                        break;
                }

                btnSolicitarCotizacion.setVisibility(View.GONE);

                switch (_mainFletesActual.getFlete().getEstatus()) {
                    case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                                btnActualizar.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                                btnActualizar.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                                btnActualizar.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                                btnActualizar.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                        switch (_SESSION_USER.getTipoDeUsuario()) {
                            case Constants.FB_KEY_ITEM_ADMINISTRADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_COLABORADOR:
                            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                                btnActualizar.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                        txtHoraSalida.setTag(txtHoraSalida.getKeyListener());
                        txtHoraSalida.setKeyListener(null);
                        txtHoraSalida.setOnClickListener(null);

                        txtCarga.setTag(txtCarga.getKeyListener());
                        txtCarga.setKeyListener(null);

                        txtNumEmbarque.setTag(txtNumEmbarque.getKeyListener());
                        txtNumEmbarque.setKeyListener(null);

                        txtDestinatario.setTag(txtDestinatario.getKeyListener());
                        txtDestinatario.setKeyListener(null);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                        txtHoraSalida.setTag(txtHoraSalida.getKeyListener());
                        txtHoraSalida.setKeyListener(null);
                        txtHoraSalida.setOnClickListener(null);

                        txtCarga.setTag(txtCarga.getKeyListener());
                        txtCarga.setKeyListener(null);

                        txtNumEmbarque.setTag(txtNumEmbarque.getKeyListener());
                        txtNumEmbarque.setKeyListener(null);

                        txtDestinatario.setTag(txtDestinatario.getKeyListener());
                        txtDestinatario.setKeyListener(null);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                        txtHoraSalida.setTag(txtHoraSalida.getKeyListener());
                        txtHoraSalida.setKeyListener(null);
                        txtHoraSalida.setOnClickListener(null);

                        txtCarga.setTag(txtCarga.getKeyListener());
                        txtCarga.setKeyListener(null);

                        txtNumEmbarque.setTag(txtNumEmbarque.getKeyListener());
                        txtNumEmbarque.setKeyListener(null);

                        txtDestinatario.setTag(txtDestinatario.getKeyListener());
                        txtDestinatario.setKeyListener(null);
                        break;
                    case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                        txtHoraSalida.setTag(txtHoraSalida.getKeyListener());
                        txtHoraSalida.setKeyListener(null);
                        txtHoraSalida.setOnClickListener(null);

                        txtCarga.setTag(txtCarga.getKeyListener());
                        txtCarga.setKeyListener(null);

                        txtNumEmbarque.setTag(txtNumEmbarque.getKeyListener());
                        txtNumEmbarque.setKeyListener(null);

                        txtDestinatario.setTag(txtDestinatario.getKeyListener());
                        txtDestinatario.setKeyListener(null);
                        break;
                    default:
                        break;
                }

                pDialogRender.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbFlete.addValueEventListener(listenerFletes);

        /**Modifica valores predeterminados de ciertos elementos**/
        //btnTitulo.setText(getString(Constants.TITLE_FORM_ACTION.get(_MAIN_DECODE.getAccionFragmento())));
        //fabClientes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mode_edit_white_18dp));

    }

    private void onCargarSpinnerTiposRemolques() {
        this.onCargarTiposRemolques();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, tiposRemolquesList);

        int itemSelection = onPreRenderSelectTipoRemolque();


        spinnerTipoRemolque.setAdapter(adapter);
        spinnerTipoRemolque.setSelection(itemSelection);
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

    private int onPreRenderSelectTipoRemolque() {
        int item = 0;

        if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (TiposRemolques tipoRemolque : tiposRemolques) {
                item++;
                if (tipoRemolque.getTipoRemolque().equals(_mainFletesActual.getFlete().getTipoDeRemolque())) {
                    break;
                }
            }
        }

        return item;
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
            case R.id.btn_datos_generales_fletes:
                linearLayout.setVisibility((linearLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_solicitar_cotizacion_datos_generales:
                if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
                    this.showQuestion();
                } else {
                    this.validationRegister();
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
                        true).show();
                break;
        }
    }

    private void validationRegister() {
        Boolean authorized = true;

        String fechaSalida = txtFechaSalida.getText().toString();
        String horaSalida = txtHoraSalida.getText().toString();
        String carga = txtCarga.getText().toString();
        String numEmbarque = txtNumEmbarque.getText().toString();
        String destinatario = txtDestinatario.getText().toString();

        if (TextUtils.isEmpty(fechaSalida)) {
            txtFechaSalida.setError("El campo es obligatorio", null);
            txtFechaSalida.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(horaSalida)) {
            txtHoraSalida.setError("El campo es obligatorio", null);
            txtHoraSalida.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(carga)) {
            txtCarga.setError("El campo es obligatorio", null);
            txtCarga.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(numEmbarque)) {
            txtNumEmbarque.setError("El campo es obligatorio", null);
            txtNumEmbarque.requestFocus();
            authorized = false;
        }

        if (TextUtils.isEmpty(destinatario)) {
            txtDestinatario.setError("El campo es obligatorio", null);
            txtDestinatario.requestFocus();
            authorized = false;
        }

        if (spinnerBodegaCarga.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerBodegaCarga.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (spinnerBodegaDescarga.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerBodegaDescarga.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (spinnerTipoRemolque.getSelectedItemId() <= 0L) {
            TextView errorTextSE = (TextView) spinnerTipoRemolque.getSelectedView();
            errorTextSE.setError("El campo es obligatorio");
            errorTextSE.setTextColor(Color.RED);
            errorTextSE.setText("El campo es obligatorio");//changes t
            errorTextSE.requestFocus();

            authorized = false;
        }

        if (authorized) {
            this.createSimpleFlete();
        } else {
            Toast.makeText(getContext(), "Es necesario capturar campos obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createSimpleFlete() {
        Fletes flete = new Fletes();

        long tsFechaSalida = 0L;

        try {
            tsFechaSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(txtFechaSalida.getText()
                    .toString() + " 01:00:00").getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        flete.setFechaDeSalida(tsFechaSalida);
        flete.setHoraDeSalida(Integer.valueOf(txtHoraSalida.getText().toString()));
        flete.setCliente(spinnerCliente.getSelectedItem().toString());
        flete.setBodegaDeCarga(spinnerBodegaCarga.getSelectedItem().toString());
        flete.setCarga(txtCarga.getText().toString());
        flete.setTipoDeRemolque(spinnerTipoRemolque.getSelectedItem().toString());
        flete.setNumeroDeEmbarque(txtNumEmbarque.getText().toString());
        flete.setDestinatario(txtDestinatario.getText().toString());
        flete.setBodegaDeDescarga(spinnerBodegaDescarga.getSelectedItem().toString());

        activityInterface.createSolicitudCotizacion(flete, _bodegaCargaActual, _bodegaDescargaActual);
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

        switch (parent.getId()) {
            case R.id.spinner_datos_generales_cliente:
                if (position > 0) {

                    Clientes cliente = clientes.get(position - 1);
                    firebaseIdCliente = cliente.getFirebaseId();

                    this.loadBodegaCarga();
                    this.loadBodegaDescarga();
                } else {

                    bodegasCargasList = new ArrayList<>();
                    bodegasCargasList.add("Seleccione ...");

                    bodegasDescargasList = new ArrayList<>();
                    bodegasDescargasList.add("Seleccione ...");

                    this.onCargarSpinnerBodegaCarga();
                    this.onCargarSpinnerBodegaDescarga();
                    this.cleanBodegaCarga();
                    this.cleanBodegaDescarga();

                }
                break;
            case R.id.spinner_datos_generales_bodega_carga:

                if (position > 0) {
                    Bodegas bodega = bodegasCargas.get(position - 1);
                    this.loadDireccionCarga(bodega);
                } else {
                    this.cleanBodegaCarga();
                }

                break;
            case R.id.spinner_datos_generales_bodega_descarga:

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
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
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
                pDialog.dismiss();
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /**
     * Asigna los valores de los transportista a su combo
     **/
    private void onCargarSpinnerClientes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.text_spinner, clientesList);

        int itemSelection = onPreRenderSelectCliente();

        spinnerCliente.setAdapter(adapter);
        spinnerCliente.setSelection(itemSelection);
    }


    private int onPreRenderSelectCliente() {
        int item = 0;

        if (_SESSION_USER.getTipoDeUsuario().equals(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE)) {
            for (Clientes cliente : clientes) {
                item++;
                if (cliente.getFirebaseId().equals(_SESSION_USER.getFirebaseId())) {
                    firebaseIdCliente = cliente.getFirebaseId();
                    loadBodegaCarga();
                    loadBodegaDescarga();
                    break;
                }
            }
        } else if (_MAIN_DECODE.getAccionFragmento() == Constants.ACCION_EDITAR) {
            for (Clientes miCliente : clientes) {
                item++;
                if (miCliente.getFirebaseId().equals(
                        _mainFletesActual.getBodegaDeCarga().getFirebaseIdDelCliente())) {
                    break;
                }
            }
        }

        return item;
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

    private void setProgressBar() {

        int progress = 0;

        switch (_mainFletesActual.getFlete().getEstatus()) {
            case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                progress = 15;
                break;
            case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                progress = 30;
                break;
            case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                progress = 45;
                break;
            case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                progress = 70;
                break;
            case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                progress = 80;
                break;
            case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                progress = 90;
                break;
            case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                progress = 95;
                break;
            case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                progress = 100;
                break;
            case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                progress = 0;
                break;
            default:
                break;
        }

        RegistroFletesFragment.setFleteProgressBar(progress);

    }

}
