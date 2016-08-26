package com.example.android.paktw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalendarActivity33 extends Fragment {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.textView)
    TextView textView;

    public CalendarActivity33() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(getContext());
        // Inserting Events
        Log.d("Insert: ", "Inserting ..");
        try{
//            db.createEvent(2, "Menanam", "13 Desember 2015", "Menanam tumbuhan", "aktif");
//            db.getAllEvent();
        }catch(android.content.ActivityNotFoundException e){
            Log.d("Insert: ", "Gagal melakukan Insert");
        }


    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(getActivity());

//        widget.setOnDateChangedListener(this);
//        widget.setOnMonthChangedListener(this);

        //Setup initial text
//        textView.setText(getSelectedDatesString());

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


//    @Override
//    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
//        textView.setText(getSelectedDatesString());
//    }
//
//    @Override
//    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
//        //noinspection ConstantConditions
//        ActionBar actionbar=((ActionBarActivity)getActivity()).getSupportActionBar();
//        actionbar.setTitle(FORMATTER.format(date.getDate()));
//    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

}
