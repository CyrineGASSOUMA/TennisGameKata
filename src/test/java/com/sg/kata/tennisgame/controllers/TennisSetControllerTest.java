package com.sg.kata.tennisgame.controllers;

import com.sg.kata.tennisgame.dto.*;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.services.SetTennis.ISetService;
import com.sg.kata.tennisgame.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TennisSetController.class)
public class TennisSetControllerTest {

    @InjectMocks

    TennisSetController tennisSetController;

    @Mock
    ISetService setService;

    String name,namePlayer1,namePlayer2,surname,surnamePlayer1,surnamePlayer2,nameNotFound;
    PlayerDto playerDto1,playerDto2;
    GameDto gameDto;
    ResultDto<SetOutputDto> resultDto;
    SetOutputDto setOutputDto;
    GameOutputDto gameOutputDto;

    @Before
    public void init() throws SaveUpdateDBException, SearchParamsException, SetClosedException, NoWinnerOfPointException, PlayerNotFoundException, PlayersNotExistException, GameClosedException {
        name = "Roger";
        surname="Federer";
        nameNotFound="Philipe";
        namePlayer1="Roger";
        namePlayer2="Jimmy";
        surnamePlayer1="Federer";
        surnamePlayer2="Connors";
        playerDto1 = new PlayerDto(namePlayer1,surnamePlayer1,false);
        playerDto2 = new PlayerDto(namePlayer2,surnamePlayer2,true);
        gameDto = new GameDto(playerDto1,playerDto2);
        setOutputDto= new SetOutputDto(gameDto.getPlayer1(),gameDto.getPlayer2(), GAMESTATE.INPROGRESS,null,0,0,false);
        Map<String,Integer> mapResult = new HashMap<>();
        mapResult.put("",0);
        gameOutputDto= new GameOutputDto(playerDto1,playerDto2,mapResult, GAMESTATE.INPROGRESS,playerDto2,false,"");
        resultDto=new ResultDto<>();
        resultDto.setCode("Success");
        resultDto.setMessage("We are playing");
        resultDto.setData(setOutputDto);

    }
    @Test
    public void playSet() throws Exception {
        ResultDto<SetOutputDto> setOutputDtoResultDto= tennisSetController.playSet(gameOutputDto);
        assertNotNull(setOutputDtoResultDto);
        assertEquals(setOutputDtoResultDto.getCode(),"Success");
    }

}
