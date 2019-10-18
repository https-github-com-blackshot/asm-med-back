package kz.beeset.med.admin.service;

import kz.beeset.med.admin.model.Speciality;
import kz.beeset.med.admin.utils.error.InternalException;

import java.util.List;

public interface ISpecialityService {

    List<Speciality> getAllSpecialities() throws InternalException;
    Speciality getSpecialityById(String id) throws InternalException;
    Speciality setSpeciality(Speciality guide) throws InternalException;
}
