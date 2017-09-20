package org.beigesoft.service;

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
