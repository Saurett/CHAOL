package com.indev.chaol.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.indev.chaol.MainRegisterActivity;
import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.utils.CircleDecorator;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.HashSet;


/**
 * Created by saurett on 24/02/2017.
 */

public class ListadoAgendaFragment extends Fragment implements View.OnClickListener, OnDateSelectedListener {

    private NavigationDrawerInterface activityInterface;
    private FloatingActionButton fabAgendas;
    private MaterialCalendarView mvcAgenda;
    private TextView txtSelectedDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_agenda, container, false);

        fabAgendas = (FloatingActionButton) view.findViewById(R.id.fab_listado_agenda);
        mvcAgenda = (MaterialCalendarView) view.findViewById(R.id.calendarView_agenda);
        txtSelectedDay = (TextView) view.findViewById(R.id.txt_selected_day);

        mvcAgenda.setOnDateChangedListener(this);

        fabAgendas.setOnClickListener(this);

        this.onPreRender();

        return view;
    }

    @Override
    public void onStart() {
        /**Remueve los fragmentos secundarios**/
        activityInterface.removeSecondaryFragment();
        mvcAgenda.clearSelection();
/*
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction mainFragment = fragmentManager.beginTransaction();
        mainFragment.replace(R.id.listado_tractores_container, new TractoresFragment(), Constants.FRAGMENT_TRACTORES);
        mainFragment.commit();

        */


        super.onStart();
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
        this.setCurrentDate();
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
        mvcAgenda.addDecorator(new CircleDecorator(getContext(),R.drawable.selected_circle, calendarDays));
        mvcAgenda.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        mvcAgenda.state().edit()
                .commit();

        this.setCurrentSelectionPanel(today);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_listado_tractores:
                DecodeExtraParams extraParams = new DecodeExtraParams();

                extraParams.setTituloActividad(getString(Constants.TITLE_ACTIVITY.get(v.getId())));
                extraParams.setTituloFormulario(getString(R.string.default_form_title_new));
                extraParams.setAccionFragmento(Constants.ACCION_REGISTRAR);
                extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(v.getId()));

                Intent intent = new Intent(getActivity(), MainRegisterActivity.class);
                intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
                startActivity(intent);
                break;
            case R.id.fab_listado_agenda:
                mvcAgenda.setVisibility((mvcAgenda.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
        }

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        this.setCurrentSelectionPanel(date);
    }

    public void setCurrentSelectionPanel(CalendarDay date) {
        int dayOfWeek = date.getCalendar().get(Calendar.DAY_OF_WEEK);
        int selectedDay = date.getDay();

        txtSelectedDay.setText(selectedDay + "\n" + DateTimeUtils.getDayName(dayOfWeek));
    }

    public void openFragment(CalendarDay date) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction mainFragment = fragmentManager.beginTransaction();
        mainFragment.replace(R.id.listado_agenda_container, new RemolquesFragment(), Constants.FRAGMENT_REMOLQUES);
        mainFragment.commit();
    }
}
