package com.sg.kata.tennisgame.dto;

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
    String code;
    String message;
    T data;
}
