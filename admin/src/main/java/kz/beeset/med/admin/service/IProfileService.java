package kz.beeset.med.admin.service;

import kz.beeset.med.admin.model.Profile;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.utils.error.InternalException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IProfileService {
    Page<Profile> readProfilesPageable(Map<String, String> allRequestParams) throws InternalException;
    Profile getById(String id) throws InternalException;
    Profile getByUserId(String userId) throws InternalException;
    List<Profile> readIterable() throws InternalException;
    Page<Profile> readPageable(Map<String, String> allRequestParams) throws InternalException;
    Profile createProfile(Profile profile) throws InternalException;
    void removeProfileById(String id) throws InternalException;
}