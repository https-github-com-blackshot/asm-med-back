Процесс входа пользователя в систему:
1. Ввод данных на фронт-энде (организация, логин, пароль)
2. Проверка заданныхданных, <логин> + <пароль> + <наличие id_Организации в поле organization>(если организация задана, а если не задана, то игнорировать)
3. Если не совпало, то ошибка
4. Если совпало, то считать роли для пользователя по данной организации(если организация задана, а если не задана, то роли без организации, т.е. системные)
5. По всем назначенным ролям получаем права, которые входят в эти роли
6. По всем полученным правам получаем дерево меню, которое доступно пользователю.
    В каждом "праве" в поле menuIds перечислены меню, доступные для данного права. Нужно достать всё дерево по кажой id-шке -
    это 3 разновидности меню:
        - все дочки, т.е. всё что ниже (path содержит эту id)
        - все родители, т.е. всё что выше (перечисление id-шек из поля path, до заданной id)
        - само меню по указанному id


Swagger-UI
http://localhost:8765/med/med-admin/swagger-ui.html
или локально поднятый один микросервис
http://localhost:9210/swagger-ui.html


Порядок  запуска микросервисов на стенде (с ограничением по памяти 512МБ)
1. Eureka сервер (port 8761)
    java -jar -Xmx512m eureka/build/libs/eureka-1.0.1.jar

2. GateWay (port 8765)
    java -jar -Xmx512m gateway/build/libs/gateway-1.0.1.jar
      или
    java -jar -Xmx512m gateway2/build/libs/gateway2-1.0.1.jar
    java -jar -Dspring.profiles.active=prod -Xmx512m gateway2/build/libs/gateway2-1.0.1.jar

3. Микросервис модуля Admin (port 9210):
    java -jar -Xmx512m admin/build/libs/admin-1.0.1.jar
      или для подключения к удалённой БД на сервере 62.173.140.134
    java -jar -Dspring.profiles.active=prod -Xmx512m admin/build/libs/admin-1.0.1.jar

4. Микросервис модуля Constructor (port 9211):
    java -jar -Xmx512m constructor/build/libs/constructor-1.0.1.jar
      или для подключения к удалённой БД на сервере 62.173.140.134
    java -jar -Dspring.profiles.active=prod -Xmx512m constructor/build/libs/constructor-1.0.1.jar


5. Микросервис модуля КИ (port 9212):
    java -jar -Xmx2048m ki/build/libs/ki-1.0.1.jar
      или с подключением к удаленной БД на сервере 62.173.140.134
    java -jar -Dspring.profiles.active=prod -Xmx512m ki/build/libs/ki-1.0.1.jar

5.1 Микросервис модуля отчетов
    java -jar -Xmx512m  rmm/build/libs/rmm-1.0.1.jar
    или
    java -jar -Dspring.profiles.active=prod -Xmx512m rmm/build/libs/rmm-1.0.1.jar

6. Микросервис модуля Communication (port 9213):


7. Микросервис модуля Integration (port 9298):
    java -jar -Xmx512m  invitro/build/libs/invitro-integration-1.0.1.jar
    или
    java -jar -Dspring.profiles.active=prod -Xmx512m invitro/build/libs/invitro-integration-1.0.1.jar


8. Микросервис модуля DMP (port 9297):
    java -jar -Xmx512m  dmp/build/libs/dmp-1.0.1.jar
    или
    java -jar -Dspring.profiles.active=prod -Xmx512m  dmp/build/libs/dmp-1.0.1.jar

9. Микросервис модуля Migration
    java -jar -Xmx512m migration/build/libs/migration-1.0.1.jar
    или
    java -jar -Dspring.profiles.active=prod -Xmx512m migration/build/libs/migration-1.0.1.jar
10. Микросервис модуля ETL
    java -jar -Xmx512m etl/build/libs/etl-1.0.1.jar
    или
    java -jar -Dspring.profiles.active=prod -Xmx512m etl/build/libs/etl-1.0.1.jar

java -jar users/build/libs/users-1.0.1.jar
java -jar jur-person/build/libs/jurperson-1.0.1.jar
java -jar medserv/build/libs/medserv-1.0.1.jar
java -jar results/build/libs/results-1.0.1.jar
java -jar doctors/build/libs/doctors-1.0.1.jar
java -jar user-results/build/libs/user-results-1.0.1.jar


для запуска микросервисов на продакшене:
java -jar -Dspring.profiles.active=prod eureka-1.0.1.jar
java -jar -Dspring.profiles.active=prod users-1.0.1.jar

Пример для запуска на дублирубщем сервере (это для продакшена)
java -jar -Deureka.client.serviceUrl.defaultZone=${EUREKA_URI:http://185.244.217.113:8761/eureka} admin-1.0.1.jar

Пример переопределения порта (это для продакшена)
java -jar -Dserver.port=9220 admin-1.0.1.jar

Запуск с оптимизацией GarbageCollector (для двух-ядерного процессора значение 2, для четырех-ядерного нужно заменить на 4) и ограничением по памяти 4GB
java -jar -XX:+UseParallelGC -XX:ParallelGCThreads=2 -Xmx4096m admin-1.0.1.jar


----------------------------------------------------------------------------------------------------------------
РАБОТА С MONGODB в Studio 3T

1. Переключиться в БД(схему) admin
use admin

2. Показать статус кластера, статус балансера по кластеру (работает в данный момент или нет)
db.printShardingStatus()
sh.getBalancerState()
sh.status()
sh.isBalancerRunning()


3. Сделать БД cloudoc распределенной по кластеру (выполняется только 1 раз, при создании БД)
sh.enableSharding("cloudoc")

4. Переключиться в БД cloudoc
use cloudoc

5. Создание коллекции(таблицы) users
db.createCollection('users');
db.users.ensureIndex({_id: "hashed"});

6. Создание коллекции(таблицы) menus
db.createCollection('menus');
db.menus.ensureIndex({_id: "hashed"});

7. Создание коллекции(таблицы) rights
db.createCollection('rights');
db.rights.ensureIndex({_id: "hashed"});

8. Создание коллекции(таблицы) roles
db.createCollection('roles');
db.roles.ensureIndex({_id: "hashed"});

8.1 Создание коллекции(таблицы) specialities
db.createCollection('specialities');
db.specialities.ensureIndex({_id: "hashed"});

8.2 создание коллекции КИ клинические исследования
db.createCollection('ki');
db.ki.ensureIndex({_id: "hashed"});

8.2 создание коллекции КИ клинические исследования Тип расписании
db.createCollection('schedule_type');
db.schedule_type.ensureIndex({_id: "hashed"});

8.2 создание коллекции session
db.createCollection('session');
db.session.ensureIndex({_id: "hashed"});

и т.д. со всеми коллекциями(таблицами)

9. Переключиться в БД(схему) admin
use admin

10. (!!! обязательно) Сделать коллекции(таблицы) в БД(схеме) cloudoc распределенными, после создания, чтобы коллекция была пустой
     если коллекция уже наполнена какимито данными, то нужно их сохранить путем создания бакапа экспортом данных из этой коллекции,
     в Studio 3T это удобно делается нажатием правой кнопкой мыши на коллекции. Удалить коллекцию, создать заново,
     сделать распределенной и после этого загрузить сохраненные данные путем импорта в Studio 3T
sh.shardCollection("cloudoc.users", {"_id": "hashed"});
sh.shardCollection("cloudoc.menu", {"_id": "hashed"});
sh.shardCollection("cloudoc.rights", {"_id": "hashed"});
sh.shardCollection("cloudoc.roles", {"_id": "hashed"});
sh.shardCollection("cloudoc.ki", {"_id": "hashed"});
и т.д. со всеми коллекциями(таблицами)

11. Запустить балансировку данных по "чанкам" коллекции принудительно ("чанки" должны быть настроены)
    Это нужно делать, если добавили сервер (хотя балансер сам запустится через какое-то время), или при импорте
    в какой-нибудь шард данных
    Частая ошибка разработчиков - это создание коллекции без шардирования по кластеру, т.е. обязательно нужно
    после создания коллекции запускать команду указанную в пункте 10 на созданную коллекцию
    sh.startBalancer() - эту команду нужно запускать только при незапущенном в данный момент балансере,
                         иначе пойдут дупликации данных. Проверить статус можно в пункте 1 sh.isBalancerRunning()
    или
    sh.setBalancerState(true) - так же запускать при незапущенном балансере


12. Создание GridFS коллекции:
    12.1 создать коллекцию в разделе GridFS
    12.2 из под cloudoc создать hashed индекс db.cloudoсfs.files.ensureIndex({_id: "hashed"});
    12.2 из под admin выполнить команду sh.shardCollection("cloudoc.cloudosfs.files", {"_id": "hashed"});

13. Уникальность по двум полям

db.getCollection('fdc_form_field').createIndex( { "formId": 1, "code": 1 }, { unique: true } )