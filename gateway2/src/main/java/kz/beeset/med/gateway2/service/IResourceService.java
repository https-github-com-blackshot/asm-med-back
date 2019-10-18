package kz.beeset.med.gateway2.service;



import kz.beeset.med.admin.model.Resource;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.model.TreeData;
import kz.beeset.med.gateway2.util.model.TreeDataFolderBased;

import java.util.List;

public interface IResourceService {
    List<Resource> getAllResources() throws InternalException;
    List<Resource> setResourceList(List<Resource> list) throws InternalException;
    Resource getResourceById(String id) throws InternalException;
    List<Resource> getAllByParentId(String parentId) throws InternalException;
    List<TreeData> getTreeBased() throws InternalException;
    List<TreeDataFolderBased> getTreeFolderBased() throws InternalException;
    Resource setResourse(Resource resource) throws InternalException;
}
