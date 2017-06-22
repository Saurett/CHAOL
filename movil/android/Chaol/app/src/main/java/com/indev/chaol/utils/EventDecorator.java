package com.indev.chaol.utils;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jvier on 15/06/2017.
 */

public class EventDecorator implements DayViewDecorator {

    private int[] colors;
    private HashSet<CalendarDay> dates;

    public EventDecorator(Collection<CalendarDay> dates, int... colors) {
        this.colors = colors;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new MyDotSpan(5, 5, colors));
    }
}
