package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.PlayerOutputDto;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.repositories.IPlayerRepository;
import com.sg.kata.tennisgame.utils.exceptions.SearchParamsException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.exception.DataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(classes = PlayerService.class)
public class PlayerServiceTest {
    @Autowired
    PlayerService playerService;

    @MockBean
    IPlayerRepository playerRepository;

    String name, surname;
    PlayerModel playerModel;
    List<PlayerModel> playerModelList;

    @Before
    public void init(){
        name="Nadal";
        surname = "Raphael";
        playerModel = new PlayerModel("Nadal","Raphael",0,true);
        playerModel.setIdPlayer(1L);
        playerModelList= new ArrayList<>();
        playerModelList.add(playerModel);
        when(playerRepository.findPlayerByNameAndSurname(name,surname)).thenReturn(playerModelList);
        when(playerRepository.save(playerModel)).thenReturn(playerModel);
        when(playerRepository.findAll()).thenReturn(playerModelList);
    }

    @Test
    public void getPlayerModelByNameAndSurnameTest()throws  DataException,SearchParamsException{
        PlayerModel playerModelResult = playerService.getPlayerModelByNameAndSurname(name,surname).get(0);
        assertNotNull(playerModelResult);
        assertEquals(playerModelResult.getName(),name);
        assertEquals(playerModelResult.getSurname(),surname);
        assertEquals(playerModelResult.getName(), "Nadal");
        assertEquals(playerModelResult.getSurname(),"Raphael");
        assertEquals(playerModelResult.getScore(),0);
        assertEquals(playerModelResult.getWinAPoint(),true);
    }

    @Test
    public void getPlayersWithScore(){
        List<PlayerOutputDto> playerOutputDtoListResult = playerService.getPlayersWithScore();
        assertNotNull(playerOutputDtoListResult);
        assertEquals(playerOutputDtoListResult.size(),1);
        assertEquals(playerOutputDtoListResult.get(0).getName(),"Nadal");
        assertEquals(playerOutputDtoListResult.get(0).getSurname(),"Raphael");
        assertEquals(playerOutputDtoListResult.get(0).getScore(),0);


    }

    @Test
    public void saveOrUpdatePlayerTest()throws Exception{
        PlayerModel playerModelResult = playerService.saveOrUpdatePlayer(playerModel);
        assertNotNull(playerModelResult);
        assertEquals(playerModelResult.getName(),name);
        assertEquals(playerModelResult.getSurname(),surname);
        assertEquals(playerModelResult.getName(), "Nadal");
        assertEquals(playerModelResult.getSurname(),"Raphael");
        assertEquals(playerModelResult.getScore(),0);
        assertEquals(playerModelResult.getWinAPoint(),true);
    }

    @Test
    public void findPlayerScoreByNameSurnameServiceTest()throws Exception{
        int score = playerService.findPlayerScoreByNameSurnameService(playerModel.getName(),playerModel.getSurname());
        assertEquals(score,0);
    }
}
