package org.beigesoft.service;

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
import java.io.OutputStream;

import org.beigesoft.model.IRequestData;
import org.beigesoft.model.IHasId;

/**
 * <p>Abstraction of service that makes file-report into stream
 * for given entity.</p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public interface IEntityFileReporter<T extends IHasId<ID>, ID> {

  /**
   * <p>Makes file-report into stream for given entity.</p>
   * @param pAddParam additional param
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @param pSous servlet output stream
   * @throws Exception - an exception
   **/
   void makeReport(Map<String, Object> pAddParam,
    T pEntity, IRequestData pRequestData, OutputStream pSous) throws Exception;
}
