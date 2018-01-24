package es.ryanair.interconnections.model;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 23/01/2018 Time: 16:48
 */
public class RoutesPair {

  //A-B:
  //A-C, A-D
  //C-B, D-B
  private Route departureRoute;
  private Route arrivalRoute;

  public RoutesPair(Route departureRoute, Route arrivalRoute) {
    this.departureRoute = departureRoute;
    this.arrivalRoute = arrivalRoute;
  }

  public Route getDepartureRoute() {
    return departureRoute;
  }

  public void setDepartureRoute(Route departureRoute) {
    this.departureRoute = departureRoute;
  }

  public Route getArrivalRoute() {
    return arrivalRoute;
  }

  public void setArrivalRoute(Route arrivalRoute) {
    this.arrivalRoute = arrivalRoute;
  }
}
