Spring encapsulates the complex processes involved with servlet

by providing the following

-ErrorPages

-BasicErrorController autoLoaded

-ErrorMVCAutoConfiguration

->auto loads ErrorPage, with base URI set to /error
->if no specified error code, exception, loads the base error page

->errorPage is called if there is an error beyond the servlet container
-> or response.sendError() is called



-> this achieves the following where
the developer only needs to upload the error page,
and the BasicErrorController will, according to its rules
and priority logic order, will call upon the matching page

-> where the priority order of BasicErrorController is

1. view templates
   resources/templates/error/500.html
   resources/templates/error/5xx.html
2. static and public resources  ( for stuff you do not plan on changing afterwards)
   resources/static/error/400.html
   resources/static/error/404.html
   resources/static/error/4xx.html
3. no matching pages from the above
   resources/templates/error.html

more specific codes (500, 404) have higher priority than those with
unspecified html error codes (5xx, 4xx)

테스트
http://localhost:8080/error-404 404.html
http://localhost:8080/error-400 4xx.html 
http://localhost:8080/error-500 500.html
http://localhost:8080/error-ex 500.html (exceptions are considered an internal server error)


BasicErrorController sends the following information to the view through the Model object it passes

* timestamp: Fri Feb 05 00:00:00 KST 2021
* status: 400
* error: Bad Request
* exception: org.springframework.validation.BindException
* trace: 예외 trace
* message: Validation failed for object='data'. Error count: 1
* errors: Errors(BindingResult)
* path: 클라이언트 요청 경로 (`/hello`)

these information can be used by the view templates



exposing too much information about internal server errors bid unwell for the website's security
as well as user experience

thus, BasicErrorController provides the option whether to provide certain error information into the model

this can be done through modifying application.properties
server.error.include-exception=false : exception inclusion (true, false)

server.error.include-message=never : message inclusion option
server.error.include-stacktrace=never : trace inclusion
server.error.include-binding-errors=never : errors inclusion

for options that can accommodate the value never, it can have 3 other options

1. never
2. always
3. on_param -> used if the parameter is present


on_param will expose the information if there is a parameter.
Used often during debugging, within development servers.
Not recommended to use on the actual running server

if option set to on_param,
relevant information will be visible upon an HTTP request, as the values are included in the model
i.e) message=&errors=&trace=


other options:
server.error.whitelabel.enabled=true -> if there are no matching error page, give the default spring whitelabel error page



server.error.path=/error -> sets the error page URI path. affects both servlet global error page path and the error page path by BasicErrorController
error page path URI

TO create custom controllers to manage errors, one can either implement ErrorController interface or
extend(inherit) BasicErrorController.


