package com.sg.kata.tennisgame.exceptions;

public class PlayerNotFoundException extends CustomisedException  {
    public PlayerNotFoundException(Class clazz, String codeException, String msgException) {
        super(clazz, codeException, msgException);
    }
}
