package net.proselyte.jwtappdemo.dto;



import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import net.proselyte.jwtappdemo.model.Character;
import net.proselyte.jwtappdemo.model.Comics;

import java.util.ArrayList;
import java.util.List;

@ApiModel("ComicsDto Сущность, представляющая поля комикса, в ответе не запросы ")
public class ComicsDto {

    @Schema(description = "Идентификатор комикса в системе")
    private Integer id;

    @Schema(description = "Мнемонический идентификатор комикса в системе")
    private String slug;

    @Schema(description = "Имя комикса")
    private String name;

    @Schema(description = "Путь, по которому лежит картинка в системе")
    private String imagePath;

    @Schema(description = "Авторы комиксы")
    private String authors;

    @Schema(description = "Художники")
    private String painter;

    @Schema(description = "Описание комикса")
    private String description;

    List<CharacterDto> characters;

    public ComicsDto(Comics characterBySlug) {
        this.id = characterBySlug.getId();
        this.slug = characterBySlug.getSlug();
        this.authors = characterBySlug.getAuthors();
        this.name = characterBySlug.getName();
        this.imagePath = characterBySlug.getImagePath();
        this.painter = characterBySlug.getPainter();
        this.description = characterBySlug.getDescription();
        this.characters = new ArrayList<>();
        for(Character character: characterBySlug.getCharacters()){
            characters.add(new CharacterDto(character,1));
        }
    }

    public ComicsDto(Comics characterBySlug,int i) {
        this.id = characterBySlug.getId();
        this.slug = characterBySlug.getSlug();
        this.authors = characterBySlug.getAuthors();
        this.name = characterBySlug.getName();
        this.imagePath = characterBySlug.getImagePath();
        this.painter = characterBySlug.getPainter();
        this.description = characterBySlug.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPainter() {
        return painter;
    }

    public void setPainter(String painter) {
        this.painter = painter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<CharacterDto> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterDto> characters) {
        this.characters = characters;
    }
}
