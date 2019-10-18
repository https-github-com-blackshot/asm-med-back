package kz.beeset.med.dmp.controller;

import io.swagger.annotations.*;
import kz.beeset.med.constructor.constant.SecurityConstants;
import kz.beeset.med.dmp.model.DMPPatientAppeal;
import kz.beeset.med.dmp.model.DMPPatientAppealComment;
import kz.beeset.med.dmp.service.IDMPPatientAppealService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dmp/appeal")
@Api(tags = {"APPEAL"}, description = "APPEAL", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPPatientAppealController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPPatientAppealController.class);

    @Autowired
    private IDMPPatientAppealService service;

    @RequestMapping(value = "/getAllByStatusAndDoctor/{dmpDoctorId}/{status}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllByStatusAndDoctor(@PathVariable(name = "dmpDoctorId") String dmpDoctorId,
                                                     @PathVariable(name = "status") String status) {
        try {
            return builder(success(service.getAllByStatusAndDMPDoctorId(status, dmpDoctorId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/getAllByStatusAndDmp/{dmpId}/{status}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllByStatusAndDMP(@PathVariable(name = "dmpId") String dmpId,
                                                  @PathVariable(name = "status") String status) {
        try {
            return builder(success(service.getAllByStatusAndDMPId(status, dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/getAllByStatusAndDoctorAndDMP/{dmpDoctorId}/{status}/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllByStatusAndDoctorAndDMP(@PathVariable(name = "dmpDcotorId") String dmpDcotorId,
                                                           @PathVariable(name = "status") String status,
                                                           @PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(service.getAllByStatusAndDMPDoctorAndDMP(status, dmpDcotorId, dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @RequestMapping(value = "/getAllByDoctor/{dmpDoctorId}/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllByDoctor(@PathVariable(name = "dmpDoctorId") String dmpDoctorId,
                                            @PathVariable(name = "dmpId") String dmpId,
                                            @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(service.getAllByDMPDoctor(dmpDoctorId, dmpId, allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @RequestMapping(value = "/get/custom-appeals/pageable/{dmpDoctorId}/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCustomAppealsPageable(@PathVariable(name = "dmpDoctorId") String dmpDoctorId,
                                            @PathVariable(name = "dmpId") String dmpId,
                                            @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(service.getAllCustomByDMPDoctor(dmpDoctorId, dmpId, allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Строка для поиска", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице.", paramType = "query")
    })
    @RequestMapping(value = "search/custom-appeals/pageable/{dmpDoctorId}/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> searchCustomAppealsPageable(@PathVariable(name = "dmpDoctorId") String dmpDoctorId,
                                                      @PathVariable(name = "dmpId") String dmpId,
                                                      @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        try {
            return builder(success(service.searchCustomByDMPDoctor(dmpDoctorId, dmpId, allRequestParams)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/getAllByStatusAndProject/{status}/{dmpId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllByStatusAndProject(@PathVariable(name = "status") String status,
                                                      @PathVariable(name = "dmpId") String dmpId) {
        try {
            return builder(success(service.getAllByStatusAndDMPId(status, dmpId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(service.getById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> create(@ApiParam(name = "Обьект JSON") @RequestBody @Valid DMPPatientAppeal patientAppeal,
                                    BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
        try {
            HttpHeaders headers = new HttpHeaders();

            if (patientAppeal == null) {
                throw new IllegalArgumentException("Нельзя создавать patientAppeal null");
            }


            return builder(success(service.create(patientAppeal)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@ApiParam(name = "Обьект JSON") @RequestBody @Valid DMPPatientAppeal patientAppeal,
                                    BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
        try {
            HttpHeaders headers = new HttpHeaders();

            if (patientAppeal == null) {
                throw new IllegalArgumentException("Нельзя обновлять patientAppeal null");
            }

            return builder(success(service.update(patientAppeal)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

//    @RequestMapping(value = "/comment/list/{id}", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity<?> getPatientAppealCommentAttachmentInfo(@PathVariable(name = "id") String id) {
//        try {
//            return builder(success(service.getDMPPatientAppealCommentAttachmentInfo(id)));
//        } catch (InternalException e) {
//            LOGGER.error(e.getMessage(), e);
//            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
//        }
//    }

    @ApiOperation(value = "Создать комментарии для обращение")
    @RequestMapping(value = "/comment/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPatientAppealComment(@ApiParam(name = "Обьект JSON") @RequestBody @Valid DMPPatientAppealComment patientAppealComment) {
        try {
            return builder(success(service.createDMPatientAppealComment(patientAppealComment)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

//    @ApiOperation(value = "Загрузить изображение для обращения пациента", consumes = "multipart/form-data")
//    @PostMapping(value = "/comment/attachment/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadAppealAttachment(@RequestParam("file") MultipartFile file,
//                                                    HttpServletRequest request) {
//        try {
//            System.out.println("PatientAppealController.uploadAppealAttachment");
//            if (request instanceof MultipartHttpServletRequest == false) {
//
//                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Требуется Multipart запрос."));
//            }
//
//            return builder(success(service.saveAttachment(file.getInputStream(), file.getContentType(), file.getOriginalFilename())));
//        } catch (InternalException e) {
//            LOGGER.error(e.getMessage(), e);
//            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
//        } catch (IOException e) {
//            LOGGER.error(e.getMessage(), e);
//            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Ошибка при чтении загруженного файла: " + e.getMessage()));
//        }
//    }

//    @RequestMapping(value = "/comment/attachment/{id}/info", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity<?> getPatientAppealCommentAttachmentList(@PathVariable(name = "id") String commentId) {
//        try {
//            return builder(success(service.getDMPPatientAppealCommentAttachmentList(commentId)));
//        } catch (InternalException e) {
//            LOGGER.error(e.getMessage(), e);
//            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
//        }
//    }

//    @RequestMapping(value = "/comment/attachment/{id}", method = RequestMethod.GET)
//    public ResponseEntity<?> getCommentAttachment(
//            @ApiParam(name = "id", value = "Идентификатор файла.")
//            @PathVariable String id,
//            HttpServletRequest request, HttpServletResponse response) throws InternalException {
//        try {
//
//            GridFsResource resource = service.downloadFile(id);
//
//            if (resource == null) {
//                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден."));
//            }
//
//            String contentType = resource.getContentType();
//
//            HttpHeaders responseHeaders = new HttpHeaders();
//
//            responseHeaders.set("Content-Type", contentType);
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, e.getMessage()));
//        }
//    }

    @ApiOperation(value = "Обновить статус обращения пациента")
    @PutMapping("/status/{appealId}/{statusId}")
    public ResponseEntity<?> updatePatientAppealStatus(@PathVariable("appealId") String appealId,
                                                       @PathVariable("statusId") String statusId,
                                                       HttpServletRequest request) {
        try {

            return builder(success(service.updateStatus(appealId,statusId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/file/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@ApiParam(name = "fileId", value = "Идентификатор  для получения документа")
                                          @PathVariable String fileId) {

        try {
            GridFsResource resource = service.downloadDocument(fileId);

            if (resource == null) {
                throw new CloudocObjectNotFoundException("Файл с идентификатором '"
                        + fileId + " не найден", resource.getClass());
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

    @ApiOperation(value = "Загрузка документа", tags = {"APPEAL"}, consumes = "multipart/form-data", notes = "uploadFiles() method()")
    @RequestMapping(value = "/upload/files/{dmpPatientAppealIds}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPatientAppealFiles(@RequestParam("file") List<MultipartFile> files,
                                         @ApiParam(name = "dmpPatientAppealId", value = "Идентификатор обращения к которому присваиваются загружаемый файлы")
                                         @PathVariable List<String> dmpPatientAppealIds,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {

        try {

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR,
                        "Требуется Multipart запрос."));

            }

            service.uploadPatientAppealFiles(files,  dmpPatientAppealIds);

            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Загрузка документа", tags = {"APPEAL"}, consumes = "multipart/form-data", notes = "uploadFiles() method()")
    @RequestMapping(value = "/upload/comment/files/{dmpPatientAppealCommentId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPatientAppealFiles(@RequestParam("file") List<MultipartFile> files,
                                                      @ApiParam(name = "dmpPatientAppealCommentId", value = "Идентификатор обращения к которому присваиваются загружаемый файлы")
                                                      @PathVariable String dmpPatientAppealCommentId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws IOException {

        try {

            if (request instanceof MultipartHttpServletRequest == false) {
                return builder(errorWithDescription(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Требуется Multipart запрос."));
            }

            service.uploadPatientAppealCommentFiles(files,  dmpPatientAppealCommentId);

            return builder(success("OK"));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Загрузка документа", tags = {"APPEAL"})
    @RequestMapping(value = "/read/status/list", method = RequestMethod.GET)
    public ResponseEntity<?> readStatusList() throws IOException {
        try {
            return builder(success(service.getStatusList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Загрузка документа", tags = {"APPEAL"})
    @RequestMapping(value = "/read/eventType/list", method = RequestMethod.GET)
    public ResponseEntity<?> readEventTypeList() throws IOException {
        try {
            return builder(success(service.getEventTypeList()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

    @ApiOperation(value = "Загрузка документа", tags = {"APPEAL"})
    @RequestMapping(value = "/read/comment/list/{dmpPatientAppealId}", method = RequestMethod.GET)
    public ResponseEntity<?> readCommentList(@PathVariable String dmpPatientAppealId) throws IOException {
        try {
            return builder(success(service.getDMPPatientAppealCommentsByAppealId(dmpPatientAppealId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }

    }

}
