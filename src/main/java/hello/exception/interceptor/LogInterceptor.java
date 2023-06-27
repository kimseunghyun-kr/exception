package hello.exception.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);
        log.info("REQUEST [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]", logId, request.getDispatcherType(), requestURI);

        if(ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }

//    filter is provided by servlet, so filters have access to DispatcherType
//    which can set when the filter is called

//    however Interceptors are provided by Spring, thus they do not have access to DispatcherType
//    and are always called

//    however, interceptors are easy to add and remove according to their request paths, so by
//    putting the error page path to excludePathPatterns.
//    the interceptor can only be called during normal client calls
}
