package org.beigesoft.replicator.filter;

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
