package org.beigesoft.web.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.log.ILogger;

/**
 * <p>Software manager service.
 * At least it should manage "is show debug messages" for Logger.
 * Of course it should be able to instantiate and replace any working service
 * in application factory. It should be able to change other settings
 * (e.g. view descriptor) initialized from XML.
 * </p>
 *
 * @author Yury Demidenko
 */
public class MngSoftware implements IMngSoftware {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Set is show debug messages.</p>
   * @param pIsShowDebugMessage is show debug messages?
   * @throws Exception - an exception
   **/
  @Override
  public final void setIsShowDebugMessages(
    final boolean pIsShowDebugMessage) throws Exception {
    this.logger.setIsShowDebugMessages(pIsShowDebugMessage);
  }

  /**
   * <p>Get is show debug messages.</p>
   * @throws Exception - an exception
   * @return is show debug messages?
   **/
  @Override
  public final boolean getIsShowDebugMessages() throws Exception {
    return this.logger.getIsShowDebugMessages();
  }

  /**
   * <p>Set is show debug messages for this class.</p>
   * @param pClazz of bean
   * @param pIsShowDebugMessage is show debug messages?
   * @throws Exception - an exception
   **/
  @Override
  public final void setIsShowDebugMessages(final Class<?> pClazz,
    final boolean pIsShowDebugMessage) throws Exception {
    this.logger.setIsShowDebugMessages(pClazz, pIsShowDebugMessage);
  }

  /**
   * <p>Get is show debug messages for this class.</p>
   * @param pClazz of bean
   * @throws Exception - an exception
   * @return is show debug messages?
   **/
  @Override
  public final boolean getIsShowDebugMessages(
    final Class<?> pClazz) throws Exception {
    return this.logger.getIsShowDebugMessages(pClazz);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for logger.</p>
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
