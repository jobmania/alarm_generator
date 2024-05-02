package com.son.alarm_pjt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ResponseDto<T> {
    private String code;
    private Boolean success;
    private T data;
    private Boolean error;
    private Boolean sts;
    private String msg;


    public static <T> ResponseDto<T> success(T data, String msg){
        return new ResponseDto<>("200",true,data,false,true,msg);

    }

    public static <T> ResponseDto<T> fail(String code, String msg){
        return new ResponseDto<>(code,false,null,true,true,msg);

    }


}
