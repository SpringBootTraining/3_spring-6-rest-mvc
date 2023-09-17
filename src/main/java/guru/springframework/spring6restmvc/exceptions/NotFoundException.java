package guru.springframework.spring6restmvc.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * By using @ResponseStatus on our custom exception classes, this allows us the following:
 *      1- Make the Exception class global for all our controllers.
 *      2- Omit the ExceptionHandler method in the controllers or in the ExceptionController class that has the @ControllerAdvice annotation
 *      3- Also we don't have to create a global ExceptionController class with @ControllerAdvice annotation.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value not found")
public class NotFoundException extends RuntimeException{

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
