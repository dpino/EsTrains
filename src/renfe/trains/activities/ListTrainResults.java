package renfe.trains.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import renfe.trains.R;
import renfe.trains.model.TrainItem;
import renfe.trains.services.RenfeXHR;
import renfe.trains.views.TrainAdapterView;
import android.app.ListActivity;
import android.content.Context;
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

        // Set empty list
        TrainAdapter adapter = new TrainAdapter(this, Collections.EMPTY_LIST);
        setListAdapter(adapter);

        // Get parameters
        Bundle parameters = getIntent().getExtras();
        String origin = (String) parameters.get("origin");
        String destination = (String) parameters.get("destination");
        Date date = (Date) parameters.get("date");

        // Do query
        List<TrainItem> trains = searchTrains(origin, destination, date);
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

}
