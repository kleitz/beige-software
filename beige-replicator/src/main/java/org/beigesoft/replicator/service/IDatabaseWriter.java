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
   * @param pAddParam additional params (e.g. where clause)
   * @param pEntityClass Entity Class
   * @param pWriter writer
   * @return entities count
   * @throws Exception - an exception
   **/
  <T> int retrieveAndWriteEntities(Map<String, Object> pAddParam,
    Class<T> pEntityClass, Writer pWriter) throws Exception;
}
