package bootcamp.geulhyang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {
    private String code;
    private Long age;
    private String nickname;

    private String gender;
}
