package org.beigesoft.handler;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Hashtable;

import org.beigesoft.model.IRequestData;
import org.beigesoft.service.IProcessor;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Transactional handler for any requests,
 * transaction per request method.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class TransactionalRequestHandler<RS> implements IHandlerRequest {

  /**
   * <p>Database service.</p>
   */
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Processors factory.</p>
   **/
  private IFactoryAppBeansByName<IProcessor> processorsFactory;

  /**
   * <p>Handle request.</p>
   * @param pRequestData Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(
    final IRequestData pRequestData) throws Exception {
    try {
      String processorName = pRequestData.getParameter("nmPrc");
      Hashtable<String, Object> addParam = new Hashtable<String, Object>();
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      IProcessor proc = this.processorsFactory.lazyGet(addParam, processorName);
      proc.process(addParam, pRequestData);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  //Simple getters and setters:
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
   * <p>Getter for processorsFactory.</p>
   * @return IFactoryAppBeansByName<IProcessor>
   **/
  public final IFactoryAppBeansByName<IProcessor> getProcessorsFactory() {
    return this.processorsFactory;
  }

  /**
   * <p>Setter for processorsFactory.</p>
   * @param pProcessorsFactory reference
   **/
  public final void setProcessorsFactory(
    final IFactoryAppBeansByName<IProcessor> pProcessorsFactory) {
    this.processorsFactory = pProcessorsFactory;
  }
}
