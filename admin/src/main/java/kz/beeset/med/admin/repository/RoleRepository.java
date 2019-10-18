package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.Role;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoleRepository extends ResourceUtilRepository<Role, String> {

    Role getById(String id);

    List<Role> getAllByCode(String code);

    List<Role> getAllByRightsContains(String rightId);

    List<Role> getAllByIdIn(List<String> roleIds);
}
