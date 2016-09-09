package org.beigesoft.accounting.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.service.ISrvEntity;
import org.beigesoft.accounting.persistable.AccSettings;

/**
 * <p>Accounting settings service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvAccSettings extends ISrvEntity<AccSettings> {

  /**
   * <p>Retrieve/get Accounting settings.</p>
   * @return Accounting settings
   * @throws Exception - an exception
   **/
  AccSettings lazyGetAccSettings() throws Exception;

  /**
   * <p>Clear Accounting settings to retrieve from
   * database new version.</p>
   **/
  void clearAccSettings();
}
