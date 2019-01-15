package com.sg.kata.tennisgame.utils.exceptions;

public class PlayersNotExistException  extends CustomisedException   {
    public PlayersNotExistException(Class clazz, String codeException, String msgException) {
        super(clazz, codeException, msgException);
    }
}
