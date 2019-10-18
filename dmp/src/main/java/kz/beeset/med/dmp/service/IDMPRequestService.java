package kz.beeset.med.dmp.service;


import kz.beeset.med.dmp.model.DMPRequest;
import kz.beeset.med.dmp.model.custom.DMPRequestCustom;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IDMPRequestService {

    Page<DMPRequest> readPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPRequest> search(Map<String, String> allRequestParams) throws InternalException;
    Page<DMPRequestCustom> readCustomPageable(Map<String, String> allRequestParams) throws DataAccessException, InternalException;
    Page<DMPRequestCustom> searchCustom(Map<String, String> allRequestParams) throws InternalException;
    List<DMPRequest> readIterable() throws InternalException;
    DMPRequest get(String id) throws InternalException;
    DMPRequest create(DMPRequest dmpRequest) throws InternalException;
    DMPRequest update(DMPRequest dmpRequest) throws InternalException;
    DMPRequest accept(DMPRequest dmpRequest) throws InternalException;
    DMPRequest decline(DMPRequest dmpRequest) throws InternalException;
    DMPRequest recall(String dmpRequestId) throws InternalException;
    DMPRequest delete(String id) throws InternalException;

    DMPRequest uploadFile(List<MultipartFile> files, String dmpRequestId) throws InternalException, IOException;
    String saveDocument(InputStream content, String mimeType, String fileName) throws InternalException;
    GridFsResource downloadDocument(String fileId) throws InternalException;


}
