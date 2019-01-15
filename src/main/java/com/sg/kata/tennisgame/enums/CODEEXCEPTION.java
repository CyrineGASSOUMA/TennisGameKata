package com.sg.kata.tennisgame.enums;

import lombok.AllArgsConstructor;

public enum CODEEXCEPTION {
    SUCCESS("SUCCESS"),
    NOWINNEROFPOINT("There is no winner of point"),
    NOTFOUND("Player Not Found"),
    CLOSED("The Game is Closed"),
    PLAYERSNOTFOUND("The two players aren't found in the database"),
    PLAYERNOTFOUND("One player isn't found in the database"),
    SAVEUPDATEPROBLEM("A problem occured when saving or updating in database"),
    SEARCHPARAMS("Params for searching aren't good"),
    UNKNOWN("Unknown Error")
    ;
    private final String codeValue;

    CODEEXCEPTION(String value) {
        this.codeValue = value;
    }

    public String getCodeValue() {
        return this.codeValue;
    }
}
