package kz.beeset.med.dmp.repository.dmpv2;

import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTEventComment;
import kz.beeset.med.dmp.repository.ResourceUtilRepository;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DMPV2MDTEventCommentRepository extends ResourceUtilRepository<DMPV2MDTEventComment, String> {
    List<DMPV2MDTEventComment> getAllByIdIn(List<String> idList) throws InternalException;
}
