package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.models.SetModel;
import com.sg.kata.tennisgame.repositories.IGameRepository;
import com.sg.kata.tennisgame.exceptions.SaveUpdateDBException;
import com.sg.kata.tennisgame.exceptions.SearchParamsException;
import com.sg.kata.tennisgame.services.GameTennis.GameService;
import com.sg.kata.tennisgame.services.GameTennis.IGameService;
import com.sg.kata.tennisgame.services.PlayerTennis.IPlayerService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(classes = GameService.class)
public class GameServiceTest {

    @Autowired
    IGameService gameService;

    @MockBean
    IGameRepository gameRepository;

    @MockBean
    IPlayerService playerService;

    PlayerDto playerDto1, playerDto2,playerDto3, playerDto4;
    PlayerModel playerModel1, playerModel2;
    List<PlayerModel> playerModelList;

    GameModel gameModel;

    String namePlayer1,namePlayer2,surnamePlayer1,surnamePlayer2;

    List <PlayerModel> firstPlayerList;
    List<PlayerModel>secondPlayerList;
    SetModel setModel;
    List<GameModel>gameModelList;




    @Before
    public void init() throws SaveUpdateDBException,SearchParamsException {
        namePlayer1="Roger";
        namePlayer2="Jimmy";
        surnamePlayer1="Federer";
        surnamePlayer2="Connors";
        setModel= new SetModel(1L,"Set",GAMESTATE.INPROGRESS,null,false);
        playerDto1 = new PlayerDto(namePlayer1,surnamePlayer1,false);
        playerDto2 = new PlayerDto(namePlayer2,surnamePlayer2,true);

        playerDto3 = new PlayerDto(namePlayer1,surnamePlayer1,true);
        playerDto4 = new PlayerDto(namePlayer2,surnamePlayer2,false);

        playerModel1 = new PlayerModel("Roger","Federer",15,0,true,false);
        playerModel2 = new PlayerModel("Jimmy","Connors",40,0,false,false);
        playerModelList = new ArrayList<>();
        playerModelList.add(playerModel1);
        playerModelList.add(playerModel2);

        firstPlayerList=new ArrayList<>();
        firstPlayerList.add(playerModel1);
        secondPlayerList= new ArrayList<>();
        secondPlayerList.add(playerModel2);

        gameModel= new GameModel(1L,"GameTennis 1", GAMESTATE.INPROGRESS,false,playerModelList,null);
        gameModelList=new ArrayList<>();
        gameModelList.add(gameModel);
        setModel.setGameModelList(gameModelList);
        when(playerService.saveOrUpdatePlayer(playerModel1)).thenReturn(playerModel1);
        when(playerService.saveOrUpdatePlayer(playerModel2)).thenReturn(playerModel2);
        when(playerService.getPlayerModelByNameAndSurname(namePlayer1,surnamePlayer1,1L)).thenReturn(firstPlayerList);
        when(playerService.getPlayerModelByNameAndSurname(namePlayer2,surnamePlayer2,1L)).thenReturn(secondPlayerList);
        when(playerService.findPlayerScoreByNameSurnameService(namePlayer1,surnamePlayer1,1L)).thenReturn(15);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameModel));
        when(gameRepository.save(gameModel)).thenReturn(gameModel);





    }
    @Test
    public void playTennisGameANdFinishingItServiceTest()throws Exception{
        GameOutputDto gameOutputDtoResult = gameService.playTennisGameService(playerDto1,playerDto2,setModel);
        assertNotNull(gameOutputDtoResult.getScorePlayers());
        assertEquals(gameOutputDtoResult.getScorePlayers().size(),2);
        assertEquals(gameOutputDtoResult.getStateGame(),GAMESTATE.FINISHED);
        assertEquals(gameOutputDtoResult.getWinnerOfTheGame(),playerDto2);
        assertEquals(gameOutputDtoResult.getPlayer1().getName(),"Roger");
        assertEquals(gameOutputDtoResult.getPlayer1().getSurname(),"Federer");
        assertEquals(gameOutputDtoResult.getPlayer1().getWinAPoint(),false);
        List<String> players= new ArrayList<>();
        gameOutputDtoResult.getScorePlayers().keySet().forEach((k)->players.add(k));
        assertNotNull(players);
        assertEquals(players.get(0),"Jimmy Connors");
        assertEquals(players.get(1),"Roger Federer");
        assertEquals(gameOutputDtoResult.getPlayer2().getName(),"Jimmy");
        assertEquals(gameOutputDtoResult.getPlayer2().getSurname(),"Connors");
        assertEquals(gameOutputDtoResult.getPlayer2().getWinAPoint(),true);
    }

    @Test
    public void playTennisGameServiceTest()throws Exception{
        GameOutputDto gameOutputDtoResult = gameService.playTennisGameService(playerDto3,playerDto4,setModel);
        assertNotNull(gameOutputDtoResult);
        assertEquals(gameOutputDtoResult.getStateGame(),GAMESTATE.INPROGRESS);
        assertEquals(gameOutputDtoResult.getWinnerOfTheGame(),playerDto3);


    }
}
