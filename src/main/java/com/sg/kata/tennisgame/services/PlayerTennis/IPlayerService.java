package com.sg.kata.tennisgame.services.PlayerTennis;

import com.sg.kata.tennisgame.dto.PlayerOutputDto;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.exceptions.SaveUpdateDBException;
import com.sg.kata.tennisgame.exceptions.SearchParamsException;

import java.util.List;

public interface IPlayerService {
    public List<PlayerModel> getPlayerModelByNameAndSurname(String name, String surname , Long idGame) throws SearchParamsException;

    public PlayerModel saveOrUpdatePlayer(PlayerModel playerModel)throws SaveUpdateDBException;

    public int findPlayerScoreByNameSurnameService(String name, String surname, Long idGAme) throws SearchParamsException;
    public List<PlayerOutputDto> getPlayersWithScore() ;
    public  Boolean playerHasAdvantage(PlayerModel playerModel, Long idGame);
}
