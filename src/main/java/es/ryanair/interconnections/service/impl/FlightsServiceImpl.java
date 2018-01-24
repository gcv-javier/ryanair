package es.ryanair.interconnections.service.impl;

import com.google.gson.Gson;
import es.ryanair.interconnections.dto.FlightResponse;
import es.ryanair.interconnections.dto.Leg;
import es.ryanair.interconnections.model.Day;
import es.ryanair.interconnections.model.Flight;
import es.ryanair.interconnections.model.RoutesPair;
import es.ryanair.interconnections.model.Schedule;
import es.ryanair.interconnections.service.FlightsService;
import es.ryanair.interconnections.service.RestAdapterService;
import es.ryanair.interconnections.service.RoutesService;
import es.ryanair.interconnections.service.exception.ApiException;
import es.ryanair.interconnections.service.exception.ValidationException;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 18/01/2018 Time: 11:06
 */
@Service
public class FlightsServiceImpl implements FlightsService {

  private static final Logger log = Logger.getLogger(FlightsServiceImpl.class);

  @Autowired
  private RoutesService routesService;

  @Autowired
  private RestAdapterService restAdapterService;

  private static final String DATE_PATTERN = "YYYY-MM-dd'T'HH:mm";
  private static final String TIME_PATTERN = "HH:mm";

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
  @Override
  public String getInterconnections(final String departure, final String arrival, final LocalDateTime departureDateTime,
          final LocalDateTime arrivalDateTime) {

    final List<FlightResponse> flightResponses = new ArrayList<>();

    try {
      //1- Get direct connectios, without a jump
      log.debug("1.1 - Getting direct connections...");
      List<FlightResponse> directConnections = new ArrayList<>();
      try {
        //validate routes, because they come from user and they don't have to exist
        directConnections = getDirectConnections(departure, arrival, departureDateTime, arrivalDateTime, false);
        log.debug(directConnections.size() + " direct connections found");
      } catch (ApiException e) {
        log.warn("There is not direct connections between: " + departure + " and " + arrival);
      }

      //2- Get interconnections, one jump
      log.debug("1.2 - Getting one jump connections...");

      final List<FlightResponse> oneJumpConnections = getOneJumpConnections(departure, arrival, departureDateTime,
              arrivalDateTime);

      log.debug(oneJumpConnections.size() + " one jump connections found");

      //response include direct and one jum connections
      flightResponses.addAll(directConnections);
      flightResponses.addAll(oneJumpConnections);

      log.debug("- " + flightResponses.size() + " interconnections found.");

    } catch (ValidationException e) {
      return e.getMessage();
    }

    if (flightResponses.size() == 0) {
      return "No flights available.";
    }

    //Parse classes to Json
    Gson gson = new Gson();
    String json = gson.toJson(flightResponses, List.class);

    return json;
  }

  /**
   * Returns connections that are not direct and use only one jump between departure and arrival.
   *
   * @param departure
   * @param arrival
   * @param departureDateTime
   * @param arrivalDateTime
   * @return
   */
  private List<FlightResponse> getOneJumpConnections(final String departure, final String arrival, final LocalDateTime departureDateTime,
          final LocalDateTime arrivalDateTime) {

    final List<FlightResponse> matchedConnections = new ArrayList<>();
    // A-B: 2 route pairs
    // Pair: A-C, C-B
    // Pair: A-D, D-B
    final List<RoutesPair> oneJumpRoutes = routesService.getOneJumpRoutes(departure, arrival);

    if (oneJumpRoutes.size() > 0) {
      for (RoutesPair oneJumpRoute : oneJumpRoutes) {
        try {

          //A-C
          final List<FlightResponse> firstFlights;
          try {
            firstFlights = getDirectConnections(oneJumpRoute.getDepartureRoute().getAirportFrom(),
                    oneJumpRoute.getDepartureRoute().getAirportTo(), departureDateTime, arrivalDateTime, true);
          } catch (ApiException e) {
            //api error: we jump to next routes pair
            continue;
          }

          //C-B
          final List<FlightResponse> secondFlights;
          try {
            secondFlights = getDirectConnections(oneJumpRoute.getArrivalRoute().getAirportFrom(),
                    oneJumpRoute.getArrivalRoute().getAirportTo(), departureDateTime, arrivalDateTime, true);
          } catch (ApiException e) {
            //api error: we jump to next routes pair
            continue;
          }

          //We need to have direct connections from A to C and C to B
          if (firstFlights.size() > 0 && secondFlights.size() > 0) {
            final List<FlightResponse> tempMatchConnections = getMatchedConnections(firstFlights, secondFlights);
            matchedConnections.addAll(tempMatchConnections);
          }
        } catch (ValidationException e) {
          //not gonna happen because these are valid routes (we loaded when applications was loaded)
        }
      }
    }
    return matchedConnections;
  }

  /**
   * For a list of flights, this method checks the time restriction (the difference between the arrival and the next
   * departure should be 2h or greater)
   *
   * @param firstFlight
   * @param secondFlight
   * @return
   */
  private List<FlightResponse> getMatchedConnections(final List<FlightResponse> firstFlight,
          final List<FlightResponse> secondFlight) {

    final List<FlightResponse> flights = new ArrayList<>();
    //For interconnected flights the difference between the arrival and the next departure should be 2h or greater

    //Check from the different A-C flights, which ones match with C-B, with the time restriction (2h)

    for (FlightResponse firstFlightResponse : firstFlight) {
      for (FlightResponse secondFlightResponse : secondFlight) {
        //only one leg because they are direct
        if (LocalDateTime.parse(firstFlightResponse.getLegs().get(0).getArrivalDateTime(), DateTimeFormat.forPattern(
                DATE_PATTERN)).plusHours(2).isBefore(LocalDateTime.parse(secondFlightResponse.getLegs().get(0)
                .getDepartureDateTime(), DateTimeFormat.forPattern(DATE_PATTERN)))) {
          Leg leg1 = firstFlightResponse.getLegs().get(0);
          Leg leg2 = secondFlightResponse.getLegs().get(0);
          List<Leg> legs = new ArrayList<>();
          legs.add(leg1);
          legs.add(leg2);

          FlightResponse flightResponse = new FlightResponse(1L, legs);
          flights.add(flightResponse);

        }
      }
    }
    return flights;

  }

  /**
   * This method returns only direct connections, not connections with one jump.
   *
   * @param departure
   * @param arrival
   * @param departureDateTime
   * @param arrivalDateTime
   * @param notValidateRoute
   * @return
   * @throws ValidationException
   * @throws ApiException
   */
  private List<FlightResponse> getDirectConnections(final String departure, final String arrival,
          final LocalDateTime departureDateTime, final LocalDateTime arrivalDateTime, boolean notValidateRoute)
          throws ValidationException, ApiException {

    //Routes have been already loaded when application is deployed. Only routes with connectingAirport to null
    //Check that the input route exist
    if (notValidateRoute || routesService.checkValidRoute(departure, arrival)) {
      //notValidateRoute is to avoid calling checkValidRoute when it is not necessary. (when the direct connections
      // is got from the user we need to check it, but if we use one route from the api, we don't need to check it)

      //Get flights from external service, for the month of the year requested
      final Schedule schedule = restAdapterService.getSchedule(departure, arrival, departureDateTime.getYear(),
              departureDateTime.getMonthOfYear());

      //Filtering: not earlier than the specified departure datetime
      //and arriving to a given arrival airport not later than the specified arrival datetime

      //1- Departure Day
      final List<Day> days = schedule.getDays().stream().filter(
              day -> day.getDay().intValue() >= departureDateTime.getDayOfMonth()).collect(Collectors.toList());

      //2- Departure Time
      List<Day> auxDays = new ArrayList<>();
      for (Day day : days) {
        //we need to create a new date setting the same year, month, day than the departure time, but with the request time
        final List<Flight> flights = day.getFlights().stream().filter(flight -> LocalDateTime.parse(
                flight.getDepartureTime(), DateTimeFormat.forPattern(TIME_PATTERN)).withDayOfMonth(
                departureDateTime.getDayOfMonth()).withMonthOfYear(departureDateTime.getMonthOfYear()).withYear(
                departureDateTime.getYear()).isAfter(departureDateTime)).collect(Collectors.toList());
        if (flights.size() > 0) {
          Day newDay = new Day(day.getDay(), flights);
          auxDays.add(newDay);
        }
      }

      //we continue working with days instead of auxDays
      days.removeAll(days);
      days.addAll(auxDays);

      //3- Arrival Day
      auxDays = days.stream().filter(day -> day.getDay().intValue() <= arrivalDateTime.getDayOfMonth()).collect(
              Collectors.toList());

      //we continue working with days instead of auxDays
      days.removeAll(days);
      days.addAll(auxDays);

      //4- Arrival Time
      auxDays = new ArrayList<>();
      for (Day day : days) {
        //we need to create a new date setting the same year, month, day than the arribal time, but with the request time
        final List<Flight> flights = day.getFlights().stream().filter(flight -> LocalDateTime.parse(
                flight.getArrivalTime(), DateTimeFormat.forPattern(TIME_PATTERN)).withDayOfMonth(
                arrivalDateTime.getDayOfMonth()).withMonthOfYear(arrivalDateTime.getMonthOfYear()).withYear(
                arrivalDateTime.getYear()).isBefore(arrivalDateTime)).collect(Collectors.toList());
        if (flights.size() > 0) {
          Day newDay = new Day(day.getDay(), flights);
          auxDays.add(newDay);
        }
      }

      //we continue working with days instead of auxDays
      days.removeAll(days);
      days.addAll(auxDays);

      return generateDirectFlightResponse(departure, arrival, departureDateTime, schedule, days);

    } else {
      throw new ValidationException("Route not valid, please insert another route.");
    }

  }

  /**
   * Parses data to response, only for direct flights, 0 stops.
   *
   * @param departure
   * @param arrival
   * @param departureDateTime
   * @param schedule
   * @param days
   * @return
   */
  private List<FlightResponse> generateDirectFlightResponse(final String departure, final String arrival,
          final LocalDateTime departureDateTime, final Schedule schedule, final List<Day> days) {
    //parse response
    final List<FlightResponse> flightResponses = new ArrayList<>();
    for (Day day : days) {
      //in the examples there is only one flight for day, but they can be more than one
      for (Flight flight : day.getFlights()) {
        //depatureDateTime.getMonthOfYear() is schedule.getMonth()
        //departure's month and year are the same as arrival's month and year.
        Leg leg = new Leg(departure, arrival, LocalDateTime.parse(flight.getDepartureTime(), DateTimeFormat.forPattern(
                TIME_PATTERN)).withDayOfMonth(day.getDay().intValue()).withMonthOfYear(schedule.getMonth().intValue())
                .withYear(departureDateTime.getYear()), LocalDateTime.parse(flight.getArrivalTime(),
                DateTimeFormat.forPattern(TIME_PATTERN)).withDayOfMonth(day.getDay().intValue()).withMonthOfYear(
                schedule.getMonth().intValue()).withYear(departureDateTime.getYear()));

        FlightResponse flightResponse = new FlightResponse(0L, Arrays.asList(leg));

        flightResponses.add(flightResponse);
      }
    }
    return flightResponses;
  }
}
