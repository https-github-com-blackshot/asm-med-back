package kz.beeset.med.gateway2.repository;

import kz.beeset.med.admin.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

    User findByIdn(String idn);

    User findBySignupToken(String token);

    User findUserById(String id);

    @Query(value = "{$or:[{username:'?0'},{idn:'?0'},{mobilePhone:'?0'},{email:'?0'}]}")
    User findUserByIdnOrMobilePhoneNumber(String idnMobilePhoneNumberEmail);

    @Query(value = "{$or:[{username:'?0'},{idn:'?0'},{mobilePhone:'?0'},{email:'?0'}]}")
    User getByUsernameOrIdnOrMobilePhoneOrEmail(String idnOrMobilePhoneOrUsernameOrEmail);
}
