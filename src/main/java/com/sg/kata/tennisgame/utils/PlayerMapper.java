package com.sg.kata.tennisgame.utils;

import com.sg.kata.tennisgame.dto.PlayerOutputDto;
import com.sg.kata.tennisgame.models.PlayerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PlayerMapper {
    PlayerMapper PLAYER_MAPPER= Mappers.getMapper(PlayerMapper.class);

    PlayerOutputDto playerOutputDtoToPlayer(PlayerModel playerModel);



}

