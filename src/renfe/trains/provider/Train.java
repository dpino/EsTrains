package renfe.trains.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class Train implements BaseColumns {

	public static final String AUTHORITY = "renfe.trains.provider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

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
