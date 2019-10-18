package kz.beeset.med.admin.service;

import kz.beeset.med.admin.model.Right;
import kz.beeset.med.admin.model.Role;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.model.TreeDataFolderBased;

import java.util.List;

public interface IRoleService {
    List<Role> read() throws InternalException;
    List<Role> readByCode(String code) throws InternalException;
    List<Role> readByIdIn(List<String> ids) throws InternalException;
    Role findRoleById(String id) throws InternalException;
    Role create(Role role) throws InternalException;
    Role update(Role role) throws InternalException;
    List<TreeDataFolderBased> getTreeFolderBasedRigth(String roleId) throws InternalException;
    Role setRoleRights(List<String> rights, String roleId) throws InternalException;
    void deleteRoleById(String roleId) throws  InternalException;
    List<Right> getRigthListByRoleId(String roleId) throws InternalException;
    void  deleteRoleRight(String roleId,String rightId)throws InternalException;
}
