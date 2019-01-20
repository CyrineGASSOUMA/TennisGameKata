package com.sg.kata.tennisgame.repositories;

import com.sg.kata.tennisgame.models.SetModel;
import org.springframework.data.repository.CrudRepository;

public interface ISetRepository extends CrudRepository<SetModel, Long> {
}
