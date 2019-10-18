package kz.beeset.med.dmp.service;

import kz.beeset.med.dmp.model.DMPPatient;
import kz.beeset.med.dmp.model.DMPProtocol;
import kz.beeset.med.dmp.model.custom.DMPFileProtocol;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IDMPProtocolService {

    Page<DMPProtocol> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPProtocol> search(Map<String, String> allRequestParams) throws InternalException;
    List<DMPProtocol> readIterableByDMPId(String dmpId) throws InternalException;
    DMPProtocol get(String id) throws InternalException;

    List<DMPProtocol> getAllByUserId(String userId) throws InternalException;
    DMPProtocol create(DMPProtocol dmpProtocol) throws InternalException;
    DMPProtocol update(DMPProtocol dmpProtocol) throws InternalException;
    DMPProtocol delete(String id) throws InternalException;

    DMPProtocol uploadFile(List<MultipartFile> files, String dmpProtocolId) throws InternalException, IOException;
    String saveDocument(InputStream content, String mimeType, String fileName) throws InternalException;
    GridFsResource downloadDocument(String fileId) throws InternalException;


}
