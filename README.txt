1. Fix the junit test configuration in org.air.contentstandards\src\test\resources\run.properties
2. Copy the mysql-connector-java-5.1.22-bin.jar mysql jar to $CATALINA_HOME/lib (if using TC server in STS, place that JAR file in \vfabric-tc-server-developer-2.7.2.RELEASE\base-instance\lib 
3. Add JDBC resource information into Tomcat's context.xml file in $CATALINA_HOME/conf:

    <Resource name="jdbc/contentstandards" auth="Container"
		type="javax.sql.DataSource" username="db-username" password="db-username-password"
		driverClassName="com.mysql.jdbc.Driver"
		url="jdbc:mysql://mysql-server-fqdn:3306/StandardsRepository_Dev"
		validationQuery="select 1" maxActive="5" maxIdle="2" removeAbandoned="true"
		logAbandoned="true" />
	<Parameter name="ooExecPath"
		value="C:\Program Files (x86)\OpenOffice 4\program\" override="false" />
	<Parameter name="ooSpreadsheetPath" value="C:\Workspace\temp"
		override="false" />	
	<Parameter name="logger.coreStandardsDevLogPath" value="C:\Workspace\temp\tomcat\logs"
		override="false" />
	<Parameter name="logger.debuglevel" override="false" value="ERROR" />

   Note: name = ooExecPath ---> value = path where OpenOffice is installed on your machine
    	 name = ooSpreadsheetPath ---> value = path to the working area where your spreadsheet file is located  
         mysql-server-fqdn (in url) = fully-qualified domain name of the MySQL server being used to host the Standards Repository.
(Sample context xml)
         db-username and db-username-password = credentials for access to the Standards Repository DB on the mysql server.

4. In STS: windows-->Preferences-->Server-->Runtime Environment-->select server and click on edit button-->change installation directory to your existing server location \springsource\vfabric-tc-server-developer-2.7.2.RELEASE
5. In STS: right click on server in STS -->preferences-->click on switch location
