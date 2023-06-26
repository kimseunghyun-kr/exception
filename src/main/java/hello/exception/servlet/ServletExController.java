package hello.exception.servlet;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
public class ServletExController {

//   exception control flow
//    WAS(bubbles up to this point) <- filter <- servlet <- interceptor <- controller(point of exception)
//    there actually are some spring interventions (Spring base error page) but it will be turned off
//    for this particular demonstration through applications.properties
//    server.error.whitelabel.enabled=false

//    WAS will return error 500 (exceptions are counted as an error that cannot be resolved within the server)
//    this is different from the usual whitelabel page seen


    @GetMapping("/error-ex")
    public void errorEx(){
        throw new RuntimeException("Exception occured");

    }


//    sendError() control flow
//    WAS(detects if sendError() has been called) <- filter <- servlet <- interceptor <- controller
//    after detection, shows the corresponding error page
//    then, the WAS detects the viewString that handles the error
//    which in this case, is new ErrorPage(RuntimeException.class,"/error-page/500")
//    should a RuntimeException reach WAS
//    then, it calls the view mapped to the viewString

//    full control flow
//    1. WAS(exception reaches here) <- filter <- servlet <-interceptor <-controller(source
//    2. WAS `/error-page/500` request(again) -> filter -> servlet -> interceptor -> controller(/error-page/500) -> View

//    IMPORTANT NOTE IS THAT THE WEB BROWSER(CLIENT) DOES NOT KNOW THAT THE SERVER HAS THESE PROCESSES  GOING ON.
//    ONLY THE SERVER TAKES THE ADDITIONAL STEPS TO FIND THE ERROR PAGE
//    ENCAPSULATION / BLACK BOX

//    The was does not merely request for the view, but it adds the error information in
//    request.attribute , hence, the included information can be used within the error page
//    through request.getAttribute()


    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }
    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }


}
