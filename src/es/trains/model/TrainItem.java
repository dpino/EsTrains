package es.trains.model;

import android.database.Cursor;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class TrainItem {

    private String code;

    private String departure;

    private String arrive;

    private String length;

    public TrainItem() {

    }

    public TrainItem(Cursor cursor) {
        this(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4));
    }

    public TrainItem(String code, String arrive, String departure, String length) {
        this.code = code;
        this.departure = departure;
        this.arrive = arrive;
        this.length = length;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

}
