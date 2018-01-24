package es.ryanair.interconnections.model;

import java.io.Serializable;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 18/01/2018 Time: 14:35
 */
public class Route implements Serializable {

  private String airportFrom;
  private String airportTo;
  private String connectingAirport;
  private boolean newRoute;
  private boolean seasonalRoute;
  private String group;

  public String getAirportFrom() {
    return airportFrom;
  }

  public void setAirportFrom(String airportFrom) {
    this.airportFrom = airportFrom;
  }

  public String getAirportTo() {
    return airportTo;
  }

  public void setAirportTo(String airportTo) {
    this.airportTo = airportTo;
  }

  public String getConnectingAirport() {
    return connectingAirport;
  }

  public void setConnectingAirport(String connectingAirport) {
    this.connectingAirport = connectingAirport;
  }

  public boolean isNewRoute() {
    return newRoute;
  }

  public void setNewRoute(boolean newRoute) {
    this.newRoute = newRoute;
  }

  public boolean isSeasonalRoute() {
    return seasonalRoute;
  }

  public void setSeasonalRoute(boolean seasonalRoute) {
    this.seasonalRoute = seasonalRoute;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  @Override
  public String toString() {
    return "Route{" +
            "airportFrom='" + airportFrom + '\'' +
            ", airportTo='" + airportTo + '\'' +
            ", connectingAirport='" + connectingAirport + '\'' +
            ", newRoute=" + newRoute +
            ", seasonalRoute=" + seasonalRoute +
            ", group='" + group + '\'' +
            '}';
  }
}
