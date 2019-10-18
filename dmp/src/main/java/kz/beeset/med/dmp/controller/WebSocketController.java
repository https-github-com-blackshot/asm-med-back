package kz.beeset.med.dmp.controller;

import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.service.IDMPPatientService;
import kz.beeset.med.dmp.utils.CommonService;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController extends CommonService {

    @Autowired
    private IDMPPatientService dmpPatientService;

    @MessageMapping("/update/dmp/deviceStat")
    @SendTo("/dmp/patients/read/dmp/patient/pageable")
    public ResponseEntity<?> getPatients(Map<String, String> allRequestParams) throws Exception {
        try {
            return builder(success(dmpPatientService.readPageable(allRequestParams)));
        } catch (InternalException e) {
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
