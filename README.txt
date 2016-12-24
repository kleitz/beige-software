site: http://www.beigesoft.org
or https://sites.google.com/site/beigesoftware

version 1.1.3.
Change conformation dialog
Fix ID appearance
Fix several mistakes

version 1.1.2.
!05 Dec 2016 fixed error that raised only on MS Windows "not found \beige-orm\persistance-sqlite.xml"
Added balance sheet report.
Added import data from Tax accounting into Market (business) one.
Added Other loses, Retained looses accounts
Added services purchased (into purchase invoice)
Added services to sell (into sales invoice)
Added gross income revenue account SalesServices with subaccount ServiceToSaleCategory

version 1.1.1.
Added import full database copy with WEB-service (e.g. from SQlite to MySql).
Added payByDate to sales/purchase invoices to report invoices list with payByDate conditions. 
Added MoveItems to move items within warehouse/between warehouses.
Added BeginningInventory that makes accounting (Debit Inventory only) entries, and its lines make warehouse load entries.

Files overview.
* Beige-Common Java library. It contains common abstractions, some implementations, some models, e.g. IFactory, IDelegate, Srvi18N, APersistableBase.
* Beige-Settings library. It helps to make quickly settings for class and its fields with properties XML.
    Instead of create a lot of files to describe every class and its fields it use describing by type, by name,
    e.g. setting "java.lang.Integer"-"INTEGER NOT NULL" will be assigned for every field of this type.
* Beige-ORM library is simple lightweight multi-platform (JDBC/Android) ORM library.
  It supports Postgresql, MySql, H2 and SQlite.
* Beige-JDBC used with Beige-ORM for JDBC platform.
* Beige-Replicator replicate/persist any entity according XML settings and user's requirements with a file or network (HTTP). Right now it has implemented XML format of stored/transferred data.
* Beige-WEB-Jar contains of servlets, factory and other Java, properties and UVD settings XML files for Beige-WEB.
* Beige-WEB is CRUD interface based on standard JEE MVC (servlet, JSP, JSTL) and AJAX, JSON. 
  It renders forms (include entity pickers) and lists of any entity according XML settings.
  It contains only non-java webapp files: JSP, js, css, web.xml.
* Beige-Accounting consist of accounting models and services, e.g. SalesInvoice and SrvSalesInvoice.
* Beige-Accounting-Web-Jar contains of servlets, application factory and other Java, properties and UVD settings XML files for Beige-Accounting-Web.
* Beige-Accounting-Web is BeigeAccounting web application that contains of JSP, JS, CSS files.
* Beige-Accounting-AJetty is BeigeAccounting based on embedded A-Jetty for standard Java and SQLite.
* Beige-Android-Jar is library for Android that consist of implementation of database service for use in Beige-ORM and some other Java classes.
* Beige-Android-Test is tests of Beige-ORM for Android platform.
* Beige-Accounting-Android is Beige Accounting on embedded A-Jetty for Android.

There are dependencies:

https://github.com/demidenko05/a-javabeans - sources of OpenJDK7 javabeans adapted for Android (a-tomcat and a-jetty use it).

https://github.com/demidenko05/a-tomcat-all - source of Apache Tomcat to precompile JSP/JSTL for A-Jetty.

https://github.com/demidenko05/a-jetty-all - source of A-Jetty is Jetty 9.2 adapted for Android

Prerequisites for building from source:
* JDK7 (not 8, jdk 8 can not compile a-javabeans, but can run it).
* last of Apache Maven and Ant.
* MySql 5.1.7+ with registered user/password "beigeaccounting/beigeaccounting" and created databases "beigeaccounting" and "beigeaccountingtest".
* Android SDK without Studio and downloaded last version and 19 API. It requires some 32bit libs for 64bit Fedora (dnf install glibc.i686 glibc-devel.i686 libstdc++.i686 zlib-devel.i686 ncurses-devel.i686 libX11-devel.i686 libXrender.i686 libXrandr.i686)
* Google Chrome, Opera, or Chromium browser (html5-dialog ready).

SQlite is already inside JDBC driver (its size is 5MB)

Installation:
All software are installed by simple "mvn clean install".
Test web application Beige-WEB can be started by "mvn clean install tomcat7:run -P webtest" then open "http://localhost:8080/beige-web/"
but after that run "mvn clean install" to install it as WEB library.
Web application beige-accounting-web can be started by "mvn clean install tomcat7:run -P mysql"
or just copy war file inside a JEE server, also copy mysql-jdbc-connector5.1.40.jar and HikariCP-2.4.3.jar into [server]/lib.

You can make your own JEE(JSP/JSTL) web-app to run on embedded A-Jetty for Standard Java or Android Java.
To do this:
1. install all maven projects (a-javabeans, a-tomcat, a-jetty).
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
11. copy directory server from source anywhere and copy there your jar with dependencies
12. start your jar with dependencies "java -jar [your jar]"
13. to make your webapp working on embedded A-Jetty on Android see example beige-accounting-android.
