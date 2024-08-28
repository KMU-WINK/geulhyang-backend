package bootcamp.geulhyang.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home() {
        // 문자열을 ResponseEntity로 래핑
        String responseBody = "Hello, this is a simple string response from /home endpoint!";
        return new ResponseEntity<>(responseBody, HttpStatus.OK); // 200 OK 응답 반환
    }
}
