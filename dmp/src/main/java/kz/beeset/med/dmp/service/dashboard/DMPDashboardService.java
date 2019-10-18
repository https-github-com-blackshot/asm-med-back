package kz.beeset.med.dmp.service.dashboard;

import kz.beeset.med.dmp.constant.*;
import kz.beeset.med.dmp.model.DMPDoctor;
import kz.beeset.med.dmp.model.dashboard.DashboardWidget;
import kz.beeset.med.dmp.repository.*;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DMPDashboardService implements IDMPDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPDashboardService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private DMPPatientRepository dmpPatientRepository;
    @Autowired
    private DMPDoctorRepository dmpDoctorRepository;
    @Autowired
    private DMPNotificationRepository dmpNotificationRepository;
    @Autowired
    private DMPPatientAppealRepository dmpPatientAppealRepository;
    @Autowired
    private DMPProtocolRepository dmpProtocolRepository;
    @Autowired
    private DMPMDTEventRepository dmpmdtEventRepository;
    @Autowired
    private DMPMDTTemplateRepository dmpmdtTemplateRepository;


    @Override
    public DashboardWidget getPatientsWidget(String userId) throws InternalException {

        List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByUserId(userId);
        List<String> dmpDoctorIds = dmpDoctorList.stream().map(DMPDoctor::getId).collect(Collectors.toList());
        Integer patientsCount = dmpPatientRepository.countAllByDmpDoctorIdsInAndState(dmpDoctorIds, DMPPatientConstants.STATUS_ACTIVE);

        DashboardWidget dashboardWidget = new DashboardWidget();
        dashboardWidget.setTitle("Пациенты");
        dashboardWidget.setDataLabel("Количество пациентов");
        dashboardWidget.setDataCount(patientsCount);
        dashboardWidget.setBackDataLabel("Количество пациентов");
        dashboardWidget.setBackDataCount(patientsCount);
        dashboardWidget.setDataExtraLabel("");
        dashboardWidget.setLink("/doctor-portal/dmp-patients");

        return dashboardWidget;
    }

    @Override
    public DashboardWidget getNotificationsWidget(String userId) throws InternalException {

        Integer notificationsCount = dmpNotificationRepository.countAllByDmpDoctorUserIdAndState(userId, DMPNotificationConstants.STATUS_ACTIVE);

        DashboardWidget dashboardWidget = new DashboardWidget();
        dashboardWidget.setTitle("Уведомления");
        dashboardWidget.setDataLabel("Количество уведомлений");
        dashboardWidget.setDetail("Количество уведомлений");
        dashboardWidget.setDataCount(notificationsCount);
        dashboardWidget.setBackDataLabel("Количество уведомлений");
        dashboardWidget.setBackDataCount(notificationsCount);
        dashboardWidget.setDataExtraLabel("");
        dashboardWidget.setLink("/doctor-portal/dmp-notifications");

        return dashboardWidget;
    }

    @Override
    public DashboardWidget getProtocolsWidget(String userId) throws InternalException {

        List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByUserId(userId);
        List<String> dmpIds = dmpDoctorList.stream().map(DMPDoctor::getDmpId).collect(Collectors.toList());
        Integer protocolsCount = dmpProtocolRepository.countAllByDmpIdInAndState(dmpIds, DMPProtocolConstants.STATUS_ACTIVE);

        DashboardWidget dashboardWidget = new DashboardWidget();
        dashboardWidget.setTitle("Протокола");
        dashboardWidget.setDataLabel("Количество протоколов");
        dashboardWidget.setDetail("Количество протоколов");
        dashboardWidget.setDataCount(protocolsCount);
        dashboardWidget.setBackDataLabel("Количество протоколов");
        dashboardWidget.setBackDataCount(protocolsCount);
        dashboardWidget.setDataExtraLabel("");
        dashboardWidget.setLink("/doctor-portal/dmp-protocols");

        return dashboardWidget;

    }

    @Override
    public DashboardWidget getPatientAppealsWidget(String userId) throws InternalException {

        List<DMPDoctor> dmpDoctorList = dmpDoctorRepository.getAllByUserId(userId);
        List<String> dmpDoctorIds = dmpDoctorList.stream().map(DMPDoctor::getId).collect(Collectors.toList());
        Integer patientAppealsCount = dmpPatientAppealRepository.countAllByDmpDoctorIdInAndStatus(dmpDoctorIds, "active");

        DashboardWidget dashboardWidget = new DashboardWidget();
        dashboardWidget.setTitle("Обращение пациентов");
        dashboardWidget.setDataLabel("Количество обращений");
        dashboardWidget.setDetail("Количество обращений");
        dashboardWidget.setDataCount(patientAppealsCount);
        dashboardWidget.setBackDataLabel("Количество обращений");
        dashboardWidget.setBackDataCount(patientAppealsCount);
        dashboardWidget.setDataExtraLabel("");
        dashboardWidget.setLink("/doctor-portal/dmp-patient-appeals");

        return dashboardWidget;
    }

    @Override
    public DashboardWidget getMultiDisciplineTeamEventsWidget(String userId) throws InternalException {
        Integer eventCount = dmpmdtEventRepository.countAllByPendingDoctorUserIdListContainsOrAcceptedDoctorUserIdListContainsOrOwnerIdAndState(userId, userId, userId, DMPMDTEventConstants.STATUS_ACTIVE);

        DashboardWidget dashboardWidget = new DashboardWidget();
        dashboardWidget.setTitle("События по мультидисциплинарным командам");
        dashboardWidget.setDataLabel("Количестов событий");
        dashboardWidget.setDetail("Количестов событий");
        dashboardWidget.setDataCount(eventCount);
        dashboardWidget.setBackDataLabel("Количестов событий");
        dashboardWidget.setBackDataCount(eventCount);
        dashboardWidget.setDataExtraLabel("");
        dashboardWidget.setLink("/doctor-portal/dmp-mdt-events");

        return dashboardWidget;
    }

    @Override
    public DashboardWidget getMultiDisciplineTeamTemplatesWidget(String userId) throws InternalException {

        Integer templatesCount = dmpmdtTemplateRepository.countAllByOwnerIdAndState(userId, DMPMDTTemplateConstants.STATUS_ACTIVE);

        DashboardWidget dashboardWidget = new DashboardWidget();
        dashboardWidget.setTitle("Мои шаблоны по мультидисциплинарным командам");
        dashboardWidget.setDataLabel("Количестов шаблонов");
        dashboardWidget.setDetail("Количестов шаблонов");
        dashboardWidget.setDataCount(templatesCount);
        dashboardWidget.setBackDataLabel("Количестов шаблонов");
        dashboardWidget.setBackDataCount(templatesCount);
        dashboardWidget.setDataExtraLabel("");
        dashboardWidget.setLink("/doctor-portal/dmp-mdt-templates");

        return dashboardWidget;
    }
}
