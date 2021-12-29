package net.proselyte.jwtappdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;

@ApiModel("ExceptionDto Сущность, представляющий поля ошибки при некорректном использовании Marvel API ")
public class ExceptionDto {

     @JsonProperty("status")
     @Schema(description = "Статус ошибки")
     private String status;


     @JsonProperty("message")
     @Schema(description = "Сообщение об ошибке")
     private String message;

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
