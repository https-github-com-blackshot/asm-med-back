package kz.beeset.med.gateway2.service;

import kz.beeset.med.admin.model.Right;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.model.TreeData;
import kz.beeset.med.gateway2.util.model.TreeDataFolderBased;

import java.util.List;

public interface IRightService {
    List<Right> getAllRights() throws InternalException;
    Right getRightById(String id) throws InternalException;
    List<Right> getAllByParentId(String parentId) throws InternalException;
    List<Right> setRightList(List<Right> list) throws InternalException;
    List<TreeData> getTreeBased() throws InternalException;
    Right setRight(Right right) throws InternalException;
    List<TreeDataFolderBased> getTreeRightResources(String rightId) throws InternalException;
    Right setRightResources(List<String> resources, String rightId) throws InternalException;
    void deleteRightById(String rightId) throws  InternalException;
}
