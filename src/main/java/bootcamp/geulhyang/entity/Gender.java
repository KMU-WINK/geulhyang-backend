package bootcamp.geulhyang.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("male"),
    FEMALE("female");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender fromString(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender: " + value);
    }
}
