package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMPProtocol;
import kz.beeset.med.dmp.model.DMPRequest;
import kz.beeset.med.dmp.model.custom.DMPFileProtocol;
import kz.beeset.med.dmp.service.IDMPRequestService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.CloudocObjectNotFoundException;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("dmp/request")
@Api(tags = {"DMPRequest"}, description = "DMPRequest", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPRequestController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPRequestController.class);

    @Autowired
    private IDMPRequestService dmpRequestService;

    @ApiOperation(value = "Получить объект/список DMPRequest по параметрам", tags = {"DMPRequest"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "fio", dataType = "string", value = "Возвращает список объектов с данным ФИО", paramType = "query"),
            @ApiImplicitParam(name = "idn", dataType = "string", value = "Возвращает список объектов с данным ИИН", paramType = "query"),
            @ApiImplicitParam(name = "phone", dataType = "string", value = "Возвращает список объектов с данным номером телефона", paramType = "query"),
            @ApiImplicitParam(name = "email", dataType = "string", value = "Возвращает список объектов с данной почта", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "int", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPRequest существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/request/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPRequestPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpRequestService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPRequest", tags = {"DMPRequest"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "int", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/request/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPRequestPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpRequestService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить объект/список DMPRequest по параметрам", tags = {"DMPRequest"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "fio", dataType = "string", value = "Возвращает список объектов с данным ФИО", paramType = "query"),
            @ApiImplicitParam(name = "idn", dataType = "string", value = "Возвращает список объектов с данным ИИН", paramType = "query"),
            @ApiImplicitParam(name = "phone", dataType = "string", value = "Возвращает список объектов с данным номером телефона", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "int", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "email", dataType = "string", value = "Возвращает список объектов с данной почта", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPRequest существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/request/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPRequestCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpRequestService.readCustomPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список DMPRequest", tags = {"DMPRequest"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "int", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Возвращает список объектов с данным userId", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/request/custom/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPRequestCustomPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpRequestService.searchCustom(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Получить список всех DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "read/dmp/request/iterable", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPRequestIterable() {
        try {
            return builder(success(dmpRequestService.readIterable()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPRequest по заданному ID", tags = {"DMPRequest"})
    @RequestMapping(value = "read/dmp/request/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPRequest(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpRequestService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "create/dmp/request", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPRequest(@RequestBody @Valid DMPRequest dmpRequest) {
        try {
            return builder(success(dmpRequestService.create(dmpRequest)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "update/dmp/request", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPRequest(@RequestBody @Valid DMPRequest dmpRequest) {
        try {
            return builder(success(dmpRequestService.update(dmpRequest)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Принять объект DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "accept/dmp/request", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> acceptDMPRequest(@RequestBody @Valid DMPRequest dmpRequest) {
        try {
            return builder(success(dmpRequestService.accept(dmpRequest)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Отклонить объект DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "decline/dmp/request", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> declineDMPRequest(@RequestBody @Valid DMPRequest dmpRequest) {
        try {
            return builder(success(dmpRequestService.decline(dmpRequest)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Отозвать DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "recall/dmp/request/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> recallDMPRequest(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpRequestService.recall(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPRequest", tags = {"DMPRequest"})
    @RequestMapping(value = "delete/dmp/request/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPRequest(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpRequestService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Скачать файлы запроса", tags = {"DMPRequest"})
    @RequestMapping(value = "/file/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@ApiParam(name = "fileId", value = "Идентификатор  для получения документа.")
                                     @PathVariable String fileId, HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {

            GridFsResource resource = dmpRequestService.downloadDocument(fileId);

            if (resource == null) {
                throw new CloudocObjectNotFoundException("Файл с идентификатором '"
                        + fileId + " не имеет документ.", resource.getClass());
            }

            String contentType = resource.getContentType();


            HttpHeaders responseHeaders = new HttpHeaders();
            LOGGER.debug("[getGuideDocument()] file content type: " + contentType);
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

    @ApiOperation(value = "Обновление документа ", tags = {"DMPRequest"}, consumes = "multipart/form-data", notes = "uploadFiles() method()")
    @RequestMapping(value = "/files/{dmpRequestId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(@ApiParam(name = "dmpRequestId", value = "Идентификатор для получения док.")
                                         @RequestParam("file") List<MultipartFile> files, @PathVariable String dmpRequestId, HttpServletRequest request, HttpServletResponse response) throws IOException, InternalException {
        try {

            if (!(request instanceof MultipartHttpServletRequest)) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Требуется Multipart запрос."));
            }

            return builder(success(dmpRequestService.uploadFile(files, dmpRequestId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
