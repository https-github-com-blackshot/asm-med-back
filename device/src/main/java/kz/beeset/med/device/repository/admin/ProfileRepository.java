package kz.beeset.med.device.repository.admin;


import kz.beeset.med.admin.model.Profile;
import kz.beeset.med.device.repository.ResourceUtilRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends ResourceUtilRepository<Profile, String> {

    Page<Profile> findAllByDoctorsContainsAndState(String doctorUserId, int state, Pageable pageable);
    Page<Profile> findAll(Query query, Pageable pageable);

    List<Profile> findAllByState(int state);
    List<Profile> findAll(final Query query);

    Profile getByIdAndState(String id, int state);
    Profile getByUserIdAndState(String userId, int state);
}
