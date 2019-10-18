package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.Right;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RightRepository extends ResourceUtilRepository<Right, String> {
    Right getById(String id);
    List<Right> getAllByParentId(String parentId);
    List<Right> getByResources(String resourceId);
    List<Right> getAllByIdIn(List<String> rightIds);
    //resources
    List<Right> getAllByResourcesContains(String resourceId);
}
