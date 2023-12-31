package hello.exception.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION =
            "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE =
            "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI =
            "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME =
            "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE =
            "javax.servlet.error.status_code";
    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("error page 500");
        printErrorInfo(request);
        return "error-page/500";
    }


    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex=",
                request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}",
                request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}",
                request.getAttribute(ERROR_MESSAGE));
        //      for the case of ex, Spring encapsulates the NestedServletException
        log.info("ERROR_REQUEST_URI: {}",
                request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}",
                request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}",
                request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }


//    javax.servlet.error.exception : exception
//    javax.servlet.error.exception_type : exception type
//    javax.servlet.error.message : errormsg
//    javax.servlet.error.request_uri : client request URI
//    javax.servlet.error.servlet_name : name of servlet with error
//    javax.servlet.error.status_code : HTTP status code


//    understanding servlet exceptions - filters , interceptors and DispatchType

//    full control flow
//    1. WAS(exception reaches here) <- filter <- servlet <-interceptor <-controller(source
//    2. WAS `/error-page/500` request(again) -> filter -> servlet -> interceptor -> controller(/error-page/500) -> View

//    when an error/exception occurs, WAS generates {2} from the control flow.
//    but given the instance of 1, where the code passed through both the filter and the interceptor, but caused an exception at the controller {1},
//    one can see the duplicate call of the interceptor and the filter objects at {2} is inefficient
//    this is the reason why servlet provides an additional information called DispatcherType
//    which is used to differentiate, for the filter and the interceptor, whether its call is genuine,
//    (from the client), or due to the error callback {2}.

//    DispatcherType is an enum implemented as follows
//    javax.servlet.DispatcherType
//    public enum DispatcherType {
//     FORWARD, -> forward call from a servlet to another servlet or JSP
//    --- RequestDispatcher.forward(request, response);
//     INCLUDE, -> when the servlet includes a result from another servlet or JSP
//    --- RequestDispatcher.include(request, response);
//     REQUEST, -> a normal client request
//     ASYNC, -> servlet asynchronous call
//     ERROR -> error request (error callback)
//    }

//    the produces value in the @RequestMapping ensures that when the client has an Accept header as JSON,
//    this controller method is called, for errors.
//    as a recap, JackSonMessageConverter changes the Map<> Object within response back to a Json Object
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }


//    API exception handling can also be done through what Spring boot provides by default.
//    Taking a look at the BasicErrorController code again,

//    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
//    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {}

//    @RequestMapping
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {}


//     errorHtml() : produces = MediaType.TEXT_HTML_VALUE
//     error() : other cases, returns as JSON in HTTPBODY through ResponseEntity

//     recap that spring boot's default error setting will lead to /error URI
//     applies to BasicErrorController as well,
//     modifiable by changing server.error.path on applications.settings

//     BasicErrorController provides the following settings
//    -> recap from before (springBootErrorPage.md)
//     server.error.include-binding-errors=always
//     server.error.include-exception=true
//     server.error.include-message=always
//     server.error.include-stacktrace=always

//     best practice to log the above, not revealing it to Users
}
