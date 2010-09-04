package renfe.trains.provider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class RenfeTrainProvider extends ContentProvider {

    public static final String TAG = "RenfeTrainProvider";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "dd-MM-yyyy");

    private static final String DATABASE_NAME = "renfe-trains.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TBL_TRAIN = "Train";
    private static final String TBL_TIMETABLE = "Timetable";

    private static final int TRAINS = 1;
    private static final int TRAIN_ID = 2;
    private static final int TIMETABLE = 3;
    private static final int TIMETABLE_ID = 4;

    private static final UriMatcher URL_MATCHER;

    public static HashMap<String, String> TRAIN_PROJECTION_MAP;

    public static HashMap<String, String> TIMETABLE_PROJECTION_MAP;

    private DatabaseHelper mOpenHelper;

    private static SQLiteDatabase mDB;

    public static SQLiteDatabase getDB() {
        return mDB;
    }

    /**
     *
     * @author Diego Pino <dpino@igalia.com>
     *
     */
    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        private void createTrain(SQLiteDatabase db) {
            String sql = "CREATE TABLE train ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "code TEXT NOT NULL, " + "departure TEXT NOT NULL, "
                    + "arrive TEXT NOT NULL, " + "length TEXT NOT NULL, "
                    + "timetable_id INTEGER NOT NULL);";
            db.execSQL(sql);
        }

        private void createTimetable(SQLiteDatabase db) {
            String sql = "CREATE TABLE timetable ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "origin TEXT NOT NULL, " + "destination TEXT NOT NULL, "
                    + "date INTEGER NOT NULL);";
            db.execSQL(sql);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTimetable(db);
            createTrain(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        if (mOpenHelper != null) {
            mDB = mOpenHelper.getReadableDatabase();
        }
        return (mDB != null);
    }

    @Override
    public String getType(Uri uri) {
        switch (URL_MATCHER.match(uri)) {
        case TRAINS:
            return "vnd.android.cursor.dir/vnd.trains.trains";
        case TRAIN_ID:
            return "vnd.android.cursor.item/vnd.trains.train";
        case TIMETABLE:
            return "vnd.android.cursor.dir/vnd.timetables.timetables";
        case TIMETABLE_ID:
            return "vnd.android.cursor.item/vnd.timetables.timetables";
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Log.d(TAG, uri.toString());
        // Log.d(TAG, String.valueOf(URL_MATCHER.match(uri)));
        try {
            if (URL_MATCHER.match(uri) == TIMETABLE) {
                return insertTimetable(uri, values);
            }

            if (URL_MATCHER.match(uri) == TRAINS) {
                return insertTrain(uri, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unknown URL " + uri);
        }

        return null;
    }

    private Uri insertTrain(Uri uri, ContentValues values) throws Exception {
        try {
            long rowID = mDB.insert(TBL_TRAIN, null, values);

            if (rowID > 0) {
                uri = ContentUris.withAppendedId(
                        renfe.trains.provider.Train.CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(uri, null);
                return uri;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't insert new train");
        }

        throw new IllegalArgumentException("Unknown URL " + uri);
    }

    private Uri insertTimetable(Uri uri, ContentValues values) throws Exception {
        String today = sdf.format(new Date());
        if (!values.containsKey(renfe.trains.provider.Timetable.DATE)) {
            values.put(renfe.trains.provider.Timetable.DATE, today);
        }

        try {
            long rowID = mDB.insert(TBL_TIMETABLE, null, values);

            if (rowID > 0) {
                uri = ContentUris.withAppendedId(
                        renfe.trains.provider.Timetable.CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(uri, null);
                return uri;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't insert new timetable");
        }

        throw new IllegalArgumentException("Unknown URL " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sort) {

        // TODO Auto-generated method stub
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String _id;

        switch (URL_MATCHER.match(uri)) {
        case TRAINS:
            qb.setTables(TBL_TRAIN);
            qb.setProjectionMap(TRAIN_PROJECTION_MAP);
            if (sort == null) {
                sort = renfe.trains.provider.Train.DEFAULT_ORDER;
            }
            break;
        case TRAIN_ID:
            qb.setTables(TBL_TRAIN);
            _id = uri.getPathSegments().get(1);
            selection = (TextUtils.isEmpty(selection)) ? "_id=" + _id : "_id="
                    + _id + " AND " + selection;
            if (sort == null) {
                sort = renfe.trains.provider.Train.DEFAULT_ORDER;
            }
            break;
        case TIMETABLE:
            qb.setTables(TBL_TIMETABLE);
            qb.setProjectionMap(TIMETABLE_PROJECTION_MAP);
            if (sort == null) {
                sort = renfe.trains.provider.Timetable.DEFAULT_ORDER;
            }
            break;
        case TIMETABLE_ID:
            qb.setTables(TBL_TIMETABLE);
            _id = uri.getPathSegments().get(1);
            selection = (TextUtils.isEmpty(selection)) ? "_id=" + _id : "_id="
                    + _id + " AND " + selection;
            if (sort == null) {
                sort = renfe.trains.provider.Timetable.DEFAULT_ORDER;
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
        }

        try {
            Cursor cursor = qb.query(mDB, projection, selection, selectionArgs,
                    null, null, sort);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String _id;
        int count = 0;

        switch (URL_MATCHER.match(uri)) {
        case TRAINS:
            count = mDB.delete(TBL_TRAIN, selection, selectionArgs);
            break;
        case TRAIN_ID:
            _id = uri.getPathSegments().get(1);
            selection = (TextUtils.isEmpty(selection)) ? "_id=" + _id : "_id="
                    + _id + " AND " + selection;
            count = mDB.delete(TBL_TRAIN, selection, selectionArgs);
            break;
        case TIMETABLE:
            count = mDB.delete(TBL_TIMETABLE, selection, selectionArgs);
            break;
        case TIMETABLE_ID:
            _id = uri.getPathSegments().get(1);
            selection = (TextUtils.isEmpty(selection)) ? "_id=" + _id : "_id="
                    + _id + " AND " + selection;
            count = mDB.delete(TBL_TIMETABLE, selection, selectionArgs);
            break;
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        String _id;
        int count = 0;

        switch (URL_MATCHER.match(uri)) {
        case TRAINS:
            count = mDB.update(TBL_TRAIN, values, selection, selectionArgs);
            break;
        case TRAIN_ID:
            _id = uri.getPathSegments().get(1);
            selection = (TextUtils.isEmpty(selection)) ? "_id=" + _id : "_id="
                    + _id + " AND " + selection;
            count = mDB.update(TBL_TRAIN, values, selection, selectionArgs);
            break;
        case TIMETABLE:
            count = mDB.update(TBL_TIMETABLE, values, selection, selectionArgs);
            break;
        case TIMETABLE_ID:
            _id = uri.getPathSegments().get(1);
            selection = (TextUtils.isEmpty(selection)) ? "_id=" + _id : "_id="
                    + _id + " AND " + selection;
            count = mDB.update(TBL_TIMETABLE, values, selection, selectionArgs);
            break;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;

    }

    static {
        URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URL_MATCHER.addURI(Timetable.AUTHORITY, "trains", TRAINS);
        URL_MATCHER.addURI(Timetable.AUTHORITY, "trains/#", TRAIN_ID);
        URL_MATCHER.addURI(Train.AUTHORITY, "timetables", TIMETABLE);
        URL_MATCHER.addURI(Train.AUTHORITY, "timetables/#", TIMETABLE_ID);

        TRAIN_PROJECTION_MAP = new HashMap<String, String>();
        TRAIN_PROJECTION_MAP.put(Train._ID, "_id");
        TRAIN_PROJECTION_MAP.put(Train.ARRIVE, "arrive");
        TRAIN_PROJECTION_MAP.put(Train.DEPARTURE, "departure");
        TRAIN_PROJECTION_MAP.put(Train.LENGTH, "length");
        TRAIN_PROJECTION_MAP.put(Train.CODE, "code");
        TRAIN_PROJECTION_MAP.put(Train.TIMETABLE_ID, "timetable_id");

        TIMETABLE_PROJECTION_MAP = new HashMap<String, String>();
        TIMETABLE_PROJECTION_MAP.put(Timetable._ID, "_id");
        TIMETABLE_PROJECTION_MAP.put(Timetable.ORIGIN, "origin");
        TIMETABLE_PROJECTION_MAP.put(Timetable.DESTINATION, "destination");
        TIMETABLE_PROJECTION_MAP.put(Timetable.DATE, "date");
    }

}
