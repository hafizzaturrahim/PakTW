package com.example.android.paktw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.SessionManager;
import com.example.android.paktw.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hafizh on 14/11/2015.
 */
public class DetailEventActivity extends AppCompatActivity {
    SessionManager session;
    int id_event;
    int mYear, mMonth, mDay;
    String dateChosen;
    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id_event = getIntent().getIntExtra("id_event",0);
        boolean isHistory = getIntent().getBooleanExtra("isHistory",false);

        TextView name = (TextView)findViewById(R.id.name_event);
        final TextView date = (TextView)findViewById(R.id.date_event);
        TextView detail = (TextView)findViewById(R.id.detail_event);
        Button changeDate = (Button)findViewById(R.id.btnChangeDate);

        if(isHistory == true){
            changeDate.setVisibility(new View(DetailEventActivity.this).GONE);
        }
        // Session Manager
        session = new SessionManager(DetailEventActivity.this);

        final DatabaseHelper db = new DatabaseHelper(DetailEventActivity.this);
        Event ev = db.getSingleEvent(session.getUsernameSession(), id_event);

        name.setText(ev.getName_event());
        date.setText(db.ChangeDateFormat(ev.getDate_event()));
        detail.setText(ev.getDetail_event());

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        //new alert dialog
        final DatePicker datePicker = new DatePicker(this);
        datePicker.init(mYear, mMonth, mDay, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setCalendarViewShown(false);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Ubah Tanggal");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Picker", datePicker.getYear() + " "
                        + (datePicker.getMonth() + 1) + " "
                        + datePicker.getDayOfMonth());
                dateChosen = String.valueOf(datePicker.getYear()+ "-" +(datePicker.getMonth() + 1)+ "-" + datePicker.getDayOfMonth());
                Date dateCondition = null;
                try {
                     dateCondition = myFormat.parse(dateChosen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(dateCondition.before(c.getTime())){
                    Toast.makeText(DetailEventActivity.this, "Tanggal tidak valid.", Toast.LENGTH_SHORT).show();
                }
                else{
                    date.setText(db.ChangeDateFormat(dateChosen));
                    updateDateEvent();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Log.d("Picker", "Cancelled!");
                    }
                });
        builder.setView(datePicker);
        final AlertDialog alertDialog = builder.create();

        changeDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                dpd.show();
                alertDialog.show();
            }
        });
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

//    @Override
//    public void onBackPressed() {
////        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////        startActivity(intent);
////        finish();
//    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public void updateDateEvent(){
        DatabaseHelper db = new DatabaseHelper(DetailEventActivity.this);
        String oldDate = db.getSingleEvent(session.getUsernameSession(),id_event).getDate_event();
        long difference = findDiferenceDate(oldDate, dateChosen);
        int result = (int) difference;
        Log.d("selisih", String.valueOf(result));
        int resultInverse = 0 - result;

        db.updateDateEvent(id_event, dateChosen, session.getUsernameSession());
        int i = id_event + 1;
        while(i <= db.getAllEvent(session.getUsernameSession()).size() ){
            Event event = db.getSingleEvent(session.getUsernameSession(),i);

//            Log.d("event",event.getName_event() + event.getCount_day());
            db.updateDateEvent(i,CalculateDate(event.getDate_event(),result),session.getUsernameSession());
            i++;
        }

//        for(int j = 0; j < db.getAllEvent(session.getUsernameSession()).size();j++){
//            Event event = db.getSingleEvent(session.getUsernameSession(),j);
//            if (id_event > j){
//                if(verifyDate(dateChosen,event.getDate_event()) == false){
//                    Toast.makeText(DetailEventActivity.this, "Tanggal tidak boleh kurang dari event sebelumnya", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//            }
//            else if(id_event == id_event){
//                db.updateDateEvent(id_event, dateChosen, session.getUsernameSession());
//            }
//            else{
//                db.updateDateEvent(j,CalculateDate(event.getDate_event(),result),session.getUsernameSession());
//            }
//        }

    }

    public String CalculateDate(String date, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, day); // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat newsdf = new SimpleDateFormat("yyyy-MM-dd");
        String output = newsdf.format(c.getTime());
        return output;
    }

    public long findDiferenceDate(String oldDate, String newDate){
//        String oldDate = "23 01 1997";
//        String newDate = "27 04 1997";
        long diff = 0;
        try {
            Date date1 = myFormat.parse(oldDate);
            Date date2 = myFormat.parse(newDate);
            diff = date2.getTime() - date1.getTime();
            Log.d("diference ", String.valueOf(diff));
//            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public boolean verifyDate(String dateNow, String dateBefore){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = myFormat.parse(dateNow);
            date2 = myFormat.parse(dateBefore);
//            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date1.before(date2)){
            return false;
        }
        else{
            return true;
        }
    }


}
