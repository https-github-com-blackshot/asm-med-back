package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.custom.DMPV2MDTEventCustom;
import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTEvent;
import kz.beeset.med.dmp.model.dmpv2.DMPV2MDTEventComment;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IDMPV2MDTEventService {
    Page<DMPV2MDTEvent> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPV2MDTEventCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPV2MDTEvent> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPV2MDTEvent> readIterableByDiseaseId(String dmpId) throws InternalException;
    DMPV2MDTEvent get(String id) throws InternalException;
    List<DMPV2MDTEvent> getByUserId(String id) throws InternalException;
    DMPV2MDTEvent create(DMPV2MDTEvent DMPV2MDTEvent) throws InternalException;
    DMPV2MDTEvent update(DMPV2MDTEvent DMPV2MDTEvent) throws InternalException;
    DMPV2MDTEvent delete(String id) throws InternalException;
    DMPV2MDTEvent updateEventAddingCommentId(String eventId, String uploadCode, String commentId) throws InternalException;
    DMPV2MDTEvent updateEventAcceptByDoctor(String eventId, String userId) throws InternalException;
    DMPV2MDTEvent updateEventDeclineByDoctor(String eventId, String userId) throws InternalException;
    void deleteFiles(List<String> idList) throws InternalException;

    GridFsResource downloadFile(String fileId) throws InternalException;
    String saveFile(InputStream content, String mimeType, String fileName) throws IOException, InternalException;
    void deleteDocById(String id) throws InternalException;

    DMPV2MDTEventComment createComment(DMPV2MDTEventComment dmpmdtEventComment) throws InternalException;
    List<DMPV2MDTEventComment> getCommentListByEventId(String eventId) throws InternalException;
}
