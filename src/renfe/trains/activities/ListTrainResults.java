package renfe.trains.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import renfe.trains.R;
import renfe.trains.model.TrainItem;
import renfe.trains.provider.Timetable;
import renfe.trains.provider.Train;
import renfe.trains.services.RenfeXHR;
import renfe.trains.views.TrainAdapterView;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListTrainResults extends ListActivity {

    private static final String TAG = "ListTrainResults";

    private static final Uri TIMETABLE_URI = renfe.trains.provider.Timetable.CONTENT_URI;

    private static final Uri TRAIN_URI = renfe.trains.provider.Train.CONTENT_URI;

    private final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    private Cache cache = Cache.getInstance();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set empty list
        TrainAdapter adapter = new TrainAdapter(this, Collections.EMPTY_LIST);
        setListAdapter(adapter);

        // Get parameters
        Bundle parameters = getIntent().getExtras();
        String origin = (String) parameters.get("origin");
        String destination = (String) parameters.get("destination");
        Date date = (Date) parameters.get("date");

        // Set route
        TextView tvRoute = (TextView) findViewById(renfe.trains.R.id.route);
        tvRoute.setText(origin + " - " + destination);

        // Do query
        List<TrainItem> trains = searchTrains(origin, destination, date);
        adapter = new TrainAdapter(this, trains);
        setListAdapter(adapter);
    }

    private List<TrainItem> searchTrains(String origin, String destination,
            Date date) {
        List<TrainItem> result = new ArrayList<TrainItem>();

        renfe.trains.model.Timetable timetable = new renfe.trains.model.Timetable(
                origin, destination, date);
        result = cache.searchTrains(this, origin, destination, date);
        if (result.isEmpty()) {
            result = searchTrainsXHR(origin, destination, date);
            cache.save(this, timetable, result);
        }
        return result;
    }

    private List<TrainItem> searchTrainsXHR(String origin, String destination,
            Date date) {

        RenfeXHR xhr = new RenfeXHR();

        Map<String, String> params = new HashMap<String, String>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("date", toISO8601(date));
        params.put("output", "json");
        xhr.execute(params);

        return parseTrainList(xhr.getResponseAsString());
    }

    private List<TrainItem> parseTrainList(String json) {
        List<TrainItem> result = new ArrayList<TrainItem>();
        try {
            JSONArray response = new JSONArray(json);
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);
                TrainItem trainItem = new TrainItem();
                trainItem.setCode(object.getString("code"));
                trainItem.setArrive(object.getString("arrive"));
                trainItem.setDeparture(object.getString("departure"));
                trainItem.setLength(object.getString("length"));
                result.add(trainItem);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private String toISO8601(Date date) {
        return iso8601DateFormat.format(date);
    }

    /**
     *
     * @author Diego Pino <dpino@igalia.com>
     *
     */
    private class TrainAdapter extends BaseAdapter {

        private Context context;

        private List<TrainItem> trains;

        public TrainAdapter(Context context, List<TrainItem> trains) {
            this.context = context;
            this.trains = trains;
        }

        public int getCount() {
            return trains.size();
        }

        public Object getItem(int position) {
            return trains.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TrainItem train = trains.get(position);
            return new TrainAdapterView(this.context, train);
        }

    }

    /**
     *
     * @author Diego Pino <dpino@igalia.com>
     *
     */
    private static class Cache {

        private final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");

        private static Cache cache;

        private Cache() {

        }

        public static Cache getInstance() {
            if (cache == null) {
                cache = new Cache();
            }
            return cache;
        }

        public List<TrainItem> searchTrains(Activity activity, String origin,
                String destination, Date date) {
            List<TrainItem> result = new ArrayList<TrainItem>();
            Long idTimetable = searchTimetable(activity, origin, destination,
                    date);
            if (idTimetable != null) {
                result.addAll(searchTrains(activity, idTimetable));
            }
            return result;
        }

        private Long searchTimetable(Activity activity, String origin,
                String destination, Date date) {

            String[] projection = { Timetable._ID };

            // Compose where params
            List<String> params = new ArrayList<String>();
            params.add("origin=" + wrap('\'', origin));
            params.add("destination=" + wrap('\'', destination));
            params.add("date=" + wrap('\'', toISO8601(date)));
            String where = StringUtils.join(params, " AND ");

            Cursor cursor = activity.managedQuery(TIMETABLE_URI, projection,
                    where, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    return cursor.getLong(0);
                }
            }
            return null;
        }

        private List<TrainItem> searchTrains(Activity activity, Long idTimetable) {

            String[] projection = { Train._ID, Train.CODE, Train.ARRIVE,
                    Train.DEPARTURE, Train.LENGTH };

            String where = Train.TIMETABLE_ID + "=" + idTimetable;
            Cursor cursor = activity.managedQuery(TRAIN_URI, projection, where,
                    null, null);
            return asList(cursor);
        }

        private List<TrainItem> asList(Cursor cursor) {
            List<TrainItem> result = new ArrayList<TrainItem>();

            if (cursor == null || cursor.getCount() == 0) {
                return result;
            }

            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                result.add(new TrainItem(cursor));
                cursor.moveToNext();
            }
            return result;
        }

        private String toISO8601(Date date) {
            return iso8601DateFormat.format(date);
        }

        private String wrap(Character chr, String str) {
            return chr + str + chr;
        }

        private void save(Activity activity,
                renfe.trains.model.Timetable timetable, List<TrainItem> trains) {
            save(activity, timetable);
            for (TrainItem each : trains) {
                save(activity, timetable, each);
            }
        }

        private void save(Activity activity,
                renfe.trains.model.Timetable timetable) {
            ContentValues values = new ContentValues();

            values.put(Timetable.ORIGIN, timetable.getOrigin());
            values.put(Timetable.DESTINATION, timetable.getDestination());
            values.put(Timetable.DATE, toISO8601(timetable.getDate()));

            Uri timetableUri = activity.getContentResolver().insert(
                    TIMETABLE_URI, values);
            timetable.setId(getId(timetableUri));
        }

        private Long getId(Uri uri) {
            List<String> segments = uri.getPathSegments();
            String id = segments.get(segments.size() - 1);
            Log.d(TAG, "id: " + id);
            return Long.parseLong(id);
        }

        private void save(Activity activity,
                renfe.trains.model.Timetable timetable, TrainItem trainItem) {
            ContentValues values = new ContentValues();

            values.put(Train.CODE, trainItem.getCode());
            values.put(Train.ARRIVE, trainItem.getArrive());
            values.put(Train.DEPARTURE, trainItem.getDeparture());
            values.put(Train.LENGTH, trainItem.getLength());
            values.put(Train.TIMETABLE_ID, timetable.getId());

            activity.getContentResolver().insert(TRAIN_URI, values);
        }

    }

}
