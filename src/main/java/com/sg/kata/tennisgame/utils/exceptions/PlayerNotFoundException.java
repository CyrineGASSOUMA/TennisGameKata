package com.sg.kata.tennisgame.utils.exceptions;

public class PlayerNotFoundException extends CustomisedException  {
    public PlayerNotFoundException(Class clazz, String codeException, String msgException) {
        super(clazz, codeException, msgException);
    }
}
