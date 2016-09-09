package org.beigesoft.factory;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.service.SrvI18n;

/**
 * <pre>
 * Factory of service I18N of app scope. Client don't care about scope.
 * </pre>
 *
 * @author Yury Demidenko
 **/
public class FactorySrvI18nVagAppScoped
  implements IFactoryVagueScoped<ISrvI18n> {

  /**
   * <p>service I18N.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Return (create if it's null) service I18N of app scope.</p>
   **/
  @Override
  public final ISrvI18n createOrGet() {
    if (this.srvI18n == null) {
      this.srvI18n = new SrvI18n();
    }
    return this.srvI18n;
  }
}
