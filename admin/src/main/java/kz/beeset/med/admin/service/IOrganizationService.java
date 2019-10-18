package kz.beeset.med.admin.service;

import kz.beeset.med.admin.model.Organization;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.model.TreeData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface IOrganizationService{

        List<Organization> read() throws InternalException;

        List<Organization> readButRoot() throws InternalException;

        Page<Organization> read(Query query, Pageable pageableRequest) throws InternalException;

        List<TreeData> tree() throws InternalException;

        List<Organization> readPath(String path) throws InternalException;

        Organization get(String id) throws InternalException ;

        Organization getSingleOrgUnit(String id) throws InternalException ;

        Organization create(Organization organization) throws InternalException;

        Organization update(Organization organization) throws InternalException;

        Organization updateNew(Organization organization) throws InternalException;

        public void delete(Organization organization) throws InternalException;

        List<TreeData> search(String searchString, Pageable pageableRequest) throws InternalException;

       List<Organization> readByCode(String code) throws InternalException;
}
