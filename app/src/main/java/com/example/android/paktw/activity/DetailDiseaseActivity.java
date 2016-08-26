package com.example.android.paktw.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.SessionManager;
import com.example.android.paktw.model.Disease;

/**
 * Created by Hafizh on 14/11/2015.
 */
public class DetailDiseaseActivity extends AppCompatActivity {
    SessionManager session;
    int id_disease;
    int mYear, mMonth, mDay;
    String dateChosen;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_disease);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id_disease = getIntent().getIntExtra("id_disease",0);


        TextView name_d = (TextView)findViewById(R.id.name_disease);
        TextView penyebab = (TextView)findViewById(R.id.penyebab_disease);
        TextView gejala = (TextView)findViewById(R.id.gejala_disease);
        TextView pengendalian = (TextView)findViewById(R.id.pengendalian);

        // Session Manager
        session = new SessionManager(DetailDiseaseActivity.this);

        DatabaseHelper db = new DatabaseHelper(DetailDiseaseActivity.this);
        Disease ds = db.getSingleDisease(id_disease);

        name_d.setText(ds.getName());
        penyebab.setText(ds.getPenyebab_disease());
        gejala.setText(ds.getGejala_disease());
        pengendalian.setText(ds.getPengendalian());
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

}
