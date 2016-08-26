package com.example.android.paktw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.SessionManager;
import com.example.android.paktw.model.Event;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    DatabaseHelper db = new DatabaseHelper(CalendarActivity.this);
    SessionManager session;
    int control = 0;
    List<Event> ev;
    Calendar calendar;
    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
    boolean isHistory = false;

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        // Session Manager
        session = new SessionManager(CalendarActivity.this);

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        ev = db.getAllEvent(session.getUsernameSession());

        calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

        //Setup initial text
        textView.setText(getSelectedDatesString());

        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(control != 69){
                    Intent intent = new Intent(CalendarActivity.this,DetailEventActivity.class);
                    intent.putExtra("id_event",ev.get(control).getId_event());
                    if(isHistory){
                        intent.putExtra("isHistory", true);
                    }
                    startActivity(intent);
                }
            }
        });

        new getDateSelector().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        textView.setText(getSelectedDatesString());
//        Log.d("textView.setText(getSelectedDatesString());", String.valueOf(123));
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
//        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        String message = "";

//        CalendarDay dateAllEvent = widget.getCurrentDate();
        if (date == null) {
            return "No Selection";
        }
        else {
            for(int i = 0; i < db.getAllEvent(session.getUsernameSession()).size();i++){
                String dateinCal = myFormat.format(date.getDate());
//                event = db.getSingleEvent(session.getUsernameSession(), i);
//            String evDate = event.getDate_event();
                Date dateEv = null;
                try {
                    dateEv = myFormat.parse(ev.get(i).getDate_event());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("dateinCal", dateinCal + " " + ev.get(i).getName_event());
                if(dateinCal.equals(ev.get(i).getDate_event())){
                    message = ev.get(i).getName_event();
                    if(calendar.getTime().after(dateEv)){
                        isHistory = true;
                    }
                    else{
                        isHistory = false;
                    }
                    control = i;
                    break;
                }
                else{
                    control = 69;
                    message = "No activities";
                }
            }
        }
        return  message;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class getDateSelector extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < db.getAllEvent(session.getUsernameSession()).size(); i++) {
                String dateEvent = ev.get(i).getDate_event();
                try {
//                    DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    Date date = myFormat.parse(dateEvent);
                    calendar.setTime(date);
                }
                catch (ParseException e)
                {

                }
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);

//                calendar.add(Calendar.DATE, 3);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }
}
