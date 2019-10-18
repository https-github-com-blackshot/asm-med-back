package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.Speciality;

public interface SpecialityRepository extends ResourceUtilRepository<Speciality, String> {
    Speciality getById(String id);
}
