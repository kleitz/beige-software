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
 * <p>Service to write a field of replicable/persistable entity.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvFieldWriter {

  /**
   * <p>
   * Write field of entity into a stream
   * (writer - file or pass it through network).
   * </p>
   * @param pAddParam additional params (e.g. exclude fields set)
   * @param pField value
   * @param pFieldName Field Name
   * @param pWriter writer
   * @throws Exception - an exception
   **/
  void write(Map<String, Object> pAddParam,
    Object pField, String pFieldName, Writer pWriter) throws Exception;
}
