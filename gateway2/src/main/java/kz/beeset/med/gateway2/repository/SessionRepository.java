package kz.beeset.med.gateway2.repository;

import kz.beeset.med.admin.model.Session;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<Session, String> {
    Session findByToken(String token);
    Session findFirstByTokenOrderByTokenCreateDateDesc(String token);
    Session findFirstByUser_IdOrderByTokenCreateDateDesc(ObjectId userId);
}
