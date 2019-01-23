package com.sg.kata.tennisgame.dto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiOperation("PlayerTennis DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDto {

    @ApiParam(value = "The first player")
    PlayerDto player1;

    @ApiParam(value = "The second player")
    PlayerDto player2;
}
