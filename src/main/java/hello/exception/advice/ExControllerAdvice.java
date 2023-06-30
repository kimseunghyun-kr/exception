package hello.exception.advice;

import hello.exception.api.ApiExceptionV2Controller;
import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//@ControllerAdvice exists to separate the exception logic from normal control flow logic
//@RestControllerAdvice and @ControllerAdvice both exists

//@ControllerAdvice provides the @ExceptionHandler and @InitBinder annotations to the class
//@ControllerAdvice applies globally to all controllers should it not be designated to a class
//@RestControllerAdvice has an additional @ResponseBody to @ControllerAdvice
@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }


//    the following methods can be used to designate which controllers the controllerAdvice will be working on

//    Target all Controllers annotated with @RestController
//    @ControllerAdvice(annotations = RestController.class)
//    public class ExampleAdvice1 {}

//    // Target all Controllers within specific packages
//    @ControllerAdvice("org.example.controllers")
//    public class ExampleAdvice2 {}

//    // Target all Controllers assignable to specific classes
//    @ControllerAdvice(assignableTypes = {ControllerInterface.class,
//            AbstractController.class})
//    public class ExampleAdvice3 {}

//    further docs at
//    https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-advice.html
}
