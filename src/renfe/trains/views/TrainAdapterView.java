package renfe.trains.views;

import renfe.trains.R;
import renfe.trains.model.TrainItem;
import android.content.Context;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class TrainAdapterView extends LinearLayout {

    public TrainAdapterView(Context context, TrainItem train) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.train_row, this);

        TextView departure = (TextView) findViewById(R.id.tvDeparture);
        departure.setText(train.getDeparture(), TextView.BufferType.SPANNABLE);
        setBold(departure);

        TextView arrive = (TextView) findViewById(R.id.tvArrive);
        arrive.setText(train.getArrive(), TextView.BufferType.SPANNABLE);
        setBold(arrive);

        TextView length = (TextView) findViewById(R.id.tvLength);
        length.setText(train.getLength());

        TextView code = (TextView) findViewById(R.id.tvCode);
        code.setText(getTrainModel(train.getCode()));
    }

    private String getTrainModel(String code) {
        int pos = code.indexOf(" ");
        if (pos >= 0) {
            return code.substring(pos, code.length());
        }
        return code;
    }

    private void setBold(TextView tv) {
        Spannable str = (Spannable) tv.getText();
        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}