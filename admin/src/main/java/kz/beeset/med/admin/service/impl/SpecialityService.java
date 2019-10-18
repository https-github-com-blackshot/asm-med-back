package kz.beeset.med.admin.service.impl;

import kz.beeset.med.admin.model.Speciality;
import kz.beeset.med.admin.repository.GuideRepository;
import kz.beeset.med.admin.repository.SpecialityRepository;
import kz.beeset.med.admin.service.ISpecialityService;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityService implements ISpecialityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuideService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    SpecialityRepository specialityRepository;

    @Override
    public List<Speciality> getAllSpecialities() throws InternalException {
        try {
            return specialityRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getAllSpecialities", e);
        }
    }

    @Override
    public Speciality getSpecialityById(String id) throws InternalException {
        try {
            return specialityRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:getSpecialityById", e);
        }
    }

    @Override
    public Speciality setSpeciality(Speciality guide) throws InternalException {
        try {
            return specialityRepository.save(guide);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:setSpeciality", e);
        }
    }
}
