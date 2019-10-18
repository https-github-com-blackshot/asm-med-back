package kz.beeset.med.admin.service;

import kz.beeset.med.admin.constant.ModuleTypeObject;
import kz.beeset.med.admin.model.Resource;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.model.TreeData;
import kz.beeset.med.admin.utils.model.TreeDataFolderBased;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Locale;

public interface IResourceService {
    List<Resource> getAllResources(List<String> types) throws InternalException;
    List<Resource> setResourceList(List<Resource> list) throws InternalException;
    Resource getResourceById(String id) throws InternalException;
    List<Resource> getAllByParentId(String parentId) throws InternalException;
    List<TreeData> getTreeBased() throws InternalException;
    List<TreeDataFolderBased> getTreeFolderBased() throws InternalException;
    Resource setResourse(Resource resource) throws InternalException;
    void deleteResourceById(String resourceId) throws  InternalException;
    List<String> getResourceTypeList()  throws  InternalException;
    List<ModuleTypeObject> getModuleType()  throws  InternalException;
    Page<Resource> read(Query query, Pageable pageableRequest) throws DataAccessException, InternalException;
    List<?> readByTree(Query query, String type, Locale locale, String token) throws InternalException;
}
