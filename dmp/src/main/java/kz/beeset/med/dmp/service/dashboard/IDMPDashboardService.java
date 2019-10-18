package kz.beeset.med.dmp.service.dashboard;

import kz.beeset.med.dmp.model.dashboard.DashboardWidget;
import kz.beeset.med.dmp.utils.error.InternalException;

public interface IDMPDashboardService {

    DashboardWidget getPatientsWidget(String userId) throws InternalException;
    DashboardWidget getNotificationsWidget(String userId) throws InternalException;
    DashboardWidget getProtocolsWidget(String userId) throws InternalException;
    DashboardWidget getPatientAppealsWidget(String userId) throws InternalException;
    DashboardWidget getMultiDisciplineTeamEventsWidget(String userId) throws InternalException;
    DashboardWidget getMultiDisciplineTeamTemplatesWidget(String userId) throws InternalException;

}
