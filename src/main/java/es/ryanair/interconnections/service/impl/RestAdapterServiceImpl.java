package es.ryanair.interconnections.service.impl;

import es.ryanair.interconnections.model.Route;
import es.ryanair.interconnections.model.Schedule;
import es.ryanair.interconnections.service.RestAdapterService;
import es.ryanair.interconnections.service.exception.ApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 22/01/2018 Time: 10:06
 */
//@Service Inyected from context
public class RestAdapterServiceImpl implements RestAdapterService {

  private RestTemplate restTemplate;

  //https://api.ryanair.com/core/3/routes
  private String routesApiUrl;
  //https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{month}
  private String scheduleApiUrl;

  public RestAdapterServiceImpl(String routesApiUrl, String scheduleApiUrl) {
    this.routesApiUrl = routesApiUrl;
    this.scheduleApiUrl = scheduleApiUrl;

    this.restTemplate = new RestTemplate();

    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_OCTET_STREAM));
    restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
  }

  /**
   * Calls the API (rest service) to get available Routes
   *
   * @return
   */
  @Override
  public List<Route> getRoutes() {
    //we need to use exchange insted of getForObject because Jackson problems serializing response into List of Routes.
    //@SuppressWarnings("unchecked") List<Route> routes = restTemplate.getForObject(routesApiUrl, List.class);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ParameterizedTypeReference<List<Route>> typeRef = new ParameterizedTypeReference<List<Route>>() {
    };
    ResponseEntity<List<Route>> responseEntity = restTemplate.exchange(routesApiUrl, HttpMethod.GET, entity, typeRef);

    List<Route> routes = responseEntity.getBody();
    return routes;
  }

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
  @Override
  public Schedule getSchedule(final String departure, final String arrival, final int year, final int monthOfYear) throws ApiException {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("departure", departure);
    parameters.put("arrival", arrival);
    parameters.put("year", String.valueOf(year));
    parameters.put("month", String.valueOf(monthOfYear));

    @SuppressWarnings("unchecked") Schedule schedule;
    try {
      schedule = restTemplate.getForObject(scheduleApiUrl, Schedule.class, parameters);
    } catch (HttpClientErrorException e) {
      //API can return:  {"code":"Error","message":"Resource not found"}
      String responseBody = e.getResponseBodyAsString();
      String statusText = e.getStatusText();
      HttpStatus statusCode = e.getStatusCode();
      throw new ApiException("Api error: " + statusCode.toString() + ", " + responseBody + ", " + statusText);
    }

    return schedule;
  }
}
