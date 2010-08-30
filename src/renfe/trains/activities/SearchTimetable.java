package renfe.trains.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import renfe.trains.R;
import renfe.trains.services.RenfeXHR;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class SearchTimetable extends Activity {

    private static final String TAG = "SearchTimetable";

    private final SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM");

    private final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    private final Calendar c = Calendar.getInstance();

    private final Date today = new Date();

    private Date selectedDate = new Date();

    private EditText txtSelectedDate;

    private AutoCompleteTextView acDestination;

    private AutoCompleteTextView acOrigin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_timetable);

        txtSelectedDate = (EditText) findViewById(R.id.selectedDate);
        refreshSelectedDate();

        initializeOrigins();
        initializeDestinations();
    }

    private void initializeOrigins() {
        acOrigin = (AutoCompleteTextView) findViewById(R.id.acOrigin);
        feedAutocompleteTextView(acOrigin, getTrainStations());
    }

    private void initializeDestinations() {
        acDestination = (AutoCompleteTextView) findViewById(R.id.acDestination);
        feedAutocompleteTextView(acDestination, getTrainStations());
    }

    private String[] getTrainStations() {
        return getResources().getStringArray(R.array.train_stations);
    }

    private void feedAutocompleteTextView(AutoCompleteTextView autocomplete,
            String[] elements) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, elements);
        autocomplete.setAdapter(adapter);
    }

    private void refreshSelectedDate() {
        txtSelectedDate.setText(sdf.format(selectedDate));
    }

    public void today(View v) {
        selectedDate = today;
        refreshSelectedDate();
    }

    public void tomorrow(View v) {
        selectedDate = plusDays(today, 1);
        refreshSelectedDate();
    }

    public void dayAfterTomorrow(View v) {
        selectedDate = plusDays(today, 2);
        refreshSelectedDate();
    }

    public void nextDay(View v) {
        selectedDate = plusDays(selectedDate, 1);
        refreshSelectedDate();
    }

    private Date plusDays(Date date, int days) {
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public void prevDay(View v) {
        selectedDate = minusDays(selectedDate, 1);
        refreshSelectedDate();
    }

    private Date minusDays(Date date, int days) {
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return c.getTime();
    }

    public void nextMonth(View v) {
        selectedDate = plusMonth(selectedDate, 1);
        refreshSelectedDate();
    }

    private Date plusMonth(Date date, int months) {
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public void prevMonth(View v) {
        selectedDate = minusMonth(selectedDate, 1);
        refreshSelectedDate();
    }

    private Date minusMonth(Date date, int months) {
        c.setTime(date);
        c.add(Calendar.MONTH, -months);
        return c.getTime();
    }

    public void searchTrains(View v) {
        Editable origin = acOrigin.getText();
        Editable destination = acDestination.getText();

        if (origin.toString().length() == 0) {
            acOrigin.setError("Por favor, seleccione un origen");
            return;
        }

        if (destination.toString().length() == 0) {
            acDestination.setError("Por favor, seleccione un destino");
            return;
        }

        searchTrains(origin.toString(), destination.toString(), selectedDate);
    }

    private void searchTrains(String origin, String destination, Date date) {
        RenfeXHR xhr = new RenfeXHR();
        Map<String, String> params = new HashMap<String, String>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("date", toISO8601(date));
        xhr.execute(params);
    }

    private String toISO8601(Date date) {
        return iso8601DateFormat.format(date);
    }

}
