package es.ryanair.interconnections.service.impl;

import es.ryanair.interconnections.model.Route;
import es.ryanair.interconnections.model.RoutesPair;
import es.ryanair.interconnections.service.RestAdapterService;
import es.ryanair.interconnections.service.RoutesService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 18/01/2018 Time: 12:38
 */
@Service
public class RoutesServiceImpl implements RoutesService {

  private static final Logger log = Logger.getLogger(RoutesServiceImpl.class);

  private List<Route> routes;

  @Autowired
  private RestAdapterService restAdapterService;

  /**
   * Load routes when application is deployed. One singleton service. That way we only load routes once and not every
   * time user makes a request.
   */
  @PostConstruct
  public void loadRoutes() {

    log.debug("0 - Loading routes...");

    final List<Route> readRoutes = restAdapterService.getRoutes();

    log.debug(readRoutes.size() +
            " route(s) read, but only routes with empty connectingAirport should be used, so filtering them...");

    //only routes with empty connectingAirport should be used (value set to null)
    routes = readRoutes.stream().filter(route -> route.getConnectingAirport() == null).collect(Collectors.toList());

    log.debug("Finally there are: " + routes.size() + " route(s).");

    log.debug("- Routes loaded successfully: " + routes);

  }

  /**
   * Checks if an input route (departure, arrival) is in the available routes
   *
   * @param departure
   * @param arrival
   * @return
   */
  @Override
  public boolean checkValidRoute(final String departure, final String arrival) {
    final long count = routes.stream().filter(route -> (route.getAirportFrom().equals(departure) &&
            route.getAirportTo().equals(arrival))).count();
    return count > 0;
  }

  /**
   * Returns the intermediate routes to get from departure to arrival.
   *
   * @param departure
   * @param arrival
   * @return
   */
  @Override
  public List<RoutesPair> getOneJumpRoutes(final String departure, final String arrival) {
    //A-B: Get the routes from A to X
    final List<Route> departureRoutes = routes.stream().filter(route -> (route.getAirportFrom().equals(departure) &&
            !(route.getAirportTo().equals(arrival)))).collect(Collectors.toList());

    //A-B: Get the routes from Y to B
    final List<Route> arrivalRoutes = routes.stream().filter(route -> (!(route.getAirportFrom().equals(departure)) &&
            route.getAirportTo().equals(arrival))).collect(Collectors.toList());

    //Get only the routes where X=Y
    final List<RoutesPair> result = new ArrayList<>();
    for (Route departureRoute : departureRoutes) {
      for (Route arrivalRoute : arrivalRoutes) {
        if (departureRoute.getAirportTo().equals(arrivalRoute.getAirportFrom())) {
          final RoutesPair routesPair = new RoutesPair(departureRoute, arrivalRoute);
          result.add(routesPair);
        }
      }
    }
    return result;
  }

  public List<Route> getRoutes() {
    return routes;
  }

  public void setRoutes(List<Route> routes) {
    this.routes = routes;
  }

}
