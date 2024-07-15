package bootcamp.geulhyang.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class JwtConfig {

    @Value("eod31f98h91hf2kdla")
    private String secretKey;

    @Value("1800000")
    private long validityInMilliseconds;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }
}