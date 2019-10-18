package kz.beeset.med.gateway2.repository;


import kz.beeset.med.admin.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {
    Resource getById(String id);
    List<Resource> getAllByParentId(String parentId);
}
