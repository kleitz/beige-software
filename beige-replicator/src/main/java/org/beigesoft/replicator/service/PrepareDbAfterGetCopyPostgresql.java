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
import java.util.Set;
import java.util.Date;
import java.io.Writer;

import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.delegate.IDelegator;
import org.beigesoft.persistable.APersistableBase;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that reset auto-incremented ID sequences (APersistableBase)
 * after identical copy of another database.
 * Also it release AppFactory beans.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrepareDbAfterGetCopyPostgresql<RS>
  implements IDelegator {

  /**
   * <p>Factory App-Beans.</p>
   **/
  private IFactoryAppBeans factoryAppBeans;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Persistable classes set.</p>
   **/
  private Set<Class<?>> classes;

  /**
   * <p>It prepares database after import.</p>
   * @param pAddParams additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void make(
    final Map<String, Object> pAddParams) throws Exception {
    int preparedEntitiesCount = 0;
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      for (Class<?> entityClass : this.classes) {
        if (APersistableBase.class.isAssignableFrom(entityClass)) {
          preparedEntitiesCount++;
          String queryMaxId = "select max(ITSID) as MAXID from "
            + entityClass.getSimpleName().toUpperCase() + ";";
          Integer maxId = this.srvDatabase
            .evalIntegerResult(queryMaxId, "MAXID");
          if (maxId != null) {
            maxId++;
            String querySec = "alter sequence " + entityClass.getSimpleName()
              .toUpperCase() + "_ITSID_SEQ restart with " + maxId + ";";
            this.srvDatabase.executeQuery(querySec);
          }
        }
      }
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
    this.factoryAppBeans.releaseBeans();
    Writer htmlWriter = (Writer) pAddParams.get("htmlWriter");
    if (htmlWriter != null) {
      htmlWriter.write("<h4>" + new Date().toString() + ", "
      + PrepareDbAfterGetCopyPostgresql.class.getSimpleName()
        + ", app-factory beans has released" + "</h4>");
    }
    this.logger.info(null, PrepareDbAfterGetCopyPostgresql.class,
      "Total sequence prepared: " + preparedEntitiesCount
        + ", app-factory beans has released");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for factoryAppBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final IFactoryAppBeans getFactoryAppBeans() {
    return this.factoryAppBeans;
  }

  /**
   * <p>Setter for factoryAppBeans.</p>
   * @param pFactoryAppBeans reference
   **/
  public final void setFactoryAppBeans(
    final IFactoryAppBeans pFactoryAppBeans) {
    this.factoryAppBeans = pFactoryAppBeans;
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
   * <p>Getter for classes.</p>
   * @return Set<Class<?>>
   **/
  public final Set<Class<?>> getClasses() {
    return this.classes;
  }

  /**
   * <p>Setter for classes.</p>
   * @param pClasses reference
   **/
  public final void setClasses(final Set<Class<?>> pClasses) {
    this.classes = pClasses;
  }
}
