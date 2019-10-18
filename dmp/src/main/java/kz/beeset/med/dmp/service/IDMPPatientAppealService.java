package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMP;
import kz.beeset.med.dmp.model.DMPPatientAppeal;
import kz.beeset.med.dmp.model.DMPPatientAppealComment;
import kz.beeset.med.dmp.model.common.CommentAndAttachmentInfo;
import kz.beeset.med.dmp.model.common.CommentAttachment;
import kz.beeset.med.dmp.model.custom.DMPPatientAppealCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IDMPPatientAppealService {

    List<DMPPatientAppeal> getAllDMPPatientAppealList(Query query) throws InternalException;
    List<DMPPatientAppeal> getAllByStatusAndDMPDoctorId(String status, String dmpDoctorId) throws InternalException;
    List<DMPPatientAppeal> getAllByStatus(String status) throws InternalException;
    List<DMPPatientAppeal> getAllByStatusAndDMPDoctorAndDMP(String status, String dmpDoctorId, String dmpId) throws InternalException;
    Page<DMPPatientAppeal> getAllByDMPDoctor(String dmpDoctorId, String dmpId, Map<String, String> allRequestParams) throws InternalException;
    Page<DMPPatientAppealCustom> getAllCustomByDMPDoctor(String dmpDoctorId, String dmpId, Map<String, String> allRequestParams) throws InternalException;
    Page<DMPPatientAppealCustom> searchCustomByDMPDoctor(String dmpDoctorId, String dmpId, Map<String, String> allRequestParams) throws InternalException;

    List<DMPPatientAppeal> getAllByStatusAndDMPId(String status, String dmpId) throws InternalException;
    DMPPatientAppeal getById(String id) throws InternalException;
    void deleteById(String id) throws InternalException;

    List<String> create(DMPPatientAppeal appeal) throws InternalException;

    List<DMPPatientAppeal> createAppeals(List<DMPPatientAppeal> appealList) throws InternalException;
    DMPPatientAppeal update(DMPPatientAppeal appeal) throws InternalException;
    DMPPatientAppeal updateStatus(String appealId, String status) throws InternalException;

    Object getStatusList() throws InternalException;
    Object getEventTypeList() throws InternalException;

    DMPPatientAppealComment createDMPatientAppealComment(DMPPatientAppealComment appealComment) throws InternalException;
    List<DMPPatientAppealComment> getDMPPatientAppealCommentsByAppealId(String appealId) throws InternalException;
//    List<CommentAttachment> getDMPPatientAppealCommentAttachmentList(String commentId) throws InternalException;
//    List<CommentAndAttachmentInfo> getDMPPatientAppealCommentAttachmentInfo(String appealId) throws InternalException;

    List<DMP> getDMPListByUserId(String userId) throws  InternalException;

    void uploadPatientAppealFiles(List<MultipartFile> files, List<String> dmpPatientAppealIds) throws InternalException;
    void uploadPatientAppealCommentFiles(List<MultipartFile> files, String dmpPatientAppealCommentId) throws InternalException;
    String saveDocument(InputStream content, String mimeType, String fileName) throws InternalException;
    GridFsResource downloadDocument(String fileId) throws InternalException;

}
