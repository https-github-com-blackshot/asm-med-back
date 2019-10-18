package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.Tutorial;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialRepository extends ResourceUtilRepository<Tutorial, String> {

    @Query("{id:'?0'}")
    Tutorial getById(String id);

}
