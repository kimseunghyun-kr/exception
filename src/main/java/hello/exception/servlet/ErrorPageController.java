package hello.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
