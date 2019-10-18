package kz.beeset.med.dmp.repository.admin;

import kz.beeset.med.admin.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    Session findFirstByTokenOrderByTokenCreateDateDesc(String token);
}
