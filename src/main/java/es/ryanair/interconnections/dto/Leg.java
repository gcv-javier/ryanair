package es.ryanair.interconnections.dto;

import org.joda.time.LocalDateTime;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 23/01/2018 Time: 14:27
 */
public class Leg {

  private String departureAirport;
  private String arrivalAirport;
  private String departureDateTime;
  private String arrivalDateTime;

  private static final String DATE_PATTERN = "YYYY-MM-dd'T'HH:mm";

  public Leg(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime,
          LocalDateTime arrivalDateTime) {
    this.departureAirport = departureAirport;
    this.arrivalAirport = arrivalAirport;
    this.departureDateTime = departureDateTime.toString(DATE_PATTERN);
    this.arrivalDateTime = arrivalDateTime.toString(DATE_PATTERN);
  }

  public String getDepartureAirport() {
    return departureAirport;
  }

  public void setDepartureAirport(String departureAirport) {
    this.departureAirport = departureAirport;
  }

  public String getArrivalAirport() {
    return arrivalAirport;
  }

  public void setArrivalAirport(String arrivalAirport) {
    this.arrivalAirport = arrivalAirport;
  }

  public String getDepartureDateTime() {
    return departureDateTime;
  }

  public void setDepartureDateTime(String departureDateTime) {
    this.departureDateTime = departureDateTime;
  }

  public String getArrivalDateTime() {
    return arrivalDateTime;
  }

  public void setArrivalDateTime(String arrivalDateTime) {
    this.arrivalDateTime = arrivalDateTime;
  }

  @Override
  public String toString() {
    return "Leg{" +
            "departureAirport='" + departureAirport + '\'' +
            ", arrivalAirport='" + arrivalAirport + '\'' +
            ", departureDateTime=" + departureDateTime +
            ", arrivalDateTime=" + arrivalDateTime +
            '}';
  }
}
