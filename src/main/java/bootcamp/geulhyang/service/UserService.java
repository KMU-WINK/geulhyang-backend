package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.KakaoUserInfoDto;
import bootcamp.geulhyang.dto.LoginStatusDto;
import bootcamp.geulhyang.dto.RegisterDto;
import bootcamp.geulhyang.entity.User;
import bootcamp.geulhyang.repository.UserRepository;
import bootcamp.geulhyang.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;



@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthService kakaoAuthService;



    public UserService(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider,
            KakaoAuthService kakaoAuthService)
    {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoAuthService = kakaoAuthService;
    }

    public LoginStatusDto processKakaoLogin(String authorizationCode) {
        String kakaoToken = kakaoAuthService.getKakaoAccessToken(authorizationCode);
        KakaoUserInfoDto kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(kakaoToken);

        // 사용자 정보 처리 및 JWT 토큰 생성
        User user = userRepository.findByKakaoId(kakaoUserInfo.getId())
                        .orElse(null);

        if (user == null) {
            return new LoginStatusDto("firstLogin", kakaoToken);
        }

        String token = jwtTokenProvider.createToken(user.getId(), user.getNickname());
        return new LoginStatusDto("existLogin", token);
    }

    public String register(RegisterDto registerDto) {
        KakaoUserInfoDto kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(registerDto.getCode());

        User newUser = new User();
        newUser.setKakaoId(kakaoUserInfo.getId());
        newUser.setNickname(registerDto.getNickname());
        newUser.setGenderFromString(registerDto.getGender());
        newUser.setAge(registerDto.getAge());

        userRepository.save(newUser);

        return jwtTokenProvider.createToken(newUser.getId(), newUser.getNickname());
    }
}
