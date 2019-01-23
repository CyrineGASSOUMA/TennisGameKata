package com.sg.kata.tennisgame.services.GameTennis;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.SetModel;
import com.sg.kata.tennisgame.exceptions.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
public interface IGameService {
    public GameOutputDto playTennisGameService(PlayerDto player1, PlayerDto player2, SetModel setModel)throws GameClosedException,SaveUpdateDBException,PlayerNotFoundException,SearchParamsException,NoWinnerOfPointException,PlayersNotExistException ;
    public List<GameModel> findGames();

}
