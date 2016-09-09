package org.beigesoft.web.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import javax.servlet.http.HttpServletRequest;
import org.beigesoft.log.ILogger;

/**
 * <p>Abstraction of business (transactional) generic service for
 * any Entity (persistable).
 * Do not use it in other business (transactional) service.
 * It must return(attach) full loaded models (non-leazy).
 * It designed to be used in a servlet.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvWebEntity {

  /**
   * <p>Create entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void create(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Create entity transactional (use RDBMS).</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void createTransactional(HttpServletRequest pReq) throws Exception;

  /**
   * <p>View entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void view(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Print entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void print(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Copy entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void copy(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Edit entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void edit(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Confirm delete entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void confirmDelete(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Save entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void save(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Delete entity.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void delete(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Create entity from owned list with using RDBMS.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void createFromOwnedList(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Create entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void createFromOwnedListTransactional(
    HttpServletRequest pReq) throws Exception;

  /**
   * <p>Copy entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void copyFromOwnedList(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Edit entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void editFromOwnedList(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Confirm delete entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void confirmDeleteFromOwnedList(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Save entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void saveFromOwnedList(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Delete entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void deleteFromOwnedList(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Retrieve entity page.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  void retrievePage(HttpServletRequest pReq) throws Exception;

  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  ILogger getLogger();

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  void setLogger(ILogger pLogger);
}
