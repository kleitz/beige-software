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
import java.io.Reader;

/**
 * <p>Service to read a replicable/persistable entity.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvEntityReader {

  /**
   * <p>
   * Read entity(fill fields) from a stream (reader - file or through network).
   * It is invoked when it's start of &lt;entity
   * </p>
   * @param pAddParam additional params
   * @param pReader reader.
   * @return entity filled/refreshed.
   * @throws Exception - an exception
   **/
  Object read(Map<String, Object> pAddParam, Reader pReader) throws Exception;

  /**
   * <p>
   * Read entity attributes from stream.
   * </p>
   * @param pAddParam additional params
   * @param pReader reader.
   * @return attributes map
   * @throws Exception - an exception
   **/
  Map<String, String> readAttributes(Map<String, Object> pAddParam,
    Reader pReader) throws Exception;
}
