package com.sg.kata.tennisgame.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name= "Player")
@ApiModel(value="Player Model")
public class PlayerModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_player",insertable = false)
    @Getter
    @Setter
    @ApiModelProperty(value="The id of the game in the db- autogeneated value",example = "1")
    Long idPlayer;

    @Column(name="name")
    @ApiModelProperty(value="The name of the player",example = "Philipe")
    String name;

    @Column(name="The surname of the player")
    @ApiModelProperty(value="The first player",example = "")
    String surname;

    @Column(name="score")
    @ApiModelProperty(value="The first player",example = "")
    int score;

    @Column(name="winner_of_game")
    @ApiModelProperty(value="The first player",example = "")
    Boolean winner;


    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_game")
    @ApiModelProperty(value="The first player",example = "")
    GameModel game;
}
