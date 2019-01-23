package com.sg.kata.tennisgame.services.SetTennis;

import com.sg.kata.tennisgame.dto.GameDto;
import com.sg.kata.tennisgame.dto.SetOutputDto;
import com.sg.kata.tennisgame.exceptions.*;

public interface ISetService {
    public SetOutputDto playSetTennis(GameDto gameDto) throws SearchParamsException, SaveUpdateDBException, GameClosedException, NoWinnerOfPointException, PlayersNotExistException, PlayerNotFoundException,SetClosedException ;
    }
