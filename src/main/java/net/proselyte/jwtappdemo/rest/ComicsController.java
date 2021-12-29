package net.proselyte.jwtappdemo.rest;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import net.proselyte.jwtappdemo.dto.*;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsAddException;
import net.proselyte.jwtappdemo.exception.characterException.CharacterException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsException;
import net.proselyte.jwtappdemo.exception.imageException.ImageExceptionBadRequest;
import net.proselyte.jwtappdemo.model.Character;
import net.proselyte.jwtappdemo.model.Comics;
import net.proselyte.jwtappdemo.service.ComicsService;
import net.proselyte.jwtappdemo.service.ImageSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(
        value = "/v1/public/comics",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
    @Api(description = "Контроллер комиксов")
public class ComicsController {

    private final ComicsService comicsService;
    private final ImageSaveService storageImage;

    @Autowired
    public ComicsController(ComicsService comicsService, ImageSaveService storageImage) {
        this.comicsService = comicsService;
        this.storageImage = storageImage;
    }

    @GetMapping
    @Operation(
            summary = "GET запрос на получение всех комиксов",
            description = "Позволяет получить все комиксы, которые будут отфильтрованные по переданным параметрам в запросе"
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
            @ApiResponse(code = 404, message = "В случае, если в базе нету комиксов или по переданным в запросе параметрам фильтрации не найдены комиксы"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"No comics were found with the passed parameters ... But they will be on sale soon, wait.\"\n}")
                    })
            )
    })
    public ResponseEntity<List<ComicsDto>> findAllComics(
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "offset") Integer offset,
            @Parameter(description = "Лимит выводимых на страницу эллементов")
            @RequestParam(value = "limit") Integer limit,
            @Parameter(description = "Имя(параметр фильтрации)")
            @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Описание (параметр фильтрации)")
            @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "Авторы (параметр фильтрации)")
            @RequestParam(value = "authors", required = false) String authors,
            @Parameter(description = "Художник (параметр фильтрации)")
            @RequestParam(value = "painter", required = false) String painter,
            @Parameter(description = "Индекс сортировки комиксов(1 - по имени ,2 - Мнеманический идентификатор, 3 - Описание, 4 - художник, 5- Авторы)")
            @RequestParam(value = "sortIndex", required = false) Integer sortIndex
    ) throws ComicsException {
        List<Comics> comicsCharacter = comicsService.findAll(offset, limit, sortIndex, name, description, authors, painter);
        List<ComicsDto> response = new ArrayList<>();
        if (comicsCharacter == null)
            throw new ComicsException("No comics were found with the passed parameters ... But they will be on sale soon, wait.");
        comicsCharacter.forEach(x -> response.add(new ComicsDto(x)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{comicsId}")
    @Operation(
            summary = "GET запрос на получение комикса по его slug(мнемоническому идентификатору)",
            description = "Позволяет получить комикс, по его мнемоническому идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Не возвращается"),
            @ApiResponse(code = 401, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 403, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 404, message = "В случае, если в базе нету комиксов или по переданным в запросе параметрам фильтрации не найдены комиксы"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"Ooo...It seems that no such item has been found. Try to refine your filtering options\"\n}")
                    })
            )
    })
    public ResponseEntity<ComicsDto> getComicsById(@PathVariable("comicsId") String slug) throws ComicsException {
        return ResponseEntity.ok(new ComicsDto(comicsService.getCharacterBySlug(slug)));
    }

    @GetMapping("/{comicsId}/character")
    @Operation(
            summary = "GET запрос на получение всех супергероев одного комикса",
            description = "Позволяет получить всех супергероев одного комикса по его slug (мнемонический идендификатор)"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Не возвращается"),
            @ApiResponse(code = 401, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 403, message = "Не возвращается, так как все GET методы разрешены для неавторизованных пользователей"),
            @ApiResponse(code = 404, message = "В случае, если в базе нету комиксов или по переданным в запросе параметрам фильтрации не найдены комиксы"
                    , response = ApiResponse.class, examples = @Example(value =
                    {
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{\n    \"status\": \"404 NOT_FOUND\",\n" +
                                            "    \"message\": \"Ooo... Comics with slug йцуйцууй not found\"\n}")
                    })
            )
    })
    public List<CharacterDto> getCharacterOfComicsById(
            @Parameter(description = "Мнемонический идентификатор комикса")
            @PathVariable("comicsId") String slug,
            @Parameter(description = "Номер страницы")
            @RequestParam(value = "offset") Integer offset,
            @Parameter(description = "Лимит выводимых на страницу эллементов")
            @RequestParam(value = "limit") Integer limit,
            @Parameter(description = "Имя комикса")
            @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Заголовок комикса")
            @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "Мнемонический идентификатор супергероя комиксов")
            @RequestParam(value = "slug", required = false) String slugCharacter,
            @Parameter(description = "Индекс сортировки супергероев(1 - по имени ,2 - Мнеманический идентификатор, 3 - Заголовок)")
            @RequestParam(value = "sortIndex", required = false) Integer sortIndex
    ) throws ComicsException, CharacterException {
        List<Character> characters = comicsService.getCharacterOfComicsById(offset, limit, slug,
                                     name, title, sortIndex, slugCharacter);
        List<CharacterDto> characterResponse = new ArrayList<>();
        characters.forEach(x->characterResponse.add(new CharacterDto(x,1)));
        return characterResponse;
    }

    @RequestMapping(
            value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "POST запрос на добавление комикса",
            description = "Позволяет добавить комикс в базу данных. В body передается ComicsAddRequest, подробнее об body, которое нужно передать: читать в Models документации(класс ComicsAddRequest)"
    )
    @ApiImplicitParam(name = "Authorization", value = "Токен авторизации, должен начинаться СТРОГО С Bearer_", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NDA1OTE3OTgsImV4cCI6MTY0MDU5NTM5OH0.Gci9qeQdFCrQVUHpNqE_yQGYmHlHUfALDSoneC2TI2I")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "В случае, если один из параметров не прошел валидацию", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"status\": \"400 BAD_REQUEST\",\n" +
                                            "    \"message\": \"Errors validation: Title should not be empty, Authors should not be empty, Name should not be empty, Name should be between 1 and 90 characters, Painter should not be empty, Check your fields for correct entry\"\n" +
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
    public ComicsDto addComics(@Valid ComicsAddRequest comics, BindingResult validationErrors)
            throws ComicsAddException {
        if(validationErrors.hasErrors()){
            StringBuilder errors = new StringBuilder("Errors validation: ");
            for (Object object : validationErrors.getAllErrors()) {
                if(object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    errors.append(fieldError.getDefaultMessage()).append(", ");
                }
            }
            errors.append("Check your fields for correct entry");
            throw new ComicsAddException(errors.toString());
        }
        return new ComicsDto(comicsService.addComics(comics));
    }

    @PostMapping("/{slug}/img/save")
    @Consumes({javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA})
    @Operation(
            summary = "POST запрос на загрузку картинки по slug комикса",
            description = "Позволяет загрузить и организовать раздачу статики по пути: {имя домена сайта}/{путь загрузки файла}. Путь загрузки файла также возвращается в запросах GET по комиксам"
    )
    @ApiImplicitParam(name = "Authorization", value = "Токен авторизации, должен начинаться СТРОГО С Bearer_", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE2NDA1OTE3OTgsImV4cCI6MTY0MDU5NTM5OH0.Gci9qeQdFCrQVUHpNqE_yQGYmHlHUfALDSoneC2TI2I")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "В случае, если файл не был передан или передан не того формата(логика подрузумивает форматы загрузки только png и jpg)", response = ApiResponse.class,
                    examples = @Example(value =
                            {
                                    @ExampleProperty(mediaType = "application/json", value = "{\n" +
                                            "    \"status\": \"400 BAD_REQUEST\",\n" +
                                            "    \"message\": \"The file: тейбл букс.txt format transferred to the server is not supported\"\n" +
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
            @ApiResponse(code = 403, message = "Не возвращается, не заложено бизнес-логикой"),
            @ApiResponse(code = 404, message = "Не возвращается, не заложено бизнес-логикой")
    })
    public ImageResultSaveDto uploadFile(
            @Parameter(description = "Файл загрузки")
            @RequestParam MultipartFile file,
            @Parameter(description = "Мнемонический идендификатор комикса")
            @PathVariable("slug") String slug
    ) throws IOException, ImageExceptionBadRequest, ComicsException
    {
        if (storageImage.saveImage(file, slug))
            return new ImageResultSaveDto("200", "File saved successfully");
        throw new IOException("An error occurred while saving the file");//автоматически прокинится в 500
    }

}

