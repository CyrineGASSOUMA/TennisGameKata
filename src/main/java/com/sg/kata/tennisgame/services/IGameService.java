package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.utils.exceptions.*;

public interface IGameService {
    public GameOutputDto playTennisGameService(PlayerDto player1, PlayerDto player2 )throws GameClosedException,SaveUpdateDBException,PlayerNotFoundException,SearchParamsException,NoWinnerOfPointException,PlayersNotExistException ;
}
