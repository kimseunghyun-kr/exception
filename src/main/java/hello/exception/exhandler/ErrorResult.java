package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;


//API error object for ExceptionHandler. TO be auto mapped to JSON by messageConverter
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
