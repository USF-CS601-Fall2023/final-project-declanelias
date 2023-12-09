package server.Database;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Link implements Comparable<Link> {

    private String link;
    private LocalDateTime date;

    public Link(String link, LocalDateTime date) {
        this.link = link;
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return date.toLocalTime().format(formatter);
    }


    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return date.toLocalDate().format(formatter);
    }

    @Override
    public int compareTo(Link o) {
        return -date.compareTo(o.date);
    }

}
