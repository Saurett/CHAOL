package com.indev.chaol.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.CircleDecorator;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.indev.chaol.utils.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by saurett on 24/02/2017.
 */

public class ListadoAgendaFragment extends Fragment implements View.OnClickListener, OnDateSelectedListener {

    private NavigationDrawerInterface activityInterface;
    private FloatingActionButton fabAgendas;
    private MaterialCalendarView mvcAgenda;
    private TextView txtSelectedDay;
    private ProgressDialog pDialog;

    private static Usuarios _SESSION_USER;

    /**
     * Declaraciones para Firebase
     **/
    private FirebaseDatabase database;
    private DatabaseReference drFletes;

    private String firebaseIDCliente;
    private String firebaseIDransportista;
    private String firebaseIDChofer;
    private List<String> firebaseIDTransportistas;
    private Map<CalendarDay,List<String>> _calendarFirebases;

    private ValueEventListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_agenda, container, false);

        _SESSION_USER = (Usuarios) getActivity().getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        fabAgendas = (FloatingActionButton) view.findViewById(R.id.fab_listado_agenda);
        mvcAgenda = (MaterialCalendarView) view.findViewById(R.id.calendarView_agenda);
        txtSelectedDay = (TextView) view.findViewById(R.id.txt_selected_day);


        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        database = FirebaseDatabase.getInstance();
        drFletes = database.getReference(Constants.FB_KEY_MAIN_FLETES_POR_ASIGNAR);

        mvcAgenda.setOnDateChangedListener(this);
        fabAgendas.setOnClickListener(this);

        this.onPreRender();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    /**Guarda en una lista de firebase el dia con su respectivo firebaseID**/
    private void saveCalendarDay(CalendarDay calendarDay, Fletes flete) {
        List<String> firebases = (_calendarFirebases.containsKey(calendarDay)
                ? _calendarFirebases.get(calendarDay) : new ArrayList<String>());
        firebases.add(flete.getFirebaseId());
        _calendarFirebases.put(calendarDay,firebases);
    }

    @Override
    public void onStart() {
        super.onStart();

        /**Remueve los fragmentos secundarios**/
        activityInterface.removeSecondaryFragment();

        _calendarFirebases = new HashMap<>();

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CalendarDay> calendarDaysGray = new ArrayList<>();
                List<CalendarDay> dotGOCalendar = new ArrayList<>();
                List<CalendarDay> dotGBCalendar = new ArrayList<>();
                List<CalendarDay> dotGOBCalendar = new ArrayList<>();

                _calendarFirebases = new HashMap<>();

                firebaseIDCliente = "";
                firebaseIDransportista = "";
                firebaseIDChofer = "";
                firebaseIDTransportistas = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

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

                    if (null != flete.getFechaDeSalida()) {

                        String date = DateTimeUtils.getParseTimeStamp(flete.getFechaDeSalida());

                        CalendarDay calendarDay = DateTimeUtils.getCalendarDay(date);

                        switch (flete.getEstatus()) {
                            case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGBCalendar.contains(calendarDay)) {
                                    dotGBCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGOCalendar.contains(calendarDay)) {
                                    dotGOCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGBCalendar.contains(calendarDay)) {
                                    dotGBCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);


                                break;
                            case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGOCalendar.contains(calendarDay)) {
                                    dotGOCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGOCalendar.contains(calendarDay)) {
                                    dotGOCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGOCalendar.contains(calendarDay)) {
                                    dotGOCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                if (!dotGBCalendar.contains(calendarDay)) {
                                    dotGBCalendar.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);


                                break;
                            case Constants.FB_KEY_ITEM_STATUS_CANCELADO:

                                if (checkInvisibleCount()) continue;

                                if (!calendarDaysGray.contains(calendarDay)) {
                                    calendarDaysGray.add(calendarDay);
                                }

                                saveCalendarDay(calendarDay,flete);

                                break;
                            default:
                                break;
                        }

                        if (dotGBCalendar.contains(calendarDay) & dotGOCalendar.contains(calendarDay)) {
                            dotGOBCalendar.add(calendarDay);
                        }

                    }
                }

                calendarDaysGray.removeAll(dotGOBCalendar);
                dotGOCalendar.removeAll(dotGOBCalendar);
                dotGBCalendar.removeAll(dotGOBCalendar);

                calendarDaysGray.removeAll(dotGOCalendar);
                dotGBCalendar.removeAll(dotGOCalendar);

                calendarDaysGray.removeAll(dotGBCalendar);
                dotGOCalendar.removeAll(dotGBCalendar);


                mvcAgenda.addDecorator(new EventDecorator(calendarDaysGray, Color.GRAY));
                mvcAgenda.addDecorator(new EventDecorator(dotGOCalendar, Color.GRAY, Color.YELLOW));
                mvcAgenda.addDecorator(new EventDecorator(dotGBCalendar, Color.GRAY, Color.BLACK));
                mvcAgenda.addDecorator(new EventDecorator(dotGOBCalendar, Color.GRAY, Color.YELLOW, Color.BLACK));

                setCurrentDate();
                openFragment(mvcAgenda.getSelectedDate());

                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        };

        drFletes.addValueEventListener(listener);
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

    public void onPreRender() {
        //this.setCurrentDate();
        //this.openFragment(mvcAgenda.getSelectedDate());
    }

    public void setCurrentDate() {
        CalendarDay today = CalendarDay.from(
                DateTimeUtils.getCurrentYear(),
                DateTimeUtils.getCurrentMonth(),
                DateTimeUtils.getCurrentDay());

        HashSet<CalendarDay> calendarDays = new HashSet<>();
        calendarDays.add(today);

        mvcAgenda.setCurrentDate(today);
        mvcAgenda.setSelectedDate(today);
        mvcAgenda.addDecorator(new CircleDecorator(getContext(), R.drawable.selected_circle, calendarDays));
        mvcAgenda.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        mvcAgenda.state().edit()
                .commit();

        this.setCurrentSelectionPanel(today);
    }

    @Override
    public void onStop() {
        super.onStop();
        drFletes.removeEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_listado_agenda:
                DecodeExtraParams extraParams = new DecodeExtraParams();

                extraParams.setTituloActividad(getString(Constants.TITLE_ACTIVITY.get(v.getId())));
                extraParams.setTituloFormulario(getString(R.string.default_form_title_new));
                extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
                extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(v.getId()));

                Intent intent = new Intent(getActivity(), MainRegisterActivity.class);
                intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
                intent.putExtra(Constants.KEY_SESSION_USER, _SESSION_USER);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        this.setCurrentSelectionPanel(date);
    }

    /**Accede a la agenda del dia seleccionado**/
    public void setCurrentSelectionPanel(CalendarDay date) {
        int dayOfWeek = date.getCalendar().get(Calendar.DAY_OF_WEEK);
        int selectedDay = date.getDay();

        txtSelectedDay.setText(selectedDay + "\n" + DateTimeUtils.getDayName(dayOfWeek));

        this.openFragment(date);
    }

    public void openFragment(CalendarDay date) {

        List<String> firebases = (_calendarFirebases.containsKey(date)
                ? _calendarFirebases.get(date) : new ArrayList<String>());

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        getActivity().getIntent().putExtra("lista", (Serializable) firebases);

        FragmentTransaction mainFragment = fragmentManager.beginTransaction();
        mainFragment.replace(R.id.listado_agenda_container, new AgendasFragment(), Constants.FRAGMENT_AGENDA);
        mainFragment.commit();
    }
}
