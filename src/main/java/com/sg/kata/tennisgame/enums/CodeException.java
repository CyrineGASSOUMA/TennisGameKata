package com.sg.kata.tennisgame.enums;

public enum CodeException {
    NOWINNEROFPOINT("There is no winner of point"),
    NOTFOUND("PlayerTennis Not Found"),
    CLOSED("The GameTennis is Closed"),
    CLOSEDSET("The Set is Closed"),
    PLAYERSNOTFOUND("The two players aren't found in the database"),
    PLAYERNOTFOUND("One player isn't found in the database"),
    SAVEUPDATEPROBLEM("A problem occured when saving or updating in database"),
    SEARCHPARAMS("Params for searching aren't good"),
    UNKNOWN("Unknown Error");
    private final String codeValue;

    CodeException(String value) {
        this.codeValue = value;
    }

    public String getCodeValue() {
        return this.codeValue;
    }
}
