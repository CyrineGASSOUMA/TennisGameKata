package com.sg.kata.tennisgame.services.SetTennis;

import com.sg.kata.tennisgame.dto.GameDto;
import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.dto.SetOutputDto;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.models.SetModel;
import com.sg.kata.tennisgame.repositories.ISetRepository;
import com.sg.kata.tennisgame.exceptions.*;
import com.sg.kata.tennisgame.services.GameTennis.IGameService;
import com.sg.kata.tennisgame.services.PlayerTennis.IPlayerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
     *
     * @param gameDto
     * @return SetOutputDto
     * @throws SearchParamsException
     * @throws SaveUpdateDBException
     * @throws GameClosedException
     * @throws NoWinnerOfPointException
     * @throws PlayersNotExistException
     * @throws PlayerNotFoundException
     */
    public SetOutputDto playSetTennis(GameDto gameDto) throws SearchParamsException, SaveUpdateDBException, GameClosedException, NoWinnerOfPointException, PlayersNotExistException, PlayerNotFoundException, SetClosedException {
        logger.info("initialise local variables");
        PlayerModel winnerSet = null;
        PlayerModel looserSet = null;
        PlayerModel playerModel1 = null;
        PlayerModel playerModel2 = null;
        SetModel setModelWithTieBreak = null;
        PlayerModel winnerTieBreak = null;
        PlayerModel winnerLastTieBreak = null;
        PlayerModel looserTieBreak = null;
        int winnerSetScore = 0;
        int looserSetScore = 0;
        SetOutputDto setOutputDtoResult = null;
        logger.info("create a current set");
        SetModel currentSetModel = findOrCreateSet(1L);
        if (currentSetModel.getStateGame().equals(GAMESTATE.FINISHED))
            throw new SetClosedException(this.getClass(), CODEEXCEPTION.CLOSEDSET.getCodeValue(), "The Set is Closed");

        logger.info("Play The first GameTennis of the Set");
        if (gameService.findGames().size() <= 1) {
            gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel);
            setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto("", "", true), 0, 0, false);

        } else {
            logger.info("Get the information of the last game and its players");
            GameModel lastGame = gameService.findGames().get(gameService.findGames().size() - 1);
            playerModel1 = playerService.getPlayerModelByNameAndSurname(gameDto.getPlayer1().getName(), gameDto.getPlayer1().getSurname(), lastGame.getIdGame()).get(0);
            playerModel2 = playerService.getPlayerModelByNameAndSurname(gameDto.getPlayer2().getName(), gameDto.getPlayer2().getSurname(), lastGame.getIdGame()).get(0);
            if (!maxScore(playerModel1.getScoreSet(), playerModel2.getScoreSet())) {
                logger.info("we are still playing because we didn't find any player who has a score set : 6");
                gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel);
                setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto("", "", true), 0, 0, false);
            } else if (maxScore(playerModel1.getScoreSet(), playerModel2.getScoreSet())) {
                logger.info("we get the actual looser and winner because one player has reached a score of 6");
                looserSet = getLooserOfSet(playerModel1, playerModel2);
                winnerSet = getMaxScoreOfSet(playerModel1, playerModel2);
                if (looserSet.getScoreSet() <= 4) {
                    logger.info("we can close the set because one player reached a score of 6 and the other the score of 4 or less");
                    winnerSetScore = winnerSet.getScoreSet();
                    looserSetScore = looserSet.getScoreSet();
                    currentSetModel.setStateGame(GAMESTATE.FINISHED);
                    saveOrUpdateSet(currentSetModel);
                    setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto(winnerSet.getName(), winnerSet.getSurname(), true), winnerSetScore, looserSetScore, false);
                } else if (looserSet.getScoreSet() == 5) {
                    logger.info("we are still playing because the looser reach the score of 5");
                    gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel);
                    setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto("", "", true), 0, 0, false);

                }
            }
            if (playerModel1.getScoreSet() == 7 || playerModel2.getScoreSet() == 7) {
                logger.info("we can close the set because one player reached a score of 7 ");
                winnerSet = getPlayerScore7(playerModel1, playerModel2);
                looserSet = getLooserOfSet(playerModel1, playerModel2);
                winnerSetScore = winnerSet.getScoreSet();
                looserSetScore = looserSet.getScoreSet();
                currentSetModel.setStateGame(GAMESTATE.FINISHED);
                saveOrUpdateSet(currentSetModel);
                setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto(winnerSet.getName(), winnerSet.getSurname(), true), winnerSetScore, looserSetScore, false);
            }

            if (playerModel1.getScoreSet() == 6 && playerModel2.getScoreSet() == 6 && !checkTieBreakRule(currentSetModel)) {
                logger.info("we are still playing because the two players have a score of 6");
                currentSetModel.setTieBreakRule(true);
                setModelWithTieBreak = saveOrUpdateSet(currentSetModel);
                gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2(), setModelWithTieBreak);
                setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto(winnerSet.getName(), winnerSet.getSurname(), true), winnerSetScore, looserSetScore, true);

            }
            if (checkTieBreakRule(currentSetModel)) {
                logger.info("The tie break rule is activated => we calculate the Tie break score when playing");
                GameOutputDto gameOutputDto = gameService.playTennisGameService(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel);
                logger.info("Get the models of the two players");
                PlayerModel firstPlayer = playerService.getPlayerModelByNameAndSurname(gameOutputDto.getPlayer1().getName(), gameOutputDto.getPlayer1().getSurname(), lastGame.getIdGame()).get(0);
                PlayerModel secondPlayer = playerService.getPlayerModelByNameAndSurname(gameOutputDto.getPlayer2().getName(), gameOutputDto.getPlayer2().getSurname(), lastGame.getIdGame()).get(0);
                logger.info("Get the models of the two players in the GameTennis -1");
                PlayerModel lastGameFirstPlayer = playerService.getPlayerModelByNameAndSurname(gameOutputDto.getPlayer1().getName(), gameOutputDto.getPlayer1().getSurname(), lastGame.getIdGame() - 1).get(0);
                PlayerModel lastGameSecondPlayer = playerService.getPlayerModelByNameAndSurname(gameOutputDto.getPlayer2().getName(), gameOutputDto.getPlayer2().getSurname(), lastGame.getIdGame() - 1).get(0);
                logger.info("Identify the actual winner");
                PlayerModel actualWinner = playerService.getPlayerModelByNameAndSurname(gameOutputDto.getWinnerOfTheGame().getName(), gameOutputDto.getWinnerOfTheGame().getSurname(), lastGame.getIdGame()).get(0);
                logger.info("Update The Tie break score of the two players");
                if (actualWinner.getName().equals(firstPlayer.getName())) {
                    firstPlayer.setTieBreakScore(lastGameFirstPlayer.getTieBreakScore() + 1);
                    secondPlayer.setTieBreakScore(lastGameSecondPlayer.getTieBreakScore());
                } else if (actualWinner.getName().equals(secondPlayer.getName())) {
                    firstPlayer.setTieBreakScore(lastGameFirstPlayer.getTieBreakScore());
                    secondPlayer.setTieBreakScore(lastGameSecondPlayer.getTieBreakScore() + 1);
                }
                logger.info("Update the two players in the database");
                playerService.saveOrUpdatePlayer(firstPlayer);
                playerService.saveOrUpdatePlayer(secondPlayer);
                setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto(winnerSet.getName(), winnerSet.getSurname(), true), winnerSetScore, looserSetScore, true);

            }
            if (getENdTieBreakRule(playerModel1, playerModel2)) {
                logger.info("we can close the set because we get the final tie break score");
                currentSetModel.setStateGame(GAMESTATE.FINISHED);
                saveOrUpdateSet(currentSetModel);
                setOutputDtoResult = new SetOutputDto(gameDto.getPlayer1(), gameDto.getPlayer2(), currentSetModel.getStateGame(), new PlayerDto("", "", true), winnerSetScore, looserSetScore, true);
            }


        }
        return setOutputDtoResult;
    }


    /**
     * Identify the condition that close the tie break rule
     *
     * @param playerModel1
     * @param playerModel2
     * @return boolean
     */
    private boolean getENdTieBreakRule(PlayerModel playerModel1, PlayerModel playerModel2) {
        logger.info("check if the tie break score of one player is at least 7 points and 2 points more than his opponent ");
        if ((playerModel1.getTieBreakScore() >= 7 || playerModel2.getTieBreakScore() >= 7) &&
                (Math.abs(playerModel1.getTieBreakScore() - Math.abs(playerModel2.getTieBreakScore())) >= 2)) {
            return true;
        } else return false;
    }

    /**
     * Check if the tie break rule is activated or not
     *
     * @param setModel
     * @return boolean
     */
    private boolean checkTieBreakRule(SetModel setModel) {
        logger.info("check the activation of the tie break rule");
        Optional<SetModel> setModel1 = Optional.ofNullable(setRepository.findById(setModel.getIdSet())).orElse(null);
        if (setModel1.isPresent() && setModel1.get().isTieBreakRule())
            return true;
        else return false;
    }

    /**
     * Get the player who have a score of 7
     *
     * @param playerModel1
     * @param playerModel2
     * @return PlayerModel
     */
    private PlayerModel getPlayerScore7(PlayerModel playerModel1, PlayerModel playerModel2) {
        return (playerModel1.getScoreSet() == 7) ? playerModel1 : playerModel2;
    }

    /**
     * Get the looser between two players
     *
     * @param playerModel1
     * @param playerModel2
     * @return
     */
    private PlayerModel getLooserOfSet(PlayerModel playerModel1, PlayerModel playerModel2) {
        return (playerModel1.getScoreSet() < playerModel2.getScoreSet()) ? playerModel1 : playerModel2;
    }


    /**
     * Get the max score of the set between the two players
     *
     * @param playerModel1
     * @param playerModel2
     * @return PlayerModel
     */
    private PlayerModel getMaxScoreOfSet(PlayerModel playerModel1, PlayerModel playerModel2) {
        return (playerModel1.getScoreSet() > playerModel2.getScoreSet()) ? playerModel1 : playerModel2;
    }

    /**
     * Indicate if the max score is 6 or not
     *
     * @param scorePlayer1
     * @param scorePlayer2
     * @return boolean
     */
    private boolean maxScore(int scorePlayer1, int scorePlayer2) {
        return ((scorePlayer1 == 6) || (scorePlayer2 == 6)) ? true : false;

    }

    /**
     * Save or update a set
     *
     * @param setModel
     * @return SetModel
     * @throws SaveUpdateDBException
     */
    private SetModel saveOrUpdateSet(SetModel setModel) throws SaveUpdateDBException {
        logger.info("save or update a player in the database");
        return Optional.ofNullable(setRepository.save(setModel))
                .orElseThrow(() -> new SaveUpdateDBException(this.getClass(), CODEEXCEPTION.SAVEUPDATEPROBLEM.getCodeValue(), "Database save/ update problem"));

    }

    /**
     * Find ao create a set of a tennis game
     *
     * @param idSet
     * @return
     */
    private SetModel findOrCreateSet(Long idSet) {
        logger.info("Get The Set of Tennis By its id");
        Optional<SetModel> setModel = Optional.of(setRepository.findById(idSet)).orElse(null);
        return (setModel.isPresent()) ? setModel.get()
                : new SetModel(idSet, "Set 1", GAMESTATE.INPROGRESS, null, false);

    }

}
