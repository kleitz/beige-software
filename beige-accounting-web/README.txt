beige-accounting-web.war is ordinal JEE WEB application.

To install application in your JEE server, for example Apache Tomcat 7:
1. you should have MySql server with created user and empty database
2. make sure that Tomcat has libraries: HikariCP-2.4.3.jar, mysql-connector-java-5.1.40.jar, slf4j-api-1.7.12.jar (versions may be different)
3. Unpack WAR file and change user/password/database with yours ones in WEB-INF/classes/beige-orm/mysql/app-settings.xml:
<entry key="databaseName">[yourdbname]</entry>
<entry key="userName">[yourdbusername]</entry>
<entry key="userPassword">[yourdbuserpass]</entry>
<entry key="databaseUrl">[yourdburl]</entry>
  Pack new WAR file (it is actually ZIP archive).
4. copy WAR file inside "[tomcat_home]/webapps"
5. type in browser address same as WAR file i.e. "beige-accounting-web"
6. after creating database add users with MySql query:
insert into USERTOMCAT values ('admin', '[strongpassword]');
insert into USERTOMCAT values ('user', '[strongpassword]');
insert into USERROLETOMCAT  values ('admin', 'admin');
insert into USERROLETOMCAT  values ('user', 'user');

If you has different JEE server, then you have to make JEE-authentication by himself. E.g. Jetty uses different database tables (USERJETTY ...). You may need to reassemble WAR with new XML settings.

license:

GNU General Public License version 2 - http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html

3-D PARTY LICENSES:

CSS/Javascript framework Bootstrap by Twitter, Inc:
MIT License
https://github.com/twbs/bootstrap/blob/master/LICENSE

JQuery by JS Foundation and other contributors:
MIT license
https://jquery.org/license

DejaVu fonts by Bitstream:
https://dejavu-fonts.github.io/License.html

site: http://www.beigesoft.org
or https://sites.google.com/site/beigesoftware

