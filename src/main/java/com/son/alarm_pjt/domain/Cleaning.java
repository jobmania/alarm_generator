package com.son.alarm_pjt.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "cleaning")
@ToString
public class Cleaning {
    @Id
    private String id;
    private Member member;
    private Task task;
    private String date;

    public Cleaning(Member member, Task task, String date) {
        this.member = member;
        this.task = task;
        this.date = date;
    }
}
