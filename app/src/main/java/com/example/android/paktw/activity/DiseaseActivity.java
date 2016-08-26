package com.example.android.paktw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.DiseaseAdapter;

public class DiseaseActivity extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView rv;
    private DiseaseAdapter mAdapter;
    DatabaseHelper db;
    TextView keterangan;
    public DiseaseActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_disease, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv_disease);
        keterangan = (TextView) rootView.findViewById(R.id.keterangan);
        setHasOptionsMenu(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        showDisease();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_disease, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.setGejala(newText,mAdapter,db.getAllDisease());
        if(mAdapter.getItemCount()==0){
            keterangan.setText("Hasil pencarian tidak ditemukan");
        }
        else{
            keterangan.setText("");
        }
        rv.scrollToPosition(0);
        return false;
    }

    public void showDisease(){
        db = new DatabaseHelper(getContext());
        mAdapter = new DiseaseAdapter(getContext());
        mAdapter.initializeData();
        mAdapter = new DiseaseAdapter(getContext(),db.getAllDisease());
        rv.setAdapter(mAdapter);
    }


}
