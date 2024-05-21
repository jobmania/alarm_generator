package com.son.alarm_pjt.domain;

import com.son.alarm_pjt.domain.Enum.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(name, member.name) && gender == member.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender);
    }
}
