package com.example.android.paktw.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Hafizh on 28/11/2015.
 */
public class taskWeather extends AsyncTask<String, Void, Void> {
    private ProgressDialog pDialog;
    Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MM-yyyy");
    public taskWeather(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Fetching data");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        // URL to get contacts JSON
        String jsonUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?" +
                "q=Malang&units=metric&cnt=6&APPID=a4f161b4c56a2e0a250cf9c5ecf892b8";

        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(jsonUrl, ServiceHandler.GET);

        Log.d("Response: ", "> " + jsonStr);
        DatabaseHelper db = new DatabaseHelper(mContext);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONObject cityObj = jsonObj.getJSONObject("city");
                String cityName = cityObj.getString("name");

                JSONArray weatherArray = jsonObj.getJSONArray("list");

                Time dayTime = new Time();
                dayTime.setToNow();

                // we start at the day returned by local time. Otherwise this is a mess.
                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                // now we work exclusively in UTC
                dayTime = new Time();

                // looping through All Contacts
                for (int i = 0; i < weatherArray.length(); i++) {
                    long dt;
                    int weatherId;
                    long dateTime;
                    Date dateWeather = new Date();
                    double windSpeed;
                    int high;
                    int low;
                    double humidity;
                    String description;
                    int id_status;
                    JSONObject dailyWeather = weatherArray.getJSONObject(i);

                    // Cheating to convert this to UTC time, which is what we want anyhow
                    dateTime = dayTime.setJulianDay(julianStartDay + i);

                    dt = dailyWeather.getLong("dt");
                    humidity = dailyWeather.getDouble("humidity");
                    windSpeed = dailyWeather.getDouble("speed");

                    JSONObject weatherObject =
                            dailyWeather.getJSONArray("weather").getJSONObject(0);
                    description = weatherObject.getString("description");
                    id_status = weatherObject.getInt("id");
                    JSONObject tempObject = dailyWeather.getJSONObject("temp");
                    high = tempObject.getInt("max");
                    low = tempObject.getInt("min");

                    String date = ConvertFromUnixString(dt);
                    try {
                        dateWeather = sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //check if table Weather has same data
//                    IDWeather = db.getIDWeather();
//                    for(int j= 0; j < IDWeather.size();j++){
//                        if(String.valueOf(dt).equals(IDWeather.get(j))){
//                            Log.d("Response: ", "> " + j);
//                        }
//                    }
                    db.deleteWeather(dt);
                    db.createWeather(dt,date, String.valueOf(high), windSpeed, humidity, description,id_status);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Tidak bisa mendapat data dari URL yang diminta");
            pDialog.dismiss();
            Toast.makeText(mContext, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
//                Toast.makeText(HospitalActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

        /**
         * Updating parsed JSON data into ListView
         * */
//        ListAdapter adapter = new SimpleAdapter(
//                HospitalActivity.this, resultList,
//                R.layout.list_data_hospital, new String[] { "name", "vicinity"},
//                new int[] { R.id.textHospital,R.id.vicinityHospital});
//        lv.setAdapter(adapter);

    }

    public String ConvertFromUnixString(long unix){
        Date date = new Date(unix * 1000L); // *1000 is to convert seconds to milliseconds
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // give a timezone reference for formating (see comment at the bottom
        return sdf.format(date);
    }

}
