package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import hello.exception.resolver.MyHandlerExceptionResolver;
import hello.exception.resolver.UserHandlerExceptionResolver;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Bean
    public FilterRegistrationBean<?> logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

//        this calls the filter upon client request and both the error.
//        default value is DispatcherType.REQUEST only.
//        unless you want to apply the filter on the error page, just leave it as default
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST,
                DispatcherType.ERROR);
        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/*.ico"
                        , "/error", "/error-page/**" //오류 페이지 경로
                );
    }

//        the "/error-page/** is keeping the interceptors being called during calls to error pages
//        (sole gatekeeper lol)
        
//        overall view final
        
//  /hello normal client call
//  WAS(/hello, dispatchType=REQUEST) -> filter -> servlet -> interceptor -> controller -> View
        
//  /error-ex error call

//  filter uses DispatchType to remove duplicate calls - (dispatchType=REQUEST )
//  interceptor uses excludepaths to remove duplicate calls during errors - excludePathPatterns("/error-page/**")

//  1. WAS(/error-ex, dispatchType=REQUEST) -> filter -> servlet -> interceptor -> controller
//  2. WAS(exception bubbles till here) <- filter <- servlet <- interceptor <- controller(exception occurs)
//  3. WAS error page check
//  4. WAS(/error-page/500, dispatchType=ERROR) -> filter(x) -> servlet -> interceptor(x) -> 
//  controller(/error-page/500) -> View

//    do not use configureHandlerExceptionResolvers as it will remove the HandlerExceptionResolver that is added by default.
//    use extendHandlerExceptionResolvers instead.
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver>
                                                    resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

}
