package org.beigesoft.orm.service;

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
import org.beigesoft.service.ISrvEntity;

/**
 * <p>Simple business service for persistable entity.
 * It just delegate to ORM service for persist logic.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class SrvEntitySimple<T extends IHasId<?>> implements ISrvEntity<T> {

  /**
   * <p>Entity class.</p>
   **/
  private final Class<T> entityClass;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<?> srvOrm;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public SrvEntitySimple(final Class<T> pEntityClass) {
    this.entityClass = pEntityClass;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   **/
  public SrvEntitySimple(final Class<T> pEntityClass,
    final ISrvOrm<?> pSrvOrm) {
    this.entityClass = pEntityClass;
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntity(final Map<String, ?> pAddParam) throws Exception {
    T entity = this.srvOrm.createEntity(this.entityClass);
    entity.setIsNew(true);
    return entity;
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntity(final Map<String, ?> pAddParam,
    final T pEntity) throws Exception {
    return this.srvOrm.retrieveEntityById(this.entityClass, pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntityById(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    return this.srvOrm.retrieveEntityById(this.entityClass, pId);
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveCopyEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    T entity = this.srvOrm.retrieveCopyEntity(this.entityClass, pId);
    entity.setIsNew(true);
    return entity;
  }

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, ?> pAddParam, final T pEntity,
    final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      this.srvOrm.insertEntity(pEntity);
    } else {
      this.srvOrm.updateEntity(pEntity);
    }
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final T pEntity) throws Exception {
    this.srvOrm.deleteEntity(this.entityClass, pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    this.srvOrm.deleteEntity(this.entityClass, pId);
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param pAddParam additional param
   * @return list of all business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveList(
    final Map<String, ?> pAddParam) throws Exception {
    return this.srvOrm.retrieveList(this.entityClass);
  }

  /**
   * <p>Retrieve a list of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions e.g. null
   * or "where ITSDATE>21313211 order by ITSID"
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveListWithConditions(
    final Map<String, ?> pAddParam,
      final String pQueryConditions) throws Exception {
    return this.srvOrm.retrieveListWithConditions(this.entityClass,
      pQueryConditions);
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrievePage(final Map<String, ?> pAddParam,
      final Integer pFirst, final Integer pPageSize) throws Exception {
    return this.srvOrm.retrievePage(this.entityClass,
      pFirst, pPageSize);
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrievePageWithConditions(
    final Map<String, ?> pAddParam,
      final String pQueryConditions,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    return this.srvOrm.retrievePageWithConditions(this.entityClass,
      pQueryConditions, pFirst, pPageSize);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCount(
    final Map<String, ?> pAddParam) throws Exception {
    return this.srvOrm.evalRowCount(this.entityClass);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCountWhere(final Map<String, ?> pAddParam,
    final String pWhere) throws Exception {
    return this.srvOrm.evalRowCountWhere(this.entityClass, pWhere);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for entityClass.</p>
   * @return Class<T>
   **/
  public final Class<T> getEntityClass() {
    return this.entityClass;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm
   **/
  public final ISrvOrm<?> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<?> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
}
