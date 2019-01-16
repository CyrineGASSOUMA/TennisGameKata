package com.sg.kata.tennisgame.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@ApiOperation("Player DTO")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class PlayerDto {

    @NonNull
    @ApiParam(value="The name of the player",example = "")
    String name;

    @NonNull
    @ApiParam(value="The surname of the player",example = "")
    String surname;

    @NonNull
    @ApiParam(value="The player win a point or not",example = "")
    Boolean winAPoint;

}
