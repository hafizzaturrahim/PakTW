package com.example.android.paktw.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.CalendarAdapter;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.SessionManager;

import java.util.Calendar;


public class HomeActivity extends Fragment {
//    EditText etDate;
    int mYear, mMonth, mDay;
    String dateChosen;
    int control = 0;

    // Session Manager Class
    SessionManager session;

    public HomeActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManager(getContext());

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        etDate = (EditText) rootView.findViewById(R.id.etDate);
        TextView rencana = (TextView)rootView.findViewById(R.id.textRencana);
        TextView keterangan = (TextView)rootView.findViewById(R.id.keterangan);
        // Get a reference to the ListView, and attach this com.example.android.paktw.adapter to it.
        final RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.rv);
        DatabaseHelper db = new DatabaseHelper(getActivity());


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

//        // Launch Date Picker Dialog
//        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        // Display Selected date in EditText
////                        etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        dateChosen = String.valueOf(year+ "-" +(monthOfYear + 1)+ "-" +dayOfMonth);
//                        control = 1;
//                        Fragment fragment = new HomeActivity();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.content_body, fragment);
//                        fragmentTransaction.commit();
//                        showUpcoming(rv);
//                    }
//                }, mYear, mMonth, mDay);

        //new alert dialog
        final DatePicker datePicker = new DatePicker(getActivity());
        datePicker.init(mYear, mMonth, mDay, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setCalendarViewShown(false);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Mulai Perencanaan");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Picker", datePicker.getYear() + " "
                        + (datePicker.getMonth() + 1) + " "
                        + datePicker.getDayOfMonth());
                dateChosen = String.valueOf(datePicker.getYear()+ "-" +(datePicker.getMonth() + 1)+ "-" + datePicker.getDayOfMonth());
                control = 1;
                Fragment fragment = new HomeActivity();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_body, fragment);
                fragmentTransaction.commit();
                showUpcoming(rv);
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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dpd.show();
                alertDialog.show();
            }
        });

        if(db.getAllEvent(session.getUsernameSession()).size()== 0){
            rencana.setText("Tidak Ada Rencana");
            rv.setVisibility(rootView.GONE);
            keterangan.setText("Anda sedang tidak memiliki kalender aktif.\n" +
                    "Silahkan tekan tombol '+' untuk \n" +
                    "mengatur perencanaan tanam");
        }
        else{
//            rencana.setVisibility(rootView.GONE);
//            etDate.setVisibility(rootView.GONE);
            fab.setVisibility(rootView.GONE);
            keterangan.setVisibility(rootView.GONE);
            showUpcoming(rv);
        }
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

    public void showUpcoming(RecyclerView rv){
        rv.setHasFixedSize(true);
        DatabaseHelper db = new DatabaseHelper(getContext());
        LinearLayoutManager listEvent = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(listEvent);
        rv.setItemAnimator(new DefaultItemAnimator());
        CalendarAdapter adapter;
        adapter = new CalendarAdapter(getActivity());
//        adapter.startCalendarPlan("2015-12-15");
        if(control == 1){
            adapter.startCalendarPlan(dateChosen);
            adapter.initializeData();
        }
        adapter.notifyDataSetChanged();
        rv.setAdapter(new CalendarAdapter(db.getActiveEvent(session.getUsernameSession()),getContext()));
    }
}
