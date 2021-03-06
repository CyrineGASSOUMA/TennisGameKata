package com.sg.kata.tennisgame.models;

import com.sg.kata.tennisgame.enums.GameState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Entity(name = "set_tennis")
@ApiModel(value = "SetModel")

public class SetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_set", insertable = false)
    @ApiModelProperty(value = "The id of the tennis set in the db- autogeneated value", example = "1")
    Long idSet;

    @Column(name = "name_set")
    @NonNull
    @ApiModelProperty(value = "The name of the set", example = "Set 1")
    String nameSet;

    @Column(name = "state")
    @NonNull
    @ApiModelProperty(value = "The state of the set", example = "FINISHed")
    @Enumerated(EnumType.STRING)
    GameState stateGame;


    @OneToMany(mappedBy = "setModel", fetch = FetchType.LAZY)
    @ApiModelProperty(value = "The list of games in the set")
    List<GameModel> gameModelList;

    @ApiModelProperty(value = "The tie break rule", example = "true")
    @Column(name = "tie_break")
    boolean tieBreakRule;


}
