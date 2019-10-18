package kz.beeset.med.dmp.service.impl;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPOrganizationMap;
import kz.beeset.med.dmp.repository.DMPOrganizationMapRepository;
import kz.beeset.med.dmp.repository.DMPRepository;
import kz.beeset.med.dmp.service.IDMPOrganizationMapService;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class DMPOrganizationMapService implements IDMPOrganizationMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPOrganizationMapService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPRepository dmpRepository;
    @Autowired
    private DMPOrganizationMapRepository dmpOrganizationMapRepository;

    @Override
    public List<DMP> getDMPListByOrganizationId(String organizationId) throws InternalException {
        try {
            List<DMPOrganizationMap> dmpOrganizationMapList = dmpOrganizationMapRepository.findAllByOrganizationIdAndState(organizationId, DefaultConstant.STATUS_ACTIVE);
            List<String> dmpIds = dmpOrganizationMapList.stream().map(DMPOrganizationMap::getDmpId).collect(Collectors.toList());
            return dmpRepository.findAllByIdInAndState(dmpIds, DefaultConstant.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMP> getDMPListByOrganizationId(String organizationId)" + "-", e);
        }
    }

    @Override
    public List<DMPOrganizationMap> save(String organizationId, HashMap<String, Boolean> checkboxes) throws InternalException {
        try {

            List<String> selectedDMPs = new ArrayList<>();

            checkboxes.keySet().forEach(dmpId -> {
                if (checkboxes.get(dmpId)) {
                    selectedDMPs.add(dmpId);
                }
            });

            if (selectedDMPs.contains(null)) {
                throw new IllegalArgumentException("Has null value");
            }

            List<DMP> dmpList = getDMPListByOrganizationId(organizationId);

            List<String> skippedList = new ArrayList<>();
            List<String> removedList = new ArrayList<>();

            dmpList.forEach(dmp -> {
                if (selectedDMPs.contains(dmp.getId())) {
                    skippedList.add(dmp.getId());
                } else {
                    removedList.add(dmp.getId());
                }
            });

            List<DMPOrganizationMap> listToDeactivate = dmpOrganizationMapRepository.findAllByOrganizationIdAndDmpIdInAndState(organizationId, removedList, DefaultConstant.STATUS_ACTIVE);
            dmpOrganizationMapRepository.deleteAll(listToDeactivate);

            List<DMPOrganizationMap> dmpOrganizationMapList = new ArrayList<>();

            selectedDMPs.forEach(selectedId -> {

                if (!skippedList.contains(selectedId)) {
                    DMPOrganizationMap dmpOrganizationMap = new DMPOrganizationMap();
                    dmpOrganizationMap.setDmpId(selectedId);
                    dmpOrganizationMap.setOrganizationId(organizationId);
                    dmpOrganizationMap.setState(DefaultConstant.STATUS_ACTIVE);
                    dmpOrganizationMapList.add(dmpOrganizationMap);
                }

            });

            return dmpOrganizationMapRepository.saveAll(dmpOrganizationMapList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPOrganizationMap> save(String organizationId, List<DMP> selectedDMPs)" + "-", e);
        }
    }

    @Override
    public void delete(String orgId, String dmpId) throws InternalException {
        try {
            DMPOrganizationMap dmpOrganizationMap = dmpOrganizationMapRepository.findByOrganizationIdAndDmpIdAndState(orgId, dmpId, DefaultConstant.STATUS_ACTIVE);
            dmpOrganizationMapRepository.delete(dmpOrganizationMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" + "-", e);
        }
    }
}
