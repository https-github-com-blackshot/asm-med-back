package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.Guide;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends ResourceUtilRepository<Guide, String> {
    Guide getById(String id);
}
