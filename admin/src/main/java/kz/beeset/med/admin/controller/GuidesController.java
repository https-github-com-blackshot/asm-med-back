package kz.beeset.med.admin.controller;

import io.swagger.annotations.*;
import kz.beeset.med.admin.model.Guide;
import kz.beeset.med.admin.model.Guide;
import kz.beeset.med.admin.service.IGuideService;
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
@RequestMapping("/guide")
@Api(tags = {"Guide"}, description = "Обучение", authorizations = {@Authorization(value = "bearerAuth")})
public class GuidesController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuidesController.class);

    @Autowired
    IGuideService guideService;

    @ApiOperation(value = "Получить список guides", tags = {"Guide"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllGuides() {
        try {
            return builder(success(guideService.getAllGuides()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить guide по ID", tags = {"Guide"})
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getGuideById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(guideService.getGuideById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Создать ресурс", tags = {"Guide"})
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setResourse(@Valid @RequestBody Guide guide) {
        try {
            return builder(success(guideService.setGuide(guide)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновление документа обучение", tags = {"Guide"}, consumes = "multipart/form-data",
            notes = "uploadGuideDocument() method()")
    @RequestMapping(value = "/document/{guideId}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadGuideDocument(@RequestParam("file") MultipartFile file,
                                                 @ApiParam(name = "guideId",
                                                           value = "Идентификатор обучение для получения изображения.")
                                                   @PathVariable String guideId,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException,
            InternalException {

        try {Guide guide = guideService.getGuideById(guideId);
            if (guide == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "guide not found"));
            }

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));
            }

            try {
                String mimeType = file.getContentType();
                String fileName = file.getName() + " - " + guide.getId();
                String fileId = guideService.saveDocument(file.getInputStream(), mimeType, fileName);
                LOGGER.debug("[uploadGuideDocument()] - fileId: " + fileId);
                guide.setFileId(fileId);

                guideService.setGuide(guide);
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

    @ApiOperation(value = "Получить документ Обучение", tags = {"Users"},
            notes = "Тело ответа содержит необработанные данные документ, " +
                    "представляющие изображение пользователя. Тип содержимого типа " +
                    "ответа соответствует типу mimeType, который был задан при создании изображения.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Обучение было найдено и" +
                    " имеет документ, которое возвращается в теле."),
            @ApiResponse(code = 404, message = "Указывает, что запрашиваемый Обучение " +
                    "не найден или у руководства нет изображения профиля. Описание состояния" +
                    " содержит дополнительную информацию об ошибке.")
    })

    @RequestMapping(value = "/document/{guideId}", method = RequestMethod.GET)
    public ResponseEntity<?> getGuideDocument(
            @ApiParam(name = "guideId", value = "Идентификатор  для получения документа.")
            @PathVariable String guideId,
            HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {

            Guide guide = guideService.getGuideById(guideId);
            if (guide == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            GridFsResource resource = guideService.downloadDocument(guide.getFileId());

            if (resource == null) {
                throw new CloudocObjectNotFoundException("Обучение с идентификатором '"
                        + guide.getId() + "' не имеет документ.", resource.getClass());
            }

            String contentType = resource.getContentType();


            HttpHeaders responseHeaders = new HttpHeaders();
            LOGGER.debug("[getGuideDocument()] file content type: " + contentType );
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

    @ApiOperation(value = "Обновление изображения обучение", tags = {"Guide"}, consumes = "multipart/form-data",
            notes = "uploadGuideAvatar() method()")
    @RequestMapping(value = "/avatar/{guideId}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadGuideAvatar(@RequestParam("file") MultipartFile file,
                                                 @ApiParam(name = "guideId",
                                                         value = "Идентификатор обучение для получения изображения.")
                                                 @PathVariable String guideId,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException,
            InternalException {

        try {Guide guide = guideService.getGuideById(guideId);
            if (guide == null) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "guide not found"));
            }

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));
            }

            try {
                String mimeType = file.getContentType();
                String fileName = file.getName() + " - " + guide.getId();
                String fileId = guideService.saveAvatar(file.getInputStream(), mimeType, fileName);
                LOGGER.debug("[uploadGuideAvatar()] - fileId: " + fileId);
                guide.setGuideAvaId(fileId);

                guideService.setGuide(guide);
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

    @ApiOperation(value = "Получить изображения Обучение", tags = {"Users"},
            notes = "Тело ответа содержит необработанные данные изображения, " +
                    "представляющие изображение пользователя. Тип содержимого типа " +
                    "ответа соответствует типу mimeType, который был задан при создании изображения.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Обучение было найдено и" +
                    " имеет документ, которое возвращается в теле."),
            @ApiResponse(code = 404, message = "Указывает, что запрашиваемый Обучение " +
                    "не найден или у Обучение нет изображения профиля. Описание состояния" +
                    " содержит дополнительную информацию об ошибке.")
    })

    @RequestMapping(value = "/avatar/{guideId}", method = RequestMethod.GET)
    public ResponseEntity<?> getGuideAvatar(
            @ApiParam(name = "guideId", value = "Идентификатор  для получения изображения.")
            @PathVariable String guideId,
            HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {

            Guide guide = guideService.getGuideById(guideId);
            if (guide == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            GridFsResource resource = guideService.downloadAvatar(guide.getGuideAvaId());

            if (resource == null) {
                throw new CloudocObjectNotFoundException("Обучение с идентификатором '"
                        + guide.getId() + "' не имеет изображения.", resource.getClass());
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
    @ApiOperation("Удаление обучения по id")
    ResponseEntity<?> delete(@ApiParam("ID обучения") @PathVariable(name = "id") String id) throws InternalException {
        try {
            guideService.deleteGuideById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}
