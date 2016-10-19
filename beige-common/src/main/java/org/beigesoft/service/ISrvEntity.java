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

import java.util.List;
import java.util.Map;

import org.beigesoft.model.IHasId;

/**
 * <p>Abstraction of business service for persistable entity.
 * It usually delegate to ORM service for persist logic.
 * It additionally has security and other business logic.
 * It can has transaction logic.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public interface ISrvEntity<T extends IHasId<?>> {

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  T createEntity(Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  T retrieveEntity(Map<String, Object> pAddParam, T pEntity) throws Exception;

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  T retrieveEntityById(Map<String, Object> pAddParam,
    Object pId) throws Exception;

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  T retrieveCopyEntity(Map<String, Object> pAddParam,
    Object pId) throws Exception;

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached for standard ORM only
   * @throws Exception - an exception
   **/
  void saveEntity(Map<String, Object> pAddParam, T pEntity,
    boolean isEntityDetached) throws Exception;

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  void deleteEntity(Map<String, Object> pAddParam, T pEntity) throws Exception;

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  void deleteEntity(Map<String, Object> pAddParam,
    Object pId) throws Exception;

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param pAddParam additional param
   * @return list of all business objects
   * @throws Exception - an exception
   */
  List<T> retrieveList(Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Retrieve a list of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects
   * @throws Exception - an exception
   */
  List<T> retrieveListWithConditions(Map<String, Object> pAddParam,
    String pQueryConditions) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  List<T> retrievePage(Map<String, Object> pAddParam,
      Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  List<T> retrievePageWithConditions(Map<String, Object> pAddParam,
    String pQueryConditions,
      Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @return Integer row count
   * @throws Exception - an exception
   */
  Integer evalRowCount(Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  Integer evalRowCountWhere(Map<String, Object> pAddParam,
    String pWhere) throws Exception;
}
