package kz.beeset.med.admin.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import kz.beeset.med.admin.configs.MongoConfig;
import kz.beeset.med.admin.model.User;
import kz.beeset.med.admin.model.common.UserOrgRoleUnit;
import kz.beeset.med.admin.model.common.UserRoleOrgMap;
import kz.beeset.med.admin.repository.UsersRepository;
import kz.beeset.med.admin.utils.error.ErrorCode;
import kz.beeset.med.admin.utils.error.InternalException;
import kz.beeset.med.admin.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private MongoConfig mongoConfig;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoDbFactory dbFactory;

    public Page<User> read(Query query, Pageable pageableRequest) throws DataAccessException, InternalException {
        try {
            return usersRepository.findAll(query, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<User> read(Query query, Pageable pageableRequest)" +
                    "-", e);
        }
    }

    public User findUserByIdn(String idn) throws DataAccessException {

        User user = null;

        try {

            user = usersRepository.findByIdn(idn);

        } catch (EmptyResultDataAccessException e) {

            return null;
        }

        return user;
    }

    public User findUserById(String id) throws DataAccessException, InternalException {
        try {

            User user = null;

            try {

                user = usersRepository.findUserById(id);

            } catch (EmptyResultDataAccessException e) {

                return null;
            }

            return user;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "User findUserById(String id)" +
                    "-", e);
        }
    }

    public List<User> readByIdn(String idn) throws DataAccessException, InternalException {
        try {
            return usersRepository.readByIdn(idn);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByIdn(String idn)" +
                    "-", e);
        }
    }

    public User findByIdn(String idn) throws DataAccessException {

        User user = null;

        try {
            user = usersRepository.findByIdn(idn);
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

        return user;
    }

    public void updateUserShortInformation(User newUserInfo) throws InternalException {
        try {
            User user = usersRepository.findUserById(newUserInfo.getId());
            if ((user.getMobilePhone() != null && !user.getMobilePhone().equals(newUserInfo.getMobilePhone()))
                    || (user.getMobilePhone() == null && newUserInfo.getMobilePhone() != null)
            ) {
                if (usersRepository.getUsersByMobilePhone(newUserInfo.getMobilePhone()).size() > 0) {
                    throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Пользователь с таким номером телефона существует!");
                }
            }
            if ((user.getIdn() != null && !user.getIdn().equals(newUserInfo.getIdn()))
                    || (user.getIdn() == null && newUserInfo.getIdn() != null)
            ) {
                if (usersRepository.getUsersByIdn(newUserInfo.getIdn()).size() > 0) {
                    throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Пользователь с таким ИИН существует!");
                }
            }
            user.setName(newUserInfo.getName());
            user.setMiddlename(newUserInfo.getMiddlename());
            user.setSurname(newUserInfo.getSurname());
            user.setBirthday(newUserInfo.getBirthday());
            user.setEmail(newUserInfo.getEmail());
            user.setSex(newUserInfo.getSex());
            user.setShowMobilePhone(newUserInfo.isShowMobilePhone());
            user.setMobilePhone(newUserInfo.getMobilePhone());
            user.setContactPhone(newUserInfo.getContactPhone());
            user.setAboutYourself(newUserInfo.getAboutYourself());
            user.setWorkplace(newUserInfo.getWorkplace());
            user.setJob(newUserInfo.getJob());
            user.setColor(newUserInfo.getColor());
            user.setWeight(newUserInfo.getWeight());
            user.setHeight(newUserInfo.getHeight());
            user.setNationality(newUserInfo.getNationality());
            usersRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "(updateUserShortInformation)", e);
        }
    }

    public List<User> read() throws InternalException {
        try {
            return usersRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> read()" +
                    "-", e);
        }
    }

    public User get(String id) throws InternalException {
        try {
            return usersRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "User get(String id)" +
                    "-", e);
        }
    }

    public User create(User user) throws InternalException {
        try {

            user.setSysRegDate(LocalDateTime.now());
            user.setHealthStatus(6); // Устройтве не зарегистрировано
            return usersRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "User create(User user)" +
                    "-", e);
        }
    }

    public User update(User user) throws InternalException {
        try {
            return usersRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "User update(User user)" +
                    "-", e);
        }
    }

    public void delete(User user) throws InternalException {
        try {
            usersRepository.delete(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(User user)" +
                    "-", e);
        }
    }

    /**
     * Далее идут примеры различных условий выборки
     */
    public List<User> readByPensStatus() throws InternalException {
        try {
            return usersRepository.readByPensStatus();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByPensStatus()" +
                    "-", e);
        }
    }

    public List<User> readByIdnAndPensGreaterThan(String idn, String pens) throws InternalException {
        try {
            return usersRepository.readByIdnAndPensGreaterThan(idn, pens);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByIdnAndPensGreaterThan(String idn, String pens)" +
                    "-", e);
        }
    }

    public List<User> readByIdnAndPensGreaterThanEqual(String idn, String pens) throws InternalException {
        try {
            return usersRepository.readByIdnAndPensGreaterThanEqual(idn, pens);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByIdnAndPensGreaterThanEqual(String idn, String pens)" +
                    "-", e);
        }
    }

    public List<User> readByIdnOrPens(String idn, String pens) throws InternalException {
        try {
            return usersRepository.readByIdnAndPensGreaterThanEqual(idn, pens);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByIdnOrPens(String idn, String pens)" +
                    "-", e);
        }
    }

    public List<User> readByBirthdate(java.util.Date dt) throws InternalException {
        try {
            return usersRepository.readByBirthdate(dt);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByBirthdate(java.util.Date dt)" +
                    "-", e);
        }
    }

    public List<User> readByBirthdatePeriod(java.util.Date dtStart, java.util.Date dtEnd) throws InternalException {
        try {
            return usersRepository.readByBirthdatePeriod(dtStart, dtEnd);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readByBirthdatePeriod(java.util.Date dtStart, java.util.Date dtEnd)" +
                    "-", e);
        }
    }

    public List<User> readPensPagenation(String pens, int pageNum, int itemsOnPage) throws InternalException {
        try {
            Page<User> page = usersRepository.readPensPagenation(pens, PageRequest.of(pageNum, itemsOnPage));
            return page.getContent();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> readPensPagenation(String pens, int pageNum, int itemsOnPage)" +
                    "-", e);
        }
    }

    public List<User> userPaginateList(Query query) throws InternalException {
        try {
            return mongoTemplate.find(query, User.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<User> userPaginateList(Query query)" +
                    "-", e);
        }
    }

    /**
     * Пример работы с GridFS (сохранение файлов)
     */
    public String saveAvatar(MultipartFile file) throws IOException, InternalException {
        try {
            // Добавляем свою произвольную информацию к файлу
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("kind", "avatar");

            // не сохраняется content-type - непонятно почему, так и не разобрался
            return gridFsOperations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData).toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String saveAvatar(MultipartFile file)" +
                    "-", e);
        }
    }

    /**
     * Пример работы с GridFS (чтение файлов по id)
     */
    public GridFsResource downloadFile(String fileId) throws IOException, InternalException {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)));
            return new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "GridFsResource downloadFile(String fileId)" +
                    "-", e);
        }

    }


    /**
     * Пример работы с GridFS (сохранение файлов)
     */
    public String saveUserPicture(InputStream content, String mimeType, String fileName) throws IOException, InternalException {
        try {
            // Добавляем свою произвольную информацию к файлу
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("kind", "avatar");
            metaData.put("mimeType", mimeType);
            LOGGER.debug("[saveUserPicture()] - mimeType: " + mimeType);
            return gridFsOperations.store(content, fileName, mimeType, metaData).toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "String saveUserPicture(InputStream content, String mimeType)" +
                    "-", e);
        }
    }

    public String save(InputStream inputStream, String contentType, String filename,
                       String username, String bucket, String mimeType) {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "image");
        metaData.put("kind", "avatar");
        metaData.put("mimeType", mimeType);
        LOGGER.debug("[save()] - mimeType: " + mimeType);

        GridFS gridFS = new GridFS((DB) dbFactory.getDb(), bucket);
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream);
        gridFSInputFile.setMetaData(metaData);

        if (bucket.equals("photo")) {
            gridFSInputFile.setChunkSize(1024000);  //1 mb
        } else if (bucket.equals("mp3")) {
            gridFSInputFile.setChunkSize(10240000); //10 mb
        }

        try {
            gridFSInputFile.saveChunks();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gridFSInputFile.save();

        return gridFSInputFile.getId().toString();
    }

    /**
     * Метод возвращает bucket(таблицу) где хранятся файлы
     * по умолчанию bucket не берется из MongoDatabase, хотя в MongoConfig классе установка этого значения реализована
     */
    private GridFSBucket getGridFs() throws InternalException {
        try {
            MongoDatabase db = mongoDbFactory.getDb();
            return GridFSBuckets.create(db, mongoConfig.getDefaultBucket());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "GridFSBucket getGridFs()" +
                    "-", e);
        }
    }

    // yernur
    public Page<User> search(String searchString, Pageable pageableRequest) throws InternalException {
        try {
            return usersRepository.query(searchString, pageableRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Page<User> search(String searchString, Pageable pageableRequest)" +
                    "-", e);
        }
    }

    public List<UserRoleOrgMap> addOrgWhithId(UserRoleOrgMap unit) throws InternalException {
        try {
            User user = usersRepository.getById(unit.getUserId());

            List<UserRoleOrgMap> userRoleOrgMapsCurrent = new ArrayList<>();

            UserRoleOrgMap orgMap = new UserRoleOrgMap();

            if (user.getUserRoleOrgMapList() != null) {
                userRoleOrgMapsCurrent = user.getUserRoleOrgMapList();
            }
//            userRoleOrgMapsCurrent = user.getUserRoleOrgMapList();
//
//            if (!equalOrgId(userRoleOrgMapsCurrent, unit.getOrgId())) {
//                orgMap.setOrgId(unit.getOrgId());
//                userRoleOrgMapsCurrent.add(orgMap);
//            }

//        } else {

            userRoleOrgMapsCurrent.add(unit);

//        }

            user.setUserRoleOrgMapList(userRoleOrgMapsCurrent);

            usersRepository.save(user);

            return userRoleOrgMapsCurrent;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<UserRoleOrgMap> addOrgWhithId(UserRoleOrgMap unit)" +
                    "-", e);
        }

    }

    public List<UserRoleOrgMap> delOrgWhithId(UserOrgRoleUnit unit) throws InternalException {
        try {

            User user = usersRepository.getById(unit.getUserId());

            List<UserRoleOrgMap> userRoleOrgMapsCurrent = new ArrayList<>();

            UserRoleOrgMap orgMap = new UserRoleOrgMap();

            if (user.getUserRoleOrgMapList() != null) {

                userRoleOrgMapsCurrent = user.getUserRoleOrgMapList();

                orgMap = getUserRoleOrgMapById(unit.getOrgId(), userRoleOrgMapsCurrent);

                userRoleOrgMapsCurrent.remove(orgMap);

//            for (UserRoleOrgMap orgMap : userRoleOrgMapsCurrent){
//
//                if(orgMap.getOrgId().equals(unit.getOrgId())){
//                    userRoleOrgMapsCurrent.remove(orgMap);
//                }
//
//            }

            }

            user.setUserRoleOrgMapList(userRoleOrgMapsCurrent);

            usersRepository.save(user);

            return userRoleOrgMapsCurrent;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<UserRoleOrgMap> delOrgWhithId(UserOrgRoleUnit unit)" +
                    "-", e);
        }

    }

    public List<UserRoleOrgMap> addRoleinOrg(UserOrgRoleUnit unit) throws InternalException {
        try {
            User user = usersRepository.getById(unit.getUserId());

            List<UserRoleOrgMap> userRoleOrgMaps = user.getUserRoleOrgMapList();
            List<UserRoleOrgMap> userRoleOrgMapsNew = user.getUserRoleOrgMapList();
            List<String> roles = new ArrayList<String>();

            if (userRoleOrgMaps != null) {
//TODO Перерисать ВСЕ!
                UserRoleOrgMap userRoleOrgMapNew = null;
                for (UserRoleOrgMap userRoleOrgMap : userRoleOrgMaps) {
                    if (userRoleOrgMap.getOrgId().equals(unit.getOrgId())) {
                        if (userRoleOrgMap.getRoles() != null)
                            roles.addAll(userRoleOrgMap.getRoles());
                        userRoleOrgMapNew = userRoleOrgMap;
                    }
                }
                userRoleOrgMapsNew.remove(userRoleOrgMapNew);
                roles.add(unit.getRoleId());
                assert userRoleOrgMapNew != null;
                userRoleOrgMapNew.setRoles(roles);
                userRoleOrgMapsNew.add(userRoleOrgMapNew);
            }

            user.setUserRoleOrgMapList(userRoleOrgMapsNew);

            usersRepository.save(user);

            return userRoleOrgMapsNew;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<UserRoleOrgMap> addRoleinOrg(UserOrgRoleUnit unit)" +
                    "-", e);
        }


//        UserRoleOrgMap userRoleOrgMap = getUserRoleOrgMapById(unit.getOrgId(), map);
//        map.remove(userRoleOrgMap);
//        if (userRoleOrgMap.getRoles() != null)
//            roles = userRoleOrgMap.getRoles();
//
//        roles.add(unit.getRoleId());
//        userRoleOrgMap.setRoles(roles);
//        map.add(userRoleOrgMap);

    }

    public List<UserRoleOrgMap> delRoleinOrg(UserOrgRoleUnit unit) throws InternalException {
        try {
            User user = usersRepository.getById(unit.getUserId());

            List<UserRoleOrgMap> userRoleOrgMaps = user.getUserRoleOrgMapList();
            UserRoleOrgMap userRoleOrgMap = userRoleOrgMaps.stream()
                    .filter(o -> o.getOrgId().equals(unit.getOrgId()))
                    .findFirst()
                    .get();
            List<String> roles = userRoleOrgMap.getRoles();
            userRoleOrgMaps.remove(userRoleOrgMap);
            roles.removeIf(role -> role.equals(unit.getRoleId()));
            userRoleOrgMap.setRoles(roles);
            userRoleOrgMaps.add(userRoleOrgMap);
//        List<UserRoleOrgMap> userRoleOrgMapsNew = user.getUserRoleOrgMapList();
//
//        List<String> roles = new ArrayList<>();
//        List<String> rolesNew = new ArrayList<>();
//
//        if (userRoleOrgMaps != null) {
//            for (UserRoleOrgMap userRoleOrgMap: userRoleOrgMaps){
//                if(userRoleOrgMap.getOrgId().equals(unit.getOrgId()))
//                    roles.addAll(userRoleOrgMap.getRoles());
//                    rolesNew.addAll(roles);
//                    for(String role: roles)
//                        if(role.equals(unit.getRoleId())) {
//                            userRoleOrgMapsNew.remove(userRoleOrgMap);
//                            rolesNew.remove(role);
//                            userRoleOrgMap.setRoles(rolesNew);
//                            userRoleOrgMapsNew.add(userRoleOrgMap);
//                        }
//            }
//
//        } else
//            new Throwable();

            user.setUserRoleOrgMapList(userRoleOrgMaps);

            usersRepository.save(user);

            return userRoleOrgMaps;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<UserRoleOrgMap> delRoleinOrg(UserOrgRoleUnit unit)" +
                    "-", e);
        }


//        UserRoleOrgMap userRoleOrgMap = getUserRoleOrgMapById(unit.getOrgId(), userRoleOrgMaps);
//        userRoleOrgMaps.remove(userRoleOrgMap);
//        if (userRoleOrgMap.getRoles() != null)
//            roles = userRoleOrgMap.getRoles();
//
//        roles.remove(unit.getRoleId());
//        userRoleOrgMap.setRoles(roles);
//        userRoleOrgMaps.add(userRoleOrgMap);

    }

    private boolean equalOrgId(List<UserRoleOrgMap> userRoleOrgMapsCurrent, String OrgId) throws InternalException {
        try {
            boolean equal = false;

            for (UserRoleOrgMap orgMap : userRoleOrgMapsCurrent) {
                if (orgMap.getOrgId().equals(OrgId)) {

                    equal = true;

                }
            }

            return equal;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "boolean equalOrgId(List<UserRoleOrgMap> userRoleOrgMapsCurrent, String OrgId)" +
                    "-", e);
        }


    }

    private UserRoleOrgMap getUserRoleOrgMapById(String id, List<UserRoleOrgMap> userRoleOrgMaps) throws InternalException {
        try {
            for (UserRoleOrgMap orgMap : userRoleOrgMaps) {
                if (orgMap.getOrgId().equals(id))
                    return orgMap;
            }
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "UserRoleOrgMap getUserRoleOrgMapById(String id, List<UserRoleOrgMap> userRoleOrgMaps)" +
                    "-", e);
        }
    }

}
