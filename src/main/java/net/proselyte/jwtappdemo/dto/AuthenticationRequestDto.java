package net.proselyte.jwtappdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Schema(description = "AuthenticationRequestDto Сущность предоставляющая пользователя в запросе, при аутентификации пользователя в системе")
@Component
public class AuthenticationRequestDto {
    @Schema(description = "Логин пользователя")
    private String username;
    @Schema(description = "Пароль пользователя")
    private String password;
}
