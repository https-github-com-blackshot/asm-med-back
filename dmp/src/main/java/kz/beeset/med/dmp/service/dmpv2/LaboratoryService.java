package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.constant.DMPV2.DiseaseConstants;
import kz.beeset.med.dmp.constant.DMPV2.LaboratoryConstants;
import kz.beeset.med.dmp.model.common.Form;
import kz.beeset.med.dmp.model.dmpv2.Category;
import kz.beeset.med.dmp.model.dmpv2.Laboratory;
import kz.beeset.med.dmp.repository.dmpv2.CategoryRepository;
import kz.beeset.med.dmp.repository.dmpv2.LaboratoryRepository;
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
public class LaboratoryService implements ILaboratoryService{

    private static final Logger LOGGER = LoggerFactory.getLogger(LaboratoryService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private LaboratoryRepository laboratoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Laboratory> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = LaboratoryConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = LaboratoryConstants.DEFAUT_PAGE_SIZE;

            String sortBy = LaboratoryConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(LaboratoryConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(LaboratoryConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(LaboratoryConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(LaboratoryConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("categoryId")) {
                query.addCriteria(Criteria.where(LaboratoryConstants.CATEGORY_ID_FIELD_NAME).is(allRequestParams.get("categoryId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(LaboratoryConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(LaboratoryConstants.STATE_FIELD_NAME).is(LaboratoryConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return laboratoryRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    " Page<Laboratory> readPageable(Map<String, String> allRequestParams) )" +
                    "-", e);
        }
    }

    @Override
    public Page<Laboratory> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = LaboratoryConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = LaboratoryConstants.DEFAUT_PAGE_SIZE;

            String sortBy = LaboratoryConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(LaboratoryConstants.SORT_DIRECTION_DESC))
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

            return laboratoryRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Laboratory> searchPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<Laboratory> readIterable() throws InternalException {
        try {
            return laboratoryRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Laboratory> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<Laboratory> readIterableByIdIn(List<String> ids) throws InternalException {
        try {
            return laboratoryRepository.findAllByIdInAndState(ids, LaboratoryConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Laboratory> readIterableByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public List<Laboratory> readIterableByCategoryid(String categoryId) throws InternalException {
        try {
            return laboratoryRepository.findAllByCategoryIdAndState(categoryId, LaboratoryConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Laboratory> readIterableByCategoryId(String categoryId)" +
                    "-", e);
        }
    }

    @Override
    public Object readCategorizedLaboratoriesByIdIn(List<String> ids) throws InternalException {
        try {
            List<Object> data = new ArrayList<>();

            List<Laboratory> laboratories = laboratoryRepository.findAllByIdInAndState(ids, LaboratoryConstants.STATUS_ACTIVE);
            List<Category> laboratoryCategories = categoryRepository.findAllByFilterAndState("laboratory", DefaultConstant.STATUS_ACTIVE);

            laboratoryCategories.forEach(category -> {
                List<Laboratory> filteredLabs = laboratories.stream().filter(laboratory -> laboratory.getCategoryId().equals(category.getId())).collect(Collectors.toList());

                LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                content.put("category", category);
                content.put("laboratories", filteredLabs);

                data.add(content);
            });

            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Object readCategorizedLaboratoriesByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public Object readCategorizedLaboratories() throws InternalException {
        try {
            List<Object> data = new ArrayList<>();

            List<Laboratory> laboratories = readIterable();
            List<Category> laboratoryCategories = categoryRepository.findAllByFilterAndState("laboratory", DefaultConstant.STATUS_ACTIVE);

            laboratoryCategories.forEach(category -> {
                List<Laboratory> filteredLabs = laboratories.stream().filter(laboratory -> laboratory.getCategoryId().equals(category.getId())).collect(Collectors.toList());

                LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                content.put("category", category);
                content.put("laboratories", filteredLabs);

                data.add(content);
            });

            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Object readAllLaboratoriesFilteredByCategory()" +
                    "-", e);
        }
    }

    @Override
    public Laboratory readOne(String id) throws InternalException {
        try {
            return laboratoryRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Laboratory readOne(String id)" +
                    "-", e);
        }
    }

    @Override
    public Laboratory create(Laboratory laboratory) throws InternalException {
        try {
            laboratory.setState(LaboratoryConstants.STATUS_ACTIVE);
            return laboratoryRepository.save(laboratory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Laboratory create(Laboratory laboratory)" +
                    "-", e);
        }
    }

    @Override
    public Laboratory update(Laboratory laboratory) throws InternalException {
        try {
            laboratory.setState(LaboratoryConstants.STATUS_ACTIVE);
            return laboratoryRepository.save(laboratory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Laboratory update(Laboratory laboratory)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            Laboratory laboratory = laboratoryRepository.getById(id);
            laboratory.setState(LaboratoryConstants.STATUS_DELETED);
            laboratoryRepository.save(laboratory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }
    }

}
