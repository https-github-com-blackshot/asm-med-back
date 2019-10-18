package kz.beeset.med.admin.repository;


import kz.beeset.med.admin.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends ResourceUtilRepository<Profile, String> {
    Profile getByIdAndState(String id, int state);
    Profile getByUserIdAndState(String id, int state);
    List<Profile> findAllByState(int state);

    Page<Profile> findAllByDoctorsContainsAndState(String doctorUserId, int state, Pageable pageable);

    Page<Profile> findAll(Query query, Pageable pageable);

    List<Profile> findAll(final Query query);

}
