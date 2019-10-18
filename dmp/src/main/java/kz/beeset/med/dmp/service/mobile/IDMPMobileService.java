package kz.beeset.med.dmp.service.mobile;

import io.swagger.models.auth.In;
import kz.beeset.med.dmp.firebase.PushNotificationModel;
import kz.beeset.med.dmp.model.*;
import kz.beeset.med.dmp.model.custom.DMPDoctorCustom;
import kz.beeset.med.dmp.model.custom.DMPInvitationCustom;
import kz.beeset.med.dmp.model.custom.DMPPatientCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDMPMobileService {

    /**********************************************************************************
     *  Авторзация
     */

    // http://IP_ADRESS:8766/med/med-dmp/mobile/reg/doc/user/rights          GET
    byte[] getUserRightsDocument() throws InternalException; // Загрузить документ с правами пользователя

    // http://IP_ADRESS:8766/med/med-dmp/mobile/reg/doc/user/agreement       GET
    byte[] getUserAgreementDocument() throws InternalException; // Загрузить документ с пользовательским соглашением

    // http://IP_ADRESS:8766/med/login через gateway2, сервис его уже готов
    // http://IP_ADRESS:8766/med/logout через gateway2, сервис его уже готов

    /**********************************************************************************
     *  Восстановление пароля
     */

    // http://IP_ADRESS:8766/med/forgotPassword/{idnOrMobilePhoneNumber} через gateway2 Authentication Controller, сервис его уже готов

    /**********************************************************************************
     * Список пациентов
     */
    Page<DMPPatientCustom> readDMPPatientPageable(Map<String, String> allRequestParams) throws InternalException;
    DMPPatient getDMPPatientById(String dmpPatientId) throws InternalException;

    /**********************************************************************************
     *  Регистрация в систему
     */

    /**********************************************************************************
     *  Регистрация в ПУЗ
     */
    Page<DMP> readDMPPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMP> readDMPByUserId(String userId) throws InternalException;
    DMPPatient registerPatientToDMP(DMPPatient dmpPatient) throws InternalException;
    Page<DMPDoctorCustom> readDMPDoctorCustomPageable(Map<String, String> allRequestParams) throws InternalException;
    void refuseDoctorParticipation(String dmpPatientId, String dmpDoctorId) throws InternalException;
    DMPDoctorCustom getDMPDoctorInfo(String userId, String dmpId) throws InternalException;


    /**********************************************************************************
     *  Приглашение врача
     */

    Page<DMPInvitation> readDMPInvitationPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPInvitation> searchDMPInvitation(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPInvitationCustom> readDMPInvitationCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPInvitationCustom> searchDMPInvitationCustomPageable(Map<String, String> allRequestParams) throws InternalException;
    List<DMPInvitation> readDMPInvitationIterableByDMPId(String dmpId) throws InternalException;
    DMPInvitation getDMPInvitation(String id) throws InternalException;
    DMPInvitation createDMPInvitation(DMPInvitation dmpInvitation) throws InternalException;
    DMPInvitation acceptDMPInvitation(String dmpInvitationId) throws InternalException;
    DMPInvitation rejectDMPInvitation(String dmpInvitationId) throws InternalException;
    DMPInvitation recallDMPInvitation(String dmpInvitationId) throws InternalException;

    /**********************************************************************************
     *  Данные об устройтсве
     */

    DMPDeviceStat registerDMPPatientDevice(DMPDeviceStat dmpDeviceStat) throws InternalException;
    DMPDeviceStat updateDeviceStat(DMPDeviceStat dmpDeviceStat) throws InternalException;
    DMPDeviceStatConfig configureDevice(DMPDeviceStatConfig dmpDeviceStatConfig) throws InternalException;
    DMPDeviceStatConfig getDeviceConfiguration(String dmpPatientId) throws InternalException;


    /**********************************************************************************
     *  Отправка и получение уведомлений
     */
    String sendNotificationToOneUser(PushNotificationModel model, String clientToken) throws InternalException;
    String sendNotificationToGroupByTopic(PushNotificationModel model, String dmpId) throws InternalException;
    void subscribeUser(String dmpId, List<String> clientTokens) throws InternalException;


    /**********************************************************************************
     *  Ссылки на zdrav.kz
     */


}
