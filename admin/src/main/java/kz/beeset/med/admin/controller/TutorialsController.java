package kz.beeset.med.admin.controller;

import io.swagger.annotations.*;
import kz.beeset.med.admin.model.Tutorial;
import kz.beeset.med.admin.service.ITutorialService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.CloudocObjectNotFoundException;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/tutorial")
@Api(tags = {"Tutorial"}, description = "Tutorial", authorizations = {@Authorization(value = "bearerAuth")})
public class TutorialsController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuidesController.class);

    @Autowired
    ITutorialService tutorialService;

    @ApiOperation(value = "Получить список guides", tags = {"Tutorial"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllTutorials() {
        try {
            return builder(success(tutorialService.getAllTutorials()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить tutorial по ID", tags = {"Tutorial"})
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getTutorialById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(tutorialService.getTutorialById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать tutorial", tags = {"Tutorial"})
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setTutorial(@Valid @RequestBody Tutorial tutorial) {
        try {
            LOGGER.info("tutorial: "+tutorial);
            return builder(success(tutorialService.setTutorial(tutorial)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновление изображения руководства", tags = {"Tutorial"}, consumes = "multipart/form-data",
            notes = "uploadGuideDocument() method()")
    @RequestMapping(value = "/upload/{tutorialId}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadTutorialPicture(@RequestParam("file") MultipartFile file,
                                               @ApiParam(name = "tutorialId",
                                                       value = "Идентификатор руководства для получения изображения.")
                                               @PathVariable String tutorialId,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws IOException, InternalException {

        try {Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
            if (tutorial == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "tutorial not found"));
            }

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));
            }

            try {
                String mimeType = file.getContentType();
                String fileName = file.getName() + " - " + tutorial.getId();
                String pictureId = tutorialService.saveFile(file.getInputStream(), mimeType, fileName);
                LOGGER.debug("[uploadGuideDocument()] - pictureId: " + pictureId);
                tutorial.setTutorialAvaId(pictureId);

                tutorialService.setTutorial(tutorial);
            } catch (Exception e) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Ошибка при чтении загруженного файла: " + e.getMessage()));
            }

            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить изображение руководства", tags = {"Users"},
            notes = "Тело ответа содержит необработанные данные изображения, " +
                    "представляющие изображение пользователя. Тип содержимого типа " +
                    "ответа соответствует типу mimeType, который был задан при создании изображения.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что руководство было найдено и" +
                    " имеет изображение, которое возвращается в теле."),
            @ApiResponse(code = 404, message = "Указывает, что запрашиваемый руководство " +
                    "не найден или у руководства нет изображения профиля. Описание состояния" +
                    " содержит дополнительную информацию об ошибке.")
    })

    @RequestMapping(value = "/picture/{tutorialId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPicture(
            @ApiParam(name = "tutorialId", value = "Идентификатор руководства для получения изображения.")
            @PathVariable String tutorialId,
            HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {

            Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
            if (tutorial == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            GridFsResource resource = tutorialService.downloadFile(tutorial.getTutorialAvaId());

            if (resource == null) {
                throw new CloudocObjectNotFoundException("Руководство с идентификатором '"
                        + tutorial.getId() + "' не имеет изображения.", resource.getClass());
            }

            String contentType = resource.getContentType();


            HttpHeaders responseHeaders = new HttpHeaders();

            responseHeaders.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удаление руководства по id")
    ResponseEntity<?> delete(@ApiParam("ID руководства") @PathVariable(name = "id") String id) throws InternalException {
        try {
            tutorialService.deleteTutorialById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
