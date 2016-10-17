package org.beigesoft.web.factory;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Date;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.HashMap;

import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.orm.service.ASrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.service.ISrvRecordRetriever;
import org.beigesoft.orm.service.SrvEntitySimple;
import org.beigesoft.orm.service.SrvEntityOwnedSimple;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.service.SrvI18n;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.web.service.SrvWebEntity;
import org.beigesoft.web.service.SrvWebMvc;
import org.beigesoft.web.service.SrvPage;
import org.beigesoft.web.service.MngSoftware;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.log.ILogger;
import org.beigesoft.web.service.UtlJsp;
import org.beigesoft.service.UtilXml;
import org.beigesoft.replicator.service.ClearDbThenGetAnotherCopyXmlHttp;
import org.beigesoft.replicator.service.DatabaseWriterXml;
import org.beigesoft.replicator.service.DatabaseReaderIdenticalXml;
import org.beigesoft.replicator.service.SrvEntityReaderXml;
import org.beigesoft.replicator.service.ISrvEntityFieldFiller;
import org.beigesoft.replicator.service.ISrvFieldWriter;
import org.beigesoft.replicator.service.SrvEntityFieldPersistableBaseRepl;
import org.beigesoft.replicator.service.SrvEntityFieldUserTomcatRepl;
import org.beigesoft.replicator.service.SrvEntityFieldHasIdStringRepl;
import org.beigesoft.replicator.service.SrvEntityFieldHasIdLongRepl;
import org.beigesoft.replicator.service.SrvEntityFieldFillerStd;
import org.beigesoft.replicator.service.SrvEntityWriterXml;
import org.beigesoft.replicator.service.SrvFieldWriterXmlStd;
import org.beigesoft.replicator.service.SrvFieldHasIdWriterXml;

/**
 * <p>Application beans factory for any RDBMS.
 * This is simple, cheap but powerful alternative to CDI.
 * </p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public abstract class AFactoryAppBeans<RS> implements IFactoryAppBeans {

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

  /**
   * <p>Properties service.</p>
   **/
  private UtlProperties utlProperties;

  /**
   * <p>Utils JSP.</p>
   */
  private UtlJsp utlJsp;

  /**
   * <p>SrvI18n.</p>
   */
  private SrvI18n srvI18n;

    /**
   * <p>Is show debug messages.</p>
   **/
  private boolean isShowDebugMessage = true;

  /**
   * <p>ORM Settings Directory. Must be settled!</p>
   **/
  private String ormSettingsDir = "beige-orm";

  /**
   * <p>ORM Settings Base File. Must be settled!</p>
   **/
  private String ormSettingsBaseFile = "persistence-sqlite.xml";

  /**
   * <p>UVD Settings Directory. Must be settled!</p>
   **/
  private String uvdSettingsDir = "beige-uvd";

  /**
   * <p>UVD Settings Base File. Must be settled!</p>
   **/
  private String uvdSettingsBaseFile = "base.xml";

  /**
   * <p>Manager UVD settings.</p>
   **/
  private MngSettings mngUvdSettings;

  /**
   * <p>Entity service.</p>
   **/
  private SrvWebEntity srvWebEntity;

  /**
   * <p>Page service.</p>
   */
  private SrvPage srvPage;

  /**
   * <p>Software manager.</p>
   */
  private MngSoftware mngSoftware;

  /**
   * <p>ORM service.</p>
   **/
  private ASrvOrm<RS> srvOrm;

  /**
   * <p>Service that fill entity from request.</p>
   **/
  private SrvWebMvc<RS> srvWebMvc;

  /**
   * <p>Helper to create Insert Update statement
   * by Android way.</p>
   **/
  private HlpInsertUpdate hlpInsertUpdate;

  /**
   * <p>Service that read database from XML message.</p>
   **/
  private ClearDbThenGetAnotherCopyXmlHttp<RS> srvGetDbCopyXml;

  /**
   * <p>Service that read database from XML message.</p>
   **/
  private DatabaseWriterXml<RS> databaseWriter;

  /**
   * <p>Manager Replicator settings.</p>
   **/
  private MngSettings mngSettingsGetDbCopy;

  /**
   * <p>Service escape XML.</p>
   **/
  private UtilXml utilXml;

  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entitiesMap;

  /**
   * <p>Database Name or JDBC URL.</p>
   **/
  private String databaseName;

  /**
   * <p>Database user.</p>
   **/
  private String databaseUser;

  /**
   * <p>Database password.</p>
   **/
  private String databasePassword;

  /**
   * <p>Beans map.</p>
   **/
  private Map<String, Object> beansMap = new HashMap<String, Object>();

  /**
   * <p>Factory Over Beans.</p>
   **/
  private IFactoryAppBeans factoryOverBeans;

  /**
   * <p>Last request bean time. It's used to release memory (beans)
   * after N minutes of idle.</p>
   **/
  private long lastRequestTime = 0L;

  /**
   * <p>It's used to release memory (beans)
   * after N minutes of idle.</p>
   **/
  private int minutesOfIdle = 30;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGet(
    final String pBeanName) throws Exception {
    setLastRequestTime(new Date().getTime());
    if ("ISrvOrm".equals(pBeanName)) {
      return lazyGetSrvOrm();
    } else if ("clearDbThenGetAnotherCopyHttp".equals(pBeanName)) {
      return lazyGetClearDbThenGetAnotherCopyXmlHttp();
    } else if ("IDatabaseWriter".equals(pBeanName)) {
      return lazyGetDatabaseWriter();
    } else if ("IUtilXml".equals(pBeanName)) {
      return lazyGetUtilXml();
    } else if ("ILogger".equals(pBeanName)) {
      return lazyGetLogger();
    } else if ("IMngSoftware".equals(pBeanName)) {
      return lazyGetMngSoftware();
    } else if ("ISrvI18n".equals(pBeanName)) {
      return lazyGetSrvI18n();
    } else if ("UtlJsp".equals(pBeanName)) {
      return lazyGetUtlJsp();
    } else if ("ISrvWebEntity".equals(pBeanName)) {
      return lazyGetSrvWebEntity();
    } else if ("ISrvRecordRetriever".equals(pBeanName)) {
      return lazyGetSrvRecordRetriever();
    } else if ("ISrvDatabase".equals(pBeanName)) {
      return lazyGetSrvDatabase();
    } else if ("ISrvPage".equals(pBeanName)) {
      return lazyGetSrvPage();
    } else if ("ISrvWebMvc".equals(pBeanName)) {
      return lazyGetSrvWebMvc();
    } else if ("mngUvdSettings".equals(pBeanName)) {
      return lazyGetMngUvdSettings();
    } else if ("UtlProperties".equals(pBeanName)) {
      return lazyGetUtlProperties();
    } else if ("UtlReflection".equals(pBeanName)) {
      return lazyGetUtlReflection();
    } else if ("HlpInsertUpdate".equals(pBeanName)) {
      return lazyGetHlpInsertUpdate();
    } else {
      Object bean = lazyGetOtherBean(pBeanName);
      if (bean != null) {
        return bean;
      } else {
        if (this.factoryOverBeans != null) {
          bean = this.factoryOverBeans.lazyGet(pBeanName);
        }
        if (bean != null) {
          return bean;
        }
        //common entity services
        bean = lazyGetSrvEntity(pBeanName);
        if (bean != null) {
          return bean;
        }
      }
      lazyGetLogger().info(AFactoryAppBeans.class,
        "There is no bean for name " + pBeanName);
      return null;
    }
  }

  //To override:
  /**
   * <p>Get other bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  public abstract Object lazyGetOtherBean(
    final String pBeanName) throws Exception;

  /**
   * <p>Instantiate ORM  service.</p>
   * @return ASrvOrm<RS> - ORM  service
   */
  public abstract ASrvOrm<RS> instantiateSrvOrm();

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return SrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  public abstract ILogger lazyGetLogger() throws Exception;

  /**
   * <p>Get SrvRecordRetriever in lazy mode.</p>
   * @return SrvRecordRetriever - SrvRecordRetriever
   * @throws Exception - an exception
   */
  public abstract ISrvRecordRetriever<RS>
    lazyGetSrvRecordRetriever() throws Exception;

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return ISrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  public abstract ISrvDatabase<RS> lazyGetSrvDatabase() throws Exception;

  //Work:
  /**
   * <p>Get SrvOrmH2 in lazy mode.</p>
   * @return SrvOrmH2 - SrvOrmH2
   * @throws Exception - an exception
   */
  public final synchronized ASrvOrm<RS>
    lazyGetSrvOrm() throws Exception {
    if (this.srvOrm == null) {
      this.srvOrm = instantiateSrvOrm();
      this.srvOrm.setSrvRecordRetriever(lazyGetSrvRecordRetriever());
      this.srvOrm.setSrvDatabase(lazyGetSrvDatabase());
      this.srvOrm.setSrvSqlEscape(new SrvSqlEscape());
      this.srvOrm.setHlpInsertUpdate(lazyGetHlpInsertUpdate());
      this.srvOrm.setLogger(lazyGetLogger());
      MngSettings mngOrmSettings = new MngSettings();
      mngOrmSettings.setLogger(lazyGetLogger());
      this.srvOrm.setMngSettings(mngOrmSettings);
      this.srvOrm.loadConfiguration(this.ormSettingsDir,
        this.ormSettingsBaseFile);
      this.srvOrm.initializeDatabase();
      this.entitiesMap = new HashMap<String, Class<?>>();
      for (Class<?> clazz : this.srvOrm.getMngSettings()
        .getClasses()) {
        this.entitiesMap.put(clazz.getSimpleName(), clazz);
      }
      lazyGetLogger().info(AFactoryAppBeans.class,
        "ASrvOrm has been created.");
    }
    return this.srvOrm;
  }

  /**
   * <p>Handler of event on database changed,
   * right now SQlite DB is able to be changed.</p>
   * @throws Exception - an exception
   */
  public final synchronized void handleDatabaseChanged() throws Exception {
    releaseBeans();
  }

  /**
   * <p>Get ClearDbThenGetAnotherCopyXmlHttp in lazy mode.</p>
   * @return ClearDbThenGetAnotherCopyXmlHttp - ClearDbThenGetAnotherCopyXmlHttp
   * @throws Exception - an exception
   */
  public final synchronized ClearDbThenGetAnotherCopyXmlHttp<RS>
    lazyGetClearDbThenGetAnotherCopyXmlHttp() throws Exception {
    if (this.srvGetDbCopyXml == null) {
      this.srvGetDbCopyXml = new ClearDbThenGetAnotherCopyXmlHttp<RS>();
      SrvEntityReaderXml srvEntityReaderXml = new SrvEntityReaderXml();
      srvEntityReaderXml.setUtilXml(lazyGetUtilXml());
      SrvEntityFieldFillerStd srvEntityFieldFillerStd =
        new SrvEntityFieldFillerStd();
      srvEntityFieldFillerStd.setUtlReflection(lazyGetUtlReflection());
      SrvEntityFieldPersistableBaseRepl srvEntityFieldPersistableBaseRepl =
        new SrvEntityFieldPersistableBaseRepl();
      srvEntityFieldPersistableBaseRepl
        .setUtlReflection(lazyGetUtlReflection());
      SrvEntityFieldHasIdStringRepl srvEntityFieldHasIdStringRepl =
        new SrvEntityFieldHasIdStringRepl();
      srvEntityFieldHasIdStringRepl
        .setUtlReflection(lazyGetUtlReflection());
      SrvEntityFieldHasIdLongRepl srvEntityFieldHasIdLongRepl =
        new SrvEntityFieldHasIdLongRepl();
      srvEntityFieldHasIdLongRepl
        .setUtlReflection(lazyGetUtlReflection());
      SrvEntityFieldUserTomcatRepl srvEntityFieldUserTomcatRepl =
        new SrvEntityFieldUserTomcatRepl();
      Map<String, ISrvEntityFieldFiller> fieldsFillersMap =
        new HashMap<String, ISrvEntityFieldFiller>();
      fieldsFillersMap.put("SrvEntityFieldFillerStd",
        srvEntityFieldFillerStd);
      fieldsFillersMap.put("SrvEntityFieldPersistableBaseRepl",
        srvEntityFieldPersistableBaseRepl);
      fieldsFillersMap.put("SrvEntityFieldHasIdStringRepl",
        srvEntityFieldHasIdStringRepl);
      fieldsFillersMap.put("SrvEntityFieldHasIdLongRepl",
        srvEntityFieldHasIdLongRepl);
      fieldsFillersMap.put("SrvEntityFieldUserTomcatRepl",
        srvEntityFieldUserTomcatRepl);
      srvEntityReaderXml.setFieldsFillersMap(fieldsFillersMap);
      srvEntityReaderXml.setMngSettings(lazyGetMngSettingsGetDbCopy());
      DatabaseReaderIdenticalXml<RS> databaseReaderIdenticalXml =
       new DatabaseReaderIdenticalXml<RS>();
      databaseReaderIdenticalXml.setUtilXml(lazyGetUtilXml());
      databaseReaderIdenticalXml.setSrvEntityReader(srvEntityReaderXml);
      databaseReaderIdenticalXml.setSrvDatabase(lazyGetSrvDatabase());
      databaseReaderIdenticalXml.setSrvOrm(lazyGetSrvOrm());
      databaseReaderIdenticalXml.setLogger(lazyGetLogger());
      this.srvGetDbCopyXml.setUtilXml(lazyGetUtilXml());
      this.srvGetDbCopyXml.setSrvEntityReaderXml(srvEntityReaderXml);
      this.srvGetDbCopyXml.setMngSettings(lazyGetMngSettingsGetDbCopy());
      this.srvGetDbCopyXml.setSrvDatabase(lazyGetSrvDatabase());
      this.srvGetDbCopyXml.setDatabaseReader(databaseReaderIdenticalXml);
      this.srvGetDbCopyXml.setLogger(lazyGetLogger());
      lazyGetLogger().info(AFactoryAppBeans.class,
        "ClearDbThenGetAnotherCopyXmlHttp has been created.");
    }
    return this.srvGetDbCopyXml;
  }

  /**
   * <p>Get DatabaseWriterXml in lazy mode.</p>
   * @return DatabaseWriterXml - DatabaseWriterXml
   * @throws Exception - an exception
   */
  public final synchronized DatabaseWriterXml<RS> lazyGetDatabaseWriter(
    ) throws Exception {
    if (this.databaseWriter == null) {
      this.databaseWriter = new DatabaseWriterXml<RS>();
      SrvEntityWriterXml srvEntityWriterXml = new SrvEntityWriterXml();
      SrvFieldWriterXmlStd srvFieldWriterXmlStd = new SrvFieldWriterXmlStd();
      srvFieldWriterXmlStd.setUtilXml(lazyGetUtilXml());
      SrvFieldHasIdWriterXml srvFieldHasIdWriterXml =
        new SrvFieldHasIdWriterXml();
      srvFieldHasIdWriterXml.setUtilXml(lazyGetUtilXml());
      srvEntityWriterXml.setMngSettings(lazyGetMngSettingsGetDbCopy());
      srvEntityWriterXml.setUtlReflection(lazyGetUtlReflection());
      Map<String, ISrvFieldWriter> fieldsWritersMap =
        new HashMap<String, ISrvFieldWriter>();
      fieldsWritersMap.put("SrvFieldWriterXmlStd", srvFieldWriterXmlStd);
      fieldsWritersMap.put("SrvFieldHasIdWriterXml", srvFieldHasIdWriterXml);
      srvEntityWriterXml.setFieldsWritersMap(fieldsWritersMap);
      this.databaseWriter.setLogger(lazyGetLogger());
      this.databaseWriter.setSrvEntityWriter(srvEntityWriterXml);
      this.databaseWriter.setSrvDatabase(lazyGetSrvDatabase());
      this.databaseWriter.setSrvOrm(lazyGetSrvOrm());
      lazyGetLogger().info(AFactoryAppBeans.class,
        "DatabaseWriterXml has been created.");
    }
    return this.databaseWriter;
  }

  /**
   * <p>Get MngSettings for replicator in lazy mode.</p>
   * @return MngSettings - MngSettings replicator
   * @throws Exception - an exception
   */
  public final synchronized MngSettings lazyGetMngSettingsGetDbCopy(
    ) throws Exception {
    if (this.mngSettingsGetDbCopy == null) {
      this.mngSettingsGetDbCopy = new MngSettings();
      this.mngSettingsGetDbCopy.setLogger(lazyGetLogger());
      this.mngSettingsGetDbCopy
        .loadConfiguration("beige-get-db-copy", "base.xml");
      lazyGetLogger().info(AFactoryAppBeans.class,
        "MngSettings replicator has been created.");
    }
    return this.mngSettingsGetDbCopy;
  }

  /**
   * <p>Get UtilXml in lazy mode.</p>
   * @return UtilXml - UtilXml
   * @throws Exception - an exception
   */
  public final synchronized UtilXml lazyGetUtilXml(
    ) throws Exception {
    if (this.utilXml == null) {
      this.utilXml = new UtilXml();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "UtilXml has been created.");
    }
    return this.utilXml;
  }

  /**
   * <p>Get HlpInsertUpdate in lazy mode.</p>
   * @return HlpInsertUpdate - HlpInsertUpdate
   * @throws Exception - an exception
   */
  public final synchronized HlpInsertUpdate lazyGetHlpInsertUpdate(
    ) throws Exception {
    if (this.hlpInsertUpdate == null) {
      this.hlpInsertUpdate = new HlpInsertUpdate();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "HlpInsertUpdate has been created.");
    }
    return this.hlpInsertUpdate;
  }

  /**
   * <p>Get UtlJsp in lazy mode.</p>
   * @return UtlJsp - UtlJsp
   * @throws Exception - an exception
   */
  public final synchronized UtlJsp lazyGetUtlJsp(
    ) throws Exception {
    if (this.utlJsp == null) {
      this.utlJsp = new UtlJsp();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "UtlJsp has been created.");
    }
    return this.utlJsp;
  }

  /**
   * <p>Get MngSoftware in lazy mode.</p>
   * @return MngSoftware - MngSoftware
   * @throws Exception - an exception
   */
  public final synchronized MngSoftware lazyGetMngSoftware(
    ) throws Exception {
    if (this.mngSoftware == null) {
      this.mngSoftware = new MngSoftware();
      this.mngSoftware.setLogger(lazyGetLogger());
      lazyGetLogger().info(AFactoryAppBeans.class,
        "MngSoftware has been created.");
    }
    return this.mngSoftware;
  }

  /**
   * <p>Get UtlProperties in lazy mode.</p>
   * @return UtlProperties - UtlProperties
   * @throws Exception - an exception
   */
  public final synchronized UtlProperties lazyGetUtlProperties(
    ) throws Exception {
    if (this.utlProperties == null) {
      this.utlProperties = new UtlProperties();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "UtlProperties has been created.");
    }
    return this.utlProperties;
  }

  /**
   * <p>Get UtlReflection in lazy mode.</p>
   * @return UtlReflection - UtlReflection
   * @throws Exception - an exception
   */
  public final synchronized UtlReflection lazyGetUtlReflection(
    ) throws Exception {
    if (this.utlReflection == null) {
      this.utlReflection = new UtlReflection();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "UtlReflection has been created.");
    }
    return this.utlReflection;
  }

  /**
   * <p>Get SrvI18n in lazy mode.</p>
   * @return SrvI18n - SrvI18n
   * @throws Exception - an exception
   */
  public final synchronized SrvI18n lazyGetSrvI18n() throws Exception {
    if (this.srvI18n == null) {
      this.srvI18n = new SrvI18n();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "SrvI18n has been created.");
    }
    return this.srvI18n;
  }

  /**
   * <p>Get SrvPage in lazy mode.</p>
   * @return SrvPage - SrvPage
   * @throws Exception - an exception
   */
  public final synchronized SrvPage lazyGetSrvPage() throws Exception {
    if (this.srvPage == null) {
      this.srvPage = new SrvPage();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "SrvPage has been created.");
    }
    return this.srvPage;
  }

  /**
   * <p>Get MngSettings in lazy mode.</p>
   * @return MngSettings - MngSettings
   * @throws Exception - an exception
   */
  public final synchronized MngSettings
    lazyGetMngUvdSettings() throws Exception {
    if (this.mngUvdSettings == null) {
      this.mngUvdSettings = new MngSettings();
      this.mngUvdSettings.setLogger(lazyGetLogger());
      this.mngUvdSettings.loadConfiguration(this.uvdSettingsDir,
        this.uvdSettingsBaseFile);
      lazyGetLogger().info(AFactoryAppBeans.class,
        "mngUvdSettings has been created.");
    }
    return this.mngUvdSettings;
  }

  /**
   * <p>Get SrvWebMvc in lazy mode.</p>
   * @return SrvWebMvc - SrvWebMvc
   * @throws Exception - an exception
   */
  public final synchronized SrvWebMvc<RS> lazyGetSrvWebMvc() throws Exception {
    if (this.srvWebMvc == null) {
      this.srvWebMvc = new SrvWebMvc<RS>();
      this.srvWebMvc.setLogger(lazyGetLogger());
      this.srvWebMvc.setSrvOrm(lazyGetSrvOrm());
      this.srvWebMvc.setUtlReflection(lazyGetUtlReflection());
      lazyGetLogger().info(AFactoryAppBeans.class,
        "ISrvWebMvc has been created.");
    }
    return this.srvWebMvc;
  }

  /**
   * <p>Get SrvWebEntity in lazy mode.</p>
   * @return SrvWebEntity - SrvWebEntity
   * @throws Exception - an exception
   */
  public final synchronized SrvWebEntity lazyGetSrvWebEntity()
    throws Exception {
    if (this.srvWebEntity == null) {
      this.srvWebEntity = new SrvWebEntity();
      this.srvWebEntity.setSrvWebMvc(lazyGetSrvWebMvc());
      this.srvWebEntity.setSrvOrm(lazyGetSrvOrm());
      this.srvWebEntity.setSrvDatabase(lazyGetSrvDatabase());
      this.srvWebEntity.setMngUvdSettings(lazyGetMngUvdSettings());
      this.srvWebEntity.setSrvPage(lazyGetSrvPage());
      this.srvWebEntity.setEntitiesMap(this.entitiesMap);
      this.srvWebEntity.setSrvI18n(lazyGetSrvI18n());
      this.srvWebEntity.setUtlJsp(lazyGetUtlJsp());
      this.srvWebEntity.setUtlReflection(lazyGetUtlReflection());
      this.srvWebEntity.setLogger(lazyGetLogger());
      this.srvWebEntity.setFactoryEntityServices(this);
      lazyGetLogger().info(AFactoryAppBeans.class,
        "ISrvWebEntity has been created.");
    }
    return this.srvWebEntity;
  }

  /**
   * <p>Get SrvEntity in lazy mode.</p>
   * @param pSrvName - service name
   * @return SrvWebEntity - SrvWebEntity
   * @throws Exception - an exception
   */
  protected final synchronized Object lazyGetSrvEntity(
    final String pSrvName) throws Exception {
    Object srvEntity = beansMap.get(pSrvName);
    if (srvEntity == null) {
      Class<?> entityClass = this.entitiesMap
        .get(pSrvName.replaceFirst("srv", ""));
      if (entityClass != null) {
        srvEntity = new SrvEntitySimple(entityClass, lazyGetSrvOrm());
        beansMap.put(pSrvName, srvEntity);
        lazyGetLogger().info(AFactoryAppBeans.class,
          pSrvName + " has been created.");
        //owned must be settled
        Map<String, String> clSettings = lazyGetMngUvdSettings()
          .getClassesSettings().get(entityClass.getCanonicalName());
        if (clSettings != null) { //ORM has persitable
          // entities that never shown
          String ownedLists = clSettings.get("ownedLists");
          if (ownedLists != null) {
            LinkedHashSet<String> ownedListsSet = lazyGetUtlProperties()
              .evalPropsStringsSet(ownedLists);
            for (String className : ownedListsSet) {
              Class<?> classOwned = Class.forName(className);
              @SuppressWarnings("unchecked")
              Object srvEntityOwned = new SrvEntityOwnedSimple(classOwned,
                entityClass, lazyGetSrvOrm());
              this.beansMap.put("srv" + classOwned.getSimpleName(),
                srvEntityOwned);
              lazyGetLogger().info(AFactoryAppBeans.class,
                "srv" + classOwned.getSimpleName() + " has been created.");
            }
          }
        }
      }
    }
    return srvEntity;
  }

  //Simple setters to replace services during runtime:
  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final synchronized void setUtlReflection(
    final UtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }

  /**
   * <p>Setter for utlProperties.</p>
   * @param pUtlProperties reference
   **/
  public final synchronized void setUtlProperties(
    final UtlProperties pUtlProperties) {
    this.utlProperties = pUtlProperties;
  }

  /**
   * <p>Setter for utlJsp.</p>
   * @param pUtlJsp reference
   **/
  public final synchronized void setUtlJsp(final UtlJsp pUtlJsp) {
    this.utlJsp = pUtlJsp;
  }

  /**
   * <p>Setter for srvI18n.</p>
   * @param pSrvI18n reference
   **/
  public final synchronized void setSrvI18n(final SrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngUvdSettings reference
   **/
  public final synchronized void setMngUvdSettings(
    final MngSettings pMngUvdSettings) {
    this.mngUvdSettings = pMngUvdSettings;
  }

  /**
   * <p>Setter for srvWebEntity.</p>
   * @param pSrvWebEntity reference
   **/
  public final synchronized void setSrvWebEntity(
    final SrvWebEntity pSrvWebEntity) {
    this.srvWebEntity = pSrvWebEntity;
  }

  /**
   * <p>Setter for srvPage.</p>
   * @param pSrvPage reference
   **/
  public final synchronized void setSrvPage(final SrvPage pSrvPage) {
    this.srvPage = pSrvPage;
  }

  /**
   * <p>Setter for mngSoftware.</p>
   * @param pMngSoftware reference
   **/
  public final synchronized void setMngSoftware(
    final MngSoftware pMngSoftware) {
    this.mngSoftware = pMngSoftware;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final synchronized void setSrvOrm(final ASrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Setter for srvWebMvc.</p>
   * @param pSrvWebMvc reference
   **/
  public final synchronized void setSrvWebMvc(final SrvWebMvc<RS> pSrvWebMvc) {
    this.srvWebMvc = pSrvWebMvc;
  }

  /**
   * <p>Setter for hlpInsertUpdate.</p>
   * @param pHlpInsertUpdate reference
   **/
  public final synchronized void setHlpInsertUpdate(
    final HlpInsertUpdate pHlpInsertUpdate) {
    this.hlpInsertUpdate = pHlpInsertUpdate;
  }

  /**
   * <p>Getter for entitiesMap.</p>
   * @return Map<String, Class<?>>
   **/
  public final synchronized Map<String, Class<?>> getEntitiesMap() {
    return this.entitiesMap;
  }

  /**
   * <p>Getter for beansMap.</p>
   * @return Map<String, Object>
   **/
  public final synchronized Map<String, Object> getBeansMap() {
    return this.beansMap;
  }

  /**
   * <p>Getter for factoryOverBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final synchronized IFactoryAppBeans getFactoryOverBeans() {
    return this.factoryOverBeans;
  }

  /**
   * <p>Setter for factoryOverBeans.</p>
   * @param pFactoryOverBeans reference
   **/
  public final synchronized void setFactoryOverBeans(
    final IFactoryAppBeans pFactoryOverBeans) {
    this.factoryOverBeans = pFactoryOverBeans;
  }

  /**
   * <p>Setter for srvGetDbCopyXml.</p>
   * @param pClearDbThenGetAnotherCopyXmlHttp reference
   **/
  public final synchronized void setClearDbThenGetAnotherCopyXmlHttp(
    final ClearDbThenGetAnotherCopyXmlHttp<RS>
      pClearDbThenGetAnotherCopyXmlHttp) {
    this.srvGetDbCopyXml = pClearDbThenGetAnotherCopyXmlHttp;
  }

  /**
   * <p>Setter for databaseWriter.</p>
   * @param pDatabaseWriter reference
   **/
  public final synchronized void setDatabaseWriter(
    final DatabaseWriterXml<RS> pDatabaseWriter) {
    this.databaseWriter = pDatabaseWriter;
  }

  /**
   * <p>Setter for mngSettingsGetDbCopy.</p>
   * @param pMngSettingsGetDbCopy reference
   **/
  public final synchronized void setMngSettingsGetDbCopy(
    final MngSettings pMngSettingsGetDbCopy) {
    this.mngSettingsGetDbCopy = pMngSettingsGetDbCopy;
  }

  /**
   * <p>Setter for utilXml.</p>
   * @param pUtilXml reference
   **/
  public final synchronized void setUtilXml(
    final UtilXml pUtilXml) {
    this.utilXml = pUtilXml;
  }
  //Simple getters and setters:
  /**
   * <p>Getter for minutesOfIdle.</p>
   * @return int
   **/
  public final synchronized int getMinutesOfIdle() {
    return this.minutesOfIdle;
  }

  /**
   * <p>Setter for minutesOfIdle.</p>
   * @param pMinutesOfIdle reference
   **/
  public final synchronized void setMinutesOfIdle(final int pMinutesOfIdle) {
    this.minutesOfIdle = pMinutesOfIdle;
  }

  /**
   * <p>Getter for lastRequestTime.</p>
   * @return long
   **/
  public final synchronized long getLastRequestTime() {
    return this.lastRequestTime;
  }

  /**
   * <p>Setter for lastRequestTime.</p>
   * @param pLastRequestTime reference
   **/
  public final synchronized void setLastRequestTime(
    final long pLastRequestTime) {
    this.lastRequestTime = pLastRequestTime;
  }

  /**
   * <p>Geter for ormSettingsDir.</p>
   * @return String
   **/
  public final String getOrmSettingsDir() {
    return this.ormSettingsDir;
  }

  /**
   * <p>Setter for ormSettingsDir.</p>
   * @param pOrmSettingsDir reference/value
   **/
  public final void setOrmSettingsDir(final String pOrmSettingsDir) {
    this.ormSettingsDir = pOrmSettingsDir;
  }

  /**
   * <p>Geter for ormSettingsBaseFile.</p>
   * @return String
   **/
  public final String getOrmSettingsBaseFile() {
    return this.ormSettingsBaseFile;
  }

  /**
   * <p>Setter for ormSettingsBaseFile.</p>
   * @param pOrmSettingsBaseFile reference/value
   **/
  public final void setOrmSettingsBaseFile(final String pOrmSettingsBaseFile) {
    this.ormSettingsBaseFile = pOrmSettingsBaseFile;
  }

  /**
   * <p>Geter for uvdSettingsDir.</p>
   * @return String
   **/
  public final String getUvdSettingsDir() {
    return this.uvdSettingsDir;
  }

  /**
   * <p>Setter for uvdSettingsDir.</p>
   * @param pUvdSettingsDir reference
   **/
  public final void setUvdSettingsDir(final String pUvdSettingsDir) {
    this.uvdSettingsDir = pUvdSettingsDir;
  }

  /**
   * <p>Geter for uvdSettingsBaseFile.</p>
   * @return String
   **/
  public final String getUvdSettingsBaseFile() {
    return this.uvdSettingsBaseFile;
  }

  /**
   * <p>Setter for uvdSettingsBaseFile.</p>
   * @param pUvdSettingsBaseFile reference
   **/
  public final void setUvdSettingsBaseFile(final String pUvdSettingsBaseFile) {
    this.uvdSettingsBaseFile = pUvdSettingsBaseFile;
  }

  /**
   * <p>Set is show debug messages.</p>
   * @param pIsShowDebugMessage is show debug messages?
   **/
  public final synchronized void setIsShowDebugMessages(
    final  boolean pIsShowDebugMessage) {
    this.isShowDebugMessage = pIsShowDebugMessage;
  }

  /**
   * <p>Get is show debug messages.</p>
   * @return is show debug messages?
   **/
  public final synchronized boolean getIsShowDebugMessages() {
    return this.isShowDebugMessage;
  }

  /**
   * <p>Getter for databaseName.</p>
   * @return String
   **/
  public final String getDatabaseName() {
    return this.databaseName;
  }

  /**
   * <p>Setter for databaseName.</p>
   * @param pDatabaseName reference
   **/
  public final void setDatabaseName(final String pDatabaseName) {
    this.databaseName = pDatabaseName;
  }

  /**
   * <p>Getter for databaseUser.</p>
   * @return String
   **/
  public final String getDatabaseUser() {
    return this.databaseUser;
  }

  /**
   * <p>Setter for databaseUser.</p>
   * @param pDatabaseUser reference
   **/
  public final void setDatabaseUser(final String pDatabaseUser) {
    this.databaseUser = pDatabaseUser;
  }

  /**
   * <p>Getter for databasePassword.</p>
   * @return String
   **/
  public final String getDatabasePassword() {
    return this.databasePassword;
  }

  /**
   * <p>Setter for databasePassword.</p>
   * @param pDatabasePassword reference
   **/
  public final void setDatabasePassword(final String pDatabasePassword) {
    this.databasePassword = pDatabasePassword;
  }
}
