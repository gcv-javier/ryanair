package es.ryanair.interconnections.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 22/01/2018 Time: 9:55
 */
public class Day implements Serializable {

  private Long day;
  private List<Flight> flights;

  public Day(Long day, List<Flight> flights) {
    this.day = day;
    this.flights = flights;
  }

  public Day() {
  }

  public Long getDay() {
    return day;
  }

  public void setDay(Long day) {
    this.day = day;
  }

  public List<Flight> getFlights() {
    return flights;
  }

  public void setFlights(List<Flight> flights) {
    this.flights = flights;
  }

  @Override
  public String toString() {
    return "Day{" +
            "day=" + day +
            ", flights=" + flights +
            '}';
  }
}
