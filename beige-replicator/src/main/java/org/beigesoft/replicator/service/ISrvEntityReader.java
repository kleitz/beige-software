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
