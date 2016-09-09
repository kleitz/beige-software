package org.beigesoft.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <p>Abstraction of I18N service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvI18n {

  /**
   * <p>Evaluate message for current locale.</p>
   * @param key of message
   * @return String internationalized message
   **/
  String getMsg(String key);
}
