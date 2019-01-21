package com.sg.kata.tennisgame.dto;

import com.sg.kata.tennisgame.enums.GAMESTATE;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiOperation("Set output DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SetOutputDto {

    @ApiParam(value = "The first player")
    PlayerDto player1;

    @ApiParam(value = "The second player")
    PlayerDto player2;

    @ApiParam(value = "Thestate of the set")
    GAMESTATE gamestate;

    @ApiParam(value = "The winner of the set")
    PlayerDto winnerOfTheSet;

    @ApiParam(value = "The score of the winner of the set")
    int winnerSetScore;

    @ApiParam(value = "The score of the looser of the set")
    int looserSetScore;

    @ApiParam(value = "The tie break rule is activated or not")
    boolean tieBreakActivation;


}
