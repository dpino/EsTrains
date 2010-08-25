package renfe.trains.views;

import renfe.trains.model.Train;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class TrainAdapterView extends LinearLayout {

	public TrainAdapterView(Context context, Train train) {
		super(context);

		this.setOrientation(HORIZONTAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 0);

		TextView tvCode = new TextView(context);
		tvCode.setText(train.getCode());
		addView(tvCode, params);

		TextView tvDeparture = new TextView(context);
		tvDeparture.setText(train.getDeparture());
		addView(tvDeparture, params);

		TextView tvArrive = new TextView(context);
		tvArrive.setText(train.getArrive());
		addView(tvArrive, params);

		TextView tvLength = new TextView(context);
		tvLength.setText(train.getLength());
		addView(tvLength, params);
	}

}
