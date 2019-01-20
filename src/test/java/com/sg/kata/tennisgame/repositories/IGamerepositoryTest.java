package com.sg.kata.tennisgame.repositories;

import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import org.hibernate.exception.DataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = IGameRepository.class)
@EntityScan(basePackageClasses = GameModel.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class IGamerepositoryTest {

    @Autowired
    IGameRepository gameRepository;

    @MockBean
    IPlayerRepository playerRepository;

    GameModel gameModel;
    PlayerModel playerModel;
    List<PlayerModel> playerModelList;

    @Before
    public void init(){
        //ici
        gameModel= new GameModel(1L,"Game 1", GAMESTATE.FINISHED,false,null,null);
        playerModel = new PlayerModel("Philipe","UYTR",30,0,true,false);
        playerModel.setGame(gameModel);
        playerModel.setIdPlayer(1L);
        when(playerRepository.save(playerModel)).thenReturn(playerModel);
    }

    @Test
    public void saveTest()throws DataException{
        GameModel gameModelResult = Optional.ofNullable(gameRepository.save(gameModel)).orElse(null);
        assertNotNull(gameModelResult);
        assertEquals(Optional.ofNullable(gameModelResult.getIdGame()),Optional.of(1L));
        assertEquals(gameModelResult.getNameGame(),"Game 1");
        assertEquals(gameModelResult.getStateGame(),GAMESTATE.FINISHED);
        assertNull(gameModelResult.getPlayerModelList());
        assertNotSame(gameModelResult,gameModel);

    }
}
