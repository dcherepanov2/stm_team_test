package net.proselyte.jwtappdemo.rest;

import io.swagger.annotations.Api;
import net.proselyte.jwtappdemo.dto.AuthenticationRequestDto;
import net.proselyte.jwtappdemo.dto.CharacterDtoRequest;
import net.proselyte.jwtappdemo.dto.ExceptionDto;
import net.proselyte.jwtappdemo.model.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "Контроллер, созданный с целью отдать сваггеру нужные объекты для их описания. Не используется для реализации бизнес-логики", hidden = true)
public class ForDocumentationApiController {

    @PostMapping("/$2a$12$lGFGDSbgG8DF0c6QtstDhuHC3/WjVP33Szv.KIP3rCpksC")
    public User userEntity(){
        return new User();
    }

    @PostMapping("/$2a$12$lGFGDSbgG8DF0c6QtstDhuHC3/WjVP33Szv.KIP3rCpks")
    public ExceptionDto exceptionEntity(){
        return new ExceptionDto();
    }

    @PostMapping("/$2a$12$lGFGDSbgG8DF0c6QtstDhuHC3/WjVP33Szv.KIP3rCps")
    public CharacterDtoRequest characterDtoRequestEntity(){
        return new CharacterDtoRequest();
    }

    @PostMapping("/$2a$12$lGFGDSbgG8DF0c6QtstDhuHC3/WjVP33SzvKIP3rCps")
    public AuthenticationRequestDto authenticationRequestDto(){
        return new AuthenticationRequestDto();
    }
}
