package bootcamp.geulhyang.controller;

import bootcamp.geulhyang.dto.KaKaoTokenDto;
import bootcamp.geulhyang.dto.RegisterDto;
import bootcamp.geulhyang.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody KaKaoTokenDto kakaoTokenDto) {
        log.info("Received authorization code: {}", kakaoTokenDto.getCode());

        String jwtToken = userService.processKakaoLogin(kakaoTokenDto.getCode());

        if (jwtToken.equals("firstLogin")) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "new_user");
            response.put("message", "none");
            return ResponseEntity.ok(response);
        }

        // jwtToken 로그 찍기
        log.info("Generated JWT Token: {}", jwtToken);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterDto registerDto) {
        String jwtToken = userService.register(registerDto);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }
}
