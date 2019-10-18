package kz.beeset.med.gateway2.constant;

public final class UserConstants {

    private UserConstants(){};

    //general
    public final static String ROOT_ORGANIZATION_ID = "20048d4c96f13900eccb2539";

    public final static String DEFAULT_USER_ROLE_ID = "50058d4c96f13900eccb2539";

    public final static String EMPTY_STRING = "";

    public final static String SORT_DIRECTION_ASC = "asc";

    public final static String SORT_DIRECTION_DESC = "desc";

    public final static int DEFAUT_PAGE_NUMBER = 0;

    public final static int DEFAUT_PAGE_SIZE = 5;

    public final static boolean DEFAULT_IS_ACTIVE = false;

    // user
    public static final String USER_ENTITY_ID = "user";

    public static final String ID_FIELD_NAME = "id";

    /**
     * Активный ли пользователь
     */
    public static final String ISACTIVE_FIELD_NAME = "isActive";

    /**
     * Логин
     */
    public static final String USERNAME_FIELD_NAME = "username";

    /**
     * Пароль
     */
    public static final String PASSWORD_FIELD_NAME = "password";

    /**
     * Аватар Id
     */
    public static final String AVATARID_FIELDS_EMAIL = "avatarId";

    /**
     * E-mail
     */
    public static final String EMAIL_FIELD_NAME = "email";

    /**
     * ИИН
     */
    public static final String IDN_FIELD_NAME = "idn";

    /**
     * Имя
     */
    public static final String NAME_FIELD_NAME = "name";

    /**
     * Фамилия
     */
    public static final String SURNAME_FIELD_NAME = "surname";

    /**
     * Отчество
     */
    public static final String MIDDLENAME_FIELD_NAME = "middlename";

    /**
     * Дата рождения
     */
    public static final String BIRTHDAY_FIELD_NAME = "birthday";

    /**
     * Адрес
     */
    public static final String ADDRESS_FIELD_NAME = "address";

    /**
     * Пол
     */
    public static final String SEX_FIELD_NAME = "sex";

    /**
     * Мой личный номер телефона(мобильный)
     */
    public static final String MOBILEPHONE_FIELD_NAME = "mobilePhone";

    /**
     * Показать пациентам при записи на прием
     */
    public static final String ISSHOWMOBILEPHONE_FIELD_NAME = "isShowMobilePhone";

    /**
     * Контактные телефоны(видны пациентам при записи на прием)
     */
    public static final String CONTACTPHONE_FIELD_NAME = "contactPhone";

    /**
     * Кратко о себе
     */
    public static final String ABOUT_YOURSELF_FIELD_NAME = "about_yourself";

    /**
     * Язык интерфейса
     */
    public static final String INTERFACE_LANG_FIELD_NAME = "interface_lang";

    /**
     * Категория
     */
    public static final String CATEGORY_FIELD_NAME = "category";

    /**
     * Стаж работы в медицине(в годах)
     */
    public static final String EXPERIENCE_FIELD_NAME = "experiance";

    /**
     * Специальность
     */
    public static final String SPECIALITYIDS_FIELD_NAME = "specialityIds";

    /**
     * Признак пенсионера
     */
    public static final String PENS_FIELD_NAME = "pens";

    /**
     * Признак инвалидности
     */
    public static final String INVALIDITY_FIELD_NAME = "invalidity";

    /**
     * Страховой полис
     */
    public static final String INSURPOLICY_FIELD_NAME = "insurPolicy";

    /**
     * Дата регистрации в системе
     */
    public static final String SYS_REG_DATE_FIELD_NAME = "sys_reg_date";

    /**
     * Организации
     */
    public static final String ORGANIZATIONIDS_FIELD_NAME = "organizationIds";

    /**
     * Врачи
     */
    public static final String DOCTORIDS_FIELD_NAME = "doctorIds";

    /**
     * Список прав
     */
    public final static String RIGHTSIDS_FIELD_NAME = "rightIds";

    /**
     * Список ролей
     */
    public static final String ROLEIDS_FIELD_NAME = "roleIds";

    public final static String STATE_FIELD_NAME = "state";

    public final static String LAST_MODIFIED_DATE_FIELD_NAME = "lastModifiedDate";

    public final static String LAST_MODIFIED_BY_FIELD_NAME = "lastModifiedBy";

    public final static String CREATED_DATE_FIELD_NAME = "createdDate";

    public final static String CREATED_BY_FIELD_NAME = "createdBy";

    public final static int STATUS_ACTIVE = 1;

    public final static int STATUS_DELETED = 5;

}
