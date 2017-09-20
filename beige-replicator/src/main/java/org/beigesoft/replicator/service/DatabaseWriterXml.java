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
import java.util.List;
import java.io.Writer;

import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.persistable.DatabaseInfo;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that retrieve entities from DB and write them into stream
 * (file or network connection) as XML by given writer.
 * This is transaction service. It used for replication
 * (export or identical copy) purposes.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class DatabaseWriterXml<RS> implements IDatabaseWriter {

  /**
   * <p>Entity Writer XML.</p>
   **/
  private ISrvEntityWriter srvEntityWriter;

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
   * <p>
   * Retrieve requested entities from DB then write them into a stream
   * by given writer.
   * </p>
   * @param T Entity Class
   * @param pAddParam additional params (e.g. where and order clause)
   * @param pEntityClass Entity Class
   * @param pWriter writer
   * @return entities count
   * @throws Exception - an exception
   **/
  @Override
  public final <T> int retrieveAndWriteEntities(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass, final Writer pWriter) throws Exception {
    //e.g. "limit 20 offset 19":
    //e.g. "where (ITSID>0 and IDDATABASEBIRTH=2135) limit 20 offset 19":
    String conditions = (String) pAddParam.get("conditions");
    int requestingDatabaseVersion = Integer
      .parseInt((String) pAddParam.get("requestingDatabaseVersion"));
    int databaseVersion = this.srvDatabase.getVersionDatabase();
    List<T> entities = null;
    int entitiesCount = 0;
    DatabaseInfo di;
    if (requestingDatabaseVersion == databaseVersion) {
      try {
        this.srvDatabase.setIsAutocommit(false);
        this.srvDatabase.
          setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
        this.srvDatabase.beginTransaction();
        di = getSrvOrm()
          .retrieveEntityWithConditions(pAddParam, DatabaseInfo.class, "");
        String requestedDatabaseIdStr = (String) pAddParam
          .get("requestedDatabaseId");
        if (requestedDatabaseIdStr != null) {
          int requestedDatabaseId = Integer.parseInt(requestedDatabaseIdStr);
          if (requestedDatabaseId != di.getDatabaseId()) {
            String error = "Different requested database ID! required/is: "
                + requestedDatabaseId + "/" + di.getDatabaseId();
            this.logger.error(null, DatabaseWriterXml.class, error);
            pWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            pWriter.write("<message error=\"" + error + "\">\n");
            pWriter.write("</message>\n");
            return entitiesCount;
          }
        }
        if (conditions == null) {
          entities = getSrvOrm().retrieveList(pAddParam, pEntityClass);
        } else {
          entities = getSrvOrm()
            .retrieveListWithConditions(pAddParam, pEntityClass, conditions);
        }
        entitiesCount = entities.size();
        this.srvDatabase.commitTransaction();
      } catch (Exception ex) {
        this.srvDatabase.rollBackTransaction();
        throw ex;
      } finally {
        this.srvDatabase.releaseResources();
      }
      this.logger.info(null, DatabaseWriterXml.class, "Start write entities of "
        + pEntityClass.getCanonicalName() + " count=" + entitiesCount);
      pWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      pWriter.write("<message databaseId=\"" + di.getDatabaseId()
        + "\" databaseVersion=\"" + di.getDatabaseVersion()
          + "\" description=\"" + di.getDescription() + "\" entitiesCount=\""
            + entitiesCount + "\">\n");
      for (T entity : entities) {
        this.srvEntityWriter.write(pAddParam, entity, pWriter);
      }
      pWriter.write("</message>\n");
      this.logger.info(null, DatabaseWriterXml.class,
        "Entities has been wrote");
    } else {
      this.logger.error(null, DatabaseWriterXml.class,
        "Send error message - Different database version!");
      pWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      pWriter.write("<message error=\"Different database version!\">\n");
      pWriter.write("</message>\n");
    }
    return entitiesCount;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvEntityWriter.</p>
   * @return ISrvEntityWriter
   **/
  public final ISrvEntityWriter getSrvEntityWriter() {
    return this.srvEntityWriter;
  }

  /**
   * <p>Setter for srvEntityWriter.</p>
   * @param pSrvEntityWriter reference
   **/
  public final void setSrvEntityWriter(
    final ISrvEntityWriter pSrvEntityWriter) {
    this.srvEntityWriter = pSrvEntityWriter;
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
}
