package com.sg.kata.tennisgame.controllers;

import com.sg.kata.tennisgame.dto.*;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.models.SetModel;
import com.sg.kata.tennisgame.services.IGameService;
import com.sg.kata.tennisgame.services.IPlayerService;
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
import static org.junit.Assert.assertNotNull;

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
    SetModel setModel;
    List<GameModel>gameModelList;
    GameModel gameModel;
    List<PlayerModel>playerModelList;
    PlayerModel playerModel1,playerModel2;



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
        gameOutputDto= new GameOutputDto(playerDto1,playerDto2,mapResult, GAMESTATE.INPROGRESS,playerDto2,false,"");
        resultDto = new ResultDto<>();
        resultDto.setCode("Succes");
        resultDto.setMessage("Playing");
        resultDto.setData(gameOutputDto);
        gameModelList= new ArrayList<>();
        playerModelList = new ArrayList<>();
        playerModel1 = new PlayerModel(namePlayer1,surnamePlayer1,40,0,true,false);
        playerModel2 = new PlayerModel(namePlayer2,surnamePlayer2,15,0,false,false);
        playerModelList.add(playerModel1);
        playerModelList.add(playerModel2);
        gameModel = new GameModel(1L,"Game1",GAMESTATE.INPROGRESS,false,playerModelList,null);
        gameModelList.add(gameModel);
        setModel= new SetModel(1L,"Set",GAMESTATE.INPROGRESS,gameModelList, false);
        playerOutputDto1 = new PlayerOutputDto(namePlayer1,surnamePlayer1,0);
        playerOutputDto2 = new PlayerOutputDto(namePlayer2,surnamePlayer2,40);
        playerOutputDtoList = new ArrayList<>();
        playerOutputDtoList.add(playerOutputDto1);
        playerOutputDtoList.add(playerOutputDto2);
        Mockito.when(playerService.getPlayersWithScore()).thenReturn(playerOutputDtoList);
    }

    @Test
    public void playTest() throws Exception {
        ResultDto<GameOutputDto> resultGameOutputDto= tennisGameController.playGame(gameDto);
        assertNotNull(resultGameOutputDto);
        assertEquals(resultGameOutputDto.getCode(),"Success");

    }

    @Test
    public void checkTheScoreOfAPlayerControllerTest(){
        ResultDto<PlayerOutputDto> playerOutputDtoResultDto = tennisGameController.checkTheScoreOfAPlayerController(namePlayer1,surnamePlayer1);

        assertEquals(playerOutputDtoResultDto.getCode(),"Success");
        assertEquals(playerOutputDtoResultDto.getMessage(),"The Score of the player");
        assertEquals(playerOutputDtoResultDto.getData(),playerOutputDto1);


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
