package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.KakaoUserInfoDto;
import bootcamp.geulhyang.entity.User;
import bootcamp.geulhyang.repository.UserRepository;
import bootcamp.geulhyang.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private KakaoAuthService kakaoAuthService;

    public String processKakaoLogin(String accessToken) {
        KakaoUserInfoDto kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(accessToken);

        User user = userRepository.findByKakaoId(kakaoUserInfo.getId())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKakaoId(kakaoUserInfo.getId());
                    newUser.setEmail(kakaoUserInfo.getEmail());
                    newUser.setNickname(kakaoUserInfo.getNickname());
                    return userRepository.save(newUser);
                });

        return jwtTokenProvider.createToken(user.getId(), user.getEmail());
    }

    
}
