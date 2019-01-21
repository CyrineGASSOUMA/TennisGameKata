package com.sg.kata.tennisgame.models;

import com.sg.kata.tennisgame.enums.GAMESTATE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Game")
@ApiModel(value = "GameModel")

public class GameModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game", insertable = false)
    @ApiModelProperty(value = "The id of the game in the db- autogeneated value", example = "3")
    Long idGame;

    @Column(name = "name_game")
    @NonNull
    @ApiModelProperty(value = "The name of the game", example = "Game")
    String nameGame;

    @Column(name = "state")
    @NonNull
    @ApiModelProperty(value = "The state of the game", example = "FINISHed")
    @Enumerated(EnumType.STRING)
    GAMESTATE stateGame;

    @Column(name = "deuce_rule")
    @NonNull
    @ApiModelProperty(value = "The deuce rule is activated or not ")
    boolean deuce;


    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    @ApiModelProperty(value = "The list of the player in the Game", example = "")
    List<PlayerModel> playerModelList;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_set")
    @ApiModelProperty(value = "The set of games", example = "")
    SetModel setModel;


}
