package org.beigesoft.replicator.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.io.Reader;

import org.beigesoft.service.IUtilXml;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that reads entities from given stream XML
 * (file or network connection) and insert them into database.
 * It makes database identical copy i.e. it does not change any
 * field (itsId, databaseBirth, idBirth).
 * This is transactional service! It just insert entities,
 * so you have to delete all records (empty database) before make copy.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class DatabaseReaderIdenticalXml<RS> implements IDatabaseReader {

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
   * <p>
   * Read entities from stream (by given reader) and insert them
   * into DB with no changes. DB must be emptied before coping.
   * </p>
   * @param pReader Reader
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void readAndStoreEntities(final Reader pReader,
    final Map<String, ?> pAddParam) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      while (this.utilXml.readUntilStart(pReader, "entity")) {
        Object entity = this.srvEntityReader.read(pReader, null);
        this.srvOrm.insertEntity(entity);
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
}
