package kz.beeset.med.gateway2.repository;

import kz.beeset.med.gateway2.model.EmailText;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailTextRepository extends MongoRepository<EmailText, String> {
    EmailText findByCode(String code);
}
