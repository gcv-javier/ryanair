package es.ryanair.interconnections.dto;

import java.util.List;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 18/01/2018 Time: 9:45
 */
public class FlightResponse {

  private Long stops;
  private List<Leg> legs;

  public FlightResponse(Long stops, List<Leg> legs) {
    this.stops = stops;
    this.legs = legs;
  }

  public Long getStops() {
    return stops;
  }

  public void setStops(Long stops) {
    this.stops = stops;
  }

  public List<Leg> getLegs() {
    return legs;
  }

  public void setLegs(List<Leg> legs) {
    this.legs = legs;
  }

  @Override
  public String toString() {
    return "FlightResponse{" +
            "stops=" + stops +
            ", legs=" + legs +
            '}';
  }
}
