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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = IGameRepository.class)
@EntityScan(basePackageClasses = GameModel.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class IPlayerRepositoryTest {

    @Autowired
    IPlayerRepository playerRepository;

    @MockBean
    IGameRepository gameRepository;

    PlayerModel playerModel;
    GameModel gameModel;
    List<PlayerModel> playerModelList;
    @Before
    public void init(){
        //ici
        gameModel = new GameModel(1L,"Game 1", GAMESTATE.FINISHED,false,null,null);
        //when(gameRepository.save(gameModel)).thenReturn(gameModel);
        playerModel = new PlayerModel("Philipe","UYTR",30,0,true,false);
        playerModel.setGame(gameModel);
        playerModel.setIdPlayer(1L);
        playerModelList = new ArrayList<>();
        playerModelList.add(playerModel);
        gameModel.setPlayerModelList(playerModelList);
        // when(gameRepository.save(gameModel)).thenReturn(gameModel);


    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFindPlayerByIdTest()throws DataException{
        PlayerModel playerModelResult = Optional.ofNullable(playerRepository.save(playerModel)).orElse(null);
    }

}
