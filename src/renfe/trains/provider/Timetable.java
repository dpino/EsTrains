package renfe.trains.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class Timetable implements BaseColumns {

    public static final String AUTHORITY = "renfe.trains.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String DEFAULT_ORDER = "departure DESC";

    /**
     * <p>
     * </p>
     */
    public static final String CODE = "code";

    /**
     * <p>
     * </p>
     */
    public static final String ARRIVE = "arrive";

    /**
     * <p>
     * </p>
     */
    public static final String DEPARTURE = "departure";

    /**
     * <p>
     * </p>
     */
    public static final String LENGTH = "length";

    /**
     * <p>
     * </p>
     */
    public static final String TRAIN_ID = "train_id";

}
