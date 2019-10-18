package kz.beeset.med.dmp.service.dmpv2;

import kz.beeset.med.dmp.model.dmpv2.Protocol;
import kz.beeset.med.dmp.utils.error.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IProtocolService {

    Page<Protocol> readPageable(Map<String, String> allRequestParams) throws InternalException;

    Page<Protocol> searchPageable(Map<String, String> allRequestParams) throws InternalException;

    List<Protocol> readIterable() throws InternalException;

    List<Protocol> readIterableByDMPV2Id(String dmpV2Id) throws InternalException;

    Protocol readOne(String id) throws InternalException;

    Protocol create(Protocol protocol) throws InternalException;

    Protocol update(Protocol protocol) throws InternalException;

    String saveFile(InputStream content, String mimeType, String fileName) throws IOException, InternalException;

    void delete(String id) throws InternalException;

    GridFsResource downloadFile(String fileId) throws IOException, InternalException;

}
