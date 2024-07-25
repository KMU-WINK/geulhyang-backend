package bootcamp.geulhyang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId;
    private String nickname;
    private Long age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public void setGenderFromString(String gender) {
        this.gender = Gender.fromString(gender);
    }
}
