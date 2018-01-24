package es.ryanair.interconnections.model;

import java.io.Serializable;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 22/01/2018 Time: 9:55
 */
public class Flight implements Serializable {

  private Long number;
  private String departureTime;
  private String arrivalTime;

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public String getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(String departureTime) {
    this.departureTime = departureTime;
  }

  public String getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(String arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  @Override
  public String toString() {
    return "Flight{" +
            "number=" + number +
            ", departureTime='" + departureTime + '\'' +
            ", arrivalTime='" + arrivalTime + '\'' +
            '}';
  }
}
