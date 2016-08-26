package com.example.android.paktw.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.paktw.model.CalendarPlan;
import com.example.android.paktw.model.Disease;
import com.example.android.paktw.model.Event;
import com.example.android.paktw.model.History;
import com.example.android.paktw.model.User;
import com.example.android.paktw.model.Weather;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat event
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_paktw";

    // Table Names
    private static final String CALENDAR_PLAN = "CalendarPlan";
    private static final String EVENT = "Event";
    private static final String WEATHER = "Weather";

    // Column names
    private static final String KEY_ID_CAL = "id_calendar";
    private static final String KEY_DATE = "date_start";
    private static final String KEY_STATS = "status";
    private static final String KEY_DAY = "count_day";

    // Column names
    private static final String KEY_ID_EVENT = "id_event";
    private static final String KEY_NAME = "name_event";
    private static final String KEY_DATE_EVENT = "date_event";
    private static final String KEY_DETAIL = "detail_event";

    // Table Create Statements

    private static final String CREATE_USER =
            "CREATE TABLE User ( " +
                    "id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "password TEXT)";

    // CalendarPlan table create statement
    private static final String CREATE_CALENDAR_PLAN =
            "CREATE TABLE CalendarPlan (" +
                    "id_calendar INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date_start DATETIME," +
                    "status TEXT," +
                    "username TEXT REFERENCES User(username))";

    // Event table create statement
    private static final String CREATE_EVENT =
            "CREATE TABLE Event (" +
                    "id_event INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name_event TEXT," +
                    "date_event DATETIME," +
                    "detail_event TEXT," +
                    "status TEXT,"+
                    "id_calendar INTEGER REFERENCES CalendarPlan(id_calendar))";

    // Event table create statement
    private static final String CREATE_WEATHER =
            "CREATE TABLE Weather (" +
                    "id_weather LONG PRIMARY KEY," +
//                    "city TEXT," +
                    "date_weather DATETIME," +
                    "temperature TEXT," +
                    "wind_speed DOUBLE," +
                    "humidity DOUBLE," +
                    "condition TEXT," +
                    "id_status INTEGER )";

    private static final String CREATE_DISEASE =
            "CREATE TABLE Disease ( " +
                    "id_disease INTEGER PRIMARY KEY," +
                    "name TEXT," +
                    "penyebab_disease TEXT," +
                    "gejala_disease TEXT," +
                    "pengendalian TEXT )";

    private static final String CREATE_HISTORY =
            "CREATE TABLE History ( " +
                    "id_history INTEGER PRIMARY KEY," +
                    "id_calendar INTEGER," +
                    "date_start DATETIME," +
                    "count_day INTEGER," +
                    "id_event INTEGER," +
                    "name_event TEXT," +
                    "date DATETIME, " +
                    "detail_event TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_CALENDAR_PLAN);
        db.execSQL(CREATE_EVENT);
        db.execSQL(CREATE_WEATHER);
        db.execSQL(CREATE_DISEASE);
        db.execSQL(CREATE_HISTORY);

        Log.e(LOG, "create all tables");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS CalendarPlan");
        db.execSQL("DROP TABLE IF EXISTS Event");
        db.execSQL("DROP TABLE IF EXISTS Disease");
        db.execSQL("DROP TABLE IF EXISTS History");
        db.execSQL("DROP TABLE IF EXISTS Weather");
        // create new tables
        onCreate(db);
    }

    /**
     * Creating a CalendarPlan
     */
    public void createUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = new User();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        // insert row
        db.insert("User", null, values);
        // Closing database connection
        db.close();
    }

    /**
     * Get single Data User to get password
     */
    public String getPassUser(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=db.query("User", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "TIDAK ADA";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }

    /**
     * Check if username already exist
     */
    public String checkUsername(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=db.query("User", null, " username=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "N";
        }else{
            return "Y";
        }
    }

    /**
     * Creating a CalendarPlan
     */
    public void createCalendarPlan(CalendarPlan cal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID_CAL, cal.getId_calendar());
        values.put(KEY_DATE, cal.getDate_start());
        values.put(KEY_STATS, cal.getStatus());
        values.put("username", cal.getUsername());

        // insert row
        db.insert(CALENDAR_PLAN, null, values);
        // Closing database connection
        db.close();
    }

    /**
     * get single CalendarPlan
     */
    public CalendarPlan getCalendarPlan(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CALENDAR_PLAN + " WHERE username = '" +username+ "'" ;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        CalendarPlan cp = new CalendarPlan();
        cp.setId_calendar(c.getInt(c.getColumnIndex(KEY_ID_CAL)));
        cp.setDate_start((c.getString(c.getColumnIndex(KEY_DATE))));
        cp.setStatus(c.getString(c.getColumnIndex(KEY_STATS)));
        cp.setUsername(c.getString(c.getColumnIndex("username")));

        c.close();
        return cp;
    }

    /*
     * Updating a CalendarPlan
    */
    public int updateCalendarPlan(CalendarPlan calendarPlan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATS, calendarPlan.getStatus());

        // updating row
        return db.update(CALENDAR_PLAN, values, KEY_ID_CAL + " = ?",
                new String[] { String.valueOf(calendarPlan.getId_calendar()) });
    }

    /*
    * Deleting a CalendarPlan
    */
    public void deleteCalendarPlan(int id_event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CALENDAR_PLAN, KEY_ID_CAL + " = ?",
                new String[] { String.valueOf(id_event) });
    }

    /*
    * Creating Event
    */
    public long createEvent(String name_event, String date_event, String detail_event, int id_calendar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name_event);
        values.put(KEY_DATE_EVENT, date_event);
        values.put(KEY_DETAIL, detail_event);
        values.put(KEY_STATS, "aktif");
        values.put("id_calendar", id_calendar);

        // insert row
        return db.insert(EVENT, null, values);
    }

    /**
     * getting all Event
     * */
    public List<Event> getAllEvent(String username) {
        List<Event> evList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Event E JOIN CalendarPlan C ON E.id_calendar = C.id_calendar " +
                "WHERE username = '" +username+ "'" ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event t = new Event();
                t.setId_event(c.getInt((c.getColumnIndex(KEY_ID_EVENT))));
                t.setName_event(c.getString(c.getColumnIndex(KEY_NAME)));
                t.setDate_event(c.getString(c.getColumnIndex(KEY_DATE_EVENT)));
                t.setStatus(c.getString(c.getColumnIndex(KEY_STATS)));
                t.setDetail_event(c.getString(c.getColumnIndex(KEY_DETAIL)));
//                t.setCount_day(c.getInt(c.getColumnIndex("count_day")));
                // adding to evList list
                evList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return evList;
    }

    /**
     * getting all Event
     * */
    public List<Event> getActiveEvent(String username) {
        List<Event> evList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Event E JOIN CalendarPlan C ON E.id_calendar = C.id_calendar " +
                "WHERE date_event >= date('now') and username = '" +username+ "' " ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event t = new Event();
                t.setId_event(c.getInt((c.getColumnIndex(KEY_ID_EVENT))));
                t.setName_event(c.getString(c.getColumnIndex(KEY_NAME)));
                t.setDate_event(ChangeDateFormat(c.getString(c.getColumnIndex(KEY_DATE_EVENT))));
                t.setStatus(c.getString(c.getColumnIndex(KEY_STATS)));
                t.setDetail_event(c.getString(c.getColumnIndex(KEY_DETAIL)));
//                t.setCount_day(c.getInt(c.getColumnIndex("count_day")));
                // adding to evList list
                evList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return evList;
    }

    /**
     * getting all name and date Event
     * */
    public Event getSingleEvent(String username, int id_event) {
        List<Event> evList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM Event E JOIN CalendarPlan C ON E.id_calendar = C.id_calendar " +
                "WHERE id_event = " +id_event+ " and username = '" +username+ "'" ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Event t = new Event();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                t.setId_event(c.getInt(c.getColumnIndex("id_event")));
                t.setName_event(c.getString(c.getColumnIndex(KEY_NAME)));
                t.setDate_event(c.getString(c.getColumnIndex(KEY_DATE_EVENT)));
                t.setDetail_event(c.getString(c.getColumnIndex("detail_event")));
//                t.setCount_day(c.getInt(c.getColumnIndex("count_day")));
                // adding to evList list
                evList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return t;
    }

    /**
     * change date an Event
     * */
    public int updateDateEvent(int id_event, String newDate, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        CalendarPlan cp = getCalendarPlan(username);
        int id_cal = cp.getId_calendar();
        ContentValues values = new ContentValues();
        values.put("date_event", newDate);
        Log.d(LOG, "update date " + id_event);

        // updating row
        return db.update("Event", values, "id_event = ? AND id_calendar = ?",
                new String[]{String.valueOf(id_event), String.valueOf(id_cal)});
    }

    /**
     * get old Event
     * */
    public List<Event> getOldEvent(String username) {
        List<Event> evList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Event E JOIN CalendarPlan C ON E.id_calendar = C.id_calendar " +
                "WHERE date_event < date('now') AND username = '" +username+ "'" ;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event t = new Event();
                t.setId_event(c.getInt(c.getColumnIndex("id_event")));
                t.setName_event(c.getString(c.getColumnIndex(KEY_NAME)));
                t.setDate_event(c.getString(c.getColumnIndex(KEY_DATE_EVENT)));
                t.setDetail_event(c.getString(c.getColumnIndex("detail_event")));
                t.setStatus(c.getString(c.getColumnIndex("status")));
//                t.setCount_day(c.getInt(c.getColumnIndex("count_day")));
                t.setId_calendar(c.getInt(c.getColumnIndex("id_calendar")));
                evList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return evList;
    }

    /**
     * change status Event
     * */
    public int changeStatusEvent(int id_event, int id_calendar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", "'expired'");

        Log.d(LOG, "changing event to expired " + id_event);
        // updating row
        return db.update("Event", values, "id_event = ? AND id_calendar = ?",
                new String[] { String.valueOf(id_event), String.valueOf(id_calendar)});
    }
    /**
     * Updating a Event
     */
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName_event());
        values.put(KEY_DATE_EVENT, event.getDate_event());
        values.put(KEY_STATS, event.getStatus());
        values.put(KEY_DETAIL, event.getDetail_event());

        // updating row
        return db.update(EVENT, values, KEY_ID_EVENT + " = ?",
                new String[] { String.valueOf(event.getId_event()) });
    }

    public int updateEvent(int id_event, String new_date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE_EVENT, new_date);

        // updating row
        return db.update(EVENT, values, KEY_ID_EVENT + " = ?",
                new String[] { Integer.toString(id_event) } );
    }

    /*
    * Deleting a CalendarPlan
    */
    public void deleteEvent(int id_event) {
        SQLiteDatabase db = this.getWritableDatabase();

        if( id_event == 1010){
            db.delete(EVENT, null,null);
            Log.e(LOG, "Deleting All Event");
        }
        else{
            db.delete(EVENT, KEY_ID_CAL + " = ?",
                    new String[]{String.valueOf(id_event)});
            Log.e(LOG, "Deleting Partial Event");
        }

    }

    /*
    * Creating Weather
    */
    public long createWeather(
            long id_weather, String date_weather, String temperature,
            double wind_speed, double humidity, String condition, int id_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_weather", id_weather);
//        values.put("city", city);
        values.put("date_weather", date_weather);
        values.put("temperature", temperature);
        values.put("wind_speed", wind_speed);
        values.put("humidity", humidity);
        values.put("condition", condition);
        values.put("id_status", id_status);

        // insert row
        long weather = db.insert(WEATHER, null, values);
        return weather;
    }

    /**
     * getting all Weather
     * */
    public List<Weather> getAllWeather() {
        List<Weather> evList = new ArrayList<Weather>();
        String selectQuery = "SELECT * FROM (SELECT * FROM Weather ORDER BY id_weather DESC LIMIT 6) ORDER BY id_weather ASC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Weather t = new Weather();
                t.setId_weather(c.getInt((c.getColumnIndex("id_weather"))));
//                t.setCity(c.getString(c.getColumnIndex("city")));
                t.setDate_weather(c.getString(c.getColumnIndex("date_weather")));
                t.setTemperature(c.getString(c.getColumnIndex("temperature")));
                t.setWind(c.getDouble(c.getColumnIndex("wind_speed")));
                t.setHumidity(c.getDouble(c.getColumnIndex("humidity")));
                t.setStatus(c.getString(c.getColumnIndex("condition")));
                t.setId_weather(c.getInt(c.getColumnIndex("id_status")));
                // adding to evList list
                evList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return evList;
    }

    /**
     * getting all ID Weather
     * */
    public List<Weather> getIDWeather() {
        List<Weather> evList = new ArrayList<Weather>();
        String selectQuery = "SELECT id_weather FROM Weather";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Weather t = new Weather();
                t.setId_weather(c.getInt((c.getColumnIndex("id_weather"))));
                // adding to evList list
                evList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return evList;
    }

    /**
     * getting all ID Weather
     * */
    public String getCityWeather() {
        List<Weather> evList = new ArrayList<Weather>();
        String dataCity = null;
        String selectQuery = "SELECT city FROM Weather LIMIT 1";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() == 0){
            c.close();
            return "nothing";
        }else{
            c.moveToFirst();
            Log.e(LOG, "dataCity : " + c.getString(0));
            c.close();
            return c.getString(0);
        }
    }

    /*
    * Deleting a Weather
    */
    public void deleteWeather(long id_weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "Deleting...");
        db.delete("Weather", "id_weather = ?",
                new String[] { String.valueOf(id_weather) });
    }

    /*
    * Creating Disease
    */
    public long createDisease(
            int id_disease, String name,String penyebab, String gejala, String pengobatan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_disease", id_disease);
        values.put("name", name);
        values.put("penyebab_disease", penyebab);
        values.put("gejala_disease", gejala);
        values.put("pengendalian",pengobatan);

        // insert row
        long disease = db.insert("Disease", null, values);
        return disease;
    }

    /**
     * getting all Disease
     * */
    public List<Disease> getAllDisease() {
        List<Disease> dsList = new ArrayList<Disease>();
        String selectQuery = "SELECT * FROM Disease";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Disease t = new Disease();
                t.setId_disease(c.getInt((c.getColumnIndex("id_disease"))));
                t.setName(c.getString(c.getColumnIndex("name")));
                t.setPenyebab_disease(c.getString(c.getColumnIndex("penyebab_disease")));
                t.setGejala_disease(c.getString(c.getColumnIndex("gejala_disease")));
                t.setPengendalian(c.getString(c.getColumnIndex("pengendalian")));
                // adding to evList list
                dsList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return dsList;
    }


    /**
     * getting single Disease
     * */
    public Disease getSingleDisease(int id_disease) {
        List<Disease> dsList = new ArrayList<Disease>();
        Disease t = new Disease();
        String selectQuery = "SELECT * FROM Disease WHERE id_disease = " +id_disease;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                t.setId_disease(c.getInt((c.getColumnIndex("id_disease"))));
                t.setName(c.getString(c.getColumnIndex("name")));
                t.setPenyebab_disease(c.getString(c.getColumnIndex("penyebab_disease")));
                t.setGejala_disease(c.getString(c.getColumnIndex("gejala_disease")));
                t.setPengendalian(c.getString(c.getColumnIndex("pengendalian")));
                // adding to evList list
                dsList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return t;
    }
    /*
    * Deleting a Disease
    */
    public void deleteDisease() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Disease",null,null);
    }


    /*
    * Creating History
    */
    public long createHistory(
            int id_history, int id_calendar, String date_start, int count_day,
            int id_event, String name_event, String date, String detail_event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_history", id_history);
        values.put("id_calendar", id_calendar);
        values.put("date_start", date_start);
        values.put("id_event", id_event);
        values.put("name_event", name_event);
        values.put("date", date);
        values.put("detail_event", detail_event);
        values.put("count_day", count_day);

        Log.e(LOG, "Create a history");
        // insert row
        long history = db.insert("History", null, values);
        return history;
    }

    /**
     * getting all History
     * */
    public List<History> getAllHistory() {
        List<History> histList = new ArrayList<History>();
        String selectQuery = "SELECT * FROM History";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                History t = new History();
                t.setId_history(c.getInt((c.getColumnIndex("id_history"))));
                t.setId_calendar(c.getInt((c.getColumnIndex("id_calendar"))));
                t.setDate_start(c.getString(c.getColumnIndex("date_start")));
                t.setCount_day(c.getInt(c.getColumnIndex("count_day")));
                t.setId_event(c.getInt(c.getColumnIndex("id_event")));
                t.setName_event(c.getString(c.getColumnIndex("name_event")));
                t.setDate(ChangeDateFormat(c.getString(c.getColumnIndex("date"))));
                t.setDetail_event(c.getString(c.getColumnIndex("detail_event")));
                // adding to evList list
                histList.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return histList;
    }

    public int getIDHistory(int position){
        History hs = new History();
        String selectQuery = "SELECT * FROM History WHERE id_history = " +position;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                hs.setId_history(c.getInt(c.getColumnIndex("id_history")));
            } while (c.moveToNext());
        }
        c.close();
        return hs.getId_history();
    }

    public void deleteHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "Deleting from history...");
        db.delete("History", null,null);
    }

    public void deleteHistory(int id_history) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Deleting : ", String.valueOf(id_history));
        db.delete("History", "id_history = ?",
                new String[] {String.valueOf(id_history)});
    }

    public String ChangeDateFormat(String date){
        Locale id = new Locale("in", "ID");
        String pattern = "yyyy-MM-dd";
        Date dateOld = null;

        try{
            dateOld = new SimpleDateFormat(pattern, id).parse(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf;
        new SimpleDateFormat(pattern, id);

        DateFormatSymbols dfs = new DateFormatSymbols(id);

        String[] days = dfs.getWeekdays();
        String newDays[] = new String[days.length];
        for (int i = 0; i < days.length; i++) {
            newDays[i] = days[i];
        }

        dfs.setWeekdays(newDays);

        String[] Months = dfs.getMonths();
        String months[] = new String[Months.length];
        for (int j = 0; j < Months.length; j++) {
            months[j] = Months[j].toUpperCase();
        }

        dfs.setShortMonths(months);

        String Newpattern = "EEEE, dd MMMM yyyy";
        sdf = new SimpleDateFormat(Newpattern, dfs);
        String after = sdf.format(dateOld);
        return after;
    }

}
