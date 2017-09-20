package org.beigesoft.replicator.service;

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
