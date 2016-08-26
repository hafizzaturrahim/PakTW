package com.example.android.paktw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.activity.DetailEventActivity;
import com.example.android.paktw.model.CalendarPlan;
import com.example.android.paktw.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.EventViewHolder>{
    List<Event> event;
    Context mcontext;
    DatabaseHelper db;

    // Session Manager Class
    SessionManager session;

    public CalendarAdapter(Context context){
        mcontext = context;
        db = new DatabaseHelper(context);

        // Session Manager
        session = new SessionManager(context);
    }

    public CalendarAdapter(List<Event> event, Context context){
        this.event = event;
        mcontext = context;
        db = new DatabaseHelper(context);

        // Session Manager
        session = new SessionManager(context);
        setExpiredData();
        Log.e("call setExpiredData", "mbuh");
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        CardView cv;
        private ClickListener clickListener;
        TextView EventName;
        TextView EventDate;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            EventName = (TextView)itemView.findViewById(R.id.event_name);
            EventDate = (TextView)itemView.findViewById(R.id.event_date);

            // We set listeners to the whole item view, but we could also
            // specify listeners for the title or the icon.
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /* Setter for listener. */
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }

        public interface ClickListener {
            void onClick(View v, int position, boolean isLongClick);
        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);

        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int position) {
        eventViewHolder.EventName.setText(event.get(position).getName_event());
        eventViewHolder.EventDate.setText(event.get(position).getDate_event());
//        Log.d("st " + String.valueOf(event.get(position).getId_event()),
//                event.get(position).getName_event() + event.get(position).getStatus());
        
        eventViewHolder.setClickListener(new EventViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                mcontext = v.getContext();
//                Toast.makeText(mcontext, "tes " + event.get(position).getName_event(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(mcontext, DetailEventActivity.class);
                intent.putExtra("id_event",event.get(position).getId_event());
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void initializeData(){
        Log.d("create calendar sucess", "caradapter");
        CalendarPlan cp = db.getCalendarPlan(session.getUsernameSession());
        String start_date = cp.getDate_start();
        if(db.getAllEvent(session.getUsernameSession()).size() == 0){
            db.createEvent("Pemberian Pupuk Kandang",CalculateDate(start_date,1),"Memberikan pupuk kandang ke tanah yang sudah siap untuk diatanami wortel dengan volume minimal 50 kg untuk 100 meter",cp.getId_calendar());
            db.createEvent("Penyebaran Benih ",CalculateDate(start_date,2),"Menabur benih ke ladang yang sudah siap untuk ditanami wortel dengan volume minimal 200 gram untuk tiap 100 meter",cp.getId_calendar());
            db.createEvent("Pemberian Obat / Pestisida ",CalculateDate(start_date,12),"Pemberian obat pupuk daun + pestisida (fungisida bila perlu)",cp.getId_calendar());
            db.createEvent("Penjarangan Wortel ",CalculateDate(start_date,15),"Mengurangi jumlah wortel populasi Mengambil batang wortel sampai tersisa batang wortel dengan jarak 5 cm an, sekaligus mencabut rumput liar ",cp.getId_calendar());
            db.createEvent("Pemberian Obat / Pestisida ",CalculateDate(start_date,22),"Pemberian obat pupuk daun + pestisida (fungisida bila perlu)",cp.getId_calendar());
            db.createEvent("Pemberian Pupuk Kimia ",CalculateDate(start_date,30),"Pemberian pupuk kimia tergantung dari jenis tanahnya, disarankan yang mengandung banyak  kandungan oksigen dan nitrogen ",cp.getId_calendar());
            db.createEvent("Pencabutan Rumput Liar ",CalculateDate(start_date,32),"Mencabut rumput liar sekitar wortel ",cp.getId_calendar());
            db.createEvent("Pemberian Pupuk Kimia ",CalculateDate(start_date,40),"Pemberian pupuk kimia tergantung dari jenis tanahnya, disarankan yang mengandung banyak  kandungan oksigen dan nitrogen ",cp.getId_calendar());
            db.createEvent("Pemberian Pupuk Kimia ",CalculateDate(start_date,45),"Pemberian pupuk kimia tergantung dari jenis tanahnya, disarankan yang mengandung banyak  kandungan oksigen dan nitrogen ",cp.getId_calendar());
            db.createEvent("Panen ",CalculateDate(start_date,60),"Jabut wortel \uF04A",cp.getId_calendar());
        }
//        db.deleteEvent(1010);
    }

    //insert to history table for every event that went before current day
    public void setExpiredData(){
        List<Event> evToHistory = db.getOldEvent(session.getUsernameSession());
        CalendarPlan calendarPlan = new CalendarPlan();
        calendarPlan = db.getCalendarPlan(session.getUsernameSession());
        for(int i = 0; i < evToHistory.size();i++){
            if(!evToHistory.get(i).getStatus().equals("expired")){
            Log.d("Event " +evToHistory.get(i).getId_event() ,evToHistory.get(i).getName_event() + evToHistory.get(i).getStatus());
                 db.createHistory(
                        evToHistory.get(i).getId_event(),
                        evToHistory.get(i).getId_calendar(),
                        evToHistory.get(i).getDate_event(),
                        evToHistory.get(i).getCount_day(),
                        evToHistory.get(i).getId_event(),
                        evToHistory.get(i).getName_event(),
                        evToHistory.get(i).getDate_event(),
                        evToHistory.get(i).getDetail_event());
            }
        }
    }

    public void startCalendarPlan(String newdate){
//        db.deleteCalendarPlan(1);
        CalendarPlan cp = new CalendarPlan();
//        cp.setId_calendar(1);
        cp.setDate_start(newdate);
        cp.setStatus("aktif");
        cp.setUsername(session.getUsernameSession());
        db.createCalendarPlan(cp);
    }

    //count date for event from date_start in Calendar Plan
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



}

