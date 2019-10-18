package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.constructor.constant.DefaultConstant;
import kz.beeset.med.dmp.constant.DMPV2.DiagnosticsConstants;
import kz.beeset.med.dmp.model.common.Form;
import kz.beeset.med.dmp.model.dmpv2.Category;
import kz.beeset.med.dmp.model.dmpv2.Diagnostics;
import kz.beeset.med.dmp.repository.dmpv2.CategoryRepository;
import kz.beeset.med.dmp.repository.dmpv2.DiagnosticsRepository;
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
public class DiagnosticsService implements IDiagnosticsService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticsService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DiagnosticsRepository diagnosticsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Diagnostics> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DiagnosticsConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DiagnosticsConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DiagnosticsConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(DiagnosticsConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(DiagnosticsConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(DiagnosticsConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(DiagnosticsConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("categoryId")) {
                query.addCriteria(Criteria.where(DiagnosticsConstants.CATEGORY_ID_FIELD_NAME).is(allRequestParams.get("categoryId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DiagnosticsConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(DiagnosticsConstants.STATE_FIELD_NAME).is(DiagnosticsConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return diagnosticsRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Diagnostics> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }


    @Override
    public Page<Diagnostics> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = DiagnosticsConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = DiagnosticsConstants.DEFAUT_PAGE_SIZE;

            String sortBy = DiagnosticsConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(DiagnosticsConstants.SORT_DIRECTION_DESC))
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

            return diagnosticsRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Diagnostics> searchPageable(Map<String, String> allRequestParams))" +
                    "-", e);
        }
    }

    @Override
    public List<Diagnostics> readIterable() throws InternalException {
        try {
            return diagnosticsRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Diagnostics> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<Diagnostics> readIterableByIdIn(List<String> ids) throws InternalException {
        try {
            return diagnosticsRepository.findAllByIdInAndState(ids, DiagnosticsConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Diagnostics> readIterableByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public List<Diagnostics> readIterableByCategoryId(String categoryId) throws InternalException {
        try {
            return diagnosticsRepository.findAllByCategoryIdAndState(categoryId, DiagnosticsConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Diagnostics> readIterableByCategoryId(String categoryId)" +
                    "-", e);
        }
    }

    @Override
    public Object readCategorizedDiagnosticsByIdIn(List<String> ids) throws InternalException {
        try {
            List<Object> data = new ArrayList<>();

            List<Diagnostics> diagnostics = diagnosticsRepository.findAllByIdInAndState(ids, DiagnosticsConstants.STATUS_ACTIVE);
            List<Category> diagnosticsCategories = categoryRepository.findAllByFilterAndState("diagnostics", DefaultConstant.STATUS_ACTIVE);

            diagnosticsCategories.forEach(category -> {
                List<Diagnostics> filteredDiagnostics = diagnostics.stream().filter(diagnostic -> diagnostic.getCategoryId().equals(category.getId())).collect(Collectors.toList());

                LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                content.put("category", category);
                content.put("diagnostics", filteredDiagnostics);

                data.add(content);
            });

            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Object readCategorizedDiagnosticsByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public Object readCategorizedDiagnostics() throws InternalException {
        try {
            List<Object> data = new ArrayList<>();

            List<Diagnostics> diagnostics = readIterable();
            List<Category> diagnosticsCategories = categoryRepository.findAllByFilterAndState("diagnostics", DefaultConstant.STATUS_ACTIVE);

            diagnosticsCategories.forEach(category -> {
                List<Diagnostics> filteredDiagnostics = diagnostics.stream().filter(diagnostic -> diagnostic.getCategoryId().equals(category.getId())).collect(Collectors.toList());

                LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                content.put("category", category);
                content.put("diagnostics", filteredDiagnostics);

                data.add(content);
            });

            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Object readCategorizedDiagnostics()" +
                    "-", e);
        }
    }

    @Override
    public Diagnostics readOne(String id) throws InternalException {
        try {
            return diagnosticsRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Diagnostics readOne(String id)" +
                    "-", e);
        }
    }

    @Override
    public Diagnostics create(Diagnostics diagnostics) throws InternalException {
        try {
            diagnostics.setState(DiagnosticsConstants.STATUS_ACTIVE);
            return diagnosticsRepository.save(diagnostics);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Diagnostics create(Diagnostics diagnostics)" +
                    "-", e);
        }
    }

    @Override
    public Diagnostics update(Diagnostics diagnostics) throws InternalException {
        try {
            diagnostics.setState(DiagnosticsConstants.STATUS_ACTIVE);
            return diagnosticsRepository.save(diagnostics);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Diagnostics update(Diagnostics diagnostics)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            Diagnostics diagnostics = diagnosticsRepository.getById(id);
            diagnostics.setState(DiagnosticsConstants.STATUS_DELETED);
            diagnosticsRepository.save(diagnostics);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }

    }

    @Override
    public List<Form> readFormsByDiagnosticsId(String diagnosticsId) throws InternalException {
        return null;
    }
}
