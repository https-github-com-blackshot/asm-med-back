package kz.beeset.med.dmp.service.dmpv2;


import kz.beeset.med.dmp.model.dmpv2.Category;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ICategoryService {

    Page<Category> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Page<Category> searchPageable(Map<String, String> allRequestParams) throws InternalException;
    List<Category> readIterable() throws InternalException;
    List<Category> readIterableByFilter(String filter) throws InternalException;
    Category readOne(String id) throws InternalException;
    Category create(Category category) throws InternalException;
    Category update(Category category) throws InternalException;
    void delete(String id) throws InternalException;

}
