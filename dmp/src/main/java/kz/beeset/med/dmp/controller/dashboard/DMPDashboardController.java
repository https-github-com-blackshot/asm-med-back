package kz.beeset.med.dmp.controller.dashboard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.beeset.med.dmp.controller.DMPAdminController;
import kz.beeset.med.dmp.model.dashboard.DashboardWidget;
import kz.beeset.med.dmp.service.dashboard.IDMPDashboardService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("dmp/dashboard")
@Api(tags = {"DMPDashboard"}, description = "DMPDashboard", authorizations = {@Authorization(value = "bearerAuth")})
public class DMPDashboardController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPDashboardController.class);

    @Autowired
    private IDMPDashboardService dashboardService;

    @ApiOperation(value = "Получить DMPDashoard виджеты по заданному ID врача", tags = {"DMPDashboard"})
    @RequestMapping(value = "read/widgets/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDMPDashboardWidgets(@PathVariable(name = "userId") String userId) {
        try {
            List<DashboardWidget> dashboardWidgetList = new ArrayList<>();

            DashboardWidget widget1 = dashboardService.getPatientsWidget(userId);
            DashboardWidget widget2 = dashboardService.getNotificationsWidget(userId);
            DashboardWidget widget3 = dashboardService.getProtocolsWidget(userId);
            DashboardWidget widget4 = dashboardService.getPatientAppealsWidget(userId);
            DashboardWidget widget5 = dashboardService.getMultiDisciplineTeamEventsWidget(userId);
            DashboardWidget widget6 = dashboardService.getMultiDisciplineTeamTemplatesWidget(userId);

            dashboardWidgetList.add(widget1);
            dashboardWidgetList.add(widget2);
            dashboardWidgetList.add(widget3);
            dashboardWidgetList.add(widget4);
            dashboardWidgetList.add(widget5);
            dashboardWidgetList.add(widget6);

            return builder(success(dashboardWidgetList));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return builder(errorWithDescription(e.getErrorRef(), e.getMessage()));
        }
    }

}
