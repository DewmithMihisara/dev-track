package com.dev.dev_track.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto implements Serializable {
    private Integer code;
    private String message;
    private Map<String,Object> data;

    public ResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
