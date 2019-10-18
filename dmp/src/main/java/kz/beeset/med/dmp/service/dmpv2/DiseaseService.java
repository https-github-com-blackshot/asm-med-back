package kz.beeset.med.dmp.service.dmpv2;



import kz.beeset.med.dmp.constant.DMPV2.DiseaseConstants;
import kz.beeset.med.dmp.model.common.Form;
import kz.beeset.med.dmp.model.dmpv2.Disease;
import kz.beeset.med.dmp.repository.dmpv2.DiseaseRepository;
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
public class DiseaseService implements IDiseaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiseaseService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Override
    public Page<Disease> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DiseaseConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DiseaseConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DiseaseConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DiseaseConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(DiseaseConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(DiseaseConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DiseaseConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("categoryId")) {
                query.addCriteria(Criteria.where(DiseaseConstants.CATEGORY_ID_FIELD_NAME).is(allRequestParams.get("categoryId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DiseaseConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DiseaseConstants.STATE_FIELD_NAME).is(DiseaseConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return diseaseRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    " Page<Disease> readPageable(Map<String, String> allRequestParams) )" +
                    "-", e);
        }
    }

    @Override
    public Page<Disease> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DiseaseConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DiseaseConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DiseaseConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DiseaseConstants.SORT_DIRECTION_DESC))
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
            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return diseaseRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Disease> searchPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<Disease> readIterable() throws InternalException {
        try {
            return diseaseRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Disease> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<Disease> readIterableByCategoryId(String categoryId) throws InternalException {
        try {
            return diseaseRepository.findAllByCategoryIdAndState(categoryId, DiseaseConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Disease> readIterableByCategoryId(String categoryId)" +
                    "-", e);
        }
    }

    @Override
    public List<Disease> readIterableByIdIn(List<String> diseaseIds) throws InternalException {
        try {
            return diseaseRepository.findAllByIdIn(diseaseIds);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Disease> readIterableByIdIn(List<String> diseaseIds)" +
                    "-", e);
        }
    }

    @Override
    public Disease readOne(String id) throws InternalException {
        try {
            return diseaseRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Disease readOne(String id)" +
                    "-", e);
        }
    }

    @Override
    public Disease create(Disease disease) throws InternalException {
        try {
            disease.setState(DiseaseConstants.STATUS_ACTIVE);
            return diseaseRepository.save(disease);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Disease create(Disease disease)" +
                    "-", e);
        }
    }

    @Override
    public Disease update(Disease disease) throws InternalException {
        try {
            disease.setState(DiseaseConstants.STATUS_ACTIVE);
            return diseaseRepository.save(disease);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Disease update(Disease disease)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            Disease disease = diseaseRepository.getById(id);
            disease.setState(DiseaseConstants.STATUS_DELETED);
            diseaseRepository.save(disease);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }

    }

    @Override
    public List<Form> readFormsByDiseaseId(String diseaseId) throws InternalException {
        return null;
    }

}
