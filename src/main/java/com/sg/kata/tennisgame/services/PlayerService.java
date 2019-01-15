package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.repositories.IPlayerRepository;
import com.sg.kata.tennisgame.utils.exceptions.SaveUpdateDBException;
import com.sg.kata.tennisgame.utils.exceptions.SearchParamsException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level= AccessLevel.PRIVATE)
public class PlayerService implements IPlayerService {
    Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    IPlayerRepository playerRepository;

    /**
     * find player in the db by his name and his surname
     * @param name
     * @param surname
     * @return List<PlayerModel>
     * @throws SearchParamsException
     */
    @Transactional
    public List<PlayerModel> getPlayerModelByNameAndSurname(String name, String surname)throws  SearchParamsException{
        logger.info("Get the player by its name and surname from the database");
        return Optional.ofNullable(playerRepository.findPlayerByNameAndSurname(name,surname))
                                .orElseThrow(()->new SearchParamsException(this.getClass(), CODEEXCEPTION.SEARCHPARAMS.getCodeValue(), "Params for searching aren't good"));




    }

    /**
     * save or update a player
     * @param playerModel
     * @return PlayerModel
     * @throws SaveUpdateDBException
     */
    @Transactional
    public PlayerModel saveOrUpdatePlayer(PlayerModel playerModel) throws  SaveUpdateDBException{
        logger.info("save or update a player in the database");
        return Optional.ofNullable(playerRepository.save(playerModel))
                                .orElseThrow(()->new SaveUpdateDBException(this.getClass(), CODEEXCEPTION.SAVEUPDATEPROBLEM.getCodeValue(), "Database save/ update problem"));

    }

    /**
     * Search the actual score of the player in the database by its name and surname
     * @param name
     * @param surname
     * @return int
     * @throws SearchParamsException
     */
    @Transactional
    public int findPlayerScoreByNameSurnameService(String name, String surname) throws SearchParamsException{
        logger.info("get the actual score of a player by its name and surname from the database");
        return  Optional.ofNullable(playerRepository.findPlayerScoreByNameSurname(name,surname))
                .orElseThrow(()->new SearchParamsException(this.getClass(), CODEEXCEPTION.SEARCHPARAMS.getCodeValue(), "Params for searching aren't good"));

    }

}
