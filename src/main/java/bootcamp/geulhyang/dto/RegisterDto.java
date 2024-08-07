package bootcamp.geulhyang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {
    private String token;
    private Long age;
    private String nickname;
    private String gender;
}
