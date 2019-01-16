package com.sg.kata.tennisgame.services;

import com.sg.kata.tennisgame.dto.GameOutputDto;
import com.sg.kata.tennisgame.dto.PlayerDto;
import com.sg.kata.tennisgame.enums.CODEEXCEPTION;
import com.sg.kata.tennisgame.enums.GAMESTATE;
import com.sg.kata.tennisgame.models.GameModel;
import com.sg.kata.tennisgame.models.PlayerModel;
import com.sg.kata.tennisgame.repositories.IGameRepository;
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
@FieldDefaults(level= AccessLevel.PRIVATE)
public class GameService implements  IGameService{

    Logger logger = LoggerFactory.getLogger(GameService.class);
    @Autowired
    IGameRepository gameRepository;

    @Autowired
    IPlayerService playerService;

    /**
     * play a step in a game between two players
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
    public GameOutputDto playTennisGameService(PlayerDto player1, PlayerDto player2 ) throws GameClosedException,SaveUpdateDBException,PlayerNotFoundException,SearchParamsException,NoWinnerOfPointException,PlayersNotExistException {
        String winnerOfTheGame = "";
        PlayerDto loserOfTheGame = null;
        logger.info("Check the actual game if exists or create a new game");
        GameModel currentGame = checkOrInitialiseTheGame(player1, player2);
        logger.info("check if the two players exist in the database");
        if (checkPlayers(player1, player2)){

            logger.info("Check if the game is still in progress");
            if (currentGame.getStateGame() == GAMESTATE.INPROGRESS) {
                logger.info("get the player who win a point");
                PlayerDto winnerOfThePoint = getWinnerOfThePoint(player1, player2);
                logger.info("Get the looser of the point");
                loserOfTheGame = (!winnerOfThePoint.equals(player1)) ? player1 : player2;
                logger.info("Get the model of the winner from the database");

                PlayerModel winnerOfThePointModel = playerService.getPlayerModelByNameAndSurname(winnerOfThePoint.getName(), winnerOfThePoint.getSurname()).get(0);
                logger.info("if his last score is 40 and he is wining a point => he is a winner of the game");

                    if (winnerOfThePointModel.getScore() == 40) {
                        logger.info("set the score of the winner to 0");
                        winnerOfThePointModel.setScore(0);
                        logger.info("Change the state of the Game to Finished");
                        currentGame.setStateGame(GAMESTATE.FINISHED);
                        logger.info("Update the Game in the database");
                        try {
                            saveOrUpdateGame(currentGame);
                        } catch (SaveUpdateDBException e) {
                            e.printStackTrace();
                        }
                        logger.info("Change the infos of the winner");
                        winnerOfTheGame = winnerOfThePointModel.getName() + " " + winnerOfThePointModel.getSurname();
                        winnerOfThePointModel.setWinAPoint(true);
                        logger.info("Change the infos of the looser");
                        PlayerModel looser = playerService.getPlayerModelByNameAndSurname(loserOfTheGame.getName(), loserOfTheGame.getSurname()).get(0);
                        looser.setGame(currentGame);
                        looser.setScore(0);
                        logger.info("Update the looser in the database");
                        try {
                            playerService.saveOrUpdatePlayer(looser);
                        } catch (SaveUpdateDBException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        logger.info(" increment the score of the winner of the point");
                        winnerOfThePointModel.setScore(getTheNewScore(winnerOfThePointModel.getScore()));

                    }
                logger.info("update the winner in the db");
                try {
                    playerService.saveOrUpdatePlayer(winnerOfThePointModel);
                } catch (SaveUpdateDBException e) {
                    e.printStackTrace();
                }
            }
            else if (currentGame.getStateGame() == GAMESTATE.FINISHED) {
                logger.error("The Game is Finished, we can't play more in this game. It's closed");
                throw new GameClosedException(this.getClass(), CODEEXCEPTION.CLOSED.getCodeValue(), "The Game is closed ");
            }
    }

        logger.info("fill the information of the output : Players + Game +status of the Game and Winner");
        Map<String, Integer> myMap = new HashMap<>();
        myMap.put(player1.getName() +" "+player1.getSurname(),playerService.findPlayerScoreByNameSurnameService(player1.getName(),player1.getSurname()));
        myMap.put(player2.getName()+" "+player2.getSurname(),playerService.findPlayerScoreByNameSurnameService(player2.getName(),player2.getSurname()));
        GameOutputDto gameOutputDto= GameOutputDto.builder()
                .playerDto1(player1)
                .playerDto2(player2)
                .stateGame(currentGame.getStateGame())
                .scorePlayers(myMap)
                .winnerOfTheGame(winnerOfTheGame)
                .build();
        return  gameOutputDto;
    }

    /**
     * Check if the two/one player(s) entered by the tennis referee exist in the database or not
     * @param playerDto1
     * @param playerDto2
     * @return Boolean
     * @throws PlayerNotFoundException
     * @throws PlayersNotExistException
     * @throws SearchParamsException
     */
    private Boolean checkPlayers(PlayerDto playerDto1,PlayerDto playerDto2) throws PlayerNotFoundException,PlayersNotExistException,SearchParamsException{
        logger.info("Check if the players exist");
        if((playerService.getPlayerModelByNameAndSurname(playerDto1.getName(),playerDto1.getSurname()).size()==0 &&
                playerService.getPlayerModelByNameAndSurname(playerDto2.getName(),playerDto2.getSurname()).size()==0))
            throw new PlayersNotExistException(this.getClass(), CODEEXCEPTION.PLAYERSNOTFOUND.getCodeValue(), "Two players doesn't exist");
        else if (playerService.getPlayerModelByNameAndSurname(playerDto1.getName(),playerDto1.getSurname()).size()==0)
            throw new PlayerNotFoundException(this.getClass(),CODEEXCEPTION.PLAYERNOTFOUND.getCodeValue(),"The Player"+playerDto1.getName()+" "+playerDto1.getSurname()+" doesn't exist in the database");
        else if(playerService.getPlayerModelByNameAndSurname(playerDto2.getName(),playerDto2.getSurname()).size()==0)
            throw new PlayerNotFoundException(this.getClass(),CODEEXCEPTION.PLAYERNOTFOUND.getCodeValue(),"The Player"+playerDto2.getName() +" "+playerDto2.getSurname()+" doesn't exist in the database");
        else return true;



    }

    /**
     * check if the game exist or initialise it
     * @param player1
     * @param player2
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel checkOrInitialiseTheGame(PlayerDto player1, PlayerDto player2) throws  SaveUpdateDBException{
        logger.info("Check if the game exist or initialise it");
        return (getGameById(1L)==null)?initialiseGameWithPlayers(player1,player2):getGameById(1L);
    }


    /**
     * save or update a game model
     * @param gameModel
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel saveOrUpdateGame(GameModel gameModel) throws SaveUpdateDBException{
        logger.info("save or update a game in the database");
        return Optional.ofNullable(gameRepository.save(gameModel))
                .orElseThrow(()->new SaveUpdateDBException(this.getClass(), CODEEXCEPTION.SAVEUPDATEPROBLEM.getCodeValue(), "Database save/ update problem"));
    }

    /**
     * initialise the players that will play in the game
     * @param player1
     * @param player2
     * @return List<PlayerModel>
     */
    private List<PlayerModel> initialisePlayers(PlayerDto player1,PlayerDto player2){
        logger.info("initialise the players of the new game");
        List<PlayerModel> playerModelList= new ArrayList<>();
        try{
            PlayerModel playerModel1 =playerService.saveOrUpdatePlayer(new PlayerModel(player1.getName(),player1.getSurname(),0,false));
            playerModel1.setGame(null);
            PlayerModel playerModel2 = playerService.saveOrUpdatePlayer(new PlayerModel(player2.getName(),player2.getSurname(),0,false));
            playerModel2.setGame(null);
            playerModelList.add(playerModel1);
            playerModelList.add(playerModel2);
            playerService.saveOrUpdatePlayer(playerModel1);
            playerService.saveOrUpdatePlayer(playerModel2);
        }
         catch (SaveUpdateDBException e) {
            e.printStackTrace();
    }
        return playerModelList;

    }

    /**
     * initialise the game and its players before starting playing
     * @param player1
     * @param player2
     * @return GameModel
     * @throws SaveUpdateDBException
     */
    private GameModel initialiseGameWithPlayers(PlayerDto player1,PlayerDto player2) throws SaveUpdateDBException{
        logger.info("initialise the game with its players");
        List<PlayerModel> currentPlayersList = initialisePlayers(player1,player2);
        GameModel gameModel = saveOrUpdateGame(new GameModel(0L,"Game 1",GAMESTATE.INPROGRESS,currentPlayersList));
        currentPlayersList.forEach(playerItem->{
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
     * @param gameId
     * @return GameModel
     */
    private GameModel getGameById(Long gameId){
        logger.info("get The game by its id");
        Optional<GameModel> gameModel= Optional.of(gameRepository.findById(gameId)).orElse(null);
         return  (gameModel.isPresent())? gameModel.get() :null;
    }


    /**
     * get the winAPoint of the current point between the two players
     * @param player1
     * @param player2
     * @return PlayerDto
     * @throws NoWinnerOfPointException
     */
    private PlayerDto getWinnerOfThePoint(PlayerDto player1, PlayerDto player2 ) throws NoWinnerOfPointException{
        logger.info("Specify the player which win a point");
        if(player1.getWinAPoint()==player2.getWinAPoint())
            throw new NoWinnerOfPointException(this.getClass(), CODEEXCEPTION.NOWINNEROFPOINT.getCodeValue(),"We must have a unique winner of the point");
        else if (player1.getWinAPoint() == true) return player1;
        else return player2;


    }

    /**
     * get the new score by the last score when a player win
     * @param lastScore
     * @return int
     */
    private int getTheNewScore(int lastScore){
        logger.info("return the new score according to the last score");
        int currentScore = 0;
        switch (lastScore){
            case 0 :
                currentScore =15;
                break;

            case 15 :
                currentScore =30;
                break;

            case 30 :
                currentScore =40;
                break;

            case 40 :
                currentScore =0;
                break;

            default :
                currentScore =-1;
                break;
        }
        return currentScore;
    }



}
