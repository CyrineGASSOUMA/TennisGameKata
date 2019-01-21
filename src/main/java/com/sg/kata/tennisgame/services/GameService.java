package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.models.SetModel;
import com.sg.kata.tennisgame.repositories.IGameRepository;
import com.sg.kata.tennisgame.utils.PlayerMapper;
import com.sg.kata.tennisgame.utils.exceptions.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameService implements IGameService {

    Logger logger = LoggerFactory.getLogger(GameService.class);
    @Autowired
    IGameRepository gameRepository;

    @Autowired
    IPlayerService playerService;

    /**
     * play a step in a game between two
     *
     * @param player1
     * @param player2
     * @return GameOutputDto
     * @throws GameClosedException
     * @throws SaveUpdateDBException
     * @throws PlayerNotFoundException
     * @throws SearchParamsException
     * @throws NoWinnerOfPointException
     * @throws PlayersNotExistException
     */
    @Transactional
    public GameOutputDto playTennisGameService(PlayerDto player1, PlayerDto player2, SetModel currentSetModel) throws GameClosedException, SaveUpdateDBException, PlayerNotFoundException, SearchParamsException, NoWinnerOfPointException, PlayersNotExistException {
        String winnerOfTheGame = "";
        PlayerDto loserOfTheGame = null;
        String playerHasAdvantage = "";
        Boolean deuce = false;
        logger.info("Get or create the id of the game");

        logger.info("Check the actual game if exists or create a new game");
        GameModel currentGame = checkOrInitialiseTheGame(getOrCreateIdGame(), player1, player2, currentSetModel);
        logger.info("check if the two players exist in the database");
        if (checkPlayers(player1, player2, currentGame.getIdGame())) {
            logger.info("Check if the game is still in progress");
            if (currentGame.getStateGame() == GAMESTATE.INPROGRESS) {
                logger.info("get the player who win a point");
                PlayerDto winnerOfThePoint = getWinnerOfThePoint(player1, player2);
                logger.info("Get the looser of the point");
                loserOfTheGame = (!winnerOfThePoint.equals(player1)) ? player1 : player2;
                logger.info("Get the model of the winner from the database");
                PlayerModel winnerOfThePointModel = playerService.getPlayerModelByNameAndSurname(winnerOfThePoint.getName(), winnerOfThePoint.getSurname(), currentGame.getIdGame()).get(0);
                logger.info("if his last score is 40 and he is wining a point => he is a winner of the game");
                PlayerModel looser = playerService.getPlayerModelByNameAndSurname(loserOfTheGame.getName(), loserOfTheGame.getSurname(), currentGame.getIdGame()).get(0);
                logger.info("Check if we have a winner : One player have the score 40 and the other a score less than 40 or the player who has the advantage win a point");
                if (playerService.playerHasAdvantage(winnerOfThePointModel, currentGame.getIdGame()) || (winnerOfThePointModel.getScore() == 40 && looser.getScore() < 40)) {
                    logger.info("Close the game when finding a winner ");
                    winnerOfTheGame = closeTheGame(winnerOfThePointModel, looser, currentGame, currentSetModel);
                } else if (currentGame.isDeuce()) {
                    logger.info("associate the advantage to the winner if the deuce rule is activated");
                    winnerOfThePointModel.setHasAdvantage(true);
                    playerHasAdvantage = winnerOfThePointModel.getName() + " " + winnerOfThePointModel.getSurname() + " has the advantage";
                    playerService.saveOrUpdatePlayer(looser);
                    currentGame.setDeuce(false);
                    saveOrUpdateGame(currentGame, currentSetModel);
                } else if (winnerOfThePointModel.getScore() != 40) {
                    logger.info(" increment the score of the winner of the point if his actual score isn't 40");
                    winnerOfThePointModel.setScore(getTheNewScore(winnerOfThePointModel.getScore()));

                }
                if ((isDeuce(player1, player2, currentGame.getIdGame())) && !winnerOfThePointModel.getHasAdvantage()) {
                    logger.info("activate the deuce rule if the score of the players is 40 and the winner hasn't the advantage");
                    currentGame.setDeuce(true);
                    saveOrUpdateGame(currentGame, currentSetModel);
                    looser.setHasAdvantage(false);
                    deuce = true;

                }

                winnerOfThePointModel.getSurname();

                logger.info("update the winner in the db");
                try {
                    playerService.saveOrUpdatePlayer(winnerOfThePointModel);
                } catch (SaveUpdateDBException e) {
                    e.printStackTrace();
                }
            } else if (currentGame.getStateGame() == GAMESTATE.FINISHED) {
                logger.error("The Game is Finished, we can't play more in this game. It's closed");
                throw new GameClosedException(this.getClass(), CODEEXCEPTION.CLOSED.getCodeValue(), "The Game is closed ");
            }
        }

        logger.info("fill the information of the output : Players + Game +status of the Game and Winner");
        Map<String, Integer> myMap = new HashMap<>();
        myMap.put(player1.getName() + " " + player1.getSurname(), playerService.findPlayerScoreByNameSurnameService(player1.getName(), player1.getSurname(), currentGame.getIdGame()));
        myMap.put(player2.getName() + " " + player2.getSurname(), playerService.findPlayerScoreByNameSurnameService(player2.getName(), player2.getSurname(), currentGame.getIdGame()));
        GameOutputDto gameOutputDto = GameOutputDto.builder()
                .playerDto1(player1)
                .playerDto2(player2)
                .stateGame(currentGame.getStateGame())
                .scorePlayers(myMap)
                .winnerOfTheGame(getWinnerOfThePoint(player1, player2))
                .deuceRule(deuce)
                .playerHasAdvantage(playerHasAdvantage)
                .build();
        return gameOutputDto;
    }


    /**
     * Get all the games
     *
     * @return List<GameModel>
     */
    private List<GameModel> getAllGames() {
        logger.info("Get all the games in the db");
        return (List<GameModel>) Optional.ofNullable(gameRepository.findAll()).orElse(null);
    }

    /**
     * Get the id of the game or create it
     *
     * @return Long
     * @throws SaveUpdateDBException
     */
    private Long getOrCreateIdGame() throws SaveUpdateDBException {
        logger.info("get the id of the game or create it");
        List<GameModel> gameModelList = getAllGames();
        if (gameModelList.size() == 0) return 1L;
        else if (gameModelList.get(gameModelList.size() - 1).getStateGame() == GAMESTATE.INPROGRESS) {
            return gameModelList.get(gameModelList.size() - 1).getIdGame() + 1L;
        } else if (gameModelList.get(gameModelList.size() - 1).getStateGame() == GAMESTATE.FINISHED) {
            return gameModelList.get(gameModelList.size() - 1).getIdGame() + 1L;
        } else throw new SaveUpdateDBException(this.getClass(), "", "");


    }

    /**
     * Update the information of the players and the game when the game is finished and return the winner
     *
     * @param winnerOfThePointModel
     * @param looser
     * @param currentGame
     * @return String
     */
    private String closeTheGame(PlayerModel winnerOfThePointModel, PlayerModel looser, GameModel currentGame, SetModel currentSetModel) throws SearchParamsException {
        logger.info("set the score of the winner to 0");
        winnerOfThePointModel.setScore(0);
        logger.info("Change the state of the Game to Finished");
        currentGame.setStateGame(GAMESTATE.FINISHED);
        logger.info("Update the Game in the database");
        try {
            saveOrUpdateGame(currentGame, currentSetModel);
        } catch (SaveUpdateDBException e) {
            e.printStackTrace();
        }
        logger.info("Change the infos of the winner");
        String winnerOfTheGame = winnerOfThePointModel.getName() + " " + winnerOfThePointModel.getSurname();
        winnerOfThePointModel.setWinAPoint(true);

        logger.info("Update The Set Score of the winner");
        if (currentGame.getIdGame() == 1L) {
            winnerOfThePointModel.setScoreSet(1);
        } else if (!currentSetModel.isTieBreakRule()) {
            winnerOfThePointModel.setScoreSet(getSetScoreOfLastGame(winnerOfThePointModel, currentGame.getIdGame()) + 1);
        }
        logger.info("Change the infos of the looser");
        looser.setGame(currentGame);
        looser.setScore(0);
        if (currentGame.getIdGame() > 1L) {
            looser.setScoreSet(getSetScoreOfLastGame(looser, currentGame.getIdGame()));
        }
        logger.info("Update the looser in the database");
        try {
            playerService.saveOrUpdatePlayer(looser);
        } catch (SaveUpdateDBException e) {
            e.printStackTrace();
        }
        return winnerOfTheGame;
    }

    /**
     * Find games
     *
     * @return List<GameModel>
     */
    @Transactional
    public List<GameModel> findGames() {
        logger.info("Get the games that exist in the db");
        return (List<GameModel>) Optional.ofNullable(gameRepository.findAll()).orElse(null);

    }

    /**
     * Get the score of the set
     *
     * @param playerModel
     * @param idGame
     * @return int
     * @throws SearchParamsException
     */
    private int getSetScoreOfLastGame(PlayerModel playerModel, Long idGame) throws SearchParamsException {
        logger.info("Get the score of the set of the player");
        return Optional.ofNullable(playerService.getPlayerModelByNameAndSurname(playerModel.getName(), playerModel.getSurname(), idGame - 1).
                get(0).getScoreSet()).orElse(0);
    }

    /**
     * Check if we activate the deuce rule or not
     *
     * @param playerDto1
     * @param playerDto2
     * @return Boolean
     * @throws SearchParamsException
     */
    private Boolean isDeuce(PlayerDto playerDto1, PlayerDto playerDto2, Long idGame) throws SearchParamsException {
        if (playerService.findPlayerScoreByNameSurnameService(playerDto1.getName(), playerDto1.getSurname(), idGame) ==
                playerService.findPlayerScoreByNameSurnameService(playerDto2.getName(), playerDto2.getSurname(), idGame) &&
                playerService.findPlayerScoreByNameSurnameService(playerDto1.getName(), playerDto1.getSurname(), idGame) == 40
                ) return true;
        else return false;
    }

    /**
     * Check if the two/one player(s) entered by the tennis referee exist in the database or not
     *
     * @param playerDto1
     * @param playerDto2
     * @return Boolean
     * @throws PlayerNotFoundException
     * @throws PlayersNotExistException
     * @throws SearchParamsException
     */
    private Boolean checkPlayers(PlayerDto playerDto1, PlayerDto playerDto2, Long idGame) throws PlayerNotFoundException, PlayersNotExistException, SearchParamsException {
        logger.info("Check if the players exist");
        if ((playerService.getPlayerModelByNameAndSurname(playerDto1.getName(), playerDto1.getSurname(), idGame).size() == 0 &&
                playerService.getPlayerModelByNameAndSurname(playerDto2.getName(), playerDto2.getSurname(), idGame).size() == 0))
            throw new PlayersNotExistException(this.getClass(), CODEEXCEPTION.PLAYERSNOTFOUND.getCodeValue(), "Two players doesn't exist");
        else if (playerService.getPlayerModelByNameAndSurname(playerDto1.getName(), playerDto1.getSurname(), idGame).size() == 0)
            throw new PlayerNotFoundException(this.getClass(), CODEEXCEPTION.PLAYERNOTFOUND.getCodeValue(), "The Player" + playerDto1.getName() + " " + playerDto1.getSurname() + " doesn't exist in the database");
        else if (playerService.getPlayerModelByNameAndSurname(playerDto2.getName(), playerDto2.getSurname(), idGame).size() == 0)
            throw new PlayerNotFoundException(this.getClass(), CODEEXCEPTION.PLAYERNOTFOUND.getCodeValue(), "The Player" + playerDto2.getName() + " " + playerDto2.getSurname() + " doesn't exist in the database");
        else return true;


    }

    /**
     * check if the game exist or initialise it
     *
     * @param player1
     * @param player2
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel checkOrInitialiseTheGame(Long idGame, PlayerDto player1, PlayerDto player2, SetModel setModel) throws SaveUpdateDBException, SearchParamsException {
        logger.info("Check if the game exist or initialise it");
        return (getGameById(idGame) == null) ? initialiseGameWithPlayers(player1, player2, setModel, idGame) : getGameById(idGame);
    }


    /**
     * save or update a game model
     *
     * @param gameModel
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel saveOrUpdateGame(GameModel gameModel, SetModel setModel) throws SaveUpdateDBException {
        logger.info("save or update a game in the database");
        gameModel.setSetModel(setModel);
        return Optional.ofNullable(gameRepository.save(gameModel))
                .orElseThrow(() -> new SaveUpdateDBException(this.getClass(), CODEEXCEPTION.SAVEUPDATEPROBLEM.getCodeValue(), "Database save/ update problem"));
    }


    /**
     * initialise the players that will play in the game
     *
     * @param player1
     * @param player2
     * @return List<PlayerModel>
     */
    private List<PlayerModel> initialisePlayers(PlayerDto player1, PlayerDto player2, Long idGame) throws SearchParamsException {
        logger.info("initialise the players of the new game");
        int setScore1 = 0;
        int setScore2 = 0;
        List<PlayerModel> lastGamePlayer1List = playerService.getPlayerModelByNameAndSurname(player1.getName(), player1.getSurname(), idGame - 1);
        List<PlayerModel> lastGamePlayer2List = playerService.getPlayerModelByNameAndSurname(player2.getName(), player2.getSurname(), idGame - 1);


        if (lastGamePlayer1List.size() != 0 && lastGamePlayer2List.size() != 0) {
            setScore1 = lastGamePlayer1List.get(0).getScoreSet();
            setScore2 = lastGamePlayer2List.get(0).getScoreSet();
        }


        List<PlayerModel> playerModelList = new ArrayList<>();
        try {
            PlayerModel playerModel1 = playerService.saveOrUpdatePlayer(new PlayerModel(player1.getName(), player1.getSurname(), 0, setScore1, false, false));
            playerModel1.setGame(null);
            PlayerModel playerModel2 = playerService.saveOrUpdatePlayer(new PlayerModel(player2.getName(), player2.getSurname(), 0, setScore2, false, false));
            playerModel2.setGame(null);
            playerModelList.add(playerModel1);
            playerModelList.add(playerModel2);
            playerService.saveOrUpdatePlayer(playerModel1);
            playerService.saveOrUpdatePlayer(playerModel2);
        } catch (SaveUpdateDBException e) {
            e.printStackTrace();
        }
        return playerModelList;

    }

    /**
     * initialise the game and its players before starting playing
     *
     * @param player1
     * @param player2
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel initialiseGameWithPlayers(PlayerDto player1, PlayerDto player2, SetModel setModel, Long idGame) throws SaveUpdateDBException, SearchParamsException {
        logger.info("initialise the game with its players");
        List<GameModel> gameModelList = getAllGames();
        if (gameModelList.size() == 0) {
            List<PlayerModel> currentPlayersList = initialisePlayers(player1, player2, 1L);
            return associatePlayersToGame(currentPlayersList, setModel);
        } else if (gameModelList.get(gameModelList.size() - 1).getStateGame() == GAMESTATE.FINISHED) {
            List<PlayerModel> currentPlayersList = initialisePlayers(player1, player2, idGame);
            return associatePlayersToGame(currentPlayersList, setModel);

        } else return gameModelList.get(gameModelList.size() - 1);

    }

    /**
     * Associate the two players to the Game
     *
     * @param currentPlayersList
     * @param setModel
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel associatePlayersToGame(List<PlayerModel> currentPlayersList, SetModel setModel) throws SaveUpdateDBException {
        GameModel gameModel = saveOrUpdateGame(new GameModel(0L, "Game", GAMESTATE.INPROGRESS, false, currentPlayersList, null), setModel);
        currentPlayersList.forEach(playerItem -> {
            playerItem.setGame(gameModel);
            try {
                playerService.saveOrUpdatePlayer(playerItem);
            } catch (SaveUpdateDBException e) {
                e.printStackTrace();
            }
        });
        return gameModel;

    }

    /**
     * get the current game by its id
     *
     * @param gameId
     * @return GameModel
     */
    private GameModel getGameById(Long gameId) {
        logger.info("get The game by its id");
        Optional<GameModel> gameModel = Optional.of(gameRepository.findById(gameId)).orElse(null);
        return (gameModel.isPresent()) ? gameModel.get() : null;
    }


    /**
     * get the winAPoint of the current point between the two players
     *
     * @param player1
     * @param player2
     * @return PlayerDto
     * @throws NoWinnerOfPointException
     */
    private PlayerDto getWinnerOfThePoint(PlayerDto player1, PlayerDto player2) throws NoWinnerOfPointException {
        logger.info("Specify the player which win a point");
        if (player1.getWinAPoint() == player2.getWinAPoint())
            throw new NoWinnerOfPointException(this.getClass(), CODEEXCEPTION.NOWINNEROFPOINT.getCodeValue(), "We must have a unique winner of the point");
        else if (player1.getWinAPoint() == true) return player1;
        else return player2;


    }

    /**
     * get the new score by the last score when a player win
     *
     * @param lastScore
     * @return int
     */
    private int getTheNewScore(int lastScore) {
        logger.info("return the new score according to the last score");
        int currentScore = 0;
        switch (lastScore) {
            case 0:
                currentScore = 15;
                break;

            case 15:
                currentScore = 30;
                break;

            case 30:
                currentScore = 40;
                break;

            case 40:
                currentScore = 0;
                break;

            default:
                currentScore = -1;
                break;
        }
        return currentScore;
    }


}
