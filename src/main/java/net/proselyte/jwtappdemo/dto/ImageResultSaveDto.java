package net.proselyte.jwtappdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;

@ApiModel("ImageResultSaveDto Сущность, представляющий поля ответа при корректном сохранении картинки ")
public class ImageResultSaveDto {
    @JsonProperty("status")
    @Schema(description = "Статус загрузки картинки на сервер")
    String status;

    @JsonProperty("message")
    @Schema(description = "Сообщение о статусе загрузки")
    String message;

    public ImageResultSaveDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
