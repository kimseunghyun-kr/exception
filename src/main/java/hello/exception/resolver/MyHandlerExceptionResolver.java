package hello.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;


//    HandlerExceptionResolver has the following interface.
//    public interface HandlerExceptionResolver {
//        ModelAndView resolveException(
//                HttpServletRequest request, HttpServletResponse response,
//                Object handler, Exception ex);
//    }
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

//    ExceptionResolver 가 ModelAndView 를 반환하는 이유는 마치 try, catch를 하듯이, Exception 을
//처리해서 정상 흐름 처럼 변경하는 것이 목적이다. 이름 그대로 Exception 을 Resolver(해결)하는 것이
//목적이다.
//    The reason why ExceptionResolver returns ModelAndView is to seamlessly incorporate the control flow
//    of exceptions back to the usual control flow which returns a modelAndView (controller -> viewResolver)
//    simply put, it exists to resolve an exception back to the existing controller control flow

//여기서는 IllegalArgumentException 이 발생하면 response.sendError(400) 를 호출해서 HTTP
//상태 코드를 400으로 지정하고, 빈 ModelAndView 를 반환한다.
//    In this instance, the IllegalArgumentException calls the response.sendError(400),
//    setting the error code to 400, and returning an empty ModelAndView(null)

//반환 값에 따른 동작 방식
//HandlerExceptionResolver 의 반환 값에 따른 DispatcherServlet 의 동작 방식은 다음과 같다.
//빈 ModelAndView: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지
//않고, 정상 흐름으로 서블릿이 리턴된다.

//    control flow according to the return value
//    DispatcherServlet behaves as such according to HandlerExceptionResolver's return value
//    empty ModelAndView: for empty ModelAndView like new ModelAndView(), DispatcherServlet does not render
//    the view( no side effect ) and returns the servlet back to a normal (non-exception) servlet control flow
//ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링
//한다.
//    non-empty ModelAndView : renders the view;
//null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는
//ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.
//    null: if null is returned, it will call the ExceptionResolver. If there are no registered
//    registered ExceptionResolver to handle the exception, it will throw the exception out of the servlet container.
//  ExceptionResolver use cases
//예외 상태 코드 변환
//    returns exception status code
//예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를
//처리하도록 위임
//    transfers the exception call via response.sendError(xxx) to give control over to servlet,
//    prompting the servlet container to handle the exception according to the status code
//이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어서 스프링 부트가 기본으로 설정한 /
//error 가 호출됨
//    then, WAS will find the servlet error page and initiate callback sequence to find the respective view,
//    i.e) the default spring /error.
//뷰 템플릿 처리
//ModelAndView 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공
//    view template handling(resolution)
//    fills in values in the ModelAndView, to render a custom view render for the clients
//API 응답 처리
//response.getWriter().println("hello"); 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는
//것도 가능하다. 여기에 JSON 으로 응답하면 API 응답 처리를 할 수 있다.
//    API response handling
//    through methods like response.getWriter.println("hello"), enables devs to hard code what kind of
//    data to pass over. Should the return message be set to be passed ot the client as a JSON format,
//    able to handle responses as an API spec
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try{
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

            }
        } catch (IOException e) {
            log.error("resolver exception");
        }

        return null;
    }
}
