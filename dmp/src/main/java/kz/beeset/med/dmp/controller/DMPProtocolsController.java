package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.dmp.model.DMPProtocol;
import kz.beeset.med.dmp.service.IDMPProtocolService;
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
@RequestMapping("dmp/protocols")
@Api(tags = {"DMPProtocols"}, description = "DMPProtocols", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPProtocolsController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPProtocolsController.class);

    @Autowired
    private IDMPProtocolService dmpProtocolService;

    @ApiOperation(value = "Получить объект/список DMPProtocols по параметрам", tags = {"DMPProtocols"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Возвращает объект с данным id", paramType = "query"),
            @ApiImplicitParam(name = "dmpId", dataType = "string", value = "Возвращает список объектов с данным ID пуза", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Возвращает список объектов с данным названием", paramType = "query"),
            @ApiImplicitParam(name = "description", dataType = "string", value = "Возвращает список объектов с данным описанием", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "code", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что объект или список объектов DMPProtocols существуют и возвращает их.")
    })
    @RequestMapping(value = "/read/dmp/protocol/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDMPProtocolPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpProtocolService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список DMPProtocols", tags = {"DMPProtocols"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что пользователи существуют и возвращает.")
    })
    @RequestMapping(value = "/search/dmp/protocol/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> searchDMPProtocolPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(dmpProtocolService.search(allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить список всех DMPProtocols", tags = {"DMPProtocols"})
    @RequestMapping(value = "read/dmp/protocol/iterable/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPProtocolIterable(@PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(dmpProtocolService.readIterableByDMPId(dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPProtocols по заданному ID", tags = {"DMPProtocols"})
    @RequestMapping(value = "read/dmp/protocol/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPProtocol(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpProtocolService.get(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить DMPProtocols по заданному List<String> dmpIds", tags = {"DMPProtocols"})
    @RequestMapping(value = "read/dmp/protocol/byUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPProtocolsByDmpIds(@PathVariable(name = "userId") String userId) {
        try {
            return builder(success(dmpProtocolService.getAllByUserId(userId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать объект DMPProtocols", tags = {"DMPProtocols"})
    @RequestMapping(value = "create/dmp/protocol", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createDMPProtocol(@RequestBody @Valid DMPProtocol dmpProtocol) {
        try {
            return builder(success(dmpProtocolService.create(dmpProtocol)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }


    @ApiOperation(value = "Обновить объект DMPProtocols", tags = {"DMPProtocols"})
    @RequestMapping(value = "update/dmp/protocol", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateDMPProtocol(@RequestBody @Valid DMPProtocol dmpProtocol) {
        try {
            return builder(success(dmpProtocolService.update(dmpProtocol)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Обновить объект DMPProtocols", tags = {"DMPProtocols"})
    @RequestMapping(value = "delete/dmp/protocol/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteDMPProtocol(@PathVariable(name = "id") String id) {
        try {
            return builder(success(dmpProtocolService.delete(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/file/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getFile(
            @ApiParam(name = "fileId", value = "Идентификатор  для получения документа.")
            @PathVariable String fileId,
            HttpServletRequest request, HttpServletResponse response) throws InternalException {
        try {

            GridFsResource resource = dmpProtocolService.downloadDocument(fileId);

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

    @ApiOperation(value = "Обновление документа ", tags = {"DMPFileProtocol"}, consumes = "multipart/form-data",
            notes = "uploadFiles() method()")
    @RequestMapping(value = "/files/{dmpProtocolId}", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(@RequestParam("file") List<MultipartFile> files,
                                         @ApiParam(name = "dmpProtocolId",
                                                 value = "Идентификатор для получения док.")
                                         @PathVariable String dmpProtocolId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException,
            InternalException {

        try {

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));

            }

            DMPProtocol dmpProtocol = dmpProtocolService.uploadFile(files, dmpProtocolId);
            return builder(success(dmpProtocolService.create(dmpProtocol)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
