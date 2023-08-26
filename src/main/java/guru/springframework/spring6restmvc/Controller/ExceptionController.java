package guru.springframework.spring6restmvc.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * @ControllerAdvice annotation used to create a global exception class for all controllers in the spring boot application
 */
//@ControllerAdvice
public class ExceptionController {

    /**
     * The @ExceptionHandler annotation in Spring Boot is used to define methods that handle specific exceptions thrown during the execution of controller methods, allowing for customized error responses.
     * If one of the Controller methods throws a NotFoundException the below method will handle the exception
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }
}
