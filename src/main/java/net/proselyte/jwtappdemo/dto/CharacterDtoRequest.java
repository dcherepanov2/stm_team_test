package net.proselyte.jwtappdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Component
@ApiModel("CharacterDtoRequest Сущность, представляющий поля супергероя при запросе добавления супергероя")
public class CharacterDtoRequest {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 90, message = "Name should be between 1 and 90 characters")
    @JsonProperty("name")
    @Schema(description = "Имя супергероя. Минимальный размер 1, максимальный 90")
    private String name;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 1, max = 1000, message = "Title should be between 1 and 1000 characters")
    @JsonProperty("title")
    @Schema(description = "Заголовок с текстом. Минимальный размер 1, максимальный 1000")
    @Column(name = "title", columnDefinition = "VARCHAR(1000) NOT NULL")
    private String title;

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
}
