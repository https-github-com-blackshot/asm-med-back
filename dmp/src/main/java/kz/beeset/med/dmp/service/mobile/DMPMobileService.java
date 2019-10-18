package kz.beeset.med.dmp.service.mobile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.firebase.FCMClientService;
import kz.beeset.med.dmp.firebase.PushNotificationModel;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.common.User;
import kz.beeset.med.dmp.model.custom.DMPDoctorCustom;
import kz.beeset.med.dmp.model.custom.DMPInvitationCustom;
import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.repository.*;
import kz.beeset.med.dmp.service.*;
import kz.beeset.med.dmp.service.feign.IAdminService;
import kz.beeset.med.dmp.service.impl.DMPDoctorService;
import kz.beeset.med.dmp.utils.IOUtils;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DMPMobileService implements IDMPMobileService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPMobileService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPInvitationRepository dmpInvitationRepository;
    @Autowired
    private IDMPInvitationService dmpInvitationService;
    @Autowired
    private IDMPDoctorService dmpDoctorService;
    @Autowired
    private IDMPPatientService dmpPatientService;
    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;
    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;
    @Autowired
    private DMPDeviceStatRepository dmpDeviceStatRepository;
    @Autowired
    private DMPDeviceStatConfigRepository dmpDeviceStatConfigRepository;
    @Autowired
    private DMPNotificationRepository dmpNotificationRepository;
    @Autowired
    private IDMPDeviceStatService dmpDeviceStatService;
    @Autowired
    private IDMPService dmpService;
    @Autowired
    private FCMClientService fcmClientService;
    @Autowired
    private IAdminService adminService;


    @Override
    public byte[] getUserRightsDocument() throws InternalException {
        try {
            InputStream in = getClass().getResourceAsStream("/static/user_rights.pdf");

            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public byte[] getUserAgreementDocument() throws InternalException {
        try {
            InputStream in = getClass().getResourceAsStream("/static/user_agreement.pdf");

            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public Page<DMPPatientCustom> readDMPPatientPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            return dmpPatientService.readPageable(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPPatient getDMPPatientById(String dmpPatientId) throws InternalException {
        try {
            return dmpPatientService.get(dmpPatientId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public Page<DMP> readDMPPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            return dmpService.read(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public List<DMP> readDMPByUserId(String userId) throws InternalException {
        try {
            List<DMPPatient> dmpPatientList = dmpPatientRepository.getAllByUserId(userId);
            List<String> dmpIds = dmpPatientList.stream().map(DMPPatient::getDmpId).collect(Collectors.toList());
            return dmpRepository.findAllByIdInAndState(dmpIds, DMPConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMP> readByUserId(String userId)" +
                    "-", e);
        }
    }

    @Override
    public DMPPatient registerPatientToDMP(DMPPatient dmpPatient) throws InternalException {
        try {
            dmpPatient.setCreatedBy("");
            dmpPatient.setCreatedDate(LocalDateTime.now());
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);

            dmpPatient.setHealthStatus(6);

            return dmpPatientRepository.save(dmpPatient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPPatient create(DMPPatient dmpPatient)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPDoctorCustom> readDMPDoctorCustomPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            return dmpDoctorService.readCustomPageable(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPDoctor> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public void refuseDoctorParticipation(String dmpPatientId, String dmpDoctorId) throws InternalException {
        try {
            DMPPatient dmpPatient = dmpPatientRepository.findById(dmpPatientId).get();
            List<String> newDoctorIdsList = new ArrayList<>();

            dmpPatient.getDmpDoctorIds().forEach(doctorId -> {
                if (!dmpDoctorId.equals(dmpDoctorId))
                    newDoctorIdsList.add(doctorId);
            });

            dmpPatient.setDmpDoctorIds(newDoctorIdsList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPDoctorCustom getDMPDoctorInfo(String userId, String dmpId) throws InternalException {
        try {
            DMPDoctorCustom dmpDoctorCustom = new DMPDoctorCustom();

            LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(userId).getBody();
            Gson gson = new Gson();
            JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
            User user = gson.fromJson(data, User.class);

            dmpDoctorCustom.setUser(user);

            DMPDoctor dmpDoctor = dmpDoctorRepository.findByDmpIdAndUserIdAndState(dmpId, userId, DMPDoctorConstants.STATUS_ACTIVE);

            dmpDoctorCustom.setDmpDoctor(dmpDoctor);

            return dmpDoctorCustom;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public Page<DMPInvitation> readDMPInvitationPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            return dmpInvitationService.readPageable(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public Page<DMPInvitation> searchDMPInvitation(Map<String, String> allRequestParams) throws InternalException {
        try {
            return dmpInvitationService.search(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public Page<DMPInvitationCustom> readDMPInvitationCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException {
        try {
            return dmpInvitationService.readCustomPageable(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public Page<DMPInvitationCustom> searchDMPInvitationCustomPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            return dmpInvitationService.searchCustomPageable(allRequestParams);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public List<DMPInvitation> readDMPInvitationIterableByDMPId(String dmpId) throws InternalException {
        try {
            return dmpInvitationService.readIterableByDMPId(dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPInvitation getDMPInvitation(String id) throws InternalException {
        try {
            return dmpInvitationService.get(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPInvitation createDMPInvitation(DMPInvitation dmpInvitation) throws InternalException {
        try {
            return dmpInvitationService.create(dmpInvitation);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPInvitation acceptDMPInvitation(String dmpInvitationId) throws InternalException {
        try {
            DMPInvitation dmpInvitation = dmpInvitationRepository.getById(dmpInvitationId);

            DMPPatient dmpPatient = dmpPatientRepository.findByUserIdAndDmpIdAndState(dmpInvitation.getPatientUserId(), dmpInvitation.getDmpId(), DMPPatientConstants.STATUS_ACTIVE);

            if (dmpPatient == null) {
                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) adminService.getUser(dmpInvitation.getPatientUserId()).getBody();
                Gson gson = new Gson();
                JsonObject data = gson.toJsonTree(body.get("data")).getAsJsonObject();
                User user = gson.fromJson(data, User.class);

                dmpPatient = new DMPPatient();

                dmpPatient.setUserId(user.getId());
                dmpPatient.setDmpId(dmpInvitation.getDmpId());
                dmpPatient.setCodeNumber(user.getIdn());

                List<String> dmpDoctorIds = new ArrayList<>();
                dmpDoctorIds.add(dmpInvitation.getDmpDoctorId());
                dmpPatient.setDmpDoctorIds(dmpDoctorIds);

                dmpPatient.setCreatedBy("");
                dmpPatient.setCreatedDate(LocalDateTime.now());
                dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);

                dmpPatientRepository.save(dmpPatient);
            } else {
                List<String> dmpDoctorIds = dmpPatient.getDmpDoctorIds();
                dmpDoctorIds.add(dmpInvitation.getDmpDoctorId());

                dmpPatient.setLastModifiedBy("");
                dmpPatient.setLastModifiedDate(LocalDateTime.now());
                dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);

                dmpPatientRepository.save(dmpPatient);
            }


            dmpInvitation.setLastModifiedBy("");
            dmpInvitation.setLastModifiedDate(LocalDateTime.now());
            dmpInvitation.setInvitationStatus(DMPInvitationConstants.STATUS_ACCEPTED);
            dmpInvitation.setState(DMPInvitationConstants.STATUS_ACTIVE);

            return dmpInvitationRepository.save(dmpInvitation);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPInvitation rejectDMPInvitation(String dmpInvitationId) throws InternalException {
        try {
            DMPInvitation dmpInvitation = dmpInvitationRepository.getById(dmpInvitationId);

            dmpInvitation.setLastModifiedBy("");
            dmpInvitation.setLastModifiedDate(LocalDateTime.now());
            dmpInvitation.setInvitationStatus(DMPInvitationConstants.STATUS_REJECTED);
            dmpInvitation.setState(DMPInvitationConstants.STATUS_ACTIVE);

            return dmpInvitationRepository.save(dmpInvitation);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPInvitation recallDMPInvitation(String dmpInvitationId) throws InternalException {
        try {
            DMPInvitation dmpInvitation = dmpInvitationRepository.getById(dmpInvitationId);

            dmpInvitation.setLastModifiedBy("");
            dmpInvitation.setLastModifiedDate(LocalDateTime.now());
            dmpInvitation.setInvitationStatus(DMPInvitationConstants.STATUS_RECALLED);
            dmpInvitation.setState(DMPInvitationConstants.STATUS_ACTIVE);

            return dmpInvitationRepository.save(dmpInvitation);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPDeviceStat registerDMPPatientDevice(DMPDeviceStat dmpDeviceStat) throws InternalException {
        try {

            dmpDeviceStat.setState(DMPDeviceStatConstants.STATUS_ACTIVE);
            dmpDeviceStat.setCreatedDate(LocalDateTime.now());

            dmpDeviceStat = dmpDeviceStatRepository.save(dmpDeviceStat);

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStat.getDmpPatientId());

            dmpPatient.setHealthStatus(dmpDeviceStatService.defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatient.setLastModifiedDate(LocalDateTime.now());

            dmpPatientRepository.save(dmpPatient);

            return dmpDeviceStat;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPDeviceStat updateDeviceStat(DMPDeviceStat dmpDeviceStat) throws InternalException {
        try {

            dmpDeviceStat.setState(DMPDeviceStatConstants.STATUS_ACTIVE);
            dmpDeviceStat.setLastModifiedDate(LocalDateTime.now());

            dmpDeviceStat = dmpDeviceStatRepository.save(dmpDeviceStat);

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStat.getDmpPatientId());

            dmpPatient.setHealthStatus(dmpDeviceStatService.defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatient.setLastModifiedDate(LocalDateTime.now());

            dmpPatientRepository.save(dmpPatient);

            return dmpDeviceStat;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPDeviceStatConfig configureDevice(DMPDeviceStatConfig dmpDeviceStatConfig) throws InternalException {
        try {

            dmpDeviceStatConfig.setState(DMPDeviceStatConfigConstants.STATUS_ACTIVE);
            dmpDeviceStatConfig.setCreatedDate(LocalDateTime.now());

            dmpDeviceStatConfig = dmpDeviceStatConfigRepository.save(dmpDeviceStatConfig);

            DMPDeviceStat dmpDeviceStat = dmpDeviceStatService.getLastRecordByDMPPatientId(dmpDeviceStatConfig.getDmpPatientId());

            DMPPatient dmpPatient = dmpPatientRepository.getById(dmpDeviceStatConfig.getDmpPatientId());
            dmpPatient.setHealthStatus(dmpDeviceStatService.defineHealthStatus(dmpDeviceStat));
            dmpPatient.setState(DMPPatientConstants.STATUS_ACTIVE);
            dmpPatient.setLastModifiedDate(LocalDateTime.now());
            dmpPatientRepository.save(dmpPatient);

            return dmpDeviceStatConfig;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public DMPDeviceStatConfig getDeviceConfiguration(String dmpPatientId) throws InternalException {
        try {
            return dmpDeviceStatConfigRepository.getByDMPPatientId(dmpPatientId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public String sendNotificationToOneUser(PushNotificationModel model, String clientToken) throws InternalException {
        try {
            return fcmClientService.sendPersonal(model, clientToken);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public String sendNotificationToGroupByTopic(PushNotificationModel model, String dmpId) throws InternalException {
        try {
            return fcmClientService.sendByTopic(model, dmpId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }

    @Override
    public void subscribeUser(String dmpId, List<String> clientTokens) throws InternalException {
        try {
            fcmClientService.subscribeUsers(dmpId, clientTokens);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Файл не найден", e);
        }
    }
}
