package org.beigesoft.web.factory;

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

import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.handler.HandlerEntityRequest;
import org.beigesoft.handler.HndlEntityFileReportReq;
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
   * <p>Get HndlEntityFileReportReq in lazy mode.</p>
   * @return HndlEntityFileReportReq - HndlEntityFileReportReq
   * @throws Exception - an exception
   */
  HndlEntityFileReportReq<RS>
    lazyGetHndlEntityFileReportReq() throws Exception;

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
