package com.sg.kata.tennisgame.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@ApiOperation("Player DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultDto<T> {
    @ApiModelProperty(value="The code of the result of web service")
    String code;
    @ApiModelProperty(value="The message of the result of web service")
    String message;
    @ApiModelProperty(value="The data of the result of web service")
    T data;
}
