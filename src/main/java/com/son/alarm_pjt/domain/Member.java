package com.son.alarm_pjt.domain;

import com.son.alarm_pjt.domain.Enum.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "member")
@ToString
public class Member {
    @Id
    private String id;
    private String name;
    private Gender gender;

    public Member(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
    }
}
