package renfe.trains.model;

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

    public TrainItem(String code, String departure, String arrive, String length) {
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
