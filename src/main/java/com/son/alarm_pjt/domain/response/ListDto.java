package com.son.alarm_pjt.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListDto {
    private String memberName;
    private String taskName;
    private String date;
}
