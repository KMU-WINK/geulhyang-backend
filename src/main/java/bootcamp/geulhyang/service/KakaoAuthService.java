package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.KakaoUserInfoDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService {

    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Value("e6cba8bdc76a3d72e7e3de193056c084")
    private String clientId;

    @Value("wjnS0ZheNgfIrhsJ6BbykhQnByllG4bU")
    private String clientSecret;

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USER_INFO_URL, HttpMethod.GET, entity, String.class);

        // JSON parsing logic to extract user info
        JSONObject body = new JSONObject(response.getBody());
        KakaoUserInfoDto kakaoUserInfoDto = new KakaoUserInfoDto();
        kakaoUserInfoDto.setId(body.getString("id"));
        kakaoUserInfoDto.setEmail(body.getJSONObject("kakao_account").getString("email"));
        kakaoUserInfoDto.setNickname(body.getJSONObject("properties").getString("nickname"));

        return kakaoUserInfoDto;
    }
}
