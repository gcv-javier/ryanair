package es.ryanair.interconnections;

import es.ryanair.interconnections.service.FlightsService;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 18/01/2018 Time: 11:02
 */
//@Controller
@RestController
@RequestMapping("/")
public class FlightsController {

  private static final Logger log = Logger.getLogger(FlightsController.class);

  @Autowired
  private FlightsService flightsService;

  private static final String DATE_PATTERN = "YYYY-MM-dd'T'HH:mm";

  /**
   * The server input that serves user requests
   *
   * @param req
   * @param model
   * @return
   */
  @RequestMapping(value = "/interconnections", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public String getInterconnections(HttpServletRequest req, ModelMap model) {
    String departure = req.getParameter("departure");
    String arrival = req.getParameter("arrival");
    String departureDateTime = req.getParameter("departureDateTime");
    String arrivalDateTime = req.getParameter("arrivalDateTime");

    if (departure == null || departure.isEmpty() || arrival == null || arrival.isEmpty() || departureDateTime == null ||
            departureDateTime.isEmpty() || arrivalDateTime == null || arrivalDateTime.isEmpty()) {
      return "Missing input parameters: http://<HOST>/<CONTEXT>/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}";
    }
    LocalDateTime depatureLocalDateTime;
    LocalDateTime arrivalLocalDateTime;
    try {
      //i.e 2018-03-01T07:00
      depatureLocalDateTime = LocalDateTime.parse(departureDateTime, DateTimeFormat.forPattern(DATE_PATTERN));
      arrivalLocalDateTime = LocalDateTime.parse(arrivalDateTime, DateTimeFormat.forPattern(DATE_PATTERN));
    } catch (IllegalArgumentException e) {
      return "Date format error, please use the following format: " + DATE_PATTERN;
    }

    //Routes have been already loaded when application is deployed. Only routes with connectingAirport to null

    log.debug("1 - Getting Interconnections...");

    final String interconnections = flightsService.getInterconnections(departure, arrival, depatureLocalDateTime,
            arrivalLocalDateTime);

    model.addAttribute("interconnections", interconnections);

    return interconnections;

  }

}
