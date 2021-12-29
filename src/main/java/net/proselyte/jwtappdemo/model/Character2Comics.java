package net.proselyte.jwtappdemo.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity
@Table(name = "character2comics")
@ApiModel("Character2Comics Сущность предоставляющая связь ManyToMany между комиксами и супергероями")
public class Character2Comics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comics_id")
    private Integer comicsId;

    @Column(name = "character_id")
    private Integer characterId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComicsId() {
        return comicsId;
    }

    public void setComicsId(Integer comicsId) {
        this.comicsId = comicsId;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }
}
