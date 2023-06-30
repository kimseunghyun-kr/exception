package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }


//    this is just a sample showcase to show how BasicErrorController can b extended to modify JSON messages
//    However, managing such API errors(RESPONSEENTITY) is a chore through the BasicErrorController
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
