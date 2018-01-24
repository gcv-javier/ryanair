package es.ryanair.interconnections.service;

import es.ryanair.interconnections.model.Route;
import es.ryanair.interconnections.model.RoutesPair;

import java.util.List;

/**
 * This class provides methods to work with {@link Route} and {@link RoutesPair}
 *
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 24/01/2018 Time: 14:23
 */
public interface RoutesService {

  /**
   * Checks if an input route (departure, arrival) is in the available routes
   *
   * @param departure
   * @param arrival
   * @return
   */
  boolean checkValidRoute(final String departure, final String arrival);

  /**
   * Returns the intermediate routes to get from departure to arrival.
   *
   * @param departure
   * @param arrival
   * @return
   */
  List<RoutesPair> getOneJumpRoutes(final String departure, final String arrival);
}
