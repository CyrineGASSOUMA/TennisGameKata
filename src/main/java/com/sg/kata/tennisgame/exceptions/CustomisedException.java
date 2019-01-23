package com.sg.kata.tennisgame.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomisedException extends Exception {
    private String code;
    private String message;

    /**
     * Get the name of the current class and method
     * @param clazz
     * @return String
     */
    public String getCurrentClassAndMethod(Class clazz) {
        String currentMethod  = new Object() {}.getClass().getEnclosingMethod().getName();
        return "[" + clazz.getSimpleName() + "] [" + currentMethod + "] : ";
    }

    /**
     * The constructor of the CustomisedException
     * @param clazz
     * @param code
     * @param msg
     */
    public CustomisedException (Class clazz, String code,  String msg) {
        this.code = code;
        this.message = getCurrentClassAndMethod(clazz)+ msg;
    }
}
