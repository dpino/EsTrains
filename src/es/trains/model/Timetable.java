package es.trains.model;

import java.util.Date;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class Timetable {

    private Long id;

    private String origin;

    private String destination;

    private Date date;

    public Timetable() {

    }

    public Timetable(String origin, String destination, Date date) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDate() {
        return date;
    }

}
