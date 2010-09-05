package es.trains.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class Timetable implements BaseColumns {

    public static final String AUTHORITY = "es.trains.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/timetables");

    public static final String DEFAULT_ORDER = "date DESC";

    /**
     * <p>
     * </p>
     */
    public static final String ORIGIN = "origin";

    /**
     * <p>
     * </p>
     */
    public static final String DESTINATION = "destination";

    /**
     * <p>
     * </p>
     */
    public static final String DATE = "date";

}
