package com.sg.kata.tennisgame.controllers;

import com.sg.kata.tennisgame.dto.GameDto;
import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.dto.ResultDto;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.services.GameService;
import com.sg.kata.tennisgame.services.IGameService;
import com.sg.kata.tennisgame.utils.exceptions.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value={"/kata/tennis/game/"})
@Api(value="Tennis Game",tags={"Tennis Game"},description="The controller that allows a tennis refree to manage a score of a game")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class TennisGameController {

    @Autowired
    IGameService gameService;

    @CrossOrigin
    @PostMapping("/play/{gameDto}")
    public ResultDto<GameOutputDto> play(@RequestBody @Valid @ApiParam("The information of the input object") GameDto gameDto ) throws Exception {
        ResultDto<GameOutputDto>resultDto= new ResultDto<>();

        try{
            resultDto.setCode("Success");
            resultDto.setMessage("Playing The Game");
            resultDto.setData(gameService.playTennisGameService(gameDto.getPlayer1(),gameDto.getPlayer2()));

        }
        catch(GameClosedException gameClosedException){
            resultDto.setCode(gameClosedException.getCode());
            resultDto.setMessage(gameClosedException.getMessage());
        }
        catch(NoWinnerOfPointException noWinnerOfThePointException){
            resultDto.setCode(noWinnerOfThePointException.getCode());
            resultDto.setMessage(noWinnerOfThePointException.getMessage());

        }
        catch(PlayersNotExistException playersNotFoundException){
            resultDto.setCode(playersNotFoundException.getCode());
            resultDto.setMessage(playersNotFoundException.getMessage());

        }
        catch(PlayerNotFoundException playerNotFoundException){
            resultDto.setCode(playerNotFoundException.getCode());
            resultDto.setMessage(playerNotFoundException.getMessage());

        }
        catch(SearchParamsException searchParamsException){
            resultDto.setCode(searchParamsException.getCode());
            resultDto.setMessage(searchParamsException.getMessage());

        }

        catch(SaveUpdateDBException saveOrUpdateException){
            resultDto.setCode(saveOrUpdateException.getCode());
            resultDto.setMessage(saveOrUpdateException.getMessage());
            resultDto.setData(new GameOutputDto());

        }
        catch(Exception e){
            resultDto.setCode("Error");
            resultDto.setMessage(CODEEXCEPTION.UNKNOWN.getCodeValue());
            resultDto.setData(new GameOutputDto());


        }

        return resultDto;

    }




}
