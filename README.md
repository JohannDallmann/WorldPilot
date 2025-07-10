# WorldPilot

### Before starting the application:
* define environment variables in an .env-file in the WorldPilot-root-path, which should due to safety reasons not be submitted to git. They are necessary for docker-compose. It should contain the following attributes:
  * MYSQL_ROOT_PASSWORD
  * MYSQL_DATABASE
  * MYSQL_USER
  * MYSQL_PASSWORD
  * MYSQL_KEYCLOAK_DATABASE
  * KEYCLOAK_ADMIN
  * KEYCLOAK_ADMIN_PASSWORD
* define environment variables in the "location-service"-path for the "run LocationServiceApplication" locally for the IDE, as they are used in the application.properties. They should contain the same DB-credentials as in the .env-file and are also important to connect with MySQL-workbench:
  * SPRING_DATASOURCE_USERNAME
  * SPRING_DATASOURCE_PASSWORD

### How to start the application:

* start Rancher Desktop (or another Containerization Tool)
* navigate to WorldPilot-root-path (e.g. shift+right click in Explorer, open Powershell) and execute "docker-compose up -d" to start MySQL-DB and Keycloak
* Backend is not dockerized and worldpilot-webapp is not implemented yet
* start backend-application locally in order to create DB-tables initially
* access [Keycloak-GUI](http://localhost:8090) and login with credentials defined in environment variables in .env-file (KEYCLOAK_ADMIN, KEYCLOAK_ADMIN_PASSWORD)
* create a new realm. Import "realm-export.json" from the config-package in location-service
* create a new "testuser" in the realm, create a password "testuser", mark password as not temporary, assign all user attributes, enable "email verified", map realm-role to created user
* The user can now be used to get a valid token: POST "http://localhost:8090/realms/worldpilot/protocol/openid-connect/token" via postman with the following body-attributes ("x-www-form-urlencoded"): client_id, username, password, grant_type
* The issued token can now be used for every request with Autorization "Bearer Token"
  * GetLocationPage awaits a paginated-object. Use "http://localhost:8080/locations?page=1&size=3&sort=id,desc" as URL.
  * For setting filters, at least an empty JSON {} needs to be submitted as request body.
* All endpoints can also be accessed via [swagger](http://localhost:8080/swagger-ui/index.html). Via Authorize a valid token needs to be applied.
* "docker-compose -v down" shuts down and deletes the volume. After restart all DB-data and Realm-data has to be recreated.
* "docker-compose up --build --force-recreate -d" rebuilds all containers

### Database:
* In order to persist keycloak-data, a second DB is necessary. It is started via "mysql/init/create-keycloak-db.sql", because only one DB can be started automatically.
* Instead of deleting the whole volume of the DB, it might make more sense to just execute "DROP TABLE locations;" to delete the whole table or empty the table with "TRUNCATE TABLE locations;" in MySQL-workbench.
* For adding a new location via workbench a script is needed in order to enter a UUID properly.
* The ShadowEntity "LocationDuplicateEntity" always needs to have the same columns as "LocationEntity" to be recognized by the table.

### Good to know:
* location-service uses Mapstruct for mapping: if Entities change, the new mapper needs to be activated via "mvn clean install"

