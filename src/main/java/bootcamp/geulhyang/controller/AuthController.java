package bootcamp.geulhyang.controller;

import bootcamp.geulhyang.dto.KaKaoTokenDto;
import bootcamp.geulhyang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody KaKaoTokenDto kakaoTokenDto) {
        String jwtToken = userService.processKakaoLogin(kakaoTokenDto.getAccessToken());
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }
}
