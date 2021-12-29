package net.proselyte.jwtappdemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "character")
@ApiModel("Character Сущность предоставляющая супергероя, служит для сохранения в базу данных")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Positive
    @JsonProperty("id")
    private Integer id;

    @NotEmpty(message = "Slug should not be empty")
    @Size(min = 10, max = 16, message = "Slug should be between 10 and 16 characters")
    @JsonProperty("slug")
    private String slug;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 90, message = "Name should be between 1 and 90 characters")
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 1, max = 1000, message = "Title should be between 1 and 1000 characters")
    @Column(columnDefinition = "TEXT")
    @JsonProperty("title")
    private String title;

    @ManyToMany
    @JoinTable(
            name = "character2comics",
            joinColumns  = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "comics_id")
    )
    List<Comics> comics = new ArrayList<>();

    @NotNull
    @Column(name = "comics_string")
    private String comics_string;//поле созданно для динамической сортировки, slug комикса указывается через запятую

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Comics> getComics() {
        return comics;
    }

    public void setComics(List<Comics> comics) {
        this.comics = comics;
    }

    public String getComics_string() {
        return comics_string;
    }

    public void setComics_string(String comics_string) {
        this.comics_string = comics_string;
    }
}
