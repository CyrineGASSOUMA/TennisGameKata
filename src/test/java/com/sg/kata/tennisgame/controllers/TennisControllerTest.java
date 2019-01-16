package com.sg.kata.tennisgame.controllers;

import com.sg.kata.tennisgame.dto.*;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.services.GameService;
import com.sg.kata.tennisgame.services.IGameService;
import com.sg.kata.tennisgame.services.IPlayerService;
import com.sg.kata.tennisgame.utils.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static springfox.documentation.builders.PathSelectors.any;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TennisGameController.class)
public class TennisControllerTest {
    @Mock
    IGameService gameService;

    @Mock
    IPlayerService playerService;

    @InjectMocks
    TennisGameController tennisGameController;

    GameDto gameDto;
    PlayerDto playerDto1, playerDto2;
    String namePlayer1,namePlayer2, surnamePlayer1,surnamePlayer2, name, surname,nameNotFound;
    ResultDto<GameOutputDto> resultDto;
    GameOutputDto gameOutputDto;
    PlayerOutputDto playerOutputDto1, playerOutputDto2;
    List<PlayerOutputDto> playerOutputDtoList;

    @Before
    public void setUp() throws Exception {
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
        Map<String,Integer> mapResult = new HashMap<>();
        gameOutputDto= new GameOutputDto(playerDto1,playerDto2,mapResult, GAMESTATE.INPROGRESS,"Roger Federer");
        resultDto = new ResultDto<>();
        resultDto.setCode("Succes");
        resultDto.setMessage("Playing");
        resultDto.setData(gameOutputDto);
        Mockito.when(gameService.playTennisGameService(gameDto.getPlayer1(),gameDto.getPlayer2())).thenReturn(gameOutputDto);

        playerOutputDto1 = new PlayerOutputDto(namePlayer1,surnamePlayer1,30);
        playerOutputDto2 = new PlayerOutputDto(namePlayer2,surnamePlayer2,40);
        playerOutputDtoList = new ArrayList<>();
        playerOutputDtoList.add(playerOutputDto1);
        playerOutputDtoList.add(playerOutputDto2);
        Mockito.when(playerService.getPlayersWithScore()).thenReturn(playerOutputDtoList);



    }

    @Test
    public void playTest() throws Exception {
        ResultDto<GameOutputDto> resultGameOutputDto= tennisGameController.play(gameDto);
        assertNotNull(resultGameOutputDto);
        assertEquals(resultGameOutputDto.getData(),gameOutputDto);
        assertEquals(resultGameOutputDto.getCode(),"Success");
        assertEquals(resultGameOutputDto.getMessage(),"Playing The Game");
        assertNotEquals(resultGameOutputDto.getCode(), CODEEXCEPTION.NOTFOUND);

    }

    @Test
    public void checkTheScoreOfAPlayerControllerTest(){
        ResultDto<PlayerOutputDto> playerOutputDtoResultDto = tennisGameController.checkTheScoreOfAPlayerController(namePlayer1,surnamePlayer1);

    }

    @Test
    public void checkTheScoreOfAllPlayersTest(){
        ResultDto<List<PlayerOutputDto>> playerOutputDtoResultDto = tennisGameController.checkTheScoreOfAllPlayers();
        assertNotNull(playerOutputDtoResultDto);
        assertEquals(playerOutputDtoResultDto.getCode(),"Success");
        assertEquals(playerOutputDtoResultDto.getMessage(),"The Score of the two players");
        assertEquals(playerOutputDtoResultDto.getData(),playerOutputDtoList);
    }

    @Test
    public void checkTheScoreOfAllPlayersNotFoundTest(){
        List<PlayerOutputDto> playerOutputDtoEmptyList = new ArrayList<>();
        Mockito.when(playerService.getPlayersWithScore()).thenReturn(playerOutputDtoEmptyList);
        ResultDto<List<PlayerOutputDto>> playerOutputDtoResultDto = tennisGameController.checkTheScoreOfAllPlayers();
        assertNotNull(playerOutputDtoResultDto);
        assertEquals(playerOutputDtoResultDto.getCode(),"Empty");
        assertEquals(playerOutputDtoResultDto.getMessage(),"There is no players in the database");
        assertEquals(playerOutputDtoResultDto.getData(),null);

    }
}
