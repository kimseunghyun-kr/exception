package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//One of the three default ExceptionResolvers provided by Spring Boot.
//        with the following priorities in HandlerExceptionResolverComposite
//        1. ExceptionHandlerExceptionResolver
//        2. ResponseStatusExceptionResolver
//        3. DefaultHandlerExceptionResolver

//ResponseStatusExceptionResolver
//        -> sets the HTTP status code for the following
//        1. for Exceptions with @ResponseStatus tags
//        i.e) @ResponseStatus(value = HttpStatus.NOT_FOUND)
//        2. ResponseStatusException (s)

//the following shows usage of @ResponseStatus()
//although convenient, this is limited to exceptions that are either created / modifiable by the developer
//unable to apply to library code.
//it is also difficult to create dynamic control flow changes according to certain conditions
//due to usage of annotations (inherent issue of annotations -> unable to apply conditions mostly unless accomodated for)
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "BAD REQUEST")
//able to record exception message in message.properties
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
}
