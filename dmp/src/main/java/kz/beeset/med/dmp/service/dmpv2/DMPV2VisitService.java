package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.constant.DMPV2.DiseaseConstants;
import kz.beeset.med.dmp.model.dmpv2.DMPV2Visit;
import kz.beeset.med.dmp.model.dmpv2.Disease;
import kz.beeset.med.dmp.repository.dmpv2.DMPV2VisitRepository;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DMPV2VisitService implements IDMPV2VisitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DMPV2VisitService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPV2VisitRepository repository;

    @Override
    public Page<DMPV2Visit> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DefaultConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("nameRu")) {
                query.addCriteria(Criteria.where("nameRu").is(allRequestParams.get("nameRu")));
            }
            if (allRequestParams.containsKey("nameKz")) {
                query.addCriteria(Criteria.where("nameKz").is(allRequestParams.get("nameKz")));
            }
            if (allRequestParams.containsKey("nameEn")) {
                query.addCriteria(Criteria.where("nameEn").is(allRequestParams.get("nameEn")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where("description").is(allRequestParams.get("description")));
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
                    "Page<DMPV2Visit> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<DMPV2Visit> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DefaultConstant.DEFAUT_PAGE_NUMBER;

            int pageSize = DefaultConstant.DEFAUT_PAGE_SIZE;

            String sortBy = DefaultConstant.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DefaultConstant.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("nameRu")) {
                query.addCriteria(Criteria.where("nameRu").is(allRequestParams.get("nameRu")));
            }
            if (allRequestParams.containsKey("nameKz")) {
                query.addCriteria(Criteria.where("nameKz").is(allRequestParams.get("nameKz")));
            }
            if (allRequestParams.containsKey("nameEn")) {
                query.addCriteria(Criteria.where("nameEn").is(allRequestParams.get("nameEn")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where("description").is(allRequestParams.get("description")));
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
                    "Page<DMPV2Visit> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2Visit> readIterable() throws InternalException {
        try {
            return repository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2Visit> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<DMPV2Visit> readIterableByPatientId(String patientId) throws InternalException {
        try {
            List<DMPV2Visit> visits = repository.findAllByPatientIdAndStateOrderByCounterDesc(patientId, DefaultConstant.STATUS_ACTIVE);
            return visits;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2Visit> readIterableByPatientId()" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Visit readLastVisit(String patientId) throws InternalException {
        try {
            List<DMPV2Visit> visits = repository.findAllByPatientIdAndStateOrderByCounterDesc(patientId, DefaultConstant.STATUS_ACTIVE);
            return visits != null && visits.size() > 0 ? visits.get(0) : null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<DMPV2Visit> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Visit readOne(String id) throws InternalException {
        try {
            return repository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2Visit readOne(String id" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Visit create(DMPV2Visit value) throws InternalException {
        try {
            value.setState(DefaultConstant.STATUS_ACTIVE);
            return repository.save(value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2Visit create(DMPV2Visit value)" +
                    "-", e);
        }
    }

    @Override
    public DMPV2Visit update(DMPV2Visit value) throws InternalException {
        try {
            DMPV2Visit oldVisit = this.repository.getById(value.getId());
            if (oldVisit == null) {
                LOGGER.error("DMPV2Visit null with id: " + value.getId());
                throw new IllegalArgumentException("Нельзя обновлять visit null");
            }
            oldVisit.setDmpV2Id(value.getDmpV2Id());
            oldVisit.setMeasurement(value.getMeasurement());
            oldVisit.setState(DefaultConstant.STATUS_ACTIVE);
            return repository.save(oldVisit);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "DMPV2Visit update(DMPV2Visit value)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            DMPV2Visit value = repository.getById(id);
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
