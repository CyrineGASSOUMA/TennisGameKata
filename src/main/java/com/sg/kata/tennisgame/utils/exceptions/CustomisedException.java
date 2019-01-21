package com.sg.kata.tennisgame.utils.exceptions;

import com.sg.kata.tennisgame.utils.MsgUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomisedException extends Exception {
    @Autowired
    MsgUtils msgUtils = new MsgUtils();

    private String code;
    private String message;

    public CustomisedException (Class clazz, String code,  String msg) {
        this.code = code;
        this.message = msgUtils.getCurrentClassAndMethod(clazz)+ msg;
    }
}
