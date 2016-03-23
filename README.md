# Welcome to the Core Standards Project

CoreStandards enables users to create and upload various publications. It includes the following functionalities:

* Create Publication From Spreadsheet
* View Existing Publication
* Download Publication Spreadsheet Instructions
* Download Sample Publication Spreadsheet

## License ##
This project is licensed under the [AIR Open Source License v1.0](http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf).

## Getting Involved ##
We would be happy to receive feedback on its capabilities, problems, or future enhancements:

* For general questions or discussions, please use the [Forum](http://forum.opentestsystem.org/viewforum.php?f=7).
* Use the **Issues** link to file bugs or enhancement requests.
* Feel free to **Fork** this project and develop your changes!

## Module Overview

### Webapp
The Webapp module contains the Corestandards UI and REST APIs.

## Setup
In general, building the code and deploying the WAR file is a good first step.  CoreStandards, however, has a number of other steps that need to be performed in order to fully set up the system.

### Config Folder
Within the file system of the deployment (local file system if running locally or within Tomcat file directories), create a configuration folder structure as follows:
```
{CONFIG-FOLDER-NAME}/progman/
example: /my-app-config/progman/
``` 
Within the deepest folder ('/progman/'), place a file named 'pm-client-security.properties' with the following contents:

```
#security props
oauth.access.url={the URL of OAuth2 access token provider}
pm.oauth.client.id={Client ID for program management client, can be shared amongst all client users or application/consumer specific values}
pm.oauth.client.secret={Password for program management client, can be shared amongst all client users or application/consumer specific values}
pm.oauth.batch.account={OAuth Client id configured in OAM to allow get an OAuth token for the ‘batch' web service call to program management(for loading configs during start up)}
pm.oauth.batch.password={OAuth Client secret/password configured in OAM to allow get an OAuth token for the ‘batch' web service call to program management(for loading configs during start up)}

working example:
oauth.access.url=https://drc-dev-secure.opentestsystem.org/auth/oauth2/access_token?realm=/sbac
pm.oauth.client.id=pm
pm.oauth.client.secret=OAUTHCLIENTSECRET
pm.oauth.batch.account=test@example.com
pm.oauth.batch.password=FaKePassW0rd
```
Add environment variable `-DSB11_CONFIG_DIR` to application server startup as shown in Tomcat (Run Configuration).

### Tomcat (Run Configuration)
Like other SBAC applications, CoreStandards must be set up with active profiles and program management settings.

* `-Dspring.profiles.active` - Active profiles should be comma separated. Typical profiles for the `-Dspring.profiles.active` include:
  * `progman.client.impl.integration` - Use the integrated program management.
  * `progman.client.impl.null` - Use the program management null implementation.
  * `mna.client.integration` - Integrate with the MnA (Monitoring and Alerting) component.
  * `mna.client.null` - Use the null MnA component (i.e., do not integrate with MNA).
* `-Dprogman.baseUri` - This URI is the base URI for where the Program Management REST module is deployed.
*  `-Dprogman.locator` - The locator variable describes which combinations of name and environment (with optional overlay) should be loaded from Program Management. For example: `"component1-urls,dev"` would look up the name component1-urls for the dev environment at the configured REST endpoint.  Multiple lookups can be performed by using a semicolon to delimit the pairs (or triplets with overlay): `"component1-urls,dev;component1-other,dev"`
*  `-DSB11_CONFIG_DIR` - Locator string needed to find the CoreStandards properties to load.
*  `-Djavax.net.ssl.trustStore` - Location of .jks file which contains security certificates for SSO, Program Management and Permission URLs specified inside baseuri and Program Management.
*  `-Djavax.net.ssl.trustStorePassword` - Password string for the keystore.jks file.

```
 Example:
-Dspring.profiles.active="progman.client.impl.integration,mna.client.integration" 
-Dprogman.baseUri=http://<program-management-url>/programmanagement.rest/ 
-Dprogman.locator="corestandards,local" 
-DSB11_CONFIG_DIR=<CONFIG-FOLDER-NAME>
-Djavax.net.ssl.trustStore="<filesystem_dir>/saml_keystore.jks" 
-Djavax.net.ssl.trustStorePassword="xxxxxx"
```

## Program Management Properties
Program Management properties need to be set for running CoreStandards. Example CoreStandards properties can be found at /Documents/Installation/corestandards-progman-config.txt.

#### Database Properties
The following parameters need to be configured inside Program Management for database.

* `datasource.url=jdbc:mysql://<db.url>:<db.port>/<schemaname>` - The JDBC URL of the database from which Connections can and should be acquired. Can be localhost. Port is usually 3306.
* `datasource.username=<db-username>` -  Username that will be used for the DataSource's default getConnection() method. 
* `encrypt:datasource.password=<db-password>` - Password that will be used for the DataSource's default getConnection() method.
* `datasource.driverClassName=com.mysql.jdbc.Driver` - The fully qualified class name of the JDBC driverClass that is expected to provide Connections.
* `datasource.minPoolSize=5` - Minimum number of Connections a pool will maintain at any given time.
* `datasource.acquireIncrement=5` - Determines how many connections at a time datasource will try to acquire when the pool is exhausted.
* `datasource.maxPoolSize=20` - Maximum number of Connections a pool will maintain at any given time.
* `datasource.checkoutTimeout=60000` - The number of milliseconds a client calling getConnection() will wait for a Connection to be checked in or acquired when the pool is exhausted. Zero means wait indefinitely. Setting any positive value will cause the getConnection() call to time out and break with an SQLException after the specified number of milliseconds.
* `datasource.maxConnectionAge=0` - Seconds, effectively a time to live. A Connection older than maxConnectionAge will be destroyed and purged from the pool. This differs from maxIdleTime in that it refers to absolute age. Even a Connection which has not been idle will be purged from the pool if it exceeds maxConnectionAge. Zero means no maximum absolute age is enforced. 
* `datasource.acquireRetryAttempts=5` - Defines how many times datasource will try to acquire a new Connection from the database before giving up. If this value is less than or equal to zero, datasource will keep trying to fetch a Connection indefinitely.
* `datasource.idleConnectionTestPeriod=14400` - If this is a number greater than 0, connection pool manager will test all idle, pooled but unchecked-out connections, every this number of seconds. Default is 300 sec.
* `datasource.testConnectionOnCheckout=false` - If true, an operation will be performed at every connection checkout to verify that the connection is valid. Default is false.
* `datasource.testConnectionOnCheckin=false` - If true, an operation will be performed asynchronously at every connection checkin to verify that the connection is valid. Default is false.

#### MNA (Monitoring and Alerting) properties
The following parameters need to be configured inside program management for MNA.

* `mna.mnaUrl=http://<mna-context-url>/mna-rest/` - URL of the Monitoring and Alerting client server's rest url.
* `mnaServerName=corestandards_dev` -  Used by the MNA clients to identify which server is sending the log/metrics/alerts.
* `mnaNodeName=dev` - Used by the MNA clients to identify who is sending the log/metrics/alerts. There is a discrete mnaServerName and a node in case you want to say XXX for server name & node1/node2 in a clustered environment giving you the ability to search across clustered nodes by server name or specifically for a given node. It’s being stored in the db for metric/log/alert, but not displayed.
* `mna.logger.level=ERROR` - Used to control what is logged to the Monitoring and Alerting system. Logging Levels (ALL - Turn on all logging levels; TRACE, DEBUG, INFO, WARN, ERROR, OFF - Turn off logging).


#### SSO properties
The following parameters need to be configured inside Program Management for SSO.	

* `permission.uri=https://<permission-app-context-url>/rest` - The base URL of the REST API for the Permissions application.
* `component.name=CoreStandards` - The name of the component that this CoreStandards deployment represents. This must match the name of the component in Program Management and the name of the component in the Permissions application.
* `corestandards.security.idp=https://<idp-url>` - The URL of the SAML-based identity provider (OpenAM).
* `corestandards.webapp.saml.metadata.filename=corestandards_local_sp.xml` - Name of OpenAM SP (Service Provider) Metadata file which has been uploaded for the environment, as well as placed inside the server's filesystem. 
* `corestandards.security.dir=file:////<sp-file-location-folder>` - Location of the metadata file.
* `corestandards.security.saml.keystore.cert=<cert-name>` - Name of the Keystore cert being used.
* `corestandards.security.saml.keystore.pass=<password>` - Password for keystore cert.
* `corestandards.security.saml.alias=corestandards_webapp` - Alias for identifying the web application.
* `oauth.tsb.client=tsb` - OAuth Client id configured in OAM to allow the SAML bearer workflow to convert a SAML assertion into an OAuth token for the ‘coordinated web service” call to TSB.
* `oauth.access.url=https://<oauth-url>` - OAuth URL to OAM to allow the SAML bearer workflow to POST to get an OAuth token for any "machine to machine" calls requiring OAUTH.
* `encrypt:oauth.tsb.client.secret=<password>` - OAuth Client secret/password configured in OAM (under the client id) to allow the SAML bearer workflow to convert a SAML assertion into an OAuth token for the "coordinated web service” call to TSB.
* `encrypt:mna.oauth.client.secret=<password>` -  OAuth Client secret/password configured in OAM to allow get an OAuth token for the "batch" web service call to MnA.
* `mna.oauth.client.id=mna` - OAuth Client id configured in OAM to allow get an OAuth token for the "batch" web service call to MnA.
* `encrypt:corestandards.oauth.resource.client.secret=<password>` - OAuth Client secret/password configured in OAM to allow it to get an OAuth token for the "batch" web service call to Core Standards.
* `corestandards.oauth.resource.client.id=corestandards` - OAuth Client id configured in OAM to allow it to get an OAuth token for the "batch" web service call to Core Standards.
* `corestandards.oauth.checktoken.endpoint=http://<oauth-url>` - OAuth URL to OAM to allow the SAML bearer workflow to perform a GET to check that an OAuth token is valid.

## SP Metadata file for SSO
An SP metadata file for configuring the SSO needs to be created. A sample SSO metadata file pointing to localhost can be found at [/Documents/Installation/corestandards_local_sp.xml](https://bitbucket.org/sbacoss/corestandardsdev/src/20c911686bae11e9024e2c3dd7b49c46eb43ab75/Documents/Installation/corestandards_local_sp.xml?at=default)
The entity id and URL must be changed according to the environment. Upload this file to OpenAM and place it inside the server file system.
The `corestandards.webapp.saml.metadata.filename` and `corestandards.security.dir` values must be set in Program Management to reflect the metadata file name and location.
```
Example:
corestandards.webapp.saml.metadata.filename=corestandards_local_sp.xml
corestandards.security.dir=file:////usr/securitydir
```


## Build
These are the steps that should be taken in order to build all of the CoreStandards related artifacts.

### Pre-Dependencies
* Tomcat 6 or higher
* Maven (mvn) version 3.X or higher installed
* Java 7
* Access to sb11-shared-build repository
* Access to sb11-shared-code repository
* Access to sb11-security repository
* Access to sb11-rest-api-generator repository
* Access to sb11-program-management repository
* Access to sb11-monitoring-alerting-client repository

### Build order

If building all components from scratch the following build order is needed:

* sb11-shared-security
* sb11-shared-code
* prog-mgmnt-client
* prog-mgmnt-client-null-impl
* monitoring-alerting.client-null-impl
* monitoring-alerting.client

## Dependencies
CoreStandards has a number of direct dependencies that are necessary for it to function.  These dependencies are already built into the Maven POM files.

### Compile Time Dependencies
* sb11-shared-security
* sb11-shared-code
* prog-mgmnt-client
* prog-mgmnt-client-null-impl
* monitoring-alerting.client-null-impl
* monitoring-alerting.client
* mysql-connector-java
* spring-context
* jackson-mapper-asl
* spring-webmvc
* spring-web
* org.springframework
* spring-expression
* spring-core
* spring-beans
* spring-security-config
* spring-security-web
* spring-security-taglibs
* aspectjrt
* spring-security-core
* xercesImpl
* slf4j-api
* jcl-over-slf4j
* slf4j-log4j12
* log4j
* javax.inject
* c3p0
* unoil
* juh
* jurt
* ridl
* commons-fileupload
* commons-io

### Test Dependencies
* junit
* spring-test
* spring-mock

### Runtime Dependencies
* Servlet API