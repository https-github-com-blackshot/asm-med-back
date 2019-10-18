package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPMDTEvent;
import kz.beeset.med.dmp.model.DMPMDTEventComment;
import kz.beeset.med.dmp.model.custom.DMPMDTEventCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDMPMDTEventService {

    Page<DMPMDTEvent> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPMDTEventCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPMDTEvent> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPMDTEvent> readIterableByDMPId(String dmpId) throws InternalException;
    DMPMDTEvent get(String id) throws InternalException;
    List<DMPMDTEvent> getByUserId(String id) throws InternalException;
    DMPMDTEvent create(DMPMDTEvent dmpmdtEvent) throws InternalException;
    DMPMDTEvent update(DMPMDTEvent dmpmdtEvent) throws InternalException;
    DMPMDTEvent delete(String id) throws InternalException;
    DMPMDTEvent updateEventAddingCommentId(String eventId, String uploadCode, String commentId) throws InternalException;
    DMPMDTEvent updateEventAcceptByDoctor(String eventId, String userId) throws InternalException;
    DMPMDTEvent updateEventDeclineByDoctor(String eventId, String userId) throws InternalException;
    void deleteFiles(List<String> idList) throws InternalException;

    GridFsResource downloadFile(String fileId) throws InternalException;
    String saveFile(InputStream content, String mimeType, String fileName) throws IOException, InternalException;
    void deleteDocById(String id) throws InternalException;

    DMPMDTEventComment createComment(DMPMDTEventComment dmpmdtEventComment) throws InternalException;
    List<DMPMDTEventComment> getCommentListByEventId(String eventId) throws InternalException;
}
