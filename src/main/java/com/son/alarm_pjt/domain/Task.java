package com.son.alarm_pjt.domain;

import com.son.alarm_pjt.domain.Enum.Gender;
import com.son.alarm_pjt.domain.Enum.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "task")
@ToString
public class Task {
    @Id
    private String id;

    private String name;
    private Gender gender;
    private Level level;
    private int assignNum;

    public Task(String name, Gender gender, Level level, int num) {
        this.name = name;
        this.gender = gender;
        this.level = level;
        this.assignNum = num;
    }
}
