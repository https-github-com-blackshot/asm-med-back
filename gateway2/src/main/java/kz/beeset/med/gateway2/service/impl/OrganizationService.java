package kz.beeset.med.gateway2.service.impl;

import kz.beeset.med.gateway2.constant.OrganizationConstant;
import kz.beeset.med.admin.model.Organization;
import kz.beeset.med.gateway2.repository.OrganizationRepository;
import kz.beeset.med.gateway2.service.IOrganizationService;
import kz.beeset.med.gateway2.util.error.ErrorCode;
import kz.beeset.med.gateway2.util.error.InternalException;
import kz.beeset.med.gateway2.util.error.InternalExceptionHelper;
import kz.beeset.med.gateway2.util.model.TreeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrganizationService implements IOrganizationService {
    @Override
    public Page<Organization> read(Query query, Pageable pageableRequest) throws InternalException {
        return null;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    // ------------------------------------- основные методы ------------------------------------------------

    public List<Organization> read() throws InternalException {//MultiValueMap<String, String> params
        try {
            return organizationRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Organization> read()" +
                    "-", e);
        }
    }

    public List<TreeData> tree() throws InternalException {
        try {
            List<TreeData> treeDataList = getMapedChilds(getOrgMap(organizationRepository.findAll()), "null");
            return treeDataList;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<TreeData> tree()" +
                    "-", e);
        }

//        return getChilds(getOrgMap(orgUnitRepository.findAll()),"null");
//        List<Organization> orgUnitTree = getChilds(getOrgMap(orgUnitRepository.findAll()),"null");


//        for(TreeData treeData: treeDataList){
//            treeData.setChildren(orgUnit.getChildren());
//            orgUnit.setChildren(null);
//            treeData.setChildren(orgUnit.getChildren());
//            treeData.setData(orgUnit);
//            treeDataList.add(treeData);
//        }

//        return treeDataList;

    }

    /**
     * Чтение БД начиная с указанного пути и построение
     *
     * @param path путь состоящий из id с разделителями ">", начиная с которого нужно построить дерево
     * @return
     */
    public List<Organization> readPath(String path) throws InternalException {
        try {
            if (path == null) {
                path = OrganizationConstant.separator;
            }

            // получаем мап по родителям
            HashMap<String, List<Organization>> orgMap = getChildsMap(path);
            List<Organization> results = new ArrayList<>();
            String parentStr;

            // строим дерево из мапа раскиданного по родителям
            // если запрашивается корневой узел ">", то достаём список записей, которые лежат в корне дерева,
            // иначе достаём начиная с последнего элемента в пути
            if (path.equals(OrganizationConstant.separator)) {
                parentStr = "null";
            } else {
                if (path.length() > 1) {
                    String s = path.substring(0, path.length() - 1);
                    parentStr = s.substring(s.lastIndexOf(OrganizationConstant.separator) + 1);
                } else {
                    parentStr = path;
                }
            }

            results.addAll(getChilds(orgMap, parentStr));

            return results;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Organization> readPath(String path)" +
                    "-", e);
        }

    }

//    public TreeTableReturn findPaginated(int page, int size) {
//
//        TreeTableReturn treeTableReturn = new TreeTableReturn();
//
//        Page<Organization> orgPage = orgUnitRepository.findAll(new PageRequest(page, size));
//
//        treeTableReturn.setElmentAllCount(orgPage.getTotalElements());
//        treeTableReturn.setPagesAllCount(orgPage.getTotalPages());
//        treeTableReturn.setData(orgPage.getContent());
//
//        return treeTableReturn;
//    }

    /**
     * Функция реализующая метод get контроллера
     *
     * @param id идентификатор записи
     * @return обьект с его дочерними записями
     */
    public Organization get(String id) throws InternalException {
        try {
            Organization organization = organizationRepository.getById(id);
            //оределяем путь, элементы которого нужно достать, т.е. всю иерархию начиная с этого элемента
            String path = ">";
            if (organization.getPath() != null) {
                path = organization.getPath();
                if (organization.getPath().equals(OrganizationConstant.separator)) {
                    path = OrganizationConstant.separator + organization.getId();
                }
            }
            // получаем из БД все дочерние записи по отноению к этой
            HashMap<String, List<Organization>> orgMap = getChildsMap(path);
            // задаём полученные из БД дочерние записи
            organization.setChildren(getChilds(orgMap, organization.getId()));
            return organization;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Organization get(String id)" +
                    "-", e);
        }

    }

    /**
     * Возврат с БД организацию по id
     *
     * @param id
     * @return возвращает сохраненный в БД объект с указанным параметром ID
     */
    public Organization getSingleOrgUnit(String id) throws InternalException {
        try {
            return organizationRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Organization getSingleOrgUnit(String id)" +
                    "-", e);
        }
    }

    /**
     * Создание записи в справочнике юр.лиц
     *
     * @param organization юр.лицо которое создаём (по хорошему это должен быть dto, а не истинный обьект)
     * @return возвращает сохраненный в БД обьект (юр.лицо) и присвоенным id, уровнем иерархии и признаком активности.
     */
    public Organization create(Organization organization) throws InternalException {
        try {
            // 1. задаём путь в иерархии, по которому будет лежать запись и в дальнейшем искаться по этому пути
            Organization organizationParent = null;

            if (organization.getParentId() != null) {

                // 1.2. Пытаемся найти родителя
                organizationParent = organizationRepository.getById(organization.getParentId());

                if (organizationParent != null) {

                    if (organizationParent.getPath() == null || organizationParent.getPath().isEmpty()) {

                        organization.setPath(OrganizationConstant.separator);

                    } else {

                        organization.setPath(organizationParent.getPath() + organizationParent.getId() + OrganizationConstant.separator);
                    }
                }
            }

            // 2. если родитель не был найден, то указываем этой записи корневой путь
            if (organizationParent == null) { // && organization.getPath() == null
                organization.setPath(OrganizationConstant.separator);
            }

            if (organization.getId() != null)
                organization.setId(null);

            // 3. сохраняем в БД
            organization.setState(OrganizationConstant.STATUS_ACTIVE);
            organization.setIsActive(1);

            organization.setCreatedBy("");
            organization.setCreatedDate(LocalDateTime.now());

            organization = organizationRepository.save(organization);

            return organization;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Organization create(Organization organization)" +
                    "-", e);
        }

    }

    /**
     * Изменение записи двух-фазным коммитом
     *
     * @param organization юр.лицо которое изменяем (по хорошему это должен быть dto, а не истинный обьект)
     * @return возвращает сохраненный в БД обьект (юр.лицо) и присвоенным id, уровнем иерархии и признаком активности.
     */
    public Organization update(Organization organization) throws InternalException {
        try {
            // Two phase commit for update operation
            // Двух-фазный коммит

            // 1. достаём текущее состояние этого обьекта вместе с его дочерними записями
            Organization organizationOld = this.get(organization.getId()); //orgPersonRepository.getById(organization.getId());

            // 2. формируем новый путь для всей иерархии дочерних записей
            // в случае несовпадения родителя, т.е. если на фронт-энде изменили родителя
            if (organizationOld.getParentId() == null || !organizationOld.getParentId().equals(organization.getParentId())) {
                // 2.1 достаём родителя, чтобы взять его полный путь
                Organization newOrganizationParent = organizationRepository.getById(organization.getParentId());

                // если указали родителя и его не нашли, то выдаём ошибку
                if (organization.getParentId() != null && newOrganizationParent == null) {
                    throw new UnsupportedOperationException("Parent not found");
                }

                String newPath = null;
                // если указан новый родитель, то нужно обновить путь у этой запись и у всех его дорних новым
                if (newOrganizationParent != null) {
                    newPath = newOrganizationParent.getPath() + newOrganizationParent.getId() + OrganizationConstant.separator;
                }
                // а если родитель не указан, то выдаю ошибку, что не найден родитель
                else {
                    newPath = OrganizationConstant.separator;
                }

                // будем накапливать команды update в переменной bulkOps, чтобы выполнить их одним запросом, а не отдельными для каждой дочерней записи
                BulkOperations bulkOps1 = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Organization.class);
                BulkOperations bulkOps2 = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Organization.class);

                // на всякий случай восстанавливаю (перебиваю) путь который пришел с фронт-энда на истинный
                organization.setPath(organizationOld.getPath());

                // 2.2 сначала записываем новый путь этой записи
                putUpdateInBulk(bulkOps1, bulkOps2, organizationOld.getPath(), newPath, organization);

                // 2.3 затем записываем новый путь всем участникам транзакции
                setNewPathForChilds(bulkOps1, bulkOps2, organizationOld.getPath(), newPath, organizationOld.getChildren());

                // 2.4 выполняем накопленные команды update для поля "newPath"
                // 1-ая стадия двух-фазного коммита: вставка в БД (запись на диск)
                bulkOps1.execute();

                // 2.5 перебиваем старый путь новым и удаляем поле с транзакцией
                bulkOps2.execute();

                organization.setPath(newPath);
                organization.setNewPath(null);
            }


            // сохраняем новое состояние в БД (все поля)
            organization = organizationRepository.save(organization);

            // читаем новое состояние вместе с дочерними запясями
            organization = this.get(organization.getId());

            return organization;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Organization update(Organization organization)" +
                    "-", e);
        }
    }

    public Organization updateNew(Organization organization) throws InternalException {
        try {
            return organizationRepository.save(organization);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "Organization updateNew(Organization organization)" +
                    "-", e);
        }
//        Organization orgUnitOld = orgUnitRepository.getById(organization.getId());
//        Organization orgUnitNew = new Organization();
//        orgUnitNew = orgUnitOld;

    }

    public void delete(Organization organization) throws InternalException {
        try {

            organization.setState(OrganizationConstant.STATUS_DELETED);
            organizationRepository.save(organization);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void delete(Organization organization)" +
                    "-", e);
        }
    }

    public List<TreeData> search(String searchString, Pageable pageableRequest) throws InternalException {
        try {
            List<Organization> organizations = organizationRepository.query(searchString, pageableRequest).getContent();

            return getMapedChilds(getOrgMap(organizations), "null");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<TreeData> search(String searchString, Pageable pageableRequest)" +
                    "-", e);
        }
    }

    // ------------------------------------- вспомогательные методы ------------------------------------------------

    private HashMap<String, List<Organization>> getOrgMap(List<Organization> organizationList) throws InternalException {
        try {
            HashMap<String, List<Organization>> orgMap = new HashMap<>();

            String parentStr;

            for (Organization org : organizationList) {
                // проверяем имеет ли родителя, если не имеет, то родителя указываем как "null"
                if (org.getParentId() == null) {
                    parentStr = "null";
                } else {
                    parentStr = org.getParentId();
                }
                // проверяем есть ли уже такой родитель в мапе, если нет, то добавляем в мап этого родителя
                if (orgMap.get(parentStr) == null) {
                    List<Organization> childs = new ArrayList<>();
                    orgMap.put(parentStr, childs);
                }
                // раскидываем по родителям, добавляем к списку непосредственного родителя, затем из этого мапа будем строить дерево
                orgMap.get(parentStr).add(org);
            }

            return orgMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "HashMap<String, List<Organization>> getOrgMap(List<Organization> organizationList)" +
                    "-", e);
        }
    }

    /**
     * Функция раскладывает все найденные по заданному пути записи по своим родителям
     *
     * @param path полный(!) путь, по которому будут вытаскиваться из БД вся иерархия
     * @return HashMap<id_Родителя       ,               список               дочерних               обьектов               родителя>
     */
    private HashMap<String, List<Organization>> getChildsMap(String path) throws InternalException {
        try{
            HashMap<String, List<Organization>> orgMap = new HashMap<>();

            String parentStr;

            // вытаскиваем из БД все записи начинающиеся с заданного (!)полного пути
            for (Organization org : organizationRepository.readPath(path, PageRequest.of(0, 100))) {
                // проверяем имеет ли родителя, если не имеет, то родителя указываем как "null"
                if (org.getParentId() == null) {
                    parentStr = "null";
                } else {
                    parentStr = org.getParentId();
                }
                // проверяем есть ли уже такой родитель в мапе, если нет, то добавляем в мап этого родителя
                if (orgMap.get(parentStr) == null) {
                    List<Organization> childs = new ArrayList<>();
                    orgMap.put(parentStr, childs);
                }
                // раскидываем по родителям, добавляем к списку непосредственного родителя, затем из этого мапа будем строить дерево
                orgMap.get(parentStr).add(org);
            }
            return orgMap;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "HashMap<String, List<Organization>> getChildsMap(String path)" +
                    "-", e);
        }
    }

    /**
     * Рекурсивная функция - возвращает список дочерних записей, беребирает входящий HashMap
     *
     * @param orgMap источник данных, должен содержать весь набор записей из БД
     * @param parent id записи, для которой нужно построить дерево, будут включены все дочерние записи для этого id
     * @return возвращает список дочерних записей
     */
    private List<Organization> getChilds(HashMap<String, List<Organization>> orgMap, String parent) throws InternalException {
        try{
            List<Organization> results = new ArrayList<>();
            if (orgMap.get(parent) != null) {
                for (Organization org : orgMap.get(parent)) {
                    results.add(org);
                    org.setChildren(getChilds(orgMap, org.getId()));
                }
            }
            return results;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Organization> getChilds(HashMap<String, List<Organization>> orgMap, String parent)" +
                    "-", e);
        }
    }

    private List<TreeData> getMapedChilds(HashMap<String, List<Organization>> orgMap, String parent) throws InternalException {
        try{
            List<TreeData> results = new ArrayList<>();

            if (orgMap.get(parent) != null) {

                for (Organization org : orgMap.get(parent)) {

                    TreeData treeData = new TreeData();

                    treeData.setData(org);

                    results.add(treeData);

                    treeData.setChildren(getMapedChilds(orgMap, org.getId()));

                }

            }

            return results;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<TreeData> getMapedChilds(HashMap<String, List<Organization>> orgMap, String parent)" +
                    "-", e);
        }
    }

    /**
     * Рекурсивная процедура - задаёт новый путь всем дочерним запясям
     *
     * @param bulkOps1 - подготовленная переменная для операций производимых в фазе №1 списком, а не по одной
     * @param bulkOps2 - подготовленная переменная для операций производимых в фазе №2 списком, а не по одной
     * @param oldPath  - старый путь, который нужно изменить на новый
     * @param newPath  - новый путь, которым будет изменяться старый
     * @param orgs     - список обьектов, к которому будет применяться команда "update"
     */
    private void setNewPathForChilds(BulkOperations bulkOps1, BulkOperations bulkOps2, String oldPath, String newPath, List<Organization> orgs) throws InternalException {
        try{

            if (orgs != null) {
                for (Organization org : orgs) {
                    // задаём новый путь текущей записи
                    putUpdateInBulk(bulkOps1, bulkOps2, oldPath, newPath, org);
                    // задаём новый путь его дочерним записям
                    if (org.getChildren() != null && org.getChildren().size() != 0) {
                        setNewPathForChilds(bulkOps1, bulkOps2, oldPath, newPath, org.getChildren());
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void setNewPathForChilds(BulkOperations bulkOps1," +
                    " BulkOperations bulkOps2, String oldPath, String newPath, List<Organization> orgs)" +
                    "-", e);
        }
    }

    /**
     * Процедура подготавливаем операции update для 1-ой и 2-ой стадии двух-фазного коммита
     *
     * @param bulkOps1 переменная для операций производимых в фазе №1 списком, а не по одной
     * @param bulkOps2 переменная для операций производимых в фазе №2 списком, а не по одной
     * @param oldPath  старый путь, который нужно изменить на новый
     * @param newPath  новый путь, которым будет изменяться старый
     * @param org      обьект, к которому будет применяться команда "update"
     */
    private void putUpdateInBulk(BulkOperations bulkOps1, BulkOperations bulkOps2, String oldPath,
                                 String newPath, Organization org) throws InternalException {
        try{
            /* 1. вырезаем старый путь до начала иерархии, которую переносим, т.е. до переносимой записи
             * например было вот так: >5b2b5e7b69155b181475c8ea>5b2b5eef69155b181475c8f0>5b2b5f0369155b181475c8f1
             * делаем вот так:        >5b2b5eb069155b181475c8ec>5b2b5eef69155b181475c8f0>5b2b5f0369155b181475c8f1
             * т.е. запись у которой id="5b2b5eef69155b181475c8f0" переносим из "5b2b5e7b69155b181475c8ea" в "5b2b5eb069155b181475c8ec"
             * соответственно всем дочерним записям тоже нужно изменить начало пути, а конец оставить прежним
             * в итоге будет сформировано две коменды update:
             * для 5b2b5eef69155b181475c8f0 - путь был >5b2b5e7b69155b181475c8ea>
             *                                станет   >5b2b5eb069155b181475c8ec>
             * для 5b2b5f0369155b181475c8f1 - путь был >5b2b5e7b69155b181475c8ea>5b2b5eef69155b181475c8f0>
             *                                станет   >5b2b5eb069155b181475c8ec>5b2b5eef69155b181475c8f0>
             */

            Query query = new Query(Criteria.where("id").is(org.getId()));
            // ищем старый путь у дочерних записей
            int bi = org.getPath().lastIndexOf(oldPath);

            /* 2. составляем новый путь */
            String path = null;
            // если не нашли в старом полном пути, часть старого полного пути родителя, то помещаем в корень к новому родителю
            if (bi < 0) {
                path = newPath;
            }
            // а если же нашли в старом полном пути когда это дочка, внук или правнук, вырезаем часть, которая после переносимого обьекта
            // и прибавляем к новому пути
            else {
                String pathEnd;
                if (!oldPath.equals(OrganizationConstant.separator)) {
                    pathEnd = org.getPath().replaceAll(oldPath, "");
                } else {
                    if (org.getPath() != null && !org.getPath().equals(OrganizationConstant.separator) && org.getPath().length() > 1) {
                        pathEnd = org.getPath().substring(1);
                    } else {
                        pathEnd = "";
                    }
                }
                path = newPath + pathEnd;
                String endPath = path.substring(path.length() - 1);
                if (!endPath.equals(OrganizationConstant.separator)) {
                    path = path + OrganizationConstant.separator;
                }
            }

            org.setNewPath(path);
            Update update1 = Update.update("newPath", path);
            Update update2 = Update.update("isActive", org.getIsActive()).rename("newPath", "path");
            bulkOps1.updateOne(query, update1);
            bulkOps2.updateOne(query, update2);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "void putUpdateInBulk(BulkOperations bulkOps1," +
                    " BulkOperations bulkOps2, String oldPath, String newPath, Organization org)" +
                    "-", e);
        }
    }

    public List<Organization> readByCode(String code) throws InternalException {
        try{
            return organizationRepository.findByCode(code);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "List<Organization> readByCode(String code)" +
                    "-", e);
        }
    }
}