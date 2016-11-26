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

import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.io.Writer;

import org.beigesoft.log.ILogger;
import org.beigesoft.delegate.IDelegator;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Service that clear database
 * before get identical copy of another one.
 * This is transactional service.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvClearDatabase<RS> implements IDelegator {

  /**
   * <p>Replicators settings manager.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>It will clear current database.</p>
   * @param pAddParams additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void make(
    final Map<String, Object> pAddParams) throws Exception {
    ArrayList<Class<?>> classesArr =
      new ArrayList<Class<?>>(this.mngSettings.getClasses());
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      this.logger.info(SrvClearDatabase.class, "Start clear database.");
      for (int i = classesArr.size() - 1; i >= 0; i--) {
        Class<?> entityClass = classesArr.get(i);
        this.srvDatabase.executeDelete(entityClass.getSimpleName()
          .toUpperCase(), null);
      }
      this.srvDatabase.commitTransaction();
      Writer htmlWriter = (Writer) pAddParams.get("htmlWriter");
      if (htmlWriter != null) {
        htmlWriter.write("<h4>" + new Date().toString() + ", "
        + SrvClearDatabase.class.getSimpleName()
          + ", database has been cleared" + "</h4>");
      }
      this.logger.info(SrvClearDatabase.class, "Finish clear database.");
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
}
