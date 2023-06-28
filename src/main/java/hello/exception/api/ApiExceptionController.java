package hello.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionController {

//    under a normal valid call, the data is returned in the JSON format required.
//    but should an error occur, it returns the HTML error page we had made.
//    given that the client accepts JSON, the above result should be avoided,
//    as HTML, unless for web browsers, don't really work as meaningfully in other places, especially as a medium for data


//    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if(id.equals(("ex"))){
            throw new RuntimeException("wrong user");
        }

        return new MemberDto(id,"hello" + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }

    @GetMapping("/api/members/{id}")
    public MemberDto getMemberV1(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        return new MemberDto(id, "hello " + id);
    }
}
