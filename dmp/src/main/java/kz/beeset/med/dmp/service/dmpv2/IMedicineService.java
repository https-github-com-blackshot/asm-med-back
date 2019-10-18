package kz.beeset.med.dmp.service.dmpv2;


import kz.beeset.med.dmp.model.dmpv2.Medicine;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface    IMedicineService {

    Page<Medicine> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<Medicine> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<Medicine> readIterable() throws InternalException;
    List<Medicine> readIterableByCategoryId(String categoryId) throws InternalException;
    List<Medicine> readIterableByIdIn(List<String> ids) throws InternalException;
    Medicine readOne(String id) throws InternalException;
    Medicine create(Medicine medicine) throws InternalException;
    Medicine update(Medicine medicine) throws InternalException;
    void delete(String id) throws InternalException;



}

