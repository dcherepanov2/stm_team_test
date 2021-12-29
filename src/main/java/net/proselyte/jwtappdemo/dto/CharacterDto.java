package net.proselyte.jwtappdemo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import net.proselyte.jwtappdemo.model.Character;
import net.proselyte.jwtappdemo.model.Comics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel("CharacterDto Сущность, представляющий поля супергероя в ответе")
public class CharacterDto {

    @JsonProperty("slug")
    @Schema(description = "Мнемонический идентфикатор в системе")
    private String slug;

    @JsonProperty("name")
    @Schema(description = "Имя супергероя")
    private String name;

    @JsonProperty("title")
    @Schema(description = "Заголовок супергероя")
    private String title;

    @JsonProperty("comics")
    @Schema(description = "Комиксы супергероя")
    List<ComicsDto> comics = new ArrayList<>();

    public CharacterDto(Character character) {
        this.slug = character.getSlug();
        ArrayList<ComicsDto> comicsDtos = new ArrayList<>();
        character.getComics().forEach(x -> comicsDtos.add(new ComicsDto(x,1)));
        this.comics = comicsDtos;
        this.title = character.getTitle();
        this.name = character.getName();
    }

    public CharacterDto(Character character, int i) {
        this.slug = character.getSlug();
        this.title = character.getTitle();
        this.name = character.getName();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ComicsDto> getComics() {
        return comics;
    }

    public void setComics(List<ComicsDto> comics) {
        this.comics = comics;
    }
}
