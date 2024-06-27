package com.son.alarm_pjt.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ListDto {
    private String memberName;
    private String taskName;
    private String date;
}
