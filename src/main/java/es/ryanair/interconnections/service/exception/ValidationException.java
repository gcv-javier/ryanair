package es.ryanair.interconnections.service.exception;

/**
 * @author Javier Garcia-Cuerva Velasco (gcv.javier@gmail.com)
 * @version 1.0 Date: 23/01/2018 Time: 16:28
 */
public class ValidationException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ValidationException() {
    super();
  }

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationException(Throwable cause) {
    super(cause);
  }

  public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
