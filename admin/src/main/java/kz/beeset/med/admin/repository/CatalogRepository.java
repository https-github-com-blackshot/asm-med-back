package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.constant.DefaultConstant;
import kz.beeset.med.admin.model.catalog.Catalog;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends ResourceUtilRepository<Catalog, String> {
    Catalog getById(String id)throws DataAccessException;
    Catalog getCatalogByUrlCode(String urlCode) throws DataAccessException;

    Page<Catalog> findAll(Pageable pageable) throws DataAccessException;
    void deleteCatalogById(String id) throws DataAccessException;
}