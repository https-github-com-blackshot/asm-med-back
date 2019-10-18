package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.UserNotification;

import java.util.List;

public interface UserNotificationRepository extends ResourceUtilRepository<UserNotification, String> {

    UserNotification getById(String id);
    List<UserNotification> getAllByUserIdAndState(String userId, int state);
}
