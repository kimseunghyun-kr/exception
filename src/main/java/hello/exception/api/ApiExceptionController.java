package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

//    under a normal valid call, the data is returned in the JSON format required.
//    but should an error occur, it returns the HTML error page we had made.
//    given that the client accepts JSON, the above result should be avoided,
//    as HTML, unless for web browsers, don't really work as meaningfully in other places, especially as a medium for data


    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if(id.equals(("ex"))){
            throw new RuntimeException("wrong user");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("wrong input value");
        }

        if (id.equals("user-ex")) {
            throw new UserException("user error");
        }

        return new MemberDto(id,"hello" + id);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new
                IllegalArgumentException());
    }

    //One of the three default ExceptionResolvers provided by Spring Boot.
//        with the following priorities in HandlerExceptionResolverComposite
//        1. ExceptionHandlerExceptionResolver
//        2. ResponseStatusExceptionResolver
//        3. DefaultHandlerExceptionResolver

//    this Controller method is a basic method to demonstrate DefaultHandlerExceptionResolver being called
//    through causing a TypeMismatchError during parameter binding,
//    by inputting a String to the @RequestParam Integer Data within the HTTP request.
//
//    DefaultHandlerExceptionResolver handles exceptions that occur within Spring's scope
//    A main use case for DefaultHandlerExceptionResolver can be for TypeMismatchException
//    which occurs during parameter binding.
//    Without intervention from HandlerExceptionResolvers, this exception is thrown to the servlet container,
//    resulting in error 500.
//    However, given the that parameter binding errors are typically caused by clients
//    giving a bad HTTP request, under the HTTP framework, it should be given an error 400;
//    DefaultHandlerExceptionResolver does precisely that, converting the error from 500 to 400;
//    there are many other such predetermined exception control flow rerouting/ handling methods
//    within DefaultHandlerExceptionResolver.

//    within DefaultHandlerExceptionResolver.handleTypeMisMatch,
//    the following code is present
//    response.sendError(HttpServletResponse.SC_BAD_REQUEST) (400)
//    so basically response.sendError() is still used, but just that it was already coded out by Spring
//    as sendError(400) is called, WAS will request for /error internally as callback

//    the following controller method
    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
    }



    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }


//    this is just a sample showcase to show how BasicErrorController can b extended to modify JSON messages
//    However, managing such API errors(RESPONSE ENTITY) is a chore through the BasicErrorController
//    unlike the HTML pages.
//    This is so as, for each API, according to each controller and exception, there may arise a need to
//    output a different result.
//    for instance, should there be a same exception in an API managing users, and an API managing items
//    despite the exception being the same, the output must be different.
//    as can be seen from the above case, this adds far more micromanaging components to API exceptions
//    (creating different exceptions, giving a different status according to the exceptions/errors.
//    hence, use BasicErrorController exception handling for HTML and not for API methods

//    When an exception passes the servlet container and reaches the WAS, it will be resolved as a 500(internal server error)
//    status code.
//    The following code will show how to result a 400 or a 404 according to the exceptions arising, instead of giving the 500 status code plainly
//    when /api/members/bad is called, it will result in an IllegalArgumentException
//    for /api/members/ex, it will result in a RuntimeException

//    at default, both cases will result in an error 500

//    Spring thus provides a HandlerExceptionResolver,
//    which handles exceptions that are thrown outside of handlers,
//    and allows the definition of new control flows according to the exceptions thrown.
//    Note: postHandle() will still not be called even if exception is handled by ExceptionResolver

}
