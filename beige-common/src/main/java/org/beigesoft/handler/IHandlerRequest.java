package org.beigesoft.handler;

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
