package kz.beeset.med.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.beeset.med.admin.model.Speciality;
import kz.beeset.med.admin.service.ISpecialityService;
import kz.beeset.med.admin.utils.CommonService;
import kz.beeset.med.admin.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/speciality")
@Api(tags = {"Speciality"}, description = "Специальность", authorizations = {@Authorization(value = "bearerAuth")})
public class SpecialityController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuidesController.class);

    @Autowired
    ISpecialityService specialityService;

    @ApiOperation(value = "Получить список specialities", tags = {"Speciality"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllSpecialities() {
        try {
            return builder(success(specialityService.getAllSpecialities()));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Получить speciality по ID", tags = {"Speciality"})
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getSpecialityById(@PathVariable(name = "id") String id) {
        try {
            return builder(success(specialityService.getSpecialityById(id)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Создать ресурс", tags = {"Speciality"})
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> setResourse(@Valid @RequestBody Speciality speciality) {
        try {
            return builder(success(specialityService.setSpeciality(speciality)));
        } catch (InternalException e) {
            LOGGER.error("ERROR - Error while saving setLifeAction: " + e.makeString());
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }
}
