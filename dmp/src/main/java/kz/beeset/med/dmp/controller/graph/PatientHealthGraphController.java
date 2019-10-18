package kz.beeset.med.dmp.controller.graph;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.beeset.med.dmp.controller.DMPAdminController;
import kz.beeset.med.dmp.service.graph.IPatientHealthGraphService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dmp/patient/graph")
@Api(tags = {"GRAPH"}, description = "GRAPH", authorizations = {@Authorization(value = "bearerAuth")})
public class PatientHealthGraphController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientHealthGraphController.class);

    @Autowired
    private IPatientHealthGraphService patientHealthGraphService;

    @ApiOperation(value = "Получить GRAPH по ID пациента", tags = {"GRAPH"})
    @RequestMapping(value = "/{dmpPatientId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getPatientGraph(@PathVariable(name = "dmpPatientId") String dmpPatientId) {
        try {
            return builder(success(patientHealthGraphService.getGraphByDMPPatientId(dmpPatientId)));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
