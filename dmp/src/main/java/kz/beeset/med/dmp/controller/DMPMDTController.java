package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.DMPMDTEventComment;
import kz.beeset.med.dmp.model.DMPMDTTemplate;
import kz.beeset.med.dmp.model.DMPVisit;
import kz.beeset.med.dmp.service.IDMPMDTEventService;
import kz.beeset.med.dmp.service.IDMPMDTTemplateService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.CloudocObjectNotFoundException;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dmp/mdt")
@Api(tags = {"DMPMDT"}, description = "DMPMDT", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPMDTController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPMDTController.class);

    @Autowired
    private IDMPMDTTemplateService dmpmdtTemplateService;
    @Autowired
    private IDMPMDTEventService dmpmdtEventService;

    @ApiOperation(value = "Получить объект/список DMPMDTTemplate по параметрам", tags = {"DMPMDT"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "ownerId", dataType = "string", value = "Возвращает список объектов с данным ID инициатора", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPMDTTemplate существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/mdtTemplate/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPMDTTemplatePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpmdtTemplateService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPMDTTemplate", tags = {"DMPMDT"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/mdtTemplate/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPMDTTemplatePageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpmdtTemplateService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMDTTemplate", tags = {"DMPMDT"})
    @RequestMapping(value = "read/dmp/mdtTemplateiterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTTemplateIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpmdtTemplateService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPMDTTemplate по заданному ID", tags = {"DMPMDT"})
    @RequestMapping(value = "read/dmp/mdtTemplate/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTTemplate(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpmdtTemplateService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPMDTTemplate по заданному userID", tags = {"DMPMDT"})
    @RequestMapping(value = "/read/dmp/mdtTemplates/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTTemplateByUserId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpmdtTemplateService.getByUserId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPMDTTemplate", tags = {"DMPMDT"})
    @RequestMapping(value = "create/dmp/mdtTemplate", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPMDTTemplate(@RequestBody @Valid DMPMDTTemplate dmpmdtTemplate) {
        try {
            return builder(success(dmpmdtTemplateService.create(dmpmdtTemplate)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPMDTTemplate", tags = {"DMPMDT"})
    @RequestMapping(value = "update/dmp/mdtTemplate", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPMDTTemplate(@RequestBody @Valid DMPMDTTemplate dmpmdtTemplate) {
        try {
            return builder(success(dmpmdtTemplateService.update(dmpmdtTemplate)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPMDTTemplate", tags = {"DMPMDT"})
    @RequestMapping(value = "delete/dmp/mdtTemplate/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPMDTTemplate(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpmdtTemplateService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPMDTEvent по параметрам", tags = {"DMPMDT"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "teamId", dataType = "string", value = "Возвращает список объектов с данным ID команды", paramType = "query"),
            @ApiImplicitParam(name = "ownerId", dataType = "string", value = "Возвращает список объектов с данным ID инициатора", paramType = "query"),
            @ApiImplicitParam(name = "dmpVisitId", dataType = "string", value = "Возвращает список объектов с данным ID визита", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "userId in team с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPMDTEvent существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/mdtEvent/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPMDTEventPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpmdtEventService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPMDTEventCustom по параметрам", tags = {"DMPMDT"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "teamId", dataType = "string", value = "Возвращает список объектов с данным ID команды", paramType = "query"),
            @ApiImplicitParam(name = "ownerId", dataType = "string", value = "Возвращает список объектов с данным ID инициатора", paramType = "query"),
            @ApiImplicitParam(name = "dmpVisitId", dataType = "string", value = "Возвращает список объектов с данным ID визита", paramType = "query"),
            @ApiImplicitParam(name = "dmpPatientId", dataType = "string", value = "Возвращает список объектов с данным ID пациента", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "userId in team с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPMDTEventCustom существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/mdtEvent/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPMDTEventCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpmdtEventService.readCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPMDTEvent", tags = {"DMPMDT"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "userId in team с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/mdtEvent/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPMDTEventPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpmdtEventService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMDTEvent", tags = {"DMPMDT"})
    @RequestMapping(value = "/read/dmp/mdtEvent/one/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTEventIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpmdtTemplateService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPMDTEvent по заданному ID", tags = {"DMPMDT"})
    @RequestMapping(value = "/read/dmp/mdtEvent/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTEvent(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpmdtEventService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPMDTEvent по заданному userID", tags = {"DMPMDT"})
    @RequestMapping(value = "/read/dmp/mdtEvents/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTEventByUserId(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpmdtEventService.getByUserId(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPMDTEvent", tags = {"DMPMDT"})
    @RequestMapping(value = "/create/dmp/mdtEvent", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPMDTEvent(@RequestBody @Valid DMPMDTEvent dmpmdtEvent) {
        try {
            return builder(success(dmpmdtEventService.create(dmpmdtEvent)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPMDTEvent", tags = {"DMPMDT"})
    @RequestMapping(value = "/update/dmp/mdtEvent", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPMDTEvent(@RequestBody @Valid DMPMDTEvent dmpmdtEvent) {
        try {
            return builder(success(dmpmdtEventService.update(dmpmdtEvent)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Accept DMPMDTEvent by doctor", tags = {"DMPMDT"})
    @RequestMapping(value = "/update/accept/dmp/mdtEvent/{eventId}/{userId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateAcceptDMPMDTEvent(@PathVariable(value = "eventId") String eventId,
                                               @PathVariable(value = "userId") String userId) {
        try {
            return builder(success(dmpmdtEventService.updateEventAcceptByDoctor(eventId,userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Decline DMPMDTEvent by doctor", tags = {"DMPMDT"})
    @RequestMapping(value = "/update/decline/dmp/mdtEvent/{eventId}/{userId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDeclineDMPMDTEvent(@PathVariable(value = "eventId") String eventId,
                                               @PathVariable(value = "userId") String userId) {
        try {
            return builder(success(dmpmdtEventService.updateEventDeclineByDoctor(eventId,userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPMDTEvent", tags = {"DMPMDT"})
    @RequestMapping(value = "/delete/dmp/mdtEvent/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPMDTEvent(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpmdtEventService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Add doc", tags = {"DMPMDT"}, consumes = "multipart/form-data")
    @RequestMapping(value = "/upload/dmp/file", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, InternalException {

        try {

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));
            }

            String mimeType = file.getContentType();
            String fileName = file.getName();
            String fileId = dmpmdtEventService.saveFile(file.getInputStream(), mimeType, fileName);
            LOGGER.debug("[uploadFile()] - file name : " + fileName);
            LOGGER.debug("[uploadFile()] - file id: " + fileId);

            return builder(success(fileId));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Remove doc by id", tags = {"DMPMDT"})
    @RequestMapping(value = "/delete/dmp/file/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPMDTEventFile(@PathVariable(name = "id") String id) {
        try {
            dmpmdtEventService.deleteDocById(id);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Remove docs by id list", tags = {"DMPMDT"})
    @RequestMapping(value = "/delete/dmp/list/file", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> deleteDMPMDTEventFileList(@RequestBody @Valid List<String> idList) {
        try {
            dmpmdtEventService.deleteFiles(idList);
            return builder(success("Success"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value="Get document by id",tags = {"DMPMDT"})
    @RequestMapping(value = "/read/dmp/file/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPicture(
            @ApiParam(name = "fileId", value = "Идентификатор fileId для получения file.")
            @PathVariable String fileId,
            HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {
            GridFsResource resource = dmpmdtEventService.downloadFile(fileId);

            if (resource == null) {
                throw new CloudocObjectNotFoundException("File с идентификатором '"
                        + fileId + "' not found.", resource.getClass());
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

    @ApiOperation(value = "Создать объект DMPMDTEventComment", tags = {"DMPMDT"})
    @RequestMapping(value = "/create/dmp/mdtEventComment", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPMDTTemplate(@RequestBody @Valid DMPMDTEventComment eventComment) {
        try {
            return builder(success(dmpmdtEventService.createComment(eventComment)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPMDTEventComment по event id", tags = {"DMPMDT"})
    @RequestMapping(value = "/read/dmp/mdtEventComment/list/{eventId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPMDTEventCommentListByUploadId(@PathVariable(name = "eventId") String eventId) {
        try {
            return builder(success(dmpmdtEventService.getCommentListByEventId(eventId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPMDTEvent adding commentId", tags = {"DMPMDT"})
    @RequestMapping(value = "/update/dmp/mdtEvent/addComment/{eventId}/{uploadCode}/{commentId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPMDTEventAddingComment(@PathVariable(name = "eventId") String eventId,
                                                  @PathVariable(name = "uploadCode") String uploadCode,
                                                  @PathVariable(name = "commentId") String commentId) {
        try {
            return builder(success(dmpmdtEventService.updateEventAddingCommentId(eventId,uploadCode,commentId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}
