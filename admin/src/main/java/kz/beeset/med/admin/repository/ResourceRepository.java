package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.Resource;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends ResourceUtilRepository<Resource, String> {
    Resource getById(String id);
    List<Resource> getAllByParentId(String parentId);
//    @Query("{'type':?1, '$or':[ {'id':?0}, {'parentId':?0} ]}")
    @Query("{'$or':[ {'id':?0} ], 'type':?1}")
    List<Resource> getAllByIdInAndType(List<String> ids, String type);
}
