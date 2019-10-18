package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.custom.DMPV2NotificationsCustom;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Notifications;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDMPV2NotificationsService {

    Page<DMPV2Notifications> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPV2Notifications> search(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPV2NotificationsCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPV2NotificationsCustom> searchCustomPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMPV2Notifications> readIterableByProfileId(String profileId) throws InternalException;
    List<DMPV2Notifications> readIterableByDoctorUserIdAndPatientUserId(String doctorUserId, String patientUserId) throws InternalException;
    List<DMPV2Notifications> readIterableByPatientUserId(String patientUserId) throws InternalException;
    DMPV2Notifications get(String id) throws InternalException;
    DMPV2Notifications create(DMPV2Notifications dmpv2Notification) throws InternalException;
    DMPV2Notifications update(DMPV2Notifications dmpv2Notification) throws InternalException;
    void delete(String id) throws InternalException;

}
