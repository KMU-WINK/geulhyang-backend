package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.KakaoUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class KakaoAuthService {

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        KakaoUserInfoDto kakaoUserInfoDto = new KakaoUserInfoDto();
        kakaoUserInfoDto.setId(body.getLong("id"));
        kakaoUserInfoDto.setNickname(body.getJSONObject("properties").getString("nickname"));

        return kakaoUserInfoDto;
    }
}
