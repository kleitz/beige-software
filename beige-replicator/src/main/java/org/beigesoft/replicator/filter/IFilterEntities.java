package org.beigesoft.replicator.filter;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;

/**
 * <p>Service that make SQL WHERE filter for an entity.</p>
 *
 * @author Yury Demidenko
 */
public interface IFilterEntities {

  /**
   * <p>
   * It makes SQL WHERE filter for an entity.
   * </p>
   * @param pEntityClass Entity Class
   * @param pAddParam additional params (e.g. requested database ID)
   * @return filter null or conditions without WHERE word e.g. "ITSID>12"
   * @throws Exception - an exception
   **/
  String makeFilter(Class<?> pEntityClass,
    Map<String, Object> pAddParam) throws Exception;
}
