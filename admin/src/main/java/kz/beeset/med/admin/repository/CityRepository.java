package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.model.catalog.City;
import org.springframework.dao.DataAccessException;

public interface CityRepository extends ResourceUtilRepository<City, String> {
    City getCityById(String id) throws DataAccessException;
    void deleteCityById(String id) throws DataAccessException;
}
