package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.dto.SetOutputDto;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.repositories.ISetRepository;
import com.sg.kata.tennisgame.exceptions.*;
import com.sg.kata.tennisgame.services.GameTennis.IGameService;
import com.sg.kata.tennisgame.services.PlayerTennis.IPlayerService;
import com.sg.kata.tennisgame.services.SetTennis.ISetService;
import com.sg.kata.tennisgame.services.SetTennis.SetService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(classes = SetService.class)
public class SetServiceTest {

    @Autowired
    ISetService setService;

    @MockBean
    ISetRepository setRepository;

    @MockBean
    IPlayerService playerService;

    @MockBean
    IGameService gameService;

    GameOutputDto gameOutputDto;

    PlayerDto playerDto1, playerDto2;
    Map<String, Integer> scorePlayersList;
    GAMESTATE gamestate;
    String winnerOfTheGame,advantagePlayer,namePlayer1,namePlayer2,surnamePlayer1,surnamePlayer2 ;
    boolean deuceRule;
    PlayerModel playerModel1,playerModel2;
    GameModel gameModel1,gameModel2;
    List<GameModel>gameModelList;
    List<PlayerModel>playerModelList;

    @Before
    public void init() throws SearchParamsException, SaveUpdateDBException, GameClosedException, NoWinnerOfPointException, PlayersNotExistException, PlayerNotFoundException {
        namePlayer1=" name1";
        surnamePlayer1="surname1";
        namePlayer2="name 2";
        surnamePlayer2="surname 2";
        playerDto1 = new PlayerDto(namePlayer1,surnamePlayer1,false);
        playerDto2 = new PlayerDto(namePlayer2,surnamePlayer2,true);
        scorePlayersList= new HashMap<>();
        scorePlayersList.put(namePlayer1+" "+surnamePlayer1,40);
        scorePlayersList.put(namePlayer2+" "+surnamePlayer2,15);
        gamestate= GAMESTATE.INPROGRESS;
        winnerOfTheGame="";
        advantagePlayer="";
        deuceRule=false;
        playerModel1 = new PlayerModel(namePlayer1,surnamePlayer1,40,0,true,false);
        playerModel2 = new PlayerModel(namePlayer2,surnamePlayer2,15,0,false,false);
        List<PlayerModel> playerModelList1 = new ArrayList<>();
        playerModelList1.add(playerModel1);
        List<PlayerModel> playerModelList2 = new ArrayList<>();
        playerModelList2.add(playerModel2);
        when(playerService.getPlayerModelByNameAndSurname(namePlayer1,surnamePlayer1,1L)).thenReturn(playerModelList1);
        when(playerService.getPlayerModelByNameAndSurname(namePlayer2,surnamePlayer2,1L)).thenReturn(playerModelList2);
        when(playerService.getPlayerModelByNameAndSurname(namePlayer1,surnamePlayer1,2L)).thenReturn(playerModelList1);
        when(playerService.getPlayerModelByNameAndSurname(namePlayer2,surnamePlayer2,2L)).thenReturn(playerModelList2);
        gameModel1 = new GameModel(1L,"Game1",GAMESTATE.FINISHED,false,playerModelList,null);
        gameModel2 = new GameModel(2L,"Game1",gamestate,false,playerModelList,null);
        playerModelList = new ArrayList<>();
        playerModel1.setGame(gameModel1);
        playerModel2.setGame(gameModel2);
        playerModelList.add(playerModel1);
        playerModelList.add(playerModel2);
        gameModelList= new ArrayList<>();


        gameModelList.add(gameModel1);
        gameModelList.add(gameModel2);
        when(gameService.findGames()).thenReturn(gameModelList);
        gameOutputDto = new GameOutputDto(playerDto1,playerDto2,scorePlayersList,gamestate,null,deuceRule,advantagePlayer);

    }

    @Test
    public void playSetTennisTest() throws SaveUpdateDBException, SearchParamsException, NoWinnerOfPointException, PlayerNotFoundException, PlayersNotExistException, GameClosedException, SetClosedException {
        SetOutputDto setOutputDtoResult = setService.playSetTennis(gameOutputDto);
        assertEquals(setOutputDtoResult.getGamestate(),GAMESTATE.INPROGRESS);
        assertEquals(setOutputDtoResult.getPlayer1(),playerDto1);
        assertEquals(setOutputDtoResult.getPlayer2(),playerDto2);
        assertEquals(setOutputDtoResult.getLooserSetScore(),0);
        assertNotNull(setOutputDtoResult);
    }
}
