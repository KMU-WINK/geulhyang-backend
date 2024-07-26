package bootcamp.geulhyang.dto;

import lombok.Getter;

@Getter
public class LoginStatusDto {
    private String status;
    private String token;

    public LoginStatusDto(String status, String token) {
        this.status = status;
        this.token = token;
    }
}
