package es.trains.tests;

import es.trains.model.TrainItem;
import es.trains.provider.Train;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class TrainTests {

    public void testInsertTrain(ContentResolver contentResolver,
            TrainItem trainItem) {
        Uri uri = es.trains.provider.Train.CONTENT_URI;

        ContentValues values = new ContentValues();

        values.put(Train.CODE, trainItem.getCode());
        values.put(Train.ARRIVE, trainItem.getArrive());
        values.put(Train.DEPARTURE, trainItem.getDeparture());
        values.put(Train.LENGTH, trainItem.getLength());
        values.put(Train.TIMETABLE_ID, 1);

        contentResolver.insert(uri, values);
    }

    public void testListTrains(Activity activity) {
        Uri uri = es.trains.provider.Train.CONTENT_URI;
        String[] projection = { Train._ID, Train.CODE, Train.ARRIVE,
                Train.DEPARTURE, Train.LENGTH };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        assert (cursor.getCount() > 0);
    }

    public void testRemoveTrain(ContentResolver contentResolver, Integer id) {
        Uri uri = Uri.parse(es.trains.provider.Train.CONTENT_URI.toString()
                + "/#");
        contentResolver.delete(uri, null, null);
    }

}
