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

	public TrainItem(String code, String departure, String arrive, String length) {
		this.code = code;
		this.departure = departure;
		this.arrive = arrive;
		this.length = length;
	}

	public String getCode() {
		return code;
	}

	public String getDeparture() {
		return departure;
	}

	public String getArrive() {
		return arrive;
	}

	public String getLength() {
		return length;
	}

}
