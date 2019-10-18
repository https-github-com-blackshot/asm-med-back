package kz.beeset.med.gateway2.repository;


import kz.beeset.med.admin.model.Right;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RightRepository extends MongoRepository<Right, String> {
    Right getById(String id);
    List<Right> getAllByParentId(String parentId);
}
