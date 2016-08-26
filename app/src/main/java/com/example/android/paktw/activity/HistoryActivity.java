package com.example.android.paktw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.HistoryAdapter;


public class HistoryActivity extends Fragment {

    public HistoryActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Get a reference to the ListView, and attach this com.example.android.paktw.adapter to it.
        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.rv_history);
        TextView keterangan = (TextView)rootView.findViewById(R.id.keterangan);
        LinearLayoutManager listEvent = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(listEvent);
        DatabaseHelper db = new DatabaseHelper(getContext());
        if(db.getAllHistory().size() == 0){
            keterangan.setText("Tidak Ada Histori");
            rv.setVisibility(rootView.GONE);
        }
        else{
            keterangan.setVisibility(rootView.GONE);
            showHistory(rv);
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    // This method creates an ArrayList that has three Person objects
    // Checkout the project associated with this tutorial on Github if
    // you want to use the same images.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showHistory(RecyclerView rv){
        rv.setItemAnimator(new DefaultItemAnimator());
        DatabaseHelper db = new DatabaseHelper(getContext());
        rv.setAdapter(new HistoryAdapter(db.getAllHistory(),getContext()));
    }
}

