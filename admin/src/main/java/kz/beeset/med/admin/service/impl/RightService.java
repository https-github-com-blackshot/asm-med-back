package kz.beeset.med.admin.service.impl;

import kz.beeset.med.admin.model.Resource;
import kz.beeset.med.admin.model.Right;
import kz.beeset.med.admin.model.Role;
import kz.beeset.med.admin.repository.ResourceRepository;
import kz.beeset.med.admin.repository.RightRepository;
import kz.beeset.med.admin.repository.RoleRepository;
import kz.beeset.med.admin.service.IRightService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
import kz.beeset.med.admin.utils.model.TreeData;
import kz.beeset.med.admin.utils.model.TreeDataFolderBased;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RightService implements IRightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RightService.class);

    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    RightRepository repository;
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Right> getAllRights() throws InternalException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllRights", e);
        }
    }

    @Override
    public Right getRightById(String id) throws InternalException {
        try {
            return repository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getRightById", e);
        }
    }

    @Override
    public List<Right> getAllByParentId(String parentId) throws InternalException {
        try {
            return repository.getAllByParentId(parentId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception: getAllByParentId", e);
        }
    }

    @Override
    public List<Right> setRightList(List<Right> list) throws InternalException {
        try {
            return repository.saveAll(list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setRightList", e);
        }
    }

    @Override
    public List<TreeData> getTreeBased() throws InternalException {
        try {
            return getMapedChilds(getRightMap(repository.findAll()), "null");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getTreeBased", e);
        }
    }

    @Override
    public Right setRight(Right right) throws InternalException {
        try {
            return repository.save(right);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setRight", e);
        }
    }

    @Override
    public List<TreeDataFolderBased> getTreeRightResources(String rightId) throws InternalException {
        try {
            return getMapedChildsFolder(getResourceMap(resourceRepository.findAll()), "null", rightId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getTreeFolderBased", e);
        }
    }

    @Override
    public Right setRightResources(List<String> resources, String rightId) throws InternalException {

        Right right = repository.getById(rightId);

        right.setResources(resources);

        try {
            return repository.save(right);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setRightResources", e);
        }
    }

    @Override
    public void deleteRightById(String rightId) throws InternalException {

        List<Role> rightRoles = roleRepository.getAllByRightsContains(rightId);

        if (rightRoles != null && !rightRoles.isEmpty()) {
            rightRoles.forEach(
                    (role) -> {
                        role.getRights()
                                .removeIf(res -> res.equals(rightId));
                        roleRepository.save(role);
                    });
        }

        repository.deleteById(rightId);

    }

    @Override
    public void deleteResourceInRight(String rightdId, String resId) throws InternalException {

        Right right = repository.getById(rightdId);

        right.getResources().removeIf(id -> id.equals(resId));

        repository.save(right);

    }

    @Override
    public List<Resource> getResourcesByRightId(String rigthId) throws InternalException {
        List<Resource> resources = new ArrayList<>();
        Right right = repository.getById(rigthId);
        for (int i = 0; i < right.getResources().size(); i++) {
            resources.add(resourceRepository.getById(right.getResources().get(i)));
        }
        return resources;
    }


    private HashMap<String, List<Resource>> getResourceMap(List<Resource> resourceList) {

        HashMap<String, List<Resource>> resourceMap = new HashMap<>();

        String parentStr;

        for (Resource resource : resourceList) {
            // проверяем имеет ли родителя, если не имеет, то родителя указываем как "null"
            if (resource.getParentId() == null) {
                parentStr = "null";
            } else {
                parentStr = resource.getParentId();
            }
            // проверяем есть ли уже такой родитель в мапе, если нет, то добавляем в мап этого родителя
            if (resourceMap.get(parentStr) == null) {
                List<Resource> childs = new ArrayList<>();
                resourceMap.put(parentStr, childs);
            }
            // раскидываем по родителям, добавляем к списку непосредственного родителя, затем из этого мапа будем строить дерево
            resourceMap.get(parentStr).add(resource);
        }

        return resourceMap;

    }

    private List<TreeData> getMapedChilds(HashMap<String, List<Right>> rightMap, String parent) {

        List<TreeData> results = new ArrayList<>();

        if (rightMap.get(parent) != null) {

            for (Right right : rightMap.get(parent)) {

                TreeData treeData = new TreeData();

                treeData.setData(right);

                results.add(treeData);

                treeData.setChildren(getMapedChilds(rightMap, right.getId()));

            }

        }

        return results;
    }

    private HashMap<String, List<Right>> getRightMap(List<Right> rightList) {

        HashMap<String, List<Right>> menuMap = new HashMap<>();

        String parentStr;

        for (Right right : rightList) {
            // проверяем имеет ли родителя, если не имеет, то родителя указываем как "null"
            if (right.getParentId() == null) {
                parentStr = "null";
            } else {
                parentStr = right.getParentId();
            }
            // проверяем есть ли уже такой родитель в мапе, если нет, то добавляем в мап этого родителя
            if (menuMap.get(parentStr) == null) {
                List<Right> childs = new ArrayList<>();
                menuMap.put(parentStr, childs);
            }
            // раскидываем по родителям, добавляем к списку непосредственного родителя, затем из этого мапа будем строить дерево
            menuMap.get(parentStr).add(right);
        }

        return menuMap;

    }

    private List<TreeDataFolderBased> getMapedChildsFolder(HashMap<String, List<Resource>> resourceMap, String parent, String rightId) {

        List<TreeDataFolderBased> results = new ArrayList<>();
        Right right = repository.getById(rightId);


        if (resourceMap.get(parent) != null) {

            for (Resource resource : resourceMap.get(parent)) {
                TreeDataFolderBased treeData = new TreeDataFolderBased();
                treeData.setData(resource.getResource());
                treeData.setLabel(resource.getDescription());
                treeData.setId(resource.getId());
                treeData.setParentId(resource.getParentId());
                treeData.setCollapsedIcon("fa fa-folder");
                treeData.setExpandedIcon("fa fa-folder-open");
                if (right.getResources().contains(resource.getId()))
                    treeData.setPartialSelected(false);
                else
                    treeData.setPartialSelected(null);
                results.add(treeData);
                treeData.setChildren(getMapedChildsFolder(resourceMap, resource.getId(), rightId));

            }

        }

        return results;
    }
}
