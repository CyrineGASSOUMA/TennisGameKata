package com.sg.kata.tennisgame.dto;

import com.sg.kata.tennisgame.enums.GAMESTATE;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;

@FieldDefaults(level= AccessLevel.PRIVATE)
@ApiOperation("Game output DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameOutputDto extends GameDto {
    @ApiModelProperty(value="The couple of player Name-Surname and score of the two players",example = "")
    Map<String, Integer> scorePlayers ;

    @ApiModelProperty(value="The state of the game",example = "FINISHed")
    @Enumerated(EnumType.STRING)
    GAMESTATE stateGame;

    @ApiModelProperty(value="The name and the surname of the winAPoint of the game",example = "")
    String winnerOfTheGame;

    @Builder
    public GameOutputDto(PlayerDto playerDto1, PlayerDto playerDto2, Map<String, Integer> scorePlayers, GAMESTATE stateGame, String winnerOfTheGame){
        super(playerDto1,playerDto2);
        this.scorePlayers=scorePlayers;
        this.stateGame=stateGame;
        this.winnerOfTheGame= winnerOfTheGame;
    }
}
