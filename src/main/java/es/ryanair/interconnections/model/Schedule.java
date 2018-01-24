package es.ryanair.interconnections.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 18/01/2018 Time: 14:35
 */
public class Schedule implements Serializable {

  private Long month;
  private List<Day> days;

  public Long getMonth() {
    return month;
  }

  public void setMonth(Long month) {
    this.month = month;
  }

  public List<Day> getDays() {
    return days;
  }

  public void setDays(List<Day> days) {
    this.days = days;
  }

  @Override
  public String toString() {
    return "Schedule{" +
            "month=" + month +
            ", days=" + days +
            '}';
  }
}
