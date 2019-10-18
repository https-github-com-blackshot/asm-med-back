package kz.beeset.med.gateway2.service.impl;

import kz.beeset.med.admin.model.Right;
import kz.beeset.med.admin.model.Role;
import kz.beeset.med.gateway2.repository.RightRepository;
import kz.beeset.med.gateway2.repository.RoleRepository;
import kz.beeset.med.gateway2.service.IRoleService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.error.InternalExceptionHelper;
import kz.beeset.med.gateway2.util.model.TreeDataFolderBased;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RoleService implements IRoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RightRepository rightRepository;


    @Override
    public List<Role> read() throws InternalException {
        try {
            return roleRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:read", e);
        }

    }

    @Override
    public List<Role> readByCode(String code) throws InternalException {
        try {
            return roleRepository.getAllByCode(code);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:readByCode", e);
        }
    }

    @Override
    public Role findRoleById(String id) throws InternalException {

        try {
            return roleRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:findRoleById", e);
        }
    }

    @Override
    public Role create(Role role) throws InternalException {

        try {
            return roleRepository.save(role);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:create", e);
        }
    }

    @Override
    public Role update(Role role) throws InternalException {
        try {
            Role roleNew = roleRepository.getById(role.getId());
            roleNew.setCode(role.getCode());
            roleNew.setDescription(role.getDescription());
            roleNew.setName(role.getName());
            roleNew.setRights(role.getRights());
            roleNew.setState(role.getState());
            return roleRepository.save(roleNew);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:update", e);
        }
    }

    @Override
    public List<TreeDataFolderBased> getTreeFolderBasedRigth(String roleId) throws InternalException {
        try {
            return getMapedChildsFolder(getRightMap(rightRepository.findAll()), "null", roleId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getTreeFolderBasedRigth", e);
        }
    }

    @Override
    public Role setRoleRights(List<String> rights, String roleId) throws InternalException {


        try {
            Role role = roleRepository.getById(roleId);
            role.setRights(rights);
            return roleRepository.save(role);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setRightNew", e);
        }
    }

    @Override
    public void deleteRoleById(String resourceId) throws InternalException {

        //TODO

    }

    private HashMap<String, List<Right>> getRightMap(List<Right> rightList) throws InternalException {
        try {

            HashMap<String, List<Right>> menuMap = new HashMap<>();

            String parent;
            for (Right right : rightList) {
                if (right.getParentId() == null) {
                    parent = "null";
                } else {
                    parent = right.getParentId();
                }
                if (menuMap.get(parent) == null) {
                    List<Right> childs = new ArrayList<>();
                    menuMap.put(parent, childs);
                }
                menuMap.get(parent).add(right);
            }

            return menuMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "HashMap<String, List<Right>> getResourceMap(List<Right> resourceList)" +
                    "-", e);
        }
    }

    private List<TreeDataFolderBased> getMapedChildsFolder(HashMap<String, List<Right>> rigthMap, String parent, String roleId)
            throws InternalException {
        try {


            List<TreeDataFolderBased> results = new ArrayList<>();

            Role role = roleRepository.getById(roleId);

            if (rigthMap.get(parent) != null) {

                for (Right resource : rigthMap.get(parent)) {
                    TreeDataFolderBased treeData = new TreeDataFolderBased();
                    treeData.setData(resource.getCode());
                    treeData.setLabel(resource.getNameRu());
                    treeData.setExpandedIcon("fa fa-folder-open");
                    if (role.getRights().contains(resource.getId()))
                        treeData.setPartialSelected(false);
                    else
                        treeData.setPartialSelected(null);
                    treeData.setId(resource.getId());
                    treeData.setParentId(resource.getParentId());
                    treeData.setCollapsedIcon("fa fa-folder");
                    results.add(treeData);
                    treeData.setChildren(getMapedChildsFolder(rigthMap, resource.getId(), roleId));

                }

            }

            return results;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<TreeDataFolderBased> getMapedChildsFolder(HashMap<String, List<Right>> rigthMap, String parent)" +
                    "-", e);
        }
    }
}
