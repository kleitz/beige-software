package org.beigesoft.web.service;

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
public interface IMngSoftware {

  /**
   * <p>Set is show debug messages.</p>
   * @param pIsShowDebugMessage is show debug messages?
   * @throws Exception - an exception
   **/
  void setIsShowDebugMessages(boolean pIsShowDebugMessage) throws Exception;

  /**
   * <p>Get is show debug messages.</p>
   * @throws Exception - an exception
   * @return is show debug messages?
   **/
  boolean getIsShowDebugMessages() throws Exception;

  /**
   * <p>Set is show debug messages for this class.</p>
   * @param pClazz of bean
   * @param pIsShowDebugMessage is show debug messages?
   * @throws Exception - an exception
   **/
  void setIsShowDebugMessages(Class<?> pClazz,
    boolean pIsShowDebugMessage) throws Exception;

  /**
   * <p>Get is show debug messages for this class.</p>
   * @param pClazz of bean
   * @throws Exception - an exception
   * @return is show debug messages?
   **/
  boolean getIsShowDebugMessages(Class<?> pClazz) throws Exception;
}
