package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.KakaoUserInfoDto;
import bootcamp.geulhyang.entity.User;
import bootcamp.geulhyang.repository.UserRepository;
import bootcamp.geulhyang.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthService kakaoAuthService;

    private final String clientId;
    private final String clientUrl;
    private final String clientSecret;

    public UserService(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider,
            KakaoAuthService kakaoAuthService,
            @Value("${kakao.client-id}") String clientId,
            @Value("${client.url}") String clientUrl,
            @Value("${kakao.client-secret}") String clientSecret)
    {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoAuthService = kakaoAuthService;
        this.clientId = clientId;
        this.clientUrl = clientUrl;
        this.clientSecret = clientSecret;
    }

    public String processKakaoLogin(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", clientUrl + "/auth/kakao/callback")
                .queryParam("code", authorizationCode)
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

                // 액세스 토큰을 사용하여 사용자 정보 요청
                KakaoUserInfoDto kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(accessToken);

                // 사용자 정보 처리 및 JWT 토큰 생성
                User user = userRepository.findByKakaoId(kakaoUserInfo.getId())
                        .orElseGet(() -> {
                            User newUser = new User();
                            newUser.setKakaoId(kakaoUserInfo.getId());
                            newUser.setNickname(kakaoUserInfo.getNickname());
                            return userRepository.save(newUser);
                        });

                return jwtTokenProvider.createToken(user.getId(), user.getNickname());
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
