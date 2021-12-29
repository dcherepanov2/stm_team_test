package net.proselyte.jwtappdemo.rest;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import net.proselyte.jwtappdemo.dto.AuthenticationRequestDto;
import net.proselyte.jwtappdemo.model.User;
import net.proselyte.jwtappdemo.security.jwt.JwtTokenProvider;
import net.proselyte.jwtappdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@Api(description = "Контроллер авторизации в системе")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @RequestMapping(
            value = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "POST запрос на выдачу токена авторизации",
            description = "Позволяет авторизоваться в системе. В body передается AuthenticationRequestDto, подробнее об body, которое нужно передать: читать в Models документации"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пароль и логин верные, выдача токена", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n   \"username\": \"user_user\",\n" +
                                            "   \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyX3VzZXIiLCJyb2xlcyI6W10sImlhdCI6MTY0MDYzMjAyMywiZXhwIjoxNjQwNjM1NjIzfQ.PYRxatMgvkvsV4Kgru6lsf4KNVci5UOy1-NjMWQoQ-0\"\n}")
                            }
                    )),
            @ApiResponse(code = 400, message = "В случае, если один из параметров не прошел валидацию", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"state: \": \"400\",\n" +
                                            "    \"message\": \"Invalid username or password\"\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(code = 401, message = "Не возвращается, так как этот метод разрешен для всех пользователей"),
            @ApiResponse(code = 403, message = "Не возвращается, так как этот метод разрешен для всех пользователей"),
            @ApiResponse(code = 404, message = "В случае, если в базе нету передаваемого в теле логина"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n" +
                                            "    \"state: \": \"404\",\n" +
                                            "    \"message\": \"User with username: null not found\"\n" +
                                            "}")
                    })
            )
    })
    public ResponseEntity<Map<Object, Object>> login(AuthenticationRequestDto requestDto){
        try {
            String username = requestDto.getUsername();
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        }catch (UsernameNotFoundException e1){
            throw new UsernameNotFoundException("User with username: " + requestDto.getUsername() + " not found");
        }
        catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
