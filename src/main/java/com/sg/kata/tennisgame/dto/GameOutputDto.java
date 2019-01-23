package com.sg.kata.tennisgame.dto;

import com.sg.kata.tennisgame.enums.GameState;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiOperation("GameTennis output DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameOutputDto extends GameDto {
    @ApiModelProperty(value = "The couple of player Name-Surname and score of the two players")
    Map<String, Integer> scorePlayers;

    @ApiModelProperty(value = "The state of the game", example = "FINISHed")
    @Enumerated(EnumType.STRING)
    GameState stateGame;

    @ApiModelProperty(value = "The name and the surname of the winAPoint of the game")
    PlayerDto winnerOfTheGame;

    @ApiModelProperty(value = "The Deuce role is activated or not")
    boolean deuceRule;

    @ApiModelProperty(value = "The player who has the advantage if the deuce rule is activated")
    String advantagePlayer;

    @Builder
    public GameOutputDto(PlayerDto playerDto1, PlayerDto playerDto2, Map<String, Integer> scorePlayers, GameState stateGame, PlayerDto winnerOfTheGame, boolean deuceRule, String playerHasAdvantage) {
        super(playerDto1, playerDto2);
        this.scorePlayers = scorePlayers;
        this.stateGame = stateGame;
        this.winnerOfTheGame = winnerOfTheGame;
        this.deuceRule = deuceRule;
        this.advantagePlayer = playerHasAdvantage;
    }
}
