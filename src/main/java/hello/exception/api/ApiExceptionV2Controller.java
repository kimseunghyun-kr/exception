package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    //One of the three default ExceptionResolvers provided by Spring Boot.
//        with the following priorities in HandlerExceptionResolverComposite
//        1. ExceptionHandlerExceptionResolver
//        2. ResponseStatusExceptionResolver
//        3. DefaultHandlerExceptionResolver

//    this is the ExceptionHandlerExceptionResolver
//    recall that aHandlerExceptionResolver needed to return a ModelAndView which is redundant for an API response
//    For an API response, for both 2. and 3. response.sendError() -> which basically
//    manually adds the response data to HttpServletResponse was needed.
//    This is very tedious.
//    Furthermore, both methods are difficult to provide a different control flow for the same exception
//    that occurs in different controllers.
//    ie) RuntimeException in UserManagement and ItemManagement Controllers need not give the same information
//    within the API response

//    @ExceptionHandler and ExceptionHandlerExceptionResolver is Spring's solution to the aforementioned issues,
//    and is the de facto method used to handle errors within practical usage of Spring MVC.

//    @ExceptionHandler({desiredException.class}) -> is how @ExceptionHandler is used.
//    should this be used, all of desiredException.class' subclass will be handled ass well
//    should there be a separate @ExceptionHandler for a subclass of the desiredException,
//    that exceptionHandler will be called first, as it is more specific.

//    multiple exception classes can be present, such as
//    @ExceptionHandler({desiredException1.class, desiredException2.class})

//    the control flow for the following method is
//     -> IllegalArgumentException thrown out of controller
//    ExceptionResolver is triggered.
//    ExceptionHandlerExceptionResolver with the highest priority is triggered
//    ExceptionHandlerExceptionResolver checks if @ExceptionHandler for IllegalArgumentException is present
//    if present -> runs illegalExHandle(), due to the Controller being a @RestController
//    @ResponseBody is triggered even for illegalExHandle(),
//    HttpMessageConverter is thus triggered, converting response into a JSON
//    Http response Code is set to 400(BAD_REQUEST) by @ReponseStatus

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExHandle(IllegalArgumentException e) {
//        log.error("[exceptionHandle] ex", e);
//        return new ErrorResult("BAD", e.getMessage());
//    }

//    the desiredException.class can be truncated.
//    In this case, the Exception in the parameter will be designated

//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
//        log.error("[exceptionHandle] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
//    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandle(Exception e) {
//        log.error("[exceptionHandle] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }

//    ModelAndView can be returned to return a HTML error page
//    @ExceptionHandler(ModelAndViewDefiningException.class)
//    public ModelAndView ex(ModelAndViewDefiningException e) {
//        log.info("exception e", e);
//        return new ModelAndView("error");
//    }
    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
