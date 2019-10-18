package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.constant.DMPV2.MedicineConstants;
import kz.beeset.med.dmp.model.dmpv2.Medicine;
import kz.beeset.med.dmp.repository.dmpv2.MedicineRepository;
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
public class MedicineService implements IMedicineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());


    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public Page<Medicine> readPageable(Map<String, String> allRequestParams) throws InternalException {
        try {
            Query query = new Query();

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = MedicineConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = MedicineConstants.DEFAUT_PAGE_SIZE;

            String sortBy = MedicineConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("id")) {
                query.addCriteria(Criteria.where(MedicineConstants.ID_FIELD_NAME).is(allRequestParams.get("id")));
            }
            if (allRequestParams.containsKey("code")) {
                query.addCriteria(Criteria.where(MedicineConstants.CODE_FIELD_NAME).is(allRequestParams.get("code")));
            }
            if (allRequestParams.containsKey("name")) {
                query.addCriteria(Criteria.where(MedicineConstants.NAME_FIELD_NAME).is(allRequestParams.get("name")));
            }
            if (allRequestParams.containsKey("description")) {
                query.addCriteria(Criteria.where(MedicineConstants.DESCRIPTION_FIELD_NAME).is(allRequestParams.get("description")));
            }
            if (allRequestParams.containsKey("categoryId")) {
                query.addCriteria(Criteria.where(MedicineConstants.CATEGORY_ID_FIELD_NAME).is(allRequestParams.get("categoryId")));
            }
            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(MedicineConstants.SORT_DIRECTION_DESC))
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

            query.addCriteria(Criteria.where(MedicineConstants.STATE_FIELD_NAME).is(MedicineConstants.STATUS_ACTIVE));

            final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, new Sort(sortDirection, sortBy));

            return medicineRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    " Page<Medicine> readPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public Page<Medicine> searchPageable(Map<String, String> allRequestParams) throws InternalException {
        try {

            Sort.Direction sortDirection = Sort.Direction.ASC;

            int pageNumber = MedicineConstants.DEFAUT_PAGE_NUMBER;

            int pageSize = MedicineConstants.DEFAUT_PAGE_SIZE;

            String sortBy = MedicineConstants.ID_FIELD_NAME;

            if (allRequestParams.containsKey("sortDirection")) {

                if (allRequestParams.get("sortDirection").equals(MedicineConstants.SORT_DIRECTION_DESC))
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

            return medicineRepository.query(allRequestParams.get("searchString"), pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Medicine> searchPageable(Map<String, String> allRequestParams)" +
                    "-", e);
        }
    }

    @Override
    public List<Medicine> readIterable() throws InternalException {
        try {
            return medicineRepository.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Medicine> readIterable()" +
                    "-", e);
        }
    }

    @Override
    public List<Medicine> readIterableByCategoryId(String categoryId) throws InternalException {
        try {
            return medicineRepository.findAllByCategoryIdAndState(categoryId, MedicineConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Medicine> readIterableByCategoryId(String categoryId)" +
                    "-", e);
        }
    }

    @Override
    public List<Medicine> readIterableByIdIn(List<String> ids) throws InternalException {
        try {
            return medicineRepository.findAllByIdInAndState(ids, MedicineConstants.STATUS_ACTIVE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Medicine> readIterableByIdIn(List<String> ids)" +
                    "-", e);
        }
    }

    @Override
    public Medicine readOne(String id) throws InternalException {
        try {
            return medicineRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Medicine readOne(String id)" +
                    "-", e);
        }
    }

    @Override
    public Medicine create(Medicine medicine) throws InternalException {
        try {
            medicine.setState(MedicineConstants.STATUS_ACTIVE);
            return medicineRepository.save(medicine);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Medicine create(Medicine medicine)" +
                    "-", e);
        }
    }

    @Override
    public Medicine update(Medicine medicine) throws InternalException {
        try {
            medicine.setState(MedicineConstants.STATUS_ACTIVE);
            return medicineRepository.save(medicine);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Medicine update(Medicine medicine)" +
                    "-", e);
        }
    }

    @Override
    public void delete(String id) throws InternalException {
        try {
            Medicine medicine = medicineRepository.getById(id);
            medicine.setState(MedicineConstants.STATUS_DELETED);
            medicineRepository.save(medicine);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(String id)" +
                    "-", e);
        }

    }
}
