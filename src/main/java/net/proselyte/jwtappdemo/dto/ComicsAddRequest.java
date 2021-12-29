package net.proselyte.jwtappdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Component
public class ComicsAddRequest {

    @JsonProperty("name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 90, message = "Name should be between 1 and 90 characters")
    @Schema(description = "Название комикса. Минимальный размер 1, максимальный 90")
    private String name;

    @JsonProperty("authors")
    @NotEmpty(message = "Authors should not be empty")
    @Size(min = 1, max = 200, message = "Authors should be between 1 and 90 characters")
    @Schema(description = "Именна авторов. Минимальный размер 1, максимальный 200")
    private String authors;

    @JsonProperty("painter")
    @NotEmpty(message = "Painter should not be empty")
    @Size(min = 1, max = 200, message = "Painter should be between 1 and 90 characters")
    @Schema(description = "Имя художника. Минимальный размер 1, максимальный 200")
    private String painter;

    @JsonProperty("description")
    @NotEmpty(message = "Description should not be empty")
    @Size(min = 1, max = 300, message = "Description should be between 1 and 1000 characters")
    @Schema(description = "Имя художника. Минимальный размер 1, максимальный 300")
    @Column(columnDefinition = "VARCHAR(300) NOT NULL")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
