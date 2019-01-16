package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.PlayerOutputDto;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.utils.exceptions.PlayersNotExistException;
import com.sg.kata.tennisgame.utils.exceptions.SaveUpdateDBException;
import com.sg.kata.tennisgame.utils.exceptions.SearchParamsException;

import java.util.List;

public interface IPlayerService {
    public List<PlayerModel> getPlayerModelByNameAndSurname(String name, String surname) throws SearchParamsException;

    public PlayerModel saveOrUpdatePlayer(PlayerModel playerModel)throws SaveUpdateDBException;

    public int findPlayerScoreByNameSurnameService(String name, String surname) throws SearchParamsException;
    public List<PlayerOutputDto> getPlayersWithScore() ;
}
