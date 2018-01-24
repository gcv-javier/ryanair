package es.ryanair.interconnections.service;

import org.joda.time.LocalDateTime;

/**
 * This is the main service to get the interconnections (direct and one jump (stop))
 *
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 24/01/2018 Time: 14:14
 */
public interface FlightsService {

  /**
   * Returns all the connections from departure and arrival in the requested time. Connections can be direct, from
   * departure A to arrival B, or with one jump, drom departure A, using C, to arrival B.
   *
   * @param departure
   * @param arrival
   * @param departureDateTime
   * @param arrivalDateTime
   * @return
   */
  String getInterconnections(final String departure, final String arrival, final LocalDateTime departureDateTime,
          final LocalDateTime arrivalDateTime);
}
