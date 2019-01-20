package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameDto;
import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.dto.SetOutputDto;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.repositories.ISetRepository;
import com.sg.kata.tennisgame.utils.exceptions.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetService implements ISetService {
    Logger logger = LoggerFactory.getLogger(SetService.class);

    @Autowired
    ISetRepository setRepository;

    @Autowired
    IPlayerService playerService;

    @Autowired
    IGameService gameService;

    /**
     * Play Games of the set
     * @param gameDto
     * @return SetOutputDto
     * @throws SearchParamsException
     * @throws SaveUpdateDBException
     * @throws GameClosedException
     * @throws NoWinnerOfPointException
     * @throws PlayersNotExistException
     * @throws PlayerNotFoundException
     */
    public SetOutputDto playSetTennis(GameDto gameDto) throws SearchParamsException, SaveUpdateDBException, GameClosedException, NoWinnerOfPointException, PlayersNotExistException, PlayerNotFoundException {
        logger.info("initialise local variables");
        PlayerModel winnerSet = null;
        PlayerModel looserSet = null;
        PlayerModel playerModel1=null;
        PlayerModel playerModel2=null;
        int winnerSetScore=0;
        int looserSetScore=0;
        SetOutputDto setOutputDtoResult=null;

        logger.info("Play The first Game of the Set");
        if (gameService.findGames().size() <= 1) {
            gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2());
        } else {
            logger.info("Get the information of the last game and its players");
            GameModel lastGame = gameService.findGames().get(gameService.findGames().size() - 1);
            playerModel1 = playerService.getPlayerModelByNameAndSurname(gameDto.getPlayer1().getName(), gameDto.getPlayer1().getSurname(), lastGame.getIdGame()).get(0);
            playerModel2 = playerService.getPlayerModelByNameAndSurname(gameDto.getPlayer2().getName(), gameDto.getPlayer2().getSurname(), lastGame.getIdGame()).get(0);
            if (!maxScore(playerModel1.getScoreSet(), playerModel2.getScoreSet())) {
                logger.info("we are still playing because we didn't find any player who has a score set : 6");
                gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2());
            } else if (maxScore(playerModel1.getScoreSet(), playerModel2.getScoreSet())) {
                logger.info("we get the actual looser and winner because one player has reached a score of 6");
                looserSet = getLooserOfSet(playerModel1, playerModel2);
                winnerSet = getMaxScoreOfSet(playerModel1, playerModel2);
                if (looserSet.getScoreSet() <= 4) {
                    logger.info("we can close the set because one player reached a score of 6 and the other the score of 4 or less");
                    winnerSetScore =winnerSet.getScoreSet();
                    looserSetScore =looserSet.getScoreSet();
                    setOutputDtoResult=new SetOutputDto(new PlayerDto(winnerSet.getName(), winnerSet.getSurname(), true), winnerSetScore,looserSetScore);
                } else if (looserSet.getScoreSet() == 5) {
                    logger.info("we are still playing because the looser reach the score of 5");
                    gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2());

                }

            }
            if (playerModel1.getScoreSet() == 6 && playerModel2.getScoreSet() == 6) {
                logger.info("we are still playing because the two players have a score of 6");
                gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2());
                return null;

            } else if (playerModel1.getScoreSet() == 7 || playerModel2.getScoreSet() == 7) {
                logger.info("we can close the set because one player reached a score of 7 ");
                winnerSet = getPlayerScore7(playerModel1, playerModel2);
                looserSet = getLooserOfSet(playerModel1, playerModel2);
                winnerSetScore=winnerSet.getScoreSet();
                looserSetScore = looserSet.getScoreSet();
                setOutputDtoResult= new SetOutputDto(new PlayerDto(winnerSet.getName(), winnerSet.getSurname(), true), winnerSetScore,looserSetScore);

            }
        }
        return setOutputDtoResult;

    }


    /**
     * Get the player who have a score of 7
     * @param playerModel1
     * @param playerModel2
     * @return PlayerModel
     */
    private PlayerModel getPlayerScore7(PlayerModel playerModel1, PlayerModel playerModel2) {
        return (playerModel1.getScoreSet() == 7) ? playerModel1 : playerModel2;
    }

    /**
     * Get the looser between two players
     * @param playerModel1
     * @param playerModel2
     * @return
     */
    private PlayerModel getLooserOfSet(PlayerModel playerModel1, PlayerModel playerModel2) {
        return (playerModel1.getScoreSet() < playerModel2.getScoreSet()) ? playerModel1 : playerModel2;
    }


    /**
     * Get the max score of the set between the two players
     * @param playerModel1
     * @param playerModel2
     * @return PlayerModel
     */
    private PlayerModel getMaxScoreOfSet(PlayerModel playerModel1, PlayerModel playerModel2) {
        return (playerModel1.getScoreSet() > playerModel2.getScoreSet()) ? playerModel1 : playerModel2;
    }

    /** Indicate if the max score is 6 or not
     * @param scorePlayer1
     * @param scorePlayer2
     * @return boolean
     */
    private boolean maxScore(int scorePlayer1, int scorePlayer2) {
        return ((scorePlayer1 == 6) || (scorePlayer2 == 6)) ? true : false;

    }
}
