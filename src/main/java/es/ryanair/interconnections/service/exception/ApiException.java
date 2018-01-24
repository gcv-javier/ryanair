package es.ryanair.interconnections.service.exception;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 24/01/2018 Time: 10:50
 */
public class ApiException extends Exception {
  public ApiException() {
  }

  public ApiException(String message) {
    super(message);
  }

  public ApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApiException(Throwable cause) {
    super(cause);
  }

  public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
