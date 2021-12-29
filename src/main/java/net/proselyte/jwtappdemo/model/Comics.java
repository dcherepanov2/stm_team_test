package net.proselyte.jwtappdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comics")
@ApiModel("Comics Сущность представляющая комикс, служит для сохранения в базу данных")
public class Comics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Integer id;

    @NotEmpty(message = "Slug should not be empty")
    @Size(min = 10, max = 16, message = "Slug should be between 10 and 16 characters")
    @Column(unique = true)
    private String slug;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 90, message = "Name should be between 1 and 90 characters")
    private String name;

    private String imagePath;

    @NotEmpty(message = "Authors should not be empty")
    @Size(min = 1, max = 200, message = "Name should be between 1 and 90 characters")
    private String authors;

    @NotEmpty(message = "Painter should not be empty")
    @Size(min = 1, max = 200, message = "Name should be between 1 and 90 characters")
    private String painter;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 1, max = 300, message = "Title should be between 1 and 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "character2comics",
            joinColumns  = @JoinColumn(name = "comics_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    List<Character> characters = new ArrayList<>();

    @Column(name = "character_slug")
    String character_slug;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCharacter_slug() {
        return character_slug;
    }

    public void setCharacter_slug(String character_slug) {
        this.character_slug = character_slug;
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

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

}
