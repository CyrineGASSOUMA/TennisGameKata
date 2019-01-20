package com.sg.kata.tennisgame.dto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@ApiOperation("Set output DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SetOutputDto {
    @NonNull
    @ApiParam(value="The winner of the set")
    PlayerDto winnerOfTheSet;

    @NonNull
    @ApiParam(value="The score of the winner of the set")
    int winnerSetScore;

    @NonNull
    @ApiParam(value="The score of the looser of the set")
    int looserSetScore;


}
