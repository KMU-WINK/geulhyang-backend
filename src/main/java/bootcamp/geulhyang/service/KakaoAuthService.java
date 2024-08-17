package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.request.KakaoUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class KakaoAuthService {
    private final String clientId;
    private final String clientUrl;
    private final String clientSecret;

    public KakaoAuthService(
            @Value("${kakao.client-id}") String clientId,
            @Value("${client.url}") String clientUrl,
            @Value("${kakao.client-secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientUrl = clientUrl;
        this.clientSecret = clientSecret;
    }


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

    public String getKakaoAccessToken(String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", clientUrl + "/auth/kakao/callback")
                .queryParam("code", authCode)
                .queryParam("client_secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject body = new JSONObject(response.getBody());
                String accessToken = body.getString("access_token");

                log.info("Received access token: {}", accessToken);

                return accessToken;
            } else {
                log.error("Failed to fetch access token from Kakao API. Status code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch access token from Kakao API");
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching access token from Kakao API", e);
            throw new RuntimeException("Exception occurred while fetching access token from Kakao API", e);
        }
    }
}
