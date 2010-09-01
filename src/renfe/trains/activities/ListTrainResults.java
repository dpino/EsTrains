package renfe.trains.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import renfe.trains.R;
import renfe.trains.model.TrainItem;
import renfe.trains.provider.Timetable;
import renfe.trains.services.RenfeXHR;
import renfe.trains.views.TrainAdapterView;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListTrainResults extends ListActivity {

    private static final String TAG = "ListTrainResults";

    private final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Bundle parameters = getIntent().getExtras();
        String origin = (String) parameters.get("origin");
        String destination = (String) parameters.get("destination");
        Date date = (Date) parameters.get("date");

        List<TrainItem> trains = getTrains();
        TrainAdapter adapter = new TrainAdapter(this, trains);
        setListAdapter(adapter);

        trains = searchTrains(origin, destination, date);
        adapter = new TrainAdapter(this, trains);
        setListAdapter(adapter);
    }

    private List<TrainItem> searchTrains(String origin, String destination,
            Date date) {
        List<TrainItem> result = new ArrayList<TrainItem>();

        RenfeXHR xhr = new RenfeXHR();
        Map<String, String> params = new HashMap<String, String>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("date", toISO8601(date));
        params.put("output", "json");
        xhr.execute(params);
        result.addAll(parseTrainList(xhr.getResponseAsString()));
        return result;
    }

    private List<? extends TrainItem> parseTrainList(String json) {
        List<TrainItem> result = new ArrayList<TrainItem>();
        try {
            JSONArray response = new JSONArray(json);
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = (JSONObject) response.get(i);
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

    private List<TrainItem> getTrains() {
        List<TrainItem> result = new ArrayList<TrainItem>();
        // result.add(new TrainItem("12604 MD", "06.52", "08.44", "1.52"));
        // result.add(new TrainItem("00280 ARCO", "09.25", "11.00", "1.35"));
        // result.add(new TrainItem("12608 R", "14.47", "16.47", "2.00"));
        return result;
    }

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

    private class TrainTests {

        private void testInsertTrain(TrainItem trainItem) {
            Uri uri = renfe.trains.provider.Timetable.CONTENT_URI;

            ContentValues values = new ContentValues();

            values.put(Timetable.CODE, trainItem.getCode());
            values.put(Timetable.ARRIVE, trainItem.getArrive());
            values.put(Timetable.DEPARTURE, trainItem.getDeparture());
            values.put(Timetable.LENGTH, trainItem.getLength());
            values.put(Timetable.TRAIN_ID, 1);

            getContentResolver().insert(uri, values);
        }

        private void testListTrains() {
            Uri uri = renfe.trains.provider.Timetable.CONTENT_URI;
            String[] projection = { Timetable._ID, Timetable.CODE,
                    Timetable.ARRIVE, Timetable.DEPARTURE, Timetable.LENGTH };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            assert (cursor.getCount() > 0);
        }

        private void testRemoveTrain(Integer id) {
            Uri uri = Uri.parse(renfe.trains.provider.Timetable.CONTENT_URI
                    .toString() + "/#");
            getContentResolver().delete(uri, null, null);
        }

    }

}