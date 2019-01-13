package com.sg.kata.tennisgame.repositories;

import com.sg.kata.tennisgame.models.GameModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameRepository extends CrudRepository<GameModel, Long> {
}
