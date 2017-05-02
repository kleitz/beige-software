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

/**
 * <p>Simple non-transactional request handler.
 * It delegate request to processor that should handle transaction management
 * if it's need.</p>
 *
 * @author Yury Demidenko
 */
public class SimpleRequestHandler implements IHandlerRequest {

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
    String processorName = pRequestData.getParameter("nmPrc");
    Hashtable<String, Object> addParam = new Hashtable<String, Object>();
    IProcessor proc = this.processorsFactory.lazyGet(addParam, processorName);
    proc.process(addParam, pRequestData);
  }

  //Simple getters and setters:
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
