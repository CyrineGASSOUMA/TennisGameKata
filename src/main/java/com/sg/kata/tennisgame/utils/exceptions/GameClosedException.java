package com.sg.kata.tennisgame.utils.exceptions;

public class GameClosedException extends CustomisedException {
    public GameClosedException(Class clazz, String codeException, String msgException) {
        super(clazz, codeException, msgException);
    }
}
