package com.sg.kata.tennisgame.controllers;

import com.sg.kata.tennisgame.dto.*;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.services.ISetService;
import com.sg.kata.tennisgame.utils.exceptions.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value={"/kata/tennis/set/"})
@Api(value="Tennis Set",tags={"Tennis Set"},description="The controller that allows a tennis refree to manage a score of a set")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class TennisSetController {
    Logger logger = LoggerFactory.getLogger(TennisSetController.class);

    @Autowired
    ISetService setService;

    /**
     * Playing a set of a tennis game
     * @param gameDto
     * @return ResultDto<SetOutputDto>
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/play/{gameDto}")
    public ResultDto<SetOutputDto> playSet(@RequestBody @Valid @ApiParam("The information of the input object") GameDto gameDto ) throws SetClosedException {
        logger.info("Play a set of a tennis game");
        ResultDto<SetOutputDto>resultDto= new ResultDto<>();

        try{
            resultDto.setCode("Success");
            resultDto.setMessage("Playing The Tennis SET within a tennis match");
            resultDto.setData(setService.playSetTennis(gameDto));

        }
        catch(SetClosedException setClosedexception){
            resultDto.setCode(setClosedexception.getCode());
            resultDto.setMessage(setClosedexception.getMessage());
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
            resultDto.setData(null);

        }
        catch(Exception e){
            resultDto.setCode("Error");
            resultDto.setMessage(CODEEXCEPTION.UNKNOWN.getCodeValue());
            resultDto.setData(null);


        }

        return resultDto;

    }

}
