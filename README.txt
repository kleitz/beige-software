site: http://www.beigesoft.org

1.1.0-SNAPSHOT - A lot of mistakes was fixed (price/cost rounding, etc.).

30 Aug 2016 - 2 Sept 2016: Fixed bugs on Android version. It's checked on Android 7, 6, 5.5.1, 5.0.1, 4.2.2
For standalone version (A-Jetty, Android) when you already use a database and switch into another you can get error on balance or ledger report,
so restart server and immediately switch to desired database (this bug will be fixed).


Files overview.

beige-software-[VERSION]-src.tar.bz2 - source of:
* Beige-Common Java library. It contains common abstractions, some implementations, some models, e.g. IFactory, IDelegate, Srvi18N, APersistableBase.
* Beige-Settings library. It helps to quickly make settings for class and its fields of with properties XML.
    Instead of create a lot of files to describe every class and its fields it use describing by type, by name,
    e.g. setting "java.lang.Integer"-"INTEGER NOT NULL" will be assigned for every field of this type.
* Beige-ORM library is simple lightweight multi-platform (JDBC/Android) ORM library.
  It supports Postgresql, H2 and SQlite.
* Beige-JDBC used with Beige-ORM for JDBC platform.
* Beige-WEB-jar contains of servlets, factory and other Java files for Beige-WEB.
* Beige-WEB is CRUD interface based on standard JEE MVC (servlet, JSP, JSTL) and AJAX, JSON. 
  It renders forms (include entity pickers) and lists of any entity according XML settings.
  It contains only non-java webapp files: JSP, js, css, web.xml.
* Beige-Accounting consist of accounting models and services, e.g. SumpplierInvoice.
* Beige-Accounting-Jar contains of servlets, factoty and other Java files for Beige-Accounting-Web.
* Beige-Accounting-Web is web application that contains of JSP, JS, CSS files.
* Beige-Accounting-AJetty is Web based Accounting on embedded A-Jetty for standard Java and SQLite.
* Beige-Android -database used with Beige-ORM for Android platform.
* Beige-Android-Test tests of Beige-ORM for Android platform.
* Beige-Accounting-Android is Beige Accounting on embedded A-Jetty for Android.

beige-accounting-ajetty.zip - precompiled web-application with embedded A-Jetty for standard Java (*NIX, MS Windows, MAC) (required Google Chrome browser, JRE7+, SQLite).

beige-accounting-android.apk - precompiled web-application with embedded A-Jetty for Android (required Google Chrome browser).

bobs-pizza.sqlite - demo database

There are dependencies at folder 29June2016:

a-javabeans-1.0.0-SNAPSHOT-src.tar.bz2 - sources of OpenJDK7 javabeans adapted for Android (a-tomcat and a-jetty use it).

a-tomcat-all-1.0.3-SNAPSHOT-src.tar.bz2 - source of Apache Tomcat to precompile JSP/JSTL for A-Jetty.

a-jetty-all-1.0.1-SNAPSHOT-src.tar.bz2 -source of A-Jetty is Jetty 9.2 adapted for Android

Prerequisites for building from source:
* JDK7.
* last of Apache Maven and Ant.
* Postgresql 9.4+ with registered user/password "beigeaccounting/beigeaccounting" and created databases "beigeaccounting" and "beigeaccountingtest".
* SqlLite last version.
* Android SDK without Studio, loaded last version and 19 API. It requred some libs for 64bit Linux (apt-get install lib32z1 lib32ncurses5 lib32stdc++6)
* Google Chrome browser (html5-dialog ready).

Installation:
All software are installed by simple "mvn clean install".
Test web application Beige-WEB can be started by "mvn clean install tomcat7:run -P webtest"
but after that run "mvn clean install" to install it as WEB library.
Web application beige-accounting-web can be started by "mvn clean install tomcat7:run -P sqlite"
or just copy war file inside a JEE server, also copy sqlite-jdbc-3.8.11.2.jar and HikariCP-2.4.3.jar into [server]/lib.

You can make your own JEE(JSP/JSTL) web-app to run on embedded A-Jetty for Standard Java or Android.
To do this:
1. install all maven projects (a-javabeans, a-tomcat, a-jetty, beige-software).
2. copy a-tomcat-all/a-apache-jspc/target/a-apache-jspc-jar-with-dependencies.jar into folder a-apache-tomcat
3. copy a-tomcat-all/a-apache-jspc/src/main/resources/catalina-tasks.xml into folder a-apache-tomcat
4. add path a-apache-tomcat to environment variables as TOMCATA_HOME
5. Create maven project as jar project, copy your java sources (servlets etc.) into it.
6. Create folders server/webapp in [your-project]/src/main then copy your static files/folders (html, js, css, web.xml) into webapp
5. Add into your project maven dependences according example beige-accounting-ajetty
6. Copy a-jetty-all/webapp-test/build.xml into your old web-app project then run there "$ANT_HOME/bin/ant -Dtomcat.home=$TOMCATA_HOME -Dwebapp.path=target/[your-project-name]/".
7. Move generated servlets sources from [your-old-web-project]/target/[your-project-name]/WEB-INF/src into new project [your-project]/src/java.
8. Add servlets from generated generated_web.xml into your web.xml.
9. add maven-assembly-plugin (see beige-accounting-ajetty/pom.xml) to generate jar with dependencies with mainClass -> org.beigesoft.ajetty.BootStrapEmbedded
10. install new maven project
11. copy directory server from source anywhere and copy there your jar with dependences
12. start your jar with dependences "java -jar [your jar]"
13. to make your webapp working on embedded A-Jetty on Android see example beige-accounting-android.
