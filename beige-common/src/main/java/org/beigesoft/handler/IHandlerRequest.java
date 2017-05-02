package org.beigesoft.handler;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.model.IRequestData;

/**
 * <p>Abstraction of business service for handle request.
 * It is usually transactional service.
 * It based on abstraction of request data (get/set param, attribute)
 * that usually wrap HttpServletRequest(parameters/attributes).</p>
 *
 * @author Yury Demidenko
 */
public interface IHandlerRequest {

  /**
   * <p>Handle request.</p>
   * @param pRequestData Request Data
   * @throws Exception - an exception
   */
  void handle(IRequestData pRequestData) throws Exception;
}
