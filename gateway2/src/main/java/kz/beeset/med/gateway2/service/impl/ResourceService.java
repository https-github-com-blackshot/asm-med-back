package kz.beeset.med.gateway2.service.impl;

import kz.beeset.med.admin.model.Resource;
import kz.beeset.med.gateway2.repository.ResourceRepository;
import kz.beeset.med.gateway2.service.IResourceService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.error.InternalExceptionHelper;
import kz.beeset.med.gateway2.util.model.TreeData;
import kz.beeset.med.gateway2.util.model.TreeDataFolderBased;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ResourceService implements IResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public List<Resource> getAllResources() throws InternalException {
        try {
            return resourceRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllResources", e);
        }
    }

    @Override
    public List<Resource> setResourceList(List<Resource> list) throws InternalException {
        try {
            return resourceRepository.saveAll(list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setResourceList", e);
        }
    }

    @Override
    public Resource getResourceById(String id) throws InternalException {
        try {
            return resourceRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllResources", e);
        }
    }

    @Override
    public List<Resource> getAllByParentId(String parentId) throws InternalException {
        try {
            return resourceRepository.getAllByParentId(parentId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllByParentId", e);
        }
    }

    @Override
    public List<TreeData> getTreeBased() throws InternalException {
        try {
            return getMapedChilds(getResourceMap(resourceRepository.findAll()), "null");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getTreeBased", e);
        }
    }

    @Override
    public List<TreeDataFolderBased> getTreeFolderBased() throws InternalException {
        try {
            return getMapedChildsFolder(getResourceMap(resourceRepository.findAll()), "null");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getTreeFolderBased", e);
        }
    }

    @Override
    public Resource setResourse(Resource resource) throws InternalException {
        try {
            return resourceRepository.save(resource);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setResourse", e);
        }
    }


    private List<TreeDataFolderBased> getMapedChildsFolder(HashMap<String, List<Resource>> resourceMap, String parent) {

        List<TreeDataFolderBased> results = new ArrayList<>();

        if (resourceMap.get(parent) != null) {

            for (Resource resource : resourceMap.get(parent)) {
                TreeDataFolderBased treeData = new TreeDataFolderBased();
                treeData.setData(resource.getCode());
                treeData.setLabel(resource.getDescription());
                treeData.setId(resource.getId());
                treeData.setParentId(resource.getParentId());
                treeData.setCollapsedIcon("fa fa-folder");
                treeData.setExpandedIcon("fa fa-folder-open");
                treeData.setPartialSelected(null);
                results.add(treeData);
                treeData.setChildren(getMapedChildsFolder(resourceMap, resource.getId()));

            }

        }

        return results;
    }

    private List<TreeData> getMapedChilds(HashMap<String, List<Resource>> resourceMap, String parent) {

        List<TreeData> results = new ArrayList<>();

        if (resourceMap.get(parent) != null) {

            for (Resource resource : resourceMap.get(parent)) {

                TreeData treeData = new TreeData();

                treeData.setData(resource);

                results.add(treeData);

                treeData.setChildren(getMapedChilds(resourceMap, resource.getId()));

            }

        }

        return results;
    }

    private HashMap<String, List<Resource>> getResourceMap(List<Resource> resourceList) {

        HashMap<String, List<Resource>> menuMap = new HashMap<>();

        String parentStr;

        for (Resource resource : resourceList) {
            // проверяем имеет ли родителя, если не имеет, то родителя указываем как "null"
            if (resource.getParentId() == null) {
                parentStr = "null";
            } else {
                parentStr = resource.getParentId();
            }
            // проверяем есть ли уже такой родитель в мапе, если нет, то добавляем в мап этого родителя
            if (menuMap.get(parentStr) == null) {
                List<Resource> childs = new ArrayList<>();
                menuMap.put(parentStr, childs);
            }
            // раскидываем по родителям, добавляем к списку непосредственного родителя, затем из этого мапа будем строить дерево
            menuMap.get(parentStr).add(resource);
        }

        return menuMap;

    }
}
