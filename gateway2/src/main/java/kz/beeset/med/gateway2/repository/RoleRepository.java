package kz.beeset.med.gateway2.repository;

import kz.beeset.med.admin.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role getById(String id);
    List<Role> getAllByCode(String code);
}
