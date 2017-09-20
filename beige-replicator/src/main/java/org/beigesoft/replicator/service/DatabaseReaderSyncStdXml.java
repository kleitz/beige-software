package org.beigesoft.replicator.service;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.io.Reader;

import org.beigesoft.settings.IMngSettings;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.IUtilXml;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that reads entities from given stream XML
 * (file or network connection) and insert them into database.
 * It makes database synchronized copy i.e. if entity is APersistableBase
 * (itsId, databaseBirth, idBirth) then it be synchronized by
 * {idBirth and idDatabaseBirth}. Other by explicit ID (Long or String type).
 * This is transactional service!
 * This is only for entities of types:
 * APersistableBase, AHasIdLong and AHasIdString
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class DatabaseReaderSyncStdXml<RS> implements IDatabaseReader {

  /**
   * <p>Entity Reader.</p>
   **/
  private ISrvEntityReader srvEntityReader;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>XML service.</p>
   **/
  private IUtilXml utilXml;

  /**
   * <p>Manager Settings.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Services Entity Sync map.</p>
   **/
  private Map<String, ISrvEntitySync> srvEntitySyncMap;

  /**
   * <p>
   * Read entities from stream (by given reader) and synchronize them
   * into DB.
   * </p>
   * @param pAddParam additional params
   * @param pReader Reader
   * @throws Exception - an exception
   **/
  @Override
  public final void readAndStoreEntities(final Map<String, Object> pAddParam,
    final Reader pReader) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      while (this.utilXml.readUntilStart(pReader, "entity")) {
        Object entity = this.srvEntityReader.read(pAddParam, pReader);
        String nameEntitySync = this.mngSettings.getClassesSettings()
          .get(entity.getClass()).get("ISrvEntitySync");
        ISrvEntitySync srvEntitySync = this.srvEntitySyncMap
          .get(nameEntitySync);
        if (srvEntitySync == null) {
          throw new ExceptionWithCode(ExceptionWithCode
            .CONFIGURATION_MISTAKE, "There is no ISrvEntitySync "
              + nameEntitySync + " for " + entity.getClass());
        }
        boolean isNew = srvEntitySync.sync(pAddParam, entity);
        if (isNew) {
          this.srvOrm.insertEntity(pAddParam, entity);
        } else {
          this.srvOrm.updateEntity(pAddParam, entity);
        }
      }
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for mngSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngSettings() {
    return this.mngSettings;
  }

  /**
   * <p>Setter for mngSettings.</p>
   * @param pMngSettings reference
   **/
  public final void setMngSettings(final IMngSettings pMngSettings) {
    this.mngSettings = pMngSettings;
  }

  /**
   * <p>Getter for srvEntityReader.</p>
   * @return ISrvEntityReader
   **/
  public final ISrvEntityReader getSrvEntityReader() {
    return this.srvEntityReader;
  }

  /**
   * <p>Setter for srvEntityReader.</p>
   * @param pSrvEntityReader reference
   **/
  public final void setSrvEntityReader(
    final ISrvEntityReader pSrvEntityReader) {
    this.srvEntityReader = pSrvEntityReader;
  }

  /**
   * <p>Getter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Getter for utilXml.</p>
   * @return IUtilXml
   **/
  public final IUtilXml getUtilXml() {
    return this.utilXml;
  }

  /**
   * <p>Setter for utilXml.</p>
   * @param pUtilXml reference
   **/
  public final void setUtilXml(final IUtilXml pUtilXml) {
    this.utilXml = pUtilXml;
  }

  /**
   * <p>Getter for srvEntitySyncMap.</p>
   * @return Map<String, ISrvEntitySync>
   **/
  public final Map<String, ISrvEntitySync> getSrvEntitySyncMap() {
    return this.srvEntitySyncMap;
  }

  /**
   * <p>Setter for srvEntitySyncMap.</p>
   * @param pSrvEntitySyncMap reference
   **/
  public final void setSrvEntitySyncMap(
    final Map<String, ISrvEntitySync> pSrvEntitySyncMap) {
    this.srvEntitySyncMap = pSrvEntitySyncMap;
  }
}
