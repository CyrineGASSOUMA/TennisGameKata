package com.sg.kata.tennisgame.controllers;

import com.sg.kata.tennisgame.dto.*;
import com.sg.kata.tennisgame.enums.CodeException;
import com.sg.kata.tennisgame.enums.GameState;
import com.sg.kata.tennisgame.models.SetModel;
import com.sg.kata.tennisgame.services.GameTennis.IGameService;
import com.sg.kata.tennisgame.services.PlayerTennis.IPlayerService;
import com.sg.kata.tennisgame.exceptions.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {"/kata/tennis/game/"})
@Api(value = "Tennis GameTennis", tags = {"Tennis GameTennis"}, description = "The controller that allows a tennis refree to manage a score of a game")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TennisGameController {
    Logger logger = LoggerFactory.getLogger(TennisGameController.class);

    @Autowired
    IGameService gameService;

    @Autowired
    IPlayerService playerService;

    /**
     * Playing a game of a tennis set
     *
     * @param gameDto
     * @return ResultDto<GameOutputDto>
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/play/{gameDto}")
    public ResultDto<GameOutputDto> playGame(@RequestBody @Valid @ApiParam("The information of the input object") GameDto gameDto) throws Exception {
        logger.info("Play a game");
        ResultDto<GameOutputDto> resultDto = new ResultDto<>();

        try {
            resultDto.setCode("Success");
            resultDto.setMessage("Playing The GAME within a set of a tennis match ");
            resultDto.setData(gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2(), new SetModel(1L, "Set 1", GameState.INPROGRESS, null, false)));

        } catch (GameClosedException gameClosedException) {
            resultDto.setCode(gameClosedException.getCode());
            resultDto.setMessage(gameClosedException.getMessage());
        } catch (NoWinnerOfPointException noWinnerOfThePointException) {
            resultDto.setCode(noWinnerOfThePointException.getCode());
            resultDto.setMessage(noWinnerOfThePointException.getMessage());

        } catch (PlayersNotExistException playersNotFoundException) {
            resultDto.setCode(playersNotFoundException.getCode());
            resultDto.setMessage(playersNotFoundException.getMessage());

        } catch (PlayerNotFoundException playerNotFoundException) {
            resultDto.setCode(playerNotFoundException.getCode());
            resultDto.setMessage(playerNotFoundException.getMessage());

        } catch (SearchParamsException searchParamsException) {
            resultDto.setCode(searchParamsException.getCode());
            resultDto.setMessage(searchParamsException.getMessage());

        } catch (SaveUpdateDBException saveOrUpdateException) {
            resultDto.setCode(saveOrUpdateException.getCode());
            resultDto.setMessage(saveOrUpdateException.getMessage());
            resultDto.setData(new GameOutputDto());

        } catch (Exception e) {
            resultDto.setCode("Error");
            resultDto.setMessage(CodeException.UNKNOWN.getCodeValue());
            resultDto.setData(new GameOutputDto());


        }

        return resultDto;

    }


    @CrossOrigin
    @GetMapping(path = "/player/score/{name}/{surname}")
    @ApiOperation(value = "The score of a player")
    public ResultDto<PlayerOutputDto> checkTheScoreOfAPlayerController(@ApiParam(value = "the name of the player")
                                                                       @PathVariable("name") String name,
                                                                       @ApiParam(value = "the surname of the player")
                                                                       @PathVariable("surname") String surname) {
        logger.info("Get the scor of a player by his name and surname");
        ResultDto<PlayerOutputDto> resultDto = new ResultDto<>();
        try {
            resultDto.setCode("Success");
            resultDto.setMessage("The Score of the player");
            resultDto.setData(new PlayerOutputDto(name, surname, playerService.findPlayerScoreByNameSurnameService(name, surname, 1L)));


        } catch (SearchParamsException searchParamsException) {
            resultDto.setCode(searchParamsException.getCode());
            resultDto.setMessage(searchParamsException.getMessage());
            resultDto.setData(null);
        } catch (Exception e) {
            resultDto.setCode("Error");
            resultDto.setMessage(CodeException.UNKNOWN.getCodeValue());
            resultDto.setData(null);
        }

        return resultDto;
    }

    @CrossOrigin
    @GetMapping(path = "/players/score")
    @ApiOperation(value = "The score of the two players")
    public ResultDto<List<PlayerOutputDto>> checkTheScoreOfAllPlayers() {
        logger.info("Get All the scores, name and surname of the two players");
        ResultDto<List<PlayerOutputDto>> resultDto = new ResultDto<>();
        try {
            if (playerService.getPlayersWithScore().size() == 0) {
                resultDto.setCode("Empty");
                resultDto.setMessage("There is no players in the database");
                resultDto.setData(null);
            } else {
                resultDto.setCode("Success");
                resultDto.setMessage("The Score of the two players");
                resultDto.setData(playerService.getPlayersWithScore());
            }

        } catch (Exception e) {
            resultDto.setCode("Error");
            resultDto.setMessage(CodeException.UNKNOWN.getCodeValue());
            resultDto.setData(null);
        }
        return resultDto;

    }


}
