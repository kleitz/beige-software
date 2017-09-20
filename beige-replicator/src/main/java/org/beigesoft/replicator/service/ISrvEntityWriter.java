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
