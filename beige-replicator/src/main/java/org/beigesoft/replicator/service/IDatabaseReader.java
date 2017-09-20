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
 * <p>Service that Read entities from stream (by given reader)
 * and insert/update them into DB.</p>
 *
 * @author Yury Demidenko
 */
public interface IDatabaseReader {

  /**
   * <p>
   * Read entities from stream (by given reader) and insert/update them
   * into DB.
   * </p>
   * @param pAddParam additional params
   * @param pReader Reader
   * @throws Exception - an exception
   **/
  void readAndStoreEntities(Map<String, Object> pAddParam,
    Reader pReader) throws Exception;
}
