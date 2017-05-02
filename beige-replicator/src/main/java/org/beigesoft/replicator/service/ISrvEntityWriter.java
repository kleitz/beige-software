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
import java.io.Writer;

/**
 * <p>Service to write a replicable/persistable entity.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvEntityWriter {

  /**
   * <p>
   * Write entity into a stream (writer - file or pass it through network).
   * </p>
   * @param pAddParam additional params (e.g. exclude fields set)
   * @param pEntity object
   * @param pWriter writer
   * @throws Exception - an exception
   **/
  void write(Map<String, Object> pAddParam,
    Object pEntity, Writer pWriter) throws Exception;
}
