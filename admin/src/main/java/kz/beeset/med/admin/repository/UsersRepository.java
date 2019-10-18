package kz.beeset.med.admin.repository;

import kz.beeset.med.admin.constant.UserConstants;
import kz.beeset.med.admin.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UsersRepository extends ResourceUtilRepository<User, String> {

    /**
     * Поиск по ID
     * @param id - идентификатор
     * @return - возвращает одного пользователя
     */
    @Query("{id:'?0'}") //, state:"+UserConstants.STATUS_ACTIVE +"
    User findUserById(String id) throws DataAccessException;

    /**
     * Поиск по ИИН
     * @param idn - идентификатор
     * @return - возвращает несколько пользователя
     */
    @Query("{idn:'?0', state:"+UserConstants.STATUS_ACTIVE +"}")
    List<User> readByIdn(String idn) throws DataAccessException;

    /**
     * Поиск по ИИН
     * @param idn - идентификатор
     * @return - возвращает пользователя
     */
    @Query("{idn:'?0', state:"+UserConstants.STATUS_ACTIVE +"}")
    User findByIdn(String idn) throws DataAccessException;

    /**
     * Поиск по ИИН
     * @param idn - идентификатор
     * @return - возвращает один пользователя
     */
    @Query("{idn:'?0', state:"+UserConstants.STATUS_ACTIVE +"}")
    User readOneByIdn(String idn) throws DataAccessException;

    @Query("{mobilePhone:'?0', state:"+UserConstants.STATUS_ACTIVE +"}")
    List<User> getUsersByMobilePhone(String mobilePhone) throws DataAccessException;

    @Query("{idn:'?0', state:"+UserConstants.STATUS_ACTIVE +"}")
    List<User> getUsersByIdn(String idn) throws DataAccessException;

    /**
     * Поиск по ID
     * @param id - идентификатор
     * @return - возвращает одного пользователя
     */
    @Query("{id:'?0'}")
    User getById(String id);

    /**
     * Пример использования $in, когда нужно перечислить несколько значений
     * @return - список пользователей со значением в поле pens 1 или 2
     */
    @Query("{ pens: { $in: [ \"1\", \"2\" ] } }")
    List<User> readByPensStatus();

    /**
     * Пример использования AND и выражения если значение поля "больше", ">"
     */
    @Query("{idn:'?0',pens:{$gt:'?1'}}")
    List<User> readByIdnAndPensGreaterThan(String idn, String pens);

    /**
     * Пример использования ИИН=<idn> AND и выражения если значение поля pens ">=" <pens>
     */
    @Query("{idn:'?0',pens:{$gte:'?1'}}")
    List<User> readByIdnAndPensGreaterThanEqual(String idn, String pens);

    /**
     * Пример использования OR
     * Выборка pens = значение И какое-нибудь из перечисленных даёт true [idn = значение ИЛИ email начинается с буквы "a"]
     */
    @Query("{pens:'?1', $or: [{idn:'?0'}, {email:{$regex:'^a'}}] }")
    List<User> readByIdnOrPens(String idn, String pens);

    /**
     * Пример выборки по дате рождения меньше(ранее) "<" чем запрашиваемая
     */
    @Query("{birthdate: {$lt: ?0} }")
    List<User> readByBirthdate(java.util.Date dt);

    /**
     * Пример выборки по периоду дат
     */
    @Query("{birthdate: {$gte: ?0, $lte: ?1} }")
    List<User> readByBirthdatePeriod(java.util.Date dtBegin, java.util.Date dtEnd);

    /**
     * Пример пагинации
     */
    @Query("{pens:'?0'}")
    Page<User> readPensPagenation(String pens, Pageable pageable);

//    @Query(value = "{ $or: [ { 'title' : {$regex:?0,$options:'i'} }, { 'description' : {$regex:?0,$options:'i'} } ] }")
//    Page<Post> query(String query, Pageable page);

    @Query(value = "{ $or: [ { 'username' : {$regex:'?0', $options: 'i'} }, {idn:{$regex:'?0', $options: 'i'}}, { 'name' : {$regex:'?0', $options: 'i'} },  { 'surname' : {$regex:'?0', $options: 'i'} }, { 'middlename' : {$regex:'?0', $options: 'i'} } ], state:"+UserConstants.STATUS_ACTIVE +"}")
    Page<User> query(String searchString, Pageable pageable);
}
