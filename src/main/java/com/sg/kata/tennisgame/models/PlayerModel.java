package com.sg.kata.tennisgame.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity(name= "Player")
@ApiModel(value="Player Model")
public class PlayerModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_player",insertable = false)
    @ApiModelProperty(value="The id of the game in the db- autogeneated value",example = "1")
    Long idPlayer;

    @Column(name="name")
    @NonNull
    @ApiModelProperty(value="The name of the player",example = "Philipe")
    String name;

    @Column(name="surname")
    @NonNull
    @ApiModelProperty(value="The surname of the player")
    String surname;

    @Column(name="score")
    @NonNull
    @ApiModelProperty(value="The actual score of the player")
    int score;

    @Column(name="score_set")
    @NonNull
    @ApiModelProperty(value="The actual score of the player")
    int scoreSet;

    @Column(name="win_a_point")
    @NonNull
    @ApiModelProperty(value="The player win a point or not")
    Boolean winAPoint;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_game")
    @ApiModelProperty(value="The game where the player play")
    GameModel game;

    @Column(name="has_advantage")
    @NonNull
    @ApiModelProperty(value="The player has advantage or not")
    Boolean hasAdvantage;


}
