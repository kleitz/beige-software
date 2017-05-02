package org.beigesoft.service;

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

import org.beigesoft.model.IRequestData;
import org.beigesoft.model.IHasId;

/**
 * <p>Abstraction of service that process a request for entity,
 * e.g. "save entity changes".
 * This service is dedicated to either concrete entity or set of
 * super types.</p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public interface IEntityProcessor<T extends IHasId<ID>, ID> {

  /**
   * <p>Process entity request.</p>
   * @param pAddParam additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
   T process(Map<String, Object> pAddParam,
    T pEntity, IRequestData pRequestData) throws Exception;
}
