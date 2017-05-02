package org.beigesoft.replicator.service;

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
 * <p>Service to fill a field of replicable/persistable entity.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvEntityFieldFiller {

  /**
   * <p>
   * Fill given field of given entity according value represented as
   * string.
   * </p>
   * @param pAddParam additional params
   * @param pEntity Entity.
   * @param pFieldName Field Name
   * @param pFieldStrValue Field value
   * @throws Exception - an exception
   **/
  void fill(Map<String, Object> pAddParam,
    Object pEntity, String pFieldName, String pFieldStrValue) throws Exception;
}
