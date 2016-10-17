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
 * <p>Service that retrieve entities from DB and write them into stream
 * (file or network connection) by given writer in XML/Json format.
 * It used for replication (export or identical copy) purposes.</p>
 *
 * @author Yury Demidenko
 */
public interface IDatabaseWriter {

  /**
   * <p>
   * Retrieve requested entities from DB then write them into a stream
   * by given writer.
   * </p>
   * @param <T> Entity Class
   * @param pEntityClass Entity Class
   * @param pWriter writer
   * @param pAddParam additional params (e.g. where clause)
   * @return entities count
   * @throws Exception - an exception
   **/
  <T> int retrieveAndWriteEntities(Class<T> pEntityClass, Writer pWriter,
    Map<String, Object> pAddParam) throws Exception;
}
