package bootcamp.geulhyang.controller;

import bootcamp.geulhyang.dto.KaKaoTokenDto;
import bootcamp.geulhyang.dto.LoginStatusDto;
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

        LoginStatusDto loginStatus = userService.processKakaoLogin(kakaoTokenDto.getCode());

        // 로그에 로그인 상태와 토큰 정보를 출력
        log.info("Login status: {}", loginStatus.getStatus());
        log.info("Login token: {}", loginStatus.getToken());

        Map<String, String> response = new HashMap<>();
        response.put("token", loginStatus.getToken());
        response.put("message", loginStatus.getStatus());

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
