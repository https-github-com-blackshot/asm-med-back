package kz.beeset.med.dmp.repository;

import kz.beeset.med.dmp.model.DMPPatientAppealComment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DMPPatientAppealCommentRepository extends ResourceUtilRepository<DMPPatientAppealComment, String> {

    List<DMPPatientAppealComment> findAllByDmpPatientAppealIdAndState(String dmpPatientAppealId, int state);

}
