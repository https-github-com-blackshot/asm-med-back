package kz.beeset.med.gateway2.service;



import kz.beeset.med.admin.model.Role;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.model.TreeDataFolderBased;

import java.util.List;

public interface IRoleService {
    List<Role> read() throws InternalException;
    List<Role> readByCode(String code) throws InternalException;
    Role findRoleById(String id) throws InternalException;
    Role create(Role role) throws InternalException;
    Role update(Role role) throws InternalException;
    List<TreeDataFolderBased> getTreeFolderBasedRigth(String roleId) throws InternalException;
    Role setRoleRights(List<String> rights, String roleId) throws InternalException;
    void deleteRoleById(String resourceId) throws  InternalException;
}
