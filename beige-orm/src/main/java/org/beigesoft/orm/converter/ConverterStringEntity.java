package org.beigesoft.orm.converter;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.converter.IConverterToEntity;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Converter from String to Entity value that has ID of type Long.</p>
 *
 * @param <T> type of Entity
 * @author Yury Demidenko
 */
public class ConverterStringEntity<T>
  implements IConverterToEntity<T> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<?> srvOrm;

  /**
   * <p>Convert String to Entity value.</p>
   * @param pItsIdStr ID string value
   * @param pClazz Entity class
   * @return Entity value
   * @throws Exception an exception
   **/
  @Override
  public final T convert(final String pItsIdStr,
    final Class<T> pClazz) throws Exception {
    return srvOrm.retrieveEntityById(pClazz, pItsIdStr);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<?>
   **/
  public final ISrvOrm<?> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<?> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
}
