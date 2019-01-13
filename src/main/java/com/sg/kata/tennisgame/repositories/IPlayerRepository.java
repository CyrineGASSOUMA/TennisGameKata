package com.sg.kata.tennisgame.repositories;

import com.sg.kata.tennisgame.models.PlayerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerRepository extends CrudRepository<PlayerModel, Long> {
}
