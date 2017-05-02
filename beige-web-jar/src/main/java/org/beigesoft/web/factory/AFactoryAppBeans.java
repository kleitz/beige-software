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
import java.util.Hashtable;
import java.lang.reflect.Field;
import java.io.File;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.delegate.IDelegator;
import org.beigesoft.converter.CnvTfsObject;
import org.beigesoft.converter.CnvTfsEnum;
import org.beigesoft.converter.CnvTfsHasId;
import org.beigesoft.factory.FctConvertersToFromString;
import org.beigesoft.factory.FctFillersObjectFields;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.handler.HandlerAbout;
import org.beigesoft.holder.HolderRapiSetters;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.service.SrvI18n;
import org.beigesoft.service.SrvPage;
import org.beigesoft.service.SrvDate;
import org.beigesoft.service.UtilXml;
import org.beigesoft.service.MailSenderStd;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.log.ILogger;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.settings.holder.HldFieldsSettings;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.holder.HldCnvFromRsNames;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBnCnvBnFromRs;
import org.beigesoft.orm.service.FillEntityFromReq;
import org.beigesoft.orm.service.FillerEntitiesFromRs;
import org.beigesoft.orm.service.ASrvOrm;
import org.beigesoft.orm.service.SrvEntitiesPage;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.replicator.service.SrvClearDatabase;
import org.beigesoft.replicator.service.ReplicatorXmlHttp;
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
import org.beigesoft.web.service.MngSoftware;
import org.beigesoft.web.service.UtlJsp;

/**
 * <p>Application beans factory for any RDBMS.
 * This is simple, pure OOP, cheap but powerful alternative to CDI.
 * This factory designed for high load job. All initialized beans from this
 * factory and sub-factories are in the beansMap. Client must invoke
 * only lazyGet([beanName]) method. Factory is locked only during requested
 * bean initialization (when it not exist in the beansMap).
 * All others lazyGet[beanName]() methods from this factory and sub-factories
 * are invoked internally by these factories only during bean initialization.
 * So there is no way to make inconsistent locking by concurrent clients.
 * To hot-change configuration e.g. load fixed class and put it into factory,
 * set isReconfiguring to true, clear beans, load class and instantiate
 * new bean by using inner lazeGet[depended bean name], then unlock factory.
 * </p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public abstract class AFactoryAppBeans<RS> implements IFactoryAppBeans {

  /**
   * <p>Upload directory relative to WEB-APP path
   * without start and end separator, e.g. "static/uploads".</p>
   **/
  private String uploadDirectory = "static" + File.separator + "uploads";

  /**
   * <p>Full WEB-APP path without end separator,
   * revealed from servlet context and used for upload files.</p>
   **/
  private String webAppPath;

    /**
   * <p>Is show debug messages.</p>
   **/
  private boolean isShowDebugMessages = true;

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
   * <p>Beans map "beans name"-"beans",
   * e.g. "ISrvOrm"-"[srvOrmSqlite]".
   * All beans (include made by sub-factories) are here.</p>
   **/
  private final Map<String, Object> beansMap = new Hashtable<String, Object>();

  /**
   * <p>Factory Over Beans.</p>
   **/
  private IFactoryAppBeans factoryOverBeans;

  /**
   * <p>Factory of entity request handler with business
   * logic dependent processors.</p>
   **/
  private IFactoryBldServices<RS> factoryBldServices;

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
   * <p>ID for New Database.</p>
   **/
  private int newDatabaseId = 1;

  /**
   * <p>Flag for custom locking during reconfiguring
   * (i.e. manually setting new fixed bean).</p>
   **/
  private boolean isReconfiguring = false;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final Object lazyGet(
    final String pBeanName) throws Exception {
    if (this.isReconfiguring) {
      throw new ExceptionWithCode(ExceptionWithCode.SYSTEM_RECONFIGURING,
        "system_reconfiguring");
    }
    setLastRequestTime(new Date().getTime());
    Object bean = this.beansMap.get(pBeanName);
    // see in beige-common:
    // org.beigesoft.test.DoubleCkeckLockingWrApTest
    if (bean == null) {
      // locking:
      synchronized (this) {
        // make sure again whether it's null after locking:
        bean = this.beansMap.get(pBeanName);
        if (bean == null) {
          if (getHandlerEntityRequestName().equals(pBeanName)) {
            bean = this.factoryBldServices.lazyGetHandlerEntityRequest();
          } else if (getHandlerAboutName().equals(pBeanName)) {
            bean = lazyGetHandlerAbout();
          } else if (getSrvOrmName().equals(pBeanName)) {
            bean = lazyGetSrvOrm();
          } else if (getImportFullDatabaseCopyName().equals(pBeanName)) {
            bean = lazyGetImportFullDatabaseCopy();
          } else if (getDatabaseWriterName().equals(pBeanName)) {
            bean = lazyGetDbWriterFullCopy();
          } else if (getMailSenderName().equals(pBeanName)) {
            bean = lazyGetMailSender();
          } else if (getUtilXmlName().equals(pBeanName)) {
            bean = lazyGetUtilXml();
          } else if (getLoggerName().equals(pBeanName)) {
            bean = lazyGetLogger();
          } else if (getMngSoftwareName().equals(pBeanName)) {
            bean = lazyGetMngSoftware();
          } else if (getSrvI18nName().equals(pBeanName)) {
            bean = lazyGetSrvI18n();
          } else if (getUtlJspName().equals(pBeanName)) {
            bean = lazyGetUtlJsp();
          } else if (getSrvDatabaseName().equals(pBeanName)) {
            bean = lazyGetSrvDatabase();
          } else if (getSrvEntitiesPageName().equals(pBeanName)) {
            bean = lazyGetSrvEntitiesPage();
          } else if (getSrvDateName().equals(pBeanName)) {
            bean = lazyGetSrvDate();
          } else if (getSrvPageName().equals(pBeanName)) {
            bean = lazyGetSrvPage();
          } else if (getFillEntityFromReqName().equals(pBeanName)) {
            bean = lazyGetFillEntityFromReq();
          } else if (getMngUvdSettingsName().equals(pBeanName)) {
            bean = lazyGetMngUvdSettings();
          } else if (getUtlPropertiesName().equals(pBeanName)) {
            bean = lazyGetUtlProperties();
          } else if (getUtlReflectionName().equals(pBeanName)) {
            bean = lazyGetUtlReflection();
          } else if (getMngSettingsGetDbCopyName().equals(pBeanName)) {
            bean = lazyGetMngSettingsGetDbCopy();
          } else if (getHldFieldsCnvTfsNamesName().equals(pBeanName)) {
            bean = lazyGetHldFieldsCnvTfsNames();
          } else if (getFctConvertersToFromStringName().equals(pBeanName)) {
            bean = lazyGetFctConvertersToFromString();
          } else if (getFctFillersObjectFieldsName().equals(pBeanName)) {
            bean = lazyGetFctFillersObjectFields();
          } else if (getHolderRapiSettersName().equals(pBeanName)) {
            bean = lazyGetHolderRapiSetters();
          } else if (getHolderRapiGettersName().equals(pBeanName)) {
            bean = lazyGetHolderRapiGetters();
          } else if (getHolderRapiFieldsName().equals(pBeanName)) {
            bean = lazyGetHolderRapiFields();
          } else if (getHlpInsertUpdateName().equals(pBeanName)) {
            bean = lazyGetHlpInsertUpdate();
          } else if (getFillerEntitiesFromRsName().equals(pBeanName)) {
            bean = lazyGetFillerEntitiesFromRs();
          } else if (getFctBnCnvBnFromRsName().equals(pBeanName)) {
            bean = lazyGetFctBnCnvBnFromRs();
          } else if (getFctBnCnvIbnToColumnValuesName().equals(pBeanName)) {
            bean = lazyGetFctBnCnvIbnToColumnValues();
          } else if (getSrvSqlEscapeName().equals(pBeanName)) {
            bean = lazyGetSrvSqlEscape();
          } else if (getHldCnvToColumnsValuesNamesName().equals(pBeanName)) {
            bean = lazyGetHldCnvToColumnsValuesNames();
          } else if (getPrepareDbAfterFullImportName().equals(pBeanName)) {
            bean = lazyGetPrepareDbAfterFullImport();
          } else if (getHldCnvFromRsNamesName().equals(pBeanName)) {
            bean = lazyGetHldCnvFromRsNames();
          } else if (getFctBcFctSimpleEntitiesName().equals(pBeanName)) {
            bean = this.factoryBldServices.lazyGetFctBcFctSimpleEntities();
          } else if (getFctBcCnvEntityToColumnsValuesName().equals(pBeanName)) {
            bean = lazyGetFctBcCnvEntityToColumnsValues();
          } else {
            bean = lazyGetOtherBean(pBeanName);
            if (bean == null && this.factoryOverBeans != null) {
                bean = this.factoryOverBeans.lazyGet(pBeanName);
            }
          }
        }
      }
    }
    if (bean == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no bean with name " + pBeanName);
    }
    return bean;
  }

  /**
   * <p>Release beans (memory). This is "memory friendly" factory.
   * All beans (include from sub-factories) are here.</p>
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void releaseBeans() throws Exception {
    getEntitiesMap().clear();
    getBeansMap().clear();
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
   * <p>Is need to SQL escape (character ').</p>
   * @return for Android false, JDBC - true.
   */
  public abstract boolean getIsNeedsToSqlEscape();

  /**
   * <p>Getter of Logger name.</p>
   * @return service name
   **/
  public final String getLoggerName() {
    return "ILogger";
  }

  /**
   * <p>Get Service that prepare Database after full import
   * in lazy mode.</p>
   * @return IDelegator - preparator Database after full import.
   * @throws Exception - an exception
   */
  public abstract IDelegator
    lazyGetPrepareDbAfterFullImport() throws Exception;

  /**
   * <p>Getter of Prepare DB After Full Import service name.</p>
   * @return service name
   **/
  public final String getPrepareDbAfterFullImportName() {
    return "prepareDbAfterFullImport";
  }

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return ISrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  public abstract ISrvDatabase<RS>
    lazyGetSrvDatabase() throws Exception;

  /**
   * <p>Getter of Database service name.</p>
   * @return service name
   **/
  public final String getSrvDatabaseName() {
    return "ISrvDatabase";
  }

  //Work:
  /**
   * <p>Handler of event on database changed,
   * right now SQlite DB is able to be changed.</p>
   * @throws Exception - an exception
   */
  public final synchronized void handleDatabaseChanged() throws Exception {
    releaseBeans();
  }

  /**
   * <p>Get SrvOrm in lazy mode.</p>
   * @return SrvOrm - SrvOrm
   * @throws Exception - an exception
   */
  public final ASrvOrm<RS>
    lazyGetSrvOrm() throws Exception {
    String beanName = getSrvOrmName();
    @SuppressWarnings("unchecked")
    ASrvOrm<RS> srvOrm = (ASrvOrm<RS>) this.beansMap.get(beanName);
    if (srvOrm == null) {
      srvOrm = instantiateSrvOrm();
      srvOrm.setNewDatabaseId(this.newDatabaseId);
      srvOrm.setSrvDatabase(lazyGetSrvDatabase());
      srvOrm.setHlpInsertUpdate(lazyGetHlpInsertUpdate());
      srvOrm.setLogger(lazyGetLogger());
      srvOrm.setUtlReflection(lazyGetUtlReflection());
      srvOrm.setFctFillersObjectFields(lazyGetFctFillersObjectFields());
      srvOrm.setEntitiesFactoriesFatory(this.factoryBldServices
        .lazyGetFctBcFctSimpleEntities());
      FctBcCnvEntityToColumnsValues fctBcCnvEntityToColumnsValues =
        lazyGetFctBcCnvEntityToColumnsValues();
      srvOrm.setFactoryCnvEntityToColumnsValues(
        fctBcCnvEntityToColumnsValues);
      FillerEntitiesFromRs<RS> fillerEntitiesFromRs =
        lazyGetFillerEntitiesFromRs();
      srvOrm.setFillerEntitiesFromRs(fillerEntitiesFromRs);
      MngSettings mngOrmSettings = new MngSettings();
      mngOrmSettings.setLogger(lazyGetLogger());
      srvOrm.setMngSettings(mngOrmSettings);
      srvOrm.loadConfiguration(this.ormSettingsDir,
        this.ormSettingsBaseFile);
      fctBcCnvEntityToColumnsValues
        .setTablesMap(srvOrm.getTablesMap());
      lazyGetFctBnCnvBnFromRs().setTablesMap(srvOrm.getTablesMap());
      lazyGetFctBnCnvIbnToColumnValues().setTablesMap(srvOrm.getTablesMap());
      fillerEntitiesFromRs.setTablesMap(srvOrm.getTablesMap());
      Map<String, Object> addParam = new Hashtable<String, Object>();
      srvOrm.initializeDatabase(addParam);
      this.entitiesMap = new Hashtable<String, Class<?>>();
      for (Class<?> clazz : srvOrm.getMngSettings()
        .getClasses()) {
        this.entitiesMap.put(clazz.getSimpleName(), clazz);
      }
      this.beansMap.put(beanName, srvOrm);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvOrm;
  }

  /**
   * <p>Getter of ORM service name.</p>
   * @return service name
   **/
  public final String getSrvOrmName() {
    return "ISrvOrm";
  }

  /**
   * <p>Get ReplicatorXmlHttp full import in lazy mode.</p>
   * @return ReplicatorXmlHttp - ReplicatorXmlHttp
   * @throws Exception - an exception
   */
  public final ReplicatorXmlHttp<RS>
    lazyGetImportFullDatabaseCopy() throws Exception {
    String beanName = getImportFullDatabaseCopyName();
    @SuppressWarnings("unchecked")
    ReplicatorXmlHttp<RS> srvGetDbCopyXml =
      (ReplicatorXmlHttp<RS>) this.beansMap.get(beanName);
    if (srvGetDbCopyXml == null) {
      srvGetDbCopyXml = new ReplicatorXmlHttp<RS>();
      SrvEntityReaderXml srvEntityReaderXml = new SrvEntityReaderXml();
      srvEntityReaderXml.setUtilXml(lazyGetUtilXml());
      SrvEntityFieldFillerStd srvEntityFieldFillerStd =
        new SrvEntityFieldFillerStd();
      srvEntityFieldFillerStd.setUtilXml(lazyGetUtilXml());
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
        new Hashtable<String, ISrvEntityFieldFiller>();
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
      SrvClearDatabase<RS> srvClearDatabase = new SrvClearDatabase<RS>();
      srvClearDatabase.setMngSettings(lazyGetMngSettingsGetDbCopy());
      srvClearDatabase.setLogger(lazyGetLogger());
      srvClearDatabase.setSrvDatabase(lazyGetSrvDatabase());
      srvGetDbCopyXml.setDatabasePrepearerBefore(srvClearDatabase);
      srvGetDbCopyXml
        .setDatabasePrepearerAfter(lazyGetPrepareDbAfterFullImport());
      srvGetDbCopyXml.setUtilXml(lazyGetUtilXml());
      srvGetDbCopyXml.setSrvEntityReaderXml(srvEntityReaderXml);
      srvGetDbCopyXml.setMngSettings(lazyGetMngSettingsGetDbCopy());
      srvGetDbCopyXml.setSrvDatabase(lazyGetSrvDatabase());
      srvGetDbCopyXml.setDatabaseReader(databaseReaderIdenticalXml);
      srvGetDbCopyXml.setLogger(lazyGetLogger());
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
      this.beansMap.put(beanName, srvGetDbCopyXml);
    }
    return srvGetDbCopyXml;
  }

  /**
   * <p>Getter of Import Full Database Copy service name.</p>
   * @return service name
   **/
  public final String getImportFullDatabaseCopyName() {
    return "importFullDatabaseCopy";
  }

  /**
   * <p>Get DatabaseWriterXml for full DB copy in lazy mode.</p>
   * @return DatabaseWriterXml - DatabaseWriterXml
   * @throws Exception - an exception
   */
  public final DatabaseWriterXml<RS>
    lazyGetDbWriterFullCopy() throws Exception {
    String beanName = getDatabaseWriterName();
    @SuppressWarnings("unchecked")
    DatabaseWriterXml<RS> dbWriterXmlFullImport =
      (DatabaseWriterXml<RS>) this.beansMap.get(beanName);
    if (dbWriterXmlFullImport == null) {
      dbWriterXmlFullImport = new DatabaseWriterXml<RS>();
      SrvEntityWriterXml srvEntityWriterXml = new SrvEntityWriterXml();
      SrvFieldWriterXmlStd srvFieldWriterXmlStd = new SrvFieldWriterXmlStd();
      srvFieldWriterXmlStd.setUtilXml(lazyGetUtilXml());
      SrvFieldHasIdWriterXml srvFieldHasIdWriterXml =
        new SrvFieldHasIdWriterXml();
      srvFieldHasIdWriterXml.setUtilXml(lazyGetUtilXml());
      srvEntityWriterXml.setUtlReflection(lazyGetUtlReflection());
      Map<String, ISrvFieldWriter> fieldsWritersMap =
        new Hashtable<String, ISrvFieldWriter>();
      fieldsWritersMap.put("SrvFieldWriterXmlStd", srvFieldWriterXmlStd);
      fieldsWritersMap.put("SrvFieldHasIdWriterXml", srvFieldHasIdWriterXml);
      srvEntityWriterXml.setFieldsWritersMap(fieldsWritersMap);
      srvEntityWriterXml.setMngSettings(lazyGetMngSettingsGetDbCopy());
      dbWriterXmlFullImport.setLogger(lazyGetLogger());
      dbWriterXmlFullImport.setSrvEntityWriter(srvEntityWriterXml);
      dbWriterXmlFullImport.setSrvDatabase(lazyGetSrvDatabase());
      dbWriterXmlFullImport.setSrvOrm(lazyGetSrvOrm());
      this.beansMap.put(beanName, dbWriterXmlFullImport);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return dbWriterXmlFullImport;
  }

  /**
   * <p>Getter of Database Writer service name.</p>
   * @return service name
   **/
  public final String getDatabaseWriterName() {
    return "IDatabaseWriter";
  }

  /**
   * <p>Get MngSettings for replicator in lazy mode.</p>
   * @return MngSettings - MngSettings replicator
   * @throws Exception - an exception
   */
  public final MngSettings
    lazyGetMngSettingsGetDbCopy() throws Exception {
    String beanName = getMngSettingsGetDbCopyName();
    MngSettings mngSettingsGetDbCopy =
      (MngSettings) this.beansMap.get(beanName);
    if (mngSettingsGetDbCopy == null) {
      mngSettingsGetDbCopy = new MngSettings();
      mngSettingsGetDbCopy.setLogger(lazyGetLogger());
      mngSettingsGetDbCopy
        .loadConfiguration("beige-get-db-copy", "base.xml");
      this.beansMap.put(beanName, mngSettingsGetDbCopy);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return mngSettingsGetDbCopy;
  }

  /**
   * <p>Getter of Manager Settings for Get DB Copy name.</p>
   * @return service name
   **/
  public final String getMngSettingsGetDbCopyName() {
    return "mngSettingsGetDbCopy";
  }

  /**
   * <p>Getter of About service name.</p>
   * @return service name
   **/
  public final String getHandlerEntityRequestName() {
    return "handlerEntityRequest";
  }

  /**
   * <p>Get HandlerAbout in lazy mode.</p>
   * @return HandlerAbout - HandlerAbout
   * @throws Exception - an exception
   */
  public final HandlerAbout<RS> lazyGetHandlerAbout() throws Exception {
    String beanName = getHandlerAboutName();
    @SuppressWarnings("unchecked")
    HandlerAbout<RS> srvAbout =
      (HandlerAbout<RS>) this.beansMap.get(beanName);
    if (srvAbout == null) {
      srvAbout = new HandlerAbout<RS>();
      srvAbout.setSrvOrm(lazyGetSrvOrm());
      srvAbout.setSrvDatabase(lazyGetSrvDatabase());
      this.beansMap.put(beanName, srvAbout);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvAbout;
  }

  /**
   * <p>Getter of About service name.</p>
   * @return service name
   **/
  public final String getHandlerAboutName() {
    return "hndAbout";
  }

  /**
   * <p>Get MailSenderStd in lazy mode.</p>
   * @return MailSenderStd - MailSenderStd
   * @throws Exception - an exception
   */
  public final MailSenderStd lazyGetMailSender() throws Exception {
    String beanName = getMailSenderName();
    MailSenderStd mailSender = (MailSenderStd) this.beansMap.get(beanName);
    if (mailSender == null) {
      mailSender = new MailSenderStd(lazyGetLogger());
      this.beansMap.put(beanName, mailSender);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return mailSender;
  }

  /**
   * <p>Getter of Mail Sender service name.</p>
   * @return service name
   **/
  public final String getMailSenderName() {
    return "IMailSender";
  }

  /**
   * <p>Get SrvSqlEscape in lazy mode.</p>
   * @return SrvSqlEscape - SrvSqlEscape
   * @throws Exception - an exception
   */
  public final SrvSqlEscape lazyGetSrvSqlEscape() throws Exception {
    String beanName = getSrvSqlEscapeName();
    SrvSqlEscape srvSqlEscape = (SrvSqlEscape) this.beansMap.get(beanName);
    if (srvSqlEscape == null) {
      srvSqlEscape = new SrvSqlEscape();
      this.beansMap.put(beanName, srvSqlEscape);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvSqlEscape;
  }

  /**
   * <p>Getter of Util Xml service name.</p>
   * @return service name
   **/
  public final String getSrvSqlEscapeName() {
    return "ISrvSqlEscape";
  }

  /**
   * <p>Get UtilXml in lazy mode.</p>
   * @return UtilXml - UtilXml
   * @throws Exception - an exception
   */
  public final UtilXml lazyGetUtilXml() throws Exception {
    String beanName = getUtilXmlName();
    UtilXml utilXml = (UtilXml) this.beansMap.get(beanName);
    if (utilXml == null) {
      utilXml = new UtilXml();
      this.beansMap.put(beanName, utilXml);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return utilXml;
  }

  /**
   * <p>Getter of Util Xml service name.</p>
   * @return service name
   **/
  public final String getUtilXmlName() {
    return "IUtilXml";
  }

  /**
   * <p>Get HlpInsertUpdate in lazy mode.</p>
   * @return HlpInsertUpdate - HlpInsertUpdate
   * @throws Exception - an exception
   */
  public final HlpInsertUpdate
    lazyGetHlpInsertUpdate() throws Exception {
    String beanName = getHlpInsertUpdateName();
    HlpInsertUpdate hlpInsertUpdate =
      (HlpInsertUpdate) this.beansMap.get(beanName);
    if (hlpInsertUpdate == null) {
      hlpInsertUpdate = new HlpInsertUpdate();
      this.beansMap.put(beanName, hlpInsertUpdate);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return hlpInsertUpdate;
  }

  /**
   * <p>Getter of Helper Insert/Update name.</p>
   * @return service name
   **/
  public final String getHlpInsertUpdateName() {
    return "HlpInsertUpdate";
  }

  /**
   * <p>Get UtlJsp in lazy mode.</p>
   * @return UtlJsp - UtlJsp
   * @throws Exception - an exception
   */
  public final UtlJsp lazyGetUtlJsp() throws Exception {
    String beanName = getUtlJspName();
    UtlJsp utlJsp = (UtlJsp) this.beansMap.get(beanName);
    if (utlJsp == null) {
      utlJsp = new UtlJsp();
      this.beansMap.put(beanName, utlJsp);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return utlJsp;
  }

  /**
   * <p>Getter of UtlJsp service name.</p>
   * @return service name
   **/
  public final String getUtlJspName() {
    return "UtlJsp";
  }

  /**
   * <p>Get MngSoftware in lazy mode.</p>
   * @return MngSoftware - MngSoftware
   * @throws Exception - an exception
   */
  public final MngSoftware lazyGetMngSoftware() throws Exception {
    String beanName = getMngSoftwareName();
    MngSoftware mngSoftware = (MngSoftware) this.beansMap.get(beanName);
    if (mngSoftware == null) {
      mngSoftware = new MngSoftware();
      mngSoftware.setLogger(lazyGetLogger());
      this.beansMap.put(beanName, mngSoftware);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return mngSoftware;
  }

  /**
   * <p>Getter of  service name.</p>
   * @return service name
   **/
  public final String getMngSoftwareName() {
    return "IMngSoftware";
  }

  /**
   * <p>Get UtlProperties in lazy mode.</p>
   * @return UtlProperties - UtlProperties
   * @throws Exception - an exception
   */
  public final UtlProperties
    lazyGetUtlProperties() throws Exception {
    String beanName = getUtlPropertiesName();
    UtlProperties utlProperties = (UtlProperties) this.beansMap.get(beanName);
    if (utlProperties == null) {
      utlProperties = new UtlProperties();
      this.beansMap.put(beanName, utlProperties);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return utlProperties;
  }

  /**
   * <p>Getter of Utils Properties service name.</p>
   * @return service name
   **/
  public final String getUtlPropertiesName() {
    return "UtlProperties";
  }

  /**
   * <p>Get UtlReflection in lazy mode.</p>
   * @return UtlReflection - UtlReflection
   * @throws Exception - an exception
   */
  public final UtlReflection
    lazyGetUtlReflection() throws Exception {
    String beanName = getUtlReflectionName();
    UtlReflection utlReflection = (UtlReflection) this.beansMap.get(beanName);
    if (utlReflection == null) {
      utlReflection = new UtlReflection();
      this.beansMap.put(beanName, utlReflection);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return utlReflection;
  }

  /**
   * <p>Getter of Utils Reflection name.</p>
   * @return service name
   **/
  public final String getUtlReflectionName() {
    return "IUtlReflection";
  }

  /**
   * <p>Get SrvI18n in lazy mode.</p>
   * @return SrvI18n - SrvI18n
   * @throws Exception - an exception
   */
  public final SrvI18n lazyGetSrvI18n() throws Exception {
    String beanName = getSrvI18nName();
    SrvI18n srvI18n = (SrvI18n) this.beansMap.get(beanName);
    if (srvI18n == null) {
      srvI18n = new SrvI18n();
      this.beansMap.put(beanName, srvI18n);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvI18n;
  }

  /**
   * <p>Getter of I18n service name.</p>
   * @return service name
   **/
  public final String getSrvI18nName() {
    return "ISrvI18n";
  }

  /**
   * <p>Get SrvEntitiesPage in lazy mode.</p>
   * @return SrvEntitiesPage - SrvEntitiesPage
   * @throws Exception - an exception
   */
  public final SrvEntitiesPage<RS> lazyGetSrvEntitiesPage() throws Exception {
    String beanName = getSrvEntitiesPageName();
    @SuppressWarnings("unchecked")
    SrvEntitiesPage<RS> srvEntitiesPage =
      (SrvEntitiesPage<RS>) this.beansMap.get(beanName);
    if (srvEntitiesPage == null) {
      srvEntitiesPage = new SrvEntitiesPage<RS>();
      srvEntitiesPage.setFieldsRapiHolder(lazyGetHolderRapiFields());
      srvEntitiesPage.setSrvI18n(lazyGetSrvI18n());
      srvEntitiesPage.setSrvOrm(lazyGetSrvOrm());
      srvEntitiesPage.setSrvPage(lazyGetSrvPage());
      srvEntitiesPage.setSrvDate(lazyGetSrvDate());
      srvEntitiesPage.setMngUvdSettings(lazyGetMngUvdSettings());
      srvEntitiesPage.setEntitiesMap(getEntitiesMap());
      srvEntitiesPage
        .setConvertersFieldsFatory(lazyGetFctConvertersToFromString());
      srvEntitiesPage
        .setFieldConverterNamesHolder(lazyGetHldFieldsCnvTfsNames());
      this.beansMap.put(beanName, srvEntitiesPage);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvEntitiesPage;
  }

  /**
   * <p>Getter of Date service name.</p>
   * @return service name
   **/
  public final String getSrvEntitiesPageName() {
    return "ISrvEntitiesPage";
  }

  /**
   * <p>Get SrvDate in lazy mode.</p>
   * @return SrvDate - SrvDate
   * @throws Exception - an exception
   */
  public final SrvDate lazyGetSrvDate() throws Exception {
    String beanName = getSrvDateName();
    SrvDate srvDate = (SrvDate) this.beansMap.get(beanName);
    if (srvDate == null) {
      srvDate = new SrvDate();
      this.beansMap.put(beanName, srvDate);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvDate;
  }

  /**
   * <p>Getter of Date service name.</p>
   * @return service name
   **/
  public final String getSrvDateName() {
    return "ISrvDate";
  }

  /**
   * <p>Get SrvPage in lazy mode.</p>
   * @return SrvPage - SrvPage
   * @throws Exception - an exception
   */
  public final SrvPage lazyGetSrvPage() throws Exception {
    String beanName = getSrvPageName();
    SrvPage srvPage = (SrvPage) this.beansMap.get(beanName);
    if (srvPage == null) {
      srvPage = new SrvPage();
      this.beansMap.put(beanName, srvPage);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return srvPage;
  }

  /**
   * <p>Getter of Page service name.</p>
   * @return service name
   **/
  public final String getSrvPageName() {
    return "ISrvPage";
  }

  /**
   * <p>Get MngSettings in lazy mode.</p>
   * @return MngSettings - MngSettings
   * @throws Exception - an exception
   */
  public final MngSettings
    lazyGetMngUvdSettings() throws Exception {
    String beanName = getMngUvdSettingsName();
    MngSettings mngUvdSettings = (MngSettings) this.beansMap.get(beanName);
    if (mngUvdSettings == null) {
      mngUvdSettings = new MngSettings();
      mngUvdSettings.setLogger(lazyGetLogger());
      mngUvdSettings.loadConfiguration(this.uvdSettingsDir,
        this.uvdSettingsBaseFile);
      HolderRapiFields holderRapiFields = lazyGetHolderRapiFields();
      // prepare for setting fieldFromToStringConverter
      // according Specification Beige-WEB interface version #2:
      for (Class<?> clazz : mngUvdSettings.getClasses()) {
        for (String fieldNm : mngUvdSettings.getFieldsSettings()
          .get(clazz).keySet()) {
          String convName = mngUvdSettings.getFieldsSettings()
            .get(clazz).get(fieldNm).get("fieldFromToStringConverter");
          if (convName != null && (CnvTfsEnum.class.getSimpleName()
            .equals(convName) || CnvTfsObject.class.getSimpleName()
              .equals(convName) || convName.startsWith(CnvTfsHasId.class
                  .getSimpleName()))) {
            Field field = holderRapiFields.getFor(clazz, fieldNm);
            mngUvdSettings.getFieldsSettings().get(clazz).get(fieldNm)
              .put("fieldFromToStringConverter", convName + field.getType()
                .getSimpleName());
          }
        }
      }
      this.beansMap.put(beanName, mngUvdSettings);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return mngUvdSettings;
  }

  /**
   * <p>Getter of manager Uvd Settings service name.</p>
   * @return service name
   **/
  public final String getMngUvdSettingsName() {
    return "mngUvdSettings";
  }

  /**
   * <p>Get HolderRapiFields in lazy mode.</p>
   * @return HolderRapiFields - HolderRapiFields
   * @throws Exception - an exception
   */
  public final HolderRapiFields
    lazyGetHolderRapiFields() throws Exception {
    String beanName = getHolderRapiFieldsName();
    HolderRapiFields holderRapiFields =
      (HolderRapiFields) this.beansMap.get(beanName);
    if (holderRapiFields == null) {
      holderRapiFields = new HolderRapiFields();
      holderRapiFields.setUtlReflection(lazyGetUtlReflection());
      this.beansMap.put(beanName, holderRapiFields);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return holderRapiFields;
  }

  /**
   * <p>Getter of HolderRapiFields name.</p>
   * @return service name
   **/
  public final String getHolderRapiFieldsName() {
    return "holderRapiFields";
  }

  /**
   * <p>Get HolderRapiGetters in lazy mode.</p>
   * @return HolderRapiGetters - HolderRapiGetters
   * @throws Exception - an exception
   */
  public final HolderRapiGetters
    lazyGetHolderRapiGetters() throws Exception {
    String beanName = getHolderRapiGettersName();
    HolderRapiGetters holderRapiGetters =
      (HolderRapiGetters) this.beansMap.get(beanName);
    if (holderRapiGetters == null) {
      holderRapiGetters = new HolderRapiGetters();
      holderRapiGetters.setUtlReflection(lazyGetUtlReflection());
      this.beansMap.put(beanName, holderRapiGetters);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return holderRapiGetters;
  }

  /**
   * <p>Getter of HolderRapiGetters name.</p>
   * @return service name
   **/
  public final String getHolderRapiGettersName() {
    return "holderRapiGetters";
  }

  /**
   * <p>Get HolderRapiSetters in lazy mode.</p>
   * @return HolderRapiSetters - HolderRapiSetters
   * @throws Exception - an exception
   */
  public final HolderRapiSetters
    lazyGetHolderRapiSetters() throws Exception {
    String beanName = getHolderRapiSettersName();
    HolderRapiSetters holderRapiSetters =
      (HolderRapiSetters) this.beansMap.get(beanName);
    if (holderRapiSetters == null) {
      holderRapiSetters = new HolderRapiSetters();
      holderRapiSetters.setUtlReflection(lazyGetUtlReflection());
      this.beansMap.put(beanName, holderRapiSetters);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return holderRapiSetters;
  }

  /**
   * <p>Getter of HolderRapiSetters name.</p>
   * @return service name
   **/
  public final String getHolderRapiSettersName() {
    return "holderRapiSetters";
  }

  /**
   * <p>Get HldCnvToColumnsValuesNames in lazy mode.</p>
   * @return HldCnvToColumnsValuesNames - HldCnvToColumnsValuesNames
   * @throws Exception - an exception
   */
  public final HldCnvToColumnsValuesNames
    lazyGetHldCnvToColumnsValuesNames() throws Exception {
    String beanName = getHldCnvToColumnsValuesNamesName();
    HldCnvToColumnsValuesNames hldCnvToColumnsValuesNames =
      (HldCnvToColumnsValuesNames) this.beansMap.get(beanName);
    if (hldCnvToColumnsValuesNames == null) {
      hldCnvToColumnsValuesNames = new HldCnvToColumnsValuesNames();
      hldCnvToColumnsValuesNames.setFieldsRapiHolder(lazyGetHolderRapiFields());
      this.beansMap.put(beanName, hldCnvToColumnsValuesNames);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return hldCnvToColumnsValuesNames;
  }

  /**
   * <p>Getter of HldCnvToColumnsValuesNames name.</p>
   * @return service name
   **/
  public final String getHldCnvToColumnsValuesNamesName() {
    return "hldCnvToColumnsValuesNames";
  }

  /**
   * <p>Get FctBnCnvIbnToColumnValues in lazy mode.</p>
   * @return FctBnCnvIbnToColumnValues - FctBnCnvIbnToColumnValues
   * @throws Exception - an exception
   */
  public final FctBnCnvIbnToColumnValues
    lazyGetFctBnCnvIbnToColumnValues() throws Exception {
    String beanName = getFctBnCnvIbnToColumnValuesName();
    FctBnCnvIbnToColumnValues fctBnCnvIbnToColumnValues =
      (FctBnCnvIbnToColumnValues) this.beansMap.get(beanName);
    if (fctBnCnvIbnToColumnValues == null) {
      fctBnCnvIbnToColumnValues = new FctBnCnvIbnToColumnValues();
      fctBnCnvIbnToColumnValues.setUtlReflection(lazyGetUtlReflection());
      fctBnCnvIbnToColumnValues.setFieldsRapiHolder(lazyGetHolderRapiFields());
      fctBnCnvIbnToColumnValues
        .setGettersRapiHolder(lazyGetHolderRapiGetters());
      fctBnCnvIbnToColumnValues.setIsNeedsToSqlEscape(getIsNeedsToSqlEscape());
      fctBnCnvIbnToColumnValues.setSrvSqlEscape(lazyGetSrvSqlEscape());
      this.beansMap.put(beanName, fctBnCnvIbnToColumnValues);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fctBnCnvIbnToColumnValues;
  }

  /**
   * <p>Getter of FctBnCnvIbnToColumnValues name.</p>
   * @return service name
   **/
  public final String getFctBnCnvIbnToColumnValuesName() {
    return "fctBnCnvIbnToColumnValues";
  }

  /**
   * <p>Get FctBcCnvEntityToColumnsValues in lazy mode.</p>
   * @return FctBcCnvEntityToColumnsValues - FctBcCnvEntityToColumnsValues
   * @throws Exception - an exception
   */
  public final FctBcCnvEntityToColumnsValues
    lazyGetFctBcCnvEntityToColumnsValues() throws Exception {
    String beanName = getFctBcCnvEntityToColumnsValuesName();
    FctBcCnvEntityToColumnsValues fctBcCnvEntityToColumnsValues =
      (FctBcCnvEntityToColumnsValues) this.beansMap.get(beanName);
    if (fctBcCnvEntityToColumnsValues == null) {
      fctBcCnvEntityToColumnsValues = new FctBcCnvEntityToColumnsValues();
      fctBcCnvEntityToColumnsValues.setLogger(lazyGetLogger());
      fctBcCnvEntityToColumnsValues
        .setFieldsConvertersFatory(lazyGetFctBnCnvIbnToColumnValues());
      fctBcCnvEntityToColumnsValues
        .setFieldsConvertersNamesHolder(lazyGetHldCnvToColumnsValuesNames());
      fctBcCnvEntityToColumnsValues
        .setFieldsRapiHolder(lazyGetHolderRapiFields());
      fctBcCnvEntityToColumnsValues
        .setGettersRapiHolder(lazyGetHolderRapiGetters());
      this.beansMap.put(beanName, fctBcCnvEntityToColumnsValues);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fctBcCnvEntityToColumnsValues;
  }

  /**
   * <p>Getter of FctBcCnvEntityToColumnsValues name.</p>
   * @return service name
   **/
  public final String getFctBcCnvEntityToColumnsValuesName() {
    return "fctBcCnvEntityToColumnsValues";
  }

  /**
   * <p>Get FctFillersObjectFields in lazy mode.</p>
   * @return FctFillersObjectFields - FctFillersObjectFields
   * @throws Exception - an exception
   */
  public final FctFillersObjectFields
    lazyGetFctFillersObjectFields() throws Exception {
    String beanName = getFctFillersObjectFieldsName();
    FctFillersObjectFields fctFillersObjectFields =
      (FctFillersObjectFields) this.beansMap.get(beanName);
    if (fctFillersObjectFields == null) {
      fctFillersObjectFields = new FctFillersObjectFields();
      fctFillersObjectFields.setUtlReflection(lazyGetUtlReflection());
      fctFillersObjectFields
        .setSettersRapiHolder(lazyGetHolderRapiSetters());
      this.beansMap.put(beanName, fctFillersObjectFields);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fctFillersObjectFields;
  }

  /**
   * <p>Getter of FctFillersObjectFields name.</p>
   * @return service name
   **/
  public final String getFctFillersObjectFieldsName() {
    return "fctFillersObjectFields";
  }

  /**
   * <p>Get FctConvertersToFromString in lazy mode.</p>
   * @return FctConvertersToFromString - FctConvertersToFromString
   * @throws Exception - an exception
   */
  public final FctConvertersToFromString
    lazyGetFctConvertersToFromString() throws Exception {
    String beanName = getFctConvertersToFromStringName();
    FctConvertersToFromString fctConvertersToFromString =
      (FctConvertersToFromString) this.beansMap.get(beanName);
    if (fctConvertersToFromString == null) {
      fctConvertersToFromString = new FctConvertersToFromString();
      fctConvertersToFromString.setUtlReflection(lazyGetUtlReflection());
      fctConvertersToFromString
        .setFieldsRapiHolder(lazyGetHolderRapiFields());
      fctConvertersToFromString
        .setGettersRapiHolder(lazyGetHolderRapiGetters());
      fctConvertersToFromString
        .setSettersRapiHolder(lazyGetHolderRapiSetters());
      HldFieldsSettings hldFieldsCnvTfsNames = lazyGetHldFieldsCnvTfsNames();
      fctConvertersToFromString
        .setFieldConverterNamesHolder(hldFieldsCnvTfsNames);
      HolderRapiFields holderRapiFields = lazyGetHolderRapiFields();
      ASrvOrm<RS> srvOrm = lazyGetSrvOrm();
      // prepare for setting fieldFromToStringConverter
      // according Specification Beige-WEB interface version #2:
      for (Class<?> clazz : hldFieldsCnvTfsNames
        .getMngSettings().getClasses()) {
        for (String fieldNm : hldFieldsCnvTfsNames.getMngSettings()
          .getFieldsSettings().get(clazz).keySet()) {
          String convName = hldFieldsCnvTfsNames.getMngSettings()
            .getFieldsSettings().get(clazz).get(fieldNm)
              .get("fieldFromToStringConverter");
          if (convName.startsWith(CnvTfsEnum.class.getSimpleName())
              || convName.startsWith(CnvTfsObject.class.getSimpleName())
                || convName.startsWith(CnvTfsHasId
                  .class.getSimpleName())) {
            Field field = holderRapiFields.getFor(clazz, fieldNm);
            Class ngClass = field.getType();
            if (convName.startsWith(CnvTfsEnum.class.getSimpleName())) {
              fctConvertersToFromString
                .getEnumsClasses().add(ngClass);
            } else if (convName.startsWith(CnvTfsObject.class
              .getSimpleName())) {
              fctConvertersToFromString
                .getCompositeClasses().add(field.getType());
            } else if (convName.startsWith(CnvTfsHasId
                    .class.getSimpleName())) {
              TableSql tableSql = srvOrm.getTablesMap()
                .get(field.getType().getSimpleName());
              if (convName.startsWith(CnvTfsHasId
                    .class.getSimpleName() + "Long")) {
                fctConvertersToFromString.getHasLongIdMap()
                  .put(ngClass, tableSql.getIdFieldName());
              } else if (convName.startsWith(CnvTfsHasId
                    .class.getSimpleName() + "Integer")) {
                fctConvertersToFromString.getHasIntegerIdMap()
                  .put(ngClass, tableSql.getIdFieldName());
              } else if (convName.startsWith(CnvTfsHasId
                    .class.getSimpleName() + "String")) {
                fctConvertersToFromString.getHasStringIdMap()
                  .put(ngClass, tableSql.getIdFieldName());
              } else if (convName.startsWith(CnvTfsHasId
                    .class.getSimpleName() + "Composite")) {
                fctConvertersToFromString.getHasCompositeIdMap()
                  .put(ngClass, tableSql.getIdFieldName());
              }
            }
          }
        }
      }
      this.beansMap.put(beanName, fctConvertersToFromString);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fctConvertersToFromString;
  }

  /**
   * <p>Getter of FctConvertersToFromString name.</p>
   * @return service name
   **/
  public final String getFctConvertersToFromStringName() {
    return "fctConvertersToFromString";
  }

  /**
   * <p>Get FctBnCnvBnFromRs in lazy mode.</p>
   * @return FctBnCnvBnFromRs - FctBnCnvBnFromRs
   * @throws Exception - an exception
   */
  public final FctBnCnvBnFromRs<RS>
    lazyGetFctBnCnvBnFromRs() throws Exception {
    String beanName = getFctBnCnvBnFromRsName();
    @SuppressWarnings("unchecked")
    FctBnCnvBnFromRs<RS> fctBnCnvBnFromRs =
      (FctBnCnvBnFromRs<RS>) this.beansMap.get(beanName);
    if (fctBnCnvBnFromRs == null) {
      fctBnCnvBnFromRs = new FctBnCnvBnFromRs<RS>();
      fctBnCnvBnFromRs.setEntitiesFactoriesFatory(this.factoryBldServices
        .lazyGetFctBcFctSimpleEntities());
      fctBnCnvBnFromRs
        .setFillersFieldsFactory(lazyGetFctFillersObjectFields());
      fctBnCnvBnFromRs.setFieldsRapiHolder(lazyGetHolderRapiFields());
      this.beansMap.put(beanName, fctBnCnvBnFromRs);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fctBnCnvBnFromRs;
  }

  /**
   * <p>Getter of FctBnCnvBnFromRs name.</p>
   * @return service name
   **/
  public final String getFctBnCnvBnFromRsName() {
    return "fctBnCnvBnFromRs";
  }

  /**
   * <p>Get FillerEntitiesFromRs in lazy mode.</p>
   * @return FillerEntitiesFromRs - FillerEntitiesFromRs
   * @throws Exception - an exception
   */
  public final FillerEntitiesFromRs<RS>
    lazyGetFillerEntitiesFromRs() throws Exception {
    String beanName = getFillerEntitiesFromRsName();
    @SuppressWarnings("unchecked")
    FillerEntitiesFromRs<RS> fillerEntitiesFromRs =
      (FillerEntitiesFromRs<RS>) this.beansMap.get(beanName);
    if (fillerEntitiesFromRs == null) {
      fillerEntitiesFromRs = new FillerEntitiesFromRs<RS>();
      fillerEntitiesFromRs.setLogger(lazyGetLogger());
      fillerEntitiesFromRs
        .setFillersFieldsFactory(lazyGetFctFillersObjectFields());
      FctBnCnvBnFromRs<RS> fctBnCnvBnFromRs = lazyGetFctBnCnvBnFromRs();
      fillerEntitiesFromRs
        .setConvertersFieldsFatory(fctBnCnvBnFromRs);
      fctBnCnvBnFromRs.setFillerObjectsFromRs(fillerEntitiesFromRs);
      fillerEntitiesFromRs
        .setFieldConverterNamesHolder(lazyGetHldCnvFromRsNames());
      fillerEntitiesFromRs.setFieldsRapiHolder(lazyGetHolderRapiFields());
      this.beansMap.put(beanName, fillerEntitiesFromRs);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fillerEntitiesFromRs;
  }

  /**
   * <p>Getter of FillerEntitiesFromRs name.</p>
   * @return service name
   **/
  public final String getFillerEntitiesFromRsName() {
    return "fillerEntitiesFromRs";
  }

  /**
   * <p>Get HldCnvFromRsNames in lazy mode.</p>
   * @return HldCnvFromRsNames - HldCnvFromRsNames
   * @throws Exception - an exception
   */
  public final HldCnvFromRsNames
    lazyGetHldCnvFromRsNames() throws Exception {
    String beanName = getHldCnvFromRsNamesName();
    HldCnvFromRsNames hldCnvFromRsNames =
      (HldCnvFromRsNames) this.beansMap.get(beanName);
    if (hldCnvFromRsNames == null) {
      hldCnvFromRsNames = new HldCnvFromRsNames();
      hldCnvFromRsNames.setFieldsRapiHolder(lazyGetHolderRapiFields());
      this.beansMap.put(beanName, hldCnvFromRsNames);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return hldCnvFromRsNames;
  }

  /**
   * <p>Getter of HldCnvFromRsNames name.</p>
   * @return service name
   **/
  public final String getHldCnvFromRsNamesName() {
    return "hldCnvFromRsNames";
  }

  /**
   * <p>Getter of FctBcFctSimpleEntities name.</p>
   * @return service name
   **/
  public final String getFctBcFctSimpleEntitiesName() {
    return "fctBcFctSimpleEntities";
  }

  /**
   * <p>Get HldFieldsSettings in lazy mode.</p>
   * @return HldFieldsSettings - HldFieldsSettings
   * @throws Exception - an exception
   */
  public final HldFieldsSettings
    lazyGetHldFieldsCnvTfsNames() throws Exception {
    String beanName = getHldFieldsCnvTfsNamesName();
    HldFieldsSettings hldFieldsCnvTfsNames =
      (HldFieldsSettings) this.beansMap.get(beanName);
    if (hldFieldsCnvTfsNames == null) {
      hldFieldsCnvTfsNames =
        new HldFieldsSettings("fieldFromToStringConverter");
      hldFieldsCnvTfsNames.setMngSettings(lazyGetMngUvdSettings());
      this.beansMap.put(beanName, hldFieldsCnvTfsNames);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return hldFieldsCnvTfsNames;
  }

  /**
   * <p>Getter of HldFieldsSettings name.</p>
   * @return service name
   **/
  public final String getHldFieldsCnvTfsNamesName() {
    return "hldFieldsCnvTfsNames";
  }

  /**
   * <p>Get FillEntityFromReq in lazy mode.</p>
   * @return FillEntityFromReq - FillEntityFromReq
   * @throws Exception - an exception
   */
  public final FillEntityFromReq
    lazyGetFillEntityFromReq() throws Exception {
    String beanName = getFillEntityFromReqName();
    FillEntityFromReq fillEntityFromReq =
      (FillEntityFromReq) this.beansMap.get(beanName);
    if (fillEntityFromReq == null) {
      fillEntityFromReq = new FillEntityFromReq();
      fillEntityFromReq.setLogger(lazyGetLogger());
      fillEntityFromReq
        .setFillersFieldsFactory(lazyGetFctFillersObjectFields());
      fillEntityFromReq
        .setFieldConverterNamesHolder(lazyGetHldFieldsCnvTfsNames());
      fillEntityFromReq
        .setConvertersFieldsFatory(lazyGetFctConvertersToFromString());
      this.beansMap.put(beanName, fillEntityFromReq);
      lazyGetLogger().info(AFactoryAppBeans.class, beanName
        + " has been created.");
    }
    return fillEntityFromReq;
  }

  /**
   * <p>Getter of Fill Entity From Request service name.</p>
   * @return service name
   **/
  public final String getFillEntityFromReqName() {
    return "IFillEntityFromReq";
  }

  //Synchronized SGS:
  /**
   * <p>Setter for any bean.</p>
   * @param pBeanName Bean Name
   * @param pBean Bean
   **/
  public final synchronized void setBean(final String pBeanName,
    final Object pBean) {
    this.beansMap.put(pBeanName, pBean);
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
   * <p>Getter for factoryOverBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final synchronized IFactoryAppBeans getFactoryOverBeans() {
    return this.factoryOverBeans;
  }

  /**
   * <p>Getter for factoryBldServices.</p>
   * @return IFactoryBldServices<RS>
   **/
  public final synchronized IFactoryBldServices<RS> getFactoryBldServices() {
    return this.factoryBldServices;
  }

  /**
   * <p>Setter for factoryBldServices.</p>
   * @param pFactoryBldServices reference
   **/
  public final synchronized void setFactoryBldServices(
    final IFactoryBldServices<RS> pFactoryBldServices) {
    this.factoryBldServices = pFactoryBldServices;
  }

  //Simple getters and setters of startup settings
  /**
   * <p>Getter for entitiesMap.</p>
   * @return Map<String, Class<?>>
   **/
  public final Map<String, Class<?>> getEntitiesMap() {
    return this.entitiesMap;
  }

  /**
   * <p>Getter for minutesOfIdle.</p>
   * @return int
   **/
  public final int getMinutesOfIdle() {
    return this.minutesOfIdle;
  }

  /**
   * <p>Setter for minutesOfIdle.</p>
   * @param pMinutesOfIdle reference
   **/
  public final void setMinutesOfIdle(final int pMinutesOfIdle) {
    this.minutesOfIdle = pMinutesOfIdle;
  }

  /**
   * <p>Getter for lastRequestTime.</p>
   * @return long
   **/
  public final long getLastRequestTime() {
    return this.lastRequestTime;
  }

  /**
   * <p>Setter for lastRequestTime.</p>
   * @param pLastRequestTime reference
   **/
  public final void setLastRequestTime(
    final long pLastRequestTime) {
    this.lastRequestTime = pLastRequestTime;
  }

  /**
   * <p>Getter for uploadDirectory.</p>
   * @return String
   **/
  public final String getUploadDirectory() {
    return this.uploadDirectory;
  }

  /**
   * <p>Setter for uploadDirectory.</p>
   * @param pUploadDirectory reference
   **/
  public final void setUploadDirectory(final String pUploadDirectory) {
    this.uploadDirectory = pUploadDirectory;
  }

  /**
   * <p>Getter for webAppPath.</p>
   * @return String
   **/
  public final String getWebAppPath() {
    return this.webAppPath;
  }

  /**
   * <p>Setter for webAppPath.</p>
   * @param pWebAppPath reference
   **/
  public final void setWebAppPath(final String pWebAppPath) {
    this.webAppPath = pWebAppPath;
  }

  /**
   * <p>Getter for isShowDebugMessages.</p>
   * @return boolean
   **/
  public final boolean getIsShowDebugMessages() {
    return this.isShowDebugMessages;
  }

  /**
   * <p>Setter for isShowDebugMessages.</p>
   * @param pIsShowDebugMessages reference
   **/
  public final void setIsShowDebugMessages(final boolean pIsShowDebugMessages) {
    this.isShowDebugMessages = pIsShowDebugMessages;
  }

  /**
   * <p>Getter for ormSettingsDir.</p>
   * @return String
   **/
  public final String getOrmSettingsDir() {
    return this.ormSettingsDir;
  }

  /**
   * <p>Setter for ormSettingsDir.</p>
   * @param pOrmSettingsDir reference
   **/
  public final void setOrmSettingsDir(final String pOrmSettingsDir) {
    this.ormSettingsDir = pOrmSettingsDir;
  }

  /**
   * <p>Getter for ormSettingsBaseFile.</p>
   * @return String
   **/
  public final String getOrmSettingsBaseFile() {
    return this.ormSettingsBaseFile;
  }

  /**
   * <p>Setter for ormSettingsBaseFile.</p>
   * @param pOrmSettingsBaseFile reference
   **/
  public final void setOrmSettingsBaseFile(final String pOrmSettingsBaseFile) {
    this.ormSettingsBaseFile = pOrmSettingsBaseFile;
  }

  /**
   * <p>Getter for uvdSettingsDir.</p>
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
   * <p>Getter for uvdSettingsBaseFile.</p>
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

  /**
   * <p>Getter for beansMap.</p>
   * @return final Map<String, Object>
   **/
  public final Map<String, Object> getBeansMap() {
    return this.beansMap;
  }

  /**
   * <p>Getter for newDatabaseId.</p>
   * @return int
   **/
  public final int getNewDatabaseId() {
    return this.newDatabaseId;
  }

  /**
   * <p>Setter for newDatabaseId.</p>
   * @param pNewDatabaseId reference
   **/
  public final void setNewDatabaseId(final int pNewDatabaseId) {
    this.newDatabaseId = pNewDatabaseId;
  }

  /**
   * <p>Getter for isReconfiguring.</p>
   * @return boolean
   **/
  public final boolean getIsReconfiguring() {
    return this.isReconfiguring;
  }

  /**
   * <p>Setter for isReconfiguring.</p>
   * @param pIsReconfiguring reference
   **/
  public final void setIsReconfiguring(final boolean pIsReconfiguring) {
    this.isReconfiguring = pIsReconfiguring;
  }
}
