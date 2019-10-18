package kz.beeset.med.admin.service.impl;

import com.google.gson.Gson;
import kz.beeset.med.admin.constant.ModuleTypeConstant;
import kz.beeset.med.admin.constant.ModuleTypeObject;
import kz.beeset.med.admin.constant.ResourceConstant;
import kz.beeset.med.admin.model.*;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.admin.repository.ResourceRepository;
import kz.beeset.med.admin.repository.RightRepository;
import kz.beeset.med.admin.repository.SessionRepository;
import kz.beeset.med.admin.service.IResourceService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
import kz.beeset.med.admin.utils.model.MenuTreeDataDTO;
import kz.beeset.med.admin.utils.model.TreeData;
import kz.beeset.med.admin.utils.model.TreeDataFolderBased;
import kz.beeset.med.admin.utils.model.WidgetControllerTreeDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceService implements IResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    RightRepository rightRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoleService roleService;

    public ResourceService() {
    }

    @Override
    public List<?> readByTree(Query query, String type, Locale locale, String token) throws InternalException {
        try {
            if (type != null) {
                if (type.equals(ResourceConstant.TYPE_MENU)) {
                    if (token != null) {
                        Session session = this.sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);
                        List<UserRoleOrgMap> userRoleOrgMapList = usersService.get(session.getUser().getId()).getUserRoleOrgMapList();
                        String selectedOrganizationId = session.getSelectedOrganizationId();
//                        System.out.println("User role map org list: " + new Gson().toJson(userRoleOrgMapList));
//                        System.out.println("session.getSelectedOrganizationId() = " + selectedOrganizationId);

                        if (selectedOrganizationId != null) {
                            Optional<UserRoleOrgMap> matchingObject = userRoleOrgMapList.stream()
                                    .filter(userRoleOrgMap -> selectedOrganizationId.equals(userRoleOrgMap.getOrgId()))
                                    .findFirst();
                            UserRoleOrgMap currentOrgRoleMap = matchingObject.orElse(null);

                            if (currentOrgRoleMap != null) {
//                                System.out.println("currentOrgRoleMap.getRoles() = " + currentOrgRoleMap.getRoles());
                                List<Role> roles = roleService.readByIdIn(currentOrgRoleMap.getRoles());
                                List<String> rightIds = roles.stream().flatMap(e -> e.getRights().stream()).collect(Collectors.toList());
//                                System.out.println("rightIds = " + rightIds);
                                List<Right> rights = rightRepository.getAllByIdIn(rightIds);
                                List<String> resourceIds = rights.stream().flatMap(e -> e.getResources().stream()).collect(Collectors.toList());
//                                System.out.println("resourceIds = " + resourceIds);

                                Query query1 = new Query();
                                query1.addCriteria(new Criteria().orOperator(Criteria.where("id").in(resourceIds), Criteria.where("parentId").in(resourceIds)));
                                query1.addCriteria(Criteria.where("type").is(type));
                                List<Resource> availableMenuResources = resourceRepository.findAll(query1);
//
//                                System.out.println("============================== new Gson().toJson(availableMenuResources) = "
//                                        + new Gson().toJson(availableMenuResources));
//                            List<Resource> availableMenuResources = resourceRepository.findAll(query);

                                return getMappedMenuChildren(getResourceMap(availableMenuResources), "null", locale);
                            }
                        }
                    }
                } else if (type.equals(ResourceConstant.TYPE_CONTROLLER) || type.equals(ResourceConstant.TYPE_WIDGET)) {
                    return getMappedWidgetControllerChildren(getResourceMap(resourceRepository.findAll(query)), "null", locale);
                }
            }

            return new ArrayList<>();
        } catch (InternalException ie) {
            LOGGER.error(ie.getMessage(), ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:readByTree", e);
        }
    }

    private List<MenuTreeDataDTO> getMappedMenuChildren(HashMap<String, List<Resource>> resourceMap, String parent, Locale locale) throws InternalException {
        try {
            List<MenuTreeDataDTO> results = new ArrayList<>();

            if (resourceMap.get(parent) != null) {

                for (Resource resource : resourceMap.get(parent)) {

                    String id = resource.getCode();
                    String url = resource.getResource();
                    //TODO надо сделать
                    String title = resource.getDescriptionEn();
                    if (locale.getLanguage().equals("ru")) {
                        title = resource.getDescription();
                    }
                    if (locale.getLanguage().equals("kk")) {
                        title = resource.getDescriptionKz();
                    }

                    String translate = resource.getDescription();
                    String icon = resource.getIcon();
                    List<MenuTreeDataDTO> children = getMappedMenuChildren(resourceMap, resource.getId(), locale);
                    String type = "item";
                    if (!children.isEmpty())
                        type = "collapsable";

                    MenuTreeDataDTO treeData = new MenuTreeDataDTO();
                    treeData.setId(id);
                    treeData.setTitle(title);
                    treeData.setTranslate(translate);
                    treeData.setIcon(icon);
                    treeData.setType(type);
                    treeData.setUrl(url);
                    if (!children.isEmpty())
                        treeData.setChildren(children);
                    results.add(treeData);
                }
            }

            return results;
        } catch (InternalException ie) {
            LOGGER.error(ie.getMessage(), ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getMappedMenuChildren", e);
        }
    }

    private List<WidgetControllerTreeDataDTO> getMappedWidgetControllerChildren(HashMap<String, List<Resource>> resourceMap,
                                                                                String parent, Locale locale) throws InternalException {
        try {
            List<WidgetControllerTreeDataDTO> results = new ArrayList<>();

            if (resourceMap.get(parent) != null) {

                for (Resource resource : resourceMap.get(parent)) {

                    WidgetControllerTreeDataDTO treeData = new WidgetControllerTreeDataDTO();
                    treeData.setData(resource);
                    List<WidgetControllerTreeDataDTO> children = getMappedWidgetControllerChildren(resourceMap, resource.getId(), locale);
                    treeData.setChildren(children);
                    results.add(treeData);
                }
            }

            return results;
        } catch (InternalException ie) {
            LOGGER.error(ie.getMessage(), ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getMappedWidgetControllerChildren", e);
        }
    }

    public Page<Resource> read(Query query, Pageable pageableRequest) throws InternalException {
        try {
            return resourceRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<Resource> read(Query query, Pageable pageableRequest)" +
                    "-", e);
        }
    }

    @Override
    public List<Resource> getAllResources(List<String> types) throws InternalException {
        try {
            return types != null && !types.isEmpty()
                    ? resourceRepository.findAll(new Query().addCriteria(Criteria.where(ResourceConstant.TYPE_FIELD_NAME).in(types)))
                    : resourceRepository.findAll();
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

    @Override
    public void deleteResourceById(String resourceId) throws InternalException {
        List<Right> resourceRights = rightRepository.getAllByResourcesContains(resourceId);

        if (resourceRights != null && !resourceRights.isEmpty()) {
            resourceRights.forEach(
                    (right) -> {
                        right.getResources()
                                .removeIf(res -> res.equals(resourceId));
                        rightRepository.save(right);
                    });
        }
        resourceRepository.deleteById(resourceId);
    }

    @Override
    public List<String> getResourceTypeList() throws InternalException {
        try {
            List<String> list = new ArrayList<>();
            list.add(ResourceConstant.TYPE_CONTROLLER);
            list.add(ResourceConstant.TYPE_WIDGET);
            list.add(ResourceConstant.TYPE_MENU);
            return list;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getResourceTypeList", e);
        }
    }

    @Override
    public List<ModuleTypeObject> getModuleType() throws InternalException {
        try {
            List<ModuleTypeObject> list = new ArrayList<>();
            list.add(ModuleTypeConstant.ADMIN);
            list.add(ModuleTypeConstant.CONSTRUCTOR);
            list.add(ModuleTypeConstant.KI);
            list.add(ModuleTypeConstant.REPORT);
            return list;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getResourceTypeList", e);
        }
    }


    private List<TreeDataFolderBased> getMapedChildsFolder(HashMap<String, List<Resource>> resourceMap, String parent) throws InternalException {
        try {
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
        } catch (InternalException ie) {
            LOGGER.error(ie.getMessage(), ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getMappedWidgetControllerChildren", e);
        }
    }

    private List<TreeData> getMapedChilds(HashMap<String, List<Resource>> resourceMap, String parent) throws InternalException {
        try {
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
        } catch (InternalException ie) {
            LOGGER.error(ie.getMessage(), ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getMapedChilds", e);
        }
    }

    private HashMap<String, List<Resource>> getResourceMap(List<Resource> resourceList) throws InternalException {
        try {
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
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getResourceMap", e);
        }
    }
}
