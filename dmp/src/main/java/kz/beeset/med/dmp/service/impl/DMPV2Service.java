package kz.beeset.med.dmp.service.impl;


import kz.beeset.med.dmp.model.dmpv2.Disease;
import kz.beeset.med.dmp.model.dmpv2.DiseaseData;
import kz.beeset.med.dmp.repository.dmpv2.DiseaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.model.dmpv2.DMPV2;
import kz.beeset.med.dmp.repository.dmpv2.DMPV2Repository;
import kz.beeset.med.dmp.service.IDMPV2Service;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DMPV2Service implements IDMPV2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2Service.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPV2Repository repository;
    @Autowired
    private DiseaseRepository diseaseRepository;

    @Override
    public Page<DMPV2> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DefaultConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("sortDirection")) {
                if (allRequestParams.get("sortDirection").equals(DefaultConstant.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;
            }
            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }

            query.addCriteria(Criteria.where(DefaultConstant.STATE_FIELD_NAME).is(DefaultConstant.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return repository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPV2> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DefaultConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("sortDirection")) {
                if (allRequestParams.get("sortDirection").equals(DefaultConstant.SORT_DIRECTION_DESC))
                    sortDirection = Sort.Direction.DESC;
            }
            if (allRequestParams.containsKey("sort")) {
                sortBy = allRequestParams.get("sort");
            }
            if (allRequestParams.containsKey("page")) {
                pageNumber = Integer.parseInt(allRequestParams.get("page"));
            }
            if (allRequestParams.containsKey("size")) {
                pageSize = Integer.parseInt(allRequestParams.get("size"));
            }

            query.addCriteria(Criteria.where(DefaultConstant.STATE_FIELD_NAME).is(DefaultConstant.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return repository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<DMPV2> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2> readIterable() throws InternalException {
        try {
            return repository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public DMPV2 readOne(String id) throws InternalException {
        try {
            return repository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2 readOne(String id" +
                    "-", e);
        }
    }

    @Override
    public DMPV2 updateAfterDiseaseSelection(DMPV2 dmp) throws InternalException {
        try {
            List<Disease> diseases = diseaseRepository.findAllByIdIn(dmp.getSelectedDiseaseIds());
            DiseaseData dmpData = dmp.getDiseaseData();
            for (Disease disease: diseases) {
                DiseaseData template = disease.getTemplate();

                if (template != null) {

                    if (!template.getSubDannie().isEmpty() && !dmpData.getSubDannie().contains(template.getSubDannie())) {
                        if (!dmpData.getSubDannie().isEmpty()) {
                            dmpData.setSubDannie(dmpData.getSubDannie() + "\n\n" + template.getSubDannie());
                        } else {
                            dmpData.setSubDannie(template.getSubDannie() + "\n\n");
                        }
                    }

                    if (!template.getAnamBol().isEmpty() && !dmpData.getAnamBol().contains(template.getAnamBol())) {
                        if (!dmpData.getAnamBol().isEmpty()) {
                            dmpData.setAnamBol(dmpData.getAnamBol() + "\n\n" + template.getAnamBol());
                        } else {
                            dmpData.setAnamBol(template.getAnamBol() + "\n\n");
                        }
                    }

                    if (!template.getAnamZhiz().isEmpty() && !dmpData.getAnamZhiz().contains(template.getAnamZhiz())) {
                        if (!dmpData.getAnamZhiz().isEmpty()) {
                            dmpData.setAnamZhiz(dmpData.getAnamZhiz() + "\n\n" + template.getAnamZhiz());
                        } else {
                            dmpData.setAnamZhiz(template.getAnamZhiz() + "\n\n");
                        }
                    }

                    if (template.getDiagMethods() != null && template.getDiagMethods().size() > 0) {
                        template.getDiagMethods().forEach(method -> {
                            if (!dmpData.getDiagMethods().contains(method)) {
                                dmpData.getDiagMethods().add(method);
                            }
//                            else {
//                                dmpData.getDiagMethods().remove(method);
//                            }
                        });
                    }


                    if (!template.getDiagZakl().isEmpty() && !dmpData.getDiagZakl().contains(template.getDiagZakl())) {
                        if (!dmpData.getDiagZakl().isEmpty()) {
                            dmpData.setDiagZakl(dmpData.getDiagZakl() + "\n\n" + template.getDiagZakl());
                        } else {
                            dmpData.setDiagZakl(template.getDiagZakl() + "\n\n");
                        }
                    }

                    if (!template.getInfoMat().isEmpty() && !dmpData.getInfoMat().contains(template.getInfoMat())) {
                        if (!dmpData.getInfoMat().isEmpty()) {
                            dmpData.setInfoMat(dmpData.getInfoMat() + "\n\n" + template.getInfoMat());
                        } else {
                            dmpData.setInfoMat(template.getInfoMat() + "\n\n");
                        }
                    }

                    if (template.getLabAnalys() != null && template.getLabAnalys().size() > 0) {
                        template.getLabAnalys().forEach(lab -> {
                            if (!dmpData.getLabAnalys().contains(lab)) {
                                dmpData.getLabAnalys().add(lab);
                            }
//                            else {
//                                dmpData.getLabAnalys().remove(lab);
//                            }
                        });
                    }

                    if (template.getLekNazn() != null && template.getLekNazn().size() > 0) {
                        template.getLekNazn().forEach(medicine -> {
                            if (!dmpData.getLekNazn().contains(medicine)) {
                                dmpData.getLekNazn().add(medicine);
                            }
//                            else {
//                                dmpData.getLekNazn().remove(medicine);
//                            }
                        });
                    }

                    if (template.getProcAndInter() != null && template.getProcAndInter().size() > 0) {
                        template.getProcAndInter().forEach(pai -> {
                            if (!dmpData.getProcAndInter().contains(pai)) {
                                dmpData.getProcAndInter().add(pai);
                            }
//                            else {
//                                dmpData.getLekNazn().remove(medicine);
//                            }
                        });
                    }

                    if (!template.getNote().isEmpty() && !dmpData.getNote().contains(template.getNote())) {
                        if (!dmpData.getNote().isEmpty()) {
                            dmpData.setNote(dmpData.getNote() + "\n\n" + template.getNote());
                        } else {
                            dmpData.setNote(template.getNote() + "\n\n");
                        }
                    }

                    if (!template.getObjDannie().isEmpty() && !dmpData.getObjDannie().contains(template.getObjDannie())) {
                        if (!dmpData.getObjDannie().isEmpty()) {
                            dmpData.setObjDannie(dmpData.getObjDannie() + "\n\n" + template.getObjDannie());
                        } else {
                            dmpData.setObjDannie(template.getObjDannie() + "\n\n");
                        }
                    }

                    if (!template.getSovNomadiet().isEmpty() && !dmpData.getSovNomadiet().contains(template.getSovNomadiet())) {
                        if (!dmpData.getSovNomadiet().isEmpty()) {
                            dmpData.setSovNomadiet(dmpData.getSovNomadiet() + "\n\n" + template.getSovNomadiet());
                        } else {
                            dmpData.setSovNomadiet(template.getSovNomadiet() + "\n\n");
                        }
                    }

                    if (!template.getVrachRec().isEmpty() && !dmpData.getVrachRec().contains(template.getVrachRec())) {
                        if (!dmpData.getVrachRec().isEmpty()) {
                            dmpData.setVrachRec(dmpData.getVrachRec() + "\n\n" + template.getVrachRec());
                        } else {
                            dmpData.setVrachRec(template.getVrachRec() + "\n\n");
                        }
                    }
                }
            }

            dmp.setDiseaseData(dmpData);

            if (dmp.getSelectedLaboratoryIds() == null) {
                dmp.setSelectedLaboratoryIds(new ArrayList<>());
            }

            if (dmp.getSelectedDiagnosticIds() == null) {
                dmp.setSelectedDiagnosticIds(new ArrayList<>());
            }

            if (dmp.getSelectedDiseaseIds() == null) {
                dmp.setSelectedDiseaseIds(new ArrayList<>());
            }

            if (dmp.getSelectedMedicineIds() == null) {
                dmp.setSelectedMedicineIds(new ArrayList<>());
            }

            if (dmp.getSelectedProceduresAndInterventionsIds() == null) {
                dmp.setSelectedProceduresAndInterventionsIds(new ArrayList<>());
            }

            dmp.getDiseaseData().getDiagMethods().forEach(diagnosticId -> {
                if (!dmp.getSelectedDiagnosticIds().contains(diagnosticId)) {
                    dmp.getSelectedDiagnosticIds().add(diagnosticId);
                }
            });

            dmp.getDiseaseData().getLabAnalys().forEach(labId -> {
                if (!dmp.getSelectedLaboratoryIds().contains(labId)) {
                    dmp.getSelectedLaboratoryIds().add(labId);
                }
            });

            dmp.getDiseaseData().getLekNazn().forEach(medicineId -> {
                if (!dmp.getSelectedMedicineIds().contains(medicineId)) {
                    dmp.getSelectedMedicineIds().add(medicineId);
                }
            });

            dmp.getDiseaseData().getProcAndInter().forEach(paiId -> {
                if (!dmp.getSelectedProceduresAndInterventionsIds().contains(paiId)) {
                    dmp.getSelectedProceduresAndInterventionsIds().add(paiId);
                }
            });

            dmp.getSelectedDiagnosticIds().forEach(diagnosticId -> {
                if (!dmp.getDiseaseData().getDiagMethods().contains(diagnosticId)) {
                    dmp.getDiseaseData().getDiagMethods().add(diagnosticId);
                }
            });

            dmp.getSelectedLaboratoryIds().forEach(labId -> {
                if (!dmp.getDiseaseData().getLabAnalys().contains(labId)) {
                    dmp.getDiseaseData().getLabAnalys().add(labId);
                }
            });

            dmp.getSelectedProceduresAndInterventionsIds().forEach(paiId -> {
                if (!dmp.getDiseaseData().getProcAndInter().contains(paiId)) {
                    dmp.getDiseaseData().getProcAndInter().add(paiId);
                }
            });

            dmp.getSelectedMedicineIds().forEach(medId -> {
                if (!dmp.getDiseaseData().getLekNazn().contains(medId)) {
                    dmp.getDiseaseData().getLekNazn().add(medId);
                }
            });

            dmp.setState(DefaultConstant.STATUS_ACTIVE);

            return repository.save(dmp);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2 updateAfterDiseaseSelection(DMPV2 dmp)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2 create(DMPV2 value) throws InternalException {
        try {
            value.setState(DefaultConstant.STATUS_ACTIVE);
            return repository.save(value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2 create(DMPV2 value)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2 update(DMPV2 value) throws InternalException {
        try {
            value.setState(DefaultConstant.STATUS_ACTIVE);
            return repository.save(value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2 update(DMPV2 value)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            DMPV2 value = repository.getById(id);
            value.setState(DefaultConstant.STATUS_DELETED);
            repository.save(value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }
    }
}
