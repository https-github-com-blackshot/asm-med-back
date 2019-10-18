package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.constant.DMPV2.LaboratoryConstants;
import kz.beeset.med.dmp.constant.DMPV2.ProceduresAndInterventionsConstants;
import kz.beeset.med.dmp.model.dmpv2.Category;
import kz.beeset.med.dmp.model.dmpv2.Laboratory;
import kz.beeset.med.dmp.model.dmpv2.ProceduresAndInterventions;
import kz.beeset.med.dmp.repository.dmpv2.CategoryRepository;
import kz.beeset.med.dmp.repository.dmpv2.ProceduresAndInterventionsRepository;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProceduresAndInterventionsService implements IProceduresAndInterventionsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProceduresAndInterventionsService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private ProceduresAndInterventionsRepository proceduresAndInterventionsRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Page<ProceduresAndInterventions> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = ProceduresAndInterventionsConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = ProceduresAndInterventionsConstants.DEFAUT_PAGE_SIZE;

            String sortBy = ProceduresAndInterventionsConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(ProceduresAndInterventionsConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(ProceduresAndInterventionsConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(ProceduresAndInterventionsConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(ProceduresAndInterventionsConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("categoryId")) {
                query.addCriteria(Criteria.where(ProceduresAndInterventionsConstants.CATEGORY_ID_FIELD_NAME).is(allRequestParams.get("categoryId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(ProceduresAndInterventionsConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(ProceduresAndInterventionsConstants.STATE_FIELD_NAME).is(ProceduresAndInterventionsConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return proceduresAndInterventionsRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<ProceduresAndInterventions> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<ProceduresAndInterventions> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = ProceduresAndInterventionsConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = ProceduresAndInterventionsConstants.DEFAUT_PAGE_SIZE;

            String sortBy = ProceduresAndInterventionsConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(ProceduresAndInterventionsConstants.SORT_DIRECTION_DESC))
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

            return proceduresAndInterventionsRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<ProceduresAndInterventions> searchPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<ProceduresAndInterventions> readIterable() throws InternalException {
        try {
            return proceduresAndInterventionsRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<ProceduresAndInterventions> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<ProceduresAndInterventions> readIterableByIdIn(List<String> ids) throws InternalException {
        try {
            return proceduresAndInterventionsRepository.findAllByIdInAndState(ids, ProceduresAndInterventionsConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<ProceduresAndInterventions> readIterableByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public List<ProceduresAndInterventions> readIterableByCategoryid(String categoryId) throws InternalException {
        try {
            return proceduresAndInterventionsRepository.findAllByCategoryIdAndState(categoryId, LaboratoryConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<ProceduresAndInterventions> readIterableByCategoryid(String categoryId)" +
                    "-", e);
        }
    }

    @Override
    public Object readCategorizedListByIdIn(List<String> ids) throws InternalException {
        try {
            List<Object> data = new ArrayList<>();

            List<ProceduresAndInterventions> proceduresAndInterventions = proceduresAndInterventionsRepository.findAllByIdInAndState(ids, LaboratoryConstants.STATUS_ACTIVE);
            List<Category> paiCategories = categoryRepository.findAllByFilterAndState("proceduresAndInterventions", DefaultConstant.STATUS_ACTIVE);

            paiCategories.forEach(category -> {
                List<ProceduresAndInterventions> filteredPais = proceduresAndInterventions.stream().filter(p -> p.getCategoryId().equals(category.getId())).collect(Collectors.toList());

                LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                content.put("category", category);
                content.put("proceduresAndInterventions", filteredPais);

                data.add(content);
            });

            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Object readCategorizedListByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public Object readCategorizedList() throws InternalException {
        try {
            List<Object> data = new ArrayList<>();

            List<ProceduresAndInterventions> proceduresAndInterventions = readIterable();
            List<Category> paiCategories = categoryRepository.findAllByFilterAndState("proceduresAndInterventions", DefaultConstant.STATUS_ACTIVE);

            paiCategories.forEach(category -> {
                List<ProceduresAndInterventions> filteredPais = proceduresAndInterventions.stream().filter(p -> p.getCategoryId().equals(category.getId())).collect(Collectors.toList());

                LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                content.put("category", category);
                content.put("proceduresAndInterventions", filteredPais);

                data.add(content);
            });

            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Object readCategorizedList()" +
                    "-", e);
        }
    }

    @Override
    public ProceduresAndInterventions readOne(String id) throws InternalException {
        try {
            return proceduresAndInterventionsRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "ProceduresAndInterventions readOne(String id)" +
                    "-", e);
        }
    }

    @Override
    public ProceduresAndInterventions create(ProceduresAndInterventions proceduresAndInterventions) throws InternalException {
        try {
            proceduresAndInterventions.setState(ProceduresAndInterventionsConstants.STATUS_ACTIVE);
            return proceduresAndInterventionsRepository.save(proceduresAndInterventions);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "ProceduresAndInterventions create(ProceduresAndInterventions proceduresAndInterventions)" +
                    "-", e);
        }
    }

    @Override
    public ProceduresAndInterventions update(ProceduresAndInterventions proceduresAndInterventions) throws InternalException {
        try {
            proceduresAndInterventions.setState(ProceduresAndInterventionsConstants.STATUS_ACTIVE);
            return proceduresAndInterventionsRepository.save(proceduresAndInterventions);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "ProceduresAndInterventions update(ProceduresAndInterventions proceduresAndInterventions)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            ProceduresAndInterventions proceduresAndInterventions = proceduresAndInterventionsRepository.getById(id);
            proceduresAndInterventions.setState(LaboratoryConstants.STATUS_DELETED);
            proceduresAndInterventionsRepository.save(proceduresAndInterventions);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }
    }
}
