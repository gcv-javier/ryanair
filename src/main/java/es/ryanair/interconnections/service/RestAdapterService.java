package es.ryanair.interconnections.service;

import es.ryanair.interconnections.model.Route;
import es.ryanair.interconnections.model.Schedule;
import es.ryanair.interconnections.service.exception.ApiException;

import java.util.List;

/**
 * This interface takes care of calling external api
 *
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 24/01/2018 Time: 14:16
 */
public interface RestAdapterService {

  /**
   * Calls the API (rest service) to get available Routes
   *
   * @return
   */
  List<Route> getRoutes();

  /**
   * Calls the API (rest service) to get flights schedule
   *
   * @param departure
   * @param arrival
   * @param year
   * @param monthOfYear
   * @return
   * @throws ApiException
   */
  Schedule getSchedule(final String departure, final String arrival, final int year, final int monthOfYear) throws ApiException;
}
