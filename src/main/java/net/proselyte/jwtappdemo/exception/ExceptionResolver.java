package net.proselyte.jwtappdemo.exception;

import net.proselyte.jwtappdemo.dto.ExceptionDto;
import net.proselyte.jwtappdemo.exception.characterException.CharacterAddException;
import net.proselyte.jwtappdemo.exception.characterException.CharacterException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsAddException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsException;
import net.proselyte.jwtappdemo.exception.imageException.ImageExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(net.proselyte.jwtappdemo.security.jwt.JwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handle(Exception e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());
        exceptionDto.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
        return exceptionDto;
    }

    @ExceptionHandler({CharacterException.class, ComicsException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNoHandlerFound(Exception e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());
        exceptionDto.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
        return exceptionDto;
    }

    @ExceptionHandler({ImageExceptionBadRequest.class, CharacterAddException.class, ComicsAddException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto responseStatus(Exception e){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());
        exceptionDto.setStatus(String.valueOf(HttpStatus.BAD_REQUEST));
        return exceptionDto;
    }
}

