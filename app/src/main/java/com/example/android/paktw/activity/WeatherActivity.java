package com.example.android.paktw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.WeatherAdapter;

public class WeatherActivity extends Fragment {
    public WeatherActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        // Get a reference to the ListView, and attach this com.example.android.paktw.adapter to it.
        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.rv_weather);
        LinearLayoutManager listWeather = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(listWeather);
        DatabaseHelper db = new DatabaseHelper(getContext());
        rv.setItemAnimator(new DefaultItemAnimator());
        WeatherAdapter adapter = new WeatherAdapter(getContext());
        adapter.callGetData(getActivity());
        adapter = new WeatherAdapter(db.getAllWeather());
        rv.setAdapter(adapter);

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

    public void showCuaca(RecyclerView rv){
        DatabaseHelper db = new DatabaseHelper(getContext());
        rv.setItemAnimator(new DefaultItemAnimator());
        WeatherAdapter adapter = new WeatherAdapter(getContext());
        adapter.callGetData(getActivity());
        adapter = new WeatherAdapter(db.getAllWeather());
        rv.setAdapter(adapter);
    }

}
