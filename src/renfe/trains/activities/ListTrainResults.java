package renfe.trains.activities;

import java.util.ArrayList;
import java.util.List;

import renfe.trains.R;
import renfe.trains.model.TrainItem;
import renfe.trains.provider.Timetable;
import renfe.trains.views.TrainAdapterView;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListTrainResults extends ListActivity {

    private static final String TAG = "ListTrainResults";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // RenfeXHR xhr = new RenfeXHR();
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("origin", "vigo");
        // params.put("destination", "ourense");
        // params.put("date", "tomorrow");
        // xhr.execute(params);

        List<TrainItem> trains = getTrains();
        testInsertTrain(trains.get(0));
        TrainAdapter adapter = new TrainAdapter(this, trains);
        setListAdapter(adapter);
    }

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

    private List<TrainItem> getTrains() {
        List<TrainItem> result = new ArrayList<TrainItem>();
        result.add(new TrainItem("12604 MD", "06.52", "08.44", "1.52"));
        result.add(new TrainItem("00280 ARCO", "09.25", "11.00", "1.35"));
        result.add(new TrainItem("12608 R", "14.47", "16.47", "2.00"));
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

}
