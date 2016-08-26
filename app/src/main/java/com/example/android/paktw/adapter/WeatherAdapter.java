package com.example.android.paktw.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.paktw.R;
import com.example.android.paktw.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    List<Weather> weather;
    Context mcontext;

    public WeatherAdapter(List<Weather> weather){
        this.weather = weather;
    }

    public WeatherAdapter(Context context){
        mcontext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        CardView cv;
        private ClickListener clickListener;
        TextView dateWeather;
        TextView temperature;
        TextView wind;
        TextView humidity;
        TextView status;
        ImageView weatherIcon;


        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_weather);
            dateWeather = (TextView)itemView.findViewById(R.id.date);
            temperature = (TextView)itemView.findViewById(R.id.temperature);
            humidity = (TextView)itemView.findViewById(R.id.humidity);
            wind = (TextView)itemView.findViewById(R.id.wind);
            status = (TextView)itemView.findViewById(R.id.status);
            weatherIcon = (ImageView)itemView.findViewById(R.id.condition_img);

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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_weather, viewGroup, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder weatherViewHolder, int position) {
//        weatherViewHolder.city.setText(weather.get(position).getCity());
        setWeatherItem(weatherViewHolder,position);
    }

    @Override
    public int getItemCount() {
        return weather.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static int getIconResourceForWeatherCondition(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    public void setWeatherItem(ViewHolder weatherViewHolder,int position){
        if(position == 0){
            weatherViewHolder.dateWeather.setText("Today");
        }else{
            weatherViewHolder.dateWeather.setText(weather.get(position).getDate_weather());
        }
        weatherViewHolder.temperature.setText(weather.get(position).getTemperature()+ " \u2103");
        weatherViewHolder.humidity.setText(String.valueOf(weather.get(position).getHumidity())+ " %");
        weatherViewHolder.wind.setText(String.valueOf(weather.get(position).getWind())+ " m/s");
        weatherViewHolder.status.setText(weather.get(position).getStatus());
        weatherViewHolder.weatherIcon.setImageResource(
                getIconResourceForWeatherCondition(weather.get(position).getId_weather()));

        weatherViewHolder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
//                mcontext = v.getContext();
//                Toast.makeText(mcontext, "tes " + weather.get(position).getName_event(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mcontext, DetailEventActivity.class);
////                mcontext.startActivity(intent);
            }
        });


    }
    public void callGetData(Context newContext){
        getDataWeather wa = new getDataWeather(newContext);
        wa.execute();
    }

    private class getDataWeather extends AsyncTask<String, Void, Void> {
        private ProgressDialog pDialog;
        Context mContext;
        int control = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MM-yyyy");
        public getDataWeather(Context context) {
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

//            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo info = cm.getActiveNetworkInfo();
//            if (info != null) {
//                if (!info.isConnected()) {
//                    Toast.makeText(mContext, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
//                    jsonStr = null;
//                }
//            }

//            HttpURLConnection urlConnection = null;
//
//            try {
//                URL url = new URL(jsonUrl);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                readStream(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally{
//                urlConnection.disconnect();
//            }

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
                        Date dateWeather = new Date();
                        double windSpeed;
                        int high;
                        int low;
                        double humidity;
                        String description;
                        int id_status;
                        JSONObject dailyWeather = weatherArray.getJSONObject(i);

                        // Cheating to convert this to UTC time, which is what we want anyhow

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
                        //check if table Weather has same data
//                    IDWeather = db.getIDWeather();
//                    for(int j= 0; j < IDWeather.size();j++){
//                        if(String.valueOf(dt).equals(IDWeather.get(j))){
//                            Log.d("Response: ", "> " + j);
//                        }
//                    }
                        db.deleteWeather(dt);
                        db.createWeather(dt, date, String.valueOf(high),
                                windSpeed, humidity, description,id_status);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Tidak bisa mendapat data dari URL yang diminta");
                control = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //check if there is no connection with control checking
            if(control == 0){
                Toast.makeText(mContext, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        }

        public String ConvertFromUnixString(long unix){
            Date date = new Date(unix * 1000L); // *1000 is to convert seconds to milliseconds
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // give a timezone reference for formating (see comment at the bottom
            return sdf.format(date);
        }

    }
}