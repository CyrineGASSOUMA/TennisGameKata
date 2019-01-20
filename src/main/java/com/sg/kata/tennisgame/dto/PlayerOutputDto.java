package com.sg.kata.tennisgame.dto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@ApiOperation("Player Output DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerOutputDto {
    @NonNull
    @ApiParam(value="The name of the player",example = "Name1")
    String name;

    @NonNull
    @ApiParam(value="The surname of the player",example = "Surname1")
    String surname;

    @NonNull
    @ApiParam(value="The surname of the player",example = "15")
    int score;
}
