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

import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.handler.HandlerEntityRequest;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.service.IProcessor;

/**
 * <p>Abstraction of sub-factory of business-logic dependent services.
 * It used inside main-factory.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public interface IFactoryBldServices<RS> {

  /**
   * <p>Get HandlerEntityRequest in lazy mode.</p>
   * @return HandlerEntityRequest - HandlerEntityRequest
   * @throws Exception - an exception
   */
  HandlerEntityRequest<RS>
    lazyGetHandlerEntityRequest() throws Exception;

  /**
   * <p>Get FctBcFctSimpleEntities in lazy mode.</p>
   * @return FctBcFctSimpleEntities - FctBcFctSimpleEntities
   * @throws Exception - an exception
   */
  IFactoryAppBeansByClass<IFactorySimple<?>>
    lazyGetFctBcFctSimpleEntities() throws Exception;

  /**
   * <p>Get FctBnEntitiesProcessors in lazy mode.</p>
   * @return FctBnEntitiesProcessors - FctBnEntitiesProcessors
   * @throws Exception - an exception
   */
  IFactoryAppBeansByName<IEntityProcessor>
    lazyGetFctBnEntitiesProcessors() throws Exception;


  /**
   * <p>Get FctBnProcessors in lazy mode.</p>
   * @return FctBnProcessors - FctBnProcessors
   * @throws Exception - an exception
   */
  IFactoryAppBeansByName<IProcessor>
    lazyGetFctBnProcessors() throws Exception;
}
