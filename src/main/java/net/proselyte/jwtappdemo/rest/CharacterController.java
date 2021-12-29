package net.proselyte.jwtappdemo.rest;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import net.proselyte.jwtappdemo.dto.CharacterDto;
import net.proselyte.jwtappdemo.dto.CharacterDtoRequest;
import net.proselyte.jwtappdemo.dto.ComicsDto;
import net.proselyte.jwtappdemo.exception.characterException.CharacterAddException;
import net.proselyte.jwtappdemo.exception.characterException.CharacterException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsAddException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsException;
import net.proselyte.jwtappdemo.model.Character;
import net.proselyte.jwtappdemo.model.Comics;
import net.proselyte.jwtappdemo.service.CharacterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(
        value = "/v1/public/characters",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@Api(description = "Контроллер супергероев")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @Operation(
            summary = "GET запрос на получение всех супер героев",
            description = "Позволяет получить всех супер героев отфильтрованных по переданным параметрам в запросе"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "В случае, если один из параметров не прошел валидацию", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"timestamp\": \"2021-12-26T20:00:17.005+0000\",\n" +
                                            "    \"status\": 400,\n" +
                                            "    \"error\": \"Bad Request\",\n" +
                                            "    \"message\": \"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \\\"цйу\\\"\",\n" +
                                            "    \"path\": \"/characters\"\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(code = 401, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 403, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 404, message = "В случае, если в базе нету супергероев или по переданным в запросе параметрам фильтрации не найдены супергерои"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"It seems the superheroes haven't been added yet. Wait for the developers to do this.\"\n}")
                    })
            )
    })
    public ResponseEntity<List<CharacterDto>> getAllCharacters(
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "offset") Integer offset,
            @Parameter(description = "Лимит выводимых на страницу элементов")
            @RequestParam(value = "limit") Integer limit,
            @Parameter(description = "Параметр фильтрации имени")
            @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Параметр фильтрации заголовка")
            @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "Мнемонический идентефикатор, который подставляется в ссылку(в других методах моего API)")
            @RequestParam(value = "slug", required = false) String slug,
            @Parameter(description = "Индекс сортировки супергероев(1 - по имени ,2 - Мнеманический идентификатор, 3 - Заголовок)")
            @RequestParam(value = "sortIndex", required = false) Integer sortIndex
    ) throws CharacterException {
        List<Character> characters = characterService.findAll(offset, limit, sortIndex, name, title, slug);
        if (characters == null || characters.size() == 0)
            throw new CharacterException("It seems the superheroes haven't been added yet. Wait for the developers to do this.");
        List<CharacterDto> response = new ArrayList<>();
        characters.forEach(x -> response.add(new CharacterDto(x)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{characterId}")
    @Operation(
            summary = "GET запрос на получение cупергероя по его slug(мнемонический идентефикатор)",
            description = "Позволяет получить супергероя по его мнемоническому идентефикатору"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Не возвращается"),
            @ApiResponse(code = 401, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 403, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 404, message = "В случае, если в базе нету супергероев или по переданному slug не найдены супергерой"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n" +
                                            "    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"Ooo... Character with slug орпорпорпорплорплnot found\"\n" +
                                            "}")
                    })
            )
    })
    public ResponseEntity<CharacterDto> findCharacterBySlug(
            @Parameter(description = "Мнемонический идендетификатор супергероя")
            @PathVariable("characterId") String slug)
            throws CharacterException {
        Character character = characterService.findCharacterById(slug);
        if (character == null)
            throw new CharacterException("Ooo... Character with slug " + slug + "not found");
        CharacterDto characterDto = new CharacterDto(character);
        return ResponseEntity.ok(characterDto);
    }

    @GetMapping("/{characterId}/comics")
    @Operation(
            summary = "GET запрос на получение комиксов cупер героя по его slug(мнемонический идентефикатор)",
            description = "Позволяет получить комиксов супер героя по его мнемоническому идентефикатору"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "В случае, если один из параметров не прошел валидацию", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"timestamp\": \"2021-12-26T20:00:17.005+0000\",\n" +
                                            "    \"status\": 400,\n" +
                                            "    \"error\": \"Bad Request\",\n" +
                                            "    \"message\": \"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \\\"цйу\\\"\",\n" +
                                            "    \"path\": \"/characters\"\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(code = 401, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 403, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 404, message = "В случае, если в базе нету комиксов или по переданным в запросе параметрам фильтрации не найдены комиксы или супергерой"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n" +
                                            "    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"This hero has no comics yet ... But they will be on sale soon, wait.\"\n" +
                                            "}")
                    })
            )
    })
    public ResponseEntity<List<ComicsDto>> findComicsOfCharacterBySlug(
            @Parameter(description = "Мнемонический идендетификатор супергероя")
            @PathVariable("characterId") String slug,
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "offset") Integer offset,
            @Parameter(description = "Лимит выводимых на страницу элементов")
            @RequestParam(value = "limit") Integer limit,
            @Parameter(description = "Параметр фильтрации (по имени)")
            @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Параметр фильтрации (по описанию)")
            @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "Параметр фильтрации (по авторам)")
            @RequestParam(value = "authors", required = false) String authors,
            @Parameter(description = "Параметр фильтрации (по художнику)")
            @RequestParam(value = "painter", required = false) String painter,
            @Parameter(description = "Индекс сортировки комиксов(1 - по имени ,2 - Мнеманический идентификатор, 3 - Описание, 4 - художник, 5- Авторы)")
            @RequestParam(value = "sortIndex", required = false) Integer sortIndex
    ) throws CharacterException {
        List<Comics> comicsCharacter = characterService.findComicsOfCharacterBySlug(offset, limit, sortIndex, slug,
                name, description, authors, painter);
        if (comicsCharacter == null || comicsCharacter.size() == 0)
            throw new CharacterException("This hero has no comics yet ... But they will be on sale soon, wait.");
        List<ComicsDto> response = new ArrayList<>();
        comicsCharacter.forEach(x -> response.add(new ComicsDto(x, 1)));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(
            value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "POST запрос на добавление супер героя",
            description = "Позволяет добавить супер героя в базу. В body передается CharacterDtoRequest, подробнее об body, которое нужно передать: читать в Models документации(класс CharacterDtoRequest)"
    )
    @ApiImplicitParam(name = "Authorization", value = "Токен авторизации, должен начинаться СТРОГО С Bearer_", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NDA1OTE3OTgsImV4cCI6MTY0MDU5NTM5OH0.Gci9qeQdFCrQVUHpNqE_yQGYmHlHUfALDSoneC2TI2I")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "В случае, если один из параметров не прошел валидацию", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"status\": \"400 BAD_REQUEST\",\n" +
                                            "    \"message\": \"Errors validation: Title should be between 1 and 1000 characters, Title should not be empty, Name should be between 1 and 90 characters, Name should not be empty, Check your                 fields for correct entry\"\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(code = 401, message = "Если пользователь не авторизован в системе", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"state: \": \"401\",\n" +
                                            "    \"message\": \"Full authentication is required to access this resource\"\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(code = 403, message = "Возвращается, если пользователь имеет роль ROLE_USER, для которого запрещены добавления в базу данных"),
            @ApiResponse(code = 404, message = "Не возвращается, не заложено бизнес-логикой")
    })
    public CharacterDto addCharacter(
            @Valid CharacterDtoRequest request,
            BindingResult validationErrors
    )
            throws CharacterAddException {
        if (validationErrors.hasErrors()) {
            StringBuilder errors = new StringBuilder("Errors validation: ");
            for (Object object : validationErrors.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    errors.append(fieldError.getDefaultMessage()).append(", ");
                }
            }
            errors.append("Check your fields for correct entry");
            throw new CharacterAddException(errors.toString());
        }
        return new CharacterDto(characterService.addCharacter(request));
    }

    @Operation(
            summary = "POST запрос на закрепление комикса за супергероем и наоборот :)",
            description = "Позволяет закрепить комикс за супергероем."
    )
    @ApiImplicitParam(name = "Authorization", value = "Токен авторизации, должен начинаться СТРОГО С Bearer_", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NDA1OTE3OTgsImV4cCI6MTY0MDU5NTM5OH0.Gci9qeQdFCrQVUHpNqE_yQGYmHlHUfALDSoneC2TI2I")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Не возвращается, не заложенно бизнес-логикой", response = ApiResponse.class),
            @ApiResponse(code = 401, message = "Если пользователь не авторизован в системе", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"state: \": \"401\",\n" +
                                            "    \"message\": \"Full authentication is required to access this resource\"\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(code = 403, message = "Не возвращается, не заложоно бизнес-логикой"),
            @ApiResponse(code = 404, message = "Если не найден персонаж по переданным в запросе параметрам", response =ApiResponse.class,
                        examples = @Example(value = {
                                @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"Ooo... Character with slug 5f1007-42- not found\"\n" +
                                            "}")
                        }))
    })
    @PostMapping("/add-comics/{characterSlug}")
    public CharacterDto addComicsOfCharacter(
            @Parameter(description = "Мнемонический идендетификатор супергероя")
            @PathVariable("characterSlug") String slug
            ,            @Parameter(description = "Мнемонический идендетификатор супергероя")
            @RequestParam("comicsSlugs") String comics
    ) throws CharacterException, ComicsException, ComicsAddException {
        Character character = characterService.addComicsCharacter(slug,comics);
        return new CharacterDto(character);
    }
}

