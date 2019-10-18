package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.model.DMPMDTEventComment;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPMDTEventCommentRepository extends ResourceUtilRepository<DMPMDTEventComment, String> {
    List<DMPMDTEventComment> getAllByIdIn(List<String> idList) throws InternalException;
}
