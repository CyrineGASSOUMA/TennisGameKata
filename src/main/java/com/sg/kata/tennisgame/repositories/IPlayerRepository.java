package com.sg.kata.tennisgame.repositories;

import com.sg.kata.tennisgame.models.PlayerModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IPlayerRepository extends CrudRepository<PlayerModel, Long> {
    @Query(value="select * from Player where name=?1 and surname=?2 and id_game=?3",nativeQuery=true)
    List<PlayerModel> findPlayerByNameAndSurname(String name, String surname, Long idGAme);

    @Query(value="select score from Player where name=?1 and surname=?2 and id_game=?3",nativeQuery=true)
    int findPlayerScoreByNameSurname(String name, String surname, Long idGAme);


}
