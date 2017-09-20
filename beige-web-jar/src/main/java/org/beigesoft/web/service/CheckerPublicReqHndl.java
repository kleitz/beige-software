package org.beigesoft.web.service;

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

import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.beigesoft.delegate.IDelegateEvaluate;

/**
 * <p>It checks if request handler is public, e.g. webstore "hndTrdTrnsReq"
 * is public but standard "handlerEntityRequest" is not.</p>
 *
 * @author Yury Demidenko
 */
public class CheckerPublicReqHndl implements
  IDelegateEvaluate<HttpServletRequest, Boolean> {

  /**
   * <p>Set of public request handler names.</p>
   **/
  private final Set<String> publicHandlerNames = new HashSet<String>();

  /**
   * <p>Checks if request public.</p>
   * @param pReq request
   * @return if public
   * @throws Exception - an exception
   **/
  public final Boolean evaluate(
    final HttpServletRequest pReq) throws Exception {
    String nameHandler = pReq.getParameter("nmHnd");
    if (this.publicHandlerNames.contains(nameHandler)) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for publicHandlerNames.</p>
   * @return Set<String>
   **/
  public final Set<String> getPublicHandlerNames() {
    return this.publicHandlerNames;
  }
}
