package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.utils.exceptions.*;

import java.util.List;

public interface IGameService {
    public GameOutputDto playTennisGameService(PlayerDto player1, PlayerDto player2 )throws GameClosedException,SaveUpdateDBException,PlayerNotFoundException,SearchParamsException,NoWinnerOfPointException,PlayersNotExistException ;
    public List<GameModel> findGames();
}
